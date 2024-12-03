package com.bits.hr.service;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.service.dto.EmployeeSalaryTempDataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmployeeSalaryTempData}.
 */
public interface EmployeeSalaryTempDataService {
    /**
     * Save a employeeSalaryTempData.
     *
     * @param employeeSalaryTempDataDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeSalaryTempDataDTO save(EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO);

    /**
     * Get all the employeeSalaryTempData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeeSalaryTempDataDTO> findAll(Pageable pageable);

    /**
     * Get all the employeeSalaryTempData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeeSalaryTempDataDTO> findAllByYearMonth(Integer year, Month month, Pageable pageable);

    /**
     * Get the "id" employeeSalaryTempData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeSalaryTempDataDTO> findOne(Long id);

    /**
     * Delete the "id" employeeSalaryTempData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Delete the "id" employeeSalaryTempData.
     *
     * @param month,year the id of the entity.
     */
    void deleteByMonthYear(Month month, Integer year);
}
