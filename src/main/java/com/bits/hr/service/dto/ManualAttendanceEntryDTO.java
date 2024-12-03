package com.bits.hr.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.ManualAttendanceEntry} entity.
 */
public class ManualAttendanceEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private Instant inTime;

    private String inNote;

    private Instant outTime;

    private String outNote;

    private Boolean isLineManagerApproved;

    private Boolean isHRApproved;

    private Boolean isRejected;

    private String rejectionComment;

    @Size(min = 0, max = 250)
    private String note;

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
        if (!(o instanceof ManualAttendanceEntryDTO)) {
            return false;
        }

        return id != null && id.equals(((ManualAttendanceEntryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualAttendanceEntryDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", inTime='" + getInTime() + "'" +
            ", inNote='" + getInNote() + "'" +
            ", outTime='" + getOutTime() + "'" +
            ", outNote='" + getOutNote() + "'" +
            ", isLineManagerApproved='" + isIsLineManagerApproved() + "'" +
            ", isHRApproved='" + isIsHRApproved() + "'" +
            ", isRejected='" + isIsRejected() + "'" +
            ", rejectionComment='" + getRejectionComment() + "'" +
            ", note='" + getNote() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
