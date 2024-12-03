package com.bits.hr.service.LeaveManagement.LeaveDaysCount;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Holidays;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.HolidaysRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LeaveDaysCalculationServiceImpl implements LeaveDaysCalculationService {

    @Autowired
    private HolidaysRepository holidaysRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    private static final int MAX_CASUAL_LEAVE_MONTHLY_LIMIT = 2;

    @Override
    public int leaveDaysCalculation(LocalDate leaveStartDate, LocalDate leaveEndDate) {
        // todo :: Not Employee Specific

        leaveEndDate = leaveEndDate.plusDays(1);

        List<Holidays> allHolidays = holidaysRepository.findAllHolidaysBetweenDates(leaveStartDate, leaveEndDate);

        int daysCount = 0;

        HashSet<LocalDate> hashHolidayDates = new HashSet<>();

        for (int dateRange = 0; dateRange < allHolidays.size(); dateRange++) {
            for (
                LocalDate currentDate = allHolidays.get(dateRange).getStartDate();
                currentDate.isBefore(allHolidays.get(dateRange).getEndDate().plusDays(1));
                currentDate = currentDate.plusDays(1)
            ) {
                hashHolidayDates.add(currentDate);
            }
        }

        for (LocalDate currentDate = leaveStartDate; currentDate.isBefore(leaveEndDate); currentDate = currentDate.plusDays(1)) {
            // todo : fix according to Timeslot >> Shift
            if (currentDate.getDayOfWeek().toString().equals("FRIDAY") || currentDate.getDayOfWeek().toString().equals("SATURDAY")) {
                continue;
            }
            if (hashHolidayDates.contains(currentDate)) {
                continue;
            }
            daysCount++;
        }
        return daysCount;
    }

    @Override
    @Async
    public void holidayReCalculation(LocalDate holidayStartDate, LocalDate holidayEndDate) {
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findLeaveApplicationBetweenDates(
            holidayStartDate,
            holidayEndDate
        );

        for (int dateIndex = 0; dateIndex < leaveApplications.size(); dateIndex++) {
            LocalDate leaveStartDate = leaveApplications.get(dateIndex).getStartDate();
            LocalDate leaveEndDate = leaveApplications.get(dateIndex).getEndDate();

            if (leaveApplications.get(dateIndex).getIsRejected() != null && !leaveApplications.get(dateIndex).getIsRejected()) {
                int daysOfLeave = leaveDaysCalculation(leaveStartDate, leaveEndDate);

                leaveApplications.get(dateIndex).setDurationInDay(daysOfLeave);

                leaveApplicationRepository.save(leaveApplications.get(dateIndex));
            }
        }
    }

    @Override
    public boolean leaveApplicationConflict(String pin, LocalDate startDate, LocalDate endDate) {
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findEmployeeLeaveApplicationBetweenDates(
            pin,
            startDate,
            endDate
        );

        for (int dateIndex = 0; dateIndex < leaveApplications.size(); dateIndex++) {
            if (
                (
                    (
                        startDate.isAfter(leaveApplications.get(dateIndex).getStartDate().minusDays(1)) &&
                        startDate.isBefore(leaveApplications.get(dateIndex).getEndDate().plusDays(1))
                    ) ||
                    (
                        endDate.isAfter(leaveApplications.get(dateIndex).getStartDate().minusDays(1)) &&
                        endDate.isBefore(leaveApplications.get(dateIndex).getEndDate().plusDays(1))
                    ) ||
                    (
                        (startDate.isBefore(leaveApplications.get(dateIndex).getStartDate())) &&
                        (endDate.isAfter(leaveApplications.get(dateIndex).getEndDate()))
                    )
                ) &&
                (
                    (leaveApplications.get(dateIndex).getIsHRApproved() != null && leaveApplications.get(dateIndex).getIsHRApproved()) ||
                    (leaveApplications.get(dateIndex).getIsRejected() != null && !leaveApplications.get(dateIndex).getIsRejected())
                )
            ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAnyLAConflict(String pin, Long leaveApplicationId, LocalDate startDate, LocalDate endDate) {
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findApprovedAndPendingLeaveApplicationsByEmployeePinAndDateRange(
            pin,
            startDate,
            endDate
        );

        /* checking conflict for saved and not rejected application */
        if (leaveApplicationId != null) {
            if (leaveApplications.size() > 1) {
                return true;
            } else if (leaveApplications.size() == 1) {
                if (leaveApplications.get(0).getId().equals(leaveApplicationId)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            if (leaveApplications.size() > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int monthlyRemainingCasualLeave(long employeeId, Month month, int year) {
        final LocalDate monthStartDate = LocalDate.of(year, month, 1);
        final LocalDate monthEndDate = LocalDate.of(year, month, month.length(isLeapYear(year)));

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findApprovedAndPendingLeaveByEmployeeIdAndLeaveTypeBetweenDate(
            employeeId,
            LeaveType.MENTIONABLE_CASUAL_LEAVE,
            monthStartDate,
            monthEndDate
        );

        int totalConsumed = 0;
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            if(leaveApplication.getStartDate().getMonth() == leaveApplication.getEndDate().getMonth()){
                totalConsumed += this.leaveDaysCalculation(leaveApplication.getStartDate(), leaveApplication.getEndDate());
            }
            else{
                if(leaveApplication.getStartDate().getMonth() == month){
                    totalConsumed += this.leaveDaysCalculation(leaveApplication.getStartDate(), monthEndDate);
                }
                else{
                    totalConsumed += this.leaveDaysCalculation(leaveApplication.getEndDate().with(TemporalAdjusters.firstDayOfMonth()) , leaveApplication.getEndDate());
                }
            }
        }

        final int remaining = MAX_CASUAL_LEAVE_MONTHLY_LIMIT - totalConsumed;
        return remaining;
    }

    private boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) return true; else return false;
            } else return true;
        } else return false;
    }

    public Boolean canApplyForCasualLeave(Employee employee, long leaveApplicationId, LocalDate startDate, LocalDate endDate) {
        log.debug("REST request to get the remaining days of casual leave");

        if (employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
            return true;
        }
        // start date and end date in same month
        if (startDate.getMonth().equals(endDate.getMonth())) {
            int remaining = monthlyRemainingCasualLeave(employee.getId(), startDate.getMonth(), startDate.getYear());
            final int duration = leaveDaysCalculation(startDate, endDate);

            Optional<LeaveApplication> leaveApplicationOptional = leaveApplicationRepository.findById(leaveApplicationId);
            if (
                leaveApplicationOptional.isPresent() &&
                leaveApplicationOptional.get().getLeaveType().equals(LeaveType.MENTIONABLE_CASUAL_LEAVE)
            ) {
                remaining +=
                    leaveDaysCalculation(leaveApplicationOptional.get().getStartDate(), leaveApplicationOptional.get().getEndDate());
            }

            if (duration > remaining) {
                return false;
            } else {
                return true;
            }
        } else {
            final int startMonthRemaining = monthlyRemainingCasualLeave(employee.getId(), startDate.getMonth(), startDate.getYear());
            final int endMonthRemaining = monthlyRemainingCasualLeave(employee.getId(), endDate.getMonth(), endDate.getYear());
            final int duration = leaveDaysCalculation(startDate, endDate);
            int totalRemaining = startMonthRemaining + endMonthRemaining;

            Optional<LeaveApplication> leaveApplicationOptional = leaveApplicationRepository.findById(leaveApplicationId);
            if (
                leaveApplicationOptional.isPresent() &&
                leaveApplicationOptional.get().getLeaveType().equals(LeaveType.MENTIONABLE_CASUAL_LEAVE)
            ) {
                totalRemaining +=
                    leaveDaysCalculation(leaveApplicationOptional.get().getStartDate(), leaveApplicationOptional.get().getEndDate());
            }

            if (duration > totalRemaining) {
                return false;
            } else {
                return true;
            }
        }
    }
}
