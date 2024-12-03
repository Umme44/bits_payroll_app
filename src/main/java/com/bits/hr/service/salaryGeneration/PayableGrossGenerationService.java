package com.bits.hr.service.salaryGeneration;

public interface PayableGrossGenerationService {
    //
    Double payableGross(Integer year, Integer month, Double mainGrossSalary, Integer fractionDays);
}
