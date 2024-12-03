package com.bits.hr.service.event;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NomineeUpdateEvent extends ApplicationEvent {

    Employee employee;
    String nomineeType;
    EventType type;

    public NomineeUpdateEvent(Object source, Employee employee, String nomineeType, EventType type) {
        super(source);
        this.employee = employee;
        this.nomineeType = nomineeType;
        this.type = type;
    }
}
