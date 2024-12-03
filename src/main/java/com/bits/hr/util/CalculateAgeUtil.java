package com.bits.hr.util;

import java.time.LocalDate;

public class CalculateAgeUtil {

    public static int calculateAgeInYearByDate(LocalDate birthDate) {
        final LocalDate todayDate = LocalDate.now();
        int years = todayDate.getYear() - birthDate.getYear();

        if (
            todayDate.getMonthValue() < birthDate.getMonthValue() ||
            (todayDate.getMonthValue() == birthDate.getMonthValue() && todayDate.getDayOfMonth() < birthDate.getDayOfMonth())
        ) {
            years--;
        }

        return years;
    }
}
