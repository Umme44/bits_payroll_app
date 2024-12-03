package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeStaticFile;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeStaticFileRepository;
import com.bits.hr.service.EmployeeStaticFileService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeIdCardSummaryDTO;
import com.bits.hr.service.dto.EmployeeStaticFileDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.EmployeeIdCardService;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.service.mapper.EmployeeStaticFileMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link EmployeeStaticFile}.
 */
@Service
@Transactional
public class EmployeeStaticFileServiceImpl implements EmployeeStaticFileService {

    private final Logger log = LoggerFactory.getLogger(EmployeeStaticFileServiceImpl.class);

    private final EmployeeStaticFileRepository employeeStaticFileRepository;

    private final EmployeeStaticFileMapper employeeStaticFileMapper;

    private final EmployeeRepository employeeRepository;

    private final FileOperationService fileOperationService;

    private final EmployeeIdCardService employeeIdCardService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeMinimalMapper employeeMinimalMapper;

    public EmployeeStaticFileServiceImpl(
        EmployeeStaticFileRepository employeeStaticFileRepository,
        EmployeeStaticFileMapper employeeStaticFileMapper,
        EmployeeRepository employeeRepository,
        FileOperationService fileOperationService,
        EmployeeIdCardService employeeIdCardService
    ) {
        this.employeeStaticFileRepository = employeeStaticFileRepository;
        this.employeeStaticFileMapper = employeeStaticFileMapper;
        this.employeeRepository = employeeRepository;
        this.fileOperationService = fileOperationService;
        this.employeeIdCardService = employeeIdCardService;
    }

    @Override
    public EmployeeStaticFileDTO save(EmployeeStaticFileDTO employeeStaticFileDTO) {
        log.debug("Request to save EmployeeStaticFile : {}", employeeStaticFileDTO);
        EmployeeStaticFile employeeStaticFile = employeeStaticFileMapper.toEntity(employeeStaticFileDTO);
        employeeStaticFile = employeeStaticFileRepository.save(employeeStaticFile);
        return employeeStaticFileMapper.toDto(employeeStaticFile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeStaticFileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeStaticFiles");
        return employeeStaticFileRepository.findAll(pageable).map(employeeStaticFileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeStaticFileDTO> findOne(Long id) {
        log.debug("Request to get EmployeeStaticFile : {}", id);
        return employeeStaticFileRepository.findById(id).map(employeeStaticFileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeStaticFile : {}", id);
        employeeStaticFileRepository.deleteById(id);
    }

    @Override
    public Boolean idCardBatchSave(MultipartFile[] files) {
        try {
            boolean isSavedFile = false;
            for (MultipartFile multipartFile : files) {
                if (!Objects.requireNonNull(multipartFile.getContentType()).contains("image/")) {
                    continue;
                }

                String fileNameWithOutExtension = Objects
                    .requireNonNull(multipartFile.getOriginalFilename())
                    .substring(0, multipartFile.getOriginalFilename().indexOf("."));
                Optional<Employee> employeeOptional = employeeRepository.findByPin(fileNameWithOutExtension);
                Employee employee;

                if (employeeOptional.isPresent()) {
                    employee = employeeOptional.get();
                } else {
                    continue;
                }

                File savedFile = employeeIdCardService.save(multipartFile);

                // find any entry saved using this PIN
                Optional<EmployeeStaticFile> employeeStaticFileOptional = employeeStaticFileRepository.findEmployeeStaticFileByPin(
                    employee.getPin()
                );

                //employeeStaticFile.setEmployee(employee.get());
                if (employeeStaticFileOptional.isPresent()) {
                    // previous file deleting
                    fileOperationService.delete(employeeStaticFileOptional.get().getFilePath());
                    employeeStaticFileRepository.delete(employeeStaticFileOptional.get());
                }

                EmployeeStaticFile employeeStaticFile = new EmployeeStaticFile();
                employeeStaticFile.setEmployee(employee);
                employeeStaticFile.setFilePath(savedFile.getPath());
                employeeStaticFileRepository.save(employeeStaticFile);

                isSavedFile = true;
            }

            return isSavedFile;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public EmployeeStaticFileDTO updateExistingEmployeeStaticFile(EmployeeStaticFileDTO employeeStaticFileDTO, MultipartFile file) {
        try {
            if (employeeStaticFileDTO.getId() > 0 && !file.isEmpty()) {
                if (!employeeStaticFileDTO.getFilePath().isEmpty()) {
                    // previous file deleting
                    fileOperationService.delete(employeeStaticFileDTO.getFilePath());
                }
                File savedFile = employeeIdCardService.save(file);
                employeeStaticFileDTO.setFilePath(savedFile.getPath());
                EmployeeStaticFile employeeStaticFile = employeeStaticFileMapper.toEntity(employeeStaticFileDTO);
                return employeeStaticFileMapper.toDto(employeeStaticFileRepository.save(employeeStaticFile));
            }
            return new EmployeeStaticFileDTO();
        } catch (Exception ex) {
            ex.getStackTrace();
            return new EmployeeStaticFileDTO();
        }
    }

    @Override
    public Page<EmployeeStaticFileDTO> findAllIDCards(Pageable pageable, String searchText) throws IOException {
        log.debug("Request to get Employee Id Card List");
        Page<EmployeeStaticFileDTO> employeeStaticFileDTOPage = employeeStaticFileRepository
            .findAll(pageable, searchText)
            .map(employeeStaticFileMapper::toDto);
        List<EmployeeStaticFileDTO> employeeStaticFileDTOArrayList = new ArrayList<>();
        for (EmployeeStaticFileDTO employeeStaticFileDTO : employeeStaticFileDTOPage) {
            employeeStaticFileDTO.setGetByteStreamFromFilePath(fileOperationService.loadAsByte(employeeStaticFileDTO.getFilePath()));
            employeeStaticFileDTOArrayList.add(employeeStaticFileDTO);
        }

        return new PageImpl<>(employeeStaticFileDTOArrayList, pageable, employeeStaticFileDTOPage.getTotalElements());
    }

    @Override
    public EmployeeStaticFileDTO getCurrentUserIdCard() throws IOException {
        try {
            Optional<String> currentEmployeePin = currentEmployeeService.getCurrentEmployeePin();
            EmployeeStaticFile employeeStaticFile = employeeStaticFileRepository
                .findEmployeeStaticFileByPin(currentEmployeePin.get())
                .get();
            EmployeeStaticFileDTO employeeSalaryDTO = employeeStaticFileMapper.toDto(employeeStaticFile);
            employeeSalaryDTO.setGetByteStreamFromFilePath(fileOperationService.loadAsByte(employeeStaticFile.getFilePath()));
            employeeSalaryDTO.setFilePath("");
            return employeeSalaryDTO;
        } catch (Exception ex) {
            ex.printStackTrace();
            EmployeeStaticFileDTO employeeStaticFileDTO = new EmployeeStaticFileDTO();
            employeeStaticFileDTO.setGetByteStreamFromFilePath(
                IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/id-card-placeholder.jpg"))
            );
            return employeeStaticFileDTO;
        }
    }

    @Override
    public String getIdCardFilePath(String pin) {
        try {
            Optional<EmployeeStaticFile> employeeStaticFile = employeeStaticFileRepository.findEmployeeStaticFileByPin(pin);
            if (employeeStaticFile.isPresent()) {
                return employeeStaticFile.get().getFilePath();
            } else {
                return "/static/images/id-card-placeholder.jpg";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "/static/images/id-card-placeholder.jpg";
        }
    }

    @Override
    public EmployeeIdCardSummaryDTO getEmployeeIdCardShortSummary() {
        EmployeeIdCardSummaryDTO employeeIdCardSummaryDTO = new EmployeeIdCardSummaryDTO();

        List<Employee> employeeList = employeeStaticFileRepository.findAllEmployee();
        employeeIdCardSummaryDTO.setUploadedEmployeeList(employeeMinimalMapper.toDto(employeeList));
        employeeIdCardSummaryDTO.setTotalUploaded(employeeList.size());

        long missingEmployee = employeeRepository.count() - employeeList.size();
        employeeIdCardSummaryDTO.setMissing((int) missingEmployee);

        List<Employee> employeeList1 = employeeRepository.findAll();
        employeeList1.removeAll(employeeList);
        employeeIdCardSummaryDTO.setMissingEmployeeList(employeeMinimalMapper.toDto(employeeList1));

        return employeeIdCardSummaryDTO;
    }
}
