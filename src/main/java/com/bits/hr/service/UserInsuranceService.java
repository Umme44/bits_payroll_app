package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.service.dto.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface UserInsuranceService {
    List<InsuranceRegistrationDTO> getAllInsuranceRegistration(Employee employee);

    InsuranceRegistrationDTO getInsuranceRegistrationById(Long registrationId);

    List<InsuranceClaimDTO> getAllInsuranceClaims(String employeePin);

    InsuranceClaimDTO getInsuranceClaimById(Long claimId, String employeePin);

    InsuranceRegistrationDTO createInsuranceRegistration(InsuranceRegistrationDTO insuranceRegistrationDTO, Employee employee, User user);

    InsuranceRegistrationDTO updateInsuranceRegistration(InsuranceRegistrationDTO insuranceRegistrationDTO, Employee employee, User user);

    void deleteInsuranceRegistration(Long registrationId);

    InsuranceRelationsDTO getRemainingInsuranceRelations(Employee employee);

    EmployeeDetailsDTOForInsuranceRegistration getEmployeeDetailsForInsuranceRegistrationByEmployeeId(Long id);

    //    byte[] getEmployeeImage(String pin) throws IOException;

    public boolean isEmployeeEligibleForInsurance();
}
