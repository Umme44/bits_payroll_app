package com.bits.hr.service.finalSettlement.dto.pf;

import java.util.ArrayList;
import lombok.Data;

@Data
public class YearlyPfCollectionForDetailedPfStatement {

    private int year;
    private ArrayList<MonthlyContribution> monthlyContributionList;
    private YearlyTotalContribution yearlyTotalContribution;
    private YearlyTotalArrearPfDeduction yearlyTotalArrearPfDeduction;
    private YearlyTotalInterest yearlyTotalInterest;
}
