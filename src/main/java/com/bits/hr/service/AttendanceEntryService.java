package com.bits.hr.service;

import com.bits.hr.service.dto.AttendanceEntryDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.AttendanceEntry}.
 */
public interface AttendanceEntryService {
    /**
     * Save a attendanceEntry.
     *
     * @param attendanceEntryDTO the entity to save.
     * @return the persisted entity.
     */
    AttendanceEntryDTO create(AttendanceEntryDTO attendanceEntryDTO);
    AttendanceEntryDTO update(AttendanceEntryDTO attendanceEntryDTO);
    AttendanceEntryDTO createOrUpdate(AttendanceEntryDTO attendanceEntryDTO);

    /**
     * Get all the attendanceEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AttendanceEntryDTO> findAll(Pageable pageable);

    Page<AttendanceEntryDTO> findAll(Pageable pageable, Long employeeId, LocalDate startDate, LocalDate endDate);

    Optional<AttendanceEntryDTO> findByDateAndEmployeeId(LocalDate date, long id);

    /**
     * Get the "id" attendanceEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttendanceEntryDTO> findOne(Long id);

    /**
     * Delete the "id" attendanceEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
