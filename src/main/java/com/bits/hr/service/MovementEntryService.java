package com.bits.hr.service;

import com.bits.hr.service.dto.MovementEntryDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.MovementEntry}.
 */
public interface MovementEntryService {
    /**
     * Save a movementEntry.
     *
     * @param movementEntryDTO the entity to save.
     * @return the persisted entity.
     */
    MovementEntryDTO save(MovementEntryDTO movementEntryDTO);

    /**
     * Get all the movementEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MovementEntryDTO> findAll(Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Get the "id" movementEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MovementEntryDTO> findOne(Long id);

    /**
     * Delete the "id" movementEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    MovementEntryDTO applyAndApproveMovementEntryByHR(MovementEntryDTO movementEntryDTO);
}
