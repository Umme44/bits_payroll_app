package com.bits.hr.service.finalSettlement.dto;

import com.bits.hr.util.MathRoundUtil;
import lombok.Data;

@Data
public class NoticePay {

    private int noticeDays;
    private int numOfDays;
    private Double perDayDeduction;

    public double getTotalNoticePayAmount() {
        return MathRoundUtil.round((double) numOfDays * perDayDeduction);
    }
}
