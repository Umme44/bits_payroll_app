package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.WorkFromHomeEnableLMService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.EmployeeApprovalDTO;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/common/work-from-home/lm")
public class WorkFromHomeEnableLM {

    @Autowired
    private WorkFromHomeEnableLMService onlineAttendanceEntryLMService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @GetMapping("/sub-ordinate-employees")
    public List<EmployeeApprovalDTO> getSubOrdinateEmployees() throws URISyntaxException {
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "WorkFromHomeEnableLM");
        return onlineAttendanceEntryLMService.getSubOrdinateEmployees();
    }

    @PutMapping("/enable-selected")
    public boolean enableSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to enable selected online attendances.");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "WorkFromHomeEnableLM");
        return onlineAttendanceEntryLMService.enableSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/disable-selected")
    public boolean disableSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to disable selected online attendances ");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "WorkFromHomeEnableLM");
        return onlineAttendanceEntryLMService.disableSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/enable-all")
    public boolean enableAll() throws URISyntaxException {
        log.debug("REST request to accept all online attendances ");
        List<Long> selectedIds = onlineAttendanceEntryLMService
            .getSubOrdinateEmployees()
            .stream()
            .map(EmployeeApprovalDTO::getId)
            .collect(Collectors.toList());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "WorkFromHomeEnableLM");
        return onlineAttendanceEntryLMService.enableSelected(selectedIds);
    }

    @PutMapping("/disable-all")
    public boolean disableAll() throws URISyntaxException {
        log.debug("REST request to disable all Online attendances ");
        List<Long> selectedIds = onlineAttendanceEntryLMService
            .getSubOrdinateEmployees()
            .stream()
            .map(EmployeeApprovalDTO::getId)
            .collect(Collectors.toList());
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "WorkFromHomeEnableLM");
        return onlineAttendanceEntryLMService.disableSelected(selectedIds);
    }
}
