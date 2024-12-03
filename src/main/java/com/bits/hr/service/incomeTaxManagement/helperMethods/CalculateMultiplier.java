package com.bits.hr.service.incomeTaxManagement.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.MathRoundUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class CalculateMultiplier {

    public static int getMultiplier(int salGenMonth) {
        // multiplier 12 means this+11 future month remaining , there are no previous salary history to consider
        if (salGenMonth >= 7) {
            return 12 - (salGenMonth - 7);
        } else {
            return 6 - (salGenMonth - 1);
        }
        //        int multiplier = 12;
        //
        //        if (salGenMonth == 7) multiplier = 12;
        //        else if (salGenMonth == 8) multiplier = 11;
        //        else if (salGenMonth == 9) multiplier = 10;
        //        else if (salGenMonth == 10) multiplier = 9;
        //        else if (salGenMonth == 11) multiplier = 8;
        //        else if (salGenMonth == 12) multiplier = 7;
        //
        //        else if (salGenMonth == 1) multiplier = 6;
        //        else if (salGenMonth == 2) multiplier = 5;
        //        else if (salGenMonth == 3) multiplier = 4;
        //        else if (salGenMonth == 4) multiplier = 3;
        //        else if (salGenMonth == 5) multiplier = 2;
        //        else if (salGenMonth == 6) multiplier = 1;
        //        return multiplier;
    }

    public static int getMultiplier(Employee employee, int salGenMonth, int salGenYear, TaxQueryConfig taxQueryConfig) {
        int multiplier = getMultiplier(salGenMonth);
        if (
            employee.getEmployeeCategory() != null &&
            employee.getIsFixedTermContract() != null &&
            employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE &&
            employee.getIsFixedTermContract()
        ) {
            // special case
            // if contact end is after this income year-end return multiplier
            // if contact end is between this income year return >> what remains .

            LocalDate contactEndDate = employee.getContractPeriodEndDate();

            if (employee.getContractPeriodExtendedTo() != null) {
                contactEndDate = employee.getContractPeriodExtendedTo();
            }

            if (DateUtil.isBetween(contactEndDate, taxQueryConfig.getIncomeYearStartDate(), taxQueryConfig.getIncomeYearEndDate())) {
                int monthsAfterDocInFiscalYear = getMultiplier(contactEndDate.getMonthValue()) - 1;
                multiplier = MathRoundUtil.round(ChronoUnit.DAYS.between(employee.getDateOfJoining(), contactEndDate) / 30d);
                return multiplier; //- monthsAfterDocInFiscalYear;
            }

            return multiplier;
        } else {
            return multiplier;
        }
    }

    public static int getDistributionMultiplier(
        Employee employee,
        int salGenMonth,
        int salGenYear,
        TaxQueryConfig taxQueryConfig,
        Optional<LocalDate> lastWorkingDayOptional
    ) {
        int multiplier = getMultiplier(salGenMonth);

        // for resigning employee ( approved resignation )
        // full applicable tax will be deducted accordingly
        // 500 tk remaining tax 2 month remaining , then tax = 250 tk
        // 500 tk remaining tax 1 month remaining , then tax = 500 tk
        // updated code fragment start -- ahad
        if (lastWorkingDayOptional.isPresent()) {
            // special case
            // if contact end is after this income year-end return multiplier
            // if contact end is between this income year return >> what remains .
            LocalDate lwd = lastWorkingDayOptional.get();
            if (DateUtil.isBetweenOrEqual(lwd, taxQueryConfig.getIncomeYearStartDate(), taxQueryConfig.getIncomeYearEndDate())) {
                int monthsAfterDocInFiscalYear = getMultiplier(lwd.getMonthValue()) - 1;
                return multiplier - monthsAfterDocInFiscalYear;
            }
            return multiplier;
        }
        // updated code fragment end -- ahad
        if (
            employee.getEmployeeCategory() != null &&
            employee.getIsFixedTermContract() != null &&
            employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE &&
            employee.getIsFixedTermContract()
        ) {
            // special case
            // if contact end is after this income year-end return multiplier
            // if contact end is between this income year return >> what remains .
            LocalDate contactEndDate = employee.getContractPeriodEndDate();
            if (employee.getContractPeriodExtendedTo() != null) {
                contactEndDate = employee.getContractPeriodExtendedTo();
            }
            if (DateUtil.isBetween(contactEndDate, taxQueryConfig.getIncomeYearStartDate(), taxQueryConfig.getIncomeYearEndDate())) {
                int monthsAfterDocInFiscalYear = getMultiplier(contactEndDate.getMonthValue()) - 1;
                return multiplier - monthsAfterDocInFiscalYear;
            }
            return multiplier;
        } else {
            return multiplier;
        }
    }
}
