package com.bits.hr.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FlexScheduleApplicationEvent extends ApplicationEvent {

    private long flexScheduleApplicationId;
    private EventType type;

    public FlexScheduleApplicationEvent(Object source, long flexScheduleApplicationId, EventType type) {
        super(source);
        this.flexScheduleApplicationId = flexScheduleApplicationId;
        this.type = type;
    }
}
