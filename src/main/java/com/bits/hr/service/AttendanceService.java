package com.bits.hr.service;

import com.bits.hr.service.dto.AttendanceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Attendance}.
 */
public interface AttendanceService {
    /**
     * Save a attendance.
     *
     * @param attendanceDTO the entity to save.
     * @return the persisted entity.
     */
    AttendanceDTO save(AttendanceDTO attendanceDTO);

    /**
     * Get all the attendances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AttendanceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" attendance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttendanceDTO> findOne(Long id);

    /**
     * Delete the "id" attendance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
