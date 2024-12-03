package com.bits.hr.service.event;

import com.bits.hr.domain.SalaryCertificate;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmployeeSalaryCertificateApplicationEvent extends ApplicationEvent {

    SalaryCertificate salaryCertificate;
    EventType eventType;

    public EmployeeSalaryCertificateApplicationEvent(Object source, SalaryCertificate salaryCertificate, EventType eventType) {
        super(source);
        this.salaryCertificate = salaryCertificate;
        this.eventType = eventType;
    }
}
