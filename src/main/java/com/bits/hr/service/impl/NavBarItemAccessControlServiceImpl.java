package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.NavBarItemAccessControlService;
import com.bits.hr.service.dto.NavBarItemAccessControlDTO;
import org.springframework.stereotype.Service;

@Service
public class NavBarItemAccessControlServiceImpl implements NavBarItemAccessControlService {

    private final EmployeeRepository employeeRepository;

    public NavBarItemAccessControlServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public NavBarItemAccessControlDTO getAllAccessControl(long employeeId) {
        NavBarItemAccessControlDTO navBarItemAccessControlDTO = new NavBarItemAccessControlDTO();
        Employee employee = employeeRepository.findById(employeeId).get();
        navBarItemAccessControlDTO.setCanManageTaxAcknowledgementReceipt(canManageTacAcknowledgementReceipt(employee));
        return navBarItemAccessControlDTO;
    }

    @Override
    public boolean canManageTacAcknowledgementReceipt(Employee employee) {
        return employee.getCanManageTaxAcknowledgementReceipt() != null && employee.getCanManageTaxAcknowledgementReceipt();
    }
}
