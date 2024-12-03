package com.bits.hr.service.salaryGeneration.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaxReportConfigurations {

    private ReportLabels reportLabels;
    private SalaryIncomeLabels salaryIncomeLabels;
    //private LinkedHashMap<String,SalaryIncomeLabel> salaryIncomeLabels;
}
