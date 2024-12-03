package com.bits.hr.service.event;

import com.bits.hr.domain.EmploymentCertificate;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmploymentCertificateApplicationEvent extends ApplicationEvent {

    EmploymentCertificate employmentCertificate;
    EventType eventType;

    public EmploymentCertificateApplicationEvent(Object source, EmploymentCertificate employmentCertificate, EventType eventType) {
        super(source);
        this.employmentCertificate = employmentCertificate;
        this.eventType = eventType;
    }
}
