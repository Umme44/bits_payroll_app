package com.bits.hr.service.salaryGeneration.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalaryIncomeLabels {

    private SalaryIncomeLabel salaryIncomeBasic;
    private SalaryIncomeLabel salaryIncomeHr;
    private SalaryIncomeLabel salaryIncomeMedical;
    private SalaryIncomeLabel salaryIncomeConveyanceAllowance;
    private SalaryIncomeLabel salaryIncomeFestivalBonus;
    private SalaryIncomeLabel salaryIncomePerformanceBonus;
    private SalaryIncomeLabel salaryIncomeIncentive;
    private SalaryIncomeLabel salaryIncomePf;
    private SalaryIncomeLabel salaryIncomeTotal;
}
