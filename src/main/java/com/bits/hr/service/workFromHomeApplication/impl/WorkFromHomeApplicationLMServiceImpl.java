package com.bits.hr.service.workFromHomeApplication.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.repository.WorkFromHomeApplicationRepository;
import com.bits.hr.service.approvalProcess.WorkFromHomeEnableLMService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import com.bits.hr.service.event.ApprovalVia;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.WorkFromHomeApplicationEvent;
import com.bits.hr.service.mapper.WorkFromHomeApplicationMapper;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeApplicationLMService;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeRefreshService;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WorkFromHomeApplicationLMServiceImpl implements WorkFromHomeApplicationLMService {

    private final Logger log = LoggerFactory.getLogger(WorkFromHomeApplicationLMService.class);

    private final WorkFromHomeApplicationRepository workFromHomeApplicationRepository;

    private final WorkFromHomeApplicationMapper workFromHomeApplicationMapper;

    private final CurrentEmployeeService currentEmployeeService;

    private final EmployeeRepository employeeRepository;

    private final WorkFromHomeRefreshService workFromHomeRefreshService;

    private final WorkFromHomeEnableLMService workFromHomeEnableLMService;

    private final ApplicationEventPublisher applicationEventPublisher;

    public WorkFromHomeApplicationLMServiceImpl(
        WorkFromHomeApplicationRepository workFromHomeApplicationRepository,
        WorkFromHomeApplicationMapper workFromHomeApplicationMapper,
        CurrentEmployeeService currentEmployeeService,
        EmployeeRepository employeeRepository,
        WorkFromHomeRefreshService workFromHomeRefreshService,
        WorkFromHomeEnableLMService workFromHomeEnableLMService,
        LeaveApplicationRepository leaveApplicationRepository,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.workFromHomeApplicationRepository = workFromHomeApplicationRepository;
        this.workFromHomeApplicationMapper = workFromHomeApplicationMapper;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeRepository = employeeRepository;
        this.workFromHomeRefreshService = workFromHomeRefreshService;
        this.workFromHomeEnableLMService = workFromHomeEnableLMService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Page<WorkFromHomeApplicationDTO> getAllSubOrdinateApplications(String searchText, Employee employee, Pageable pageable) {
        Page<Employee> employeeList = employeeRepository.getActiveReportingToByIdAndByPinOrByFullName(
            searchText,
            employee.getId(),
            LocalDate.now(),
            pageable
        );
        List<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        for (Employee employee1 : employeeList.getContent()) {
            WorkFromHomeApplicationDTO workFromHomeApplicationDTO = new WorkFromHomeApplicationDTO();
            workFromHomeApplicationDTO.setEmployeeId(employee1.getId());
            workFromHomeApplicationDTO.setFullName(employee1.getFullName());
            workFromHomeApplicationDTO.setDesignationName(employee1.getDesignation().getDesignationName());
            workFromHomeApplicationDTO.setPin(employee1.getPin());
            workFromHomeApplicationDTO.setDepartmentName(employee1.getDepartment().getDepartmentName());
            workFromHomeApplicationDTO.setBandName(employee1.getBand().getBandName());
            workFromHomeApplicationDTO.setIsAllowedToGiveOnlineAttendance(employee1.getIsAllowedToGiveOnlineAttendance());

            List<WorkFromHomeApplication> futureApprovedWFHApplication = workFromHomeApplicationRepository.findFutureApprovedWFHApplication(
                employee1.getId(),
                currentDate,
                PageRequest.of(0, 1)
            );
            if (futureApprovedWFHApplication.size() > 0) {
                workFromHomeApplicationDTO.setApprovedStartDate(futureApprovedWFHApplication.get(0).getStartDate());
            }
            workFromHomeApplicationDTOList.add(workFromHomeApplicationDTO);
        }
        return new PageImpl<>(workFromHomeApplicationDTOList, pageable, employeeList.getTotalElements());
    }

    // Line Manager Pending Requests
    @Override
    public Page<WorkFromHomeApplicationDTO> getAllPendingSubOrdinateApplications(long employeeId, String searchText, Pageable pageable) {
        Page<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOList = workFromHomeApplicationRepository
            .findPendingApplicationsByLineManagerId(employeeId, searchText, pageable)
            .map(workFromHomeApplicationMapper::toDto);

        List<WorkFromHomeApplicationDTO> finalWorkFromHomeApplicationDTOList = new ArrayList<>();
        for (WorkFromHomeApplicationDTO workFromHomeApplicationDTO : workFromHomeApplicationDTOList.getContent()) {
            workFromHomeApplicationDTO.setEmployeeId(workFromHomeApplicationDTO.getEmployeeId());
            LocalDate currentDate = LocalDate.now();
            List<WorkFromHomeApplication> futureApprovedWFHApplication = workFromHomeApplicationRepository.findFutureApprovedWFHApplication(
                workFromHomeApplicationDTO.getEmployeeId(),
                currentDate,
                PageRequest.of(0, 1)
            );
            if (futureApprovedWFHApplication.size() > 0) {
                workFromHomeApplicationDTO.setApprovedStartDate(futureApprovedWFHApplication.get(0).getStartDate());
            }
            finalWorkFromHomeApplicationDTOList.add(workFromHomeApplicationDTO);
        }
        return new PageImpl<>(finalWorkFromHomeApplicationDTOList, pageable, workFromHomeApplicationDTOList.getTotalElements());
    }

    @Override
    public boolean enableSelectedLM(List<Long> selectedIds, long currentEmployeeId) {
        try {
            Optional<User> user = currentEmployeeService.getCurrentUser();
            LocalDate approvalDate = LocalDate.now();

            for (long id : selectedIds) {
                Optional<Employee> employeeOptional = Optional.empty();
                Optional<WorkFromHomeApplication> workFromHomeApplicationOptional = workFromHomeApplicationRepository.findById(id);
                if (workFromHomeApplicationOptional.isPresent()) {
                    employeeOptional = employeeRepository.findById(workFromHomeApplicationOptional.get().getEmployee().getId());
                }

                if (workFromHomeApplicationOptional.isPresent() && employeeOptional.isPresent()) {
                    Employee employee = employeeOptional.get();
                    WorkFromHomeApplication workFromHomeApplication = workFromHomeApplicationOptional.get();
                    if (employee.getReportingTo() != null && workFromHomeEnableLMService.verifySubOrdinate(employee.getId())) {
                        workFromHomeApplication.setStatus(Status.APPROVED);
                        user.ifPresent(workFromHomeApplication::setSanctionedBy);
                        workFromHomeApplication.setSanctionedAt(Instant.now());
                        workFromHomeApplicationRepository.save(workFromHomeApplication);

                        // refresh employee web punch
                        workFromHomeRefreshService.refreshWFH(approvalDate, employee);
                        publishEvent(workFromHomeApplication, EventType.APPROVED, ApprovalVia.LM);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.toString());
            return true;
        }
    }

    @Override
    public boolean disableSelectedLM(List<Long> selectedIds, long currentEmployeeId) {
        LocalDate rejectionDate = LocalDate.now();
        try {
            Optional<User> user = currentEmployeeService.getCurrentUser();

            for (long id : selectedIds) {
                Optional<Employee> employeeOptional = Optional.empty();
                Optional<WorkFromHomeApplication> workFromHomeApplicationOptional = workFromHomeApplicationRepository.findById(id);
                if (workFromHomeApplicationOptional.isPresent()) {
                    employeeOptional = employeeRepository.findById(workFromHomeApplicationOptional.get().getEmployee().getId());
                }

                if (workFromHomeApplicationOptional.isPresent() && employeeOptional.isPresent()) {
                    Employee employee = employeeOptional.get();
                    WorkFromHomeApplication workFromHomeApplication = workFromHomeApplicationOptional.get();
                    if (employee.getReportingTo() != null && workFromHomeEnableLMService.verifySubOrdinate(employee.getId())) {
                        workFromHomeApplication.setStatus(Status.NOT_APPROVED);
                        user.ifPresent(workFromHomeApplication::setSanctionedBy);
                        workFromHomeApplication.setSanctionedAt(Instant.now());
                        workFromHomeApplicationRepository.save(workFromHomeApplication);

                        // refresh employee web punch
                        workFromHomeRefreshService.refreshWFH(rejectionDate, employee);
                        publishEvent(workFromHomeApplication, EventType.REJECTED, ApprovalVia.LM);
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.toString());
            return true;
        }
    }

    private void publishEvent(WorkFromHomeApplication workFromHomeApplication, EventType event, ApprovalVia approvalVia) {
        log.debug("publishing WorkFromHomeApplication event with event: " + event);
        WorkFromHomeApplicationEvent workFromHomeApplicationEvent = new WorkFromHomeApplicationEvent(
            this,
            workFromHomeApplication,
            event,
            approvalVia
        );
        applicationEventPublisher.publishEvent(workFromHomeApplicationEvent);
    }
}
