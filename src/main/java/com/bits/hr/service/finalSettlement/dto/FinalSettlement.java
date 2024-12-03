package com.bits.hr.service.finalSettlement.dto;

import com.bits.hr.service.finalSettlement.dto.salary.SalaryAddition;
import com.bits.hr.service.finalSettlement.dto.salary.SalaryDeduction;
import com.bits.hr.service.finalSettlement.dto.salary.SalaryStructure;
import java.time.LocalDate;
import lombok.Data;

@Deprecated
public class FinalSettlement {

    private String name;
    private String pin;
    private LocalDate dateOfJoining;
    private LocalDate dateOfResignation;
    private int noticePeriodInDays;
    private LocalDate lastWorkingDay;
    private LocalDate dateOfRelease;
    private String ServiceTenure;

    private SalaryStructure salaryStructure;
    private SalaryAddition salaryAddition;
    private SalaryDeduction salaryDeduction;

    private double salaryPayable;
    private double annualIncomeTax;
    private double netSalaryPayable;

    private double totalPfPayable;
    private double totalGfPayable;

    private double TotalFinalStatementAmount;
}
