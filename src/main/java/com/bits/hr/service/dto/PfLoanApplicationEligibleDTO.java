package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.finalSettlement.dto.salary.TimeDuration;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PfLoanApplicationEligibleDTO {

    private double pfLoanEligibleAmount;

    private EmployeeCategory employeeCategory;

    private boolean isRegularConfirmedEmployee;

    private boolean eligibleBand;

    private String bandName;

    private LocalDate dateOfJoining;

    private boolean pfAccountMatured;

    private String serviceTenure;

    private boolean anyOpenRepayingPfLoan;

    private int activeLoanId;
}
