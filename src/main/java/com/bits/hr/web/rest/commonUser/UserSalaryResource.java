package com.bits.hr.web.rest.commonUser;

import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common/user-salary")
public class UserSalaryResource {

    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    EmployeeSalaryMapper employeeSalaryMapper;

    @GetMapping("")
    public List<EmployeeSalaryDTO> getAllEmployeeSalaries() {
        List<EmployeeSalaryDTO> result = employeeSalaryMapper.toDto(
            employeeSalaryRepository.getAllByEmployeePin(SecurityUtils.getCurrentEmployeePin())
        );
        Collections.reverse(result);
        return result;
    }
}
