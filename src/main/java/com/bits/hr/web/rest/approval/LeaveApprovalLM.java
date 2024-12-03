package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.approvalProcess.ApprovalProcessService;
import com.bits.hr.service.approvalProcess.PendingApplicationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@Log4j2
@RequestMapping("/api/common/leave-application")
public class LeaveApprovalLM {

    @Autowired
    private PendingApplicationService pendingApplicationService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Qualifier("leaveApplicationsLMServiceImpl")
    @Autowired
    private ApprovalProcessService approvalProcessService;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @GetMapping("lm")
    public List<LeaveApplicationDTO> getAllPending() throws URISyntaxException {
        log.debug("REST request to get pending leave lm");
        long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "LeaveApprovalLM");

        // my subordinate list (pending leave application)
        List<LeaveApplicationDTO> leaveApplicationMySubordinateList = pendingApplicationService.getAllPendingLeaveApplicationsLM(
            employeeId
        );

        List<LeaveApplicationDTO> finalPendingList = new ArrayList<>();
        finalPendingList.addAll(leaveApplicationMySubordinateList);
        return finalPendingList;
    }

    @PutMapping("approve-selected-lm")
    public boolean approveSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to Approve selected Leave Application lm");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "LeaveApprovalLM");
        return approvalProcessService.approveSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("reject-selected-lm")
    public boolean rejectSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to reject selected Leave Application ");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "LeaveApprovalLM");
        return approvalProcessService.denySelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/approve-lm/{id}")
    public boolean approveById(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to Approve Leave Application {}", id);

        List<Long> selected = new ArrayList<>();
        selected.add(id);

        return approvalProcessService.approveSelected(selected);
    }

    @PutMapping("/reject-lm/{id}")
    public boolean rejectedById(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to Reject Leave Application {}", id);

        List<Long> selected = new ArrayList<>();
        selected.add(id);

        return approvalProcessService.denySelected(selected);
    }

    @PutMapping("approve-all-lm")
    public boolean approveAll() throws URISyntaxException {
        log.debug("REST request to approve all pending Leave Application ");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "LeaveApprovalLM");
        return approvalProcessService.approveAll();
    }

    @PutMapping("reject-all-lm")
    public boolean rejectAll() throws URISyntaxException {
        log.debug("REST request to reject all pending Leave Application ");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "LeaveApprovalLM");
        return approvalProcessService.denyAll();
    }

    @GetMapping("approved-by-me")
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApprovedByMe(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Optional<Long> currentUserId = currentEmployeeService.getCurrentUserId();
        if (!currentUserId.isPresent()) throw new BadRequestAlertException(
            "No employee profile is associated with you. Please contact HR",
            "userLeaveApplication",
            "noEmployee"
        );
        Page<LeaveApplicationDTO> result = leaveApplicationService.findApprovedLeaveByUserId(pageable, currentUserId.get());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "LeaveApprovalLM");
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("approved-by-me-filter")
    public ResponseEntity<List<LeaveApplicationDTO>> getLeaveApprovedByMeFilter(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam String searchText
    ) {
        Optional<Long> currentUserId = currentEmployeeService.getCurrentUserId();
        if (!currentUserId.isPresent()) throw new BadRequestAlertException(
            "No employee profile is associated with you. Please contact HR",
            "userLeaveApplication",
            "noEmployee"
        );
        Page<LeaveApplicationDTO> result = leaveApplicationService.findApprovedLeaveByFiltering(pageable, currentUserId.get(), searchText);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "LeaveApprovalLM");
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }
}
