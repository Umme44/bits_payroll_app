package com.bits.hr.service.importXL.pf;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PfLoanRepaymentsImportService implements ImportService {

    @Override
    public boolean importFile(MultipartFile file) {
        return false;
    }
}
