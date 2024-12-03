package com.bits.hr.repository;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.RecruitmentRequisitionBudget;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the RecruitmentRequisitionBudget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentRequisitionBudgetRepository extends JpaRepository<RecruitmentRequisitionBudget, Long> {

    List<RecruitmentRequisitionBudget> findByEmployeeId(Long employeeId);

    Optional<RecruitmentRequisitionBudget> findByEmployeeAndYearAndDepartment(Employee employee, Long year, Department department);
}
