package com.bits.hr.service.dto;

import java.time.LocalDate;

public class DateRangeDTO {

    private LocalDate startDate;
    private LocalDate endDate;

    public DateRangeDTO() {}

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DateRangeDTO{" + "startDate=" + startDate + ", endDate=" + endDate + '}';
    }
}
