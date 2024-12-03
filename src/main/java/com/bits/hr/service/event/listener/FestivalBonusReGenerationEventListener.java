package com.bits.hr.service.event.listener;

import com.bits.hr.service.event.FestivalBonusReGenerationEvent;
import com.bits.hr.service.festivalBonus.generator.event.FestivalBonusReGenerationEventService;
import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class FestivalBonusReGenerationEventListener {

    @Autowired
    private FestivalBonusReGenerationEventService festivalBonusReGenerationEventService;

    @EventListener
    public void handleFestivalGenerationEvent(FestivalBonusReGenerationEvent festivalBonusReGenerationEvent) {
        LocalDate date = festivalBonusReGenerationEvent.getDate();
        festivalBonusReGenerationEventService.reGenerateFestivalBonusByDate(date);
    }
}
