package com.bits.hr.repository;

import com.bits.hr.domain.IndividualArrearSalary;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the IndividualArrearSalary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndividualArrearSalaryRepository extends JpaRepository<IndividualArrearSalary, Long> {
    @Query(
        value = "select model from IndividualArrearSalary model " +
        " where model.employee.id=:employeeId and " +
        "       lower(model.title) = lower(:title) "
    )
    List<IndividualArrearSalary> getByEmployeeAndTitle(long employeeId, String title);

    @Query(
        value = " select model from IndividualArrearSalary model " + " where lower(model.title) = lower(:title) order by model.employee.pin"
    )
    List<IndividualArrearSalary> getAllByTitle(String title);

    @Query(
        value = "SELECT " +
        "model.TITLE, " +
        "model.effective_date, " +
        "model.title_effective_from " +
        "FROM INDIVIDUAL_ARREAR_SALARY as model " +
        "group by TITLE, model.effective_date, model.title_effective_from  " +
        "order by model.effective_date desc",
        nativeQuery = true
    )
    List<Object[]> getListGroupByTitle();

    @Query(
        value = " select model from IndividualArrearSalary model " + " where lower(model.title) = lower(:title) order by model.employee.pin"
    )
    Page<IndividualArrearSalary> getAllByTitle(Pageable pageable, String title);

    @Query(
        value = "select model from IndividualArrearSalary model " +
        " where model.employee.id=:employeeId " +
        " and model.effectiveDate>=:startDate " +
        " and model.effectiveDate<=:endDate "
    )
    List<IndividualArrearSalary> getAllByEmployeeIdAndDateRange(long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(
        value = "select model from IndividualArrearSalary model " +
        " where model.employee.pin=:pin " +
        " and model.arrearPfDeduction is not null" +
        " and model.effectiveDate>=:startDate " +
        " and model.effectiveDate<=:endDate "
    )
    List<IndividualArrearSalary> getAllArrearSalaryByPinAndDateRange(String pin, LocalDate startDate, LocalDate endDate);

    @Query(value = "select coalesce(sum(model.pfContribution), 0) from IndividualArrearSalary model")
    Double getPfContributionByEmployeeId(long employeeId);

    @Query(
        value = "SELECT DISTINCT FUNCTION('YEAR', i.effectiveDate) " + "FROM IndividualArrearSalary i " + "WHERE i.employee.id=:employeeId "
    )
    List<Integer> getListOfDisbursedSalaryYears(long employeeId);

    @Query(
        value = "SELECT DISTINCT i.title " +
        "FROM IndividualArrearSalary i " +
        "WHERE i.employee.id=:employeeId and FUNCTION('YEAR', i.effectiveDate)=:year "
    )
    List<String> getListOfArrearSalaryTitleByEmployeeIdAndYear(long employeeId, Integer year);

    @Query(
        value = "select model from IndividualArrearSalary model " +
        " where model.employee.id=:employeeId and " +
        "       lower(model.title) = lower(:title) "
    )
    Optional<IndividualArrearSalary> getByEmployeeIdAndTitle(long employeeId, String title);
}
