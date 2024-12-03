package com.bits.hr.web.rest.approval;

import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.approvalProcess.FlexScheduleApprovalService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FlexScheduleApprovalDTO;
import com.bits.hr.service.dto.FlexScheduleDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.FlexScheduleApprovalEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class FlexScheduleApprovalResource {

    @Autowired
    FlexScheduleApprovalService flexScheduleApprovalService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    @PutMapping("/api/attendance-mgt/flex-schedule-approval")
    public boolean updateSelectedFlexScheduleHR(@RequestBody FlexScheduleApprovalDTO flexScheduleApprovalDTO) {
        boolean result = flexScheduleApprovalService.updateSelectedSchedule(flexScheduleApprovalDTO);
        return result;
    }

    @PutMapping("/api/common/flex-schedule-approval")
    public boolean updateSelectedFlexScheduleLM(@RequestBody FlexScheduleApprovalDTO flexScheduleApprovalDTO) {
        log.debug("REST request to update selected sub ordinate employee schedule");
        boolean result = flexScheduleApprovalService.updateSelectedScheduleLM(flexScheduleApprovalDTO);
        return result;
    }
}
