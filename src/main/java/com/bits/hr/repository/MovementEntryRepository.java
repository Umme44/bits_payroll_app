package com.bits.hr.repository;

import com.bits.hr.domain.MovementEntry;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MovementEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovementEntryRepository extends JpaRepository<MovementEntry, Long> {
    @Query("select movementEntry from MovementEntry movementEntry where movementEntry.createdBy.login = ?#{principal.username}")
    List<MovementEntry> findByCreatedByIsCurrentUser();

    @Query("select movementEntry from MovementEntry movementEntry where movementEntry.updatedBy.login = ?#{principal.username}")
    List<MovementEntry> findByUpdatedByIsCurrentUser();

    @Query("select movementEntry from MovementEntry movementEntry where movementEntry.sanctionBy.login = ?#{principal.username}")
    List<MovementEntry> findBySanctionByIsCurrentUser();

    @Query(
        value = " select movementEntry from MovementEntry movementEntry where movementEntry.employee.id=:employeeId order by movementEntry.startDate desc"
    )
    Page<MovementEntry> findAllByEmployee(long employeeId, Pageable pageable);

    @Query(
        value = "Select model " +
        " from MovementEntry model " +
        " where model.employee.pin=:pin and model.status='APPROVED' " +
        " and ( " +
        "       ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "       ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "       ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "       ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "       ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "   )   "
    )
    List<MovementEntry> getApprovedMovementEntriesByEmployeePinBetweenTwoDates(String pin, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "Select model " +
        " from MovementEntry model " +
        " where model.employee.id=:employeeId and model.status='APPROVED' " +
        " and ( " +
        "       ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "       ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "       ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "       ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "       ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "   )   "
    )
    List<MovementEntry> getApprovedMovementEntriesByEmployeeIdBetweenTwoDates(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "Select model " +
        " from MovementEntry model " +
        " where model.employee.id=:employeeId and model.status<>'NOT_APPROVED'" +
        " and ( " +
        "       ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "       ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "       ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "       ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "       ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "   )   "
    )
    List<MovementEntry> getMovementEntriesByEmployeeIdBetweenDates(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "Select model " +
        " from MovementEntry model " +
        " where model.employee.id=:employeeId " +
        " and ( " +
        "       ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "       ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "       ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "       ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "       ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "   )   "
    )
    List<MovementEntry> getALLMovementEntriesByEmployeeIdBetweenDates(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "Select model " +
        " from MovementEntry model " +
        " where model.employee.id=:employeeId and model.status='PENDING'" +
        " and ( " +
        "       ( :startDate  BETWEEN model.startDate and model.endDate ) or " +
        "       ( :endDate    BETWEEN model.startDate and model.endDate ) or " +
        "       ( model.startDate BETWEEN :startDate and :endDate ) or " +
        "       ( model.endDate   BETWEEN :startDate and :endDate ) or " +
        "       ( model.startDate  <= :startDate and model.endDate >=:endDate ) " +
        "   )   "
    )
    List<MovementEntry> findPendingMovementEntriesByEmployeeIdBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);

    //(lower(model.employee.fullName) like lower(concat('%',:searchText,'%')) OR  (lower(model.employee.pin) like lower(concat('%',:searchText,'%'))
    @Query(
        value = "select model from MovementEntry model " +
        "where ((:employeeId =-1L or model.employee.id=:employeeId))" +
        "AND (" +
        "( cast(:startDate as date) is null or  cast(:endDate as date) is null) or " +
        "( :startDate BETWEEN model.startDate and model.endDate ) or " +
        "( :endDate BETWEEN model.startDate and model.endDate ) or " +
        "( model.startDate BETWEEN :startDate and :endDate ) or " +
        "( model.endDate BETWEEN :startDate and :endDate ) or " +
        "( model.startDate <= :startDate and model.endDate >= :endDate))"
    )
    Page<MovementEntry> findAll(Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("select model from MovementEntry model where model.status = 'PENDING'")
    List<MovementEntry> getAllPending();

    @Query("select model from MovementEntry model where model.id IN :ids " + "AND model.status = 'PENDING'")
    List<MovementEntry> getAllPendingByIds(List<Long> ids);

    @Query("select model from MovementEntry model " + "where model.status = 'PENDING' AND model.employee.reportingTo.id=:lmId")
    List<MovementEntry> getAllPendingLM(Long lmId);

    @Query(
        "select count(model) from MovementEntry model " +
        "where model.status = 'PENDING' AND model.employee.reportingTo.id=:currentEmployeeId"
    )
    int getAllPendingMovementEntriesLM(Long currentEmployeeId);

    @Query("select count(model) from MovementEntry model " + "where model.status = 'PENDING'")
    int getAllPendingMovementEntriesHR();
}
