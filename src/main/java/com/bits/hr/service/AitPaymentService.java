package com.bits.hr.service;

import com.bits.hr.service.dto.AitPaymentDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.AitPayment}.
 */
public interface AitPaymentService {
    /**
     * Save a aitPayment.
     *
     * @param aitPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    AitPaymentDTO save(AitPaymentDTO aitPaymentDTO);

    /**
     * Get all the aitPayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AitPaymentDTO> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Get the "id" aitPayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AitPaymentDTO> findOne(Long id);

    /**
     * Delete the "id" aitPayment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
