package com.bits.hr.service.dto;

import com.bits.hr.service.dtoValidationCustom.AttendanceSummaryDtoValidation;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.AttendanceSummary} entity.
 */

@AttendanceSummaryDtoValidation
public class AttendanceSummaryDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private Integer month;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2199)
    private Integer year;

    @Min(value = 0)
    @Max(value = 31)
    private Integer totalWorkingDays;

    @Min(value = 0)
    @Max(value = 31)
    private Integer totalLeaveDays;

    @NotNull
    @Min(value = 0)
    @Max(value = 31)
    private Integer totalAbsentDays;

    @NotNull
    @Min(value = 0)
    @Max(value = 31)
    private Integer totalFractionDays;

    private LocalDate attendanceRegularisationStartDate;

    private LocalDate attendanceRegularisationEndDate;

    private Long employeeId;

    private String pin;
    private String fullName;
    private String designationName;
    private String departmentName;
    private String unitName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTotalWorkingDays() {
        return totalWorkingDays;
    }

    public void setTotalWorkingDays(Integer totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }

    public Integer getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public void setTotalLeaveDays(Integer totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }

    public Integer getTotalAbsentDays() {
        return totalAbsentDays;
    }

    public void setTotalAbsentDays(Integer totalAbsentDays) {
        this.totalAbsentDays = totalAbsentDays;
    }

    public Integer getTotalFractionDays() {
        return totalFractionDays;
    }

    public void setTotalFractionDays(Integer totalFractionDays) {
        this.totalFractionDays = totalFractionDays;
    }

    public LocalDate getAttendanceRegularisationStartDate() {
        return attendanceRegularisationStartDate;
    }

    public void setAttendanceRegularisationStartDate(LocalDate attendanceRegularisationStartDate) {
        this.attendanceRegularisationStartDate = attendanceRegularisationStartDate;
    }

    public LocalDate getAttendanceRegularisationEndDate() {
        return attendanceRegularisationEndDate;
    }

    public void setAttendanceRegularisationEndDate(LocalDate attendanceRegularisationEndDate) {
        this.attendanceRegularisationEndDate = attendanceRegularisationEndDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttendanceSummaryDTO)) {
            return false;
        }

        return id != null && id.equals(((AttendanceSummaryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendanceSummaryDTO{" +
            "id=" + getId() +
            ", month=" + getMonth() +
            ", year=" + getYear() +
            ", totalWorkingDays=" + getTotalWorkingDays() +
            ", totalLeaveDays=" + getTotalLeaveDays() +
            ", totalAbsentDays=" + getTotalAbsentDays() +
            ", totalFractionDays=" + getTotalFractionDays() +
            ", attendanceRegularisationStartDate='" + getAttendanceRegularisationStartDate() + "'" +
            ", attendanceRegularisationEndDate='" + getAttendanceRegularisationEndDate() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", employeePin='" + getPin() + "'" +
            "}";
    }
}
