package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.MovementEntry;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.MovementEntryRepository;
import com.bits.hr.service.MovementEntryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.mapper.MovementEntryMapper;
import com.bits.hr.util.DateUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MovementEntry}.
 */
@Service
public class MovementEntryServiceImpl implements MovementEntryService {

    private final Logger log = LoggerFactory.getLogger(MovementEntryServiceImpl.class);

    private final MovementEntryRepository movementEntryRepository;

    private final MovementEntryMapper movementEntryMapper;

    private final EmployeeRepository employeeRepository;

    private final CurrentEmployeeService currentEmployeeService;

    public MovementEntryServiceImpl(
        MovementEntryRepository movementEntryRepository,
        MovementEntryMapper movementEntryMapper,
        EmployeeRepository employeeRepository,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.movementEntryRepository = movementEntryRepository;
        this.movementEntryMapper = movementEntryMapper;
        this.employeeRepository = employeeRepository;
        this.currentEmployeeService = currentEmployeeService;
    }

    @Override
    public MovementEntryDTO save(MovementEntryDTO movementEntryDTO) {
        log.debug("Request to save MovementEntry : {}", movementEntryDTO);
        MovementEntry movementEntry = movementEntryMapper.toEntity(movementEntryDTO);
        movementEntry = movementEntryRepository.save(movementEntry);
        MovementEntryDTO result = movementEntryMapper.toDto(movementEntry);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovementEntryDTO> findAll(Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Request to get all MovementEntries");
        if (employeeId == null) {
            employeeId = -1L;
        }
        return movementEntryRepository.findAll(employeeId, startDate, endDate, pageable).map(movementEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovementEntryDTO> findOne(Long id) {
        log.debug("Request to get MovementEntry : {}", id);
        return movementEntryRepository.findById(id).map(movementEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MovementEntry : {}", id);
        movementEntryRepository.deleteById(id);
    }

    @Override
    public MovementEntryDTO applyAndApproveMovementEntryByHR(MovementEntryDTO movementEntryDTO) {
        if (movementEntryDTO.getStartDate() == null || movementEntryDTO.getEndDate() == null) {
            throw new BadRequestAlertException("Start Date or End Date is Null", "movementEntry", "dateIsNull");
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(movementEntryDTO.getEmployeeId());
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee has Not Found", "movementEntry", "employeeHasNotFound");
        }

        Optional<Long> currentUserId = currentEmployeeService.getCurrentUserId();

        if (currentUserId.isPresent()) {
            movementEntryDTO.setCreatedById(currentUserId.get());
        } else {
            throw new NoEmployeeProfileException();
        }

        movementEntryDTO.setStartTime(
            DateUtil.getInstantByDate(
                movementEntryDTO.getStartDate(),
                Instant.now().atZone(ZoneId.systemDefault()).withHour(10).withMinute(0).withSecond(0).withNano(0).toInstant()
            )
        );

        movementEntryDTO.setEndTime(
            DateUtil.getInstantByDate(
                movementEntryDTO.getEndDate(),
                Instant.now().atZone(ZoneId.systemDefault()).withHour(6).withMinute(0).withSecond(0).withNano(0).toInstant()
            )
        );

        movementEntryDTO.setStatus(Status.APPROVED);
        movementEntryDTO.setStartNote("Start note - HR Regularization");
        movementEntryDTO.setEndNote("End note - HR Regularization");
        movementEntryDTO.setNote("HR Regularization");

        movementEntryDTO.setCreatedAt(LocalDate.now());

        movementEntryDTO.setSanctionAt(LocalDate.now());
        movementEntryDTO.setSanctionById(currentUserId.get());

        return save(movementEntryDTO);
    }
}
