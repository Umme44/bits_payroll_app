package com.bits.hr.service.event;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.FlexScheduleApprovalDTO;
import com.bits.hr.service.dto.FlexScheduleDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FlexScheduleApprovalEvent extends ApplicationEvent {

    Employee employee;
    FlexScheduleApprovalDTO flexScheduleApprovalDTO;
    EventType type;

    public FlexScheduleApprovalEvent(Object source, Employee employee, FlexScheduleApprovalDTO flexScheduleApprovalDTO, EventType type) {
        super(source);
        this.employee = employee;
        this.flexScheduleApprovalDTO = flexScheduleApprovalDTO;
        this.type = type;
    }
}
