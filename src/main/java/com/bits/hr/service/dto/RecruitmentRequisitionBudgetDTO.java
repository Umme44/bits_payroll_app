package com.bits.hr.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.bits.hr.domain.RecruitmentRequisitionBudget} entity.
 */
public class RecruitmentRequisitionBudgetDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1970L)
    @Max(value = 2070L)
    private Long year;

    @NotNull
    private Long budget;

    @NotNull
    private Long remainingBudget;

    @NotNull
    private Long remainingManpower;

    private Long employeeId;
    private String employeeFullName;
    private String employeePin;

    private Long departmentId;
    private String departmentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Long getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(Long remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public Long getRemainingManpower() {
        return remainingManpower;
    }

    public void setRemainingManpower(Long remainingManpower) {
        this.remainingManpower = remainingManpower;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public String getEmployeePin() {
        return employeePin;
    }

    public void setEmployeePin(String employeePin) {
        this.employeePin = employeePin;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionBudgetDTO)) {
            return false;
        }

        return id != null && id.equals(((RecruitmentRequisitionBudgetDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RecruitmentRequisitionBudgetDTO{" +
            "id=" + id +
            ", year=" + year +
            ", budget=" + budget +
            ", remainingBudget=" + remainingBudget +
            ", remainingManpower=" + remainingManpower +
            ", employeeId=" + employeeId +
            ", employeeFullName='" + employeeFullName + '\'' +
            ", employeePin='" + employeePin + '\'' +
            ", departmentId=" + departmentId +
            ", departmentName='" + departmentName + '\'' +
            '}';
    }
}
