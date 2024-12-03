package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class InsuranceDataFixingService {

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    public boolean fixInsuranceNomineeData() {
        try {
            log.info("Fix insurance nominee data");
            List<InsuranceRegistration> insuranceRegistrations = insuranceRegistrationRepository.findAllPendingRegistrations();

            Optional<User> currentUser = currentEmployeeService.getCurrentUser();
            if (!currentUser.isPresent()) {
                throw new RuntimeException("Current user not found");
            }

            for (InsuranceRegistration registration : insuranceRegistrations) {
                if (registration.getInsuranceId() != null) {
                    registration.setInsuranceStatus(InsuranceStatus.APPROVED);
                }
                if (registration.getApprovedAt() == null) {
                    registration.setApprovedAt(Instant.now());
                }
                if (registration.getApprovedBy() == null) {
                    registration.setApprovedBy(currentUser.get());
                }
            }
            save(insuranceRegistrations);

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    List<InsuranceRegistration> save(List<InsuranceRegistration> insuranceRegistrations) {
        try {
            for (InsuranceRegistration insuranceRegistration : insuranceRegistrations) {
                insuranceRegistrationRepository.save(insuranceRegistration);
            }
            return insuranceRegistrations;
        } catch (Exception e) {
            log.error("Error while saving insurance registration", e);
            throw new RuntimeException("Error while saving insurance registration", e);
        }
    }
}
