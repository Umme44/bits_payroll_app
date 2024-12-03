package com.bits.hr.service.LeaveManagement.balanceDifference;

import com.bits.hr.service.dto.LeaveApplicationDTO;
import lombok.Data;

@Data
public class LeaveDayCountDiffObj {

    private LeaveApplicationDTO leaveApplication;
    private int newCalculatedDurationInDays;
}
