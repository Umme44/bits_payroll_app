package com.bits.hr.service.incomeTaxManagement.helperMethods;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculateTaxQueryConfig {

    /***
     Multiplier calculation

     if 5 aug (8) 2021 salary generation , income year = 2021 1 july(7) to 2022 june(6) 31
     m: 1-6 >> year-1 - year(now)
     m: 7-12 >> year(now) - year+1

     JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;

     income year start  ::  ALL END YEAR ::end year: []
     month : 7     :: JULY       :: multiplier   :12    previous month   ::start Year: []
     month : 8     :: AUGUST     :: multiplier   :11    previous month   ::start Year: [JULY]
     month : 9     :: SEPTEMBER  :: multiplier   :10    previous month   ::start Year: [JULY, AUGUST]
     month : 10    :: OCTOBER    :: multiplier   :9     previous month   ::start Year: [JULY, AUGUST,SEPTEMBER]
     month : 11    :: NOVEMBER   :: multiplier   :8     previous month   ::start Year: [JULY, AUGUST,SEPTEMBER,OCTOBER]
     month : 12    :: DECEMBER   :: multiplier   :7     previous month   ::start Year: [JULY, AUGUST,SEPTEMBER,OCTOBER,NOVEMBER]


     income year end     :: All  ::start Year: [JULY, AUGUST,SEPTEMBER,OCTOBER,NOVEMBER,DECEMBER]
     month : 1   :: JANUARY      :: multiplier   :6     previous month ::end year: []
     month : 2   ::FEBRUARY      :: multiplier   :5     previous month ::end year: [JANUARY]
     month : 3   ::MARCH         :: multiplier   :4     previous month ::end year: [JANUARY,FEBRUARY]
     month : 4   ::APRIL         :: multiplier   :3     previous month ::end year: [JANUARY,FEBRUARY,MARCH]
     month : 5   ::MAY           :: multiplier   :2     previous month ::end year: [JANUARY,FEBRUARY,MARCH,APRIL]
     month : 6   ::JUNE          :: multiplier   :1     previous month ::end year: [JANUARY,FEBRUARY,MARCH,APRIL,May]
     * * */

    public static TaxQueryConfig getConfig(EmployeeSalary employeeSalary) {
        int incomeYearStart = 0;
        int incomeYearEnd = 0;

        int salGenMonth = Month.fromEnum(employeeSalary.getMonth()); // ordinal starts from 0;

        //        if 5 aug (8) 2021 salary generation , income year = 2021 1 july(7) to 2022 june(6) 31
        //        m: 1-6 >> year-1 - year(now)
        //        m: 7-12 >> year(now) - year+1
        if (salGenMonth <= 6) {
            incomeYearEnd = employeeSalary.getYear();
            incomeYearStart = incomeYearEnd - 1;
        } else {
            incomeYearStart = employeeSalary.getYear();
            incomeYearEnd = incomeYearStart + 1;
        }

        LocalDate incomeYearStartDate = LocalDate.of(incomeYearStart, java.time.Month.JULY, 1);
        LocalDate incomeYearEndDate = LocalDate.of(incomeYearEnd, java.time.Month.JUNE, 30);
        List<Month> startYearMonths = new ArrayList<>();

        List<Month> endYearMonths = new ArrayList<>();

        switch (salGenMonth) {
            case 7: // JULY
                break;
            case 8: //AUG
                startYearMonths.add(Month.JULY);
                break;
            case 9: // sep
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                break;
            case 10: //oct
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                break;
            case 11: // nov
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                break;
            case 12: // dec
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                break;
            case 1: // jan
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                break;
            case 2: // feb
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);

                break;
            case 3: // mar
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                break;
            case 4: // apr
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);

                break;
            case 5: // may
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);
                endYearMonths.add(Month.APRIL);
                break;
            case 6: // june
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);
                endYearMonths.add(Month.APRIL);
                endYearMonths.add(Month.MAY);
                break;
        }

        TaxQueryConfig taxQueryConfig = new TaxQueryConfig();

        taxQueryConfig.setSalaryGenMonth(salGenMonth);

        taxQueryConfig.setIncomeYearStart(incomeYearStart);

        taxQueryConfig.setIncomeYearEnd(incomeYearEnd);
        taxQueryConfig.setIncomeYearStartDate(incomeYearStartDate);

        taxQueryConfig.setIncomeYearEndDate(incomeYearEndDate);
        taxQueryConfig.setStartYearMonths(startYearMonths);

        taxQueryConfig.setEndYearMonths(endYearMonths);
        return taxQueryConfig;
    }

    public static TaxQueryConfig getConfig(int trackingMonth, int trackingYear) {
        int incomeYearStart = 0;
        int incomeYearEnd = 0;

        int salGenMonth = trackingMonth; // ordinal starts from 0;

        //        if 5 aug (8) 2021 salary generation , income year = 2021 1 july(7) to 2022 june(6) 31
        //        m: 1-6 >> year-1 - year(now)
        //        m: 7-12 >> year(now) - year+1
        if (salGenMonth <= 6) {
            incomeYearEnd = trackingYear;
            incomeYearStart = incomeYearEnd - 1;
        } else {
            incomeYearStart = trackingYear;
            incomeYearEnd = incomeYearStart + 1;
        }

        LocalDate incomeYearStartDate = LocalDate.of(incomeYearStart, java.time.Month.JULY, 1);
        LocalDate incomeYearEndDate = LocalDate.of(incomeYearEnd, java.time.Month.JUNE, 30);
        List<Month> startYearMonths = new ArrayList<>();

        List<Month> endYearMonths = new ArrayList<>();

        switch (salGenMonth) {
            case 7: // JULY
                break;
            case 8: //AUG
                startYearMonths.add(Month.JULY);
                break;
            case 9: // sep
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                break;
            case 10: //oct
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                break;
            case 11: // nov
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                break;
            case 12: // dec
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                break;
            case 1: // jan
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                break;
            case 2: // feb
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);

                break;
            case 3: // mar
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                break;
            case 4: // apr
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);

                break;
            case 5: // may
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);
                endYearMonths.add(Month.APRIL);
                break;
            case 6: // june
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);
                endYearMonths.add(Month.APRIL);
                endYearMonths.add(Month.MAY);
                break;
        }

        TaxQueryConfig taxQueryConfig = new TaxQueryConfig();

        taxQueryConfig.setSalaryGenMonth(salGenMonth);

        taxQueryConfig.setIncomeYearStart(incomeYearStart);

        taxQueryConfig.setIncomeYearEnd(incomeYearEnd);
        taxQueryConfig.setIncomeYearStartDate(incomeYearStartDate);

        taxQueryConfig.setIncomeYearEndDate(incomeYearEndDate);
        taxQueryConfig.setStartYearMonths(startYearMonths);

        taxQueryConfig.setEndYearMonths(endYearMonths);
        return taxQueryConfig;
    }

    public static TaxQueryConfig getConfigForFinalSettlement(LocalDate lastWorkingDay) {
        int incomeYearStart = 0;
        int incomeYearEnd = 0;

        int salGenMonth = lastWorkingDay.getMonthValue();

        //        if 5 aug (8) 2021 salary generation , income year = 2021 1 july(7) to 2022 june(6) 31
        //        m: 1-6 >> year-1 - year(now)
        //        m: 7-12 >> year(now) - year+1
        if (salGenMonth <= 6) {
            incomeYearEnd = lastWorkingDay.getYear();
            incomeYearStart = incomeYearEnd - 1;
        } else {
            incomeYearStart = lastWorkingDay.getYear();
            incomeYearEnd = incomeYearStart + 1;
        }

        LocalDate incomeYearStartDate = LocalDate.of(incomeYearStart, java.time.Month.JULY, 1);
        LocalDate incomeYearEndDate = LocalDate.of(incomeYearEnd, java.time.Month.JUNE, 30);

        List<Month> startYearMonths = new ArrayList<>();

        List<Month> endYearMonths = new ArrayList<>();

        switch (salGenMonth) {
            case 7: // JULY
                break;
            case 8: //AUG
                startYearMonths.add(Month.JULY);
                break;
            case 9: // sep
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                break;
            case 10: //oct
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                break;
            case 11: // nov
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                break;
            case 12: // dec
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                break;
            case 1: // jan
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                break;
            case 2: // feb
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);

                break;
            case 3: // mar
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                break;
            case 4: // apr
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);

                break;
            case 5: // may
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);
                endYearMonths.add(Month.APRIL);
                break;
            case 6: // june
                startYearMonths.add(Month.JULY);
                startYearMonths.add(Month.AUGUST);
                startYearMonths.add(Month.SEPTEMBER);
                startYearMonths.add(Month.OCTOBER);
                startYearMonths.add(Month.NOVEMBER);
                startYearMonths.add(Month.DECEMBER);

                endYearMonths.add(Month.JANUARY);
                endYearMonths.add(Month.FEBRUARY);
                endYearMonths.add(Month.MARCH);
                endYearMonths.add(Month.APRIL);
                endYearMonths.add(Month.MAY);
                break;
        }

        TaxQueryConfig taxQueryConfig = new TaxQueryConfig();

        taxQueryConfig.setSalaryGenMonth(salGenMonth);

        taxQueryConfig.setIncomeYearStart(incomeYearStart);
        taxQueryConfig.setIncomeYearEnd(incomeYearEnd);

        taxQueryConfig.setIncomeYearStartDate(incomeYearStartDate);
        taxQueryConfig.setIncomeYearEndDate(incomeYearEndDate);

        taxQueryConfig.setStartYearMonths(startYearMonths);
        taxQueryConfig.setEndYearMonths(endYearMonths);

        return taxQueryConfig;
    }

    public static TaxQueryConfig getConfigforTaxReport(LocalDate StartDate, LocalDate EndDate) {
        int incomeYearStart = StartDate.getYear();
        int incomeYearEnd = EndDate.getYear();

        int salGenMonth = 0; // ordinal starts from 0;

        LocalDate incomeYearStartDate = LocalDate.of(incomeYearStart, java.time.Month.JULY, 1);
        LocalDate incomeYearEndDate = LocalDate.of(incomeYearEnd, java.time.Month.JUNE, 30);

        List<Month> startYearMonths = Arrays.asList(
            Month.JULY,
            Month.AUGUST,
            Month.SEPTEMBER,
            Month.OCTOBER,
            Month.NOVEMBER,
            Month.DECEMBER
        );

        List<Month> endYearMonths = Arrays.asList(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY, Month.JUNE);

        TaxQueryConfig taxQueryConfig = new TaxQueryConfig();

        taxQueryConfig.setSalaryGenMonth(salGenMonth);

        taxQueryConfig.setIncomeYearStart(incomeYearStart);
        taxQueryConfig.setIncomeYearEnd(incomeYearEnd);

        taxQueryConfig.setIncomeYearStartDate(incomeYearStartDate);
        taxQueryConfig.setIncomeYearEndDate(incomeYearEndDate);

        taxQueryConfig.setStartYearMonths(startYearMonths);
        taxQueryConfig.setEndYearMonths(endYearMonths);

        return taxQueryConfig;
    }
}
