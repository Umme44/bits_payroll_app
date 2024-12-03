package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.WorkFromHomeApplication} entity.
 */
public class WorkFromHomeApplicationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Size(min = 0, max = 250)
    @ValidateNaturalText
    private String reason;

    private Integer duration;

    @NotNull
    private Status status;

    private LocalDate appliedAt;

    private Instant updatedAt;

    private Instant createdAt;

    private Instant sanctionedAt;

    private Long appliedById;

    private String appliedByLogin;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long sanctionedById;

    private String sanctionedByLogin;

    private Long employeeId;

    private String pin;
    private String fullName;
    private String designationName;
    private String departmentName;
    private String unitName;
    private String bandName;

    private Boolean isAllowedToGiveOnlineAttendance;

    private LocalDate approvedStartDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDate appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getSanctionedAt() {
        return sanctionedAt;
    }

    public void setSanctionedAt(Instant sanctionedAt) {
        this.sanctionedAt = sanctionedAt;
    }

    public Long getAppliedById() {
        return appliedById;
    }

    public void setAppliedById(Long userId) {
        this.appliedById = userId;
    }

    public String getAppliedByLogin() {
        return appliedByLogin;
    }

    public void setAppliedByLogin(String userLogin) {
        this.appliedByLogin = userLogin;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(Long userId) {
        this.updatedById = userId;
    }

    public String getUpdatedByLogin() {
        return updatedByLogin;
    }

    public void setUpdatedByLogin(String userLogin) {
        this.updatedByLogin = userLogin;
    }

    public Long getSanctionedById() {
        return sanctionedById;
    }

    public void setSanctionedById(Long userId) {
        this.sanctionedById = userId;
    }

    public String getSanctionedByLogin() {
        return sanctionedByLogin;
    }

    public void setSanctionedByLogin(String userLogin) {
        this.sanctionedByLogin = userLogin;
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

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public Boolean isIsAllowedToGiveOnlineAttendance() {
        return isAllowedToGiveOnlineAttendance;
    }

    public void setIsAllowedToGiveOnlineAttendance(Boolean isAllowedToGiveOnlineAttendance) {
        this.isAllowedToGiveOnlineAttendance = isAllowedToGiveOnlineAttendance;
    }

    public LocalDate getApprovedStartDate() {
        return approvedStartDate;
    }

    public void setApprovedStartDate(LocalDate approvedStartDate) {
        this.approvedStartDate = approvedStartDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkFromHomeApplicationDTO)) {
            return false;
        }

        return id != null && id.equals(((WorkFromHomeApplicationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkFromHomeApplicationDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", reason='" + getReason() + "'" +
            ", duration=" + getDuration() +
            ", status='" + getStatus() + "'" +
            ", appliedAt='" + getAppliedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", sanctionedAt='" + getSanctionedAt() + "'" +
            ", appliedById=" + getAppliedById() +
            ", appliedByLogin='" + getAppliedByLogin() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            ", sanctionedById=" + getSanctionedById() +
            ", sanctionedByLogin='" + getSanctionedByLogin() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
