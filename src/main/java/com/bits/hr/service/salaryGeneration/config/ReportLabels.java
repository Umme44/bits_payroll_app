package com.bits.hr.service.salaryGeneration.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportLabels {

    private String totalIncomeLabel;
    private String exemptionRemarks;
    private String totalTaxLiabilityLabel;
    private String investmentLabelHeader;
    private String investmentLabelPf;
    private String investmentLabelOther;
    private String investmentLabelTotal;
    private String lessRebateLabel;
    private String netTaxLiabilityConsideringRebate;
    private String finalLabelAit;
    private String finalLabelActualTax;
    private String labelTotalPayableTax;
    private String certifyText;
    private String noteText;
}
