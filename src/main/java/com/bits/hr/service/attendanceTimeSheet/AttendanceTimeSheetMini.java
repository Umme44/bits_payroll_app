package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.enumeration.AttendanceStatus;
import java.time.LocalDate;
import java.util.Objects;

public class AttendanceTimeSheetMini {

    private LocalDate date;
    private AttendanceStatus attendanceStatus;

    public AttendanceTimeSheetMini() {}

    public AttendanceTimeSheetMini(LocalDate date, AttendanceStatus attendanceStatus) {
        this.date = date;
        this.attendanceStatus = attendanceStatus;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceTimeSheetMini that = (AttendanceTimeSheetMini) o;
        return Objects.equals(getDate(), that.getDate()) && getAttendanceStatus() == that.getAttendanceStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getAttendanceStatus());
    }

    @Override
    public String toString() {
        return "AttendanceTimeSheetMiniDTO{" + "date=" + date + ", attendanceStatus=" + attendanceStatus + '}';
    }
}
