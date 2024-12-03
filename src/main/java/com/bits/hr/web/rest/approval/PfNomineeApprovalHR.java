package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.PfNomineeApprovalService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.web.rest.NomineeCommonResource;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pf-mgt/pf-nominees-approval")
public class PfNomineeApprovalHR {

    private final Logger log = LoggerFactory.getLogger(NomineeCommonResource.class);

    private static final String ENTITY_NAME = "PfNomineeApprovalHR";

    @Autowired
    private PfNomineeApprovalService pfNomineeApprovalService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @GetMapping("/pending-list")
    public ResponseEntity<List<PfNomineeDTO>> getPendingPfNominee() {
        log.debug("REST request to rejected pf Nominees");
        List<PfNomineeDTO> pfNomineeDTOList = pfNomineeApprovalService.getPendingPfNomineeList();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, ENTITY_NAME);
        return ResponseEntity.ok().body(pfNomineeDTOList);
    }

    @GetMapping("/approved-list")
    public ResponseEntity<List<PfNomineeDTO>> getApprovedPfNominee() {
        log.debug("REST request to get approved pf Nominees");
        List<PfNomineeDTO> pfNomineeDTOList = pfNomineeApprovalService.getApprovedPfNomineeList();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, ENTITY_NAME);
        return ResponseEntity.ok().body(pfNomineeDTOList);
    }

    @PostMapping("/approve")
    public ResponseEntity<Boolean> approveSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approved Pf Nominees : {}", approvalDTO);
        Boolean result = pfNomineeApprovalService.approveSelected(approvalDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, ENTITY_NAME);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/reject")
    public ResponseEntity<Boolean> rejectSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to reject Pf Nominees : {}", approvalDTO);
        Boolean result = pfNomineeApprovalService.rejectSelected(approvalDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, ENTITY_NAME);
        return ResponseEntity.ok().body(result);
    }
}
