package com.bits.hr.service.salaryGeneration.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalaryIncomeLabel {

    private String key;
    private String head;
    private String subHead;
    private Boolean isVisibleInTaxReport;
}
