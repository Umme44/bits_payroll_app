package com.bits.hr.service;

import com.bits.hr.service.dto.PfLoanRepaymentDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfLoanRepayment}.
 */
public interface PfLoanRepaymentService {
    /**
     * Save a pfLoanRepayment.
     *
     * @param pfLoanRepaymentDTO the entity to save.
     * @return the persisted entity.
     */
    PfLoanRepaymentDTO save(PfLoanRepaymentDTO pfLoanRepaymentDTO);

    /**
     * Get all the pfLoanRepayments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfLoanRepaymentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pfLoanRepayment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PfLoanRepaymentDTO> findOne(Long id);

    /**
     * Delete the "id" pfLoanRepayment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<PfLoanRepaymentDTO> findAllByYearAndMonth(int year, int month);
}
