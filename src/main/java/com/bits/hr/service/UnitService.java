package com.bits.hr.service;

import com.bits.hr.service.dto.UnitDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Unit}.
 */
public interface UnitService {
    /**
     * Save a unit.
     *
     * @param unitDTO the entity to save.
     * @return the persisted entity.
     */
    UnitDTO save(UnitDTO unitDTO);

    /**
     * Get all the units.
     *
     * @return the list of entities.
     */
    List<UnitDTO> findAll();

    /**
     * Get a page of units.
     *
     * @return the list of entities.
     */
    Page<UnitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" unit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UnitDTO> findOne(Long id);

    /**
     * Delete the "id" unit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
