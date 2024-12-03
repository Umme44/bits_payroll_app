package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.service.dtoValidationCustom.ValidDate;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidatePhoneNumber;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.LeaveApplication} entity.
 */

@ValidDate(start = "startDate", end = "endDate")
public class LeaveApplicationDTO implements Serializable {

    private Long id;

    private LocalDate applicationDate;

    @NotNull
    private LeaveType leaveType;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Boolean isLineManagerApproved;

    private Boolean isHRApproved;

    private Boolean isRejected;

    private String rejectionComment;

    private Boolean isHalfDay;

    @NotNull
    @Min(value = 1)
    private Integer durationInDay;

    @NotNull
    @Size(min = 11, max = 17)
    @ValidatePhoneNumber
    private String phoneNumberOnLeave;

    @Lob
    private String addressOnLeave;

    @Lob
    @ValidateNaturalText
    private String reason;

    private Instant sanctionedAt;

    private Long employeeId;

    private Long sanctionedById;

    private String pin;
    private String fullName;
    private String designationName;
    private String departmentName;
    private String unitName;

    private String sanctionedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean isIsLineManagerApproved() {
        return isLineManagerApproved;
    }

    public void setIsLineManagerApproved(Boolean isLineManagerApproved) {
        this.isLineManagerApproved = isLineManagerApproved;
    }

    public Boolean isIsHRApproved() {
        return isHRApproved;
    }

    public void setIsHRApproved(Boolean isHRApproved) {
        this.isHRApproved = isHRApproved;
    }

    public Boolean isIsRejected() {
        return isRejected;
    }

    public void setIsRejected(Boolean isRejected) {
        this.isRejected = isRejected;
    }

    public String getRejectionComment() {
        return rejectionComment;
    }

    public void setRejectionComment(String rejectionComment) {
        this.rejectionComment = rejectionComment;
    }

    public Boolean isIsHalfDay() {
        return isHalfDay;
    }

    public void setIsHalfDay(Boolean isHalfDay) {
        this.isHalfDay = isHalfDay;
    }

    public Integer getDurationInDay() {
        return durationInDay;
    }

    public void setDurationInDay(Integer durationInDay) {
        this.durationInDay = durationInDay;
    }

    public String getPhoneNumberOnLeave() {
        return phoneNumberOnLeave;
    }

    public void setPhoneNumberOnLeave(String phoneNumberOnLeave) {
        this.phoneNumberOnLeave = phoneNumberOnLeave;
    }

    public String getAddressOnLeave() {
        return addressOnLeave;
    }

    public void setAddressOnLeave(String addressOnLeave) {
        this.addressOnLeave = addressOnLeave;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getSanctionedAt() {
        return sanctionedAt;
    }

    public void setSanctionedAt(Instant sanctionedAt) {
        this.sanctionedAt = sanctionedAt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveApplicationDTO)) {
            return false;
        }

        return id != null && id.equals(((LeaveApplicationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveApplicationDTO{" +
            "id=" + getId() +
            ", applicationDate='" + getApplicationDate() + "'" +
            ", leaveType='" + getLeaveType() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isLineManagerApproved='" + isIsLineManagerApproved() + "'" +
            ", isHRApproved='" + isIsHRApproved() + "'" +
            ", isRejected='" + isIsRejected() + "'" +
            ", rejectionComment='" + getRejectionComment() + "'" +
            ", isHalfDay='" + isIsHalfDay() + "'" +
            ", durationInDay=" + getDurationInDay() +
            ", phoneNumberOnLeave='" + getPhoneNumberOnLeave() + "'" +
            ", addressOnLeave='" + getAddressOnLeave() + "'" +
            ", reason='" + getReason() + "'" +
            ", sanctionedAt='" + getSanctionedAt() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", sanctionedById=" + getSanctionedById() +
            ", sanctionedByLogin='" + getSanctionedByLogin() + "'" +
            "}";
    }
}
