package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.MovementEntryApprovalLMService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.dto.MovementEntryDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common/movement-entry")
@Log4j2
public class MovementEntryApprovalLMResource {

    private static final String RESOURCE_NAME = "MovementEntryApprovalLMResource";
    private final MovementEntryApprovalLMService movementEntryApprovalLMService;

    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public MovementEntryApprovalLMResource(
        MovementEntryApprovalLMService movementEntryApprovalLMServiceImpl,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.movementEntryApprovalLMService = movementEntryApprovalLMServiceImpl;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @GetMapping("/lm")
    public ResponseEntity<List<MovementEntryDTO>> getAllPendingLM() {
        log.debug("REST request to get all pending movement approvals for LM");
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        // my subordinate list (pending movementEntry application)
        List<MovementEntryDTO> movementEntryDTOList = movementEntryApprovalLMService.getAllPending(currentEmployeeId.get());

        List<MovementEntryDTO> result = new ArrayList<>();
        result.addAll(movementEntryDTOList);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/approve-selected-lm")
    public Boolean approveSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approve selected pending movementEntry");
        User user = currentEmployeeService.getCurrentUser().get();
        Boolean status = movementEntryApprovalLMService.approveSelected(approvalDTO.getListOfIds(), user);
        eventLoggingPublisher.publishEvent(user, status, RequestMethod.PUT, RESOURCE_NAME);
        return status;
    }

    @PutMapping("/reject-selected-lm")
    public Boolean rejectSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to reject selected pending movementEntry");
        User user = currentEmployeeService.getCurrentUser().get();
        Boolean status = movementEntryApprovalLMService.denySelected(approvalDTO.getListOfIds(), user);
        eventLoggingPublisher.publishEvent(user, status, RequestMethod.PUT, RESOURCE_NAME);
        return status;
    }
}
