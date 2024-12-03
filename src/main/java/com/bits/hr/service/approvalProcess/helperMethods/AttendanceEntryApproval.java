package com.bits.hr.service.approvalProcess.helperMethods;

import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.service.approvalProcess.helperObjects.AttendanceHelper;

public class AttendanceEntryApproval {

    public static AttendanceHelper processToApproveLM(ManualAttendanceEntry manualAttendanceEntry) {
        AttendanceHelper attendanceHelper = new AttendanceHelper();

        manualAttendanceEntry.setIsLineManagerApproved(true);
        manualAttendanceEntry.setIsHRApproved(false);
        manualAttendanceEntry.setIsRejected(false);

        attendanceHelper.setManualAttendanceEntry(manualAttendanceEntry);
        attendanceHelper.setAttendanceEntry(MapToAttendanceEntry.map(manualAttendanceEntry));
        return attendanceHelper;
    }

    public static AttendanceHelper processToApproveHR(ManualAttendanceEntry manualAttendanceEntry) {
        AttendanceHelper attendanceHelper = new AttendanceHelper();
        manualAttendanceEntry.setIsLineManagerApproved(false);
        manualAttendanceEntry.setIsHRApproved(true);
        manualAttendanceEntry.setIsRejected(false);
        attendanceHelper.setManualAttendanceEntry(manualAttendanceEntry);
        attendanceHelper.setAttendanceEntry(MapToAttendanceEntry.map(manualAttendanceEntry));
        return attendanceHelper;
    }

    public static ManualAttendanceEntry processToRejectLM(ManualAttendanceEntry manualAttendanceEntry) {
        manualAttendanceEntry.setIsLineManagerApproved(false);
        manualAttendanceEntry.setIsHRApproved(false);
        manualAttendanceEntry.setIsRejected(true);
        manualAttendanceEntry.setRejectionComment("Line Manager Rejected");
        return manualAttendanceEntry;
    }

    public static ManualAttendanceEntry processToRejectHR(ManualAttendanceEntry manualAttendanceEntry) {
        manualAttendanceEntry.setIsLineManagerApproved(false);
        manualAttendanceEntry.setIsHRApproved(false);
        manualAttendanceEntry.setIsRejected(true);
        manualAttendanceEntry.setRejectionComment("HR Rejected");
        return manualAttendanceEntry;
    }
}
