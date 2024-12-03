package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.dto.FileImportDetailsDTO;
import com.bits.hr.service.importXL.payroll.IndividualArrearSalaryImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/payroll-mgt")
public class IndividualArrearImportController {

    @Autowired
    IndividualArrearSalaryImportService individualArrearSalaryImportService;

    @PostMapping("/individual-arrear-import")
    public ResponseEntity<FileImportDetailsDTO> upload(@RequestParam("file") MultipartFile file) throws Exception {
        FileImportDetailsDTO result = individualArrearSalaryImportService.importFile(file);
        return ResponseEntity.ok().body(result);
    }
}
