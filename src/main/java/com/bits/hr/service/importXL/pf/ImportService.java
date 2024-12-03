package com.bits.hr.service.importXL.pf;

import org.springframework.web.multipart.MultipartFile;

public interface ImportService {
    boolean importFile(MultipartFile file);
}
