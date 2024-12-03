package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexSchedule;
import com.bits.hr.domain.User;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FlexScheduleRepository;
import com.bits.hr.service.FlexScheduleService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FlexScheduleApprovalDTO;
import com.bits.hr.service.dto.FlexScheduleDTO;
import com.bits.hr.service.mapper.FlexScheduleMapper;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FlexSchedule}.
 */
@Service
@Transactional
public class FlexScheduleServiceImpl implements FlexScheduleService {

    private final Logger log = LoggerFactory.getLogger(FlexScheduleServiceImpl.class);

    private final FlexScheduleRepository flexScheduleRepository;

    private final FlexScheduleMapper flexScheduleMapper;

    private final CurrentEmployeeService currentEmployeeService;

    private final EmployeeRepository employeeRepository;

    public FlexScheduleServiceImpl(
        FlexScheduleRepository flexScheduleRepository,
        FlexScheduleMapper flexScheduleMapper,
        CurrentEmployeeService currentEmployeeService,
        EmployeeRepository employeeRepository
    ) {
        this.flexScheduleRepository = flexScheduleRepository;
        this.flexScheduleMapper = flexScheduleMapper;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public FlexScheduleDTO save(FlexScheduleDTO flexScheduleDTO) {
        log.debug("Request to save FlexSchedule : {}", flexScheduleDTO);
        FlexSchedule flexSchedule = flexScheduleMapper.toEntity(flexScheduleDTO);
        flexSchedule = flexScheduleRepository.save(flexSchedule);
        return flexScheduleMapper.toDto(flexSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlexScheduleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FlexSchedules");
        return flexScheduleRepository.findAll(pageable).map(flexScheduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlexScheduleDTO> findOne(Long id) {
        log.debug("Request to get FlexSchedule : {}", id);
        return flexScheduleRepository.findById(id).map(flexScheduleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FlexSchedule : {}", id);
        flexScheduleRepository.deleteById(id);
    }

    @Override
    public FlexSchedule getEffectiveFlexSchedule(long employeeId, LocalDate date) {
        // select only first result
        Pageable firstFlexSchedulePageable = PageRequest.of(0, 1);

        List<FlexSchedule> flexScheduleList = flexScheduleRepository
            .findEffectiveFlexSchedule(employeeId, date, firstFlexSchedulePageable)
            .getContent();

        if (flexScheduleList.size() == 0) {
            FlexSchedule flexSchedule = new FlexSchedule();
            Instant inTime = Instant.now();
            inTime = inTime.atZone(ZoneOffset.systemDefault()).withHour(10).withMinute(0).withSecond(0).withNano(0).toInstant();
            flexSchedule.inTime(inTime);

            Instant outTime = Instant.now();
            outTime = outTime.atZone(ZoneOffset.systemDefault()).withHour(18).withMinute(0).withSecond(0).withNano(0).toInstant();
            flexSchedule.outTime(outTime);
            return flexSchedule;
        } else {
            return flexScheduleList.get(0);
        }
    }

    @Override
    @Transactional
    public boolean saveChangedFlexSchedules(FlexScheduleApprovalDTO flexScheduleApprovalDTO, User currentUser) {
        try {
            List<Employee> selectedEmployeeList = employeeRepository.findAllById(flexScheduleApprovalDTO.getApprovalDTO().getListOfIds());

            for (Employee employee : selectedEmployeeList) {
                FlexSchedule flexSchedule = new FlexSchedule();
                flexSchedule
                    .effectiveDate(LocalDate.now())
                    .createdBy(currentUser)
                    .inTime(flexScheduleApprovalDTO.getInTime())
                    .outTime(flexScheduleApprovalDTO.getOutTime())
                    .employee(employee);

                //find any saved flexScheduleForSameDay
                List<FlexSchedule> existingFlexSchedule = flexScheduleRepository.findByEmployeeAndEffectiveDate(
                    employee.getId(),
                    flexSchedule.getEffectiveDate()
                );

                if (existingFlexSchedule.size() > 1) {
                    //delete if more than 1 records
                    for (FlexSchedule flexSchedule1 : existingFlexSchedule) {
                        flexScheduleRepository.delete(flexSchedule1);
                    }
                    //new entry for flex schedule
                    flexScheduleRepository.save(flexSchedule);
                } else if (existingFlexSchedule.size() == 1) {
                    //update existing flexSchedule
                    existingFlexSchedule.get(0).inTime(flexScheduleApprovalDTO.getInTime()).outTime(flexScheduleApprovalDTO.getOutTime());
                    flexScheduleRepository.save(existingFlexSchedule.get(0));
                } else {
                    //new entry for flex schedule change
                    flexScheduleRepository.save(flexSchedule);
                }
            }
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public LocalDateTime getStartTimeByPin(String pin, LocalDate date) {
        // ignore year month day , only time is concern here
        try {
            long employeeId = employeeRepository.getIdByPin(pin);
            FlexSchedule flexSchedule = getEffectiveFlexSchedule(employeeId, date);
            Instant inTime = flexSchedule.getInTime();
            return LocalDateTime.ofInstant(inTime, ZoneId.systemDefault());
        } catch (Exception exception) {
            return LocalDateTime.of(2020, 1, 1, 10, 0, 0);
        }
    }

    @Override
    public double getOfficeTimeDurationByPin(String pin) {
        // ignore year month day , only time is concern here

        try {
            long employeeId = employeeRepository.getIdByPin(pin);
            FlexSchedule flexSchedule = getEffectiveFlexSchedule(employeeId, LocalDate.now());
            Instant inTime = flexSchedule.getInTime();
            Instant outTime = flexSchedule.getOutTime();
            return ChronoUnit.HOURS.between(inTime, outTime);
        } catch (Exception exception) {
            return 8.00d;
        }
    }

    @Override
    public List<FlexScheduleDTO> getFlexScheduleByEffectiveDates(
        Long employeeId,
        LocalDate startEffectiveDate,
        LocalDate endEffectiveDate
    ) {
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.getFlexScheduleByEffectiveDates(
            employeeId,
            startEffectiveDate,
            endEffectiveDate
        );
        List<FlexScheduleDTO> flexScheduleDTOList = flexScheduleMapper.toDto(flexScheduleList);
        return flexScheduleDTOList;
    }
}
