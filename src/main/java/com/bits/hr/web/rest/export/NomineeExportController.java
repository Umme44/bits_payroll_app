package com.bits.hr.web.rest.export;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.NomineeUpdateEvent;
import com.bits.hr.service.xlExportHandling.EmployeeSalaryXlsxExportService;
import com.bits.hr.service.xlExportHandling.NomineeExportService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class NomineeExportController {

    private static final String RESOURCE_NAME = "NomineeExportController";

    @Autowired
    private NomineeExportService nomineeExportService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/api/payroll-mgt/export/nominee")
    public void nomineeExportToExcel(HttpServletResponse response) throws IOException {
        ExportXLPropertiesDTO result = nomineeExportService.exportNominees();

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        exportXL.export(response);
    }
}
