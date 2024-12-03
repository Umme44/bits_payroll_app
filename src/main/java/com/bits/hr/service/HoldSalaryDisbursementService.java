package com.bits.hr.service;

import com.bits.hr.service.dto.HoldSalaryDisbursementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.HoldSalaryDisbursement}.
 */
public interface HoldSalaryDisbursementService {
    /**
     * Save a holdSalaryDisbursement.
     *
     * @param holdSalaryDisbursementDTO the entity to save.
     * @return the persisted entity.
     */
    HoldSalaryDisbursementDTO save(HoldSalaryDisbursementDTO holdSalaryDisbursementDTO);

    /**
     * Get all the holdSalaryDisbursements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HoldSalaryDisbursementDTO> findAll(String searchText, Pageable pageable);

    /**
     * Get the "id" holdSalaryDisbursement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HoldSalaryDisbursementDTO> findOne(Long id);

    /**
     * Delete the "id" holdSalaryDisbursement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
