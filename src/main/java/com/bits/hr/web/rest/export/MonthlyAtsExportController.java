package com.bits.hr.web.rest.export;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetExportXLService;
import com.bits.hr.service.attendanceTimeSheet.MonthlyAttendanceTimeSheetGenerationService;
import com.bits.hr.service.attendanceTimeSheet.MonthlyAttendanceTimeSheetListDTO;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TimeRangeAndEmployeeIdListDTO;
import com.bits.hr.service.dto.TimeRangeDTO;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class MonthlyAtsExportController {

    private static final String RESOURCE_NAME = "MonthlyAtsExportController";

    public final MonthlyAttendanceTimeSheetGenerationService monthlyAttendanceTimeSheetGenerationService;
    private final AttendanceTimeSheetExportXLService atsXlExportService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public MonthlyAtsExportController(MonthlyAttendanceTimeSheetGenerationService monthlyAttendanceTimeSheetGenerationService, AttendanceTimeSheetExportXLService atsXlExportService, CurrentEmployeeService currentEmployeeService, EventLoggingPublisher eventLoggingPublisher) {
        this.monthlyAttendanceTimeSheetGenerationService = monthlyAttendanceTimeSheetGenerationService;
        this.atsXlExportService = atsXlExportService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @PostMapping("/api/payroll-mgt/monthly-ats-summary-export")
    public void exportMonthlyAtsSummaryReport(@RequestBody MonthlyAttendanceTimeSheetListDTO monthlyAts, HttpServletResponse response)
        throws IOException {
        ExportXLPropertiesDTO result = monthlyAttendanceTimeSheetGenerationService.getMonthlyAtsDataToExportExcel(
            monthlyAts.getMonthlyAttendanceTimeSheetList()
        );

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }

    @PostMapping("/api/payroll-mgt/simple-monthly-ats-summary-export")
    public void exportSimpleMonthlyAtsSummaryReport(@RequestBody TimeRangeAndEmployeeIdListDTO timeRangeAndEmployeeIdListDTO, HttpServletResponse response)
        throws IOException {
        TimeRangeDTO rangeDTO = new TimeRangeDTO();
        rangeDTO.setStartDate(timeRangeAndEmployeeIdListDTO.getStartDate());
        rangeDTO.setEndDate(timeRangeAndEmployeeIdListDTO.getEndDate());
        List<Long> employeeIdList = timeRangeAndEmployeeIdListDTO.getEmployeeIdList();

        ExportXLPropertiesDTO result = atsXlExportService.getAtsDataXLReport(rangeDTO, employeeIdList);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }
}
