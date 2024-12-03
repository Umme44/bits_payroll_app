package com.bits.hr.repository;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Nominee;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Nominee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NomineeRepository extends JpaRepository<Nominee, Long> {
    @Query(
        value = "select coalesce(sum(model.sharePercentage), 0) from Nominee model " +
        "where model.nomineeType = :nomineeType and model.employee.id = :employeeId"
    )
    Double getTotalUsedPercentage(NomineeType nomineeType, long employeeId);

    @Query(value = "select model from Nominee model " + "where model.nomineeType = :nomineeType and model.employee.id = :employeeId ")
    List<Nominee> getNomineesByEmployeeIdAndNomineeType(long employeeId, NomineeType nomineeType);

    @Query(
        value = "select model from Nominee model where model.employee=:employee and model.nomineeName=:nomineeName and model.nomineeType=:nomineeType and model.idNumber=:idNumber"
    )
    Optional<Nominee> findDuplicate(Employee employee, String nomineeName, NomineeType nomineeType, String idNumber);

    @Query(value = "select distinct(model.employee) from Nominee model")
    List<Employee> getAllEmployeeList();

    @Query(value = "select model from Nominee model where model.employee.id=:id")
    List<Nominee> getAllNomineesById(Long id);

    @Query(value = "select model from Nominee model where model.employee.id=:employeeId and model.nomineeType=:nomineeType")
    List<Nominee> getAllNomineesByIdAndNomineeType(Long employeeId, NomineeType nomineeType);

    @Query(value = "select count(model) from Nominee model where model.employee.id=:employeeId and model.nomineeType=:nomineeType")
    int totalNomineesByIdAndNomineeType(long employeeId, NomineeType nomineeType);

    @Query(
        value = "select model from Nominee model " +
        " where model.isNidVerified is null or model.isNidVerified = false " +
        " or model.isGuardianNidVerified is null or model.isGuardianNidVerified = false "
    )
    List<Nominee> getNomineesForNidVerification();

    @Query(value = "select model from Nominee model where model.imagePath = :fileName")
    List<Nominee> findByPhoto(String fileName);

    @Modifying
    @Query("delete from Nominee model where model.employee.id=:employeeId and model.nomineeType=:nomineeType")
    void deleteAllByEmployeeIdAndNomineeType(long employeeId, NomineeType nomineeType);

    @Query(value = "select model from Nominee model where model.nomineeType=:nomineeType and model.status=:status")
    List<Nominee> getAllByNomineeTypeAndStatus(NomineeType nomineeType, Status status);

    @Query(value = "select model from Nominee model where model.nomineeType=:nomineeType and model.status=:status")
    Page<Nominee> getAllNomineesByNomineeTypeAndStatus(NomineeType nomineeType, Status status, Pageable pageable);

    @Query(
        value = "select model from Nominee model where (:employeeId is null or model.employee.id=:employeeId) and model.nomineeType=:nomineeType and model.status=:status"
    )
    Page<Nominee> getAllApprovedOrPendingNominees(long employeeId, NomineeType nomineeType, Status status, Pageable pageable);

    @Query(
        value = "select model from Nominee model " +
        "               where " +
        "                                  (:searchText is null " +
        "                                  or (lower(model.employee.fullName) like lower(concat('%', :searchText, '%'))) " +
        "                                  or (model.employee.pin like (concat('%', :searchText, '%'))))" +
        "                   and model.nomineeType=:nomineeType " +
        "                   and model.status=:status"
    )
    Page<Nominee> findAllBySearchTextAndNomineeTypeAndStatus(String searchText, NomineeType nomineeType, Status status, Pageable pageable);

    @Query(value = "select model from Nominee model where model.employee.id = :employeeId ")
    List<Nominee> getNomineeByEmployeeId(long employeeId);

    @Query(value = "select model from Nominee model " + "where model.nomineeType = 'GRATUITY_FUND'")
    List<Nominee> getAllGfNominee();

    @Query(value = "select model from Nominee model " + "where model.nomineeType = 'GENERAL'")
    List<Nominee> getAllGeneralNominee();

    @Query(
        value = "select model " +
        "from Nominee model where model.nomineeType = 'GENERAL' " +
        "and (model.employee.dateOfJoining >= :startDate and model.employee.dateOfJoining <= :endDate)"
    )
    Page<Nominee> getAllGeneralNomineeByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(
        value = "select model " +
        "from Nominee model where model.nomineeType = 'GRATUITY_FUND'" +
        "and (model.employee.dateOfConfirmation >= :startDate and model.employee.dateOfJoining <= :endDate)"
    )
    Page<Nominee> getAllGFNomineeByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(
        value = "select count (model) from Nominee model " +
        "           where model.employee.id = :employeeId and model.nomineeType = :nomineeType and model.status = :status"
    )
    int findAllByEmployeeIdAndNomineeTypeAndStatus(long employeeId, NomineeType nomineeType, Status status);
}
