package com.bits.hr.service.workFromHomeApplication;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.WorkFromHomeApplicationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class WorkFromHomeRefreshService {

    private final WorkFromHomeApplicationRepository workFromHomeApplicationRepository;

    private final EmployeeRepository employeeRepository;

    public WorkFromHomeRefreshService(
        WorkFromHomeApplicationRepository workFromHomeApplicationRepository,
        EmployeeRepository employeeRepository
    ) {
        this.workFromHomeApplicationRepository = workFromHomeApplicationRepository;
        this.employeeRepository = employeeRepository;
    }

    @Async
    public void refreshWFH(LocalDate currentDate, Employee employee) {
        // find approved wfh application between WFH start date and WFH end date
        log.debug("Refresh Work From Home Status for Date = {} and Employee = {}", currentDate, employee);
        List<WorkFromHomeApplication> approvedWFH = workFromHomeApplicationRepository.findApprovedWFH(currentDate, employee.getId());
        if (approvedWFH.size() > 0) {
            log.debug("Web Punch has Turned On");
            employee.setIsAllowedToGiveOnlineAttendance(true);
        } else {
            log.debug("Web Punch has Turned Off");
            employee.setIsAllowedToGiveOnlineAttendance(false);
        }
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }
}
