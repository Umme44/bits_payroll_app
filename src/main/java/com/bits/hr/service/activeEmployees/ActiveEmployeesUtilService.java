package com.bits.hr.service.activeEmployees;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.EmployeeMinCustomDto;
import org.springframework.stereotype.Service;

@Service
public class ActiveEmployeesUtilService {

    public EmployeeMinCustomDto getEmployeeMinCustomDto(Employee employee) {
        EmployeeMinCustomDto employeeInfo = new EmployeeMinCustomDto();
        EmployeeMinCustomDto supervisorsInfo = new EmployeeMinCustomDto();

        employeeInfo.setId(employee.getId());
        employeeInfo.setPin(employee.getPin());
        employeeInfo.setName(employee.getFullName());
        employeeInfo.setDepartmentName(employee.getDepartment().getDepartmentName());
        employeeInfo.setDesignationName(employee.getDesignation().getDesignationName());
        employeeInfo.setUnitName(employee.getUnit().getUnitName());
        employeeInfo.setOfficialEmail(employee.getOfficialEmail() != null ? employee.getOfficialEmail() : null);
        employeeInfo.setOfficialPhone(employee.getOfficialContactNo() != null ? employee.getOfficialContactNo() : null);

        if (employee.getReportingTo() == null) {
            employeeInfo.setReportingTo(null);
        } else {
            supervisorsInfo.setId(employee.getReportingTo().getId());
            supervisorsInfo.setPin(employee.getReportingTo().getPin());
            supervisorsInfo.setName(employee.getReportingTo().getFullName());
            supervisorsInfo.setDepartmentName(employee.getReportingTo().getDepartment().getDepartmentName());
            supervisorsInfo.setDesignationName(employee.getReportingTo().getDesignation().getDesignationName());
            supervisorsInfo.setUnitName(employee.getReportingTo().getUnit().getUnitName());
            supervisorsInfo.setOfficialEmail(employee.getReportingTo().getOfficialEmail());
            supervisorsInfo.setOfficialPhone(employee.getReportingTo().getOfficialContactNo());
            supervisorsInfo.setReportingTo(null);

            employeeInfo.setReportingTo(supervisorsInfo);
        }
        return employeeInfo;
    }
}
