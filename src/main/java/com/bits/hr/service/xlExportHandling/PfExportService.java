package com.bits.hr.service.xlExportHandling;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.userPfStatement.UserPfStatementService;
import com.bits.hr.service.userPfStatement.dto.UserPfStatementDTO;
import com.bits.hr.service.xlExportHandling.genericCsvExport.ExportCSV;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.util.MathRoundUtil;
import io.undertow.util.BadRequestException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PfExportService {

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private ExportCSV exportCSV;

    @Autowired
    private UserPfStatementService userPfStatementService;

    public List<Integer> getAllYearsForOverallPfAmountReport() {
        return pfCollectionRepository.getListOfYearsForOverallPfAmountReport();
    }

    // Excel Report

    public ExportXLPropertiesDTO xlExportAnnualPfReport(int year) {
        List<PfAccount> pfAccountList = pfAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "pin"));

        String sheetName = "Annual_PF_Report";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        subTitleList.add("Annual PF Report till year: " + year);
        subTitleList.add("Report Generated On " + today.toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("S/L");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Employee Name");
        tableHeaderList.add("Designation");
        tableHeaderList.add("Department");
        tableHeaderList.add("Unit");
        tableHeaderList.add("Opening Balance");

        tableHeaderList.add("Members Contribution (Total)");
        tableHeaderList.add("Members Interest (Total)");

        tableHeaderList.add("Employer Contribution (Total)");
        tableHeaderList.add("Employer Interest (Total)");

        tableHeaderList.add("Remarks");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < pfAccountList.size(); i++) {
            List<PfCollection> pfCollectionList = pfCollectionRepository.getAllPfCollectionTillYearByPin(
                pfAccountList.get(i).getPin(),
                year,
                12
            );

            List<Object> dataRow = new ArrayList<>();

            dataRow.add(i + 1);
            dataRow.add(pfAccountList.get(i).getPin());
            dataRow.add(pfAccountList.get(i).getAccHolderName());
            dataRow.add(pfAccountList.get(i).getDesignationName());
            dataRow.add(pfAccountList.get(i).getDepartmentName());
            dataRow.add(pfAccountList.get(i).getUnitName());
            dataRow.add(getOpeningBalance(pfCollectionList));

            dataRow.add(getTotalMembersContribution(pfCollectionList));
            dataRow.add(getTotalMembersInterest(pfCollectionList));

            dataRow.add(getTotalEmployerContribution(pfCollectionList));
            dataRow.add(getTotalEmployerInterest(pfCollectionList));

            dataRow.add("-");

            dataList.add(dataRow);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);
        exportXLPropertiesDTO.setHasAutoSummation(false);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO xlExportPfStatementReport(LocalDate localDate) throws IOException, BadRequestException {
        List<PfAccount> pfAccountList = pfAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "pin"));

        String sheetName = "pf_statement";

        List<String> titleList = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
        LocalDate firstDayOfPreviousYear = LocalDate.of(localDate.getYear() - 1, 1, 1);

        List<String> subTitleList = new ArrayList<>();
        subTitleList.add("PF Statement Report as of " + localDate.format(dateFormatter));
        subTitleList.add("Report Generated on " + LocalDate.now().format(dateFormatter));

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("S/L");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Designation");
        tableHeaderList.add("Department");
        tableHeaderList.add("Unit");
        tableHeaderList.add("DOJ");
        tableHeaderList.add("DOC");
        tableHeaderList.add(
            "Opening Balance as on " + String.valueOf(firstDayOfPreviousYear.format(dateFormatter)) + "(with Transfer BBL)"
        );
        tableHeaderList.add("Member's Contribution " + String.valueOf(firstDayOfPreviousYear.getYear()));
        tableHeaderList.add("BRAC IT Contribution " + String.valueOf(firstDayOfPreviousYear.getYear()));
        tableHeaderList.add(
            "Total Closing Balance as on " + String.valueOf(LocalDate.of(localDate.getYear() - 1, 12, 31).format(dateFormatter))
        );
        tableHeaderList.add("Member's Contribution for the year " + String.valueOf(localDate.getYear()));
        tableHeaderList.add("BRAC IT Contribution for the year " + String.valueOf(localDate.getYear()));
        tableHeaderList.add("Total Contribution for the year " + String.valueOf(localDate.getYear()));
        tableHeaderList.add("Interest on Member's Contribution ");
        tableHeaderList.add("Interest on Company Contribution ");
        tableHeaderList.add("Total Interest ");
        tableHeaderList.add("Closing Balance as on " + String.valueOf(localDate.format(dateFormatter)));

        List<List<Object>> dataList = new ArrayList<>();

        if (pfAccountList.size() == 0) {
            dataList.add(Collections.nCopies(tableHeaderList.size(), "-"));
        }
        for (int i = 0; i < pfAccountList.size(); i++) {
            UserPfStatementDTO userPfStatementDTO = userPfStatementService.getPfStatement(pfAccountList.get(i), localDate);

            List<Object> dataRow = new ArrayList<>();

            dataRow.add(i + 1);
            dataRow.add(userPfStatementDTO.getPin());
            dataRow.add(userPfStatementDTO.getFullName());
            dataRow.add(userPfStatementDTO.getDesignationName());
            dataRow.add(userPfStatementDTO.getDepartmentName());
            dataRow.add(pfAccountList.get(i).getUnitName());
            dataRow.add(pfAccountList.get(i).getDateOfJoining());
            dataRow.add(pfAccountList.get(i).getDateOfConfirmation());
            dataRow.add(userPfStatementDTO.getOpeningBalance());
            dataRow.add(userPfStatementDTO.getPreviousYearMemberPfContribution());
            dataRow.add(userPfStatementDTO.getPreviousYearCompanyPfContribution());
            dataRow.add(userPfStatementDTO.getOpeningAndPreviousYearContributionInTotal());
            dataRow.add(userPfStatementDTO.getSelectedYearMemberPfContribution());
            dataRow.add(userPfStatementDTO.getSelectedYearCompanyPfContribution());
            dataRow.add(userPfStatementDTO.getSelectedYearTotalPfContribution());
            dataRow.add(userPfStatementDTO.getTillSelectedMonthYearPfMemberInterest());
            dataRow.add(userPfStatementDTO.getTillSelectedMonthYearPfCompanyInterest());
            dataRow.add(userPfStatementDTO.getTotalTillSelectedMonthYearPfCompanyInterest());
            dataRow.add(userPfStatementDTO.getTotalClosingBalance());

            dataList.add(dataRow);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(tableHeaderList.size());
        return exportXLPropertiesDTO;
    }

    // CSV Reports

    public Optional<File> csvExportMonthlyPfCollections() throws IOException {
        List<PfCollection> pfCollectionList = pfCollectionRepository.getMonthlyPfCollection();

        String sheetName = "Monthly_PF_Collection";

        String[] headerRow = {
            "PIN",
            "Name",
            "PF_Code",
            "Designation",
            "Department",
            "Unit",
            "Year",
            "Month",
            "Employee_Contribution",
            "Employer_Contribution",
        };

        String[][] dataList = new String[pfCollectionList.size()][10];

        for (int i = 0; i < pfCollectionList.size(); i++) {
            dataList[i][0] = getPin(pfCollectionList.get(i));
            dataList[i][1] = getName(pfCollectionList.get(i));
            dataList[i][2] = getPfCode(pfCollectionList.get(i));
            dataList[i][3] = getDesignation(pfCollectionList.get(i));
            dataList[i][4] = getDepartment(pfCollectionList.get(i));
            dataList[i][5] = getUnit(pfCollectionList.get(i));
            dataList[i][6] = getYear(pfCollectionList.get(i));
            dataList[i][7] = getMonth(pfCollectionList.get(i));

            dataList[i][8] = getEmployeeContribution(pfCollectionList.get(i));
            dataList[i][9] = getEmployerContribution(pfCollectionList.get(i));
        }

        Optional<File> csvFile = exportCSV.createCSVFile(sheetName, headerRow, dataList, CSVFormat.DEFAULT);

        return csvFile;
    }

    public Optional<File> csvExportPfCollectionInterests() throws IOException {
        List<PfCollection> pfCollectionList = pfCollectionRepository.getMonthlyPfCollection();

        String sheetName = "PF_Collection_Interest";

        String[] headerRow = {
            "PIN",
            "Name",
            "PF_Code",
            "Designation",
            "Department",
            "Unit",
            "Year",
            "Month",
            "Employee_Interest",
            "Employer_Interest",
        };

        String[][] dataList = new String[pfCollectionList.size()][10];

        for (int i = 0; i < pfCollectionList.size(); i++) {
            dataList[i][0] = getPin(pfCollectionList.get(i));
            dataList[i][1] = getName(pfCollectionList.get(i));
            dataList[i][2] = getPfCode(pfCollectionList.get(i));
            dataList[i][3] = getDesignation(pfCollectionList.get(i));
            dataList[i][4] = getDepartment(pfCollectionList.get(i));
            dataList[i][5] = getUnit(pfCollectionList.get(i));
            dataList[i][6] = getYear(pfCollectionList.get(i));
            dataList[i][7] = getMonth(pfCollectionList.get(i));

            dataList[i][8] = getEmployeeInterest(pfCollectionList.get(i));
            dataList[i][9] = getEmployerInterest(pfCollectionList.get(i));
        }

        Optional<File> csvFile = exportCSV.createCSVFile(sheetName, headerRow, dataList, CSVFormat.DEFAULT);

        return csvFile;
    }

    public Optional<File> csvExportPfOpeningBalance() throws IOException {
        List<PfCollection> pfCollectionList = pfCollectionRepository.getOpeningBalancePfCollection();

        String sheetName = "PF_Collection_Opening_Balance";

        String[] headerRow = {
            "PIN",
            "Name",
            "PF Code",
            "Designation",
            "Department",
            "Unit",
            "Year",
            "Month",
            "Employee_Contribution",
            "Employee_Interest",
            "Employer_Contribution",
            "Employer_Interest",
        };

        String[][] dataList = new String[pfCollectionList.size()][12];

        for (int i = 0; i < pfCollectionList.size(); i++) {
            dataList[i][0] = getPin(pfCollectionList.get(i));
            dataList[i][1] = getName(pfCollectionList.get(i));
            dataList[i][2] = getPfCode(pfCollectionList.get(i));
            dataList[i][3] = getDesignation(pfCollectionList.get(i));
            dataList[i][4] = getDepartment(pfCollectionList.get(i));
            dataList[i][5] = getUnit(pfCollectionList.get(i));
            dataList[i][6] = getYear(pfCollectionList.get(i));
            dataList[i][7] = getMonth(pfCollectionList.get(i));

            dataList[i][8] = getEmployeeContribution(pfCollectionList.get(i));
            dataList[i][9] = getEmployeeInterest(pfCollectionList.get(i));
            dataList[i][10] = getEmployerContribution(pfCollectionList.get(i));
            dataList[i][11] = getEmployerInterest(pfCollectionList.get(i));
        }

        Optional<File> csvFile = exportCSV.createCSVFile(sheetName, headerRow, dataList, CSVFormat.DEFAULT);

        return csvFile;
    }

    public Optional<File> csvExportPfCombinedReport() throws IOException {
        List<PfCollection> pfCollectionList = pfCollectionRepository.getAllPfCollectionSortByPinYearAndMonth();

        String sheetName = "PF_Collection_Combined_Report";

        String[] headerRow = {
            "PIN",
            "Name",
            "PF Code",
            "Designation",
            "Department",
            "Unit",
            "Year",
            "Month",
            "Collection Type",
            "Employee_Contribution",
            "Employee_Interest",
            "Employer_Contribution",
            "Employer_Interest",
            "Total Opening Balance",
        };

        String[][] dataList = new String[pfCollectionList.size()][14];

        for (int i = 0; i < pfCollectionList.size(); i++) {
            dataList[i][0] = getPin(pfCollectionList.get(i));
            dataList[i][1] = getName(pfCollectionList.get(i));
            dataList[i][2] = getPfCode(pfCollectionList.get(i));
            dataList[i][3] = getDesignation(pfCollectionList.get(i));
            dataList[i][4] = getDepartment(pfCollectionList.get(i));
            dataList[i][5] = getUnit(pfCollectionList.get(i));
            dataList[i][6] = getYear(pfCollectionList.get(i));
            dataList[i][7] = getMonth(pfCollectionList.get(i));

            dataList[i][8] = getCollectionType(pfCollectionList.get(i));

            dataList[i][9] = getEmployeeContribution(pfCollectionList.get(i));
            dataList[i][10] = getEmployeeInterest(pfCollectionList.get(i));
            dataList[i][11] = getEmployerContribution(pfCollectionList.get(i));
            dataList[i][12] = getEmployerInterest(pfCollectionList.get(i));

            dataList[i][13] = getTotalOpeningBalance(pfCollectionList.get(i));
        }

        Optional<File> csvFile = exportCSV.createCSVFile(sheetName, headerRow, dataList, CSVFormat.DEFAULT);

        return csvFile;
    }

    public Optional<File> csvExportDetailedPfContributionReport() throws IOException {
        Pageable pageable = PageRequest.of(0, 1);
        List<PfCollection> veryFirstPfCollectionsByMonthAndYear = pfCollectionRepository.getVeryFirstPfCollections(pageable);
        List<PfCollection> lastPfCollectionsByMonthAndYear = pfCollectionRepository.getTheLastPfCollections(pageable);

        // If no PF collection is found then return a blank CSV.
        if (veryFirstPfCollectionsByMonthAndYear.size() == 0 || lastPfCollectionsByMonthAndYear.size() == 0) {
            return getBlankCSV("Contribution");
        }

        LocalDate firstMonth = LocalDate.of(
            veryFirstPfCollectionsByMonthAndYear.get(0).getYear(),
            veryFirstPfCollectionsByMonthAndYear.get(0).getMonth(),
            1
        );
        LocalDate lastMonth = LocalDate.of(
            lastPfCollectionsByMonthAndYear.get(0).getYear(),
            lastPfCollectionsByMonthAndYear.get(0).getMonth(),
            1
        );

        lastMonth = lastMonth.plusMonths(1);

        int totalMonths = 0;

        for (LocalDate start = firstMonth; start.isBefore(lastMonth); start = start.plusMonths(1)) {
            totalMonths++;
        }

        List<PfAccount> pfAccountList = pfAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "pin"));

        // Total Column Count
        // (s/l, Employee Name, Pin, Dept, DOJ, DOC) = 6 Columns
        // Opening Balance                           = 1 Column
        // Monthly(Member, Company, Total)           = (totalMonths * 3) Columns
        // Total contribution                        = 2 Columns
        // ----------------------------------------------------------------------
        // Total                                     = 9 + (3 * total month)

        int totalColumn = 6 + 1 + (totalMonths * 3) + 2;

        String sheetName = "Detailed PF Contribution Report";

        String[][] dataList = new String[pfAccountList.size() + 1][totalColumn];

        LocalDate startingMonth = firstMonth;

        String[] headerRow = getHeaderRow(totalColumn, totalMonths, startingMonth);

        dataList = getSubHeaderRow(totalMonths, startingMonth, dataList, "Contribution");

        for (int i = 0; i < pfAccountList.size(); i++) {
            List<PfCollection> pfCollectionList = pfCollectionRepository.getAllPfCollectionsByPfAccountId(pfAccountList.get(i).getId());

            dataList[i + 1][0] = String.valueOf(i + 1);
            dataList[i + 1][1] = pfAccountList.get(i).getAccHolderName();
            dataList[i + 1][2] = pfAccountList.get(i).getPin();
            dataList[i + 1][3] = pfAccountList.get(i).getDepartmentName();
            dataList[i + 1][4] = pfAccountList.get(i).getDateOfJoining().toString();
            dataList[i + 1][5] = pfAccountList.get(i).getDateOfConfirmation().toString();

            dataList[i + 1][6] = String.valueOf(getOpeningBalance(pfCollectionList));

            int index = 6;

            LocalDate currentMonthYear = firstMonth;
            double totalEmployeeContribution = 0;
            double totalEmployerContribution = 0;

            for (int j = 0; j < totalMonths; j++) {
                LocalDate monthYear = currentMonthYear;

                Optional<PfCollection> pfCollectionOptionalByMonthYear = pfCollectionList
                    .stream()
                    .filter(pfCollection ->
                        !pfCollection.getCollectionType().equals(PfCollectionType.OPENING_BALANCE) &&
                        pfCollection.getMonth().equals(monthYear.getMonthValue()) &&
                        pfCollection.getYear().equals(monthYear.getYear())
                    )
                    .findFirst();

                double employeeContribution;
                double employerContribution;
                double totalContribution;

                if (pfCollectionOptionalByMonthYear.isPresent()) {
                    employeeContribution = pfCollectionOptionalByMonthYear.get().getEmployeeContribution();
                    employerContribution = pfCollectionOptionalByMonthYear.get().getEmployerContribution();
                    totalContribution = employeeContribution + employeeContribution;
                } else {
                    employeeContribution = 0;
                    employerContribution = 0;
                    totalContribution = 0;
                }

                dataList[i + 1][index + 1] = String.valueOf(Math.round(employeeContribution));
                dataList[i + 1][index + 2] = String.valueOf(Math.round(employerContribution));
                dataList[i + 1][index + 3] = String.valueOf(Math.round(totalContribution));
                index += 3;
                currentMonthYear = currentMonthYear.plusMonths(1);

                totalEmployeeContribution += employeeContribution;
                totalEmployerContribution += employerContribution;
            }
            dataList[i + 1][index + 1] = String.valueOf(Math.round(totalEmployeeContribution));
            dataList[i + 1][index + 2] = String.valueOf(Math.round(totalEmployerContribution));
        }

        Optional<File> csvFile = exportCSV.createCSVFile(sheetName, headerRow, dataList, CSVFormat.DEFAULT);

        return csvFile;
    }

    public Optional<File> csvExportDetailedPfInterestReport() throws IOException {
        Pageable pageable = PageRequest.of(0, 1);
        List<PfCollection> veryFirstPfCollectionsByMonthAndYear = pfCollectionRepository.getVeryFirstPfCollections(pageable);
        List<PfCollection> lastPfCollectionsByMonthAndYear = pfCollectionRepository.getTheLastPfCollections(pageable);

        // If no PF collection is found then return a blank CSV.
        if (veryFirstPfCollectionsByMonthAndYear.size() == 0 || lastPfCollectionsByMonthAndYear.size() == 0) {
            return getBlankCSV("Interest");
        }

        LocalDate firstMonth = LocalDate.of(
            veryFirstPfCollectionsByMonthAndYear.get(0).getYear(),
            veryFirstPfCollectionsByMonthAndYear.get(0).getMonth(),
            1
        );
        LocalDate lastMonth = LocalDate.of(
            lastPfCollectionsByMonthAndYear.get(0).getYear(),
            lastPfCollectionsByMonthAndYear.get(0).getMonth(),
            1
        );

        lastMonth = lastMonth.plusMonths(1);

        int totalMonths = 0;

        for (LocalDate start = firstMonth; start.isBefore(lastMonth); start = start.plusMonths(1)) {
            totalMonths++;
        }

        List<PfAccount> pfAccountList = pfAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "pin"));

        // Total Column Count
        // (s/l, Employee Name, Pin, Dept, DOJ, DOC) = 6 Columns
        // Opening Balance                           = 1 Column
        // Monthly(Member, Company, Total)           = (totalMonths * 3) Columns
        // Total contribution                        = 2 Columns
        // ----------------------------------------------------------------------
        // Total                                     = 9 + (3 * total month)

        int totalColumn = 6 + 1 + (totalMonths * 3) + 2;

        String sheetName = "Detailed PF Interest Report";

        String[][] dataList = new String[pfAccountList.size() + 1][totalColumn];

        LocalDate startingMonth = firstMonth;

        String[] headerRow = getHeaderRow(totalColumn, totalMonths, startingMonth);

        dataList = getSubHeaderRow(totalMonths, startingMonth, dataList, "Interest");

        for (int i = 0; i < pfAccountList.size(); i++) {
            List<PfCollection> pfCollectionList = pfCollectionRepository.getAllPfCollectionsByPfAccountId(pfAccountList.get(i).getId());

            dataList[i + 1][0] = String.valueOf(i + 1);
            dataList[i + 1][1] = pfAccountList.get(i).getAccHolderName();
            dataList[i + 1][2] = pfAccountList.get(i).getPin();
            dataList[i + 1][3] = pfAccountList.get(i).getDepartmentName();
            dataList[i + 1][4] = pfAccountList.get(i).getDateOfJoining().toString();
            dataList[i + 1][5] = pfAccountList.get(i).getDateOfConfirmation().toString();

            dataList[i + 1][6] = String.valueOf(getOpeningBalance(pfCollectionList));

            int index = 6;

            LocalDate currentMonthYear = firstMonth;
            double totalEmployeeInterest = 0;
            double totalEmployerInterest = 0;

            for (int j = 0; j < totalMonths; j++) {
                LocalDate monthYear = currentMonthYear;

                Optional<PfCollection> pfCollectionOptionalByMonthYear = pfCollectionList
                    .stream()
                    .filter(pfCollection ->
                        !pfCollection.getCollectionType().equals(PfCollectionType.OPENING_BALANCE) &&
                        pfCollection.getMonth().equals(monthYear.getMonthValue()) &&
                        pfCollection.getYear().equals(monthYear.getYear())
                    )
                    .findFirst();

                double employeeInterest;
                double employerInterest;
                double totalInterest;

                if (pfCollectionOptionalByMonthYear.isPresent()) {
                    employeeInterest = pfCollectionOptionalByMonthYear.get().getEmployeeInterest();
                    employerInterest = pfCollectionOptionalByMonthYear.get().getEmployerInterest();
                    totalInterest = employerInterest + employeeInterest;
                } else {
                    employeeInterest = 0;
                    employerInterest = 0;
                    totalInterest = 0;
                }

                dataList[i + 1][index + 1] = String.valueOf(Math.round(employeeInterest));
                dataList[i + 1][index + 2] = String.valueOf(Math.round(employerInterest));
                dataList[i + 1][index + 3] = String.valueOf(Math.round(totalInterest));
                index += 3;
                currentMonthYear = currentMonthYear.plusMonths(1);

                totalEmployeeInterest += employeeInterest;
                totalEmployerInterest += employerInterest;
            }
            dataList[i + 1][index + 1] = String.valueOf(Math.round(totalEmployeeInterest));
            dataList[i + 1][index + 2] = String.valueOf(Math.round(totalEmployerInterest));
        }

        Optional<File> csvFile = exportCSV.createCSVFile(sheetName, headerRow, dataList, CSVFormat.DEFAULT);

        return csvFile;
    }

    //PF Collection by within year & month range
    public ExportXLPropertiesDTO exportMonthlyPfCollectionsBetweenDateRange(int startYear, int startMonth, int endYear, int endMonth) {
        try {
            String startingMonth = java.time.Month.of(startMonth).toString();
            String endingMonth = java.time.Month.of(endMonth).toString();

            List<PfCollection> pfCollections = pfCollectionRepository.getPfCollectionBetweenDateRange(
                startYear,
                endYear,
                startMonth,
                endMonth
            );

            List<List<Object>> dataList = new ArrayList<>();
            String sheetName = "PF-Collection-Report-Between-Date-Range";
            List<String> titleList = new ArrayList<>();
            List<String> subTitleList = new ArrayList<>();
            LocalDate today = LocalDate.now();

            subTitleList.add(
                "PF Report from " +
                startingMonth.substring(0, 1) +
                startingMonth.substring(1).toLowerCase() +
                ", " +
                startYear +
                " to " +
                endingMonth.substring(0, 1) +
                endingMonth.substring(1).toLowerCase() +
                ", " +
                endYear
            );
            subTitleList.add("Report Generated On " + today.toString());

            List<String> tableHeaderList = new ArrayList<>();
            tableHeaderList.add("S/L");
            tableHeaderList.add("PIN");
            tableHeaderList.add("Employee Name");
            tableHeaderList.add("Designation");
            tableHeaderList.add("Department");
            tableHeaderList.add("Unit");
            tableHeaderList.add("Year");
            tableHeaderList.add("Month");
            tableHeaderList.add("Members Contribution");
            tableHeaderList.add("Members Interest");
            tableHeaderList.add("Employer Contribution");
            tableHeaderList.add("Employer Interest");

            for (int i = 0; i < pfCollections.size(); i++) {
                List<Object> dataRow = new ArrayList<>();

                dataRow.add(i + 1);
                dataRow.add(pfCollections.get(i).getPfAccount().getPin());
                dataRow.add(pfCollections.get(i).getPfAccount().getAccHolderName());
                dataRow.add(pfCollections.get(i).getPfAccount().getDesignationName());
                dataRow.add(pfCollections.get(i).getPfAccount().getDepartmentName());
                dataRow.add(pfCollections.get(i).getPfAccount().getUnitName());
                dataRow.add(pfCollections.get(i).getYear());
                dataRow.add(pfCollections.get(i).getMonth());
                dataRow.add(pfCollections.get(i).getEmployeeContribution());
                dataRow.add(pfCollections.get(i).getEmployeeInterest());
                dataRow.add(pfCollections.get(i).getEmployerContribution());
                dataRow.add(pfCollections.get(i).getEmployerInterest());

                dataList.add(dataRow);
            }

            ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
            exportXLPropertiesDTO.setSheetName(sheetName);
            exportXLPropertiesDTO.setTableDataListOfList(dataList);
            exportXLPropertiesDTO.setTitleList(titleList);
            exportXLPropertiesDTO.setSubTitleList(subTitleList);
            exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
            exportXLPropertiesDTO.setTableDataListOfList(dataList);
            exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);
            exportXLPropertiesDTO.setHasAutoSummation(false);

            return exportXLPropertiesDTO;
        } catch (Exception e) {
            log.error((e));
            throw new RuntimeException();
        }
    }

    // Helper Methods

    private String[] getHeaderRow(int totalColumn, int totalMonths, LocalDate monthStartFrom) {
        LocalDate currentMonth = monthStartFrom;

        String[] headerRow = new String[totalColumn];
        headerRow[0] = "";
        headerRow[1] = "";
        headerRow[2] = "";
        headerRow[3] = "";
        headerRow[4] = "";
        headerRow[5] = "";

        headerRow[6] = "";

        int index = 6;

        for (int j = 0; j < totalMonths; j++) {
            headerRow[index + 1] = "" + currentMonth.getMonth() + "-" + currentMonth.getYear();
            headerRow[index + 2] = "" + currentMonth.getMonth() + "-" + currentMonth.getYear();
            headerRow[index + 3] = "" + currentMonth.getMonth() + "-" + currentMonth.getYear();
            index += 3;
            currentMonth = currentMonth.plusMonths(1);
        }
        headerRow[index + 1] = "";
        headerRow[index + 2] = "";

        return headerRow;
    }

    private String[][] getSubHeaderRow(int totalMonths, LocalDate monthStartFrom, String[][] dataList, String collectionName) {
        LocalDate currentMonth = monthStartFrom;

        dataList[0][0] = "Sl. No.";
        dataList[0][1] = "Employee Name";
        dataList[0][2] = "PIN";
        dataList[0][3] = "Department";
        dataList[0][4] = "Date Of Joining";
        dataList[0][5] = "Date Of Confirmation";

        dataList[0][6] = "Opening Balance";

        int index = 6;

        for (int j = 0; j < totalMonths; j++) {
            dataList[0][index + 1] = "Member";
            dataList[0][index + 2] = "Company";
            dataList[0][index + 3] = "Total";
            index += 3;
            currentMonth.plusMonths(1);
        }

        dataList[0][index + 1] = "Total Member " + collectionName;
        dataList[0][index + 2] = "Total BRAC IT " + collectionName;

        return dataList;
    }

    private Optional<File> getBlankCSV(String collectionName) throws IOException {
        String sheetName = "Detailed PF " + collectionName + " Report";

        String[] headerRow = new String[12];
        headerRow[0] = "";
        headerRow[1] = "";
        headerRow[2] = "";
        headerRow[3] = "";
        headerRow[4] = "";
        headerRow[5] = "";

        headerRow[6] = "";

        headerRow[7] = "-";
        headerRow[8] = "-";
        headerRow[9] = "-";

        headerRow[10] = "";
        headerRow[11] = "";

        String[][] dataList = new String[1][12];

        dataList[0][0] = "Sl. No.";
        dataList[0][1] = "Employee Name";
        dataList[0][2] = "PIN";
        dataList[0][3] = "Department";
        dataList[0][4] = "Date Of Joining";
        dataList[0][5] = "Date Of Confirmation";

        dataList[0][6] = "Opening Balance";

        dataList[0][7] = "Member";
        dataList[0][8] = "Company";
        dataList[0][9] = "Total";

        dataList[0][10] = "Total Member " + collectionName;
        dataList[0][11] = "Total BRAC IT " + collectionName;

        Optional<File> csvFile = exportCSV.createCSVFile(sheetName, headerRow, dataList, CSVFormat.DEFAULT);

        return csvFile;
    }

    private String getCollectionType(PfCollection pfCollection) {
        PfCollectionType collectionType = pfCollection.getCollectionType();

        switch (collectionType) {
            case ARREAR:
                return "Arrear";
            case ADVANCE:
                return "Advance";
            case CASH:
                return "Cash";
            case MONTHLY:
                return "Monthly";
            default:
                return "Opening Balance";
        }
    }

    private int getTotalEmployerInterest(List<PfCollection> pfCollectionList) {
        try {
            double employerInterest = 0;

            for (int i = 0; i < pfCollectionList.size(); i++) {
                if (!pfCollectionList.get(i).getCollectionType().equals(PfCollectionType.OPENING_BALANCE)) {
                    employerInterest += pfCollectionList.get(i).getEmployerInterest();
                }
            }
            return MathRoundUtil.round(employerInterest);
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    private int getTotalEmployerContribution(List<PfCollection> pfCollectionList) {
        try {
            double employerContribution = 0;

            for (int i = 0; i < pfCollectionList.size(); i++) {
                if (!pfCollectionList.get(i).getCollectionType().equals(PfCollectionType.OPENING_BALANCE)) {
                    employerContribution += pfCollectionList.get(i).getEmployerContribution();
                }
            }
            return MathRoundUtil.round(employerContribution);
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    private int getTotalMembersInterest(List<PfCollection> pfCollectionList) {
        try {
            double membersInterest = 0;

            for (int i = 0; i < pfCollectionList.size(); i++) {
                if (!pfCollectionList.get(i).getCollectionType().equals(PfCollectionType.OPENING_BALANCE)) {
                    membersInterest += pfCollectionList.get(i).getEmployeeInterest();
                }
            }
            return MathRoundUtil.round(membersInterest);
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    private int getTotalMembersContribution(List<PfCollection> pfCollectionList) {
        try {
            double employeeContribution = 0;

            for (int i = 0; i < pfCollectionList.size(); i++) {
                if (!pfCollectionList.get(i).getCollectionType().equals(PfCollectionType.OPENING_BALANCE)) {
                    employeeContribution += pfCollectionList.get(i).getEmployeeContribution();
                }
            }
            return MathRoundUtil.round(employeeContribution);
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    private int getOpeningBalance(List<PfCollection> pfCollectionList) {
        try {
            Optional<PfCollection> openingBalanceOptional = pfCollectionList
                .stream()
                .filter(pfCollection -> pfCollection.getCollectionType().equals(PfCollectionType.OPENING_BALANCE))
                .findFirst();

            if (openingBalanceOptional.isPresent()) {
                double employeeContribution = openingBalanceOptional.get().getEmployeeContribution() != null
                    ? openingBalanceOptional.get().getEmployeeContribution()
                    : 0d;
                double employeeInterest = openingBalanceOptional.get().getEmployeeInterest() != null
                    ? openingBalanceOptional.get().getEmployeeInterest()
                    : 0d;

                double employerContribution = openingBalanceOptional.get().getEmployerContribution() != null
                    ? openingBalanceOptional.get().getEmployerContribution()
                    : 0d;
                double employerInterest = openingBalanceOptional.get().getEmployerInterest() != null
                    ? openingBalanceOptional.get().getEmployerInterest()
                    : 0d;

                double openingBalance = employeeContribution + employeeInterest + employerContribution + employerInterest;

                return MathRoundUtil.round(openingBalance);
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    private String getTotalOpeningBalance(PfCollection pfCollection) {
        try {
            if (pfCollection.getCollectionType().equals(PfCollectionType.OPENING_BALANCE)) {
                double employeeContribution = pfCollection.getEmployeeContribution() != null ? pfCollection.getEmployeeContribution() : 0d;
                double employeeInterest = pfCollection.getEmployeeInterest() != null ? pfCollection.getEmployeeInterest() : 0d;

                double employerContribution = pfCollection.getEmployerContribution() != null ? pfCollection.getEmployerContribution() : 0d;
                double employerInterest = pfCollection.getEmployerInterest() != null ? pfCollection.getEmployerInterest() : 0d;

                double openingBalance = employeeContribution + employeeInterest + employerContribution + employerInterest;

                return String.valueOf(MathRoundUtil.round(openingBalance));
            } else {
                return "0";
            }
        } catch (Exception e) {
            log.error(e);
            return "0";
        }
    }

    String getPin(PfCollection pfCollection) {
        try {
            return pfCollection.getPfAccount().getPin();
        } catch (Exception e) {
            return "-";
        }
    }

    String getName(PfCollection pfCollection) {
        try {
            return pfCollection.getPfAccount().getAccHolderName();
        } catch (Exception e) {
            return "-";
        }
    }

    String getPfCode(PfCollection pfCollection) {
        try {
            return pfCollection.getPfAccount().getPfCode();
        } catch (Exception e) {
            return "-";
        }
    }

    String getDesignation(PfCollection pfCollection) {
        try {
            return pfCollection.getPfAccount().getDesignationName();
        } catch (Exception e) {
            return "-";
        }
    }

    String getDepartment(PfCollection pfCollection) {
        try {
            return pfCollection.getPfAccount().getDepartmentName();
        } catch (Exception e) {
            return "-";
        }
    }

    String getUnit(PfCollection pfCollection) {
        try {
            return pfCollection.getPfAccount().getUnitName();
        } catch (Exception e) {
            return "-";
        }
    }

    String getYear(PfCollection pfCollection) {
        try {
            return pfCollection.getYear().toString();
        } catch (Exception e) {
            return "-";
        }
    }

    String getMonth(PfCollection pfCollection) {
        try {
            return Month.fromInteger(pfCollection.getMonth()).toString();
        } catch (Exception e) {
            return "-";
        }
    }

    String getEmployeeContribution(PfCollection pfCollection) {
        try {
            return pfCollection.getEmployeeContribution().toString();
        } catch (Exception e) {
            return "0";
        }
    }

    String getEmployeeInterest(PfCollection pfCollection) {
        try {
            return pfCollection.getEmployeeInterest().toString();
        } catch (Exception e) {
            return "0";
        }
    }

    String getEmployerContribution(PfCollection pfCollection) {
        try {
            return pfCollection.getEmployerContribution().toString();
        } catch (Exception e) {
            return "0";
        }
    }

    String getEmployerInterest(PfCollection pfCollection) {
        try {
            return pfCollection.getEmployerInterest().toString();
        } catch (Exception e) {
            return "0";
        }
    }
}
