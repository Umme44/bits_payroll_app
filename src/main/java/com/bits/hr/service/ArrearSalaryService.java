package com.bits.hr.service;

import com.bits.hr.service.dto.ArrearSalaryDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ArrearSalary}.
 */
public interface ArrearSalaryService {
    /**
     * Save a arrearSalary.
     *
     * @param arrearSalaryDTO the entity to save.
     * @return the persisted entity.
     */
    ArrearSalaryDTO save(ArrearSalaryDTO arrearSalaryDTO);

    /**
     * Get all the arrearSalaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArrearSalaryDTO> findAll(String searchText, int month, int year, Pageable pageable);

    /**
     * Get the "id" arrearSalary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArrearSalaryDTO> findOne(Long id);

    /**
     * Delete the "id" arrearSalary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
