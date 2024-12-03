package com.bits.hr.service;

import com.bits.hr.service.dto.ArrearSalaryMasterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ArrearSalaryMaster}.
 */
public interface ArrearSalaryMasterService {
    /**
     * Save a arrearSalaryMaster.
     *
     * @param arrearSalaryMasterDTO the entity to save.
     * @return the persisted entity.
     */
    ArrearSalaryMasterDTO save(ArrearSalaryMasterDTO arrearSalaryMasterDTO);

    /**
     * Get all the arrearSalaryMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArrearSalaryMasterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" arrearSalaryMaster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArrearSalaryMasterDTO> findOne(Long id);

    /**
     * Delete the "id" arrearSalaryMaster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
