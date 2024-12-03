package com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.incomeTaxManagement.model.IncomeTaxData;
import com.bits.hr.service.salaryGeneration.config.TaxReportConfigurations;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

/*
 * data capture object
 * processing this object make income tax statement and per month income tax calculation matrix
 * */
@Getter
@Setter
public class TaxCalculationDTO {

    public TaxCalculationDTO(Employee employee) {
        String pin = employee.getPin();
        String name = employee.getFullName();
        EmployeeCategory employeeCategory = employee.getEmployeeCategory();

        String designation = " - ";
        if (employee.getDesignation() != null && employee.getDesignation().getDesignationName() != null) {
            designation = employee.getDesignation().getDesignationName();
        }

        String department = " - ";
        if (employee.getDepartment() != null && employee.getDepartment().getDepartmentName() != null) {
            department = employee.getDepartment().getDepartmentName();
        }

        String unit = " - ";
        if (employee.getUnit() != null && employee.getUnit().getUnitName() != null) {
            unit = employee.getUnit().getUnitName();
        }

        LocalDate dateOfJoining = LocalDate.MIN;
        if (employee.getDateOfJoining() != null) {
            dateOfJoining = employee.getDateOfJoining();
        }

        LocalDate dateOfConfirmation = LocalDate.MIN;
        if (employee.getDateOfConfirmation() != null) {
            dateOfConfirmation = employee.getDateOfConfirmation();
        }

        LocalDate contactPeriodEndDate = LocalDate.MIN;
        if (employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
            contactPeriodEndDate = employee.getContractPeriodEndDate();
        }

        boolean isFixedTermContact = false;
        if (employee.getIsFixedTermContract() != null) {
            isFixedTermContact = employee.getIsFixedTermContract();
        }

        this.pin = pin;
        this.name = name;
        this.employeeCategory = employeeCategory;
        this.designation = designation;
        this.department = department;
        this.unit = unit;
        this.dateOfJoining = (dateOfJoining);
        this.dateOfConfirmation = dateOfConfirmation;
        this.contactPeriodEndDate = contactPeriodEndDate;
        this.isFixedTermContact = isFixedTermContact;
    }

    // data required for report generation
    // ** Employee Related data
    private String pin;
    private String name;
    private EmployeeCategory employeeCategory;
    private String designation;
    private String department;
    private String unit;
    private LocalDate dateOfJoining;
    private LocalDate dateOfConfirmation;
    private LocalDate contactPeriodEndDate;
    private boolean isFixedTermContact;

    // processed data goes here
    // 1
    private List<SalaryIncome> salaryIncomeList;

    private SalaryIncome totalSalaryIncome;
    // 2
    private List<TaxLiability> taxLiability;
    private TaxLiability totalTaxLiability;

    // 3
    private double maxAllowedInvestment;
    private double rebate;

    // 4
    private double netTaxLiability; // totalTaxLiability.yearlyTax - investmentRebate
    private double lastYearAdjustment; // (Approved Documents from NBR)/AIT of Vehicle/Others
    private double yearlyPayableTax;

    // section : Monthly Analysis
    private List<Double> previousTaxDeduction;
    private double currentTaxDeduction;
    private List<Double> predictedFutureTaxDeduction;
    private double totalFutureTaxDeduction;

    // section : Special Case consideration
    private boolean isMonthlyTaxCalculation = true;

    // employee resignation
    private boolean isResigningEmployee = false;
    private String specialRemarks;

    // data for tax calculation
    private IncomeTaxData incomeTaxData;

    private int remainingMonths;

    private TaxReportConfigurations taxReportConfigurations;
    private boolean hasConsolidatedExemption;

    double perYearFestivalBonus;
    double perYearPfEmployerContribution;

    public void setExceptionCaseResigningEmployee(boolean isResigningEmployee) {
        this.isResigningEmployee = true;
        this.specialRemarks = "No tax deduction due resigning employee";
    }

    public void setMonthlyAnalysis(
        List<EmployeeSalary> previousSalaries,
        double effectiveTax,
        int remainingMonths,
        double remainingMonthIncomeTax
    ) {
        this.remainingMonths = remainingMonths;

        this.previousTaxDeduction = previousSalaries.stream().map(EmployeeSalary::getTaxDeduction).collect(Collectors.toList());

        this.currentTaxDeduction = effectiveTax;

        this.totalFutureTaxDeduction = remainingMonthIncomeTax;
        this.predictedFutureTaxDeduction = new ArrayList<>();

        // remaining months are included with current this month
        for (int i = 0; i < remainingMonths; i++) {
            this.predictedFutureTaxDeduction.add((double) MathRoundUtil.round(remainingMonthIncomeTax / remainingMonths));
        }
    }
}
