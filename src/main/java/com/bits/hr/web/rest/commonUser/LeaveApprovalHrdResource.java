package com.bits.hr.web.rest.commonUser;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import com.bits.hr.web.rest.LeaveApplicationResource;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Employee}.
 */
@RestController
@RequestMapping("/api/attendance-mgt")
public class LeaveApprovalHrdResource {

    private static final String ENTITY_NAME = "leaveApplication";
    private final Logger log = LoggerFactory.getLogger(LeaveApplicationResource.class);
    private final LeaveApplicationService leaveApplicationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LeaveApprovalHrdResource(LeaveApplicationService leaveApplicationService, ApplicationEventPublisher applicationEventPublisher) {
        this.leaveApplicationService = leaveApplicationService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/leave-applications-hrd")
    public ResponseEntity<List<LeaveApplicationDTO>> getAllLeaveApplications(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of LeaveApplications");
        Page<LeaveApplicationDTO> page = leaveApplicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PutMapping("/leave-applications-hrd/approve")
    public ResponseEntity<LeaveApplicationDTO> approveLeaveApplicationSubordinate(@RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        LeaveApplicationDTO leaveApplicationDTO1 = leaveApplicationService.findOne(leaveApplicationDTO.getId()).get();

        leaveApplicationDTO1.setIsHRApproved(true);

        LeaveApplicationDTO result = leaveApplicationService.save(leaveApplicationDTO1);
        publishEvent(result, EventType.APPROVED);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/leave-applications-hrd/reject")
    public ResponseEntity<LeaveApplicationDTO> rejectLeaveApplicationSubordinate(@RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String rejectionComment = leaveApplicationDTO.getRejectionComment();
        LeaveApplicationDTO leaveApplicationDTO1 = leaveApplicationService.findOne(leaveApplicationDTO.getId()).get();
        leaveApplicationDTO1.setIsHRApproved(false);
        leaveApplicationDTO1.setIsLineManagerApproved(false);
        leaveApplicationDTO1.setIsRejected(true);
        leaveApplicationDTO1.setRejectionComment(rejectionComment);
        LeaveApplicationDTO result = leaveApplicationService.save(leaveApplicationDTO1);
        publishEvent(result, EventType.REJECTED);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private void publishEvent(LeaveApplicationDTO leaveApplication, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        LeaveApplicationEvent leaveApplicationEvent = new LeaveApplicationEvent(this, leaveApplication, event);
        applicationEventPublisher.publishEvent(leaveApplicationEvent);
    }
}
