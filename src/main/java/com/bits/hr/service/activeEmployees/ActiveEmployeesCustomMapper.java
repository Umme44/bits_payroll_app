package com.bits.hr.service.activeEmployees;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.EmployeeMinCustomDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveEmployeesCustomMapper {

    @Autowired
    private ActiveEmployeesUtilService activeEmployeesUtilService;

    public List<EmployeeMinCustomDto> mapEmployeeToActiveEmployeeCustomDTO(List<Employee> employeeList) {
        List<EmployeeMinCustomDto> employeeMinCustomDtoList = new ArrayList<>();
        for (Employee employee : employeeList) {
            EmployeeMinCustomDto employeeMinCustomDto = activeEmployeesUtilService.getEmployeeMinCustomDto(employee);
            employeeMinCustomDtoList.add(employeeMinCustomDto);
        }
        return employeeMinCustomDtoList;
    }
}
