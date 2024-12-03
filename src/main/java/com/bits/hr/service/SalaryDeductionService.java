package com.bits.hr.service;

import com.bits.hr.service.dto.SalaryDeductionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.SalaryDeduction}.
 */
public interface SalaryDeductionService {
    /**
     * Save a salaryDeduction.
     *
     * @param salaryDeductionDTO the entity to save.
     * @return the persisted entity.
     */
    SalaryDeductionDTO save(SalaryDeductionDTO salaryDeductionDTO);

    /**
     * Get all the salaryDeductions.
     *
     * @param pageable the pagination information.
     * @param searchText
     * @return the list of entities.
     */
    Page<SalaryDeductionDTO> findAll(Pageable pageable, String searchText, int month, int year);

    /**
     * Get all the salaryDeductions by year and month.
     *
     * @param pageable the pagination information.
     * @param searchText
     * @return the list of entities.
     */
    Page<SalaryDeductionDTO> findAllByYearAndMonth(int year, int month, Pageable pageable, String searchText);

    /**
     * Get the "id" salaryDeduction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SalaryDeductionDTO> findOne(Long id);

    /**
     * Delete the "id" salaryDeduction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
