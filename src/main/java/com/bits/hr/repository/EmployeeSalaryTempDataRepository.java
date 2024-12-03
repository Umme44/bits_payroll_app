package com.bits.hr.repository;

import com.bits.hr.domain.EmployeeSalaryTempData;
import com.bits.hr.domain.enumeration.Month;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data  repository for the EmployeeSalaryTempData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeSalaryTempDataRepository extends JpaRepository<EmployeeSalaryTempData, Long> {
    @Query(
        "select model from EmployeeSalaryTempData model where model.employee.id=:employeeId and model.year =:year AND model.month =:month "
    )
    List<EmployeeSalaryTempData> findEmployeeSalaryByEmployeeAndYearAndMonth(long employeeId, int year, Month month);

    @Query(value = "SELECT MONTH,YEAR  FROM EMPLOYEE_SALARY_TEMP_DATA  group by MONTH,YEAR ", nativeQuery = true)
    List<Object[]> getListGroupByMonth();

    @Transactional
    @Modifying
    @Query("delete from EmployeeSalaryTempData es where es.month =:month AND es.year =:year")
    void deleteAllByYearAndMonth(@Param("year") int year, @Param("month") Month month);

    @Query("select model from EmployeeSalaryTempData model where model.year =:year AND model.month =:month ")
    Page<EmployeeSalaryTempData> findAllByYearMonth(int year, Month month, Pageable pageable);
}
