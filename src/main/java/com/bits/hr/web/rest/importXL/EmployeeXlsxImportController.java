package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.ams.LeaveApplicationsImportService;
import com.bits.hr.service.importXL.employee.EmployeeTinNumberImportService;
import com.bits.hr.service.importXL.employee.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeeXlsxImportController {

    @Autowired
    private EmployeeLegacyImportServiceImpl employeeXlsxImportService;

    @Autowired
    private EmployeeLocationImportServiceImpl employeeLocationImportService;

    @Autowired
    private EmployeeTinNumberImportService employeeTinNumberImportService;

    @Autowired
    private ImportEmployeeAllowanceInfoServiceImpl importEmployeeAllowanceInfoService;

    @Autowired
    private EmployeeBankInformationImportServiceImpl importEmployeeBankInfoService;

    @Autowired
    private LeaveApplicationsImportService leaveApplicationsImportService;

    @Autowired
    private EmployeeDDUBatchUpdateServiceImpl employeeDDUBatchUpdateService;

    @PostMapping("/import-employees-ddu-xlsx")
    public boolean uploadDDU(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = employeeDDUBatchUpdateService.batchUpdate(file);
        return hasDone;
    }

    @PostMapping("/import-employees-common-xlsx/")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = employeeXlsxImportService.importEmployeesLegacyXl(file);
        //importFile(file, year, month);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/employees/xlsx-upload")
    public ResponseEntity<Boolean> uploadMobileBill(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        return ResponseEntity.ok(employeeXlsxImportService.importEmployeesLegacyXl(file));
    }

    @PostMapping("/import-employees-tin-number-xlsx/")
    public ResponseEntity<Boolean> uploadEmployeeTinNumber(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = employeeTinNumberImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/import-allowance")
    public ResponseEntity<Boolean> importAllowance(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        return ResponseEntity.ok(importEmployeeAllowanceInfoService.importFile(file));
    }

    @PostMapping("/import-employee-locations")
    public ResponseEntity<Boolean> importLocations(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        return ResponseEntity.ok(employeeLocationImportService.importFile(file));
    }

    @PostMapping("/import-bank-details")
    public ResponseEntity<Boolean> importBankDetails(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        return ResponseEntity.ok(importEmployeeBankInfoService.importFile(file));
    }

    @PostMapping("/import-leave-application")
    public ResponseEntity<Boolean> importLeaveApplication(@RequestParam(value = "file", required = false) MultipartFile file)
        throws Exception {
        return ResponseEntity.ok(leaveApplicationsImportService.importFile(file));
    }
}
