package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.Month;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeSalaryCertificateReportDTO {

    private String employeeName;
    private String employeeLastName;
    private String salutation;
    private String pin;
    private LocalDate joiningDate;
    private LocalDate confirmationDate;
    private EmployeeCategory employeeCategory;
    private String designation;
    private String unit;
    private String department;
    private Month month;
    private Integer year;

    private Double payableGrossBasicSalary;
    private Double payableGrossHouseRent;
    private Double payableGrossMedicalAllowance;
    private Double payableGrossConveyanceAllowance;

    private Double livingAllowance;

    private Double entertainment;
    private Double utility;
    private Double otherAddition;
    private Double payableGrossSalary;

    private Double pfDeduction;
    private Double taxDeduction;
    private Double mobileBillDeduction;
    private Double welfareFundDeduction;
    private Double otherDeduction;
    private Double totalDeduction;

    private Double netPay;
    private String netPayInWords;

    private Long signatoryPersonId;

    private String signatoryPersonName;

    private String signatoryPersonDesignation;

    private String referenceNumber;
}
