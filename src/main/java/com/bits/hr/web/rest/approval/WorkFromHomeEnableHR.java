package com.bits.hr.web.rest.approval;

import com.bits.hr.service.approvalProcess.WorkFromHomeEnableHRService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.EmployeeApprovalDTO;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/attendance-mgt/work-from-home/hr")
public class WorkFromHomeEnableHR {

    @Autowired
    private WorkFromHomeEnableHRService onlineAttendanceEntryHRService;

    @GetMapping("/employees")
    public List<EmployeeApprovalDTO> getListOfEmployee() throws URISyntaxException {
        return onlineAttendanceEntryHRService.getListOfEmployee();
    }

    @PutMapping("/enable-selected")
    public boolean enableSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to enable selected online attendances.");
        return onlineAttendanceEntryHRService.enableSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/disable-selected")
    public boolean disableSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to disable selected online attendances ");
        return onlineAttendanceEntryHRService.disabledSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("/enable-all")
    public boolean enableAll() throws URISyntaxException {
        log.debug("REST request to accept all online attendances ");
        List<Long> selectedIds = onlineAttendanceEntryHRService
            .getListOfEmployee()
            .stream()
            .map(EmployeeApprovalDTO::getId)
            .collect(Collectors.toList());
        return onlineAttendanceEntryHRService.enableSelected(selectedIds);
    }

    @PutMapping("/disable-all")
    public boolean disableAll() throws URISyntaxException {
        log.debug("REST request to disable all Online attendances ");
        List<Long> selectedIds = onlineAttendanceEntryHRService
            .getListOfEmployee()
            .stream()
            .map(EmployeeApprovalDTO::getId)
            .collect(Collectors.toList());
        return onlineAttendanceEntryHRService.disabledSelected(selectedIds);
    }
}
