package com.bits.hr.service.event;

import com.bits.hr.domain.Employee;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaxAcknowledgementReceiptEvent extends ApplicationEvent {

    Employee employee;
    EventType type;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public TaxAcknowledgementReceiptEvent(Object source, Employee employee, EventType type) {
        super(source);
        this.employee = employee;
        this.type = type;
    }
}
