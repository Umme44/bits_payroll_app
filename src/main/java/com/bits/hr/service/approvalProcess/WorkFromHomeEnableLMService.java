package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.attendanceSync.OnlinePunchService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeApprovalDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.WorkFromHomeApprovalEvent;
import com.bits.hr.service.mapper.EmployeeApprovalMapper;
import com.bits.hr.service.mapper.EmployeeCommonMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class WorkFromHomeEnableLMService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeCommonMapper employeeCommonMapper;

    @Autowired
    private OnlinePunchService onlinePunchService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeApprovalMapper employeeApprovalMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public List<EmployeeApprovalDTO> getSubOrdinateEmployees() {
        onlinePunchService.dataTuneForEligibility();
        List<Employee> mySubordinateEmployee = employeeService.getDirectReportingTo(currentEmployeeService.getCurrentEmployee().get());
        return employeeApprovalMapper.toDto(mySubordinateEmployee);
    }

    public boolean enableSelected(List<Long> selectedIds) {
        try {
            Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
            for (long id : selectedIds) {
                Optional<Employee> employeeOptional = employeeRepository.findById(id);
                if (employeeOptional.isPresent()) {
                    Employee employee = employeeOptional.get();
                    if (employee.getReportingTo() != null && currentEmployeeId.isPresent() && verifySubOrdinate(employee.getId())) {
                        employee.setIsAllowedToGiveOnlineAttendance(true);
                        employee.setUpdatedAt(LocalDateTime.now());
                        employeeRepository.save(employee);
                        publishEvent(employee, EventType.APPROVED);
                    } else {
                        log.error(" Online attendance can't be changed except line manager ");
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    public boolean disableSelected(List<Long> selectedIds) {
        try {
            Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
            for (long id : selectedIds) {
                Optional<Employee> employeeOptional = employeeRepository.findById(id);
                if (employeeOptional.isPresent()) {
                    Employee employee = employeeOptional.get();
                    if (employee.getReportingTo() != null && currentEmployeeId.isPresent() && verifySubOrdinate(employee.getId())) {
                        employee.setIsAllowedToGiveOnlineAttendance(false);
                        employee.setUpdatedAt(LocalDateTime.now());
                        employeeRepository.save(employee);
                        publishEvent(employee, EventType.REJECTED);
                    } else {
                        log.error(" Online attendance can't be changed except line manager ");
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    public boolean verifySubOrdinate(Long employeeId) {
        log.debug("verify selected employee is sub-ordinate of current employee");
        List<EmployeeApprovalDTO> employees = getSubOrdinateEmployees()
            .stream()
            .filter(x -> x.getId().equals(employeeId))
            .collect(Collectors.toList());
        if (employees.size() == 1) return true; else return false;
    }

    private void publishEvent(Employee employee, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        WorkFromHomeApprovalEvent workFromHomeApprovalEvent = new WorkFromHomeApprovalEvent(this, employee, event);
        applicationEventPublisher.publishEvent(workFromHomeApprovalEvent);
    }
}
