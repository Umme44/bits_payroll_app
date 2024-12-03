package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.NomineeService;
import com.bits.hr.service.approvalProcess.NomineeApprovalService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.NomineeDTO;
import com.bits.hr.web.rest.ArrearPaymentResource;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/pf-mgt/gf-nominees-approval")
public class GfNomineeApprovalHRResource {

    private final Logger log = LoggerFactory.getLogger(ArrearPaymentResource.class);

    private static final String ENTITY_NAME = "GfNomineeApprovalHRResource";

    @Autowired
    private NomineeService nomineeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private NomineeApprovalService nomineeApprovalService;

    @GetMapping("/all-pending-approved-nominee-list")
    public ResponseEntity<List<NomineeDTO>> getAllApprovedOrPendingNomineeList(
        @RequestParam(required = false) String searchText,
        @RequestParam(required = false) NomineeType nomineeType,
        @RequestParam(required = false) Status status,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of All Approved Or Pending NomineeList");
        Page<NomineeDTO> nomineeDTOS = nomineeService.getAllApprovedOrPendingNominees(searchText, nomineeType, status, pageable);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, ENTITY_NAME);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), nomineeDTOS);
        return ResponseEntity.ok().headers(headers).body(nomineeDTOS.getContent());
    }

    @PostMapping("/approve")
    public ResponseEntity<Boolean> approveSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approved GF Nominees : {}", approvalDTO);
        Boolean result = nomineeApprovalService.approveSelected(approvalDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, ENTITY_NAME);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/reject")
    public ResponseEntity<Boolean> rejectSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to convert Approved GF Nominees into Pending: {}", approvalDTO);
        Boolean result = nomineeApprovalService.rejectSelected(approvalDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, ENTITY_NAME);
        return ResponseEntity.ok().body(result);
    }
}
