package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.ApprovalProcessService;
import com.bits.hr.service.approvalProcess.PendingApplicationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import com.bits.hr.service.dto.MovementEntryDTO;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/common/attendance-entry")
public class AttendanceApprovalLM {

    private static final String RESOURCE_NAME = "AttendanceApprovalLM";

    @Qualifier("attendanceEntryLMServiceImpl")
    @Autowired
    private ApprovalProcessService approvalProcessService;

    @Autowired
    PendingApplicationService pendingApplicationService;

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @GetMapping("/lm")
    public List<ManualAttendanceEntryDTO> getAllPending() throws URISyntaxException {
        log.debug("REST request to get pending attendance lm");
        long currentEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();

        // my subordinate list (pending manualAttendanceEntry application)
        List<ManualAttendanceEntryDTO> manualAttendanceEntryDTOList = pendingApplicationService.getAllPendingAttendanceLM(
            currentEmployeeId
        );

        List<ManualAttendanceEntryDTO> result = new ArrayList<>();
        result.addAll(manualAttendanceEntryDTOList);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return result;
    }

    @PutMapping("/approve-selected-lm")
    public boolean approveSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to Approve selected attendance lm");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);
        return approvalProcessService.approveSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/reject-selected-lm")
    public boolean rejectSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to reject selected attendance ");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);
        return approvalProcessService.denySelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/approve-all-lm")
    public boolean approveAll() throws URISyntaxException {
        log.debug("REST request to approve all pending attendance ");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);
        return approvalProcessService.approveAll();
    }

    @PutMapping("/reject-all-lm")
    public boolean rejectAll() throws URISyntaxException {
        log.debug("REST request to reject all pending attendance ");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);
        return approvalProcessService.denyAll();
    }
}
