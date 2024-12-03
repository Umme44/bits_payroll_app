package com.bits.hr.service.attendanceTimeSheet;

import java.time.LocalDate;

@Deprecated
public interface AttendanceTimeSheetProcessingService {
    int getAbsentDaysByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);

    int getFractionDaysByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);

    int getLateDaysByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);

    int getTotalWorkingDaysByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);

    int getTotalHolidaysConsumedIncludingWeekendByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);

    int getGainedCompensatoryLeaveByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);

    int getTotalTakenLeaveByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);
}
