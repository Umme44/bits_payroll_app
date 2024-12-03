package com.bits.hr.service.fileOperations.fileService;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.EmptyFileException;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.pathBuilder.PathBuilderService;
import com.bits.hr.service.fileOperations.pathBuilder.PathCategory;
import java.io.File;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadTaxAcknowledgementReceiptService {

    private static final String FILE_PREFIX = "tax_acknowledgement_receipt";

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private PathBuilderService pathBuilderService;

    public File save(MultipartFile file) {
        // validation
        if (file.isEmpty()) {
            throw new EmptyFileException();
        }

        // validation
        if (file.getContentType() == null) {
            throw new BadRequestAlertException("File Type is invalid", "TaxAcknowledgementReceipt", "invalidFileType");
        } else if (!file.getContentType().equals(MediaType.APPLICATION_PDF_VALUE)) {
            throw new BadRequestAlertException("Only PDF is allowed", "TaxAcknowledgementReceipt", "onlyPDFAllowed");
        }

        String fileName = FILE_PREFIX + RandomStringUtils.randomAlphanumeric(8);
        Optional<String> pathOptional = pathBuilderService.buildPath(
            file.getContentType(),
            fileName,
            PathCategory.TAX_ACKNOWLEDGEMENT_RECEIPT
        );

        while (true) {
            if (!fileOperationService.isExist(pathOptional.get())) {
                break;
            } else {
                fileName = FILE_PREFIX + RandomStringUtils.randomAlphanumeric(8);
                pathOptional = pathBuilderService.buildPath(file.getContentType(), fileName, PathCategory.TAX_ACKNOWLEDGEMENT_RECEIPT);
            }
        }

        String realPath = fileOperationService.save(file, pathOptional.get());

        return fileOperationService.loadAsFile(realPath);
    }
}
