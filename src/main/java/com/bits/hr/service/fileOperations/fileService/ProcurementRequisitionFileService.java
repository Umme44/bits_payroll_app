package com.bits.hr.service.fileOperations.fileService;

import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.pathBuilder.PathBuilderService;
import com.bits.hr.service.fileOperations.pathBuilder.PathCategory;
import java.io.File;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcurementRequisitionFileService {

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private PathBuilderService pathBuilderService;

    public File save(byte[] file, String contentType) {
        String fileName = "procurement_" + RandomStringUtils.randomAlphanumeric(8);
        Optional<String> pathOptional = pathBuilderService.buildPath(contentType, fileName, PathCategory.PROCUREMENT);

        while (true) {
            if (!fileOperationService.isExist(pathOptional.get())) {
                break;
            } else {
                fileName = "procurement_" + RandomStringUtils.randomAlphanumeric(8);
                pathOptional = pathBuilderService.buildPath(contentType, fileName, PathCategory.PROCUREMENT);
            }
        }

        String realPath = fileOperationService.save(file, pathOptional.get());

        return fileOperationService.loadAsFile(realPath);
    }
}
