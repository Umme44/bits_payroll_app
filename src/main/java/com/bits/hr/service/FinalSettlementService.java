package com.bits.hr.service;

import com.bits.hr.service.dto.FinalSettlementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.FinalSettlement}.
 */
public interface FinalSettlementService {
    /**
     * Save a finalSettlement.
     *
     * @param finalSettlementDTO the entity to save.
     * @return the persisted entity.
     */
    FinalSettlementDTO save(FinalSettlementDTO finalSettlementDTO);
    FinalSettlementDTO update(FinalSettlementDTO finalSettlementDTO);
    /**
     * Get all the finalSettlements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FinalSettlementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" finalSettlement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FinalSettlementDTO> findOne(Long id);

    /**
     * Delete the "id" finalSettlement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
