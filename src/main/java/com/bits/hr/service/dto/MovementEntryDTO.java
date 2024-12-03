package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.MovementType;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.MovementEntry} entity.
 */
public class MovementEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private Instant startTime;

    @Size(min = 3, max = 250)
    @ValidateNaturalText
    private String startNote;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Instant endTime;

    @Size(min = 3, max = 250)
    @ValidateNaturalText
    private String endNote;

    private MovementType type;

    private Status status;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private LocalDate sanctionAt;

    @Size(min = 1, max = 250)
    @ValidateNaturalText
    private String note;

    private Long employeeId;

    private Long createdById;

    private String createdByLogin;

    private Long updatedById;

    private String updatedByLogin;

    private Long sanctionById;

    private String sanctionByLogin;

    private String employeeName;

    private String pin;

    private String unitName;

    private String designationName;

    private String departmentName;

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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getStartNote() {
        return startNote;
    }

    public void setStartNote(String startNote) {
        this.startNote = startNote;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getEndNote() {
        return endNote;
    }

    public void setEndNote(String endNote) {
        this.endNote = endNote;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public LocalDate getSanctionAt() {
        return sanctionAt;
    }

    public void setSanctionAt(LocalDate sanctionAt) {
        this.sanctionAt = sanctionAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public Long getSanctionById() {
        return sanctionById;
    }

    public void setSanctionById(Long userId) {
        this.sanctionById = userId;
    }

    public String getSanctionByLogin() {
        return sanctionByLogin;
    }

    public void setSanctionByLogin(String userLogin) {
        this.sanctionByLogin = userLogin;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovementEntryDTO)) {
            return false;
        }

        return id != null && id.equals(((MovementEntryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovementEntryDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", startNote='" + getStartNote() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", endNote='" + getEndNote() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", sanctionAt='" + getSanctionAt() + "'" +
            ", note='" + getNote() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", updatedById=" + getUpdatedById() +
            ", updatedByLogin='" + getUpdatedByLogin() + "'" +
            ", sanctionById=" + getSanctionById() +
            ", sanctionByLogin='" + getSanctionByLogin() + "'" +
            "}";
    }
}
