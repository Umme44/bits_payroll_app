package com.bits.hr.web.rest.salaryGeneration;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.approvalProcess.SalaryLockService;
import com.bits.hr.service.salaryGenerationFractional.FractionalSalaryGenerationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payroll-mgt")
public class FractionalSalaryGeneration {

    @Autowired
    private FractionalSalaryGenerationService fractionalSalaryGenerationService;

    @Autowired
    private SalaryLockService salaryLockService;

    @GetMapping("/fractional-salary-generation/{year}/{month}")
    public List<EmployeeSalary> salaryGeneration(@PathVariable int year, @PathVariable int month) throws Exception {
        if (salaryLockService.isLocked(String.valueOf(year), String.valueOf(month))) {
            throw new BadRequestAlertException(" Regeneration locked!! ", "Payroll Management", "entryLocked");
        }
        return fractionalSalaryGenerationService.generate(year, month);
    }
}
