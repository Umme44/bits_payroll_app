package com.bits.hr.service;

import com.bits.hr.service.dto.ArrearSalaryItemDTO;
import com.bits.hr.service.dto.ArrearSalaryItemDisburseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ArrearSalaryItem}.
 */
public interface ArrearSalaryItemService {
    /**
     * Save a arrearSalaryItem.
     *
     * @param arrearSalaryItemDTO the entity to save.
     * @return the persisted entity.
     */
    ArrearSalaryItemDTO save(ArrearSalaryItemDTO arrearSalaryItemDTO);

    /**
     * Get all the arrearSalaryItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArrearSalaryItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" arrearSalaryItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArrearSalaryItemDTO> findOne(Long id);

    /**
     * Delete the "id" arrearSalaryItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<ArrearSalaryItemDTO> findByArrearsSalaryMaster(long arrearsSalaryMasterId, Pageable pageable);

    Boolean disburseSelectedArrearSalaryItem(ArrearSalaryItemDisburseDTO arrearSalaryItemDisburseDTO);
}
