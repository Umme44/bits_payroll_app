package com.bits.hr.util;

import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryGenerationMasterService {

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    public boolean isExistSalaryGeneratorMaster(int year, int month) {
        return salaryGeneratorMasterRepository
            .findSalaryGeneratorMastersByYearAndMonth(String.valueOf(year), String.valueOf(month))
            .isPresent();
    }

    public SalaryGeneratorMaster getOrCreateSalaryGeneratorMaster(int year, int month) {
        return salaryGeneratorMasterRepository
            .findSalaryGeneratorMastersByYearAndMonth(String.valueOf(year), String.valueOf(month))
            .orElseGet(() -> {
                SalaryGeneratorMaster d = new SalaryGeneratorMaster();
                d.setYear(String.valueOf(year));
                d.setMonth(String.valueOf(month));
                return salaryGeneratorMasterRepository.save(d);
            });
    }
}
