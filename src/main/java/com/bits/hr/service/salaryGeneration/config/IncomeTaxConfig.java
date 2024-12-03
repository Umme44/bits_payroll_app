package com.bits.hr.service.salaryGeneration.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lemon
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class IncomeTaxConfig {

    private TaxRules taxRules;

    private TaxReportConfigurations taxReportConfigurations;
}
