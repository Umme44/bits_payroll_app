package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.enumeration.AttendanceStatus;
import java.util.List;
import java.util.Objects;

public class MonthlyAttendanceTimeSheetDTO {

    private long employeeId;
    private String name;
    private String pin;
    private List<AttendanceTimeSheetMini> attendanceTimeSheetMiniList;

    public MonthlyAttendanceTimeSheetDTO() {}

    public MonthlyAttendanceTimeSheetDTO(long employeeId, String name, String pin, List<AttendanceTimeSheetMini> attendanceTimeSheetMinis) {
        this.employeeId = employeeId;
        this.name = name;
        this.pin = pin;
        this.attendanceTimeSheetMiniList = attendanceTimeSheetMinis;
    }

    public int calculateWorkingDays() {
        if (attendanceTimeSheetMiniList.size() > 31) return 0;
        return (int) attendanceTimeSheetMiniList.stream().filter(x -> x.getAttendanceStatus() == AttendanceStatus.PRESENT).count();
    }

    public int calculateAbsentDays() {
        if (attendanceTimeSheetMiniList.size() > 31) return 0;
        return (int) attendanceTimeSheetMiniList.stream().filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT).count();
    }

    public int calculateLeaveDays() {
        if (attendanceTimeSheetMiniList.size() > 31) return 0;
        return (int) attendanceTimeSheetMiniList.stream().filter(x -> x.getAttendanceStatus() == AttendanceStatus.LEAVE).count();
    }

    public int calculateFractionDays() {
        if (attendanceTimeSheetMiniList.size() > 31) return 0;
        return (int) attendanceTimeSheetMiniList.stream().filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT).count();
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public List<AttendanceTimeSheetMini> getAttendanceTimeSheetMiniList() {
        return attendanceTimeSheetMiniList;
    }

    public void setAttendanceTimeSheetMiniList(List<AttendanceTimeSheetMini> attendanceTimeSheetMiniList) {
        this.attendanceTimeSheetMiniList = attendanceTimeSheetMiniList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyAttendanceTimeSheetDTO that = (MonthlyAttendanceTimeSheetDTO) o;
        return (
            getEmployeeId() == that.getEmployeeId() &&
            Objects.equals(getName(), that.getName()) &&
            Objects.equals(getPin(), that.getPin()) &&
            Objects.equals(getAttendanceTimeSheetMiniList(), that.getAttendanceTimeSheetMiniList())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getName(), getPin(), getAttendanceTimeSheetMiniList());
    }

    @Override
    public String toString() {
        return (
            "MonthlyAttendanceTimeSheetDTO{" +
            "employeeId=" +
            employeeId +
            ", name='" +
            name +
            '\'' +
            ", pin='" +
            pin +
            '\'' +
            ", attendanceTimeSheetMiniDTOS=" +
            attendanceTimeSheetMiniList +
            '}'
        );
    }
}
