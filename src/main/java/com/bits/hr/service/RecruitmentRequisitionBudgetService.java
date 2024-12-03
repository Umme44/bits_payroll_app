package com.bits.hr.service;

import com.bits.hr.domain.RecruitmentRequisitionBudget;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.RecruitmentRequisitionBudget}.
 */
public interface RecruitmentRequisitionBudgetService {

    /**
     * Save a recruitmentRequisitionBudget.
     *
     * @param recruitmentRequisitionBudgetDTO the entity to save.
     * @return the persisted entity.
     */
    RecruitmentRequisitionBudgetDTO save(RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO);

    /**
     * Get all the recruitmentRequisitionBudgets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecruitmentRequisitionBudgetDTO> findAll(Pageable pageable);


    /**
     * Get the "id" recruitmentRequisitionBudget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecruitmentRequisitionBudgetDTO> findOne(Long id);

    /**
     * Delete the "id" recruitmentRequisitionBudget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    List<RecruitmentRequisitionBudgetDTO> findByEmployeeId(Long employeeId);
    Optional<RecruitmentRequisitionBudgetDTO> findByEmployeeAndYearAndDepartmentValues(Long employeeId, Long year, Long departmentId);
    Optional<RecruitmentRequisitionBudgetDTO> findByEmployeeAndYearAndDepartment(RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO);
}
