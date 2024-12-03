package com.bits.hr.service.salaryGeneration;

import java.time.LocalDate;

public interface ProvidentFundService {
    double calculateProvidentFund(Double payableGross, Integer fractionDays, Integer month, Integer year, LocalDate dateOfConfirmation);
}
