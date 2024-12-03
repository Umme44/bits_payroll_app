package com.bits.hr.service.festivalBonus;

import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@Primary
public class FBServiceImpl implements FBService {

    @Autowired
    private FBDataPreparationServiceImpl festivalBonusDataPreparationService;

    @Qualifier("FBProRataServiceImpl")
    @Autowired
    private FBService FBProRataService;

    @Qualifier("FBRegularServiceImpl")
    @Autowired
    private FBService FBRegularService;

    public List<FestivalBonusDetails> generate(Festival festival) {
        try {
            if (festival.getIsProRata() != null && festival.getIsProRata()) {
                // generate pro rata festival bonus for selected point of time
                return FBProRataService.generate(festival);
            } else {
                // generate festival bonus
                return FBRegularService.generate(festival);
            }
        } catch (Exception ex) {
            log.error(ex);
            return new ArrayList<>();
        }
    }

    public List<FestivalBonusDetails> generateAndSave(Festival festival) {
        try {
            if (festival.getIsProRata() != null && festival.getIsProRata()) {
                // generate pro rata festival bonus for selected point of time
                return FBProRataService.generateAndSave(festival);
            } else {
                // generate festival bonus
                return FBRegularService.generateAndSave(festival);
            }
        } catch (Exception ex) {
            log.error(ex);
            return new ArrayList<>();
        }
    }
}
