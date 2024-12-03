package com.bits.hr.web.rest.export;

import com.bits.hr.domain.Employee;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetExportXLService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TimeRangeDTO;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance-user")
public class UserAtsDataExportXLController {

    @Autowired
    private AttendanceTimeSheetExportXLService atsXlExportService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @PostMapping("/export-attendance-time-sheet")
    public void exportUserAtsDataInXL(@RequestBody TimeRangeDTO timeRangeDTO, HttpServletResponse response) throws IOException {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "Employee", "noEmployee");
        }

        ExportXLPropertiesDTO result = atsXlExportService.getAtsDataXLReport(timeRangeDTO, employeeOptional.get());

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
