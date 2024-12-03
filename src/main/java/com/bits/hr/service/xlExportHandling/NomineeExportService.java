package com.bits.hr.service.xlExportHandling;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.NomineeStatus;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.NomineeService;
import com.bits.hr.service.dto.EmployeeNomineeInfo;
import com.bits.hr.service.mapper.EmployeeNomineeInfoMapper;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NomineeExportService {

    @Autowired
    NomineeService nomineeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeNomineeInfoMapper employeeNomineeInfoMapper;

    public ExportXLPropertiesDTO exportNominees() {
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeNomineeInfo> employeeNomineeInfoList = nomineeService.getEmployeeNomineeInfoList(employeeList);

        String sheetName = "Nominee Dashboard";

        List<String> titleList = new ArrayList<>();

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL.");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Full Name");
        tableHeaderList.add("Designation");
        tableHeaderList.add("Department");
        tableHeaderList.add("Unit");
        tableHeaderList.add("Date of Joining");
        tableHeaderList.add("Date Of Confirmation / Contact End Date ");
        tableHeaderList.add("Employee Category");
        tableHeaderList.add("GEN");
        tableHeaderList.add("GF");
        tableHeaderList.add("PF");
        tableHeaderList.add("Employment Status");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < employeeNomineeInfoList.size(); i++) {
            List<Object> data = new ArrayList<>();
            data.add(i + 1);
            data.add(employeeNomineeInfoList.get(i).getPin());
            data.add(employeeNomineeInfoList.get(i).getFullName());
            data.add(employeeNomineeInfoList.get(i).getDesignationName());
            data.add(employeeNomineeInfoList.get(i).getDepartmentName());
            data.add(employeeNomineeInfoList.get(i).getUnitName());
            data.add(employeeNomineeInfoList.get(i).getDateOfJoining());
            if (employeeList.get(i).getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
                if (employeeList.get(i).getContractPeriodExtendedTo() != null) {
                    data.add(employeeNomineeInfoList.get(i).getContractPeriodExtendedTo());
                } else {
                    data.add(employeeNomineeInfoList.get(i).getContractPeriodEndDate());
                }
            } else {
                data.add(employeeNomineeInfoList.get(i).getDateOfConfirmation());
            }

            data.add(getFriendlyUiForEmployeeCategory(employeeNomineeInfoList.get(i).getEmployeeCategory()));

            data.add(getFriendlyUiForNomineeApprovalStatus(employeeNomineeInfoList.get(i).getIsAllGeneralNomineeApproved()));
            data.add(getFriendlyUiForNomineeApprovalStatus(employeeNomineeInfoList.get(i).getIsAllGFNomineeApproved()));
            data.add(getFriendlyUiForNomineeApprovalStatus(employeeNomineeInfoList.get(i).getIsAllPfNomineeApproved()));
            data.add(getFriendlyUiForEmploymentStatus(employeeNomineeInfoList.get(i).getEmploymentStatus()));

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(13);

        return exportXLPropertiesDTO;
    }

    public String getFriendlyUiForEmployeeCategory(EmployeeCategory employeeCategory) {
        if (employeeCategory == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
            return "Contractual";
        } else if (employeeCategory == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            return "Regular Confirmed";
        } else if (employeeCategory == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
            return "Probational";
        } else if (employeeCategory == EmployeeCategory.CONSULTANTS) {
            return "Consultants";
        } else if (employeeCategory == EmployeeCategory.INTERN) {
            return "Intern";
        } else {
            return "Part-time";
        }
    }

    public String getFriendlyUiForNomineeApprovalStatus(NomineeStatus nomineeStatus) {
        if (nomineeStatus == NomineeStatus.ALL_APPROVED) {
            return "Hard copy received";
        } else if (nomineeStatus == NomineeStatus.PENDING) {
            return "Hard copy not received";
        } else if (nomineeStatus == NomineeStatus.NOT_ELIGIBLE) {
            return "Not Eligible";
        } else if (nomineeStatus == NomineeStatus.NOT_APPROVED) {
            return "Partially Approved";
        } else if (nomineeStatus == NomineeStatus.INCOMPLETE) {
            return "Data is not provided";
        } else {
            return "";
        }
    }

    public String getFriendlyUiForEmploymentStatus(EmploymentStatus employmentStatus) {
        if (employmentStatus == EmploymentStatus.ACTIVE) {
            return "Active";
        } else if (employmentStatus == EmploymentStatus.HOLD) {
            return "Hold";
        } else if (employmentStatus == EmploymentStatus.RESIGNED) {
            return "Resigned";
        } else {
            return "";
        }
    }
}
