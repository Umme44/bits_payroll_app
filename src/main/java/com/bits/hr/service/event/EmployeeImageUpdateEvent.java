package com.bits.hr.service.event;

import com.bits.hr.domain.Employee;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmployeeImageUpdateEvent extends ApplicationEvent {

    Employee employee;
    EventType type;

    public EmployeeImageUpdateEvent(Object source, Employee employee, EventType type) {
        super(source);
        this.employee = employee;
        this.type = type;
    }
}
