package com.bits.hr.service;

import com.bits.hr.service.dto.SalaryGenerationPreValidationDTO;

public interface SalaryGenerationPreValidationService {
    SalaryGenerationPreValidationDTO getSalaryGenerationPreValidations(int year, int month);
}
