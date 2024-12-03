package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.InsuranceConfiguration;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.repository.InsuranceConfigurationRepository;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduledInsuranceService {

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private InsuranceConfigurationRepository insuranceConfigurationRepository;

    public void excludeFromInsuranceIfEmployeeResigns() {
        List<InsuranceRegistration> insuranceRegistrations = insuranceRegistrationRepository.findAllInsuranceRegistrationsOfResignedEmployees(
            LocalDate.now()
        );

        // use enhanced
        for (int i = 0; i < insuranceRegistrations.size(); i++) {
            InsuranceRegistration insuranceRegistration = insuranceRegistrations.get(i);

            insuranceRegistration.setInsuranceStatus(InsuranceStatus.SEPARATED);
            insuranceRegistration.setUnapprovalReason("Policy Holder Resigned");
            insuranceRegistration.setUpdatedAt(Instant.now());

            insuranceRegistrationRepository.save(insuranceRegistration);
        }
    }

    public void resetInsuranceClaimBalanceAtTheVeryFirstDayOfTheYear() {
        List<InsuranceRegistration> insuranceRegistrations = insuranceRegistrationRepository.findAllApprovedRegistration("", null, null);
        List<InsuranceConfiguration> allConfigurations = insuranceConfigurationRepository.findAll();

        for (int i = 0; i < insuranceRegistrations.size(); i++) {
            insuranceRegistrations.get(i).setAvailableBalance(allConfigurations.get(0).getMaxTotalClaimLimitPerYear());
            insuranceRegistrations.get(i).setUpdatedAt(Instant.now());
            insuranceRegistrationRepository.save(insuranceRegistrations.get(i));
        }
    }

    public void excludeFromInsuranceIfChileAgeExceedMaxAgeLimit() {
        // remove try catch
        try {
            LocalDate today = LocalDate.now();
            double maxAgeLimit = insuranceConfigurationRepository.findAll().get(0).getMaxAllowedChildAge();

            LocalDate date = today.minusYears((long) maxAgeLimit);

            List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.getListOfChildRelationsWhoHaveExceededMaxAgeLimit(
                date
            );

            for (int i = 0; i < insuranceRegistrationList.size(); i++) {
                insuranceRegistrationList.get(i).setInsuranceStatus(InsuranceStatus.CANCELED);
                insuranceRegistrationList.get(i).setUnapprovalReason("Exceeded Max Age Limit");
                insuranceRegistrationList.get(i).setUpdatedAt(Instant.now());

                insuranceRegistrationRepository.save(insuranceRegistrationList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
