//package com.bits.hr.service;
//
//import com.bits.hr.domain.EmployeeResignation;
//import com.bits.hr.domain.InsuranceClaim;
//import com.bits.hr.domain.InsuranceRegistration;
//import com.bits.hr.domain.enumeration.Status;
//import com.bits.hr.repository.EmployeeResignationRepository;
//import com.bits.hr.repository.InsuranceClaimRepository;
//import com.bits.hr.repository.InsuranceRegistrationRepository;
//import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Service
//@Log4j2
//public class InsuranceDataExportForNewSystemService {
//
//    @Autowired
//    private InsuranceRegistrationRepository insuranceRegistrationRepository;
//
//    @Autowired
//    private InsuranceClaimRepository insuranceClaimRepository;
//
//    @Autowired
//    private UserInsuranceService userInsuranceService;
//
//    @Autowired
//    private EmployeeResignationService employeeResignationService;
//
//    @Autowired
//    private EmployeeResignationRepository employeeResignationRepository;
//
//    public ExportXLPropertiesDTO exportInsuranceRegistrations() throws IOException {
//        List<InsuranceRegistration> insuranceRegistrationList =
//                insuranceRegistrationRepository.findAll();
//
//        insuranceRegistrationList.sort((s1, s2) -> {
//            int s1Pin = (int) Double.parseDouble(s1.getEmployee().getPin());
//            int s2Pin = (int) Double.parseDouble(s2.getEmployee().getPin());
//            if (s1Pin - s2Pin == 0) {
//                return s1.getInsuranceRelation().getRelation().ordinal() - s2.getInsuranceRelation().getRelation().ordinal();
//            } else {
//                return (int) (Double.parseDouble(s1.getEmployee().getPin()) - Double.parseDouble(s2.getEmployee().getPin()));
//            }
//        });
//
//        String sheetName = "Insurance Registrations";
//
//        List<String> titleList = new ArrayList<>();
//
//        List<String> subTitleList = new ArrayList<>();
//
//        List<String> tableHeaderList = new ArrayList<>();
//
//        tableHeaderList.add("S/L");
//        tableHeaderList.add("Primary Key");
//        tableHeaderList.add("PIN");
//        tableHeaderList.add("Name");
//        tableHeaderList.add("Insurance Relationship");
//        tableHeaderList.add("Insurance Status");
//        tableHeaderList.add("Unapproved Reason");
//        tableHeaderList.add("Available Balance");
//
//        List<List<Object>> dataList = new ArrayList<>();
//
//        for(int i =0; i< insuranceRegistrationList.size(); i++){
//            List<Object> dataRow = new ArrayList<>();
//
//            dataRow.add(i+1);
//            dataRow.add(insuranceRegistrationList.get(i).getId().toString());
//            dataRow.add(getPin(insuranceRegistrationList.get(i)));
//            dataRow.add(getName(insuranceRegistrationList.get(i)));
//            dataRow.add(getInsuranceRelation(insuranceRegistrationList.get(i)));
//            dataRow.add(getInsuranceStatus(insuranceRegistrationList.get(i)));
//            dataRow.add(getReason(insuranceRegistrationList.get(i)));
//
//            double remainingBalance = userInsuranceService.getRemainingBalanceByRegistrationId(insuranceRegistrationList.get(i).getId());
//            dataRow.add(remainingBalance);
//
//            dataList.add(dataRow);
//        }
//
//        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
//        exportXLPropertiesDTO.setSheetName(sheetName);
//        exportXLPropertiesDTO.setTitleList(titleList);
//        exportXLPropertiesDTO.setSubTitleList(subTitleList);
//        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
//        exportXLPropertiesDTO.setTableDataListOfList(dataList);
//        exportXLPropertiesDTO.setHasAutoSummation(false);
//        exportXLPropertiesDTO.setAutoSizeColumnUpTo(30);
//
//        return exportXLPropertiesDTO;
//    }
//
//    public ExportXLPropertiesDTO exportInsuranceClaims() throws IOException {
//        List<InsuranceClaim> insuranceClaims =
//                insuranceClaimRepository.findAll();
//
//        insuranceClaims.sort((s1, s2) -> {
//            int s1Pin = (int) Double.parseDouble(s1.getInsuranceRegistration().getEmployee().getPin());
//            int s2Pin = (int) Double.parseDouble(s2.getInsuranceRegistration().getEmployee().getPin());
//            if (s1Pin - s2Pin == 0) {
//                return s1.getInsuranceRegistration().getInsuranceRelation().getRelation().ordinal() - s2.getInsuranceRegistration().getInsuranceRelation().getRelation().ordinal();
//            } else {
//                return (int) (Double.parseDouble(s1.getInsuranceRegistration().getEmployee().getPin()) - Double.parseDouble(s2.getInsuranceRegistration().getEmployee().getPin()));
//            }
//        });
//
//        String sheetName = "Insurance Claims";
//
//        List<String> titleList = new ArrayList<>();
//
//        List<String> subTitleList = new ArrayList<>();
//
//
//        List<String> tableHeaderList = new ArrayList<>();
//
//        tableHeaderList.add("S/L");
//        tableHeaderList.add("Primary Key");
//        tableHeaderList.add("Insurance Card ID");
//        tableHeaderList.add("Claimed Amount");
//        tableHeaderList.add("Settled Amount");
//        tableHeaderList.add("Settlement Date");
//        tableHeaderList.add("Payment Date");
//        tableHeaderList.add("Regretted Date");
//        tableHeaderList.add("Regretted Reason");
//        tableHeaderList.add("Claimed Status");
//
//        List<List<Object>> dataList = new ArrayList<>();
//
//        for(int i =0; i< insuranceClaims.size(); i++){
//            List<Object> dataRow = new ArrayList<>();
//
//            dataRow.add(i+1);
//            dataRow.add(insuranceClaims.get(i).getId().toString());
//            dataRow.add(insuranceClaims.get(i).getInsuranceRegistration().getInsuranceId());
//            dataRow.add(getTotalClaimedAmount(insuranceClaims.get(i)));
//            dataRow.add(getTotalAcceptedAmount(insuranceClaims.get(i)));
//
//            LocalDate approvalDate = getApprovalDate(insuranceClaims.get(i));
//            // Settlement Date
//            dataRow.add(approvalDate!=null ? approvalDate : "-");
//            // Payment Date
//            dataRow.add(approvalDate!=null ? approvalDate : "-");
//
//            // Regretted Date
//            dataRow.add("-");
//
//            dataRow.add(getClaimRejectionReason(insuranceClaims.get(i)));
//            dataRow.add(getClaimStatus(insuranceClaims.get(i).getStatus()));
//            dataList.add(dataRow);
//        }
//
//        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
//        exportXLPropertiesDTO.setSheetName(sheetName);
//        exportXLPropertiesDTO.setTitleList(titleList);
//        exportXLPropertiesDTO.setSubTitleList(subTitleList);
//        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
//        exportXLPropertiesDTO.setTableDataListOfList(dataList);
//        exportXLPropertiesDTO.setHasAutoSummation(false);
//        exportXLPropertiesDTO.setAutoSizeColumnUpTo(30);
//
//        return exportXLPropertiesDTO;
//    }
//
//
//
//    String getPin(InsuranceRegistration insuranceRegistration){
//        try {
//            return insuranceRegistration.getEmployee().getPin();
//        } catch (Exception e){
//            return "-";
//        }
//    }
//
//    String getName(InsuranceRegistration insuranceRegistration){
//        try {
//            return insuranceRegistration.getName();
//        } catch (Exception e){
//            return "-";
//        }
//    }
//
//
//    String getInsuranceRelation(InsuranceRegistration insuranceRegistration){
//        try {
//            return insuranceRegistration.getInsuranceRelation().getRelationName();
//        } catch (Exception e){
//            return "-";
//        }
//    }
//
//
//    private String getReason(InsuranceRegistration insuranceRegistration) {
//        String reason = insuranceRegistration.getReason();
//        if(reason == null){
//            return "-";
//        } else return insuranceRegistration.getReason();
//    }
//
//    private int getTotalClaimedAmount(InsuranceClaim insuranceClaim) {
//        double totalClaimAmount = 0;
//        double hospitalAccommodationCharge = insuranceClaim.getHospitalAccommodationCharge() != null ? insuranceClaim.getHospitalAccommodationCharge() : 0.0;
//        double medicalInvestigationExpense = insuranceClaim.getMedicalInvestigationExpense() != null ? insuranceClaim.getMedicalInvestigationExpense() : 0.0;
//        double surgicalCharge = insuranceClaim.getSurgicalCharge() != 0 ? insuranceClaim.getSurgicalCharge() : 0.0;
//        double medicineAndDruges = insuranceClaim.getMedicineAndDruges() != null ? insuranceClaim.getMedicineAndDruges() : 0.0;
//        double ancillaryServices = insuranceClaim.getAncillaryServices() != null ? insuranceClaim.getAncillaryServices() : 0.0;
//        double consultantFee = insuranceClaim.getConsultantFee() != null ? insuranceClaim.getConsultantFee() : 0.0;
//        double others = insuranceClaim.getOthers() != null ? insuranceClaim.getOthers() : 0.0;
//
//        totalClaimAmount = hospitalAccommodationCharge
//                + medicalInvestigationExpense
//                + surgicalCharge
//                + medicineAndDruges
//                + ancillaryServices
//                + consultantFee
//                + others;
//
//        return (int) totalClaimAmount;
//    }
//
//    private int getTotalAcceptedAmount(InsuranceClaim insuranceClaim) {
//        Double acceptedAmount = insuranceClaim.getAcceptedAmount();
//        if(acceptedAmount == null){
//            return 0;
//        } else {
//            return acceptedAmount.intValue();
//        }
//    }
//
//    private String getClaimRejectionReason(InsuranceClaim insuranceClaim) {
//        String reason = insuranceClaim.getReason();
//        if(reason == null){
//            return "-";
//        } else {
//            return reason;
//        }
//    }
//
//    private LocalDate getApprovalDate(InsuranceClaim insuranceClaim) {
//        Instant approvalDate = insuranceClaim.getApprovedAt();
//        if(approvalDate == null){
//            return null;
//        } else {
//            return approvalDate.atZone(ZoneId.systemDefault()).toLocalDate();
//        }
//    }
//
//    private String getClaimStatus(Status status){
//        if(status == Status.APPROVED){
//            return "SETTLED";
//        }else {
//            return "REGRETTED";
//        }
//    }
//
//    private String getInsuranceStatus(InsuranceRegistration insuranceRegistration) {
//        LocalDate today = LocalDate.now();
//
//        List<EmployeeResignation> employeeResignations =
//            employeeResignationRepository.findResignedEmployeeByPin(
//                insuranceRegistration.getEmployee().getPin(), today
//            );
//
//        if(employeeResignations.size()>1){
//            return "SEPARATED";
//        } else if (insuranceRegistration.getStatus().equals(Status.APPROVED)) {
//            return "APPROVED";
//        } else if (insuranceRegistration.getStatus().equals(Status.NOT_APPROVED)) {
//            return "NOT_APPROVED";
//        } else {
//            return "PENDING";
//        }
//    }
//}
//
