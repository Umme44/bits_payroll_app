package com.bits.hr.service.salaryGeneration;

/*
 *Non Mentionable Salary
 *  i. Compensatory Leave
 *       More than 4 Hr work in holiday = 1 compulsory leave gain
 *       Compensatory Leave can’t be carried forward to the next month
 * ii. Pandemic Leave
 *       14 days if granted
 * iii.Paternity Leave
 *       5 days on HR approval
 * iv. Maternity leave
 *       6 month on HR approval
 * */

import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeaveAttandanceServiceImpl implements LeaveAttandanceService {

    @Override
    public int getAbsentDays(
        EmployeeCategory employeeCategory,
        int month,
        int numberOfApprovedLeave,
        int numberOfUnapprovedLeave,
        int cashCasualLeave,
        int cashAnnualLeave,
        int HRApprovedNonMentionableLeave
    ) {
        if (numberOfApprovedLeave == 0 && numberOfUnapprovedLeave == 0) {
            return 0;
        }

        int absentDays = 0; // let's consider absent days = 0 at starting point
        int remainingDays = numberOfApprovedLeave;

        List<Integer> leaveBalance = new ArrayList<>();

        // Allowed Leave = money

        switch (employeeCategory) {
            case REGULAR_CONFIRMED_EMPLOYEE:
                leaveBalance.add(HRApprovedNonMentionableLeave);
                leaveBalance.add(cashCasualLeave);
                leaveBalance.add(cashAnnualLeave);
                for (Integer k : leaveBalance) {
                    // if unapproved leave big , eat all , go next
                    if (remainingDays >= k) {
                        // update k ==0 in DB of leave cash
                        remainingDays = remainingDays - k;
                    }
                    // if unapproved leave small , eat fraction from cash
                    else {
                        // update k as =>  k-remaining days;
                        remainingDays = 0;
                    }
                }

                absentDays = remainingDays + numberOfUnapprovedLeave;
                break;
            case REGULAR_PROVISIONAL_EMPLOYEE:
                leaveBalance.add(HRApprovedNonMentionableLeave);
                leaveBalance.add(cashCasualLeave);
                //leaveBalance.add(cashAnnualLeave);
                for (Integer k : leaveBalance) {
                    // if unapproved leave big , eat all , go next
                    if (remainingDays >= k) {
                        // update k ==0 in DB of leave cash
                        remainingDays = remainingDays - k;
                    }
                    // if unapproved leave small , eat fraction from cash
                    else {
                        // update k as =>  k-remaining days;
                        remainingDays = 0;
                    }
                }

                absentDays = remainingDays + numberOfUnapprovedLeave;
                break;
            case CONTRACTUAL_EMPLOYEE:
                leaveBalance.add(HRApprovedNonMentionableLeave);
                // leaveBalance.add(cashCasualLeave);
                // leaveBalance.add(cashAnnualLeave);
                for (Integer k : leaveBalance) {
                    // if unapproved leave big , eat all , go next
                    if (remainingDays >= k) {
                        // update k ==0 in DB of leave cash
                        remainingDays = remainingDays - k;
                    }
                    // if unapproved leave small , eat fraction from cash
                    else {
                        // update k as =>  k-remaining days;
                        remainingDays = 0;
                    }
                }

                absentDays = remainingDays + numberOfUnapprovedLeave;
                break;
            case INTERN:
                absentDays = remainingDays + numberOfUnapprovedLeave;

                break;
            default:
                return absentDays;
        }

        return absentDays;
    }

    @Override
    public int getFractionDays(
        EmployeeCategory employeeCategory,
        int month,
        int numberOfApprovedLeave,
        int numberOfUnapprovedLeave,
        int cashCasualLeave,
        int cashAnnualLeave,
        int HRApprovedNonMentionableLeave,
        LocalDate dateJoining,
        int year
    ) {
        int fractionDays = 0;
        int absentDays = getAbsentDays(
            employeeCategory,
            month,
            numberOfApprovedLeave,
            numberOfUnapprovedLeave,
            cashCasualLeave,
            cashAnnualLeave,
            HRApprovedNonMentionableLeave
        );
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth(); //28

        // if joined this month then this is exception case fraction days will be 15 for 15th day joiner in 30 days of month
        if (dateJoining.getMonthValue() == month) {
            fractionDays = daysInMonth - dateJoining.getDayOfMonth() + 1 - absentDays;
        } else {
            fractionDays = daysInMonth - absentDays;
        }
        return fractionDays;
    }
}
