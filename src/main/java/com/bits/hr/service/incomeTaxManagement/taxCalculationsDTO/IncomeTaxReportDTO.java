package com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO;

import com.bits.hr.service.dto.IncomeTaxChallanDTO;
import com.bits.hr.service.salaryGeneration.config.TaxReportConfigurations;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeTaxReportDTO {

    private String pin;
    private String name;

    private String designation;
    private String department;

    private String dateOfJoining;
    private String dateOfConfirmation;
    private String contactPeriodEndDate;
    private String tinNumber;
    private boolean isFixedTermContact;

    private int incomeYearStart;
    private int incomeYearEnd;
    private int assessmentYearStart;
    private int assessmentYearEnd;

    private double totalTaxLiabilities;

    private List<TaxLiability> taxLiabilities;
    private double summationOfIncomeSlabs;

    private List<SalaryIncome> salaryIncomes;
    private SalaryIncome totalSalaryIncome;

    private double maxAllowedInvestment;
    private double rebate;

    private int netTaxLiability;
    private int lastYearAdjustment;
    private int deductedAmount;
    private int refundable;

    private List<IncomeTaxChallanDTO> incomeTaxChallanList;

    private TaxReportConfigurations taxReportConfigurations;

    private boolean hasConsolidatedExemption;

    private double providentFundInvestment;
    private double otherInvestment;

    private String signatoryPersonSignature;
    private String signatoryPersonName;
    private String signatoryPersonDesignation;
}
