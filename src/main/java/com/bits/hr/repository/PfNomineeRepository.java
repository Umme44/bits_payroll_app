package com.bits.hr.repository;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfNominee;
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
 * Spring Data  repository for the PfNominee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PfNomineeRepository extends JpaRepository<PfNominee, Long> {
    @Query(value = "select coalesce(sum(model.sharePercentage), 0) from PfNominee model " + "where model.pfAccount.id=:pfAccountId")
    double sumConsumedSharePercentage(long pfAccountId);

    @Query(value = "select model from PfNominee model where model.pfAccount.id = :pfAccountId")
    List<PfNominee> findAllByPfAccountId(long pfAccountId);

    @Query(
        value = "select distinct(model.pfAccount) from PfNominee model " +
        "where lower(model.pfAccount.accHolderName) like :searchText " +
        "or model.pfAccount.pin like :searchText"
    )
    List<PfAccount> getPfAccountWithNominees(String searchText);

    @Query(value = "select model from PfNominee model where model.pfAccount.pin = :pin")
    Page<PfNominee> findAllByPfAccountPin(Pageable pageable, String pin);

    @Query(value = "select model from PfNominee model where model.pfAccount.pin = :pin")
    List<PfNominee> findAllByPfAccountPin(String pin);

    @Query(value = "select model from PfNominee model where model.id=:id and model.pfAccount.pin=:pin")
    Optional<PfNominee> findByIdAndPin(Long id, String pin);

    @Query(value = "SELECT sum(model.sharePercentage) FROM PfNominee model where model.pfAccount=:pfAccount")
    Double getTotalSharePercentageByPfAccount(PfAccount pfAccount);

    @Query(value = "select model from PfNominee model where model.isApproved = false OR model.isApproved is null")
    List<PfNominee> findIsNotApproved();

    @Query(value = "select model from PfNominee model where model.isApproved = true and model.isApproved is not null")
    List<PfNominee> findIsApproved();

    @Query(value = "select model from PfNominee model where model.pfAccount=:pfAccount and model.fullName=:fullName")
    Optional<PfNominee> findDuplicate(PfAccount pfAccount, String fullName);

    @Query(
        value = "select model from PfNominee model " +
        " where model.isNidVerified is null or model.isNidVerified = false " +
        " or model.isGuardianNidVerified is null or model.isGuardianNidVerified = false "
    )
    List<PfNominee> getNomineesForNidVerification();

    @Query(
        value = "select case when count(model)> 0 then true else false end from PfNominee model " +
        "where model.pfAccount.pin = :employeePin "
    )
    boolean isEmployeeEligibleForPF(Long employeePin);

    @Query(value = "select model from PfNominee model where model.photo = :photoName")
    List<PfNominee> findByPhoto(String photoName);

    @Modifying
    @Query("delete from PfNominee model where model.pfAccount.id=:pfAccountId")
    void deleteAllByPfAccountId(Long pfAccountId);

    @Query(
        value = "select model " +
        "from PfNominee model" +
        "         LEFT JOIN Employee as employee on model.pfAccount.pin = employee.pin " +
        "where (employee.dateOfConfirmation >= :startDate and employee.dateOfConfirmation <= :endDate)"
    )
    Page<PfNominee> getAllPFNomineeByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value = "select count(model) from  PfNominee model where model.pfAccount.pin=:employeePin")
    int totalNomineesByPin(String employeePin);

    @Query(value = "select count(model) from  PfNominee model where model.pfAccount.pin=:employeePin and model.isApproved=:isApproved")
    int findAllByEmployeePin(String employeePin, boolean isApproved);
}
