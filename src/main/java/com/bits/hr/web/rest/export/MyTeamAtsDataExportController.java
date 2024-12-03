package com.bits.hr.web.rest.export;

import com.bits.hr.domain.Employee;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.MyTeamService;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetExportXLService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TimeRangeAndEmployeeIdDTO;
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
@RequestMapping("api/common")
public class MyTeamAtsDataExportController {

    @Autowired
    private AttendanceTimeSheetExportXLService atsXlExportService;

    @Autowired
    private MyTeamService myTeamService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/export-my-team-ats")
    public void exportUserAtsDataInXL(@RequestBody TimeRangeAndEmployeeIdDTO timeRangeAndEmployeeIdDTO, HttpServletResponse response)
        throws IOException {
        // Line Manager [Current Employee]
        Optional<Employee> currentEmployeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!currentEmployeeOptional.isPresent()) {
            throw new BadRequestAlertException("No employee profile is associated with you. Please contact HR", "Employee", "noEmployee");
        }

        // Selected Team Member
        Optional<Employee> employeeOptional = employeeRepository.findById(timeRangeAndEmployeeIdDTO.getEmployeeId());
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "Employee", "idnull");
        }

        // Check if selected Employee is reporting to the Line Manager.
        // CurrentEmployee = Line manager
        // EmployeeId = Subordinate
        boolean isMyTeamMember = myTeamService.isMyTeamMember(currentEmployeeOptional.get(), employeeOptional.get().getId());
        if (!isMyTeamMember) {
            throw new BadRequestAlertException("Selected Employee Is Not Your Subordinate.", "Employee", "notSubordinate");
        }

        TimeRangeDTO rangeDTO = new TimeRangeDTO();
        rangeDTO.setStartDate(timeRangeAndEmployeeIdDTO.getStartDate());
        rangeDTO.setEndDate(timeRangeAndEmployeeIdDTO.getEndDate());

        ExportXLPropertiesDTO result = atsXlExportService.getAtsDataXLReport(rangeDTO, employeeOptional.get());

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
