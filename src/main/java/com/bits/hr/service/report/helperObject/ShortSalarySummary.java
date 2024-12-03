package com.bits.hr.service.report.helperObject;

import lombok.Data;

@Data
public class ShortSalarySummary {

    int month;
    int year;

    // section A
    double totalSalary;
    double holdForFinalPayment;
    double cashPayment;

    // section B
    String allowance01Name;
    String allowance02Name;
    String allowance03Name;
    String allowance04Name;
    String allowance05Name;
    String allowance06Name;

    double totalAllowance01;
    double totalAllowance02;
    double totalAllowance03;
    double totalAllowance04;
    double totalAllowance05;
    double totalAllowance06;
    double hujur;

    double totalBracBankTransfers;
    double totalOtherBankTransfers;

    public double getTotalBankTransfer() {
        return totalSalary - (holdForFinalPayment + cashPayment);
    }

    public double getSubTotalAllowance() {
        return this.totalAllowance01 + totalAllowance02 + totalAllowance03 + totalAllowance04 + totalAllowance05 + totalAllowance06 + hujur;
    }

    public double getGrandTotal() {
        return this.getTotalBankTransfer() + this.getSubTotalAllowance();
    }
}
