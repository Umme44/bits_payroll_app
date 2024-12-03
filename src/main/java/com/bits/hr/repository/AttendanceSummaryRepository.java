package com.bits.hr.repository;

import com.bits.hr.domain.AttendanceSummary;
import com.bits.hr.domain.Employee;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data  repository for the AttendanceSummary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttendanceSummaryRepository extends JpaRepository<AttendanceSummary, Long> {
    @Query(
        "select model from AttendanceSummary model inner join model.employee employee where model.year=:year AND model.month=:month AND employee.pin=:pin"
    )
    Optional<AttendanceSummary> findByYearAndMonthAndAndEmployeePin(
        @Param("year") int year,
        @Param("month") int month,
        @Param("pin") String pin
    );

    @Query("select model from AttendanceSummary model where model.year=:year AND model.month=:month AND model.employee.id=:employeeId")
    List<AttendanceSummary> findByYearAndMonthAndAndEmployeeId(
        @Param("employeeId") long employeeId,
        @Param("year") int year,
        @Param("month") int month
    );

    @Query("select model from AttendanceSummary model inner join model.employee employee where model.year=:year AND model.month=:month")
    List<AttendanceSummary> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query(
        "select model from AttendanceSummary model inner join model.employee employee " +
        "where model.year=:year AND " +
        "model.month=:month AND " +
        "(lower(model.employee.fullName) like lower(concat('%', :searchText,'%')) OR " +
        "lower(model.employee.pin) like lower(concat('%', :searchText,'%'))) order by model.employee.pin"
    )
    Page<AttendanceSummary> findPageByYearAndMonth(
        @Param("year") int year,
        @Param("month") int month,
        Pageable pageable,
        String searchText
    );

    @Query(
        "select model.employee from AttendanceSummary model inner join model.employee employee where model.year=:year AND model.month=:month"
    )
    Set<Employee> findEmployeeSetByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Transactional
    @Modifying
    @Query("delete from AttendanceSummary model where model.month =:month AND model.year =:year")
    void deleteAllByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query(
        "select model from AttendanceSummary model inner join model.employee employee where model.employee.pin=:searchText OR lower(employee.fullName)  LIKE lower(:searchTextWild) "
    )
    List<AttendanceSummary> findByEmployeePinOrName(@Param("searchText") String searchText, @Param("searchTextWild") String searchTextWild);

    @Query(
        "select model from AttendanceSummary model " +
        "where " +
        "( " +
        " lower(model.employee.pin) like lower(concat('%', :searchText, '%')) OR " +
        "lower(model.employee.fullName) like lower(concat('%', :searchText, '%')) " +
        ") AND " +
        "(:month = 0 OR model.month=:month) AND " +
        "(:year = 0 OR model.year=:year)"
    )
    Page<AttendanceSummary> findAll(Pageable pageable, String searchText, int month, int year);

    @Query(
        value = "select model from AttendanceSummary model " +
        "where model.employee.pin=:pin AND " +
        "model.month=:month AND model.year=:year"
    )
    Optional<AttendanceSummary> findByPinYearAndMonth(String pin, int month, int year);
}
