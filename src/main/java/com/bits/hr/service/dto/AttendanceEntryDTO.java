package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.util.annotation.ValidateNaturalText;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.AttendanceEntry} entity.
 */
public class AttendanceEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private Instant inTime;

    @ValidateNaturalText
    private String inNote;

    private Instant outTime;

    @ValidateNaturalText
    private String outNote;

    private String note;

    private Status status;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getInTime() {
        return inTime;
    }

    public void setInTime(Instant inTime) {
        this.inTime = inTime;
    }

    public String getInNote() {
        return inNote;
    }

    public void setInNote(String inNote) {
        this.inNote = inNote;
    }

    public Instant getOutTime() {
        return outTime;
    }

    public void setOutTime(Instant outTime) {
        this.outTime = outTime;
    }

    public String getOutNote() {
        return outNote;
    }

    public void setOutNote(String outNote) {
        this.outNote = outNote;
    }

    public Status getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        if (!(o instanceof AttendanceEntryDTO)) {
            return false;
        }

        return id != null && id.equals(((AttendanceEntryDTO) o).id);
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
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendanceEntryDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", inTime='" + getInTime() + "'" +
            ", inNote='" + getInNote() + "'" +
            ", outTime='" + getOutTime() + "'" +
            ", outNote='" + getOutNote() + "'" +
            ", status='" + getStatus() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
