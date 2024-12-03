package com.bits.hr.web.rest.approval;

import com.bits.hr.service.approvalProcess.EmployeeSalaryHoldService;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/payroll-mgt/salary-hold")
public class SalaryHoldResource {

    @Autowired
    EmployeeSalaryHoldService salaryHoldService;

    @GetMapping("/salaries")
    public List<EmployeeSalaryDTO> getListOfHoldSalaries(@RequestParam(value = "", required = false) String searchText) {
        return salaryHoldService.findAll(searchText);
    }
}
