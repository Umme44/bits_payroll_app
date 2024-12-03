package com.bits.hr.service.importXL.payroll;

import org.springframework.web.multipart.MultipartFile;

public interface LeaveAttandanceImportService {
    boolean importFile(MultipartFile file, int year, int month) throws Exception;
}
