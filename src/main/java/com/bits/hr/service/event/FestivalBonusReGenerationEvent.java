package com.bits.hr.service.event;

import java.time.LocalDate;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FestivalBonusReGenerationEvent extends ApplicationEvent {

    private LocalDate date;

    public FestivalBonusReGenerationEvent(Object source, LocalDate date) {
        super(source);
        this.date = date;
    }
}
