package com.bits.hr.service.xlExportHandling.LeaveManagement;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO.LeaveBalanceDetailViewDTO;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.LeaveBalanceDetailViewService;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LeaveBalanceExportService {

    @Autowired
    private LeaveBalanceDetailViewService leaveBalanceDetailViewService;

    public ExportXLPropertiesDTO getDataForLeaveBalanceExport(int year, List<Employee> employeeList) {
        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        List<String> tableHeaderList = new ArrayList<>();
        List<List<Object>> dataList = new ArrayList<>();

        String sheetName = "Leave balance-" + year;
        LocalDate today = LocalDate.now();
        subTitleList.add("Leave Balance of : " + year);
        subTitleList.add("Report Generated from system at " + today.toString());

        tableHeaderList.add("S/L");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Employee");
        tableHeaderList.add("Designation");
        tableHeaderList.add("Department");
        tableHeaderList.add("Unit");
        tableHeaderList.add("Employment Type");

        tableHeaderList.add("Leave Type");
        tableHeaderList.add("Carry Forwarded"); // opening balance
        tableHeaderList.add("This Year"); // amount
        tableHeaderList.add("Total");
        tableHeaderList.add("Requested");
        tableHeaderList.add("Approved");
        tableHeaderList.add("Balance");
        tableHeaderList.add("Currently Applicable balance");

        int serial = 1;

        for (Employee emp : employeeList) {
            List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService.getYearlyProcessedLeaveBalance(
                year,
                emp.getId()
            );

            for (LeaveBalanceDetailViewDTO leaveBalanceDetail : leaveBalanceDetailViewDTOList) {
                List<Object> data = new ArrayList<>();

                data.add(serial);
                data.add(toStr(emp.getPin()));
                data.add(toStr(emp.getFullName()));

                String designation = emp.getDesignation() == null ? "" : emp.getDesignation().getDesignationName();
                String department = emp.getDepartment() == null ? "" : emp.getDepartment().getDepartmentName();
                String unit = emp.getUnit() == null ? "" : emp.getUnit().getUnitName();
                String employeeCategory = getEmployeeCategory(emp);

                data.add(designation);
                data.add(department);
                data.add(unit);
                data.add(employeeCategory);

                String leaveType = getLeaveType(leaveBalanceDetail);
                Integer carryForwarded = leaveBalanceDetail.getOpeningBalance();
                Integer thisYear = toInt(leaveBalanceDetail.getAmount());

                int total = carryForwarded + thisYear;
                Integer requested = toInt(leaveBalanceDetail.getDaysRequested());
                Integer approved = toInt(leaveBalanceDetail.getDaysApproved());
                Integer balance = toInt(leaveBalanceDetail.getDaysRemaining());
                int currentlyApplicable = requested - approved;

                data.add(leaveType);
                data.add(carryForwarded);
                data.add(thisYear);
                data.add(total);
                data.add(requested);
                data.add(approved);
                data.add(balance);
                data.add(currentlyApplicable);
                dataList.add(data);
                ++serial;
            }
        }
        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(20);

        return exportXLPropertiesDTO;
    }

    private String toStr(String string) {
        return string == null ? "" : string;
    }

    private Double toDouble(Double num) {
        return num == null ? 0d : num;
    }

    private Integer toInt(Integer num) {
        return num == null ? 0 : num;
    }

    private String getEmployeeCategory(Employee employee) {
        if (employee.getEmployeeCategory() == null) {
            return "";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            return "Regular Confirmed";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE)) {
            return "Regular Probational";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)) {
            return "by Contract";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.INTERN)) {
            return "Intern";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.CONSULTANTS)) {
            return "Consultants";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.PART_TIME)) {
            return "Part Time";
        } else {
            return "";
        }
    }

    private String getLeaveType(LeaveBalanceDetailViewDTO leaveBalance) {
        return convertToTitleCase(leaveBalance.getLeaveType().toString().replace("_", " "));
    }

    private static String convertToTitleCase(String inputString) {
        if (inputString == null) {
            return "";
        }

        if (inputString.length() == 1) {
            return inputString.toUpperCase();
        }

        StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());

        Stream
            .of(inputString.split(" "))
            .forEach(stringPart -> {
                if (stringPart.length() > 1) resultPlaceHolder
                    .append(stringPart.substring(0, 1).toUpperCase())
                    .append(stringPart.substring(1).toLowerCase()); else resultPlaceHolder.append(stringPart.toUpperCase());

                resultPlaceHolder.append(" ");
            });
        return resultPlaceHolder.toString().trim();
    }
}
