package com.bits.hr.web.rest.export;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.xlExportHandling.EmployeeCommonExportUtil;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeeCommonExportController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Generic XLSX Salary Export Service
    @GetMapping("/export-employees")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=EmployeeGeneralSheet_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        //List<EmployeeSalary> listUsers = employeeSalaryRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDate monthStart = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate monthEnd = LocalDate.of(today.getYear(), today.getMonth(), today.lengthOfMonth());

        List<Employee> employeeList = employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(monthStart, monthEnd);

        EmployeeCommonExportUtil excelExporter = new EmployeeCommonExportUtil(employeeList);

        excelExporter.export(response);
    }
}
