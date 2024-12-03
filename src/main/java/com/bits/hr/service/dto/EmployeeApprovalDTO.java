package com.bits.hr.service.dto;

import java.time.Instant;
import java.util.Objects;

public class EmployeeApprovalDTO {

    private Long id;

    private String pin;

    private String picture;

    private String fullName;

    private String designationName;

    private Boolean isAllowedToGiveOnlineAttendance;

    private Instant currentInTime;

    private Instant currentOutTime;

    public EmployeeApprovalDTO() {}

    public EmployeeApprovalDTO(
        Long id,
        String pin,
        String picture,
        String fullName,
        String designationName,
        Boolean isAllowedToGiveOnlineAttendance,
        Instant currentInTime,
        Instant currentOutTime
    ) {
        this.id = id;
        this.pin = pin;
        this.picture = picture;
        this.fullName = fullName;
        this.designationName = designationName;
        this.isAllowedToGiveOnlineAttendance = isAllowedToGiveOnlineAttendance;
        this.currentInTime = currentInTime;
        this.currentOutTime = currentOutTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public Boolean isIsAllowedToGiveOnlineAttendance() {
        return isAllowedToGiveOnlineAttendance;
    }

    public void setIsAllowedToGiveOnlineAttendance(Boolean isAllowedToGiveOnlineAttendance) {
        this.isAllowedToGiveOnlineAttendance = isAllowedToGiveOnlineAttendance;
    }

    public Instant getCurrentInTime() {
        return currentInTime;
    }

    public void setCurrentInTime(Instant currentInTime) {
        this.currentInTime = currentInTime;
    }

    public Instant getCurrentOutTime() {
        return currentOutTime;
    }

    public void setCurrentOutTime(Instant currentOutTime) {
        this.currentOutTime = currentOutTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeApprovalDTO that = (EmployeeApprovalDTO) o;
        return (
            getId().equals(that.getId()) &&
            getPin().equals(that.getPin()) &&
            Objects.equals(getPicture(), that.getPicture()) &&
            getFullName().equals(that.getFullName()) &&
            Objects.equals(getDesignationName(), that.getDesignationName()) &&
            Objects.equals(isAllowedToGiveOnlineAttendance, that.isAllowedToGiveOnlineAttendance) &&
            Objects.equals(getCurrentInTime(), that.getCurrentInTime()) &&
            Objects.equals(getCurrentOutTime(), that.getCurrentOutTime())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            getId(),
            getPin(),
            getPicture(),
            getFullName(),
            getDesignationName(),
            isAllowedToGiveOnlineAttendance,
            getCurrentInTime(),
            getCurrentOutTime()
        );
    }

    @Override
    public String toString() {
        return (
            "EmployeeApprovalDTO{" +
            "id=" +
            id +
            ", pin='" +
            pin +
            '\'' +
            ", picture='" +
            picture +
            '\'' +
            ", fullName='" +
            fullName +
            '\'' +
            ", designationName='" +
            designationName +
            '\'' +
            ", isAllowedToGiveOnlineAttendance=" +
            isAllowedToGiveOnlineAttendance +
            ", currentInTime=" +
            currentInTime +
            ", currentOutTime=" +
            currentOutTime +
            '}'
        );
    }
}
