package com.bits.hr.service.dto;

import java.time.LocalDate;
import java.util.List;

public class TimeRangeAndEmployeeIdListDTO {
    private List<Long> employeeIdList;
    private LocalDate startDate;
    private LocalDate endDate;

    public List<Long> getEmployeeIdList() {
        return employeeIdList;
    }

    public void setEmployeeIdList(List<Long> employeeIdList) {
        this.employeeIdList = employeeIdList;
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

    public TimeRangeAndEmployeeIdListDTO() {
    }

    public TimeRangeAndEmployeeIdListDTO(List<Long> employeeIdList, LocalDate startDate, LocalDate endDate) {
        this.employeeIdList = employeeIdList;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "TimeRangeAndEmployeeIdListDTO{" +
            "employeeIdList=" + employeeIdList +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            '}';
    }
}
