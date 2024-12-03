package com.bits.hr.web.rest.ams;

import com.bits.hr.service.attendanceSync.AutoLeaveCuttingService;
import com.bits.hr.service.attendanceSync.AutoLeaveCuttingSummaryReportService;
import com.bits.hr.service.attendanceSync.AutoLeaveCuttingSummaryService;
import com.bits.hr.service.attendanceSync.GenerateAttendanceSummaryService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.dto.TimeRangeDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance-mgt")
public class AttendanceSyncResource {

    @Autowired
    private AutoLeaveCuttingService autoLeaveCuttingService;

    @Autowired
    private AutoLeaveCuttingSummaryService autoLeaveCuttingSummaryService;

    @Autowired
    private AutoLeaveCuttingSummaryReportService autoLeaveCuttingSummaryReportService;

    @Autowired
    private GenerateAttendanceSummaryService generateAttendanceSummaryService;

    @PostMapping("/auto-leave-cut")
    public boolean autoLeaveCut(@RequestBody TimeRangeDTO timeRangeDTO) throws Exception {
        return autoLeaveCuttingService.cutAutoLeave(timeRangeDTO.getStartDate(), timeRangeDTO.getEndDate());
    }

    @PostMapping(value = "/auto-leave-cut-summary-report", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] autoLeaveCutSummaryReport(@RequestBody TimeRangeDTO timeRangeDTO) throws Exception {
        File file = autoLeaveCuttingSummaryReportService
            .generateCSV(timeRangeDTO.getStartDate(), timeRangeDTO.getEndDate())
            .orElseThrow(() -> new RuntimeException("Could not generate csv report"));
        InputStream in = new FileInputStream(file);
        return IOUtils.toByteArray(in);
    }

    @PostMapping("/auto-leave-cut-summary")
    public List<LeaveApplicationDTO> autoLeaveCutSummary(@RequestBody TimeRangeDTO timeRangeDTO) throws Exception {
        return autoLeaveCuttingSummaryService.GenerateSummary(timeRangeDTO.getStartDate(), timeRangeDTO.getEndDate());
    }

    @GetMapping("/generate-attendance-summary/{year}/{month}")
    public boolean generateAttendanceSummary(@PathVariable int year, @PathVariable int month) throws Exception {
        return generateAttendanceSummaryService.generateAndSave(year, month);
    }
}
