package com.bits.hr.service.fileOperations.fileService;

import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.pathBuilder.PathBuilderService;
import com.bits.hr.service.fileOperations.pathBuilder.PathCategory;
import java.io.File;
import java.util.Optional;
import javassist.bytecode.ByteArray;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InsuranceFileService {

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private PathBuilderService pathBuilderService;

    public File save(MultipartFile file) {
        String fileName = "insurance_" + RandomStringUtils.randomAlphanumeric(8);
        Optional<String> pathOptional = pathBuilderService.buildPath(file.getContentType(), fileName, PathCategory.INSURANCE);

        while (true) {
            if (!fileOperationService.isExist(pathOptional.get())) {
                break;
            } else {
                fileName = "insurance_" + RandomStringUtils.randomAlphanumeric(8);
                pathOptional = pathBuilderService.buildPath(file.getContentType(), fileName, PathCategory.INSURANCE);
            }
        }

        String realPath = fileOperationService.save(file, pathOptional.get());
        return fileOperationService.loadAsFile(realPath);
    }

    public File saveAsByteArray(byte[] file) {
        String fileName = "insurance_" + RandomStringUtils.randomAlphanumeric(8);
        Optional<String> pathOptional = pathBuilderService.buildPath(MimeTypeUtils.IMAGE_JPEG.toString(), fileName, PathCategory.INSURANCE);

        while (true) {
            if (!fileOperationService.isExist(pathOptional.get())) {
                break;
            } else {
                fileName = "insurance_" + RandomStringUtils.randomAlphanumeric(8);
                pathOptional = pathBuilderService.buildPath(MimeTypeUtils.IMAGE_JPEG.toString(), fileName, PathCategory.INSURANCE);
            }
        }

        String realPath = fileOperationService.save(file, pathOptional.get());
        return fileOperationService.loadAsFile(realPath);
    }
}
