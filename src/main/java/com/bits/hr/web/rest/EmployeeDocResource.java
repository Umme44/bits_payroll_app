package com.bits.hr.web.rest;

import com.bits.hr.service.EmployeeDocService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/payroll-mgt/employee-doc")
public class EmployeeDocResource {

    @Autowired
    private EmployeeDocService employeeDocService;

    @GetMapping("/update-all-applications-status-to-pending")
    public ResponseEntity<Boolean> updateEmployeeDocStatusToPending() {
        log.debug("REST Request get EmployeeSalaryDTO by salary certificate id");
        boolean result = employeeDocService.updateEmployeeDocStatusToPending();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/export-employee-docs-detailed-report")
    public void exportEmployeeDocsDetailedReport(int year, int month, HttpServletResponse response) throws IOException {
        log.debug("Request to export employee docs report");
        ExportXLPropertiesDTO result = employeeDocService.exportEmployeeDocsDetailedReport(year, month);

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
