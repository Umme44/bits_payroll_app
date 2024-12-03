package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.IndividualArrearPayslipDTO;
import com.bits.hr.service.dto.IndividualArrearSalaryDTO;
import com.bits.hr.service.dto.IndividualArrearSalaryGroupDataDTO;
import com.bits.hr.service.selecteable.SelectableDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.IndividualArrearSalary}.
 */
public interface IndividualArrearSalaryService {
    /**
     * Save a individualArrearSalary.
     *
     * @param individualArrearSalaryDTO the entity to save.
     * @return the persisted entity.
     */
    IndividualArrearSalaryDTO save(IndividualArrearSalaryDTO individualArrearSalaryDTO);

    /**
     * Get all the individualArrearSalaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IndividualArrearSalaryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" individualArrearSalary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IndividualArrearSalaryDTO> findOne(Long id);

    /**
     * Delete the "id" individualArrearSalary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<IndividualArrearSalaryGroupDataDTO> getGroupTitles();

    Page<IndividualArrearSalaryDTO> getAllByTitle(Pageable pageable, String title);

    IndividualArrearPayslipDTO getByEmployeeAndTitle(Employee employee, String title);

    List<Integer> getListOfDisbursedArrearSalaryYears(Employee employee);

    public List<SelectableDTO> getListOfArrearSalaryTitleByEmployeeIdAndYear(Employee employee, Integer year);
}
