package com.bits.hr.service;

import com.bits.hr.service.dto.HoldFbDisbursementApprovalDTO;
import com.bits.hr.service.dto.HoldFbDisbursementDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.HoldFbDisbursement}.
 */
public interface HoldFbDisbursementService {
    /**
     * Save a holdFbDisbursement.
     *
     * @param holdFbDisbursementDTO the entity to save.
     * @return the persisted entity.
     */
    HoldFbDisbursementDTO save(HoldFbDisbursementDTO holdFbDisbursementDTO);

    List<HoldFbDisbursementDTO> disburseHoldFestivalBonus(HoldFbDisbursementApprovalDTO holdFbDisbursementApprovalDTO);

    /**
     * Get all the holdFbDisbursements.
     *
     * @param searchText
     * @param pageable   the pagination information.
     * @return the list of entities.
     */
    Page<HoldFbDisbursementDTO> findAll(String searchText, Pageable pageable);

    /**
     * Get the "id" holdFbDisbursement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HoldFbDisbursementDTO> findOne(Long id);

    /**
     * Delete the "id" holdFbDisbursement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
