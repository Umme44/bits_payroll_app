package com.bits.hr.service;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.service.dto.PfAccountDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfAccount}.
 */
public interface PfAccountService {
    /**
     * Save a pfAccount.
     *
     * @param pfAccountDTO the entity to save.
     * @return the persisted entity.
     */
    PfAccountDTO create(PfAccountDTO pfAccountDTO);

    PfAccountDTO update(PfAccountDTO pfAccountDTO);

    /**
     * Get all the pfAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfAccountDTO> findAll(String pin, Pageable pageable);

    /**
     * Get all the pfAccounts.
     *
     * @return the list of dto.
     */
    List<PfAccountDTO> findAll();

    /**
     * Get the "id" pfAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PfAccountDTO> findOne(Long id);

    /**
     * Delete the "id" pfAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    PfAccount getPfAccountOfCurrentUser();

    long getPfAccountIdOfCurrentUser();
}
