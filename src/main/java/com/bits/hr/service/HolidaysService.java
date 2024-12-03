package com.bits.hr.service;

import com.bits.hr.service.dto.HolidaysDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Holidays}.
 */
public interface HolidaysService {
    /**
     * Save a holidays.
     *
     * @param holidaysDTO the entity to save.
     * @return the persisted entity.
     */
    HolidaysDTO create(HolidaysDTO holidaysDTO);

    /**
     * Get all the holidays.
     *
     * @return the list of entities.
     */
    List<HolidaysDTO> findAll();

    /**
     * Get all Holiday of a Year
     * @param year
     * @return
     */
    @Transactional(readOnly = true)
    List<HolidaysDTO> findAllOfAYear(int year);

    /**
     * Get the "id" holidays.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HolidaysDTO> findOne(Long id);

    /**
     * Delete the "id" holidays.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    HolidaysDTO update(HolidaysDTO holidaysDTO);
}
