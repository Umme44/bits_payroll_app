package com.bits.hr.service.dto;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.Attendance} entity.
 */
public class AttendanceDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2199)
    private Integer year;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private Integer month;

    @NotNull
    @Min(value = 0)
    @Max(value = 31)
    private Integer absentDays;

    @NotNull
    @Min(value = 0)
    @Max(value = 31)
    private Integer fractionDays;

    @NotNull
    @Min(value = 0)
    @Max(value = 31)
    private Integer compensatoryLeaveGained;

    private Long employeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public Integer getFractionDays() {
        return fractionDays;
    }

    public void setFractionDays(Integer fractionDays) {
        this.fractionDays = fractionDays;
    }

    public Integer getCompensatoryLeaveGained() {
        return compensatoryLeaveGained;
    }

    public void setCompensatoryLeaveGained(Integer compensatoryLeaveGained) {
        this.compensatoryLeaveGained = compensatoryLeaveGained;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttendanceDTO)) {
            return false;
        }

        return id != null && id.equals(((AttendanceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendanceDTO{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            ", absentDays=" + getAbsentDays() +
            ", fractionDays=" + getFractionDays() +
            ", compensatoryLeaveGained=" + getCompensatoryLeaveGained() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
