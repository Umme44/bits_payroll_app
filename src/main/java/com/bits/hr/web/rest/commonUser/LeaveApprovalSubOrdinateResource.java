package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import com.bits.hr.service.mapper.LeaveApplicationMapper;
import com.bits.hr.web.rest.LeaveApplicationResource;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.LeaveApplication}.
 */
@Deprecated/** alternative {@link com.bits.hr.web.rest.approval.LeaveApprovalLM} **/
@RestController
@RequestMapping("/api/common")
public class LeaveApprovalSubOrdinateResource {

    private static final String ENTITY_NAME = "leaveApplication";
    private final Logger log = LoggerFactory.getLogger(LeaveApplicationResource.class);
    private final LeaveApplicationService leaveApplicationService;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveApplicationMapper leaveApplicationMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public LeaveApprovalSubOrdinateResource(
        LeaveApplicationService leaveApplicationService,
        LeaveApplicationRepository leaveApplicationRepository,
        EmployeeRepository employeeRepository,
        LeaveApplicationMapper leaveApplicationMapper,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.leaveApplicationService = leaveApplicationService;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.employeeRepository = employeeRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping("/leave-applications-subordinate")
    public List<LeaveApplicationDTO> getAllLeaveApplications() {
        log.debug("REST request to get all LeaveApplications");
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        List<LeaveApplicationDTO> result = new ArrayList<>();
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            if (
                leaveApplication.getEmployee() != null &&
                leaveApplication.getEmployee().getReportingTo() != null &&
                leaveApplication.getEmployee().getReportingTo().getPin() != null &&
                leaveApplication.getEmployee().getReportingTo().getPin().equals(SecurityUtils.getCurrentEmployeePin())
            ) {
                result.add(leaveApplicationMapper.toDto(leaveApplication));
            }
        }
        return result;
    }

    @GetMapping("/leave-applications-subordinate/{id}")
    public ResponseEntity<LeaveApplicationDTO> getLeaveApplication(@PathVariable("id") Long id) {
        Optional<LeaveApplicationDTO> leaveApplicationDTOOptional = leaveApplicationService.findOne(id);
        if (!leaveApplicationDTOOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationDTOOptional.get();

        Long applicantId = leaveApplicationDTO.getEmployeeId();
        Employee applicant = employeeRepository.findById(applicantId).get();
        if (!applicant.getReportingTo().getPin().equals(SecurityUtils.getCurrentEmployeePin())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseUtil.wrapOrNotFound(Optional.of(leaveApplicationDTO));
    }

    @PutMapping("/leave-applications-subordinate/approve")
    public ResponseEntity<LeaveApplicationDTO> approveLeaveApplicationSubordinate(@RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Long applicantId = leaveApplicationDTO.getEmployeeId();
        Employee applicant = employeeRepository.findById(applicantId).get();
        if (!applicant.getReportingTo().getPin().equals(SecurityUtils.getCurrentEmployeePin())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LeaveApplicationDTO leaveApplicationDTO1 = leaveApplicationService.findOne(leaveApplicationDTO.getId()).get();

        leaveApplicationDTO1.setIsLineManagerApproved(true);

        LeaveApplicationDTO result = leaveApplicationService.save(leaveApplicationDTO1);
        publishEvent(result, EventType.APPROVED);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/leave-applications-subordinate/reject")
    public ResponseEntity<LeaveApplicationDTO> rejectLeaveApplicationSubordinate(@RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Long applicantId = leaveApplicationDTO.getEmployeeId();
        Employee applicant = employeeRepository.findById(applicantId).get();
        if (!applicant.getReportingTo().getPin().equals(SecurityUtils.getCurrentEmployeePin())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
