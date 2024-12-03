package com.bits.hr.service.impl;

import com.bits.hr.domain.AttendanceEntry;
import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.service.ManualAttendanceEntryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import com.bits.hr.service.mapper.ManualAttendanceEntryMapper;
import com.bits.hr.service.search.FilterDto;
import com.bits.hr.util.DateUtil;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ManualAttendanceEntry}.
 */
@Service
@Transactional
public class ManualAttendanceEntryServiceImpl implements ManualAttendanceEntryService {

    private final Logger log = LoggerFactory.getLogger(ManualAttendanceEntryServiceImpl.class);

    private final ManualAttendanceEntryRepository manualAttendanceEntryRepository;
    private final AttendanceEntryRepository attendanceEntryRepository;

    private final ManualAttendanceEntryMapper manualAttendanceEntryMapper;
    private final CurrentEmployeeService currentEmployeeService;

    public ManualAttendanceEntryServiceImpl(
        ManualAttendanceEntryRepository manualAttendanceEntryRepository,
        AttendanceEntryRepository attendanceEntryRepository,
        ManualAttendanceEntryMapper manualAttendanceEntryMapper,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.manualAttendanceEntryRepository = manualAttendanceEntryRepository;
        this.attendanceEntryRepository = attendanceEntryRepository;
        this.manualAttendanceEntryMapper = manualAttendanceEntryMapper;
        this.currentEmployeeService = currentEmployeeService;
    }

    @Override
    public List<ManualAttendanceEntryDTO> findAllByPinAndDateRange(String pin, FilterDto filterDto) {
        if (filterDto.getStartDate() == null) {
            log.info("Start date is null, default set to current year 1st day");
            filterDto.setStartDate(LocalDate.ofYearDay(LocalDate.now().getYear(), 1));
        }
        if (filterDto.getEndDate() == null) {
            log.info("End date is null, default set to today date");
            filterDto.setEndDate(LocalDate.now());
        }

        List<ManualAttendanceEntry> manualAttendanceEntries = manualAttendanceEntryRepository.findAllByPinAndDateRange(
            pin,
            filterDto.getStartDate(),
            filterDto.getEndDate()
        );
        return manualAttendanceEntryMapper.toDto(manualAttendanceEntries);
    }

    @Override
    public List<ManualAttendanceEntryDTO> findAllPendingManualAttendance(Long employeeId, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAllPendingByEmployeeIdAndDateRange(
                employeeId,
                startDate,
                endDate
            );
            return manualAttendanceEntryMapper.toDto(manualAttendanceEntryList);
        }
        return new ArrayList<>();
    }

    @Override
    public Page<ManualAttendanceEntryDTO> findAllOfCommonUser(Pageable pageable) {
        Optional<Long> employeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!employeeId.isPresent()) {
            return Page.empty();
        }
        Page<ManualAttendanceEntry> manualAttendanceEntries = manualAttendanceEntryRepository.findAllByEmployeeId(
            employeeId.get(),
            pageable
        );
        return manualAttendanceEntries.map(manualAttendanceEntryMapper::toDto);
    }

    @Override
    public ManualAttendanceEntryDTO create(ManualAttendanceEntryDTO manualAttendanceEntryDTO) {
        log.debug("Request to save ManualAttendanceEntry : {}", manualAttendanceEntryDTO);

        /* delete entry for any previous record of selected date */
        if (manualAttendanceEntryDTO.getEmployeeId() != null) {
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAllPendingByEmployeeIdAndDate(
                manualAttendanceEntryDTO.getEmployeeId(),
                manualAttendanceEntryDTO.getDate()
            );
            manualAttendanceEntryRepository.deleteAll(manualAttendanceEntryList);
        }

        ManualAttendanceEntry manualAttendanceEntry = manualAttendanceEntryMapper.toEntity(manualAttendanceEntryDTO);
        manualAttendanceEntry = manualAttendanceEntryRepository.save(manualAttendanceEntry);
        return manualAttendanceEntryMapper.toDto(manualAttendanceEntry);
    }

    @Override
    public boolean checkExitingAttendanceEntry(ManualAttendanceEntryDTO manualAttendanceEntry) {
        Optional<ManualAttendanceEntry> findManualAttendanceEntry = manualAttendanceEntryRepository.getExistingAttendanceEntry(
            manualAttendanceEntry.getEmployeeId(),
            manualAttendanceEntry.getDate()
        );
        if (findManualAttendanceEntry.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ManualAttendanceEntryDTO update(ManualAttendanceEntryDTO manualAttendanceEntryDTO) {
        log.debug("Request to update ManualAttendanceEntry : {}", manualAttendanceEntryDTO);
        ManualAttendanceEntry manualAttendanceEntry = manualAttendanceEntryMapper.toEntity(manualAttendanceEntryDTO);
        manualAttendanceEntry = manualAttendanceEntryRepository.save(manualAttendanceEntry);
        return manualAttendanceEntryMapper.toDto(manualAttendanceEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManualAttendanceEntryDTO> findAllDTOs(Pageable pageable, long employeeId) {
        log.debug("Request to get all ManualAttendanceEntries");
        return manualAttendanceEntryRepository.findAllByEmployeeId(pageable, employeeId).map(manualAttendanceEntryMapper::toDto);
    }

    @Override
    public List<ManualAttendanceEntryDTO> findAllDTOs() {
        log.debug("Request to get all ManualAttendanceEntries");
        return manualAttendanceEntryRepository.findAll().stream().map(manualAttendanceEntryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ManualAttendanceEntry> findAll() {
        return manualAttendanceEntryRepository.findAll();
    }

    @Override
    public Page<ManualAttendanceEntryDTO> findAll(Pageable pageable) {
        return manualAttendanceEntryRepository.findAll(pageable).map(manualAttendanceEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManualAttendanceEntryDTO> findOne(Long id) {
        log.debug("Request to get ManualAttendanceEntry : {}", id);
        return manualAttendanceEntryRepository.findById(id).map(manualAttendanceEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ManualAttendanceEntry : {}", id);
        manualAttendanceEntryRepository.deleteById(id);
    }

    @Override
    public ManualAttendanceEntryDTO save(ManualAttendanceEntryDTO manualAttendanceEntryDTO) {
        log.debug("Request to save manualAttendanceEntry : {}", manualAttendanceEntryDTO);
        ManualAttendanceEntry manualAttendanceEntry = manualAttendanceEntryMapper.toEntity(manualAttendanceEntryDTO);
        manualAttendanceEntry = manualAttendanceEntryRepository.save(manualAttendanceEntry);
        return manualAttendanceEntryMapper.toDto(manualAttendanceEntry);
    }

    @Override
    public ManualAttendanceEntryDTO applyAndApproveByHR(ManualAttendanceEntryDTO manualAttendanceEntryDTO) {
        log.debug("Request to approve manualAttendance Entry by HR : {}", manualAttendanceEntryDTO);

        // approve manual attendance entry
        manualAttendanceEntryDTO.setNote("HR Regularization");
        manualAttendanceEntryDTO.setIsHRApproved(true);
        manualAttendanceEntryDTO.setIsLineManagerApproved(false);
        manualAttendanceEntryDTO.setIsRejected(false);

        // change in time to attendance date
        if (manualAttendanceEntryDTO.getInTime() != null) {
            Instant inTime = DateUtil.getInstantByDate(manualAttendanceEntryDTO.getDate(), manualAttendanceEntryDTO.getInTime());
            manualAttendanceEntryDTO.setInTime(inTime);
        }

        if (manualAttendanceEntryDTO.getOutTime() != null) {
            Instant outTime = DateUtil.getInstantByDate(manualAttendanceEntryDTO.getDate(), manualAttendanceEntryDTO.getOutTime());
            manualAttendanceEntryDTO.setOutTime(outTime);
        }

        ManualAttendanceEntry manualAttendanceEntry = manualAttendanceEntryMapper.toEntity(manualAttendanceEntryDTO);
        manualAttendanceEntry = manualAttendanceEntryRepository.save(manualAttendanceEntry);

        log.info("Find Attendance of date = {} for employeeId= {} ", manualAttendanceEntry.getDate(), manualAttendanceEntry.getEmployee());
        Optional<AttendanceEntry> attendanceEntryOptional = attendanceEntryRepository.findByEmployeeIdAndDate(
            manualAttendanceEntry.getEmployee().getId(),
            manualAttendanceEntry.getDate()
        );

        AttendanceEntry attendanceEntry;

        // get previous saved
        if (attendanceEntryOptional.isPresent()) {
            attendanceEntry = attendanceEntryOptional.get();
        } else {
            // map and save to attendance entry
            attendanceEntry = new AttendanceEntry();
            attendanceEntry.setDate(manualAttendanceEntry.getDate());
        }

        attendanceEntry.setNote(manualAttendanceEntry.getNote());
        attendanceEntry.setOutTime(manualAttendanceEntry.getOutTime());
        attendanceEntry.setInTime(manualAttendanceEntry.getInTime());
        attendanceEntry.setEmployee(manualAttendanceEntry.getEmployee());
        attendanceEntry.setPunchInDeviceOrigin(AttendanceDeviceOrigin.WEB);
        attendanceEntry.setPunchOutDeviceOrigin(AttendanceDeviceOrigin.WEB);
        attendanceEntry.setStatus(Status.APPROVED);

        if (attendanceEntry.getOutTime() == null) {
            attendanceEntry.setPunchOutDeviceOrigin(null);
        }

        attendanceEntryRepository.save(attendanceEntry);

        return manualAttendanceEntryMapper.toDto(manualAttendanceEntry);
    }

    @Override
    public Boolean findDuplicateEntryForDate(long employeeId, LocalDate date) {
        return manualAttendanceEntryRepository.findDuplicateEntryForDate(employeeId, date);
    }
}
