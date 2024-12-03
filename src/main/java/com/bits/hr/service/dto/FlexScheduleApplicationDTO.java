package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.FlexScheduleApplication} entity.
 */
public class FlexScheduleApplicationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate effectiveFrom;

    @NotNull
    private LocalDate effectiveTo;

    @Lob
    @ValidateNaturalText
    private String reason;

    @NotNull
    private Status status;

    private Instant sanctionedAt;

    private LocalDate appliedAt;

    private Instant createdAt;

    private Instant updatedAt;

    private Long requesterId;

    private String fullName;

    private String pin;

    private String designationName;

    private Long sanctionedById;

    private String sanctionedByLogin;

    private Long appliedById;

    private String appliedByLogin;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long timeSlotId;

    private Instant inTime;

    private Instant outTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getSanctionedAt() {
        return sanctionedAt;
    }

    public void setSanctionedAt(Instant sanctionedAt) {
        this.sanctionedAt = sanctionedAt;
    }

    public LocalDate getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDate appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long employeeId) {
        this.requesterId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
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

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Instant getInTime() {
        return inTime;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public Instant getOutTime() {
        return outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlexScheduleApplicationDTO)) {
            return false;
        }

        return id != null && id.equals(((FlexScheduleApplicationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexScheduleApplicationDTO{" +
            "id=" + getId() +
            ", effectiveFrom='" + getEffectiveFrom() + "'" +
            ", effectiveTo='" + getEffectiveTo() + "'" +
            ", reason='" + getReason() + "'" +
            ", status='" + getStatus() + "'" +
            ", sanctionedAt='" + getSanctionedAt() + "'" +
            ", appliedAt='" + getAppliedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", requesterId=" + getRequesterId() +
            ", sanctionedById=" + getSanctionedById() +
            ", sanctionedByLogin='" + getSanctionedByLogin() + "'" +
            ", appliedById=" + getAppliedById() +
            ", appliedByLogin='" + getAppliedByLogin() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            ", timeSlotId=" + getTimeSlotId() +
            "}";
    }
}
