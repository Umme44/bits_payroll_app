package com.bits.hr.service.finalSettlement.dto.pf;

import java.util.ArrayList;
import lombok.Data;

@Data
public class YearlyPfCollection {

    private int year;
    private ArrayList<MonthlyContribution> monthlyContributionList;
    private YearlyTotalContribution yearlyTotalContribution;
}
