package com.bits.hr.service.impl;

import com.bits.hr.domain.FileTemplates;
import com.bits.hr.repository.FileTemplatesRepository;
import com.bits.hr.service.FileTemplatesService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FileTemplatesDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.pathBuilder.PathBuilderService;
import com.bits.hr.service.fileOperations.pathBuilder.PathCategory;
import com.bits.hr.service.mapper.FileTemplatesMapper;
import com.bits.hr.util.MimeTypeToFileType;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link FileTemplates}.
 */
@Service
@Transactional
public class FileTemplatesServiceImpl implements FileTemplatesService {

    private final Logger log = LoggerFactory.getLogger(FileTemplatesServiceImpl.class);

    private final FileTemplatesRepository fileTemplatesRepository;

    private final FileTemplatesMapper fileTemplatesMapper;

    private final PathBuilderService pathBuilderService;

    private final FileOperationService fileOperationService;

    private final MimeTypeToFileType mimeTypeToFileType;

    private final CurrentEmployeeService currentEmployeeService;

    public FileTemplatesServiceImpl(
        FileTemplatesRepository fileTemplatesRepository,
        FileTemplatesMapper fileTemplatesMapper,
        PathBuilderService pathBuilderService,
        FileOperationService fileOperationService,
        MimeTypeToFileType mimeTypeToFileType,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.fileTemplatesRepository = fileTemplatesRepository;
        this.fileTemplatesMapper = fileTemplatesMapper;
        this.pathBuilderService = pathBuilderService;
        this.fileOperationService = fileOperationService;
        this.mimeTypeToFileType = mimeTypeToFileType;
        this.currentEmployeeService = currentEmployeeService;
    }

    @Override
    public FileTemplatesDTO save(FileTemplatesDTO fileTemplatesDTO, MultipartFile file) {
        log.debug("Request to save FileTemplates : {}", fileTemplatesDTO);
        // 1. delete previous saved file
        // 2. build path
        // 3. save file

        if (fileTemplatesDTO.getId() != null) {
            String filePath = fileTemplatesRepository.findById(fileTemplatesDTO.getId()).get().getFilePath();
            fileOperationService.delete(filePath);
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = "file_" + RandomStringUtils.randomAlphanumeric(8) + "." + extension;
        Optional<String> pathOptional = pathBuilderService.buildPath(fileName, PathCategory.TEMPLATES);
        while (true && pathOptional.isPresent()) {
            boolean isExist = fileOperationService.isExist(pathOptional.get());
            if (isExist) {
                fileName = "file_" + RandomStringUtils.randomAlphanumeric(8) + "." + file.getOriginalFilename();
                pathOptional = pathBuilderService.buildPath(fileName, PathCategory.TEMPLATES);
                continue;
            } else {
                break;
            }
        }

        if (pathOptional.isPresent()) {
            String str = fileOperationService.save(file, pathOptional.get());
            fileTemplatesDTO.setFilePath(pathOptional.get());
            FileTemplates fileTemplates = fileTemplatesMapper.toEntity(fileTemplatesDTO);
            fileTemplates = fileTemplatesRepository.save(fileTemplates);
            return fileTemplatesMapper.toDto(fileTemplates);
        } else {
            throw new RuntimeException("Desired file can't be saved");
        }
    }

    @Override
    public FileTemplatesDTO update(FileTemplatesDTO fileTemplatesDTO) {
        log.debug("Request to save FileTemplates : {}", fileTemplatesDTO);

        FileTemplates savedFileTemplates = fileTemplatesRepository.findById(fileTemplatesDTO.getId()).get();
        fileTemplatesDTO.setFilePath(savedFileTemplates.getFilePath());

        FileTemplates fileTemplates = fileTemplatesMapper.toEntity(fileTemplatesDTO);
        Optional<FileTemplates> fileTemplatesOptional = fileTemplatesRepository.findById(fileTemplates.getId());
        if (fileTemplatesOptional.isPresent()) {
            fileTemplates.setFilePath(fileTemplatesOptional.get().getFilePath());
        }
        fileTemplates = fileTemplatesRepository.save(fileTemplates);
        return fileTemplatesMapper.toDto(fileTemplates);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileTemplatesDTO> findAll(String searchText, String fileType, Pageable pageable) throws IOException {
        log.debug("Request to get all FileTemplates");

        Page<FileTemplatesDTO> fileTemplatesDTOPage = fileTemplatesRepository
            .findAllByFileType(searchText, fileType, pageable)
            .map(fileTemplatesMapper::toDto);

        List<FileTemplatesDTO> fileTemplatesDTOList = fileTemplatesDTOPage.getContent();
        for (FileTemplatesDTO fileTemplatesDTO : fileTemplatesDTOList) {
            try {
                //String mime = mimeTypeToFileType.getMimeType(fileTemplatesDTO.getFilePath());
                //fileTemplatesDTO.setFileContentType(mime);
                // we will not expose any file directory into front end
                String extension = StringUtils.getFilenameExtension(fileTemplatesDTO.getFilePath());
                fileTemplatesDTO.setFilePath("");
                fileTemplatesDTO.setFileContentType(extension);
            } catch (Exception ex) {
                log.warn(ex.getStackTrace().toString());
                continue;
            }
        }
        // objects are non-primitive type by nature in java
        // reference of object modified and thus elements inside pageable modified.
        // no need to wrap List into pageable here.
        return fileTemplatesDTOPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileTemplatesDTO> findOne(Long id) {
        log.debug("Request to get FileTemplates : {}", id);
        Optional<FileTemplatesDTO> fileTemplatesDTOOptional = fileTemplatesRepository.findById(id).map(fileTemplatesMapper::toDto);

        if (fileTemplatesDTOOptional.isPresent()) {
            FileTemplatesDTO fileTemplatesDTO = fileTemplatesDTOOptional.get();
            try {
                //String mime = mimeTypeToFileType.getMimeType(fileTemplatesDTO.getFilePath());
                //fileTemplatesDTO.setFileContentType(mime);
                // we will not expose any file directory into front end
                fileTemplatesDTO.setFilePath("");

                return Optional.of(fileTemplatesDTO);
            } catch (Exception ex) {
                log.warn(ex.getStackTrace().toString());
                return Optional.of(null);
            }
        } else return Optional.of(null);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileTemplates : {}", id);
        // delete file
        // delete entry
        Optional<FileTemplates> fileTemplates = fileTemplatesRepository.findById(id);
        if (fileTemplates.isPresent()) {
            fileOperationService.delete(fileTemplates.get().getFilePath());
            fileTemplatesRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FileTemplatesDTO> findAllTemplatesOfUser(String searchText, String fileType, Pageable pageable) throws IOException {
        log.debug("Request to get all FileTemplates of User");

        Page<FileTemplatesDTO> fileTemplatesDTOPage = fileTemplatesRepository
            .findAllByFileTypeCommon(searchText, fileType, pageable)
            .map(fileTemplatesMapper::toDto);
        List<FileTemplatesDTO> fileTemplatesDTOList = fileTemplatesDTOPage.getContent();
        for (FileTemplatesDTO fileTemplatesDTO : fileTemplatesDTOList) {
            try {
                // we will not expose any file directory into front end
                String extension = StringUtils.getFilenameExtension(fileTemplatesDTO.getFilePath());
                fileTemplatesDTO.setFilePath("");
                fileTemplatesDTO.setFileContentType(extension);
            } catch (Exception ex) {
                log.warn(ex.getStackTrace().toString());
                continue;
            }
        }
        return new PageImpl<>(fileTemplatesDTOList, pageable, fileTemplatesDTOPage.getTotalElements());
    }
}
