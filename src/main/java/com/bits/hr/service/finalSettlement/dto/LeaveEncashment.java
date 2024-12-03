package com.bits.hr.service.finalSettlement.dto;

import com.bits.hr.util.MathRoundUtil;
import lombok.Data;

@Data
public class LeaveEncashment {

    private int numOfDays;
    private Double perDayAmount;

    public double getTotalLeaveEncashmentPayAmount() {
        return MathRoundUtil.round((double) numOfDays * perDayAmount);
    }
}
