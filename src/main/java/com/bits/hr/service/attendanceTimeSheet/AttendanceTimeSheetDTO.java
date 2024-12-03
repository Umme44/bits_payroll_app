package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.Status;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class AttendanceTimeSheetDTO {

    private LocalDate date;

    private AttendanceStatus attendanceStatus;

    private Instant inTime;

    private String inNote;

    private Instant outTime;

    private String outNote;

    private Status status;

    private Double totalWorkingHour;

    private Double lateDuration;

    private boolean GainedCompensatoryLeave;

    private boolean hasPendingLeaveApplication;

    private boolean hasPendingManualAttendance;

    private boolean hasPendingMovementEntry;

    private boolean hasPresentStatus;

    private boolean hasMovementStatus;

    private boolean hasLeaveStatus;

    private boolean canApplyAttendanceEntry;

    private boolean canEditAttendanceEntry;

    private boolean canApplyMovementEntry;

    private boolean canApplyLeaveApplication;

    private AttendanceDeviceOrigin punchInDeviceOrigin;

    private AttendanceDeviceOrigin punchOutDeviceOrigin;

    private Boolean isAutoPunchOut;

    public AttendanceTimeSheetDTO() {}

    public AttendanceTimeSheetDTO(
        LocalDate date,
        AttendanceStatus attendanceStatus,
        Instant inTime,
        String inNote,
        Instant outTime,
        String outNote,
        Status status,
        Double totalWorkingHour,
        Double lateDuration
    ) {
        this.date = date;
        this.attendanceStatus = attendanceStatus;
        this.inTime = inTime;
        this.inNote = inNote;
        this.outTime = outTime;
        this.outNote = outNote;
        this.status = status;
        this.totalWorkingHour = totalWorkingHour;
        this.lateDuration = lateDuration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getTotalWorkingHour() {
        return totalWorkingHour;
    }

    public void setTotalWorkingHour(Double totalWorkingHour) {
        this.totalWorkingHour = totalWorkingHour;
    }

    public Double getLateDuration() {
        return lateDuration;
    }

    public void setLateDuration(Double lateDuration) {
        this.lateDuration = lateDuration;
    }

    public boolean isGainedCompensatoryLeave() {
        return GainedCompensatoryLeave;
    }

    public void setGainedCompensatoryLeave(boolean gainedCompensatoryLeave) {
        GainedCompensatoryLeave = gainedCompensatoryLeave;
    }

    public boolean isHasPendingLeaveApplication() {
        return hasPendingLeaveApplication;
    }

    public void setHasPendingLeaveApplication(boolean hasPendingLeaveApplication) {
        this.hasPendingLeaveApplication = hasPendingLeaveApplication;
    }

    public boolean isHasPendingManualAttendance() {
        return hasPendingManualAttendance;
    }

    public void setHasPendingManualAttendance(boolean hasPendingManualAttendance) {
        this.hasPendingManualAttendance = hasPendingManualAttendance;
    }

    public boolean isHasPendingMovementEntry() {
        return hasPendingMovementEntry;
    }

    public void setHasPendingMovementEntry(boolean hasPendingMovementEntry) {
        this.hasPendingMovementEntry = hasPendingMovementEntry;
    }

    public boolean isHasPresentStatus() {
        return hasPresentStatus;
    }

    public void setHasPresentStatus(boolean hasPresentStatus) {
        this.hasPresentStatus = hasPresentStatus;
    }

    public boolean isHasMovementStatus() {
        return hasMovementStatus;
    }

    public void setHasMovementStatus(boolean hasMovementStatus) {
        this.hasMovementStatus = hasMovementStatus;
    }

    public boolean isHasLeaveStatus() {
        return hasLeaveStatus;
    }

    public void setHasLeaveStatus(boolean hasLeaveStatus) {
        this.hasLeaveStatus = hasLeaveStatus;
    }

    public boolean isCanApplyAttendanceEntry() {
        return canApplyAttendanceEntry;
    }

    public void setCanApplyAttendanceEntry(boolean canApplyAttendanceEntry) {
        this.canApplyAttendanceEntry = canApplyAttendanceEntry;
    }

    public boolean isCanEditAttendanceEntry() {
        return canEditAttendanceEntry;
    }

    public void setCanEditAttendanceEntry(boolean canEditAttendanceEntry) {
        this.canEditAttendanceEntry = canEditAttendanceEntry;
    }

    public boolean isCanApplyMovementEntry() {
        return canApplyMovementEntry;
    }

    public void setCanApplyMovementEntry(boolean canApplyMovementEntry) {
        this.canApplyMovementEntry = canApplyMovementEntry;
    }

    public boolean isCanApplyLeaveApplication() {
        return canApplyLeaveApplication;
    }

    public void setCanApplyLeaveApplication(boolean canApplyLeaveApplication) {
        this.canApplyLeaveApplication = canApplyLeaveApplication;
    }

    public AttendanceDeviceOrigin getPunchInDeviceOrigin() {
        return punchInDeviceOrigin;
    }

    public void setPunchInDeviceOrigin(AttendanceDeviceOrigin punchInDeviceOrigin) {
        this.punchInDeviceOrigin = punchInDeviceOrigin;
    }

    public AttendanceDeviceOrigin getPunchOutDeviceOrigin() {
        return punchOutDeviceOrigin;
    }

    public void setPunchOutDeviceOrigin(AttendanceDeviceOrigin punchOutDeviceOrigin) {
        this.punchOutDeviceOrigin = punchOutDeviceOrigin;
    }

    public Boolean isIsAutoPunchOut() {
        return isAutoPunchOut;
    }

    public void setIsAutoPunchOut(Boolean isAutoPunchOut) {
        this.isAutoPunchOut = isAutoPunchOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceTimeSheetDTO that = (AttendanceTimeSheetDTO) o;
        return (
            Objects.equals(getDate(), that.getDate()) &&
            getAttendanceStatus() == that.getAttendanceStatus() &&
            Objects.equals(getInTime(), that.getInTime()) &&
            Objects.equals(getInNote(), that.getInNote()) &&
            Objects.equals(getOutTime(), that.getOutTime()) &&
            Objects.equals(getOutNote(), that.getOutNote()) &&
            getStatus() == that.getStatus() &&
            Objects.equals(getTotalWorkingHour(), that.getTotalWorkingHour()) &&
            Objects.equals(getLateDuration(), that.getLateDuration())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getDate(),
            getAttendanceStatus(),
            getInTime(),
            getInNote(),
            getOutTime(),
            getOutNote(),
            getStatus(),
            getTotalWorkingHour(),
            getLateDuration()
        );
    }

    @Override
    public String toString() {
        return (
            "AttendanceTimeSheetDTO{" +
            "date=" +
            date +
            ", attendanceStatus=" +
            attendanceStatus +
            ", inTime=" +
            inTime +
            ", inNote='" +
            inNote +
            '\'' +
            ", outTime=" +
            outTime +
            ", outNote='" +
            outNote +
            '\'' +
            ", status=" +
            status +
            ", totalWorkingHour=" +
            totalWorkingHour +
            ", lateDuration=" +
            lateDuration +
            '}'
        );
    }
}
