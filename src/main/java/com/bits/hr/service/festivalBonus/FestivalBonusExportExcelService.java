package com.bits.hr.service.festivalBonus;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FestivalBonusExportExcelService {

    public ExportXLPropertiesDTO exportFbReport(
        Festival festival,
        List<FestivalBonusDetails> festivalBonusDetailsList,
        EmployeeCategory employeeCategory
    ) {
        String sheetName;

        if (employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            sheetName = "Regular Confirmed Employee";
        } else {
            sheetName = "Contractual Employee";
        }

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();

        if (employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            subTitleList.add("Regular Confirmed Employee");
        } else {
            subTitleList.add("Contractual Employee");
        }

        subTitleList.add("Festival Name " + festival.getFestivalName());
        subTitleList.add("Bonus Disbursement Date " + festival.getBonusDisbursementDate().toString());

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("S/L");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Employee Name");
        tableHeaderList.add("Joining Date");

        if (employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            tableHeaderList.add("Confirmation Date");
        } else {
            tableHeaderList.add("Contract End Date");
        }

        tableHeaderList.add("Employment Status");
        tableHeaderList.add("HC");
        tableHeaderList.add("Band");
        tableHeaderList.add("Unit");
        tableHeaderList.add("Department");
        tableHeaderList.add("Gross");

        if (employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            tableHeaderList.add("Basic");
        }

        tableHeaderList.add("Festival Bonus");
        tableHeaderList.add("Remarks");

        List<List<Object>> dataList = new ArrayList<>();
        long totalHC = 0;
        double totalGross = 0;
        double totalBasic = 0;
        double totalBonusAmount = 0;

        for (int i = 0; i < festivalBonusDetailsList.size(); i++) {
            List<Object> dataRow = new ArrayList<>();

            dataRow.add(i + 1);
            Employee employee = festivalBonusDetailsList.get(i).getEmployee();
            FestivalBonusDetails festivalBonusDetails = festivalBonusDetailsList.get(i);
            dataRow.add(employee.getPin());
            dataRow.add(employee.getFullName());
            dataRow.add(employee.getDateOfJoining());

            if (employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
                if (employee.getDateOfConfirmation() != null) {
                    dataRow.add(employee.getEmployeeCategory());
                }
            } else {
                if (employee.getContractPeriodExtendedTo() != null) {
                    dataRow.add(employee.getContractPeriodExtendedTo());
                } else if (employee.getContractPeriodEndDate() != null) {
                    dataRow.add(employee.getContractPeriodEndDate());
                }
            }

            dataRow.add(getEmployeeCategory(employee));

            dataRow.add("1");
            totalHC++;

            dataRow.add(employee.getBand().getBandName());
            dataRow.add(employee.getUnit().getUnitName());
            dataRow.add(employee.getDepartment().getDepartmentName());

            if (festivalBonusDetails.getGross() == null) {
                throw new BadRequestAlertException(
                    String.format("Gross Amount is missing for %s-%s", employee.getPin(), employee.getFullName()),
                    "FBProRataDataPrepareService",
                    "fbGrossIsMissing"
                );
            }
            dataRow.add(festivalBonusDetails.getGross());
            totalGross += festivalBonusDetails.getGross();

            if (employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
                if (festivalBonusDetails.getBasic() == null) {
                    throw new BadRequestAlertException(
                        String.format("Basic Amount is missing for %s-%s", employee.getPin(), employee.getFullName()),
                        "FBProRataDataPrepareService",
                        "fbBasicIsMissing"
                    );
                }
                dataRow.add(festivalBonusDetails.getBasic());
                totalBasic += festivalBonusDetails.getBasic();
            }

            dataRow.add(festivalBonusDetails.getBonusAmount());
            totalBonusAmount += festivalBonusDetails.getBonusAmount();

            dataRow.add(festivalBonusDetails.getRemarks());

            dataList.add(dataRow);
        }

        List<Object> summationRow = new ArrayList<>();

        summationRow.add("");
        summationRow.add("");
        summationRow.add("");
        summationRow.add("");
        summationRow.add("");
        summationRow.add("");
        summationRow.add(String.valueOf(totalHC));
        summationRow.add("");
        summationRow.add("");
        summationRow.add("");
        summationRow.add(Math.floor(totalGross));

        if (employeeCategory.equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            summationRow.add(Math.floor(totalBasic));
        }

        summationRow.add(Math.floor(totalBonusAmount));
        summationRow.add("");

        dataList.add(summationRow);

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(30);
        return exportXLPropertiesDTO;
    }

    private String getEmployeeCategory(Employee employee) {
        if (employee.getEmployeeCategory() == null) {
            return "";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
            return "Regular";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE)) {
            return "Regular";
        } else if (employee.getEmployeeCategory().equals(EmployeeCategory.CONTRACTUAL_EMPLOYEE)) {
            return "Contractual";
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
}
