package com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO;

import lombok.Data;

@Data
public class TaxLiability {

    private String head;
    private String subHead;

    private double slab;
    private double rate;
    private double tax;
}
