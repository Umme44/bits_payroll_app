package com.bits.hr.service.activeEmployees;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.dto.EmployeeMinCustomDto;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ActiveEmployeesCommonService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ActiveEmployeesCustomMapper activeEmployeesCustomMapper;

    public List<EmployeeMinCustomDto> getAllActiveEmployees() {
        try {
            List<Employee> employeeList = employeeRepository.getAllActiveEmployeesTillDate(LocalDate.now());
            return activeEmployeesCustomMapper.mapEmployeeToActiveEmployeeCustomDTO(employeeList);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
