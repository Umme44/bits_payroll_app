package com.bits.hr.service;

import com.bits.hr.service.dto.BankBranchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.BankBranch}.
 */
public interface BankBranchService {
    /**
     * Save a bankBranch.
     *
     * @param bankBranchDTO the entity to save.
     * @return the persisted entity.
     */
    BankBranchDTO save(BankBranchDTO bankBranchDTO);

    /**
     * Get all the bankBranches.
     *
     * @return the list of entities.
     */
    List<BankBranchDTO> findAll();


    /**
     * Get a page of the bankBranches.
     *
     * @return the list of entities.
     */
    Page<BankBranchDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bankBranch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankBranchDTO> findOne(Long id);

    /**
     * Delete the "id" bankBranch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
