package com.bits.hr.service;

import com.bits.hr.service.dto.MovementEntryDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserMovementEntryService {
    MovementEntryDTO create(MovementEntryDTO movementEntryDTO);

    MovementEntryDTO update(MovementEntryDTO movementEntryDTO);

    Page<MovementEntryDTO> findAll(Pageable pageable);

    Optional<MovementEntryDTO> findOne(Long id);

    void delete(Long id);

    boolean findAnyConflictWithAttendance(LocalDate startDate, LocalDate endDate);

    boolean findAnyConflictWithManualAttendance(LocalDate startDate, LocalDate endDate);

    boolean findAnyConflictWithMovementEntry(Long movementEntryId, LocalDate startDate, LocalDate endDate);
}
