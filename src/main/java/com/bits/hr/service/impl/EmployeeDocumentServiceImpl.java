package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeDocument;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeDocumentRepository;
import com.bits.hr.service.EmployeeDocumentService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDocumentDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.SubDirectoryService;
import com.bits.hr.service.fileOperations.pathBuilder.PathBuilderService;
import com.bits.hr.service.fileOperations.pathBuilder.PathCategory;
import com.bits.hr.service.mapper.EmployeeDocumentMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.mail.Multipart;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class EmployeeDocumentServiceImpl implements EmployeeDocumentService {

    private final Logger log = LoggerFactory.getLogger(EmployeeDocumentServiceImpl.class);

    private final EmployeeDocumentRepository employeeDocumentRepository;

    private final EmployeeDocumentMapper employeeDocumentMapper;

    private final EmployeeService employeeService;

    private final CurrentEmployeeService currentEmployeeService;

    private final FileOperationService fileOperationService;
    private final PathBuilderService pathBuilderService;
    private final SubDirectoryService subDirectoryService;

    private static final String ENTITY_NAME = "employeeDocumentEntry";

    @Value("${spring.application.sub-folder-employee-document-tmp-path}")
    private String tempFileStoragePath;

    @Override
    @Transactional
    public EmployeeDocumentDTO save(String pin, EmployeeDocumentDTO employeeDocumentDTO, MultipartFile file) {
        log.debug("Request to save EmployeeDocument : {}", employeeDocumentDTO);

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        employeeDocumentDTO.setFileExtension(extension);

//        String fileName = "file_"+ pin + "_" + employeeDocumentDTO.getFileName() + "." + extension;
        String fileName = generateEmployeeDocumentFileName(pin, extension);

        Optional<String> pathOptional = pathBuilderService.buildPath(fileName, PathCategory.EMPLOYEE_DOCUMENTS);

        if(pathOptional.isPresent()){
            boolean isExist = fileOperationService.isExist(pathOptional.get());
            if(isExist){
                pathOptional = pathBuilderService.buildPath(fileName, PathCategory.EMPLOYEE_DOCUMENTS);
            }
        }

        if (pathOptional.isPresent()) {
            if(fileOperationService.save(file, pathOptional.get()).isEmpty()){
                throw new BadRequestAlertException("Desired file can't be saved", ENTITY_NAME, "filePathNull");
            }
            if (employeeDocumentDTO.getId() != null) {
                String filePath = employeeDocumentRepository.findById(employeeDocumentDTO.getId()).get().getFilePath();
                fileOperationService.delete(filePath);
            }
            employeeDocumentDTO.setFilePath(pathOptional.get());
            EmployeeDocument employeeDocument = employeeDocumentMapper.toEntity(employeeDocumentDTO);
            employeeDocument = employeeDocumentRepository.save(employeeDocument);
            return employeeDocumentMapper.toDto(employeeDocument);
        } else {
            throw new BadRequestAlertException("Desired file can't be saved", ENTITY_NAME, "filePathNull");
        }
    }

    @Override
    public EmployeeDocumentDTO update(String pin, EmployeeDocumentDTO employeeDocumentDTO) {
        log.debug("Request to update EmployeeDocument : {}", employeeDocumentDTO);
        Optional<EmployeeDocument> existing = employeeDocumentRepository.findById(employeeDocumentDTO.getId());
        if(existing.isEmpty()){
            throw new BadRequestAlertException("EmployeeId cannot be null " ,ENTITY_NAME , " idNull");
        }

        employeeDocumentDTO.setFileExtension(existing.get().getFileExtension());
        EmployeeDocument employeeDocument = employeeDocumentMapper.toEntity(employeeDocumentDTO);
        employeeDocument = employeeDocumentRepository.save(employeeDocument);
        return employeeDocumentMapper.toDto(employeeDocument);
    }

    @Override
    public Optional<EmployeeDocumentDTO> partialUpdate(EmployeeDocumentDTO employeeDocumentDTO) {
        log.debug("Request to partially update EmployeeDocument : {}", employeeDocumentDTO);

        return employeeDocumentRepository
            .findById(employeeDocumentDTO.getId())
            .map(existingEmployeeDocument -> {
                employeeDocumentMapper.partialUpdate(existingEmployeeDocument, employeeDocumentDTO);

                return existingEmployeeDocument;
            })
            .map(employeeDocumentRepository::save)
            .map(employeeDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDocumentDTO> findAll( Pageable pageable) {
        log.debug("Request to get all EmployeeDocument");
        Page<EmployeeDocument> res = employeeDocumentRepository.findAll(pageable);
        return res.map(employeeDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDocumentDTO> getAllByPinOrderByFileName(String pin, Pageable pageable){
        log.debug("Request to get all EmployeeDocument by PinOrderByFileName");
        Page<EmployeeDocument> res = employeeDocumentRepository.getAllByPinOrderByFileName(pin, pageable);
        return res.map(employeeDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDocumentDTO> findOne(Long id) {
        log.debug("Request to get EmployeeDocument : {}", id);
        return employeeDocumentRepository.findById(id)
            .map(employeeDocumentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeDocument : {}", id);
        Optional<EmployeeDocument> employeeDocument = employeeDocumentRepository.findById(id);
        if(employeeDocument.isPresent()){
            fileOperationService.delete(employeeDocument.get().getFilePath());
            employeeDocumentRepository.deleteById(id);
        }
    }

    public boolean existsByFileNameAndPin(String fileName, String pin){
        return employeeDocumentRepository.existsByFileNameAndPin(fileName, pin);
    }

    @Override
    public boolean uploadAndExtractZip(MultipartFile file) {
        String destination = tempFileStoragePath;
        try{
            Files.createDirectories(Paths.get(destination));

            ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            byte[] buffer = new byte[1024];

            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                String filePath = destination + File.separator + entryName;

                // If the entry is a directory, create the directory.
                if (zipEntry.isDirectory()) {
                    File dir = new File(filePath);
                    dir.mkdirs();
                } else {
                    // If the entry is a file, extract it.
                    try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, len);
//                            fileOutputStream.close();
                        }
                    }
                    catch (ZipException | FileNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Move to the next entry in the zip file
                zipEntry = zipInputStream.getNextEntry();
            }
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean uploadAndProcess(MultipartFile file) {
        String destination = tempFileStoragePath;
        Map<String, List<String>> resultMap = new HashMap<>();

        // read and move
        try{
            InputStream fileContent = file.getInputStream();

            Workbook workbook = new XSSFWorkbook(fileContent);

            // Assuming the first sheet is your target sheet
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip the header row

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String pin = String.valueOf(row.getCell(0).getStringCellValue()).trim();
                String fileName = row.getCell(1).getStringCellValue().trim();

                if(!moveFileAndPopulateDb(pin, fileName, destination)){
                    throw new BadRequestAlertException("Failed to process batch employee documents upload!", ENTITY_NAME, "batchProcessEmployeeDocuments");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    boolean moveFileAndPopulateDb(String pin, String fileName, String sourcePath) {
        File file = new File(sourcePath + File.separator + fileName);

        if(!file.exists() && file.isDirectory()){
            throw new BadRequestAlertException("Invalid file name", ENTITY_NAME, "invalidFileName");
        }

        Optional<Employee> employee = employeeService.findEmployeeByPin(pin);
        if(!employee.isPresent()){
            throw new BadRequestAlertException("Failed to find employee", ENTITY_NAME, "pinNotFound");
        }

        String encFileName = generateEmployeeDocumentFileName(pin, StringUtils.getFilenameExtension(fileName));

        String destinationFilePath = pathBuilderService.buildPath(encFileName, PathCategory.EMPLOYEE_DOCUMENTS).get();
        String destinationPath = subDirectoryService.getEmployeeDocumentDir();

        // move file to directory
        try{
            // move document form tempdir to docsDir
            Path sPath = Paths.get(sourcePath + File.separator +fileName);
            Path dPath = Paths.get(destinationPath);
            Files.move(sPath, dPath.resolve(sPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);

            // rename the document
            File newFile = new File(destinationFilePath);
            Files.move(dPath.resolve(sPath.getFileName()), newFile.toPath());
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

        // entry in database
        EmployeeDocumentDTO employeeDocumentDTO = new EmployeeDocumentDTO();
        employeeDocumentDTO.setHasEmployeeVisibility(false);
        employeeDocumentDTO.setPin(pin);
        employeeDocumentDTO.setRemarks(fileName);
        employeeDocumentDTO.setFileName(fileName);
        employeeDocumentDTO.setFileExtension(StringUtils.getFilenameExtension(fileName));
        employeeDocumentDTO.setFilePath(destinationFilePath);

        employeeDocumentDTO.setCreatedAt(Instant.now());
        employeeDocumentDTO.setCreatedBy(currentEmployeeService.getCurrentEmployee().get().getFullName());

        employeeDocumentRepository.save(employeeDocumentMapper.toEntity(employeeDocumentDTO));

        return true;
    }

    String generateEmployeeDocumentFileName(String pin, String extension){
        return  "file_" + pin + "_" + RandomStringUtils.randomAlphanumeric(8) + "." + extension;
    }
}
