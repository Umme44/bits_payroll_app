package com.bits.hr.service.salaryGeneration.config;

import com.bits.hr.config.YamlConfig;
import com.bits.hr.domain.AitConfig;
import com.bits.hr.repository.AitConfigRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lemon
 */

@Service
public class ConfigParserImpl implements ConfigParser {

    @Autowired
    AitConfigRepository aitConfigRepository;

    @Override
    public IncomeTaxConfig parseFromConfigFile() {
        return YamlConfig.read("income-tax.yml", IncomeTaxConfig.class);
    }

    /*
     * get ait configs from database
     * get applicable ait yml from aitConfigList
     * ait config list
     * income year example => july 1st 2001 to june 30 2002
     *
     * */
    @Override
    public IncomeTaxConfig parseFromDatabase(Integer incomeYearStartYear) {
        LocalDate incomeYearStartDate = LocalDate.of(incomeYearStartYear, Month.JULY, 1);
        LocalDate incomeYearEndDate = LocalDate.of(incomeYearStartYear + 1, Month.JUNE, 30);
        List<AitConfig> aitConfigList = aitConfigRepository.findAllBetween(incomeYearStartDate, incomeYearEndDate);

        if (aitConfigList.size() < 1) {
            return null;
            // return YamlConfig.readFromDatabase(aitConfigList.get(0), IncomeTaxConfig.class);
        } else {
            return YamlConfig.readFromDatabase(aitConfigList.get(0), IncomeTaxConfig.class);
        }
        // on empty database ,
        // context initialization should not fail
        // due to no data in config file .
        // so take data from yml file instead of empty configuration
        // else return YamlConfig.read("income-tax.yml", IncomeTaxConfig.class);

    }
}
