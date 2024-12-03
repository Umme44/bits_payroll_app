package com.bits.hr.service.salaryGeneration.config;

/**
 * @author lemon
 */

public interface ConfigParser {
    IncomeTaxConfig parseFromConfigFile();

    IncomeTaxConfig parseFromDatabase(Integer incomeYearStartYear);
}
