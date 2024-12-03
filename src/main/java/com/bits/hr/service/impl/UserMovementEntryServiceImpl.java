package com.bits.hr.service.impl;

import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.domain.MovementEntry;
import com.bits.hr.domain.enumeration.MovementType;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.repository.MovementEntryRepository;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.UserMovementEntryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.mapper.MovementEntryMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class UserMovementEntryServiceImpl implements UserMovementEntryService {

    @Autowired
    private MovementEntryRepository movementEntryRepository;

    @Autowired
    private MovementEntryMapper movementEntryMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private ManualAttendanceEntryRepository manualAttendanceEntryRepository;

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Override
    public MovementEntryDTO create(MovementEntryDTO movementEntryDTO) {
        LocalDate startDate = movementEntryDTO.getStartDate(); //movementEntryDTO.getStartTime().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = movementEntryDTO.getEndDate(); //movementEntryDTO.getEndTime().atZone(ZoneId.systemDefault()).toLocalDate();

        //check any attendance or leave between days.
        if (this.findAnyConflictWithAttendance(startDate, endDate)) {
            throw new BadRequestAlertException("you have attendance between dates", "MovementEntry", "");
        }
        if (findAnyConflictWithMovementEntry(currentEmployeeService.getCurrentEmployeeId().get(), startDate, endDate)) {
            throw new BadRequestAlertException("you have movement between dates", "MovementEntry", "");
        }
        if (startDate.isBefore(startDate.minusDays(30))) {
            throw new BadRequestAlertException("30 days before movement entry not acceptable", "MovementEntry", "");
        }

        Instant startTime = startDate.atTime(10, 0, 0).toInstant(ZoneOffset.of("+06:00"));
        Instant endTime = startDate.atTime(18, 0, 0).toInstant(ZoneOffset.of("+06:00"));

        movementEntryDTO.setStartDate(startDate);
        movementEntryDTO.setEndDate(endDate);

        movementEntryDTO.setStartTime(startTime);
        movementEntryDTO.setEndTime(endTime);

        movementEntryDTO.setCreatedById(currentEmployeeService.getCurrentUserId().get());
        movementEntryDTO.setCreatedAt(LocalDate.now());
        movementEntryDTO.setEmployeeId(currentEmployeeService.getCurrentEmployeeId().get());
        movementEntryDTO.setType(MovementType.MOVEMENT);
        movementEntryDTO.setStatus(Status.PENDING);

        MovementEntry movementEntry = movementEntryMapper.toEntity(movementEntryDTO);
        movementEntry = movementEntryRepository.save(movementEntry);
        return movementEntryMapper.toDto(movementEntry);
    }

    @Override
    public MovementEntryDTO update(MovementEntryDTO movementEntryDTO) {
        Optional<MovementEntry> movementEntryOptional = movementEntryRepository.findById(movementEntryDTO.getId());
        MovementEntry movementEntryDB = movementEntryOptional.get();
        if (movementEntryDB.getStatus() != Status.PENDING) {
            return null;
        }
        LocalDate startDate = movementEntryDTO.getStartDate(); //movementEntryDTO.getStartTime().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = movementEntryDTO.getEndDate(); //movementEntryDTO.getEndTime().atZone(ZoneId.systemDefault()).toLocalDate();

        //check any attendance or leave between days.
        if (this.findAnyConflictWithAttendance(startDate, endDate)) {
            throw new BadRequestAlertException("you have attendance between dates", "MovementEntry", "");
        }
        if (findAnyConflictWithMovementEntry(movementEntryOptional.get().getId(), startDate, endDate)) {
            throw new BadRequestAlertException("you have movement between dates", "MovementEntry", "");
        }
        if (startDate.isBefore(startDate.minusDays(30))) {
            throw new BadRequestAlertException("30 days before movement entry not acceptable", "MovementEntry", "");
        }

        Instant startTime = startDate.atTime(10, 0, 0).toInstant(ZoneOffset.of("+06:00"));
        Instant endTime = startDate.atTime(18, 0, 0).toInstant(ZoneOffset.of("+06:00"));

        movementEntryDTO.setStartDate(startDate);
        movementEntryDTO.setEndDate(endDate);

        movementEntryDTO.setStartTime(startTime);
        movementEntryDTO.setEndTime(endTime);

        movementEntryDTO.setUpdatedById(currentEmployeeService.getCurrentUserId().get());
        movementEntryDTO.setUpdatedAt(LocalDate.now());
        movementEntryDTO.setEmployeeId(currentEmployeeService.getCurrentEmployeeId().get());
        movementEntryDTO.setType(MovementType.MOVEMENT);
        movementEntryDTO.setStatus(Status.PENDING);

        log.debug("Request to save MovementEntry : {}", movementEntryDTO);
        MovementEntry movementEntry = movementEntryMapper.toEntity(movementEntryDTO);
        movementEntry = movementEntryRepository.save(movementEntry);
        return movementEntryMapper.toDto(movementEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovementEntryDTO> findAll(Pageable pageable) {
        Optional<Long> employeeId = currentEmployeeService.getCurrentEmployeeId();
        Page<MovementEntryDTO> movementEntryDTOList = movementEntryRepository
            .findAllByEmployee(employeeId.get(), pageable)
            .map(movementEntryMapper::toDto);
        return movementEntryDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovementEntryDTO> findOne(Long id) {
        Optional<Long> employeeId = currentEmployeeService.getCurrentEmployeeId();

        Optional<MovementEntryDTO> movementEntryDTO = movementEntryRepository.findById(id).map(movementEntryMapper::toDto);
        if (!movementEntryDTO.isPresent() || !employeeId.isPresent()) {
            return Optional.empty();
        }
        if (movementEntryDTO.get().getEmployeeId().equals(employeeId.get())) {
            return movementEntryDTO;
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        Optional<Long> employeeId = currentEmployeeService.getCurrentEmployeeId();
        Optional<MovementEntryDTO> movementEntryDTO = movementEntryRepository.findById(id).map(movementEntryMapper::toDto);
        if (employeeId.get().equals(movementEntryDTO.get().getEmployeeId())) {
            movementEntryRepository.deleteById(id);
        }
    }

    @Override
    public boolean findAnyConflictWithAttendance(LocalDate startDate, LocalDate endDate) {
        if (!currentEmployeeService.getCurrentUserId().isPresent()) {
            return false;
        }
        if (startDate != null && endDate != null) {
            HashSet<LocalDate> attendanceEntryDTO = attendanceEntryRepository.getAttendanceEntryByEmployeeIdAndBetweenTwoDates(
                currentEmployeeService.getCurrentEmployeeId().get(),
                startDate,
                endDate
            );
            if (attendanceEntryDTO.size() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean findAnyConflictWithManualAttendance(LocalDate startDate, LocalDate endDate) {
        if (!currentEmployeeService.getCurrentUserId().isPresent()) {
            return false;
        }
        if (startDate != null && endDate != null) {
            List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAllPendingByEmployeeIdAndDateRange(
                currentEmployeeService.getCurrentEmployeeId().get(),
                startDate,
                endDate
            );
            if (manualAttendanceEntryList.size() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean findAnyConflictWithMovementEntry(Long movementEntryId, LocalDate startDate, LocalDate endDate) {
        if (!currentEmployeeService.getCurrentUserId().isPresent()) {
            return false;
        }
        if (movementEntryId == null) {
            movementEntryId = -1L;
        }
        if (startDate != null && endDate != null) {
            // existing movement entry for this date
            List<MovementEntry> movementEntryList = movementEntryRepository.getMovementEntriesByEmployeeIdBetweenDates(
                currentEmployeeService.getCurrentEmployeeId().get(),
                startDate,
                endDate
            );
            if (movementEntryList.size() > 0) {
                // if user want to edit pending movement entry
                if (movementEntryList.get(0).getId().equals(movementEntryId)) {
                    return false;
                }
                return true;
            }
        }

        return false;
    }
}
