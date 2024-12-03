package com.bits.hr.web.rest.importXL;

import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.importXL.employee.impl.EmployeeMasterImportServiceImpl;
import com.bits.hr.service.mapper.EmployeeCommonMapper;
import com.bits.hr.service.mapper.EmployeeMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeeMasterImportController {

    @Autowired
    EmployeeMasterImportServiceImpl employeeMasterImportService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeCommonMapper employeeCommonMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @PostMapping("/import-employees-master-xlsx")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = employeeMasterImportService.importEmployeesLegacyXl(file);
        //importFile(file, year, month);
        return ResponseEntity.ok(hasDone);
    }
}
