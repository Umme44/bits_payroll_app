package com.bits.hr.service.dto;

import java.time.LocalDate;
import java.util.Objects;

public class TimeRangeDTO {

    private LocalDate startDate;
    private LocalDate endDate;

    public TimeRangeDTO() {}

    public TimeRangeDTO(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeRangeDTO that = (TimeRangeDTO) o;
        return Objects.equals(getStartDate(), that.getStartDate()) && Objects.equals(getEndDate(), that.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        return "TimeRangeDTO{" + "startDate=" + startDate + ", endDate=" + endDate + '}';
    }
}
