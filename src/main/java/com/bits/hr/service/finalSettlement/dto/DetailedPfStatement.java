package com.bits.hr.service.finalSettlement.dto;

import com.bits.hr.service.finalSettlement.dto.pf.TotalPf;
import com.bits.hr.service.finalSettlement.dto.pf.YearlyPfCollectionForDetailedPfStatement;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class DetailedPfStatement {

    private String name;
    private LocalDate dateOfJoining;
    private LocalDate dateOfConfirmation;
    private double openingBalance;
    private List<YearlyPfCollectionForDetailedPfStatement> yearlyPfCollection;
    private TotalPf totalContribution;
    private TotalPf totalPfInterest;
    private TotalPf totalContributionWithInterests;
    private TotalPf adjustmentForArrearsPf;
    private double netPfPayable;
}
