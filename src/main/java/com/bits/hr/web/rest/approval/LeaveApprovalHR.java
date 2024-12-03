package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.service.approvalProcess.ApprovalProcessService;
import com.bits.hr.service.approvalProcess.PendingApplicationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/attendance-mgt/leave-application")
public class LeaveApprovalHR {

    @Autowired
    PendingApplicationService pendingApplicationService;

    @Qualifier("leaveApplicationsHRServiceImpl")
    @Autowired
    ApprovalProcessService approvalProcessService;

    @GetMapping("/hr")
    public List<LeaveApplicationDTO> getAllPending() throws URISyntaxException {
        log.debug("REST request to get pending Leave Application HR");
        return pendingApplicationService.getAllPendingLeaveApplicationsHR();
    }

    @PutMapping("/approve-selected-hr")
    public boolean approveSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to Approve selected Leave Application HR");
        return approvalProcessService.approveSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/approve-hr/{id}")
    public boolean approveById(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to Approve Leave Application HR {}", id);

        List<Long> selected = new ArrayList<>();
        selected.add(id);

        return approvalProcessService.approveSelected(selected);
    }

    @PutMapping("/reject-hr/{id}")
    public boolean rejectedById(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to Reject Leave Application HR {}", id);

        List<Long> selected = new ArrayList<>();
        selected.add(id);

        return approvalProcessService.denySelected(selected);
    }

    @PutMapping("/reject-selected-hr")
    public boolean rejectSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to reject selected Leave Application ");
        return approvalProcessService.denySelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/approve-all-hr")
    public boolean approveAll() throws URISyntaxException {
        log.debug("REST request to approve all pending Leave Application ");
        return approvalProcessService.approveAll();
    }

    @PutMapping("/reject-all-hr")
    public boolean rejectAll() throws URISyntaxException {
        log.debug("REST request to reject all pending Leave Application ");
        return approvalProcessService.denyAll();
    }
}
