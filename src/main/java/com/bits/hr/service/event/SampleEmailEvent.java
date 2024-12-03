package com.bits.hr.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SampleEmailEvent extends ApplicationEvent {

    EventType type;

    public SampleEmailEvent(Object source, EventType type) {
        super(source);
        this.type = type;
    }
}
