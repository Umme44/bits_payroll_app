package com.bits.hr.service.fileOperations.fileService;

import com.bits.hr.domain.Organization;
import com.bits.hr.domain.enumeration.OrganizationFileType;
import com.bits.hr.repository.OrganizationRepository;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.pathBuilder.PathBuilderService;
import com.bits.hr.service.fileOperations.pathBuilder.PathCategory;
import java.io.File;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OrganizationFileService {

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private PathBuilderService pathBuilderService;

    @Autowired
    private OrganizationRepository organizationRepository;

    public File save(MultipartFile file) {
        String fileName = "organization_" + RandomStringUtils.randomAlphanumeric(8);
        Optional<String> pathOptional = pathBuilderService.buildPath(file.getContentType(), fileName, PathCategory.ORGANIZATION);

        while (true) {
            if (!fileOperationService.isExist(pathOptional.get())) {
                break;
            } else {
                fileName = "organization_" + RandomStringUtils.randomAlphanumeric(8);
                pathOptional = pathBuilderService.buildPath(file.getContentType(), fileName, PathCategory.ORGANIZATION);
            }
        }

        String realPath = fileOperationService.save(file, pathOptional.get());

        return fileOperationService.loadAsFile(realPath);
    }
}
