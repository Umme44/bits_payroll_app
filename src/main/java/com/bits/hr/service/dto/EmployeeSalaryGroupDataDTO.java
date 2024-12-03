package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Month;
import java.util.Objects;

public class EmployeeSalaryGroupDataDTO {

    private Month month;
    private Integer year;

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeSalaryGroupDataDTO that = (EmployeeSalaryGroupDataDTO) o;
        return month == that.month && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, year);
    }
}
