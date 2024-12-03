package com.bits.hr.service.salaryGeneration.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaxRules {

    private Boolean hasConsolidatedExemption;
    private String consolidatedExemption;
    private String houseRentExemption;
    private String medicalExemption;
    private String conveyanceExemption;
    private String maxAllowedInvestment;
    private String rebate;

    private TaxSlab[] taxSlabs;

    private FirstTaxSlabSpecialCases firstTaxSlabSpecialCases;
}
