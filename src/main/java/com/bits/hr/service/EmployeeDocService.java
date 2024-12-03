package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.domain.EmploymentCertificate;
import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeNOCRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmploymentCertificateRepository;
import com.bits.hr.repository.SalaryCertificateRepository;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmployeeDocService {

    @Autowired
    private EmployeeNOCRepository employeeNOCRepository;

    @Autowired
    private EmploymentCertificateRepository employmentCertificateRepository;

    @Autowired
    private SalaryCertificateRepository salaryCertificateRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public boolean updateEmployeeDocStatusToPending() {
        try {
            List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAllApprovedRequests();
            List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAllApprovedRequests();
            List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAllApprovedRequests();

            List<EmployeeNOC> pendingEmployeeNOCList = new ArrayList<>();
            List<EmploymentCertificate> pendingEmploymentCertificateList = new ArrayList<>();
            List<SalaryCertificate> pendingSalaryCertificateList = new ArrayList<>();

            employeeNOCList.forEach(employeeNOC -> {
                employeeNOC.setCertificateStatus(CertificateStatus.SENT_FOR_GENERATION);
                employeeNOC.setGeneratedAt(null);
                employeeNOC.setGeneratedBy(null);
                employeeNOC.setReferenceNumber(null);
                pendingEmployeeNOCList.add(employeeNOC);
            });

            employmentCertificateList.forEach(employmentCertificate -> {
                employmentCertificate.setCertificateStatus(CertificateStatus.SENT_FOR_GENERATION);
                employmentCertificate.setGeneratedAt(null);
                employmentCertificate.setGeneratedBy(null);
                employmentCertificate.setReferenceNumber(null);
                pendingEmploymentCertificateList.add(employmentCertificate);
            });

            salaryCertificateList.forEach(salaryCertificate -> {
                salaryCertificate.setStatus(Status.PENDING);
                salaryCertificate.setSanctionAt(null);
                salaryCertificate.setSanctionBy(null);
                salaryCertificate.setReferenceNumber(null);
                pendingSalaryCertificateList.add(salaryCertificate);
            });

            saveData(employeeNOCList, employmentCertificateList, salaryCertificateList);
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public ExportXLPropertiesDTO exportEmployeeDocsDetailedReport(int year, int month) {
        Instant startDate = LocalDate.of(year, month, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant endDate = LocalDate.of(year, month, 1).plusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        List<EmployeeNOC> approvedEmployeeNOCList = employeeNOCRepository.findAllApprovedRequestsBetweenDateRange(startDate, endDate);
        List<EmploymentCertificate> approvedEmploymentCertificateList = employmentCertificateRepository.findAllApprovedRequestsBetweenDateRange(
            startDate,
            endDate
        );
        List<SalaryCertificate> approvedSalaryCertificateList = salaryCertificateRepository.findAllApprovedRequestsBetweenDateRange(
            startDate.atZone(ZoneId.systemDefault()).toLocalDate(),
            endDate.atZone(ZoneId.systemDefault()).toLocalDate()
        );

        String sheetName = "Employee Docs Detailed Report";
        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        subTitleList.add("Employee Docs Detailed Report");
        subTitleList.add("Report Generated On : " + LocalDate.now());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("S/L");
        tableHeaderList.add("Ref No.");
        tableHeaderList.add("Doc Title");
        tableHeaderList.add("Requested Date");
        tableHeaderList.add("Requester PIN");
        tableHeaderList.add("Requester Name");
        tableHeaderList.add("Resolved By");
        tableHeaderList.add("Resolver PIN");
        tableHeaderList.add("Resolved Date");
        tableHeaderList.add("Duration while being resolved (In Minutes)");

        List<List<Object>> dataList = new ArrayList<>();

        if (
            approvedEmployeeNOCList.size() == 0 &&
            approvedEmploymentCertificateList.size() == 0 &&
            approvedSalaryCertificateList.size() == 0
        ) {
            List<Object> dataRow = new ArrayList<>();

            dataRow.add("-");
            dataRow.add("-");
            dataRow.add("-");
            dataRow.add("-");
            dataRow.add("-");
            dataRow.add("-");
            dataRow.add("-");
            dataRow.add("-");
            dataRow.add("-");
            dataRow.add("-");

            dataList.add(dataRow);
        } else {
            for (int i = 0; i < approvedEmployeeNOCList.size(); i++) {
                List<Object> dataRow = new ArrayList<>();
                dataRow.add(i + 1);
                dataRow.add(approvedEmployeeNOCList.get(i).getReferenceNumber());
                dataRow.add("Employee NOC");
                dataRow.add(approvedEmployeeNOCList.get(i).getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate());
                dataRow.add(approvedEmployeeNOCList.get(i).getEmployee().getPin());
                dataRow.add(approvedEmployeeNOCList.get(i).getEmployee().getFullName());

                Optional<Employee> employeeOptional = findEmployeeByEmail(approvedEmployeeNOCList.get(i).getGeneratedBy().getEmail());
                dataRow.add(employeeOptional.isPresent() ? employeeOptional.get().getFullName() : "-");
                dataRow.add(employeeOptional.isPresent() ? employeeOptional.get().getPin() : "-");

                dataRow.add(approvedEmployeeNOCList.get(i).getGeneratedAt().atZone(ZoneId.systemDefault()).toLocalDate());

                dataRow.add(
                    calculateDurationInMinutes(
                        approvedEmployeeNOCList.get(i).getCreatedAt(),
                        approvedEmployeeNOCList.get(i).getGeneratedAt()
                    )
                );
                dataList.add(dataRow);
            }

            for (int i = 0; i < approvedEmploymentCertificateList.size(); i++) {
                List<Object> dataRow = new ArrayList<>();
                dataRow.add(i + 1);
                dataRow.add(approvedEmploymentCertificateList.get(i).getReferenceNumber());
                dataRow.add("Employment Certificate");
                dataRow.add(approvedEmploymentCertificateList.get(i).getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate());
                dataRow.add(approvedEmploymentCertificateList.get(i).getEmployee().getPin());
                dataRow.add(approvedEmploymentCertificateList.get(i).getEmployee().getFullName());

                Optional<Employee> employeeOptional = findEmployeeByEmail(
                    approvedEmploymentCertificateList.get(i).getGeneratedBy().getEmail()
                );
                dataRow.add(employeeOptional.isPresent() ? employeeOptional.get().getFullName() : "-");
                dataRow.add(employeeOptional.isPresent() ? employeeOptional.get().getPin() : "-");

                dataRow.add(approvedEmploymentCertificateList.get(i).getGeneratedAt().atZone(ZoneId.systemDefault()).toLocalDate());

                dataRow.add(
                    calculateDurationInMinutes(
                        approvedEmploymentCertificateList.get(i).getCreatedAt(),
                        approvedEmploymentCertificateList.get(i).getGeneratedAt()
                    )
                );
                dataList.add(dataRow);
            }

            for (int i = 0; i < approvedSalaryCertificateList.size(); i++) {
                List<Object> dataRow = new ArrayList<>();
                dataRow.add(i + 1);
                dataRow.add(approvedSalaryCertificateList.get(i).getReferenceNumber());
                dataRow.add("Salary Certificate");
                dataRow.add(approvedSalaryCertificateList.get(i).getCreatedAt());
                dataRow.add(approvedSalaryCertificateList.get(i).getEmployee().getPin());
                dataRow.add(approvedSalaryCertificateList.get(i).getEmployee().getFullName());

                Optional<Employee> employeeOptional = findEmployeeByEmail(approvedSalaryCertificateList.get(i).getSanctionBy().getEmail());
                dataRow.add(employeeOptional.isPresent() ? employeeOptional.get().getFullName() : "-");
                dataRow.add(employeeOptional.isPresent() ? employeeOptional.get().getPin() : "-");

                dataRow.add(approvedSalaryCertificateList.get(i).getSanctionAt());

                dataRow.add(
                    calculateDurationInMinutes(
                        approvedSalaryCertificateList.get(i).getCreatedAt().atTime(10, 0, 0).atZone(ZoneId.systemDefault()).toInstant(),
                        approvedSalaryCertificateList.get(i).getSanctionAt().atTime(10, 0, 0).atZone(ZoneId.systemDefault()).toInstant()
                    )
                );
                dataList.add(dataRow);
            }
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(10);

        return exportXLPropertiesDTO;
    }

    private boolean saveData(
        List<EmployeeNOC> pendingEmployeeNOCList,
        List<EmploymentCertificate> pendingEmploymentCertificateList,
        List<SalaryCertificate> pendingSalaryCertificateList
    ) {
        try {
            for (int i = 0; i < pendingEmployeeNOCList.size(); i++) {
                employeeNOCRepository.save(pendingEmployeeNOCList.get(i));
            }
            for (int i = 0; i < pendingEmploymentCertificateList.size(); i++) {
                employmentCertificateRepository.save(pendingEmploymentCertificateList.get(i));
            }
            for (int i = 0; i < pendingSalaryCertificateList.size(); i++) {
                salaryCertificateRepository.save(pendingSalaryCertificateList.get(i));
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    private String calculateDurationInMinutes(Instant start, Instant end) {
        long durationInMinutes = (end.getEpochSecond() - start.getEpochSecond()) / 60;
        return String.valueOf(durationInMinutes);
    }

    private Optional<Employee> findEmployeeByEmail(String email) {
        List<Employee> employeeList = employeeRepository.findByEmail(email);
        // Sort the list by date of joining in descending order
        employeeList.sort((e1, e2) -> e2.getDateOfJoining().compareTo(e1.getDateOfJoining()));

        return employeeList.stream().findFirst();
    }
}
