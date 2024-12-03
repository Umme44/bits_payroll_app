package com.bits.hr.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.FlexSchedule} entity.
 */
public class FlexScheduleDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate effectiveDate;

    @NotNull
    private Instant inTime;

    @NotNull
    private Instant outTime;

    private Long employeeId;

    private String fullName;

    private String pin;

    private Long createdById;

    private String createdByLogin;

    private LocalDate startDate;
    private LocalDate endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlexScheduleDTO)) {
            return false;
        }

        return id != null && id.equals(((FlexScheduleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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

    // prettier-ignore
    @Override
    public String toString() {
        return "FlexScheduleDTO{" +
            "id=" + getId() +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", inTime='" + getInTime() + "'" +
            ", outTime='" + getOutTime() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            "}";
    }
}
