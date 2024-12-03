package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Status;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RoomRequisitionSummaryDTO {

    private LocalDate summaryStartDate;

    private LocalDate summaryEndDate;

    private Double summaryStartTime;

    private Double summaryEndTime;

    private boolean summaryBookingStatus;

    public LocalDate getSummaryStartDate() {
        return summaryStartDate;
    }

    public void setSummaryStartDate(LocalDate summaryStartDate) {
        this.summaryStartDate = summaryStartDate;
    }

    public LocalDate getSummaryEndDate() {
        return summaryEndDate;
    }

    public void setSummaryEndDate(LocalDate summaryEndDate) {
        this.summaryEndDate = summaryEndDate;
    }

    public Double getSummaryStartTime() {
        return summaryStartTime;
    }

    public void setSummaryStartTime(Double summaryStartTime) {
        this.summaryStartTime = summaryStartTime;
    }

    public Double getSummaryEndTime() {
        return summaryEndTime;
    }

    public void setSummaryEndTime(Double summaryEndTime) {
        this.summaryEndTime = summaryEndTime;
    }

    public boolean isSummaryBookingStatus() {
        return summaryBookingStatus;
    }

    public void setSummaryBookingStatus(boolean summaryBookingStatus) {
        this.summaryBookingStatus = summaryBookingStatus;
    }
}
