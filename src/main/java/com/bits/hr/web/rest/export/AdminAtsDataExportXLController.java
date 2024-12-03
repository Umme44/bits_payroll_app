package com.bits.hr.web.rest.export;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetExportXLService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TimeRangeAndEmployeeIdDTO;
import com.bits.hr.service.dto.TimeRangeDTO;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance-mgt")
public class AdminAtsDataExportXLController {

    private static final String RESOURCE_NAME = "AdminAtsDataExportXLController";

    @Autowired
    private AttendanceTimeSheetExportXLService atsXlExportService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @PostMapping("/export-attendance-time-sheet")
    public void exportUserAtsDataInXL(@RequestBody TimeRangeAndEmployeeIdDTO timeRangeAndEmployeeIdDTO, HttpServletResponse response)
        throws IOException {
        Optional<Employee> employeeOptional = employeeRepository.findById(timeRangeAndEmployeeIdDTO.getEmployeeId());

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "Employee", "noEmployee");
        }

        TimeRangeDTO rangeDTO = new TimeRangeDTO();
        rangeDTO.setStartDate(timeRangeAndEmployeeIdDTO.getStartDate());
        rangeDTO.setEndDate(timeRangeAndEmployeeIdDTO.getEndDate());
        ExportXLPropertiesDTO result = atsXlExportService.getAtsDataXLReport(rangeDTO, employeeOptional.get());
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

    @GetMapping("/export-monthly-attendance-time-sheet-report")
    public void exportMonthlyAtsReport(
        @RequestParam String searchText,
        @RequestParam Long departmentId,
        @RequestParam Long designationId,
        @RequestParam Long unitId,
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate,
        HttpServletResponse response
    ) throws IOException {
        ExportXLPropertiesDTO result = atsXlExportService.generateMonthlyAtsReport(
            searchText,
            departmentId,
            designationId,
            unitId,
            startDate,
            endDate
        );

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getSubTitleList(),
            result.getTablePreHeaderList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );
        exportXL.export(response);
    }
}
