package com.bits.hr.service.LeaveManagement.leaveBalanceDetail.helperService;

import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.LeaveBalance;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.repository.LeaveBalanceRepository;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO.LeaveBalanceDetailViewDTO;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveBalanceProcessService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeaveDaysCalculationService leaveDaysCalculationService;

    public int getDaysRequested(long employeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate) {
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getRequested(employeeId, leaveType, startDate, endDate);

        long totalDayCount = 0;
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            long daysBetween = leaveDaysCalculationService.leaveDaysCalculation(
                leaveApplication.getStartDate(),
                leaveApplication.getEndDate()
            );
            //(ChronoUnit.DAYS.between(leaveApplication.getStartDate(), leaveApplication.getEndDate())) + 1;
            totalDayCount += daysBetween;
        }
        return (int) totalDayCount;
    }

    public int getDaysApproved(long employeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate) {
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getDaysApprovedBetweenDates(
            employeeId,
            leaveType,
            startDate,
            endDate
        );

        long totalDayCount = 0;
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            long daysBetween = leaveDaysCalculationService.leaveDaysCalculation(
                leaveApplication.getStartDate(),
                leaveApplication.getEndDate()
            );
            //(ChronoUnit.DAYS.between(leaveApplication.getStartDate(), leaveApplication.getEndDate())) + 1;
            totalDayCount += daysBetween;
        }
        return (int) totalDayCount;
    }

    public int getDaysCancelled(long employeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate) {
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getRejected(employeeId, leaveType, startDate, endDate);

        long totalDayCount = 0;
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            long daysBetween = leaveDaysCalculationService.leaveDaysCalculation(
                leaveApplication.getStartDate(),
                leaveApplication.getEndDate()
            );
            //(ChronoUnit.DAYS.between(leaveApplication.getStartDate(), leaveApplication.getEndDate())) + 1;
            totalDayCount += daysBetween;
        }
        return (int) totalDayCount;
    }

    public LeaveBalanceDetailViewDTO processLeaveBalance(
        LeaveBalance leaveBalance,
        String pin,
        String name,
        LocalDate startOfYear,
        LocalDate endOfYear
    ) {
        LeaveBalanceDetailViewDTO leaveBalanceDetailViewDTO = new LeaveBalanceDetailViewDTO();
        leaveBalanceDetailViewDTO.setPin(pin);
        leaveBalanceDetailViewDTO.setName(name);
        leaveBalanceDetailViewDTO.setLeaveAmountType(leaveBalance.getLeaveAmountType());
        leaveBalanceDetailViewDTO.setLeaveType(leaveBalance.getLeaveType());
        leaveBalanceDetailViewDTO.setYear(leaveBalance.getYear());

        // opening balance / carry forwarded
        leaveBalanceDetailViewDTO.setOpeningBalance(leaveBalance.getOpeningBalance());

        // Allocated days
        leaveBalanceDetailViewDTO.setAmount(leaveBalance.getAmount());

        leaveBalanceDetailViewDTO.setClosingBalance(0);

        //  get Days Requested
        int daysRequested = getDaysRequested(leaveBalance.getEmployee().getId(), leaveBalance.getLeaveType(), startOfYear, endOfYear);
        leaveBalanceDetailViewDTO.setDaysRequested(daysRequested);

        //  get Days Approved
        int daysApproved = getDaysApproved(leaveBalance.getEmployee().getId(), leaveBalance.getLeaveType(), startOfYear, endOfYear);
        leaveBalanceDetailViewDTO.setDaysApproved(daysApproved);

        // consumed during this year
        leaveBalanceDetailViewDTO.setConsumedDuringYear(daysApproved + leaveBalance.getConsumedDuringYear());

        //  get Days Cancelled || rejected
        int daysCancelled = getDaysCancelled(leaveBalance.getEmployee().getId(), leaveBalance.getLeaveType(), startOfYear, endOfYear);
        leaveBalanceDetailViewDTO.setDaysCancelled(daysCancelled);

        //  get Days Remaining
        int daysRemaining = 0;

        // maternity leave will be of 2 leave continuous 6 months in service life

        daysRemaining = leaveBalance.getOpeningBalance() + leaveBalance.getAmount() - daysApproved - leaveBalance.getConsumedDuringYear();

        leaveBalanceDetailViewDTO.setDaysRemaining(daysRemaining);

        //  get Days Effective
        leaveBalanceDetailViewDTO.setDaysEffective(daysRemaining);

        return leaveBalanceDetailViewDTO;
    }
}
