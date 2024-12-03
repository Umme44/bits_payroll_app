package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.FlexScheduleApplicationApprovalHRService;
import com.bits.hr.service.approvalProcess.FlexScheduleApplicationApprovalLMService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("api/attendance-mgt/flex-schedule-applications-approval-hr")
public class FlexScheduleApplicationApprovalHRResource {

    @Autowired
    private FlexScheduleApplicationApprovalHRService flexScheduleApplicationApprovalHRService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    private static final String RESOURCE_NAME = "FlexScheduleApplicationApprovalHRResource";

    @GetMapping("/pending")
    public List<FlexScheduleApplicationDTO> getSubOrdinateEmployees() {
        log.debug("REST request to pending subordinate flex schedule applications.");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return flexScheduleApplicationApprovalHRService.getAllPending();
    }

    @PutMapping("/approve-selected")
    public boolean enableSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approve selected flex schedule applications.");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, approvalDTO, RequestMethod.PUT, RESOURCE_NAME);
        try {
            flexScheduleApplicationApprovalHRService.approveSelected(approvalDTO.getListOfIds(), Instant.now(), user);
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @PutMapping("/deny-selected")
    public boolean denySelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to deny selected flex schedule applications.");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, approvalDTO, RequestMethod.PUT, RESOURCE_NAME);
        try {
            flexScheduleApplicationApprovalHRService.denySelected(approvalDTO.getListOfIds(), Instant.now(), user);
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
