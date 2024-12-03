package com.bits.hr.service.impl;

import com.bits.hr.repository.AitConfigRepository;
import com.bits.hr.service.AitConfigService;
import com.bits.hr.service.SalaryGenerationPreValidationService;
import com.bits.hr.service.approvalProcess.SalaryLockService;
import com.bits.hr.service.dto.SalaryGenerationPreValidationDTO;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryGenerationPreValidationServiceImpl implements SalaryGenerationPreValidationService {

    private final AitConfigService aitConfigService;
    private final SalaryLockService salaryLockService;

    public SalaryGenerationPreValidationServiceImpl(AitConfigService aitConfigService, SalaryLockService salaryLockService) {
        this.aitConfigService = aitConfigService;
        this.salaryLockService = salaryLockService;
    }

    @Override
    public SalaryGenerationPreValidationDTO getSalaryGenerationPreValidations(int year, int month) {
        SalaryGenerationPreValidationDTO salaryGenerationPreValidationDTO = new SalaryGenerationPreValidationDTO();

        // aitConfigMissing
        int countOfAitConfigs = aitConfigService.findByYearAndMonth(year, month).size();
        boolean aitConfigMissing = countOfAitConfigs == 0 ? true : false;
        salaryGenerationPreValidationDTO.setAitConfigMissing(aitConfigMissing);

        // isLocked
        salaryGenerationPreValidationDTO.setSalaryLocked(salaryLockService.isLocked(String.valueOf(year), String.valueOf(month)));

        return salaryGenerationPreValidationDTO;
    }
}
