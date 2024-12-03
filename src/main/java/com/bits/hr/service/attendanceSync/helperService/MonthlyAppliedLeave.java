package com.bits.hr.service.attendanceSync.helperService;

import static java.time.temporal.ChronoUnit.DAYS;

import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.LeaveApplicationRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlyAppliedLeave {

    @Autowired
    LeaveApplicationRepository leaveApplicationRepository;

    public int getDaysApprovedLeave(long employeeId, LocalDate startDate, LocalDate endDate, LeaveType leaveType) {
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository
            .getLeaveApplicationByEmployeeIdBetweenTwoDates(employeeId, startDate, endDate)
            .stream()
            .filter(x -> x.getLeaveType() == leaveType && (x.getIsHRApproved() == true || x.getIsLineManagerApproved() == true))
            .collect(Collectors.toList());
        int numOfDaysApplied = 0;
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            if (leaveApplication.getStartDate().equals(leaveApplication.getEndDate())) {
                numOfDaysApplied++;
                continue;
            } else {
                numOfDaysApplied += DAYS.between(leaveApplication.getStartDate(), leaveApplication.getEndDate()) + 1;
                continue;
            }
        }
        return numOfDaysApplied;
    }
}
