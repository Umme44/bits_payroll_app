package com.bits.hr.service.impl;

import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.service.EmploymentHistoryService;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import com.bits.hr.service.mapper.EmploymentHistoryMapper;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmploymentHistory}.
 */
@Service
@Transactional
public class EmploymentHistoryServiceImpl implements EmploymentHistoryService {

    private final Logger log = LoggerFactory.getLogger(EmploymentHistoryServiceImpl.class);

    private final EmploymentHistoryRepository employmentHistoryRepository;

    private final EmploymentHistoryMapper employmentHistoryMapper;

    public EmploymentHistoryServiceImpl(
        EmploymentHistoryRepository employmentHistoryRepository,
        EmploymentHistoryMapper employmentHistoryMapper
    ) {
        this.employmentHistoryRepository = employmentHistoryRepository;
        this.employmentHistoryMapper = employmentHistoryMapper;
    }

    @Override
    public EmploymentHistoryDTO save(EmploymentHistoryDTO employmentHistoryDTO) {
        log.debug("Request to save EmploymentHistory : {}", employmentHistoryDTO);
        EmploymentHistory employmentHistory = employmentHistoryMapper.toEntity(employmentHistoryDTO);
        employmentHistory = employmentHistoryRepository.save(employmentHistory);
        return employmentHistoryMapper.toDto(employmentHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmploymentHistories");
        return employmentHistoryRepository.findAll(pageable).map(employmentHistoryMapper::toDto);
    }

    @Override
    public Page<EmploymentHistoryDTO> findAll(EventType eventType, Pageable pageable) {
        return employmentHistoryRepository.findAll(eventType, pageable).map(employmentHistoryMapper::toDto);
    }

    @Override
    public Page<EmploymentHistoryDTO> findAll(
        EventType eventType,
        Long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    ) {
        return employmentHistoryRepository.findAll(eventType, employeeId, startDate, endDate, pageable).map(employmentHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmploymentHistoryDTO> findOne(Long id) {
        log.debug("Request to get EmploymentHistory : {}", id);
        return employmentHistoryRepository.findById(id).map(employmentHistoryMapper::toDto);
    }

    @Override
    public Optional<EmploymentHistoryDTO> findOneByIdAndEvent(Long id, EventType eventType) {
        log.debug("Request to get EmploymentHistory by id: {} and event {}", id, eventType);
        return employmentHistoryRepository.findByIdAndEventType(id, eventType).map(employmentHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmploymentHistory : {}", id);
        employmentHistoryRepository.deleteById(id);
    }

    @Override
    public List<EmploymentHistoryDTO> getEmploymentHistoryByEmployeePinBetweenTwoDates(
        String pin,
        LocalDate startDate,
        LocalDate endDate,
        EventType eventType
    ) {
        return employmentHistoryRepository
            .getEmploymentHistoryByEmployeePinBetweenTwoDates(pin, startDate, endDate, eventType)
            .stream()
            .map(employmentHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<EmploymentHistoryDTO> getEmploymentHistoryByEmployeePin(String pin, EventType eventType) {
        return employmentHistoryRepository
            .getEmploymentHistoryByEmployeePin(pin, eventType)
            .stream()
            .map(employmentHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<EmploymentHistoryDTO> getEmploymentHistoryBetweenTwoDates(LocalDate startDate, LocalDate endDate, EventType eventType) {
        return employmentHistoryRepository
            .getEmploymentHistoryBetweenTwoDates(startDate, endDate, eventType)
            .stream()
            .map(employmentHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
