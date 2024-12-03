package com.bits.hr.service.utility;

import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.PfAccountRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PinTrimUtilService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    private PfAccountRepository pfAccountRepository;

    public boolean batchEmployeeSalaryPINTrim() {
        try {
            employeeSalaryRepository
                .findAll()
                .forEach(salary -> {
                    if (salary.getPin() != null) {
                        salary.setPin(salary.getPin().trim());
                    }
                    if (salary.getRefPin() != null) {
                        salary.setRefPin(salary.getRefPin().trim());
                    }
                });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean batchEmployeePINTrim() {
        try {
            employeeRepository
                .findAll()
                .forEach(employee -> {
                    employee.setPin(employee.getPin().trim());
                    if (employee.getReferenceId() != null) {
                        employee.setReferenceId(employee.getReferenceId().trim());
                    }
                    employee.setUpdatedAt(LocalDateTime.now());
                    employeeRepository.save(employee);
                });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean batchPfAccountPINTrim() {
        try {
            pfAccountRepository
                .findAll()
                .forEach(pfAccount -> {
                    pfAccount.setPin(pfAccount.getPin().trim());
                    pfAccount.setPfCode(pfAccount.getPfCode().trim());
                    pfAccountRepository.save(pfAccount);
                });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
