package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.util.annotation.ValidateNumeric;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.EmployeePinConfiguration} entity.
 */
public class EmployeePinConfigurationDTO implements Serializable {

    private Long id;

    @NotNull
    private EmployeeCategory employeeCategory;

    @NotNull
    @ValidateNumeric
    private String sequenceStart;

    @NotNull
    @ValidateNumeric
    private String sequenceEnd;

    private String lastSequence;

    private Boolean hasFullFilled;

    private Instant createdAt;

    private Instant updatedAt;

    private String lastCreatedPin;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeCategory getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(EmployeeCategory employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public String getSequenceStart() {
        return sequenceStart;
    }

    public void setSequenceStart(String sequenceStart) {
        this.sequenceStart = sequenceStart;
    }

    public String getSequenceEnd() {
        return sequenceEnd;
    }

    public void setSequenceEnd(String sequenceEnd) {
        this.sequenceEnd = sequenceEnd;
    }

    public String getLastSequence() {
        return lastSequence;
    }

    public void setLastSequence(String lastSequence) {
        this.lastSequence = lastSequence;
    }

    public Boolean isHasFullFilled() {
        return hasFullFilled;
    }

    public void setHasFullFilled(Boolean hasFullFilled) {
        this.hasFullFilled = hasFullFilled;
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

    public String getLastCreatedPin() {
        return lastCreatedPin;
    }

    public void setLastCreatedPin(String lastCreatedPin) {
        this.lastCreatedPin = lastCreatedPin;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeePinConfigurationDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeePinConfigurationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeePinConfigurationDTO{" +
            "id=" + getId() +
            ", employeeCategory='" + getEmployeeCategory() + "'" +
            ", sequenceStart='" + getSequenceStart() + "'" +
            ", sequenceEnd='" + getSequenceEnd() + "'" +
            ", lastSequence='" + getLastSequence() + "'" +
            ", hasFullFilled='" + isHasFullFilled() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", lastCreatedPin='" + getLastCreatedPin() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            "}";
    }
}
