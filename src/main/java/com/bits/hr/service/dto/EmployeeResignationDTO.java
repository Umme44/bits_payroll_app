package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.EmployeeResignation} entity.
 */
public class EmployeeResignationDTO implements Serializable {

    private Long id;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate resignationDate;

    private Status approvalStatus;

    private String approvalComment;

    private Boolean isSalaryHold;

    private Boolean isFestivalBonusHold;

    @ValidateNaturalText
    private String resignationReason;

    @NotNull
    private LocalDate lastWorkingDay;

    private String pin;
    private String name;
    private String designation;
    private String department;
    private String unit;

    private Long createdById;

    private Long uodatedById;

    private Long employeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(LocalDate resignationDate) {
        this.resignationDate = resignationDate;
    }

    public Status getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Status approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public Boolean isIsSalaryHold() {
        return isSalaryHold;
    }

    public void setIsSalaryHold(Boolean isSalaryHold) {
        this.isSalaryHold = isSalaryHold;
    }

    public Boolean isIsFestivalBonusHold() {
        return isFestivalBonusHold;
    }

    public void setIsFestivalBonusHold(Boolean isFestivalBonusHold) {
        this.isFestivalBonusHold = isFestivalBonusHold;
    }

    public String getResignationReason() {
        return resignationReason;
    }

    public void setResignationReason(String resignationReason) {
        this.resignationReason = resignationReason;
    }

    public LocalDate getLastWorkingDay() {
        return lastWorkingDay;
    }

    public void setLastWorkingDay(LocalDate lastWorkingDay) {
        this.lastWorkingDay = lastWorkingDay;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long employeeId) {
        this.createdById = employeeId;
    }

    public Long getUodatedById() {
        return uodatedById;
    }

    public void setUodatedById(Long employeeId) {
        this.uodatedById = employeeId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeResignationDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeResignationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeResignationDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", resignationDate='" + getResignationDate() + "'" +
            ", approvalStatus='" + getApprovalStatus() + "'" +
            ", approvalComment='" + getApprovalComment() + "'" +
            ", isSalaryHold='" + isIsSalaryHold() + "'" +
            ", isFestivalBonusHold='" + isIsFestivalBonusHold() + "'" +
            ", resignationReason='" + getResignationReason() + "'" +
            ", lastWorkingDay='" + getLastWorkingDay() + "'" +
            ", createdById=" + getCreatedById() +
            ", uodatedById=" + getUodatedById() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
