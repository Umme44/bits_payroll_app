package com.bits.hr.service;

import com.bits.hr.service.dto.EducationDetailsDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EducationDetails}.
 */
public interface EducationDetailsService {
    /**
     * Save a educationDetails.
     *
     * @param educationDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    EducationDetailsDTO save(EducationDetailsDTO educationDetailsDTO);

    /**
     * Get all the educationDetails.
     *
     * @return the list of entities.
     */
    List<EducationDetailsDTO> findAll();

    List<EducationDetailsDTO> findAllByEmployeeId(long employeeId);

    /**
     * Get the "id" educationDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EducationDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" educationDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
