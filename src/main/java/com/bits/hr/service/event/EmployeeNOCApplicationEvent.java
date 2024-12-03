package com.bits.hr.service.event;

import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.service.dto.EmployeeNOCDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmployeeNOCApplicationEvent extends ApplicationEvent {

    EmployeeNOC employeeNOC;
    EventType eventType;

    public EmployeeNOCApplicationEvent(Object source, EmployeeNOC employeeNOC, EventType eventType) {
        super(source);
        this.employeeNOC = employeeNOC;
        this.eventType = eventType;
    }
}
