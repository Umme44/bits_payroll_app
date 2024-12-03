package com.bits.hr.service.specializedSearch.helperMethods;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.specializedSearch.dto.EmployeeSpecializedSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapEmployeeToResult {

    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeSpecializedSearch map(Employee employee) {
        EmployeeSpecializedSearch employeeSpecializedSearch = new EmployeeSpecializedSearch();
        employeeSpecializedSearch.setId(employee.getId());
        employeeSpecializedSearch.setFullName(employee.getFullName());
        employeeSpecializedSearch.setPin(employee.getPin());
        employeeSpecializedSearch.setOfficialEmail(employee.getOfficialEmail());
        employeeSpecializedSearch.setOfficialContactNo(employee.getOfficialContactNo());
        employeeSpecializedSearch.setWhatsappId(employee.getWhatsappId());
        employeeSpecializedSearch.setSkypeId(employee.getSkypeId());
        if (employee.getDepartment() != null) {
            employeeSpecializedSearch.setDepartmentName(employee.getDepartment().getDepartmentName());
        }
        if (employee.getUnit() != null) {
            employeeSpecializedSearch.setUnitName(employee.getUnit().getUnitName());
        }
        if (employee.getDesignation() != null) {
            employeeSpecializedSearch.setDesignationName(employee.getDesignation().getDesignationName());
        }
        if (employee.getReportingTo() != null) {
            employeeSpecializedSearch.setReportingToId(employee.getReportingTo().getId());
        }
        if (employee.getReportingTo() != null) {
            String reportingToName = employeeRepository.getNameByEmployeeId(employee.getReportingTo().getId());
            employeeSpecializedSearch.setReportingToName(reportingToName);
        }

        return employeeSpecializedSearch;
    }
}
