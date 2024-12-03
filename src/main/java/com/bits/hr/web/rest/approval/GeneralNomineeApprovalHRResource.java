package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.Nominee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.NomineeService;
import com.bits.hr.service.approvalProcess.NomineeApprovalService;
import com.bits.hr.service.approvalProcess.PfNomineeApprovalService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.NomineeDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.web.rest.ArrearPaymentResource;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pf-mgt/general-nominees-approval")
public class GeneralNomineeApprovalHRResource {

    private final Logger log = LoggerFactory.getLogger(ArrearPaymentResource.class);

    private static final String ENTITY_NAME = "GeneralNomineeApprovalHRResource";

    @Autowired
    private NomineeService nomineeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private NomineeApprovalService nomineeApprovalService;

    @PostMapping("/approve")
    public ResponseEntity<Boolean> approveSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approved General Nominees : {}", approvalDTO);
        Boolean result = nomineeApprovalService.approveSelected(approvalDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, ENTITY_NAME);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/reject")
    public ResponseEntity<Boolean> rejectSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to rejected General Nominees : {}", approvalDTO);
        Boolean result = nomineeApprovalService.rejectSelected(approvalDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, ENTITY_NAME);
        return ResponseEntity.ok().body(result);
    }
}
