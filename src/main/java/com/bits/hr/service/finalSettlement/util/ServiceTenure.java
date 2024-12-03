package com.bits.hr.service.finalSettlement.util;

import com.bits.hr.service.finalSettlement.dto.salary.TimeDuration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ServiceTenure {

    public static TimeDuration calculateTenure(LocalDate startDate, LocalDate endDate) {
        TimeDuration timeDuration = new TimeDuration();
        long tempDays = 0;
        if (startDate.getDayOfMonth() != 1) {
            // full month not considered
            // 1 added as date counted from DOJ
            tempDays = startDate.getDayOfMonth() - 1;
            startDate = startDate.plusDays(tempDays);
            endDate = endDate.minusDays(tempDays);
        }

        LocalDate tempDate = startDate;
        // calculate number of months between start date and end date
        int numOfTotalMonth = (int) ChronoUnit.MONTHS.between(startDate, endDate.plusDays(1));
        int numOfCompleteYear = (int) Math.floor(numOfTotalMonth / 12);

        tempDate = tempDate.plusMonths(numOfCompleteYear * 12);

        int numOfRemainingMonth = numOfTotalMonth - (numOfCompleteYear * 12);

        tempDate = tempDate.plusMonths(numOfRemainingMonth);

        int numOfRemainingDays = (int) ChronoUnit.DAYS.between(tempDate, endDate.plusDays(1));

        timeDuration.setYear(numOfCompleteYear);
        timeDuration.setMonth(numOfRemainingMonth);
        timeDuration.setDay(numOfRemainingDays);
        return timeDuration;
    }

    public static int calculateServiceYear(LocalDate dateOfJoining, LocalDate lastWorkingDay) {
        int serviceLength = 0;
        TimeDuration timeDuration = ServiceTenure.calculateTenure(dateOfJoining, lastWorkingDay);
        serviceLength += timeDuration.getYear();
        if (timeDuration.getMonth() > 6) {
            ++serviceLength;
        }
        return serviceLength;
    }
}
