package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.repository.WorkFromHomeApplicationRepository;
import com.bits.hr.service.WorkFromHomeApplicationService;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.WorkFromHomeApplicationEvent;
import com.bits.hr.service.mapper.WorkFromHomeApplicationMapper;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkFromHomeApplication}.
 */
@Service
@Transactional
public class WorkFromHomeApplicationServiceImpl implements WorkFromHomeApplicationService {

    private final Logger log = LoggerFactory.getLogger(WorkFromHomeApplicationServiceImpl.class);

    private final WorkFromHomeApplicationRepository workFromHomeApplicationRepository;

    private final WorkFromHomeApplicationMapper workFromHomeApplicationMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    public WorkFromHomeApplicationServiceImpl(
        WorkFromHomeApplicationRepository workFromHomeApplicationRepository,
        WorkFromHomeApplicationMapper workFromHomeApplicationMapper,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.workFromHomeApplicationRepository = workFromHomeApplicationRepository;
        this.workFromHomeApplicationMapper = workFromHomeApplicationMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public WorkFromHomeApplicationDTO save(WorkFromHomeApplicationDTO workFromHomeApplicationDTO) {
        log.debug("Request to save WorkFromHomeApplication : {}", workFromHomeApplicationDTO);
        boolean result;
        if (workFromHomeApplicationDTO.getId() != null) {
            result =
                workFromHomeApplicationRepository.checkIsAppliedForUpdate(
                    workFromHomeApplicationDTO.getEmployeeId(),
                    workFromHomeApplicationDTO.getId(),
                    workFromHomeApplicationDTO.getStartDate(),
                    workFromHomeApplicationDTO.getEndDate()
                );
        } else {
            result =
                checkIsApplied(
                    workFromHomeApplicationDTO.getEmployeeId(),
                    workFromHomeApplicationDTO.getStartDate(),
                    workFromHomeApplicationDTO.getEndDate()
                );
        }
        if (result) {
            throw new RuntimeException("You have already applied for the duration");
        }

        Long range = ChronoUnit.DAYS.between(workFromHomeApplicationDTO.getStartDate(), workFromHomeApplicationDTO.getEndDate()) + 1;
        workFromHomeApplicationDTO.setDuration(Integer.parseInt(range.toString()));
        WorkFromHomeApplication workFromHomeApplicationSaved = workFromHomeApplicationRepository.save(
            workFromHomeApplicationMapper.toEntity(workFromHomeApplicationDTO)
        );
        publishEvent(workFromHomeApplicationSaved, EventType.CREATED);
        return workFromHomeApplicationMapper.toDto(workFromHomeApplicationSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkFromHomeApplicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkFromHomeApplications");
        return workFromHomeApplicationRepository.findAll(pageable).map(workFromHomeApplicationMapper::toDto);
    }

    // User End All Applied Requests by User

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkFromHomeApplicationDTO> findOne(Long id) {
        log.debug("Request to get WorkFromHomeApplication : {}", id);
        return workFromHomeApplicationRepository.findById(id).map(workFromHomeApplicationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkFromHomeApplication : {}", id);
        workFromHomeApplicationRepository.deleteById(id);
    }

    @Override
    public boolean checkIsApplied(Long employeeId, LocalDate bookingStartDate, LocalDate bookingEndDate) {
        return workFromHomeApplicationRepository.checkIsAppliedV2(employeeId, bookingStartDate, bookingEndDate);
    }

    @Override
    public boolean checkIsAppliedForUpdate(Long employeeId, Long workApplicationId, LocalDate bookingStartDate, LocalDate bookingEndDate) {
        return workFromHomeApplicationRepository.checkIsAppliedForUpdate(employeeId, workApplicationId, bookingStartDate, bookingEndDate);
    }

    private Page<WorkFromHomeApplicationDTO> getWorkFromHomeApplicationDTOS(
        Pageable pageable,
        List<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOList,
        Page<Employee> employeeList
    ) {
        for (Employee employee : employeeList) {
            WorkFromHomeApplicationDTO workFromHomeApplicationDTO = new WorkFromHomeApplicationDTO();
            workFromHomeApplicationDTO.setEmployeeId(employee.getId());
            workFromHomeApplicationDTO.setFullName(employee.getFullName());
            workFromHomeApplicationDTO.setDesignationName(employee.getDesignation().getDesignationName());
            workFromHomeApplicationDTO.setPin(employee.getPin());
            workFromHomeApplicationDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
            workFromHomeApplicationDTO.setBandName(employee.getBand().getBandName());
            workFromHomeApplicationDTO.setIsAllowedToGiveOnlineAttendance(employee.getIsAllowedToGiveOnlineAttendance());
            workFromHomeApplicationDTOList.add(workFromHomeApplicationDTO);
        }
        return new PageImpl<>(workFromHomeApplicationDTOList, pageable, employeeList.getTotalElements());
    }

    @Override
    public Page<WorkFromHomeApplicationDTO> getAllAppliedApplicationsByEmployeeId(long employeeId, Pageable pageable) {
        Page<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAllAppliedApplicationsByEmployeeId(
            employeeId,
            pageable
        );
        List<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOList = workFromHomeApplicationMapper.toDto(
            workFromHomeApplicationList.getContent()
        );
        return new PageImpl<>(workFromHomeApplicationDTOList, pageable, workFromHomeApplicationList.getTotalElements());
    }

    private void publishEvent(WorkFromHomeApplication workFromHomeApplication, EventType event) {
        log.debug("publishing WorkFromHomeApplication event with event: " + event);
        WorkFromHomeApplicationEvent workFromHomeApplicationEvent = new WorkFromHomeApplicationEvent(this, workFromHomeApplication, event);
        applicationEventPublisher.publishEvent(workFromHomeApplicationEvent);
    }
}
