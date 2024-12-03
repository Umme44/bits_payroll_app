package com.bits.hr.service.finalSettlement.dto.salary;

import lombok.Data;

@Data
public class SalaryProcess {

    private SalaryPayable salaryPayable;
    private SalaryAddition salaryAddition;
    private SalaryDeduction salaryDeduction;
}
