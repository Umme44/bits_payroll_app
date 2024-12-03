package com.bits.hr.repository;

import com.bits.hr.domain.SalaryDeduction;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data  repository for the SalaryDeduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaryDeductionRepository extends JpaRepository<SalaryDeduction, Long> {
    @Transactional
    @Modifying
    @Query(value = " delete from SalaryDeduction model " + " where model.deductionMonth =:month AND model.deductionYear =:year")
    void deleteAllByMonthAndYear(@Param("year") int year, @Param("month") int month);

    List<SalaryDeduction> findAllByDeductionYearAndDeductionMonth(int year, int month);

    @Query(
        value = " select model from SalaryDeduction model " +
        " where model.employee.id=:employeeId AND model.deductionYear=:year AND model.deductionMonth=:month "
    )
    List<SalaryDeduction> findAllByEmployeeIdAndYearAndMonth(
        @Param("employeeId") long employeeId,
        @Param("year") int year,
        @Param("month") int month
    );

    @Query(
        value = "select model from SalaryDeduction model " +
        " where model.deductionYear=:year and model.deductionMonth=:month AND " +
        "(lower(model.employee.fullName) like lower(concat('%',:searchText,'%')) OR " +
        "lower(model.employee.pin) like lower(concat('%',:searchText,'%'))) " +
        "order by model.employee.pin "
    )
    Page<SalaryDeduction> findPageByYearAndMonth(int year, int month, Pageable pageable, String searchText);

    @Query(
        value = "select model from SalaryDeduction model " +
        "where " +
        "(" +
        " lower(model.employee.fullName) like lower(concat('%',:searchText,'%')) OR " +
        "lower(model.employee.pin) like lower(concat('%',:searchText,'%'))" +
        ") AND " +
        "(:month = 0 OR model.deductionMonth=:month) AND " +
        "(:year = 0 OR model.deductionYear=:year)"
    )
    Page<SalaryDeduction> findAll(Pageable pageable, String searchText, int month, int year);
}
