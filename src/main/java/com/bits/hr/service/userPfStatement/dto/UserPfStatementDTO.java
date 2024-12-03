package com.bits.hr.service.userPfStatement.dto;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserPfStatementDTO implements Serializable {

    // Employee Info
    private String fullName;
    private String pin;
    private String designationName;
    private String departmentName;

    private LocalDate previousClosingBalanceDate;

    // Opening balance upto (Selected Year - 2) ex- for 17.Jul.2022, opening balance upto 31.Dec.2020
    private double openingBalance;
    private LocalDate openingBalanceDate;

    // previous year contribution
    private int previousYear;
    private double previousYearMemberPfContribution;
    private double previousYearCompanyPfContribution;

    // sum of Opening Balance and Previous Year Contribution
    private double openingAndPreviousYearContributionInTotal;

    // selected year contribution
    private int selectedYear;
    private double selectedYearMemberPfContribution;
    private double selectedYearCompanyPfContribution;
    private double selectedYearTotalPfContribution;

    // pf interest till selected month and year
    private double tillSelectedMonthYearPfMemberInterest;
    private double tillSelectedMonthYearPfCompanyInterest;
    private double totalTillSelectedMonthYearPfCompanyInterest;

    // closing balance till selected date
    private double totalClosingBalance;
}
