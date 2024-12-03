package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.pf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pf-mgt/import")
public class ProvidentFundImportController {

    @Autowired
    PfAccountsImportService pfAccountsImportService;

    @Autowired
    PfCollectionsImportService pfCollectionsImportService;

    @Autowired
    PfCollectionsMonthlyCollectionsImportService pfCollectionsMonthlyCollectionsImportService;

    @Autowired
    PfCollectionsOpeningBalanceImportService pfCollectionsOpeningBalanceImportService;

    @Autowired
    PfCollectionsInterestsImportService pfCollectionsInterestsImportService;

    @Autowired
    PfLoansImportService pfLoansImportService;

    // For Existing Employees
    @PostMapping("/pf-accounts")
    public ResponseEntity<Boolean> pfAccountsImport(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfAccountsImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/pf-collections")
    public ResponseEntity<Boolean> pfCollectionsImport(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfCollectionsImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/pf-collections-monthly")
    public ResponseEntity<Boolean> importMonthlyPfCollections(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfCollectionsMonthlyCollectionsImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/pf-collections-interests")
    public ResponseEntity<Boolean> importPfCollectionsInterests(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfCollectionsInterestsImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/pf-collections-opening-balance")
    public ResponseEntity<Boolean> importPfCollectionsOpeningBalances(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfCollectionsOpeningBalanceImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/pf-loans")
    public ResponseEntity<Boolean> importPfLoan(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfLoansImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    // For Previous Employees

    @PostMapping("/previous-pf-accounts")
    public ResponseEntity<Boolean> previousPfAccountsImport(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfAccountsImportService.importPreviousEmployeeFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/previous-pf-collections-monthly")
    public ResponseEntity<Boolean> previousMonthlyPfCollectionsImport(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfCollectionsMonthlyCollectionsImportService.importPreviousEmployeeFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/previous-pf-collections-interests")
    public ResponseEntity<Boolean> previousPfCollectionsInterestsImport(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfCollectionsInterestsImportService.importPreviousEmployeeFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/previous-pf-collections-opening-balance")
    public ResponseEntity<Boolean> previousPfCollectionsOpeningBalancesImport(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = pfCollectionsOpeningBalanceImportService.importPreviousEmployeeFile(file);
        return ResponseEntity.ok(hasDone);
    }

    // Assign gross and basic to all pf collections
    @GetMapping("/set-gross-and-basic-to-pf-collection")
    public ResponseEntity<Boolean> setGrossAndBasicToAllPfCollection() {
        boolean hasDone = pfCollectionsImportService.setGrossAndBasicToAllPfCollection();
        return ResponseEntity.ok(hasDone);
    }

    // Import gross and basic to pf collections

    @PostMapping("/import-gross-and-basic-to-pf-collection")
    public ResponseEntity<Boolean> importGrossAndBasicToPfCollections(@RequestParam("file") MultipartFile file) {
        boolean hasDone = pfCollectionsImportService.importGrossAndBasicToAllPfCollection(file);
        return ResponseEntity.ok(hasDone);
    }
}
