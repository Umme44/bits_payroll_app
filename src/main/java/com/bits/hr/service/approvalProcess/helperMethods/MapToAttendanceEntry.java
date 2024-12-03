package com.bits.hr.service.approvalProcess.helperMethods;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.ManualAttendanceEntry;

public class MapToAttendanceEntry {

    public static AttendanceEntry map(ManualAttendanceEntry manualAttendanceEntry) {
        AttendanceEntry attendanceEntry = new AttendanceEntry();
        attendanceEntry.employee(manualAttendanceEntry.getEmployee());
        attendanceEntry.setDate(manualAttendanceEntry.getDate());
        attendanceEntry.setInTime(manualAttendanceEntry.getInTime());
        attendanceEntry.setOutTime(manualAttendanceEntry.getOutTime());
        attendanceEntry.setInNote(manualAttendanceEntry.getInNote());
        attendanceEntry.setOutNote(manualAttendanceEntry.getOutNote());
        return attendanceEntry;
    }
}
