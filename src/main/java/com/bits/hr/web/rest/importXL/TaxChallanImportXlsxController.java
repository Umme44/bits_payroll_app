package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.incomeTax.IncomeTaxChallanImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/payroll-mgt")
public class TaxChallanImportXlsxController {

    @Autowired
    private IncomeTaxChallanImportService incomeTaxChallanImportService; // autowired with named convention

    @PostMapping("/import-tax-challan-xlsx/")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = incomeTaxChallanImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }
}
