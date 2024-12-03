package com.bits.hr.service;

import com.bits.hr.service.dto.ArrearPaymentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ArrearPayment}.
 */
public interface ArrearPaymentService {
    /**
     * Save a arrearPayment.
     *
     * @param arrearPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    ArrearPaymentDTO save(ArrearPaymentDTO arrearPaymentDTO);

    /**
     * Get all the arrearPayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArrearPaymentDTO> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    Page<ArrearPaymentDTO> findAllByArrearItem(Pageable pageable, long arrearItemId);

    /**
     * Get the "id" arrearPayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArrearPaymentDTO> findOne(Long id);

    /**
     * Delete the "id" arrearPayment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
