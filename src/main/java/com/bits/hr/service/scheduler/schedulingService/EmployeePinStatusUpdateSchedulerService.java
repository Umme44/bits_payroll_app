package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.EmployeePin;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import com.bits.hr.repository.EmployeePinRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EmployeePinStatusUpdateSchedulerService {

    @Autowired
    private EmployeePinRepository employeePinRepository;

    public void updateEmployeePinStatusIfEmployeeResigns() {
        try {
            List<EmployeePin> employeePinList = employeePinRepository.getEmployeePinsOfNewlyResignedEmployees(LocalDate.now());

            for (int i = 0; i < employeePinList.size(); i++) {
                if (employeePinList.get(i).getEmployeeCategory().equals(EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE)) {
                    employeePinList.get(i).setEmployeePinStatus(EmployeePinStatus.RESIGNED);
                    employeePinRepository.save(employeePinList.get(i));
                } else {
                    employeePinList.get(i).setEmployeePinStatus(EmployeePinStatus.CONTRACT_END);
                    employeePinRepository.save(employeePinList.get(i));
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
