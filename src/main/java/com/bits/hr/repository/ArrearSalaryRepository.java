package com.bits.hr.repository;

import com.bits.hr.domain.ArrearSalary;
import com.bits.hr.domain.enumeration.Month;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArrearSalary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArrearSalaryRepository extends JpaRepository<ArrearSalary, Long> {
    @Query("select arrSal from ArrearSalary arrSal where arrSal.employee.id=:employeeId AND arrSal.month =:month AND arrSal.year =:year")
    List<ArrearSalary> findByEmployeeIdAndYearAndMonth(Long employeeId, int year, Month month);

    @Query(
        "select model from ArrearSalary model " +
        "where " +
        "( " +
        "(lower(model.employee.fullName) like lower(concat('%',:searchText,'%'))) OR " +
        "(lower(model.employee.pin) like lower(concat('%',:searchText,'%')))" +
        ") AND " +
        "(:month is null OR model.month=:month) AND " +
        "(:year = 0 OR model.year=:year)"
    )
    Page<ArrearSalary> findAll(
        @Param("searchText") String searchText,
        @Param("month") Month month,
        @Param("year") int year,
        Pageable pageable
    );
}
