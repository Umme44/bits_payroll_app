package com.bits.hr.repository;

import com.bits.hr.domain.MobileBill;
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
 * Spring Data  repository for the MobileBill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MobileBillRepository extends JpaRepository<MobileBill, Long> {
    @Query(
        "select model from MobileBill model inner join model.employee employee where model.year=:year AND model.month=:month AND employee.pin=:pin"
    )
    List<MobileBill> findByYearAndMonthAndAndEmployeePin(@Param("year") int year, @Param("month") int month, @Param("pin") String pin);

    @Query("select model from MobileBill model where model.year=:year AND model.month=:month AND model.employee.id=:employeeId")
    List<MobileBill> findByYearAndMonthAndAndEmployeeId(
        @Param("employeeId") long employeeId,
        @Param("year") int year,
        @Param("month") int month
    );

    @Query(
        "select model from MobileBill model inner join model.employee employee " +
        "where model.year=:year AND model.month=:month AND " +
        "(lower(model.employee.fullName) like lower(concat('%',:searchText,'%')) OR " +
        "lower(model.employee.pin) like lower(concat('%',:searchText, '%')))"
    )
    Page<MobileBill> findByYearAndMonth(@Param("year") int year, @Param("month") int month, Pageable pageable, String searchText);

    @Transactional
    @Modifying
    @Query("delete from MobileBill model where model.month =:month AND model.year =:year")
    void deleteAllByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query(
        value = "select model from MobileBill model " +
        "where " +
        "( " +
        "lower(model.employee.fullName) like lower(concat('%',:searchText,'%')) OR lower(model.employee.pin) like lower(concat('%',:searchText,'%')) " +
        ") AND " +
        "(:month = 0 OR model.month=:month) AND " +
        "(:year = 0 OR model.year=:year)"
    )
    Page<MobileBill> findAll(Pageable pageable, String searchText, int month, int year);
}
