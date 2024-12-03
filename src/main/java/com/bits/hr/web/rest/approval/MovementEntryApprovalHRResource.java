package com.bits.hr.web.rest.approval;

import com.bits.hr.service.approvalProcess.MovementEntryApprovalHRService;
import com.bits.hr.service.approvalProcess.MovementEntryApprovalLMService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.MovementEntryDTO;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance-mgt/movement-entry")
@Log4j2
public class MovementEntryApprovalHRResource {

    @Autowired
    private MovementEntryApprovalHRService movementEntryApprovalHRService;

    @Autowired
    private MovementEntryApprovalLMService movementEntryApprovalLMService;

    @GetMapping("/hr")
    public ResponseEntity<List<MovementEntryDTO>> getAllPending() {
        log.debug("REST request to get all pending movementEntry");
        List<MovementEntryDTO> result = movementEntryApprovalHRService.getAllPending();
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/approve-selected-hr")
    public Boolean approveSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approve selected pending movementEntry");
        return movementEntryApprovalHRService.approveSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/reject-selected-hr")
    public Boolean rejectSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to reject selected pending movementEntry");
        return movementEntryApprovalHRService.denySelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/approve-all-hr")
    public Boolean approveAll() {
        log.debug("REST request to approve all pending movementEntry");
        return movementEntryApprovalHRService.approveAll();
    }

    @PutMapping("/reject-all-hr")
    public Boolean rejectAll() {
        log.debug("REST request to reject all pending movementEntry");
        return movementEntryApprovalHRService.denyAll();
    }
}
