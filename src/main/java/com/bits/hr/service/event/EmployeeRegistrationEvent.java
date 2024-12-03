package com.bits.hr.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmployeeRegistrationEvent extends ApplicationEvent {

    private String officialEmail;
    private String pin;

    public EmployeeRegistrationEvent(Object source, String officialEmail, String pin) {
        super(source);
        this.officialEmail = officialEmail;
        this.pin = pin.trim();
    }
}
