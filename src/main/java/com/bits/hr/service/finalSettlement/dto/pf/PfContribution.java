package com.bits.hr.service.finalSettlement.dto.pf;

import java.util.Objects;
import lombok.Data;

@Data
public class PfContribution {

    private int year;
    private double ownContribution;
    private double companyContribution;
}
