package com.bits.hr.service;

import com.bits.hr.service.dto.ReferencesDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.References}.
 */
public interface ReferencesService {
    /**
     * Save a references.
     *
     * @param referencesDTO the entity to save.
     * @return the persisted entity.
     */
    ReferencesDTO save(ReferencesDTO referencesDTO);

    /**
     * Get all the references.
     *
     * @return the list of entities.
     */
    List<ReferencesDTO> findAll();

    /**
     * Get the "id" references.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReferencesDTO> findOne(Long id);

    /**
     * Delete the "id" references.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ReferencesDTO> findAllByEmployee(long employeeId);
}
