package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.InsuranceClaim;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.enumeration.ClaimStatus;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.InsuranceClaimRepository;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.InsuranceRegistrationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.fileOperations.fileService.InsuranceFileService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.DateUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class InsuranceImportDataForNewSystemService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    // Import Insurance Registration
    public boolean importFile(MultipartFile file) throws Exception {
        List<InsuranceRegistration> insuranceRegistrationList = new ArrayList<>();
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);

            // SL    PrimaryKey    PIN     Name    InsuranceRelationship           InsuranceStatus     UnapprovedReason    AvailableBalance
            //  0       2351        1002   Sheikh         Child 1                      PENDING                 -                     20000

            for (List<String> dataItems : data) {
                if (dataItems.get(1).equals("0")) {
                    continue;
                }
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(1).equals("")) {
                    continue;
                }

                Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(
                    Long.parseLong(dataItems.get(1))
                );

                if (!insuranceRegistration.isPresent()) {
                    continue;
                }

                InsuranceRegistration savedInsuranceRegistration = insuranceRegistration.get();

                //Set InsuranceRelationship
                savedInsuranceRegistration.setInsuranceRelation(getInsuranceRelation(dataItems.get(4)));

                // Set Insurance Status
                savedInsuranceRegistration.setInsuranceStatus(getInsuranceStatus(dataItems.get(5)));

                // Set UnapprovedReason
                savedInsuranceRegistration.setUnapprovalReason(getReason(dataItems.get(6)));

                // Set AvailableBalance
                savedInsuranceRegistration.setAvailableBalance(Double.parseDouble(dataItems.get(7)));

                if (savedInsuranceRegistration.getCreatedAt() == null) {
                    savedInsuranceRegistration.setCreatedAt(Instant.now());
                }

                if (savedInsuranceRegistration.getCreatedBy() == null) {
                    savedInsuranceRegistration.setCreatedBy(currentEmployeeService.getCurrentUser().get());
                }
                insuranceRegistrationList.add(savedInsuranceRegistration);
            }

            save(insuranceRegistrationList);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //     Import Insurance Claim
    public boolean importInsuranceClaim(MultipartFile file) throws Exception {
        List<InsuranceClaim> insuranceClaimList = new ArrayList<>();
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);
            for (List<String> dataItems : data) {
                if (dataItems.get(1).equals("0")) {
                    continue;
                }
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(1).equals("")) {
                    continue;
                }

                //S/L	PrimaryKey	ClaimedAmount	SettledAmount	SettlementDate  Payment Date	isRegreated	 RegreatedDate	RegreatedReason	Claimed Status
                // 0      2351         2000             1000           11/09/2022         -           false          -               -            SETTLED

                Optional<InsuranceClaim> insuranceClaim = insuranceClaimRepository.findById(Long.parseLong(dataItems.get(1)));

                if (!insuranceClaim.isPresent()) {
                    continue;
                }

                InsuranceClaim savedInsuranceClaim = insuranceClaim.get();

                // Set Insurance ClaimedAmount
                savedInsuranceClaim.setClaimedAmount(Double.parseDouble(dataItems.get(3)));

                // Set Insurance SettleAmount
                savedInsuranceClaim.setSettledAmount(Double.parseDouble(dataItems.get(4)));

                // Set Insurance settlement Date
                String settlementDate = dataItems.get(5);
                if (!settlementDate.equals("-") && !settlementDate.equals("N/A") && !settlementDate.equals("n/a")) {
                    savedInsuranceClaim.setSettlementDate(DateUtil.xlStringToDate(settlementDate));
                }

                // Set Insurance settlement Date
                String paymentDate = dataItems.get(6);
                if (!paymentDate.equals("-") && !paymentDate.equals("N/A") && !paymentDate.equals("n/a")) {
                    savedInsuranceClaim.setPaymentDate(DateUtil.xlStringToDate(paymentDate));
                }

                // Set Insurance isRegreted Missing in Entity
                // savedInsuranceClaim.isRegreted(dataItems.get(7));

                // Set InsuranceClaim Regret Date
                String regretDate = dataItems.get(8);
                if (!regretDate.equals("-") && !regretDate.equals("N/A") && !regretDate.equals("n/a")) {
                    savedInsuranceClaim.setRegretDate(DateUtil.xlStringToDate(regretDate));
                }

                // Set InsuranceClaim Regret Reason
                savedInsuranceClaim.setRegretReason(getReason(dataItems.get(9)));

                // Set InsuranceClaim Status
                savedInsuranceClaim.setClaimStatus(getClaimStatus(dataItems.get(10)));

                insuranceClaimList.add(savedInsuranceClaim);
            }

            saveInsuranceClaim(insuranceClaimList);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void save(List<InsuranceRegistration> insuranceRegistrationList) {
        insuranceRegistrationRepository.saveAll(insuranceRegistrationList);
    }

    public void saveInsuranceClaim(List<InsuranceClaim> insuranceClaimList) {
        insuranceClaimRepository.saveAll(insuranceClaimList);
    }

    InsuranceStatus getInsuranceStatus(String insuranceStatus) {
        String status = insuranceStatus.trim().toUpperCase();

        switch (status) {
            case "APPROVED":
                return InsuranceStatus.APPROVED;
            case "NOT_APPROVED":
                return InsuranceStatus.NOT_APPROVED;
            default:
                return InsuranceStatus.PENDING;
        }
    }

    ClaimStatus getClaimStatus(String status) {
        if (status.equalsIgnoreCase("settled")) {
            return ClaimStatus.SETTLED;
        } else {
            return ClaimStatus.REGRETTED;
        }
    }

    InsuranceRelation getInsuranceRelation(String relation) {
        String relationName = relation.trim().toLowerCase();
        switch (relationName) {
            case "self":
                return InsuranceRelation.SELF;
            case "spouse":
                return InsuranceRelation.SPOUSE;
            case "child 1":
                return InsuranceRelation.CHILD_1;
            case "child 2":
                return InsuranceRelation.CHILD_2;
            default:
                return InsuranceRelation.CHILD_3;
        }
    }

    private String getReason(String reason) {
        if (reason.equals("-") || reason.equals("N/A") || reason.equals("n/a")) {
            return "";
        } else {
            return reason;
        }
    }
}
