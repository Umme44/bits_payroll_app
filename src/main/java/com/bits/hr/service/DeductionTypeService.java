package com.bits.hr.service;

import com.bits.hr.service.dto.DeductionTypeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.DeductionType}.
 */
public interface DeductionTypeService {
    /**
     * Save a deductionType.
     *
     * @param deductionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    DeductionTypeDTO save(DeductionTypeDTO deductionTypeDTO);

    /**
     * Get all the deductionTypes.
     *
     * @return the list of entities.
     */
    List<DeductionTypeDTO> findAll();

    /**
     * Get the "id" deductionType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeductionTypeDTO> findOne(Long id);

    /**
     * Delete the "id" deductionType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
