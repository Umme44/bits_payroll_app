package com.bits.hr.repository;

import com.bits.hr.domain.PfLoanRepayment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data  repository for the PfLoanRepayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PfLoanRepaymentRepository extends JpaRepository<PfLoanRepayment, Long> {
    // considering pin as pf code todo: change of logic might be needed upon pfcode requirement change
    @Query(
        "select model from PfLoanRepayment model where model.deductionYear=:year AND model.deductionMonth=:month AND model.pfLoan.pfAccount.pfCode=:pin"
    )
    Optional<PfLoanRepayment> findByYearAndMonthAndAndEmployeePin(
        @Param("year") int year,
        @Param("month") int month,
        @Param("pin") String pin
    );

    @Query("select model from PfLoanRepayment model where model.deductionYear=:year AND model.deductionMonth=:month")
    List<PfLoanRepayment> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Transactional
    @Modifying
    @Query("delete from PfLoanRepayment model where model.deductionMonth =:month AND model.deductionYear =:year")
    void deleteAllByDeductionMonthAndDeductionYear(@Param("year") int year, @Param("month") int month);

    List<PfLoanRepayment> findAllByDeductionYearAndDeductionMonth(int year, int month);

    @Query("select model from PfLoanRepayment model where model.pfLoan.id=:pfLoanId")
    List<PfLoanRepayment> findAllPfLoanRepaymentByPfLoanId(@Param("pfLoanId") Long pfLoanId);

    @Query(
        value = "select model from PfLoanRepayment model " +
        "             where model.deductionMonth=:month " +
        "               and model.deductionYear=:year"
    )
    Optional<PfLoanRepayment> findPfLoanRepaymentByYearAndMonth(int month, int year);
}
