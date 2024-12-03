package com.bits.hr.web.rest.export.leaveAttendance;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.xlExportHandling.LeaveManagement.LeaveBalanceExportService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class LeaveBalanceExportResource {

    @Autowired
    private LeaveBalanceExportService leaveBalanceExportService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/api/employee-mgt/export-leave-balance/{year}")
    public ResponseEntity<Void> exportLeaveBalanceYearWise(@PathVariable int year, HttpServletResponse response) throws IOException {
        LocalDate startDate = LocalDate.of(year, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(year, Month.DECEMBER, 31);
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=EmployeeGeneralSheet_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Employee> employeeList = employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(startDate, endDate);
        ExportXLPropertiesDTO result = leaveBalanceExportService.getDataForLeaveBalanceExport(year, employeeList);

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
        return ResponseEntity.ok().build();
    }
}
