package com.bits.hr.web.rest.importXL;

import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.importXL.flexSchedule.FlexScheduleImportXLService;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
@RequestMapping("/api/employee-mgt")
public class FlexScheduleImportResource {

    private static final String ENTITY_NAME = "FlexScheduleApplications";
    private static final String RESOURCE_NAME = "FlexScheduleImportResource";

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private FlexScheduleImportXLService flexScheduleImportXLService;

    @PostMapping("/import-flex-schedule-xlsx/")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        log.debug("REST request to upload flex schedule application import file");
        User user = currentEmployeeService.getCurrentUser().get();
        flexScheduleImportXLService.importFile(file);
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity.ok().body(true);
    }
}
