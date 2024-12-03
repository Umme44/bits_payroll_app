package com.bits.hr.service.approvalProcess.helperObjects;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.ManualAttendanceEntry;
import lombok.Data;

@Data
public class AttendanceHelper {

    ManualAttendanceEntry manualAttendanceEntry;
    AttendanceEntry attendanceEntry;
}
