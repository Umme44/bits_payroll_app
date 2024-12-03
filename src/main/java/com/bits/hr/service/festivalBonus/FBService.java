package com.bits.hr.service.festivalBonus;

import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import java.util.List;

public interface FBService {
    List<FestivalBonusDetails> generate(Festival festival);

    List<FestivalBonusDetails> generateAndSave(Festival festival);
}
