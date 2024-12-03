package com.bits.hr.service.festivalBonus.generator.event;

import com.bits.hr.domain.Festival;
import com.bits.hr.repository.FestivalRepository;
import com.bits.hr.service.festivalBonus.FBService;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FestivalBonusReGenerationEventService {

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private FBService fbService;

    public void reGenerateFestivalBonusByDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) return;
        List<Festival> festivalList = festivalRepository.getAllByBonusDisbursementDateAfterAndEqual(date);
        for (Festival festival : festivalList) {
            try {
                if (festival.getBonusDisbursementDate().isAfter(date) || festival.getBonusDisbursementDate().isEqual(date)) {
                    fbService.generateAndSave(festival);
                    log.info("Regeneration Success! for {}", festival);
                }
            } catch (Exception ex) {
                log.error("Failed to generate! {festival}", festival);
            }
        }
    }
}
