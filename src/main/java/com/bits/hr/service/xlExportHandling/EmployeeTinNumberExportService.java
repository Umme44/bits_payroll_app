package com.bits.hr.service.xlExportHandling;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.xlExportHandling.genericCsvExport.ExportCSV;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeTinNumberExportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ExportCSV exportCSV;

    public Optional<File> csvExportEmployeeTinNumber() throws IOException {
        List<Employee> employeeList = employeeRepository.findAll();

        String sheetName = "Employee_TIN_Number";

        String[] headerRow = { "PIN", "Name", "Employment Status", "TIN Number", "Tax Circle", "Tax Zone" };

        String[][] dataList = new String[employeeList.size()][6];

        for (int i = 0; i < employeeList.size(); i++) {
            dataList[i][0] = getPin(employeeList.get(i));
            dataList[i][1] = getName(employeeList.get(i));
            dataList[i][2] = getFriendlyUiForEmploymentStatus(employeeList.get(i));
            dataList[i][3] = getTinNumber(employeeList.get(i));
            dataList[i][4] = getTaxesCircle(employeeList.get(i));
            dataList[i][5] = getTaxesZone(employeeList.get(i));
        }

        Optional<File> csvFile = exportCSV.createCSVFile(sheetName, headerRow, dataList, CSVFormat.DEFAULT);

        return csvFile;
    }

    private String getPin(Employee employee) {
        try {
            return employee.getPin();
        } catch (Exception e) {
            return "-";
        }
    }

    private String getTaxesCircle(Employee employee) {
        try {
            return employee.getTaxesCircle();
        } catch (Exception e) {
            return "-";
        }
    }

    private String getTaxesZone(Employee employee) {
        try {
            return employee.getTaxesZone();
        } catch (Exception e) {
            return "-";
        }
    }

    private String getName(Employee employee) {
        try {
            return employee.getFullName();
        } catch (Exception e) {
            return "-";
        }
    }

    private String getTinNumber(Employee employee) {
        try {
            return employee.getTinNumber();
        } catch (Exception e) {
            return "-";
        }
    }

    public String getFriendlyUiForEmploymentStatus(Employee employee) {
        if (employee.getEmploymentStatus() == EmploymentStatus.ACTIVE) {
            return "Active";
        } else if (employee.getEmploymentStatus() == EmploymentStatus.HOLD) {
            return "Hold";
        } else if (employee.getEmploymentStatus() == EmploymentStatus.RESIGNED) {
            return "Resigned";
        } else {
            return "-";
        }
    }
}
