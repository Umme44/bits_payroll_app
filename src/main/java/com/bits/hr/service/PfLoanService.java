package com.bits.hr.service;

import com.bits.hr.domain.PfLoanRepayment;
import com.bits.hr.service.dto.PfLoanDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfLoan}.
 */
public interface PfLoanService {
    /**
     * Save a pfLoan.
     *
     * @param pfLoanDTO the entity to save.
     * @return the persisted entity.
     */
    PfLoanDTO save(PfLoanDTO pfLoanDTO);

    /**
     * Get all the pfLoans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfLoanDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pfLoan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PfLoanDTO> findOne(Long id);

    /**
     * Delete the "id" pfLoan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<PfLoanRepayment> getEmployeeMonthlyRepaymentAmount(long employeeId, int year, int month);
}
