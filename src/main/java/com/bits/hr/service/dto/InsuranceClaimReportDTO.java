package com.bits.hr.service.dto;

import lombok.Data;

@Data
public class InsuranceClaimReportDTO {

    private EmployeeMinimalDTO employeeBasicInfo;
    private EmployeeBankDetailsDTO employeeBankDetails;
    private InsuranceClaimDTO insuranceClaim;
    private InsuranceRegistrationDTO insuranceRegistration;
}
