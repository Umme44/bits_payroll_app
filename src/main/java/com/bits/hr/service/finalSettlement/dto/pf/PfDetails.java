package com.bits.hr.service.finalSettlement.dto.pf;

import java.util.ArrayList;
import lombok.Data;

@Data
public class PfDetails {

    private double openingBalance;
    private ArrayList<PfContribution> pfContributionList;
    private PfInterest pfInterest;
    private double pfArrear;
    private double TotalPfPayable;
    private String remarks;
}
