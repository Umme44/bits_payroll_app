package com.bits.hr.service.finalSettlement.dto;

import com.bits.hr.service.finalSettlement.dto.pf.PfDetails;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PfGfStatement {

    private String name;
    private String pin;
    private LocalDate dateOfJoining;
    private LocalDate dateOfConfirmation;
    private LocalDate lastWorkingDay;
    private LocalDate pfRulesEffectiveFrom;
    private LocalDate gfRulesEffectiveFrom;
    private String pfEntitlementTenure;
    private long pfEntitlementTenureInDays;
    private String gfEntitlementTenure;
    private long gfEntitlementTenureInDays;
    private double totalPayablePfAndGf;

    private PfDetails pfDetails;
    // gf details
    private int serviceLengthInYear;
    private double lastBasic;
    private double totalGfPayable;
}
