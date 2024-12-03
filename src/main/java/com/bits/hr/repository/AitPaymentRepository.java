package com.bits.hr.repository;

import com.bits.hr.domain.AitPayment;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AitPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AitPaymentRepository extends JpaRepository<AitPayment, Long> {
    @Query(
        value = "select model from AitPayment  model " +
        "where model.date >=:startDate and model.date <=:endDate and model.employee.id=:employeeId"
    )
    List<AitPayment> findAllBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        "select model from AitPayment model " +
        "where ( " +
        "(lower(model.employee.fullName) like lower(concat('%',:searchText,'%'))) OR " +
        "(lower(model.employee.pin) like lower(concat('%',:searchText,'%')))) AND " +
        "(( cast(:startDate as date) is null OR " +
        "cast(:endDate as date) is null) OR " +
        "(model.date between cast(:startDate as date) and cast(:endDate as date)))"
    )
    Page<AitPayment> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
