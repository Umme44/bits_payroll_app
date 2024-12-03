package com.bits.hr.service.attendanceSync.helperObjects;

import java.util.Locale;

public class LeaveCutCSV {

    private String pin;
    private String name;
    private String designation;
    private String department;
    private String unit;
    private String leaveType;
    private String startDate;
    private String endDate;
    private int durationInDay;

    public LeaveCutCSV() {}

    public LeaveCutCSV(
        String pin,
        String name,
        String designation,
        String department,
        String unit,
        String leaveType,
        String startDate,
        String endDate,
        int durationInDay
    ) {
        this.pin = pin.replaceAll("[^a-zA-Z0-9]", "");
        this.name = name.replaceAll("[^a-zA-Z0-9]", "");
        this.designation = designation.replaceAll("[^a-zA-Z0-9]", "");
        this.department = department.replaceAll("[^a-zA-Z0-9]", "");
        this.unit = unit.replaceAll("[^a-zA-Z0-9]", "");
        this.leaveType = leaveType.replaceAll("[^a-zA-Z0-9]", "").toLowerCase(Locale.ROOT);
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationInDay = durationInDay;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDurationInDay() {
        return durationInDay;
    }

    public void setDurationInDay(int durationInDay) {
        this.durationInDay = durationInDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveCutCSV that = (LeaveCutCSV) o;

        if (getDurationInDay() != that.getDurationInDay()) return false;
        if (!getPin().equals(that.getPin())) return false;
        if (!getName().equals(that.getName())) return false;
        if (!getDesignation().equals(that.getDesignation())) return false;
        if (!getDepartment().equals(that.getDepartment())) return false;
        if (!getUnit().equals(that.getUnit())) return false;
        if (!getLeaveType().equals(that.getLeaveType())) return false;
        if (!getStartDate().equals(that.getStartDate())) return false;
        return getEndDate().equals(that.getEndDate());
    }

    @Override
    public int hashCode() {
        int result = getPin().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getDesignation().hashCode();
        result = 31 * result + getDepartment().hashCode();
        result = 31 * result + getUnit().hashCode();
        result = 31 * result + getLeaveType().hashCode();
        result = 31 * result + getStartDate().hashCode();
        result = 31 * result + getEndDate().hashCode();
        result = 31 * result + getDurationInDay();
        return result;
    }

    @Override
    public String toString() {
        return (
            "LeaveCutCSV{" +
            "pin='" +
            pin +
            '\'' +
            ", name='" +
            name +
            '\'' +
            ", designation='" +
            designation +
            '\'' +
            ", department='" +
            department +
            '\'' +
            ", unit='" +
            unit +
            '\'' +
            ", leaveType='" +
            leaveType +
            '\'' +
            ", startDate='" +
            startDate +
            '\'' +
            ", endDate='" +
            endDate +
            '\'' +
            ", durationInDay=" +
            durationInDay +
            '}'
        );
    }
}
