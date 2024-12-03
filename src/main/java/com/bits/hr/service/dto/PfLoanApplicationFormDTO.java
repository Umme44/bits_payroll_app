package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import com.bits.hr.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 * A DTO for the PfLoanApplicationForm for employee details and pfContribution.
 */
@Data
public class PfLoanApplicationFormDTO implements Serializable {

    private Long pfLoanApplicationId;

    private double pfContribution;

    private double pfLoanEligibleAmount;

    private Double installmentAmount;

    private Integer noOfInstallment;

    private LocalDate instalmentStartFrom;

    private String remarks;

    private Boolean isRecommended;

    private LocalDate recommendationDate;

    private Boolean isApproved;

    private LocalDate approvalDate;

    private Boolean isRejected;

    private String rejectionReason;

    private LocalDate rejectionDate;

    private LocalDate disbursementDate;

    private Double disbursementAmount;

    private Status status;

    private String bankName;

    private String bankBranch;

    private String bankAccountNumber;

    private String chequeNumber;

    private Long recommendedById;

    private Long approvedById;

    private Long rejectedById;

    private Long pfAccountId;

    private String pfCode;

    private PfAccountStatus accountStatus;

    private PfLoanStatus pfLoanStatus;

    private String designationName;

    private String departmentName;

    private String unitName;

    private String accHolderName;

    private String pin;

    private EmployeeCategory employeeCategory;

    private boolean isRegularConfirmedEmployee;

    private boolean eligibleBand;

    private String bandName;

    private LocalDate dateOfJoining;

    private boolean pfAccountMatured;

    private int memberShipTotalDays;

    private boolean anyOpenRepayingPfLoan;

    private int activeLoanId;
}
