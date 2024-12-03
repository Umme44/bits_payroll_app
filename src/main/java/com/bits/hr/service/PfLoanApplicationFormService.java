package com.bits.hr.service;

import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfLoanApplication}.
 */
public interface PfLoanApplicationFormService {
    /**
     * Save a pfLoanApplication.
     *
     * @param pfLoanApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    PfLoanApplicationDTO save(PfLoanApplicationDTO pfLoanApplicationDTO);

    /**
     * Get by pin of the pfLoanApplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfLoanApplicationDTO> findByPin(Pageable pageable);

    /**
     * Get all the pfAccounts by pin.
     *
     * @return the list of entities.
     */
    List<PfAccountDTO> findPfAccountsOfCurrentUser() throws Exception;

    /**
     * Get current employee pf loan eligible amount.
     *
     * @return pf loan eligible amount.
     */
    double getPfLoanEligibleAmount() throws Exception;

    boolean checkAnyPfAccountForThisUser();

    /**
     * Get the "id" pfLoanApplication.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PfLoanApplicationDTO> findOne(Long id);

    /**
     * Delete the "id" pfLoanApplication.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
