package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.FlexScheduleService;
import com.bits.hr.service.TimeSlotService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FlexScheduleApprovalDTO;
import com.bits.hr.service.dto.TimeSlotDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.FlexScheduleApprovalEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
public class FlexScheduleApprovalService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    @Autowired
    FlexScheduleService flexScheduleService;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public boolean updateSelectedSchedule(FlexScheduleApprovalDTO flexScheduleApprovalDTO) {
        Optional<TimeSlotDTO> timeSlotDTO = timeSlotService.findOne(flexScheduleApprovalDTO.getTimeSlotId());

        if (timeSlotDTO.isPresent()) {
            flexScheduleApprovalDTO.setInTime(timeSlotDTO.get().getInTime());
            flexScheduleApprovalDTO.setOutTime(timeSlotDTO.get().getOutTime());
        } else {
            return false;
        }

        try {
            List<Employee> selectedEmployeeList = employeeRepository.findAllById(flexScheduleApprovalDTO.getApprovalDTO().getListOfIds());

            for (Employee employee : selectedEmployeeList) {
                employee = employee.currentInTime(flexScheduleApprovalDTO.getInTime()).currentOutTime(flexScheduleApprovalDTO.getOutTime());
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
                publishEvent(employee, flexScheduleApprovalDTO, EventType.APPROVED);
            }
            flexScheduleApprovalDTO.setEffectiveDate(LocalDate.now());
            User currentUser = currentEmployeeService.getCurrentUser().get();
            flexScheduleService.saveChangedFlexSchedules(flexScheduleApprovalDTO, currentUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateSelectedScheduleLM(FlexScheduleApprovalDTO flexScheduleApprovalDTO) {
        Optional<Employee> optionalEmployee = currentEmployeeService.getCurrentEmployee();
        if (!optionalEmployee.isPresent()) {
            return false;
        }
        //validating: lm can update only his/her team members
        List<Employee> subOrdinateTeam = employeeService.getDirectReportingTo(optionalEmployee.get());
        HashSet<Long> subOrdinateHashSet = new HashSet<>();
        List<Long> listOfValidSubOrdinateId = new ArrayList<>();

        for (Employee employee : subOrdinateTeam) {
            subOrdinateHashSet.add(employee.getId());
        }
        for (long id : flexScheduleApprovalDTO.getApprovalDTO().getListOfIds()) {
            if (subOrdinateHashSet.contains(id)) {
                listOfValidSubOrdinateId.add(id);
            } else {
                continue;
            }
        }
        flexScheduleApprovalDTO.getApprovalDTO().setListOfIds(listOfValidSubOrdinateId);
        return this.updateSelectedSchedule(flexScheduleApprovalDTO);
    }

    private void publishEvent(Employee employee, FlexScheduleApprovalDTO flexScheduleApprovalDTO, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        FlexScheduleApprovalEvent flexScheduleApprovalEvent = new FlexScheduleApprovalEvent(this, employee, flexScheduleApprovalDTO, event);
        applicationEventPublisher.publishEvent(flexScheduleApprovalEvent);
    }
}
