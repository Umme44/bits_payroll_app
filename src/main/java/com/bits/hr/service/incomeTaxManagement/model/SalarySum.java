package com.bits.hr.service.incomeTaxManagement.model;

import lombok.Data;

@Data
public class SalarySum {

    private double gross;
    private double basic;
    private double houseRent;
    private double medical;
    private double conveyance;

    private double effectiveFestivalBonus;

    private double arrears;
    private double pfContribution;
}
