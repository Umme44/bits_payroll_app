package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.InsuranceClaim;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.enumeration.ClaimStatus;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.domain.enumeration.RelationType;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
public class InsuranceImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private InsuranceFileService insuranceFileService;

    @Autowired
    private InsuranceClaimRepository insuranceClaimRepository;

    @Autowired
    private InsuranceRegistrationService insuranceRegistrationService;

    //  Import Insurance Claim [ Claims(settled/regretted) from insurance provider ]
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

                //  InsuranceCardID   settlementDate   paymentDate   regretDate   regretReason   claimedAmount   SettledAmount   claimStatus
                //         0                1               2             3             4               5               6             7

                InsuranceClaim insuranceClaim = new InsuranceClaim();

                Optional<InsuranceRegistration> insuranceRegistrationOptional = insuranceRegistrationRepository.findByInsuranceCardId(
                    dataItems.get(0).trim()
                );

                if (insuranceRegistrationOptional.isPresent()) {
                    boolean isApproved = insuranceRegistrationOptional.get().getInsuranceStatus().equals(InsuranceStatus.APPROVED);
                    if (isApproved == true) {
                        insuranceClaim.setInsuranceRegistration(insuranceRegistrationOptional.get());
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }

                insuranceClaim.setSettlementDate(doubleToDate(dataItems.get(1).trim()));
                insuranceClaim.setPaymentDate(doubleToDate(dataItems.get(2).trim()));
                insuranceClaim.setRegretDate(doubleToDate(dataItems.get(3).trim()));
                insuranceClaim.setClaimedAmount(Double.parseDouble(dataItems.get(5).trim()));
                insuranceClaim.setSettledAmount(Double.parseDouble(dataItems.get(6).trim()));
                insuranceClaim.setClaimStatus(getClaimStatus(dataItems.get(7).trim()));

                insuranceClaim.setCreatedAt(Instant.now());
                insuranceClaim.setCreatedBy(currentEmployeeService.getCurrentUser().get());

                insuranceClaimList.add(insuranceClaim);
            }
            return saveInsuranceClaim(insuranceClaimList);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return false;
        }
    }

    // Import Previous Insurance Registrations to the system
    public boolean importPreviousInsuranceRegistrations(MultipartFile file) throws Exception {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);
            for (List<String> dataItems : data) {
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                // S/L   primaryKey   pin   name   relationship   status   unApprovalReason   availableBalance
                //  0        1         2      3         4           5             6                  7

                Optional<InsuranceRegistration> insuranceRegistrationOptional = insuranceRegistrationRepository.findById(
                    Long.parseLong(dataItems.get(1).trim())
                );

                if (!insuranceRegistrationOptional.isPresent()) {
                    continue;
                }

                insuranceRegistrationOptional.get().setInsuranceRelation(getInsuranceRelation(dataItems.get(4).trim()));
                insuranceRegistrationOptional.get().setInsuranceStatus(getInsuranceStatus(dataItems.get(5).trim()));

                if (getInsuranceStatus(dataItems.get(5).trim()).equals(InsuranceStatus.SEPARATED)) {
                    insuranceRegistrationOptional.get().setUnapprovalReason("Policy Holder Resigned.");
                } else {
                    insuranceRegistrationOptional.get().setUnapprovalReason(getUnApprovalReason(dataItems.get(6).trim()));
                }

                insuranceRegistrationOptional.get().setAvailableBalance(Double.parseDouble(dataItems.get(7).trim()));

                // Created At was missing in some of the previous data.
                if (insuranceRegistrationOptional.get().getCreatedAt() == null) {
                    if (insuranceRegistrationOptional.get().getUpdatedAt() == null) {
                        if (insuranceRegistrationOptional.get().getApprovedAt() != null) {
                            insuranceRegistrationOptional.get().setCreatedAt(insuranceRegistrationOptional.get().getApprovedAt());
                            insuranceRegistrationOptional.get().setUpdatedAt(insuranceRegistrationOptional.get().getApprovedAt());
                        } else {
                            insuranceRegistrationOptional.get().setCreatedAt(Instant.now());
                            insuranceRegistrationOptional.get().setUpdatedAt(Instant.now());
                        }
                    } else {
                        insuranceRegistrationOptional.get().setCreatedAt(insuranceRegistrationOptional.get().getUpdatedAt());
                        insuranceRegistrationOptional.get().setUpdatedAt(insuranceRegistrationOptional.get().getUpdatedAt());
                    }
                }

                if (insuranceRegistrationOptional.get().getCreatedBy() == null) {
                    insuranceRegistrationOptional.get().setCreatedBy(currentEmployeeService.getCurrentUser().get());
                }

                insuranceRegistrationRepository.save(insuranceRegistrationOptional.get());
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return false;
        }
    }

    // Import Previous Insurance Registrations to the system
    public boolean importApprovedInsuranceRegistrations(MultipartFile file) throws Exception {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);
            for (List<String> dataItems : data) {
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                if (dataItems.size() == 0) {
                    continue;
                }

                // S/L    ID    Name       pin     relationship   InsuranceCardID
                //  0      1     2          3           4               5

                Optional<InsuranceRegistration> insuranceRegistrationOptional = insuranceRegistrationRepository.findById(
                    Long.parseLong(dataItems.get(1).trim())
                );

                if (!insuranceRegistrationOptional.isPresent()) {
                    continue;
                }

                insuranceRegistrationOptional.get().setInsuranceId(dataItems.get(5).trim());

                insuranceRegistrationRepository.save(insuranceRegistrationOptional.get());
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    // Import Previous Insurance Claims
    public boolean importPreviousInsuranceClaims(MultipartFile file) throws Exception {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);
            for (List<String> dataItems : data) {
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                // S/L   primaryKey   InsuranceID   claimedAmount   settledAmount   settlementDate   paymentDate   regrettedDate   regrettedReason   status
                //  0        1             2             3                4                5              6              7                8             9

                Optional<InsuranceClaim> insuranceClaimOptional = insuranceClaimRepository.findById(
                    Long.parseLong(dataItems.get(1).trim())
                );
                if (!insuranceClaimOptional.isPresent()) {
                    continue;
                }

                Optional<InsuranceRegistration> insuranceRegistrationOptional = insuranceRegistrationRepository.findByInsuranceCardId(
                    dataItems.get(2).trim()
                );
                if (!insuranceRegistrationOptional.isPresent()) {
                    continue;
                }

                boolean isValidClaim = insuranceClaimOptional
                    .get()
                    .getInsuranceRegistration()
                    .getId()
                    .equals(insuranceClaimOptional.get().getInsuranceRegistration().getId());

                if (isValidClaim == false) {
                    continue;
                }

                insuranceClaimOptional.get().setClaimedAmount(Double.parseDouble(dataItems.get(3).trim()));

                Double settledAmount = dataItems.get(4).trim().equals("-") ? null : Double.parseDouble(dataItems.get(4).trim());
                if (settledAmount != null) {
                    insuranceClaimOptional.get().setSettledAmount(settledAmount);
                }

                insuranceClaimOptional.get().setSettlementDate(doubleToDate(dataItems.get(5).trim()));
                insuranceClaimOptional.get().setPaymentDate(doubleToDate(dataItems.get(6).trim()));
                insuranceClaimOptional.get().setRegretDate(doubleToDate(dataItems.get(7).trim()));
                insuranceClaimOptional.get().setRegretReason(getString(dataItems.get(8).trim()));
                insuranceClaimOptional.get().setClaimStatus(getClaimStatus(dataItems.get(9).trim()));

                LocalDate createdDate = getClaimStatus(dataItems.get(9).trim()).equals(ClaimStatus.SETTLED)
                    ? doubleToDate(dataItems.get(5).trim())
                    : LocalDate.now();

                Instant createdAt = createdDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

                if (insuranceClaimOptional.get().getCreatedAt() == null) {
                    if (insuranceClaimOptional.get().getUpdatedAt() == null) {
                        insuranceClaimOptional.get().setCreatedAt(createdAt);
                        insuranceClaimOptional.get().setUpdatedAt(createdAt);
                    } else {
                        insuranceClaimOptional.get().setCreatedAt(insuranceClaimOptional.get().getUpdatedAt());
                    }
                }

                if (insuranceClaimOptional.get().getCreatedBy() == null) {
                    insuranceClaimOptional.get().setCreatedBy(currentEmployeeService.getCurrentUser().get());
                }

                insuranceClaimRepository.save(insuranceClaimOptional.get());
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveInsuranceClaim(List<InsuranceClaim> insuranceClaimList) {
        // If same replica doesn't exist --> Save
        // Else Update
        try {
            for (InsuranceClaim insuranceClaim : insuranceClaimList) {
                List<InsuranceClaim> existingClaim = insuranceClaimRepository.findDuplicate(
                    insuranceClaim.getInsuranceRegistration().getId(),
                    insuranceClaim.getClaimStatus().name(),
                    insuranceClaim.getRegretDate(),
                    insuranceClaim.getSettlementDate(),
                    insuranceClaim.getSettledAmount()
                );

                if (existingClaim.size() != 0) {
                    insuranceClaim.setId(existingClaim.get(0).getId());
                }
                insuranceClaimRepository.save(insuranceClaim);
                insuranceRegistrationService.updateAvailableBalanceInInsuranceRegistration(
                    insuranceClaim.getInsuranceRegistration().getId()
                );
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    InsuranceStatus getInsuranceStatus(String insuranceStatus) {
        String status = insuranceStatus.trim().toUpperCase();

        switch (status) {
            case "SEPARATED":
                return InsuranceStatus.SEPARATED;
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

    LocalDate doubleToDate(String date) {
        try {
            return DateUtil.doubleToDate(Double.parseDouble(date));
        } catch (Exception e) {
            return null;
        }
    }

    String getString(String s) {
        try {
            return s.trim();
        } catch (Exception e) {
            return null;
        }
    }

    RelationType getRelationType(String relationType) {
        String type = relationType.trim().toUpperCase();

        RelationType relationtype = null;
        switch (type) {
            case "SELF":
                relationtype = RelationType.SELF;
                break;
            case "HUSBAND":
                relationtype = RelationType.HUSBAND;
                break;
            case "WIFE":
                relationtype = RelationType.WIFE;
                break;
            case "SON":
                relationtype = RelationType.SON;
                break;
            case "DAUGHTER":
                relationtype = RelationType.DAUGHTER;
                break;
        }
        return relationtype;
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

    String getUnApprovalReason(String reason) {
        if (reason.equals("-")) {
            return null;
        } else {
            return reason.trim();
        }
    }
}
