package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.ams.InsuranceDataFixingService;
import com.bits.hr.service.importXL.ams.InsuranceImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employee-mgt")
public class InsuranceImportXlsxController {

    @Autowired
    private InsuranceImportService insuranceImportService;

    @Autowired
    private InsuranceDataFixingService insuranceDataFixingService;

    //    @PostMapping("/insurance/import-insurance-xlsx/")
    //    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
    //        //boolean hasDone = insuranceImportService.importFile(file);
    //        boolean hasDone = insuranceImportDataForNewSystemService.importFile(file);
    //        return ResponseEntity.ok(hasDone);
    //    }

    @PostMapping("/insurance/import-insurance-claim-xlsx/")
    public ResponseEntity<Boolean> importInsuranceClaim(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = insuranceImportService.importInsuranceClaim(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/insurance/import-previous-insurance-claim-xlsx/")
    public ResponseEntity<Boolean> importPreviousInsuranceClaim(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = insuranceImportService.importPreviousInsuranceClaims(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/insurance/import-previous-insurance-registrations-xlsx/")
    public ResponseEntity<Boolean> importPreviousInsuranceRegistrations(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = insuranceImportService.importPreviousInsuranceRegistrations(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/insurance/import-approved-insurance-registrations-xlsx/")
    public ResponseEntity<Boolean> importApprovedInsuranceRegistrations(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = insuranceImportService.importApprovedInsuranceRegistrations(file);
        return ResponseEntity.ok(hasDone);
    }
}
