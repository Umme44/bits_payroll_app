package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.attendanceSync.OnlinePunchService;
import com.bits.hr.service.dto.EmployeeApprovalDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.WorkFromHomeApprovalEvent;
import com.bits.hr.service.mapper.EmployeeApprovalMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class WorkFromHomeEnableHRService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeApprovalMapper employeeApprovalMapper;

    @Autowired
    OnlinePunchService onlinePunchService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public List<EmployeeApprovalDTO> getListOfEmployee() {
        onlinePunchService.dataTuneForEligibility();
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endDate = LocalDate.of(today.getYear(), today.getMonth(), startDate.lengthOfMonth());
        return employeeApprovalMapper.toDto(employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(startDate, endDate));
    }

    public boolean enableSelected(List<Long> selectedIds) {
        try {
            for (long id : selectedIds) {
                Optional<Employee> employeeOptional = employeeRepository.findById(id);
                if (employeeOptional.isPresent()) {
                    Employee employee = employeeOptional.get();
                    employee.setIsAllowedToGiveOnlineAttendance(true);
                    employee.setUpdatedAt(LocalDateTime.now());
                    employeeRepository.save(employee);

                    publishEvent(employee, EventType.APPROVED);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + "    " + ex.getStackTrace());
            return false;
        }
    }

    public boolean approveFiltered(List<Employee> filteredEmployeeList) {
        try {
            for (Employee employee : filteredEmployeeList) {
                employee.setIsAllowedToGiveOnlineAttendance(true);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + "    " + ex.getStackTrace());
            return false;
        }
    }

    public boolean disabledSelected(List<Long> selectedIds) {
        try {
            for (long id : selectedIds) {
                Optional<Employee> employeeOptional = employeeRepository.findById(id);
                if (employeeOptional.isPresent()) {
                    Employee employee = employeeOptional.get();
                    employee.setIsAllowedToGiveOnlineAttendance(false);
                    employee.setUpdatedAt(LocalDateTime.now());
                    employeeRepository.save(employee);

                    publishEvent(employee, EventType.REJECTED);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + "    " + ex.getStackTrace());
            return false;
        }
    }

    public boolean denyFiltered(List<Employee> filteredEmployeeList) {
        try {
            for (Employee employee : filteredEmployeeList) {
                employee.setIsAllowedToGiveOnlineAttendance(false);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + "    " + ex.getStackTrace());
            return false;
        }
    }

    private void publishEvent(Employee employee, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        WorkFromHomeApprovalEvent workFromHomeApprovalEvent = new WorkFromHomeApprovalEvent(this, employee, event);
        applicationEventPublisher.publishEvent(workFromHomeApprovalEvent);
    }
}
