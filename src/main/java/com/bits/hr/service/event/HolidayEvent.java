package com.bits.hr.service.event;

import com.bits.hr.service.dto.HolidaysDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HolidayEvent extends ApplicationEvent {

    HolidaysDTO holidaysDTO;
    EventType type;

    public HolidayEvent(Object source, HolidaysDTO holidaysDTO, EventType type) {
        super(source);
        this.holidaysDTO = holidaysDTO;
        this.type = type;
    }
}
