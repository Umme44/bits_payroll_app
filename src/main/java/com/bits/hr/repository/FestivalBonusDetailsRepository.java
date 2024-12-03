package com.bits.hr.repository;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FestivalBonusDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FestivalBonusDetailsRepository extends JpaRepository<FestivalBonusDetails, Long> {
    @Query(value = " select model from FestivalBonusDetails model where model.employee.id=:employeeId and model.festival.id=:festivalId ")
    List<FestivalBonusDetails> getDuplicates(long employeeId, long festivalId);

    @Query(
        value = "select model from FestivalBonusDetails model " +
        " where model.festival.id=:festivalId and model.employee.employeeCategory='CONTRACTUAL_EMPLOYEE' order by model.employee.pin "
    )
    List<FestivalBonusDetails> getByFestivalCE(long festivalId);

    @Query(
        value = "select model from FestivalBonusDetails model " +
        " where model.festival.id=:festivalId and model.employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' order by model.employee.pin "
    )
    List<FestivalBonusDetails> getByFestivalRCE(long festivalId);

    @Query(
        value = "select model from FestivalBonusDetails model " + " where model.festival.id=:festivalId group by model.employee.department"
    )
    List<FestivalBonusDetails> getByFestivalDepartmentWise(long festivalId);

    @Query(value = " select model from FestivalBonusDetails model where model.festival.id=:festivalId ")
    List<FestivalBonusDetails> getPreviousGeneration(long festivalId);

    @Query(
        value = "select model from FestivalBonusDetails model " +
        "where model.festival.id = :festivalId " +
        "and ( lower(model.employee.fullName) like :searchText " +
        "or lower(model.employee.pin) like :searchText) " +
        "order by model.employee.pin"
    )
    List<FestivalBonusDetails> getByFestivalId(long festivalId, String searchText);

    @Query(value = "select model from FestivalBonusDetails model " + "where model.festival.id = :festivalId ")
    List<FestivalBonusDetails> findByFestivalId(long festivalId);

    @Query(
        value = "select model from FestivalBonusDetails model " + " where model.festival.id = :festivalId " + " order by model.employee.pin"
    )
    List<FestivalBonusDetails> findByFestivalIdOrderByEmployeePin(long festivalId);

    @Query(value = "select count(model) from FestivalBonusDetails model where model.festival.id = :festivalId")
    int getTotalBonus(long festivalId);

    @Query(
        value = " select model from FestivalBonusDetails model where " +
        " model.festival.isProRata<>true and " +
        " model.employee.id=:employeeId and " +
        " ( model.festival.bonusDisbursementDate>=:startDate and model.festival.bonusDisbursementDate<=:endDate ) "
    )
    List<FestivalBonusDetails> getNonProRataFbBonusByEmployeeIdBetweenTimeRange(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " select model from FestivalBonusDetails model where " +
        " model.festival.isProRata=true and " +
        " model.employee.id=:employeeId and " +
        " ( model.festival.bonusDisbursementDate>=:startDate and model.festival.bonusDisbursementDate<=:endDate ) "
    )
    List<FestivalBonusDetails> getProRataFbBonusByEmployeeIdBetweenTimeRange(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = " select model from FestivalBonusDetails model " +
        " where model.employee.id=:employeeId and " +
        " ( model.festival.bonusDisbursementDate>=:startDate and model.festival.bonusDisbursementDate<=:endDate ) "
    )
    List<FestivalBonusDetails> getFbBonusByEmployeeIdBetweenTimeRange(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        "select sum(bonuseDetails.bonusAmount) from FestivalBonusDetails bonuseDetails " +
        "where bonuseDetails.id = :festivalId " +
        "and bonuseDetails.employee.department = :department " +
        "and bonuseDetails.employee.employeeCategory = :employeeCategory"
    )
    Double getTotalBonusByDepartmentAndEmploymentWise(long festivalId, Department department, EmployeeCategory employeeCategory);

    @Query(
        "Select model from FestivalBonusDetails model " +
        "Where model.isHold = true " +
        "AND " +
        "(lower(model.employee.fullName) LIKE lower(CONCAT('%',:searchText,'%')) " +
        "OR model.employee.pin LIKE lower(CONCAT('%',:searchText,'%')))"
    )
    List<FestivalBonusDetails> getFestivalBonusOnHoldList(String searchText);

    @Query(
        value = "select model from FestivalBonusDetails model " +
        "where model.employee.id = :employeeId " +
        "  and model.festival.isProRata = false " +
        "  and model.festival.bonusDisbursementDate <= :disbursedDate"
    )
    List<FestivalBonusDetails> findDisbursedByEmployeeId(long employeeId, LocalDate disbursedDate);

    @Query(
        value = " select model from FestivalBonusDetails model where " +
        " model.festival.isProRata<>true and " +
        " model.isHold<>true and " +
        " model.employee.id=:employeeId and " +
        " ( model.festival.bonusDisbursementDate>=:startDate and model.festival.bonusDisbursementDate<=:endDate ) order by model.festival.bonusDisbursementDate"
    )
    List<FestivalBonusDetails> findAllByEmployeeIdBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query("select model from FestivalBonusDetails model where model.festival.bonusDisbursementDate >= :bonusDisbursementDate")
    List<Festival> getAllByBonusDisbursementDateAfterAndEqual(LocalDate bonusDisbursementDate);
}
