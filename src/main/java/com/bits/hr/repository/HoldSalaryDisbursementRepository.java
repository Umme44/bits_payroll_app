package com.bits.hr.repository;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.HoldSalaryDisbursement;
import com.bits.hr.domain.enumeration.Month;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HoldSalaryDisbursement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HoldSalaryDisbursementRepository extends JpaRepository<HoldSalaryDisbursement, Long> {
    @Query(
        "select holdSalaryDisbursement from HoldSalaryDisbursement holdSalaryDisbursement where holdSalaryDisbursement.user.login = ?#{principal.username}"
    )
    List<HoldSalaryDisbursement> findByUserIsCurrentUser();

    @Query(
        "select model from HoldSalaryDisbursement model " +
        "where lower(model.employeeSalary.employee.fullName) like :searchText OR " +
        "model.employeeSalary.employee.pin like :searchText"
    )
    Page<HoldSalaryDisbursement> findAll(String searchText, Pageable pageable);

    @Query(
        value = "select model from HoldSalaryDisbursement model where model.employeeSalary.year =:year and model.employeeSalary.month = :month"
    )
    List<HoldSalaryDisbursement> getAllByYearAndMonth(int year, Month month);

    @Query(value = "select model from HoldSalaryDisbursement model where model.employeeSalary = :employeeSalary")
    Optional<HoldSalaryDisbursement> findByEmployeeSalary(EmployeeSalary employeeSalary);

    @Query(
        value = "select model from HoldSalaryDisbursement model " +
        "where model.employeeSalary.employee.id =:employeeId and " +
        "model.employeeSalary.year =:year and model.employeeSalary.month =:month"
    )
    Optional<HoldSalaryDisbursement> findByEmployeeIdYearAndMonth(long employeeId, int year, Month month);

    @Modifying
    @Query("delete from HoldSalaryDisbursement model where model.employeeSalary.id=:employeeSalaryId")
    void deleteBySalaryId(long employeeSalaryId);
}
