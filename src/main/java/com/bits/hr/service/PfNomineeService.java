package com.bits.hr.service;

import com.bits.hr.service.dto.NomineeValidationDTO;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.service.search.FilterDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfNominee}.
 */
public interface PfNomineeService {
    /**
     * Save a pfNominee.
     *
     * @param pfNomineeDTO the entity to save.
     * @return the persisted entity.
     */
    PfNomineeDTO save(PfNomineeDTO pfNomineeDTO);

    /**
     * Get all the pfNominees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfNomineeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pfNominee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PfNomineeDTO> findOne(Long id);

    /**
     * Delete the "id" pfNominee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    void deleteForCommonUserNominee(Long id);

    List<PfNomineeDTO> findAllByPfAccountId(Long id);

    @Transactional(readOnly = true)
    List<PfAccountDTO> getPfAccountWithNominees(FilterDto userFilterDto);

    double getRemainingSharePercentageByPFAccountId(long pfAccountId);

    double getRemainingSharePercentageByPFNominee(PfNomineeDTO pfNomineeDTO);

    boolean isEmployeeEligibleForPF();

    PfNomineeDTO validateNomineeAndGuardianNID(PfNomineeDTO pfNomineeDTO);

    PfNomineeDTO validateIsApproved(PfNomineeDTO pfNomineeDTO);

    NomineeValidationDTO validateNominee(PfNomineeDTO pfNomineeDTO);

    double getTotalConsumedSharedPercentageOfCurrentUser(long pfAccountId);

    Page<PfNomineeDTO> getAllPFNomineeByDateRange(LocalDate startDate, LocalDate endDate, Pageable page);

    List<PfNomineeDTO> findAllPfAccountByPin(String pin);
}
