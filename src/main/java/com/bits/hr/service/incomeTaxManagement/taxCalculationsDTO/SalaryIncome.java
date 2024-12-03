package com.bits.hr.service.incomeTaxManagement.taxCalculationsDTO;

import lombok.Data;

@Data
public class SalaryIncome {

    private String key;
    private boolean isVisibleInTaxReport = true;
    private String head;
    private String subHead;

    private double salary;
    private double exemption;
    private double taxableIncome;
}
