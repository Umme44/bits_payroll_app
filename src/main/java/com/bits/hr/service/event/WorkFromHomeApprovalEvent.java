package com.bits.hr.service.event;

import com.bits.hr.domain.Employee;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEvent;

@Getter
public class WorkFromHomeApprovalEvent extends ApplicationEvent {

    Employee employee;
    EventType type;

    public WorkFromHomeApprovalEvent(Object source, Employee employee, EventType type) {
        super(source);
        this.employee = employee;
        this.type = type;
    }
}
