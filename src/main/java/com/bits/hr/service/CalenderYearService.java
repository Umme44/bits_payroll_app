package com.bits.hr.service;

import com.bits.hr.service.dto.CalenderYearDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.CalenderYear}.
 */
public interface CalenderYearService {
    /**
     * Save a calenderYear.
     *
     * @param calenderYearDTO the entity to save.
     * @return the persisted entity.
     */
    CalenderYearDTO save(CalenderYearDTO calenderYearDTO);

    /**
     * Get all the calenderYears.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CalenderYearDTO> findAll(Pageable pageable);

    /**
     * Get the "id" calenderYear.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CalenderYearDTO> findOne(Long id);

    /**
     * Delete the "id" calenderYear.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
