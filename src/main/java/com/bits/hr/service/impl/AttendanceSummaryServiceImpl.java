package com.bits.hr.service.impl;

import com.bits.hr.domain.AttendanceSummary;
import com.bits.hr.repository.AttendanceSummaryRepository;
import com.bits.hr.service.AttendanceSummaryService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.dto.AttendanceSummaryDTO;
import com.bits.hr.service.mapper.AttendanceSummaryMapper;
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
 * Service Implementation for managing {@link AttendanceSummary}.
 */
@Service
@Transactional
public class AttendanceSummaryServiceImpl implements AttendanceSummaryService {

    private final Logger log = LoggerFactory.getLogger(AttendanceSummaryServiceImpl.class);

    private final AttendanceSummaryRepository attendanceSummaryRepository;

    private final EmployeeService employeeService;

    private final AttendanceSummaryMapper attendanceSummaryMapper;

    public AttendanceSummaryServiceImpl(
        AttendanceSummaryRepository attendanceSummaryRepository,
        EmployeeService employeeService,
        AttendanceSummaryMapper attendanceSummaryMapper
    ) {
        this.attendanceSummaryRepository = attendanceSummaryRepository;
        this.employeeService = employeeService;
        this.attendanceSummaryMapper = attendanceSummaryMapper;
    }

    @Override
    public AttendanceSummaryDTO save(AttendanceSummaryDTO attendanceSummaryDTO) {
        log.debug("Request to save AttendanceSummary : {}", attendanceSummaryDTO);
        AttendanceSummary attendanceSummary = attendanceSummaryMapper.toEntity(attendanceSummaryDTO);
        attendanceSummary = attendanceSummaryRepository.save(attendanceSummary);
        return attendanceSummaryMapper.toDto(attendanceSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceSummaryDTO> findAll() {
        log.debug("Request to get all AttendanceSummaries");
        return attendanceSummaryRepository
            .findAll()
            .stream()
            .map(attendanceSummaryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Page<AttendanceSummaryDTO> findAll(Pageable pageable) {
        return attendanceSummaryRepository.findAll(pageable).map(attendanceSummaryMapper::toDto);
    }

    @Override
    public Page<AttendanceSummaryDTO> findAll(Pageable pageable, String searchText, int month, int year) {
        return attendanceSummaryRepository.findAll(pageable, searchText, month, year).map(attendanceSummaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttendanceSummaryDTO> findOne(Long id) {
        log.debug("Request to get AttendanceSummary : {}", id);
        return attendanceSummaryRepository.findById(id).map(attendanceSummaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttendanceSummary : {}", id);
        attendanceSummaryRepository.deleteById(id);
    }

    //    @Override
    //    public AttendanceSummary findByYearMonthAndPin(int year, int month, String pin) {
    //
    //    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceSummaryDTO> findAllByYearAndMonth(int year, int month) {
        log.debug("Request to get all AttendanceSummaries by year and month");
        return attendanceSummaryRepository
            .findByYearAndMonth(year, month)
            .stream()
            .map(attendanceSummaryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceSummaryDTO> findAllByYearAndMonth(int year, int month, Pageable pageable, String searchText) {
        return attendanceSummaryRepository.findPageByYearAndMonth(year, month, pageable, searchText).map(attendanceSummaryMapper::toDto);
    }

    @Override
    public List<AttendanceSummaryDTO> findAllByPinAndName(String searchText) {
        String searchTextWild = "%" + searchText + "%";
        return attendanceSummaryRepository
            .findByEmployeePinOrName(searchText, searchTextWild)
            .stream()
            .map(attendanceSummaryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<AttendanceSummaryDTO> findByPinYearAndMonth(String pin, int month, int year) {
        Optional<AttendanceSummary> attendanceSummary = attendanceSummaryRepository.findByPinYearAndMonth(pin, month, year);
        return attendanceSummary.map(attendanceSummaryMapper::toDto);
    }
}
