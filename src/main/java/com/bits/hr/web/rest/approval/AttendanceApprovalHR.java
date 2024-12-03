package com.bits.hr.web.rest.approval;

import com.bits.hr.service.approvalProcess.ApprovalProcessService;
import com.bits.hr.service.approvalProcess.PendingApplicationService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import java.net.URISyntaxException;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/attendance-mgt/attendance-entry")
public class AttendanceApprovalHR {

    @Qualifier("attendanceEntryHRServiceImpl")
    @Autowired
    private ApprovalProcessService approvalProcessService;

    @Autowired
    private PendingApplicationService pendingApplicationService;

    @GetMapping("/hr")
    public List<ManualAttendanceEntryDTO> getAllPending() throws URISyntaxException {
        log.debug("REST request to get pending attendance HR");
        return pendingApplicationService.getAllPendingAttendanceHR();
    }

    @PutMapping("/approve-selected-hr")
    public boolean approveSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to Approve selected attendance HR");
        return approvalProcessService.approveSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/reject-selected-hr")
    public boolean rejectSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to reject selected attendance ");
        return approvalProcessService.denySelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/approve-all-hr")
    public boolean approveAll() throws URISyntaxException {
        log.debug("REST request to approve all pending attendance ");
        return approvalProcessService.approveAll();
    }

    @PutMapping("/reject-all-hr")
    public boolean rejectAll() throws URISyntaxException {
        log.debug("REST request to reject all pending attendance ");
        return approvalProcessService.denyAll();
    }
}
