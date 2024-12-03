package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.PRFApprovalService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common/prf/approval")
public class PRFApprovalResource {

    private final Logger log = LoggerFactory.getLogger(PRFApprovalResource.class);

    private static final String RESOURCE_NAME = "PRFApprovalResource";

    @Autowired
    private PRFApprovalService prfApprovalService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @GetMapping("/pending-list")
    public List<ProcReqMasterDTO> getAllPending() {
        log.debug("REST request to get pending PRF For Current Employee");
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        if (currentEmployeeId.isPresent()) {
            return prfApprovalService.getPendingList(currentEmployeeId.get());
        } else {
            throw new NoEmployeeProfileException();
        }
    }

    @PutMapping("/approve-by-dept-head")
    public ResponseEntity<Boolean> approvedByDepartmentHead(
        @RequestParam Boolean isCTOApprovalRequired,
        @RequestBody ApprovalDTO approvalDTO
    ) {
        log.debug("REST request to approve pending PRF");
        User user = currentEmployeeService.getCurrentUser().get();
        try {
            prfApprovalService.approvedByDepartmentHead(
                approvalDTO.getListOfIds(),
                currentEmployeeService.getCurrentEmployeeId().get(),
                isCTOApprovalRequired
            );
            eventLoggingPublisher.publishEvent(user, true, RequestMethod.PUT, RESOURCE_NAME);
            return ResponseEntity.ok().body(true);
        } catch (Exception ex) {
            eventLoggingPublisher.publishEvent(user, false, RequestMethod.PUT, RESOURCE_NAME);
            return ResponseEntity.ok().body(false);
        }
    }

    @PutMapping("/approve-selected")
    public ResponseEntity<Boolean> approveSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approve pending PRF");
        User user = currentEmployeeService.getCurrentUser().get();
        try {
            prfApprovalService.approvedByOthersExceptDeptHead(
                approvalDTO.getListOfIds(),
                currentEmployeeService.getCurrentEmployeeId().get()
            );
            eventLoggingPublisher.publishEvent(user, true, RequestMethod.PUT, RESOURCE_NAME);
            return ResponseEntity.ok().body(true);
        } catch (Exception ex) {
            eventLoggingPublisher.publishEvent(user, false, RequestMethod.PUT, RESOURCE_NAME);
            return ResponseEntity.ok().body(false);
        }
    }

    @PutMapping("/reject-selected")
    public ResponseEntity<Boolean> rejectSelected(@RequestBody ProcReqMasterDTO procReqMasterDTO) {
        log.debug("REST request to reject pending RRF");
        User user = currentEmployeeService.getCurrentUser().get();
        try {
            prfApprovalService.rejectSelected(
                procReqMasterDTO.getId(),
                currentEmployeeService.getCurrentEmployeeId().get(),
                procReqMasterDTO.getRejectionReason()
            );
            eventLoggingPublisher.publishEvent(user, true, RequestMethod.PUT, RESOURCE_NAME);
            return ResponseEntity.ok().body(true);
        } catch (Exception ex) {
            eventLoggingPublisher.publishEvent(user, false, RequestMethod.PUT, RESOURCE_NAME);
            return ResponseEntity.ok().body(false);
        }
    }
}
