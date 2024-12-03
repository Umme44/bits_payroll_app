package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.ams.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attendance-mgt")
public class AmsImportController {

    @Autowired
    private LeaveBalanceImportService leaveBalanceImportService;

    @Autowired
    private AttendanceImportService attendanceImportService;

    @Autowired
    private GarbageAttendanceImportService garbageAttendanceImportService;

    @Autowired
    private FlexScheduleImportService flexScheduleImportService;

    @Autowired
    private MovementEntryImportService movementEntryImportService;

    @PostMapping("/leave-balance/import-leave-balance-xlsx/")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = leaveBalanceImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/import-attendance-entry/")
    public ResponseEntity<Boolean> importAttendanceEntry(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = attendanceImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/import-garbage-attendance-xlsx")
    public boolean importGarbageAttendance(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = garbageAttendanceImportService.importFile(file);
        return hasDone;
    }

    @PostMapping("/import-flex-schedule-xlsx")
    public boolean importFlexSchedule(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = flexScheduleImportService.importFile(file);
        return hasDone;
    }

    @PostMapping("/import-movement-entry-xlsx")
    public boolean importMovementEntry(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = movementEntryImportService.importFile(file);
        return hasDone;
    }
}
