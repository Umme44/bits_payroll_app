package com.bits.hr.service;

import com.bits.hr.service.dto.WorkingExperienceDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.WorkingExperience}.
 */
public interface WorkingExperienceService {
    /**
     * Save a workingExperience.
     *
     * @param workingExperienceDTO the entity to save.
     * @return the persisted entity.
     */
    WorkingExperienceDTO save(WorkingExperienceDTO workingExperienceDTO);

    /**
     * Get all the workingExperiences.
     *
     * @return the list of entities.
     */
    List<WorkingExperienceDTO> findAll();

    List<WorkingExperienceDTO> findByEmployee(long employeeId);

    /**
     * Get the "id" workingExperience.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkingExperienceDTO> findOne(Long id);

    /**
     * Delete the "id" workingExperience.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
