package com.bits.hr.service;

import com.bits.hr.domain.SpecialShiftTiming;
import com.bits.hr.service.dto.SpecialShiftTimingDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link SpecialShiftTiming}.
 */
public interface SpecialShiftTimingService {
    /**
     * Save a ramadanSchedule.
     *
     * @param specialShiftTimingDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialShiftTimingDTO save(SpecialShiftTimingDTO specialShiftTimingDTO);

    /**
     * Get all the specialShiftTiming.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpecialShiftTimingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ramadanSchedule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialShiftTimingDTO> findOne(Long id);

    /**
     * Delete the "id" ramadanSchedule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<SpecialShiftTiming> findSpecialShiftByDate(LocalDate effectiveFromDate);
}
