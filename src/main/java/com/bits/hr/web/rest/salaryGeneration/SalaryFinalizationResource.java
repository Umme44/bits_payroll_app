package com.bits.hr.web.rest.salaryGeneration;

import com.bits.hr.service.salaryLockerService.MonthlySalaryFinalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payroll-mgt")
public class SalaryFinalizationResource {

    @Autowired
    private MonthlySalaryFinalization monthlySalaryFinalization;

    @GetMapping("/salary-finalize/{year}/{month}")
    public Boolean salaryGeneration(@PathVariable int year, @PathVariable int month) throws Exception {
        return monthlySalaryFinalization.finalizeMonthlySalaryAllActiveEmployee(year, month);
    }
}
