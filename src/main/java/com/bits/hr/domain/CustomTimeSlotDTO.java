package com.bits.hr.domain;

import java.time.Instant;
import java.time.LocalDate;

public interface CustomTimeSlotDTO {
    Long getTimeSlotId();

    Long getFlexScheduleId();

    String getTimeSlotTitle();

    LocalDate getEffectiveDate();

    Instant getInTime();

    Instant getOutTime();

    Long getEmployeeId();

    String getWeekEnds();
}
