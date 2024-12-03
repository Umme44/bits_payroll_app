package com.bits.hr.service.salaryGeneration;

import java.time.LocalDate;

public interface GratuityFundService {
    double calculateGratuityFund(LocalDate dateOfJoining, LocalDate calculationDate, Double CurrentMainGrossBasic);

    int CalculateServiceYear(LocalDate dateOfJoining, LocalDate calculationDate);

    double getProvisionForGratuityFundPerMonth(LocalDate dateOfJoining, LocalDate calculationDate, Double CurrentMainGrossBasic);
}
