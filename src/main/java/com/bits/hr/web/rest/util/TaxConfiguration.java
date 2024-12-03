package com.bits.hr.web.rest.util;

import com.bits.hr.domain.AitConfig;
import com.bits.hr.repository.AitConfigRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payroll-mgt")
public class TaxConfiguration {

    @Autowired
    AitConfigRepository aitConfigRepository;

    @GetMapping("/ait-config-check/{year}/{month}")
    public boolean check(@PathVariable int year, @PathVariable int month) throws Exception {
        LocalDate startDayOfMonth = LocalDate.of(year, month, 1);
        List<AitConfig> aitConfigList = aitConfigRepository.findAllBetweenOneDate(startDayOfMonth);
        return aitConfigList.size() == 0;
    }
}
