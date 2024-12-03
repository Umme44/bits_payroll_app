package com.bits.hr.service.LeaveManagement.LeaveDaysCount;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.service.approvalProcess.helperObjects.AttendanceHelper;
import java.time.LocalDate;
import java.time.Month;

public interface LeaveDaysCalculationService {
    // leaveDays calculation after applying. Calculate days after removing friday-saturday and removing holidays
    int leaveDaysCalculation(LocalDate leaveStartDate, LocalDate leaveEndDate);

    void holidayReCalculation(LocalDate holidayStartDate, LocalDate holidayEndDate);

    boolean leaveApplicationConflict(String pin, LocalDate startDate, LocalDate endDate);

    boolean hasAnyLAConflict(String pin, Long leaveApplicationId, LocalDate startDate, LocalDate endDate);

    int monthlyRemainingCasualLeave(long employeeId, Month month, int year);

    Boolean canApplyForCasualLeave(Employee employee, long leaveApplicationId, LocalDate startDate, LocalDate endDate);
}
