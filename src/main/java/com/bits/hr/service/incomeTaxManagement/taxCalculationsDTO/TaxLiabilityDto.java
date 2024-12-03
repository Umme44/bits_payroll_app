package com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxLiabilityDto {

    private int slab;
    private int rate;
    private int tax;
}
