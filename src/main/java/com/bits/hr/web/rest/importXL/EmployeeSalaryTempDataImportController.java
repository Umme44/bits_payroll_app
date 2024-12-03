package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.payroll.EmployeeSalaryTempDataImportServiceImpl;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/payroll-mgt")
public class EmployeeSalaryTempDataImportController {

    @Autowired
    EmployeeSalaryTempDataImportServiceImpl employeeSalaryTempDataImportService;

    @PostMapping("/employee-salary-temp-data/import")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, String> resultSet = employeeSalaryTempDataImportService.importFile(file);
        Boolean result = true;
        return ResponseEntity.ok(result);
    }
}
