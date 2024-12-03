package com.bits.hr.service.impl;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.Employee;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import com.bits.hr.service.mapper.AttendanceEntryMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AttendanceEntry}.
 */
@Service
@Transactional
public class AttendanceEntryServiceImpl implements AttendanceEntryService {

    private final Logger log = LoggerFactory.getLogger(AttendanceEntryServiceImpl.class);

    private final AttendanceEntryRepository attendanceEntryRepository;

    private final EmployeeRepository employeeRepository;

    private final AttendanceEntryMapper attendanceEntryMapper;

    public AttendanceEntryServiceImpl(
        AttendanceEntryRepository attendanceEntryRepository,
        EmployeeRepository employeeRepository,
        AttendanceEntryMapper attendanceEntryMapper
    ) {
        this.attendanceEntryRepository = attendanceEntryRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceEntryMapper = attendanceEntryMapper;
    }

    @Override
    public AttendanceEntryDTO create(AttendanceEntryDTO attendanceEntryDTO) {
        log.debug("Request to save AttendanceEntry : {}", attendanceEntryDTO);
        Optional<AttendanceEntry> entryOptional = attendanceEntryRepository.findAttendanceEntryByDateAndEmployee(
            attendanceEntryDTO.getDate(),
            attendanceEntryDTO.getEmployeeId()
        );
        if (entryOptional.isPresent()) {
            throw new BadRequestAlertException("An attendance entry exists for same employee and date", "attendanceEntry", "entryExists");
        }
        AttendanceEntry attendanceEntry = attendanceEntryMapper.toEntity(attendanceEntryDTO);

        if (attendanceEntry.getOutTime() == null) {
            attendanceEntry.setPunchOutDeviceOrigin(null);
        }

        attendanceEntry = attendanceEntryRepository.save(attendanceEntry);
        return attendanceEntryMapper.toDto(attendanceEntry);
    }

    @Override
    public AttendanceEntryDTO update(AttendanceEntryDTO attendanceEntryDTO) {
        log.debug("Request to save AttendanceEntry : {}", attendanceEntryDTO);
        AttendanceEntry attendanceEntry = attendanceEntryMapper.toEntity(attendanceEntryDTO);

        if (attendanceEntry.getOutTime() == null) {
            attendanceEntry.setPunchOutDeviceOrigin(null);
        }

        attendanceEntry = attendanceEntryRepository.save(attendanceEntry);
        return attendanceEntryMapper.toDto(attendanceEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AttendanceEntries");
        return attendanceEntryRepository.findAll(pageable).map(attendanceEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceEntryDTO> findAll(Pageable pageable, Long employeeId, LocalDate startDate, LocalDate endDate) {
        String pin = "";
        if (employeeId != null) {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            if (employee.isPresent()) {
                pin = employee.get().getPin();
            }
        }
        return attendanceEntryRepository.findAll(pin, startDate, endDate, pageable).map(attendanceEntryMapper::toDto);
    }

    @Override
    public Optional<AttendanceEntryDTO> findByDateAndEmployeeId(LocalDate date, long id) {
        log.debug("Request to get AttendanceEntry by employee and date");
        return attendanceEntryRepository.findAttendanceEntryByDateAndEmployee(date, id).map(attendanceEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttendanceEntryDTO> findOne(Long id) {
        log.debug("Request to get AttendanceEntry : {}", id);
        return attendanceEntryRepository.findById(id).map(attendanceEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttendanceEntry : {}", id);
        attendanceEntryRepository.deleteById(id);
    }

    public AttendanceEntryDTO createOrUpdate(AttendanceEntryDTO attendanceEntryDTO) {
        Optional<AttendanceEntry> attendanceEntryOptional = attendanceEntryRepository.findAttendanceEntryByDateAndEmployee(
            attendanceEntryDTO.getDate(),
            attendanceEntryDTO.getEmployeeId()
        );

        // update on exist
        // save on not exist
        if (attendanceEntryOptional.isPresent()) {
            AttendanceEntry attendanceEntry = attendanceEntryOptional.get();

            Instant inTime = attendanceEntryDTO.getInTime();
            Instant outTime = attendanceEntryDTO.getOutTime();
            if (inTime != null) {
                attendanceEntry.setInTime(inTime);
            }
            if (inTime != null) {
                attendanceEntry.setOutTime(outTime);
            }
            return attendanceEntryMapper.toDto(attendanceEntryRepository.save(attendanceEntry));
        } else {
            return attendanceEntryMapper.toDto(attendanceEntryRepository.save(attendanceEntryMapper.toEntity(attendanceEntryDTO)));
        }
    }
}
