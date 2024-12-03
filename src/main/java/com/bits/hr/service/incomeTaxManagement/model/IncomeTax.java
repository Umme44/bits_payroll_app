package com.bits.hr.service.incomeTaxManagement.model;

import lombok.Data;

@Data
public class IncomeTax {

    private double perYearGross;

    private double perYearBasic;
    private double perYearHouseRent;
    private double perYearMedical;
    private double perYearConveyance;
    private double perYearFestivalBonus;
    private double perYearEmployerPfContribution;

    private double perYearBasicExemption;
    private double perYearHouseRentExemption;
    private double perYearMedicalExemption;
    private double perYearConveyanceExemption;
    private double perYearFestivalBonusExemption;
    private double perYearEmployerPfContributionExemption;

    private double taxableAmountOfBasic;
    private double taxableAmountOfHouseRent;
    private double taxableAmountOfMedical;
    private double taxableAmountOfConveyance;
    private double taxableAmountOfFestivalBonus;
    private double taxableAmountOfEmployerPfContribution;

    private double totalTaxableIncome;

    private double maximumAllowedInvest;
    private double rebateFromInvest;

    private double taxFromSlabOne;
    private double taxFromSlabtwo;
    private double taxFromSlabthree;
    private double taxFromSlabFour;
    private double taxFromSlabFive;
    private double taxFromSlabSix;

    private double totalTax;
    private double perYearNetTax;

    private double netTax;
    private double perMonthTax;
}
