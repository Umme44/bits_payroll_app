package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.RRFApprovalService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common/rrf/approval")
public class RRFApprovalResource {

    private final Logger log = LoggerFactory.getLogger(RRFApprovalResource.class);

    private static final String RESOURCE_NAME = "RRFApprovalResource";

    @Autowired
    private RRFApprovalService rrfApprovalService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @GetMapping("/pending-list")
    public List<RecruitmentRequisitionFormDTO> getAllPending() {
        log.debug("REST request to get pending RRF For Current Employee");
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        if (currentEmployeeId.isPresent()) {
            return rrfApprovalService.getPendingList(currentEmployeeId.get());
        } else {
            throw new BadRequestAlertException("", "", "noEmployee");
        }
    }

    @PutMapping("/approve-selected")
    public ResponseEntity<Boolean> approveSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approve pending RRF");
        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        boolean result = rrfApprovalService.approveSelected(approvalDTO.getListOfIds(), LocalDate.now(), employee);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/reject-selected")
    public ResponseEntity<Boolean> rejectSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to reject pending RRF");
        boolean result = rrfApprovalService.rejectSelected(approvalDTO.getListOfIds(), LocalDate.now());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }
}
