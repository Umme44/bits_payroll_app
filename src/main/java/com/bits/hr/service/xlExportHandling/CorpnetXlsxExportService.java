package com.bits.hr.service.xlExportHandling;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorpnetXlsxExportService {

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusRepository;

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    public ExportXLPropertiesDTO exportSalaryDisbursement(int year, int month) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findDisbursedSalaryByYearAndMonth(
            year,
            Month.fromInteger(month)
        );

        String sheetName = "Salary Disbursement";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        subTitleList.add("Salary Disbursement of " + Month.fromInteger(month) + ", " + year);
        subTitleList.add("Report Generated On " + today.toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("PIN");
        tableHeaderList.add("bank Account Number");
        tableHeaderList.add("Employee Name");
        tableHeaderList.add("Net Pay");
        tableHeaderList.add("Narration");
        tableHeaderList.add("Mobile Number");
        tableHeaderList.add("Beneficiary E-Mail");
        tableHeaderList.add("Payment Execution Date (DD-MM-YYYY)");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < employeeSalaryList.size(); i++) {
            List<Object> data = new ArrayList<>();
            data.add(employeeSalaryList.get(i).getEmployee().getPin());
            data.add(employeeSalaryList.get(i).getEmployee().getBankAccountNo());
            data.add(employeeSalaryList.get(i).getEmployee().getFullName());
            data.add(employeeSalaryList.get(i).getNetPay());
            data.add(getNarration(employeeSalaryList.get(i)));

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO exportLivingAllowance(int year, int month) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findLivingAllowanceByMonthAndYear(
            year,
            Month.fromInteger(month)
        );

        String sheetName = "Living Allowance";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        subTitleList.add("Living Allowance's of " + Month.fromInteger(month) + ", " + year);
        subTitleList.add("Report Generated On " + today.toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("PIN");
        tableHeaderList.add("bank Account Number");
        tableHeaderList.add("Employee Name");
        tableHeaderList.add("Living Allowance");
        tableHeaderList.add("Narration");
        tableHeaderList.add("Mobile Number");
        tableHeaderList.add("Beneficiary E-Mail");
        tableHeaderList.add("Payment Execution Date (DD-MM-YYYY)");

        List<List<Object>> dataList = new ArrayList<>();

        if (employeeSalaryList.size() > 0) {
            for (int i = 0; i < employeeSalaryList.size(); i++) {
                if (employeeSalaryList.get(i).getAllowance01() == 0) {
                    continue;
                }

                List<Object> data = new ArrayList<>();
                data.add(employeeSalaryList.get(i).getEmployee().getPin());
                data.add(employeeSalaryList.get(i).getEmployee().getBankAccountNo());
                data.add(employeeSalaryList.get(i).getEmployee().getFullName());
                data.add(employeeSalaryList.get(i).getAllowance01());
                data.add(getNarration(employeeSalaryList.get(i)));

                dataList.add(data);
            }
        } else {
            List<Object> data = new ArrayList<>();
            data.add("-");
            data.add("-");
            data.add("-");
            data.add("-");

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO exportCarAllowance(int year, int month) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findCarAllowanceByMonthAndYear(year, Month.fromInteger(month));

        String sheetName = "Car Allowance";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        subTitleList.add("Car Allowance's of " + Month.fromInteger(month) + ", " + year);
        subTitleList.add("Report Generated On " + today.toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("PIN");
        tableHeaderList.add("bank Account Number");
        tableHeaderList.add("Employee Name");
        tableHeaderList.add("Car Allowance");
        tableHeaderList.add("Narration");
        tableHeaderList.add("Mobile Number");
        tableHeaderList.add("Beneficiary E-Mail");
        tableHeaderList.add("Payment Execution Date (DD-MM-YYYY)");

        List<List<Object>> dataList = new ArrayList<>();

        if (employeeSalaryList.size() > 0) {
            for (int i = 0; i < employeeSalaryList.size(); i++) {
                if (employeeSalaryList.get(i).getAllowance02() == 0) {
                    continue;
                }

                List<Object> data = new ArrayList<>();
                data.add(employeeSalaryList.get(i).getEmployee().getPin());
                data.add(employeeSalaryList.get(i).getEmployee().getBankAccountNo());
                data.add(employeeSalaryList.get(i).getEmployee().getFullName());
                data.add(employeeSalaryList.get(i).getAllowance02());
                data.add(getNarration(employeeSalaryList.get(i)));

                dataList.add(data);
            }
        } else {
            List<Object> data = new ArrayList<>();
            data.add("-");
            data.add("-");
            data.add("-");
            data.add("-");

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO exportCSAllowance(int year, int month) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findCompanySecretaryAllowanceByMonthAndYear(
            year,
            Month.fromInteger(month)
        );

        String sheetName = "Company Secretary Allowance";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();

        LocalDate today = LocalDate.now();
        subTitleList.add("Company Secretary Allowance's of " + Month.fromInteger(month) + ", " + year);
        subTitleList.add("Report Generated On " + today.toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("PIN");
        tableHeaderList.add("bank Account Number");
        tableHeaderList.add("Employee Name");
        tableHeaderList.add("Company Secretary Allowance");
        tableHeaderList.add("Narration");
        tableHeaderList.add("Mobile Number");
        tableHeaderList.add("Beneficiary E-Mail");
        tableHeaderList.add("Payment Execution Date (DD-MM-YYYY)");

        List<List<Object>> dataList = new ArrayList<>();

        if (employeeSalaryList.size() > 0) {
            for (int i = 0; i < employeeSalaryList.size(); i++) {
                if (employeeSalaryList.get(i).getAllowance04() == 0) {
                    continue;
                }

                List<Object> data = new ArrayList<>();
                data.add(employeeSalaryList.get(i).getEmployee().getPin());
                data.add(employeeSalaryList.get(i).getEmployee().getBankAccountNo());
                data.add(employeeSalaryList.get(i).getEmployee().getFullName());
                data.add(employeeSalaryList.get(i).getAllowance04());
                data.add(getNarration(employeeSalaryList.get(i)));

                dataList.add(data);
            }
        } else {
            List<Object> data = new ArrayList<>();
            data.add("-");
            data.add("-");
            data.add("-");
            data.add("-");

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO exportHouseRentAllowance(int year, int month) {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findHouseRentAllowanceByMonthAndYear(
            year,
            Month.fromInteger(month)
        );

        String sheetName = "House Rent Allowance";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        subTitleList.add("House Rent Allowance's of " + Month.fromInteger(month) + ", " + year);
        subTitleList.add("Report Generated On " + today.toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("PIN");
        tableHeaderList.add("bank Account Number");
        tableHeaderList.add("Employee Name");
        tableHeaderList.add("House Rent Allowance");
        tableHeaderList.add("Narration");
        tableHeaderList.add("Mobile Number");
        tableHeaderList.add("Beneficiary E-Mail");
        tableHeaderList.add("Payment Execution Date (DD-MM-YYYY)");

        List<List<Object>> dataList = new ArrayList<>();

        if (employeeSalaryList.size() > 0) {
            for (int i = 0; i < employeeSalaryList.size(); i++) {
                if (employeeSalaryList.get(i).getAllowance03() == 0) {
                    continue;
                }

                List<Object> data = new ArrayList<>();
                data.add(employeeSalaryList.get(i).getEmployee().getPin());
                data.add(employeeSalaryList.get(i).getEmployee().getBankAccountNo());
                data.add(employeeSalaryList.get(i).getEmployee().getFullName());
                data.add(employeeSalaryList.get(i).getAllowance03());
                data.add(getNarration(employeeSalaryList.get(i)));

                dataList.add(data);
            }
        } else {
            List<Object> data = new ArrayList<>();
            data.add("-");
            data.add("-");
            data.add("-");
            data.add("-");

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO exportYearlyTaxReturnSubmission(int startYear, int startMonth, int endYear, int endMonth) {
        try {
            LocalDate startDate = LocalDate.of(startYear, startMonth, 1);
            LocalDate endDate = LocalDate.of(endYear, endMonth, 1);

            HashSet<Employee> employeeHashSet = new HashSet<>();

            LocalDate end = LocalDate.of(endYear, endMonth, 1).plusMonths(1);
            for (LocalDate date = startDate; date.isBefore(end); date = date.plusMonths(1)) {
                List<Employee> employeeList = employeeSalaryRepository.getEmployeeListForYearlyTaxSubmissionReportBetweenTime(
                    date.getYear(),
                    Month.fromInteger(date.getMonthValue())
                );

                for (Employee employee : employeeList) {
                    employeeHashSet.add(employee);
                }
            }

            List<Employee> employees = employeeHashSet
                .stream()
                .sorted(Comparator.comparing((Employee::getPin)))
                .collect(Collectors.toList());

            String sheetName = "Yearly Tax Return Submission";

            List<String> titleList = new ArrayList<>();

            List<String> subTitleList = new ArrayList<>();
            LocalDate today = LocalDate.now();
            subTitleList.add(
                "Yearly Tax Return Submission report from " +
                startDate.getMonth() +
                ", " +
                startDate.getYear() +
                " to " +
                endDate.getMonth() +
                ", " +
                endDate.getYear()
            );
            subTitleList.add("Report Generated On " + today.getDayOfMonth() + " " + today.getMonth() + ", " + today.getYear());

            List<String> tableHeaderList = new ArrayList<>();
            tableHeaderList.add("S/L");
            tableHeaderList.add("PIN");
            tableHeaderList.add("Name");
            tableHeaderList.add("Designation");
            tableHeaderList.add("E-TIN");
            tableHeaderList.add("Basic");
            tableHeaderList.add("House Rent");
            tableHeaderList.add("Medical");
            tableHeaderList.add("Conveyance");
            tableHeaderList.add("Bonus");
            tableHeaderList.add("PF");
            tableHeaderList.add("Tax Deduction");

            List<List<Object>> dataList = new ArrayList<>();

            if (employees.size() == 0) {
                List<Object> data = new ArrayList<>();
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");
                data.add("-");

                dataList.add(data);
            }

            int count = 1;
            for (Employee employee : employees) {
                List<Object> data = new ArrayList<>();

                List<EmployeeSalary> employeeSalaryList = new ArrayList<>();
                for (LocalDate date = startDate; date.isBefore(end); date = date.plusMonths(1)) {
                    List<EmployeeSalary> employeeSalaries = employeeSalaryRepository.findByEmployeeIdAndYearAndMonth(
                        employee.getId(),
                        date.getYear(),
                        Month.fromInteger(date.getMonthValue())
                    );
                    employeeSalaryList.addAll(employeeSalaries);
                }

                data.add(count++);
                data.add(employee.getPin());
                data.add(employee.getFullName());
                data.add(employee.getDesignation().getDesignationName());
                data.add(employee.getTinNumber() != null ? employee.getTinNumber() : "-");
                data.add(getTotalBasic(employeeSalaryList));
                data.add(getTotalHouseRent(employeeSalaryList));
                data.add(getTotalMedicalAllowance(employeeSalaryList));
                data.add(getTotalConveyance(employeeSalaryList));
                data.add(getTotalBonus(employee, startDate, endDate.with(TemporalAdjusters.lastDayOfMonth())));
                data.add(getTotalPF(employee, startDate, endDate));
                data.add(getTotalTaxDeduction(employeeSalaryList));

                dataList.add(data);
            }

            ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

            exportXLPropertiesDTO.setSheetName(sheetName);
            exportXLPropertiesDTO.setTitleList(titleList);
            exportXLPropertiesDTO.setSubTitleList(subTitleList);
            exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
            exportXLPropertiesDTO.setTableDataListOfList(dataList);
            exportXLPropertiesDTO.setHasAutoSummation(false);
            exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);

            return exportXLPropertiesDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private double getTotalTaxDeduction(List<EmployeeSalary> employeeSalaryList) {
        double totalTaxDeduction = 0;

        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            totalTaxDeduction = totalTaxDeduction + employeeSalary.getTaxDeduction();
        }
        return (double) Math.round(totalTaxDeduction);
    }

    private double getTotalPF(Employee employee, LocalDate startDate, LocalDate endDate) {
        double totalPF = 0;

        for (LocalDate date = startDate; date.isBefore(endDate.plusMonths(1)); date = date.plusMonths(1)) {
            Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getMonthlyPfCollection(
                employee.getPin(),
                date.getYear(),
                date.getMonthValue()
            );

            if (pfCollectionOptional.isPresent()) {
                double employeeContribution = pfCollectionOptional.get().getEmployeeContribution() != null
                    ? pfCollectionOptional.get().getEmployeeContribution()
                    : 0;
                double employeeInterest = pfCollectionOptional.get().getEmployeeInterest() != null
                    ? pfCollectionOptional.get().getEmployeeInterest()
                    : 0;
                double employerContribution = pfCollectionOptional.get().getEmployerContribution() != null
                    ? pfCollectionOptional.get().getEmployerContribution()
                    : 0;
                double employerInterest = pfCollectionOptional.get().getEmployerInterest() != null
                    ? pfCollectionOptional.get().getEmployerInterest()
                    : 0;

                double total = employeeContribution + employeeInterest + employerContribution + employerInterest;

                totalPF = totalPF + total;
            }
        }
        return (double) Math.round(totalPF);
    }

    //    private Object getTotalTaxDeduction(Employee employee) {
    //    }

    private double getTotalBonus(Employee employee, LocalDate startDate, LocalDate endDate) {
        double totalBonus = 0;
        List<FestivalBonusDetails> detailsList = festivalBonusRepository.findAllByEmployeeIdBetweenDates(
            employee.getId(),
            startDate,
            endDate
        );
        for (FestivalBonusDetails bonusDetails : detailsList) {
            totalBonus = totalBonus + bonusDetails.getBonusAmount();
        }
        return (double) Math.round(totalBonus);
    }

    private double getTotalConveyance(List<EmployeeSalary> employeeSalaryList) {
        double totalConveyance = 0;
        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            totalConveyance = totalConveyance + employeeSalary.getMainGrossConveyanceAllowance();
        }
        return (double) Math.round(totalConveyance);
    }

    private double getTotalMedicalAllowance(List<EmployeeSalary> employeeSalaryList) {
        double totalMedicalAllowance = 0;
        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            totalMedicalAllowance = totalMedicalAllowance + employeeSalary.getMainGrossMedicalAllowance();
        }
        return (double) Math.round(totalMedicalAllowance);
    }

    private double getTotalHouseRent(List<EmployeeSalary> employeeSalaryList) {
        double totalHouseRent = 0;
        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            totalHouseRent = totalHouseRent + employeeSalary.getMainGrossHouseRent();
        }
        return (double) Math.round(totalHouseRent);
    }

    private double getTotalBasic(List<EmployeeSalary> employeeSalaryList) {
        double totalBasic = 0;
        for (EmployeeSalary employeeSalary : employeeSalaryList) {
            totalBasic = totalBasic + employeeSalary.getMainGrossBasicSalary();
        }
        return (double) Math.round(totalBasic);
    }

    private String getNarration(EmployeeSalary employeeSalary) {
        String month =
            employeeSalary.getMonth().toString().substring(0, 1).toUpperCase() +
            employeeSalary.getMonth().toString().substring(1).toLowerCase();

        String year = employeeSalary.getYear().toString();

        String narration = "Salary, " + month + " " + year;

        return narration;
    }
}
