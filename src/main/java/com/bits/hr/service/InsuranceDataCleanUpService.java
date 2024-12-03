package com.bits.hr.service;

import com.bits.hr.domain.InsuranceClaim;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.repository.InsuranceClaimRepository;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.fileOperations.FileOperationService;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class InsuranceDataCleanUpService {

    private final InsuranceClaimRepository insuranceClaimRepository;

    private final InsuranceRegistrationRepository insuranceRegistrationRepository;

    private final FileOperationService fileOperationService;

    public InsuranceDataCleanUpService(
        InsuranceClaimRepository insuranceClaimRepository,
        InsuranceRegistrationRepository insuranceRegistrationRepository,
        FileOperationService fileOperationService
    ) {
        this.insuranceClaimRepository = insuranceClaimRepository;
        this.insuranceRegistrationRepository = insuranceRegistrationRepository;
        this.fileOperationService = fileOperationService;
    }

    public boolean cleanUpInsuranceData() {
        try {
            List<InsuranceClaim> insuranceClaimList = insuranceClaimRepository.findAll();

            for (int i = 0; i < insuranceClaimList.size(); i++) {
                insuranceClaimRepository.delete(insuranceClaimList.get(i));
            }

            List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();

            for (int i = 0; i < insuranceRegistrationList.size(); i++) {
                if (insuranceRegistrationList.get(i).getPhoto() != null) {
                    String imagePath = insuranceRegistrationList.get(i).getPhoto();

                    boolean isImageExist = fileOperationService.isExist(imagePath);
                    if (isImageExist) {
                        fileOperationService.delete(imagePath);
                    }
                }
                insuranceRegistrationRepository.delete(insuranceRegistrationList.get(i));
            }
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
