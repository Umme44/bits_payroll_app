package com.bits.hr.service.approvalProcess;

import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSalaryHoldService {

    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    EmployeeSalaryMapper employeeSalaryMapper;

    public List<EmployeeSalaryDTO> findAll(String searchText) {
        if (searchText == null) {
            searchText = "";
        }
        return employeeSalaryMapper.toDto(employeeSalaryRepository.getHoldSalaries("%" + searchText.toLowerCase() + "%"));
    }
}
