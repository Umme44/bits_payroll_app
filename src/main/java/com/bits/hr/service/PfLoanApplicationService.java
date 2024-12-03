package com.bits.hr.service;

import com.bits.hr.service.dto.PfLoanApplicationDTO;
import com.bits.hr.service.dto.PfLoanApplicationEligibleDTO;
import com.bits.hr.service.dto.PfLoanApplicationFormDTO;
import com.bits.hr.service.dto.PfLoanDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfLoanApplication}.
 */
public interface PfLoanApplicationService {
    /**
     * Save a pfLoanApplication.
     *
     * @param pfLoanApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    PfLoanApplicationDTO save(PfLoanApplicationDTO pfLoanApplicationDTO);

    /**
     * Get all the pfLoanApplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfLoanApplicationDTO> findAll(Pageable pageable);

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

    /**
     * approve a pfLoanApplication.
     *
     * @param pfLoanApplicationFormDTO the entity to save.
     * @return the persisted entity.
     */
    PfLoanDTO approve(PfLoanApplicationFormDTO pfLoanApplicationFormDTO) throws Exception;

    /**
     * reject a pfLoanApplication.
     *
     * @param pfLoanApplicationFormDTO the entity to fillup reject related fields.
     * @return true if success.
     */
    PfLoanApplicationDTO reject(PfLoanApplicationFormDTO pfLoanApplicationFormDTO);

    /**
     * check pf loan eligibility.
     *
     * @param pfAccountId to get find employee details, pf account information for validation.
     * @return PfLoanApplicationFormDto details
     */
    PfLoanApplicationEligibleDTO checkPfLoanApplicationEligibility(long pfAccountId);
}
