package com.bits.hr.repository;

import com.bits.hr.domain.EmployeePin;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmployeePin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeePinRepository extends JpaRepository<EmployeePin, Long> {
    @Query("select employeePin from EmployeePin employeePin where employeePin.createdBy.login = ?#{principal.username}")
    List<EmployeePin> findByCreatedByIsCurrentUser();

    @Query("select employeePin from EmployeePin employeePin where employeePin.updatedBy.login = ?#{principal.username}")
    List<EmployeePin> findByUpdatedByIsCurrentUser();

    @Query("select model from EmployeePin model " + "where model.pin =:pin")
    Optional<EmployeePin> findByPin(String pin);

    @Query(
        "select model from EmployeePin model " +
        "where " +
        "   (" +
        "       model.pin like concat('%',:searchText,'%') " +
        "       or lower(model.fullName) like concat('%',lower(:searchText) ,'%')" +
        "   ) " +
        "and ( :selectedCategory is null or model.employeeCategory =:selectedCategory ) " +
        "and ( :pinStatus is null or model.employeePinStatus = :pinStatus ) " +
        "order by model.createdAt desc "
    )
    Page<EmployeePin> findEmployeePinsUsingFilter(
        String searchText,
        EmployeeCategory selectedCategory,
        EmployeePinStatus pinStatus,
        Pageable pageable
    );

    @Query(
        "select model from EmployeePin model " +
        "where CAST(model.pin AS long ) >= CAST(:sequenceStart AS long ) " +
        "and CAST(model.pin AS long ) <= CAST(:sequenceEnd AS long ) " +
        "order by model.pin DESC "
    )
    Page<EmployeePin> getLastPinFromTheSequence(String sequenceStart, String sequenceEnd, Pageable pageable);

    @Query(
        "select model from EmployeePin model " +
        "where CAST(model.pin AS long ) >= CAST(:sequenceStart AS long ) " +
        "and CAST(model.pin AS long ) <= CAST(:sequenceEnd AS long ) " +
        "order by model.createdAt DESC "
    )
    Page<EmployeePin> getLastCreatedPinFromTheSequence(String sequenceStart, String sequenceEnd, Pageable pageable);

    @Query("select case when count(model)>0 then false else true end from EmployeePin model " + "where model.pin=:pin")
    boolean isEmployeePinUnique(String pin);

    @Query("select model from EmployeePin model where model.pin=:pin")
    Optional<EmployeePin> findOneByPin(String pin);

    @Query(
        "select model from EmployeePin model " +
        "where model.employeePinStatus = 'JOINED' " +
        "and model.pin in (" +
        "   select er.employee.pin from EmployeeResignation er " +
        "   where er.lastWorkingDay < :today and er.approvalStatus='APPROVED'" +
        "   )" +
        ""
    )
    List<EmployeePin> getEmployeePinsOfNewlyResignedEmployees(LocalDate today);

    @Query(
        value = "select model from EmployeePin model " + "where cast(model.pin as long) < cast(:pin as long) " + "order by model.pin desc "
    )
    List<EmployeePin> getThePreviousPin(String pin, Pageable pageable);

    @Query(value = "select case when count(model) > 0 then true else false end " +
        "from EmployeePin model " +
        "where model.recruitmentRequisitionForm.rrfNumber = :rrfNumber")
    boolean isEmployeePinExistByRrfNumber(String rrfNumber);
}
