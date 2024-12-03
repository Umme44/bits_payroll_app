package com.bits.hr.service.importXL.batchOperations.employmentActions;

import org.springframework.web.multipart.MultipartFile;

public interface BatchOperationsViaXlsx {
    boolean batchOperations(MultipartFile file) throws Exception;
}
