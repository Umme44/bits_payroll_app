package com.bits.hr.service.importXL;

import org.springframework.web.multipart.MultipartFile;

public interface XlsxImportService {
    boolean importFile(MultipartFile file, int year, int month);
}
