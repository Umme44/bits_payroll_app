package com.bits.hr.service.salaryGeneration;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.time.LocalDate;

public interface LeaveAttandanceService {
    int getAbsentDays(
        EmployeeCategory employeeCategory,
        int month,
        int numberOfApprovedLeave,
        int numberOfUnapprovedLeave,
        int cashCasualLeave,
        int cashAnnualLeave,
        int HRApprovedNonMentionableLeave
    );

    int getFractionDays(
        EmployeeCategory employeeCategory,
        int month,
        int numberOfApprovedLeave,
        int numberOfUnapprovedLeave,
        int cashCasualLeave,
        int cashAnnualLeave,
        int HRApprovedNonMentionableLeave,
        LocalDate dateJoining,
        int year
    );
}
