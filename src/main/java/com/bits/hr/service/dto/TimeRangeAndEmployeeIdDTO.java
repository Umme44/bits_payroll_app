package com.bits.hr.service.dto;

import java.time.LocalDate;
import java.util.Objects;

public class TimeRangeAndEmployeeIdDTO {

    private long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;

    public TimeRangeAndEmployeeIdDTO() {}

    public TimeRangeAndEmployeeIdDTO(long employeeId, LocalDate startDate, LocalDate endDate) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
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
        TimeRangeAndEmployeeIdDTO that = (TimeRangeAndEmployeeIdDTO) o;
        return employeeId == that.employeeId && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, startDate, endDate);
    }

    @Override
    public String toString() {
        return "TimeRangeAndEmployeeIdDTO{" + "employeeId=" + employeeId + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }
}
