package com.bits.hr.service.approvalProcess.helperMethods;

import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SaveEmployeeResignation {

    @Autowired
    EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    public boolean save(EmployeeResignation employeeResignation) {
        try {
            employeeResignation.setUodatedBy(currentEmployeeService.getCurrentEmployee().get());
            employeeResignation.setUpdatedAt(LocalDate.now());
            employeeResignationRepository.save(employeeResignation);
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }
}
