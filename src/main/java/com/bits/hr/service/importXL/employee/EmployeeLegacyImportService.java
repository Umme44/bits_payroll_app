package com.bits.hr.service.importXL.employee;

import org.springframework.web.multipart.MultipartFile;

public interface EmployeeLegacyImportService extends EmployeeMasterImportService {
    boolean importEmployeesLegacyXl(MultipartFile file) throws Exception;
}
