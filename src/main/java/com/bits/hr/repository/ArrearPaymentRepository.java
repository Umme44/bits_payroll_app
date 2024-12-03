package com.bits.hr.repository;

import com.bits.hr.domain.ArrearPayment;
import com.bits.hr.domain.enumeration.Month;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArrearPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArrearPaymentRepository extends JpaRepository<ArrearPayment, Long> {
    @Query(
        value = "select model from ArrearPayment model " +
        " where model.deductTaxUponPayment=false and " +
        " model.arrearSalaryItem.employee.id=:employeeId and " +
        " model.arrearSalaryItem.isFestivalBonus = false and " +
        " model.disbursementDate >= :startDate and " +
        " model.disbursementDate <= :endDate"
    )
    List<ArrearPayment> getSalaryArrears(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "select model from ArrearPayment model " +
        " where model.arrearSalaryItem.employee.id=:employeeId and " +
        " model.arrearSalaryItem.isFestivalBonus = false and " +
        " model.salaryMonth = :month and " +
        " model.salaryYear <= :year "
    )
    List<ArrearPayment> getArrearSalaryDisbursableWithSalary(long employeeId, int year, Month month);

    @Query(
        value = "select model from ArrearPayment model " +
        "where model.arrearSalaryItem.id = :arrearSalaryItemId " +
        "AND model.isDeleted = false"
    )
    Page<ArrearPayment> findAllByArrearItem(Pageable pageable, long arrearSalaryItemId);
}
