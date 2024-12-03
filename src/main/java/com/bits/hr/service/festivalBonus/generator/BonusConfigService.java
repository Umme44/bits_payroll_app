package com.bits.hr.service.festivalBonus.generator;

import com.bits.hr.domain.FestivalBonusConfig;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.FestivalBonusConfigRepository;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BonusConfigService {

    @Autowired
    private FestivalBonusConfigRepository festivalBonusConfigRepository;

    public HashMap<EmployeeCategory, Double> getConfig() {
        HashMap<EmployeeCategory, Double> hashMap = new HashMap<>();
        List<FestivalBonusConfig> festivalBonusConfigList = festivalBonusConfigRepository.findAll();
        for (FestivalBonusConfig festivalBonusConfig : festivalBonusConfigList) {
            hashMap.put(festivalBonusConfig.getEmployeeCategory(), festivalBonusConfig.getPercentageFromGross());
        }
        return hashMap;
    }
}
