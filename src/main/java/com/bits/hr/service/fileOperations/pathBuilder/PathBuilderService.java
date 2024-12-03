package com.bits.hr.service.fileOperations.pathBuilder;

import com.bits.hr.service.fileOperations.SubDirectoryService;
import com.bits.hr.util.MimeTypeToFileType;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathBuilderService {

    @Autowired
    SubDirectoryService subDirectoryService;

    @Autowired
    MimeTypeToFileType mimeTypeToFileType;

    public Optional<String> buildPath(String MimeType, String fileName, PathCategory pathCategory) {
        switch (pathCategory) {
            case TMP:
                return Optional.of(subDirectoryService.getTempDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case TEMPLATES:
                return Optional.of(subDirectoryService.getTemplatesDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case NOMINEE:
                return Optional.of(subDirectoryService.getNomineeDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case INSURANCE:
                return Optional.of(subDirectoryService.getInsuranceDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case OFFER:
                return Optional.of(subDirectoryService.getOfferDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case ID_CARD:
                return Optional.of(subDirectoryService.getIdCardDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case TAX_ACKNOWLEDGEMENT_RECEIPT:
                return Optional.of(
                    subDirectoryService.getTaxAcknowledgementReceiptDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType)
                );
            case ORGANIZATION:
                return Optional.of(subDirectoryService.getOrganizationDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case PROCUREMENT:
                return Optional.of(subDirectoryService.getProcurementDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case EMPLOYEE_DOCUMENTS:
                return Optional.of(subDirectoryService.getEmployeeDocumentDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            case EMPLOYEE_DOCUMENTS_TMP:
                return Optional.of(subDirectoryService.getEmployeeDocumentTmpDir() + fileName + mimeTypeToFileType.getFileExtension(MimeType));
            default:
                return Optional.ofNullable(null);
        }
    }

    public Optional<String> buildPath(String fileNameWithExt, PathCategory pathCategory) {
        switch (pathCategory) {
            case TMP:
                return Optional.of(subDirectoryService.getTempDir() + fileNameWithExt);
            case TEMPLATES:
                return Optional.of(subDirectoryService.getTemplatesDir() + fileNameWithExt);
            case NOMINEE:
                return Optional.of(subDirectoryService.getNomineeDir() + fileNameWithExt);
            case OFFER:
                return Optional.of(subDirectoryService.getOfferDir() + fileNameWithExt);
            case ID_CARD:
                return Optional.of(subDirectoryService.getIdCardDir() + fileNameWithExt);
            case EMPLOYEE_DOCUMENTS:
                return Optional.of(subDirectoryService.getEmployeeDocumentDir() + fileNameWithExt);
            case EMPLOYEE_DOCUMENTS_TMP:
                return Optional.of(subDirectoryService.getEmployeeDocumentTmpDir() + fileNameWithExt);
            default:
                return Optional.empty();
        }
    }
}
