package com.bits.hr.service;

import com.bits.hr.service.dto.DesignationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Designation}.
 */
public interface DesignationService {
    /**
     * Save a designation.
     *
     * @param designationDTO the entity to save.
     * @return the persisted entity.
     */
    DesignationDTO save(DesignationDTO designationDTO);

    /**
     * Get all the designations.
     *
     * @return the list of entities.
     */
    List<DesignationDTO> findAll();


    /**
     * Get all the designations.
     *
     * @return the list of entities.
     */
    Page<DesignationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" designation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DesignationDTO> findOne(Long id);

    /**
     * Delete the "id" designation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
