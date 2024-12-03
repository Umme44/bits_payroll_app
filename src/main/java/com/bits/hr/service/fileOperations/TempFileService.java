package com.bits.hr.service.fileOperations;

import com.bits.hr.service.fileOperations.pathBuilder.PathBuilderService;
import com.bits.hr.service.fileOperations.pathBuilder.PathCategory;
import java.io.File;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TempFileService {

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private PathBuilderService pathBuilderService;

    public File save(MultipartFile file) {
        String fileName = "tmp_" + RandomStringUtils.randomAlphanumeric(8);
        Optional<String> pathOptional = pathBuilderService.buildPath(file.getContentType(), fileName, PathCategory.TMP);

        while (true) {
            if (!fileOperationService.isExist(pathOptional.get())) {
                break;
            } else {
                fileName = "tmp_" + RandomStringUtils.randomAlphanumeric(8);
                pathOptional = pathBuilderService.buildPath(file.getContentType(), fileName, PathCategory.TMP);
            }
        }

        String realPath = fileOperationService.save(file, pathOptional.get());

        return fileOperationService.loadAsFile(realPath);
    }

    public File save(String extension, String contentToWrite) {
        String fileNameWithExt = "tmp_" + RandomStringUtils.randomAlphanumeric(8) + extension;
        Optional<String> pathOptional = pathBuilderService.buildPath(fileNameWithExt, PathCategory.TMP);

        while (true) {
            if (!fileOperationService.isExist(pathOptional.get())) {
                break;
            } else {
                fileNameWithExt = "tmp_" + RandomStringUtils.randomAlphanumeric(8) + extension;
                pathOptional = pathBuilderService.buildPath(fileNameWithExt, PathCategory.TMP);
            }
        }
        String realPath = fileOperationService.save(contentToWrite, pathOptional.get());
        return fileOperationService.loadAsFile(realPath);
    }
}
