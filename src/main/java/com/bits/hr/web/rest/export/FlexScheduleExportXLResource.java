package com.bits.hr.web.rest.export;

import com.bits.hr.domain.TimeSlot;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.xlExportHandling.FlexSchedulerExportService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class FlexScheduleExportXLResource {

    private static final String ENTITY_NAME = "FlexSchedule";
    private static final String RESOURCE_NAME = "FlexScheduleExportXLResource";

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private FlexSchedulerExportService flexSchedulerExportService;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @GetMapping("/api/payroll-mgt/flex-schedule-applications-export-report")
    public void exportFlexScheduleApplicationReport(
        @RequestParam(required = false) Long employeeId,
        @RequestParam(required = false) List<Long> timeSlotIdList,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @RequestParam(required = false) Status status,
        HttpServletResponse response
    ) throws IOException {
        log.debug("REST request to get flex schedule application missing employee export file");
        User user = currentEmployeeService.getCurrentUser().get();

        if (timeSlotIdList == null || timeSlotIdList.isEmpty()) {
            timeSlotIdList = timeSlotRepository.findAll().stream().map(TimeSlot::getId).collect(Collectors.toList());
        }
        ExportXLPropertiesDTO result = flexSchedulerExportService.exportFlexScheduleApplicationsReport(
            employeeId,
            timeSlotIdList,
            startDate,
            endDate,
            status
        );

        if (result.getTableDataListOfList().size() == 0) {
            throw new BadRequestAlertException("No Data Found!", "", "");
        }
        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );

        exportXL.export(response);
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
    }

    @GetMapping("/api/payroll-mgt/flex-schedule-export")
    public void exportFlexSchedule(HttpServletResponse response) throws IOException {
        log.debug("REST request to get flex schedule application export file");
        User user = currentEmployeeService.getCurrentUser().get();

        ExportXLPropertiesDTO result = flexSchedulerExportService.exportFlexSchedule();
        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );

        exportXL.export(response);
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
    }

    @GetMapping("/api/payroll-mgt/missing-flex-schedule-export")
    public void exportMissingFlexSchedule(HttpServletResponse response) throws IOException {
        log.debug("REST request to get flex schedule application missing employee export file");
        User user = currentEmployeeService.getCurrentUser().get();
        ExportXLPropertiesDTO result = flexSchedulerExportService.exportMissingFlexSchedule();

        if (result.getTableDataListOfList().size() == 0) {
            throw new BadRequestAlertException("No Data Found!", "", "");
        }
        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );

        exportXL.export(response);
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
    }
}
