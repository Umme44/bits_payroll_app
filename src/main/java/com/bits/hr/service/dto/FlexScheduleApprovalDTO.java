package com.bits.hr.service.dto;

import java.time.Instant;
import java.time.LocalDate;

public class FlexScheduleApprovalDTO {

    private LocalDate effectiveDate;

    private ApprovalDTO approvalDTO;
    private Instant inTime;
    private Instant outTime;

    private Long timeSlotId;

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public ApprovalDTO getApprovalDTO() {
        return approvalDTO;
    }

    public void setApprovalDTO(ApprovalDTO approvalDTO) {
        this.approvalDTO = approvalDTO;
    }

    public Instant getInTime() {
        return inTime;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public Instant getOutTime() {
        return outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }
}
