package com.bits.hr.service.finalSettlement.dto;

import com.bits.hr.service.finalSettlement.dto.pf.TotalPf;
import com.bits.hr.service.finalSettlement.dto.pf.YearlyPfCollection;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class PfStatement {

    private String name;
    private LocalDate dateOfJoining;
    private LocalDate dateOfConfirmation;
    private double openingBalance;
    private List<YearlyPfCollection> yearlyPfCollection;
    private TotalPf totalContribution;
    private TotalPf totalPfInterest;
    private TotalPf totalContributionWithInterests;
    private double adjustmentForArrearsPf;
    private double netPfPayable;
}
