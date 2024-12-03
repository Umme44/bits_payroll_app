package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.util.MathRoundUtil;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class InsuranceDataExportInExcelService {

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private EmployeeResignationService employeeResignationService;

    public ExportXLPropertiesDTO exportInclusionList(String searchText, Integer year, Integer month) throws IOException {
        Instant searchFrom = null;
        Instant searchTo = null;
        if (year != 0 && month != 0) {
            searchFrom = LocalDate.of(year, month, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            searchTo =
                LocalDate
                    .of(year, month, Month.of(month).length(searchFrom.atZone(ZoneId.systemDefault()).toLocalDate().isLeapYear()))
                    .atTime(23, 59, 59)
                    .atZone(ZoneId.systemDefault())
                    .toInstant();
        }
        if (year != 0 && month == 0) {
            searchFrom = LocalDate.of(year, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            searchTo = LocalDate.of(year, 12, 31).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        }

        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAllInclusionRegistration(
            searchText,
            searchFrom,
            searchTo
        );

        insuranceRegistrationList.sort((r1, r2) -> {
            if (r1.getEmployee().getPin().equals(r2.getEmployee().getPin())) {
                return r1.getInsuranceRelation().ordinal() - r2.getInsuranceRelation().ordinal();
            } else {
                int r1Pin = Integer.parseInt(r1.getEmployee().getPin().trim());
                int r2Pin = Integer.parseInt(r2.getEmployee().getPin().trim());

                return r1Pin - r2Pin;
            }
        });

        String sheetName = "Insurance Registration Inclusion List";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        subTitleList.add("Insurance Registration Inclusion List");
        subTitleList.add("Report Generated On : " + LocalDate.now());

        List<String> tableHeaderList = new ArrayList<>();

        tableHeaderList.add("S/L");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Relationship");
        tableHeaderList.add("Date of Birth");
        tableHeaderList.add("Effective Date");
        tableHeaderList.add("Current Basic Salary");
        tableHeaderList.add("Bank account no.");
        tableHeaderList.add("Mobile no.");

        List<List<Object>> dataList = new ArrayList<>();

        // Empty value if size of list = 0
        if (insuranceRegistrationList.size() == 0) {
            List<Object> dataRow = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                dataRow.add("-");
            }
            dataList.add(dataRow);
        } else {
            for (int i = 0; i < insuranceRegistrationList.size(); i++) {
                List<Object> dataRow = new ArrayList<>();

                dataRow.add(i + 1);
                dataRow.add(getPin(insuranceRegistrationList.get(i)));
                dataRow.add(getName(insuranceRegistrationList.get(i)));
                dataRow.add(getRelation(insuranceRegistrationList.get(i)));

                LocalDate dob = insuranceRegistrationList.get(i).getDateOfBirth();
                dataRow.add(dob != null ? dob : "");

                LocalDate effectiveDate = getEffectiveDate(insuranceRegistrationList.get(i));
                dataRow.add(effectiveDate != null ? effectiveDate : "");

                int currentBasic = insuranceRegistrationList.get(i).getEmployee().getMainGrossSalary() != null
                    ? getCurrentBasicSalary(insuranceRegistrationList.get(i).getEmployee())
                    : 0;
                dataRow.add(currentBasic);

                String bankAccountNo = insuranceRegistrationList.get(i).getEmployee().getBankAccountNo();
                dataRow.add(bankAccountNo != null ? bankAccountNo : "-");

                String mobileNo = insuranceRegistrationList.get(i).getEmployee().getOfficialContactNo();
                dataRow.add(mobileNo != null ? mobileNo : "-");

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

    public ExportXLPropertiesDTO exportApprovedList(String searchText, Integer year, Integer month) throws IOException {
        Instant searchFrom = null;
        Instant searchTo = null;
        if (year != 0 && month != 0) {
            searchFrom = LocalDate.of(year, month, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            searchTo =
                LocalDate
                    .of(year, month, Month.of(month).length(searchFrom.atZone(ZoneId.systemDefault()).toLocalDate().isLeapYear()))
                    .atTime(23, 59, 59)
                    .atZone(ZoneId.systemDefault())
                    .toInstant();
        }
        if (year != 0 && month == 0) {
            searchFrom = LocalDate.of(year, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            searchTo = LocalDate.of(year, 12, 31).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        }

        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAllApprovedRegistration(
            searchText,
            searchFrom,
            searchTo
        );

        insuranceRegistrationList.sort((r1, r2) -> {
            if (r1.getEmployee().getPin().equals(r2.getEmployee().getPin())) {
                return r1.getInsuranceRelation().ordinal() - r2.getInsuranceRelation().ordinal();
            } else {
                int r1Pin = Integer.parseInt(r1.getEmployee().getPin().trim());
                int r2Pin = Integer.parseInt(r2.getEmployee().getPin().trim());

                return r1Pin - r2Pin;
            }
        });

        String sheetName = "Insurance Registration Approved List";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        subTitleList.add("Insurance Registration Approved List");
        subTitleList.add("Report Generated On : " + LocalDate.now());

        List<String> tableHeaderList = new ArrayList<>();

        tableHeaderList.add("S/L");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Relationship");
        tableHeaderList.add("Date of Birth");
        tableHeaderList.add("Effective Date");

        List<List<Object>> dataList = new ArrayList<>();

        // Empty value if size of list = 0
        if (insuranceRegistrationList.size() == 0) {
            List<Object> dataRow = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                dataRow.add("-");
            }
            dataList.add(dataRow);
        } else {
            for (int i = 0; i < insuranceRegistrationList.size(); i++) {
                List<Object> dataRow = new ArrayList<>();

                dataRow.add(i + 1);
                dataRow.add(getPin(insuranceRegistrationList.get(i)));
                dataRow.add(getName(insuranceRegistrationList.get(i)));
                dataRow.add(getRelation(insuranceRegistrationList.get(i)));

                LocalDate dob = insuranceRegistrationList.get(i).getDateOfBirth();
                dataRow.add(dob != null ? dob : "");

                LocalDate effectiveDate = getEffectiveDate(insuranceRegistrationList.get(i));
                dataRow.add(effectiveDate != null ? effectiveDate : "");

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

    public ExportXLPropertiesDTO exportExcludedList(
        String searchText,
        Integer year,
        Integer month,
        boolean isCancelled,
        boolean isSeperated
    ) throws IOException {
        Instant searchFrom = null;
        Instant searchTo = null;
        if (year != 0 && month != 0) {
            searchFrom = LocalDate.of(year, month, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            searchTo =
                LocalDate
                    .of(year, month, Month.of(month).length(searchFrom.atZone(ZoneId.systemDefault()).toLocalDate().isLeapYear()))
                    .atTime(23, 59, 59)
                    .atZone(ZoneId.systemDefault())
                    .toInstant();
        }
        if (year != 0 && month == 0) {
            searchFrom = LocalDate.of(year, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            searchTo = LocalDate.of(year, 12, 31).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        }

        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAllExcludedRegistration(
            searchText,
            searchFrom,
            searchTo,
            isCancelled,
            isSeperated
        );

        insuranceRegistrationList.sort((r1, r2) -> {
            if (r1.getEmployee().getPin().equals(r2.getEmployee().getPin())) {
                return r1.getInsuranceRelation().ordinal() - r2.getInsuranceRelation().ordinal();
            } else {
                int r1Pin = Integer.parseInt(r1.getEmployee().getPin().trim());
                int r2Pin = Integer.parseInt(r2.getEmployee().getPin().trim());

                return r1Pin - r2Pin;
            }
        });

        String sheetName;

        if (isCancelled && !isSeperated) {
            sheetName = "Insurance Registration Cancelled List";
        } else if (!isCancelled && isSeperated) {
            sheetName = "Insurance Registration Seperated List";
        } else {
            sheetName = "Insurance Registration Excluded List";
        }

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();

        if (isCancelled && !isSeperated) {
            subTitleList.add("Insurance Registration Cancelled List");
        } else if (!isCancelled && isSeperated) {
            subTitleList.add("Insurance Registration Seperated List");
        } else {
            subTitleList.add("Insurance Registration Excluded List");
        }
        subTitleList.add("Report Generated On : " + LocalDate.now());

        List<String> tableHeaderList = new ArrayList<>();

        tableHeaderList.add("S/L");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Relationship");
        tableHeaderList.add("Date of Birth");
        tableHeaderList.add("Effective Date");
        tableHeaderList.add("Last working day");
        tableHeaderList.add("Reason");

        List<List<Object>> dataList = new ArrayList<>();

        // Empty value if size of list = 0
        if (insuranceRegistrationList.size() == 0) {
            List<Object> dataRow = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                dataRow.add("-");
            }
            dataList.add(dataRow);
        } else {
            for (int i = 0; i < insuranceRegistrationList.size(); i++) {
                List<Object> dataRow = new ArrayList<>();

                dataRow.add(i + 1);
                dataRow.add(getPin(insuranceRegistrationList.get(i)));
                dataRow.add(getName(insuranceRegistrationList.get(i)));
                dataRow.add(getRelation(insuranceRegistrationList.get(i)));

                LocalDate dob = insuranceRegistrationList.get(i).getDateOfBirth();
                dataRow.add(dob != null ? dob : "");

                LocalDate effectiveDate = getEffectiveDate(insuranceRegistrationList.get(i));
                dataRow.add(effectiveDate != null ? effectiveDate : "");

                LocalDate lastWorkingDay = getLastWorkingDay(insuranceRegistrationList.get(i));
                dataRow.add(lastWorkingDay != null ? lastWorkingDay : "-");

                dataRow.add(insuranceRegistrationList.get(i).getUnapprovalReason());

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

    public ExportXLPropertiesDTO exportAllApprovedInsuranceRegistrations() throws IOException {
        try {
            List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAllApprovedRegistration(
                "",
                null,
                null
            );

            // sorting by PIN and relation
            insuranceRegistrationList.sort((r1, r2) -> {
                if (r1.getEmployee().getPin().equals(r2.getEmployee().getPin())) {
                    return r1.getInsuranceRelation().ordinal() - r2.getInsuranceRelation().ordinal();
                } else {
                    int r1Pin = Integer.parseInt(r1.getEmployee().getPin().trim());
                    int r2Pin = Integer.parseInt(r2.getEmployee().getPin().trim());

                    return r1Pin - r2Pin;
                }
            });

            String sheetName = "Approved Insurance Registrations";

            List<String> titleList = new ArrayList<>();

            List<String> subTitleList = new ArrayList<>();
            subTitleList.add("Approved Insurance Registrations");
            subTitleList.add("Report Generated On : " + LocalDate.now());

            List<String> tableHeaderList = new ArrayList<>();

            tableHeaderList.add("S/L");
            tableHeaderList.add("Primary Key");
            tableHeaderList.add("Name");
            tableHeaderList.add("PIN");
            tableHeaderList.add("Relationship");
            tableHeaderList.add("Insurance Card ID");

            List<List<Object>> dataList = new ArrayList<>();

            Integer count = 1;

            if (insuranceRegistrationList.size() == 0) {
                List<Object> data = new ArrayList<>();

                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");

                dataList.add(data);
            }

            for (InsuranceRegistration insuranceRegistration : insuranceRegistrationList) {
                List<Object> data = new ArrayList<>();

                data.add(count++);
                data.add(String.valueOf(insuranceRegistration.getId()));
                data.add(insuranceRegistration.getName());
                data.add(insuranceRegistration.getEmployee().getPin());
                data.add(insuranceRegistration.getInsuranceRelation().name());
                data.add(insuranceRegistration.getInsuranceId());

                dataList.add(data);
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
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException(e.getMessage(), "InsuranceRegistration", "internalServerError");
        }
    }

    public ExportXLPropertiesDTO exportInsuranceRegistrations() throws IOException {
        try {
            List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();

            // sorting by PIN and relation
            insuranceRegistrationList.sort((r1, r2) -> {
                if (r1.getEmployee().getPin().equals(r2.getEmployee().getPin())) {
                    return r1.getInsuranceRelation().ordinal() - r2.getInsuranceRelation().ordinal();
                } else {
                    int r1Pin = Integer.parseInt(r1.getEmployee().getPin().trim());
                    int r2Pin = Integer.parseInt(r2.getEmployee().getPin().trim());

                    return r1Pin - r2Pin;
                }
            });

            String sheetName = "All Insurance Registrations";

            List<String> titleList = new ArrayList<>();

            List<String> subTitleList = new ArrayList<>();
            subTitleList.add("Approved Insurance Registrations");
            subTitleList.add("Report Generated On : " + LocalDate.now());

            List<String> tableHeaderList = new ArrayList<>();

            tableHeaderList.add("S/L");
            tableHeaderList.add("Primary Key");
            tableHeaderList.add("Status");
            tableHeaderList.add("Name");
            tableHeaderList.add("PIN");
            tableHeaderList.add("Relationship");
            tableHeaderList.add("Insurance Card ID");

            List<List<Object>> dataList = new ArrayList<>();

            Integer count = 1;

            if (insuranceRegistrationList.size() == 0) {
                List<Object> data = new ArrayList<>();

                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");

                dataList.add(data);
            }

            for (InsuranceRegistration insuranceRegistration : insuranceRegistrationList) {
                List<Object> data = new ArrayList<>();

                data.add(count++);
                data.add(String.valueOf(insuranceRegistration.getId()));
                data.add(String.valueOf(insuranceRegistration.getInsuranceStatus()));
                data.add(insuranceRegistration.getName());
                data.add(insuranceRegistration.getEmployee().getPin());
                data.add(insuranceRegistration.getInsuranceRelation().name());
                data.add(insuranceRegistration.getInsuranceId() == null ? "-" : insuranceRegistration.getInsuranceId());

                dataList.add(data);
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
        } catch (Exception e) {
            log.error(e);
            throw new BadRequestAlertException(e.getMessage(), "InsuranceRegistration", "internalServerError");
        }
    }

    String getPin(InsuranceRegistration insuranceRegistration) {
        try {
            return insuranceRegistration.getEmployee().getPin();
        } catch (Exception e) {
            return "-";
        }
    }

    String getName(InsuranceRegistration insuranceRegistration) {
        try {
            return insuranceRegistration.getName();
        } catch (Exception e) {
            return "-";
        }
    }

    int getCurrentBasicSalary(Employee employee) {
        return MathRoundUtil.round(employee.getMainGrossSalary() * .60);
    }

    private String getRelation(InsuranceRegistration insuranceRegistration) {
        InsuranceRelation relation = insuranceRegistration.getInsuranceRelation();

        if (relation == InsuranceRelation.SELF) {
            return "Self";
        } else if (relation == InsuranceRelation.SPOUSE) {
            return "Spouse";
        } else if (relation == InsuranceRelation.CHILD_1) {
            return "Child 1";
        } else if (relation == InsuranceRelation.CHILD_2) {
            return "Child 2";
        } else {
            return "Child 3";
        }
    }

    LocalDate getLastWorkingDay(InsuranceRegistration insuranceRegistration) {
        if (insuranceRegistration.getInsuranceRelation() == InsuranceRelation.SELF) {
            Optional<LocalDate> lastWorkingDayOptional = employeeResignationService.getLastWorkingDay(
                insuranceRegistration.getEmployee().getId()
            );

            if (!lastWorkingDayOptional.isPresent()) {
                return null;
            } else {
                return lastWorkingDayOptional.get();
            }
        } else {
            return null;
        }
    }

    LocalDate getEffectiveDate(InsuranceRegistration insuranceRegistration) {
        if (insuranceRegistration.getInsuranceRelation().equals(InsuranceRelation.SELF)) {
            // If Contractual Employee -> Date of joining
            // If Regular Confirmed -> Date of confirmation
            if (insuranceRegistration.getEmployee().getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)) {
                return insuranceRegistration.getEmployee().getDateOfJoining();
            } else {
                return insuranceRegistration.getEmployee().getDateOfConfirmation();
            }
        } else {
            // If relation != self -> created at
            return insuranceRegistration.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }
}
