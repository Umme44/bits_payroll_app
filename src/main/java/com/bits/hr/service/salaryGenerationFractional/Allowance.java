package com.bits.hr.service.salaryGenerationFractional;

import lombok.Data;

@Data
public class Allowance {

    double allowance01;
    double allowance02;
    double allowance03;
    double allowance04;
    double allowance05;
    double allowance06;

    boolean isTaxableAllowance01;
    boolean isTaxableAllowance02;
    boolean isTaxableAllowance03;
    boolean isTaxableAllowance04;
    boolean isTaxableAllowance05;
    boolean isTaxableAllowance06;

    double getTotalAllowance() {
        return allowance01 + allowance02 + allowance03 + allowance04 + allowance05 + allowance06;
    }

    double getTotalTaxableAllowances() {
        double taxableAllowance = 0;
        if (isTaxableAllowance01) taxableAllowance += allowance01;
        if (isTaxableAllowance02) taxableAllowance += allowance02;
        if (isTaxableAllowance03) taxableAllowance += allowance03;
        if (isTaxableAllowance04) taxableAllowance += allowance04;
        if (isTaxableAllowance05) taxableAllowance += allowance05;
        if (isTaxableAllowance06) taxableAllowance += allowance06;
        return taxableAllowance;
    }
}
