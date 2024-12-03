package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.XlsxImportService;
import com.bits.hr.service.importXL.payroll.LeaveAttandanceImportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/payroll-mgt/import")
public class SalaryGenerationImportController {

    @Autowired
    private XlsxImportService salaryDeductionXlsxImportServiceImpl;

    @Autowired
    private XlsxImportService mobileBillImportServiceImpl;

    @Autowired
    private XlsxImportService pfLoanRepaymentXlsxImportServiceImpl;

    @Autowired
    private LeaveAttandanceImportServiceImpl leaveAttendanceImportService;

    @PostMapping("/leave-attendances/{year}/{month}")
    public ResponseEntity<Boolean> uploadLeaveAttendance(
        @PathVariable int year,
        @PathVariable int month,
        @RequestParam("file") MultipartFile file
    ) throws Exception {
        boolean hasDone = leaveAttendanceImportService.importFile(file, year, month);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/mobile-bills/{year}/{month}")
    public ResponseEntity<Boolean> uploadMobileBills(
        @PathVariable int year,
        @PathVariable int month,
        @RequestParam("file") MultipartFile file
    ) throws Exception {
        boolean hasDone = mobileBillImportServiceImpl.importFile(file, year, month);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/salary-deductions/{year}/{month}")
    public ResponseEntity<Boolean> uploadSalaryDeductions(
        @PathVariable int year,
        @PathVariable int month,
        @RequestParam("file") MultipartFile file
    ) throws Exception {
        boolean hasDone = salaryDeductionXlsxImportServiceImpl.importFile(file, year, month);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/pf-loan-repayments/{year}/{month}")
    public ResponseEntity<Boolean> uploadPfLoanRepayments(
        @PathVariable int year,
        @PathVariable int month,
        @RequestParam("file") MultipartFile file
    ) throws Exception {
        boolean hasDone = pfLoanRepaymentXlsxImportServiceImpl.importFile(file, year, month);
        return ResponseEntity.ok(hasDone);
    }
}
