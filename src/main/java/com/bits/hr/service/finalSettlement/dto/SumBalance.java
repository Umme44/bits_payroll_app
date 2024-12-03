package com.bits.hr.service.finalSettlement.dto;

import lombok.Data;

@Data
public class SumBalance {

    double totalGrossSalary;
    double totalDeduction;
    double salaryPayable;
    double netSalaryPayable;
    double totalFinalSettlementAmount;
}
