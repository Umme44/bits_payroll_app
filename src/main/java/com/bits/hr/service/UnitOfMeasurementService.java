package com.bits.hr.service;

import com.bits.hr.service.dto.UnitOfMeasurementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.UnitOfMeasurement}.
 */
public interface UnitOfMeasurementService {
    /**
     * Save a unitOfMeasurement.
     *
     * @param unitOfMeasurementDTO the entity to save.
     * @return the persisted entity.
     */
    UnitOfMeasurementDTO save(UnitOfMeasurementDTO unitOfMeasurementDTO);

    /**
     * Get all the unitOfMeasurements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UnitOfMeasurementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" unitOfMeasurement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UnitOfMeasurementDTO> findOne(Long id);

    /**
     * Delete the "id" unitOfMeasurement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean isNameUnique(Long id, String name);
}
