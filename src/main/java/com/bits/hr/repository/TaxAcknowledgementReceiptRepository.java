package com.bits.hr.repository;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.TaxAcknowledgementReceipt;
import com.bits.hr.domain.enumeration.AcknowledgementStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TaxAcknowledgementReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxAcknowledgementReceiptRepository extends JpaRepository<TaxAcknowledgementReceipt, Long> {
    @Query(
        "select taxAcknowledgementReceipt from TaxAcknowledgementReceipt taxAcknowledgementReceipt where taxAcknowledgementReceipt.receivedBy.login = ?#{principal.username}"
    )
    List<TaxAcknowledgementReceipt> findByReceivedByIsCurrentUser();

    @Query(
        "select taxAcknowledgementReceipt from TaxAcknowledgementReceipt taxAcknowledgementReceipt where taxAcknowledgementReceipt.createdBy.login = ?#{principal.username}"
    )
    List<TaxAcknowledgementReceipt> findByCreatedByIsCurrentUser();

    @Query(
        "select taxAcknowledgementReceipt from TaxAcknowledgementReceipt taxAcknowledgementReceipt where taxAcknowledgementReceipt.updatedBy.login = ?#{principal.username}"
    )
    List<TaxAcknowledgementReceipt> findByUpdatedByIsCurrentUser();

    @Query(value = "select model from TaxAcknowledgementReceipt model " + "               where model.employee.id = :employeeId")
    Page<TaxAcknowledgementReceipt> findAllByEmployeeId(Pageable pageable, long employeeId);

    @Query(
        value = "select distinct(model.employee) from TaxAcknowledgementReceipt model " +
        "               where model.fiscalYear.id = :aitConfigId "
    )
    List<Employee> findEmployeeListByAitConfigId(long aitConfigId);

    @Query(
        value = "select model from TaxAcknowledgementReceipt model " +
        "               where (:aitConfigId is null or model.fiscalYear.id = :aitConfigId) " +
        "                   and (:employeeId is null or model.employee.id = :employeeId)" +
        "                   and model.acknowledgementStatus = :acknowledgementStatus " +
        "               order by model.employee.pin, model.fiscalYear.startDate"
    )
    Page<TaxAcknowledgementReceipt> findPageByFiscalYearIdAndStatus(
        Long aitConfigId,
        Long employeeId,
        AcknowledgementStatus acknowledgementStatus,
        Pageable pageable
    );

    @Query("select model from TaxAcknowledgementReceipt model " + "       where model.id = :id and model.employee.id = :employeeId")
    Optional<TaxAcknowledgementReceipt> findByIdAndEmployeeId(long id, long employeeId);

    @Query(
        "select model from TaxAcknowledgementReceipt model " +
        "where model.employee.id=:employeeId " +
        "   and model.fiscalYear.id=:fiscalYearId"
    )
    List<TaxAcknowledgementReceipt> findByEmployeeIdAndFiscalYearId(Long employeeId, Long fiscalYearId);

    @Query("select count(model) from TaxAcknowledgementReceipt model where model.acknowledgementStatus='SUBMITTED'")
    Integer getTotalPendingTaxAcknowledgementReceipt();

    @Query(
        "select model from TaxAcknowledgementReceipt model " + "where model.fiscalYear.id=:fiscalYearId " + "order by model.employee.pin"
    )
    List<TaxAcknowledgementReceipt> findByFiscalYearId(Long fiscalYearId);
}
