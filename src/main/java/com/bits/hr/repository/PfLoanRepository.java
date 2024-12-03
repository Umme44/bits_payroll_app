package com.bits.hr.repository;

import com.bits.hr.domain.PfLoan;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PfLoan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PfLoanRepository extends JpaRepository<PfLoan, Long> {
    @Query(
        value = " select model from PfLoan model " +
        " where model.disbursementAmount=:disbursementAmount " +
        "     and model.disbursementDate=:disbursementDate " +
        "     and model.pfAccount.pin=:pin " +
        "     and model.pfAccount.pfCode=:pfCode"
    )
    Optional<PfLoan> findDuplicatePfLoan(double disbursementAmount, LocalDate disbursementDate, String pin, String pfCode);

    @Query(
        value = " select model from PfLoan model " +
        " where model.instalmentStartFrom<=:firstDayOfMonth " +
        "     and model.pfAccount.pin=:pin " +
        "     and model.status='OPEN_REPAYING' "
    )
    List<PfLoan> findPfLoanByPinInstallmentYearAndMonth(String pin, LocalDate firstDayOfMonth);

    @Query(value = "select model from PfLoan model " + "where model.pfAccount.pin=:pin" + "        and model.status=:pfLoanStatus")
    List<PfLoan> findAllPfLoanByPinAndStatus(String pin, PfLoanStatus pfLoanStatus);
}
