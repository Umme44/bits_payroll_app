package com.bits.hr.service;

import com.bits.hr.service.dto.IncomeTaxChallanDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.IncomeTaxChallan}.
 */
public interface IncomeTaxChallanService {
    /**
     * Save a incomeTaxChallan.
     *
     * @param incomeTaxChallanDTO the entity to save.
     * @return the persisted entity.
     */
    IncomeTaxChallanDTO save(IncomeTaxChallanDTO incomeTaxChallanDTO);

    /**
     * Get all the incomeTaxChallans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IncomeTaxChallanDTO> findAll(Pageable pageable, Long aitConfigId);

    /**
     * Get the "id" incomeTaxChallan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IncomeTaxChallanDTO> findOne(Long id);

    /**
     * Delete the "id" incomeTaxChallan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<IncomeTaxChallanDTO> getIncomeTaxChallanListByAitConfigId(long aitConfigId);

    String getIncomeTaxChallanForIncomeTaxStatement(long aitConfigId);
}
