package com.bits.hr.repository;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.enumeration.Month;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Data  repository for the EmployeeSalary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeSalaryRepository extends JpaRepository<EmployeeSalary, Long> {
    @Transactional
    @Modifying
    @Query("delete from EmployeeSalary es where es.month =:month AND es.year =:year")
    void deleteAllByYearAndMonth(@Param("year") int year, @Param("month") Month month);

    @Query("select es from EmployeeSalary es where es.month =:month AND es.year =:year order by es.employee.pin")
    List<EmployeeSalary> findEmployeeSalaryByYearAndMonth(@Param("year") int year, @Param("month") Month month);

    @Query(
        "select es from EmployeeSalary es inner join es.employee emp inner join emp.designation des" +
        " where es.month =:month AND es.year =:year " +
        " AND es.employeeCategory='CONTRACTUAL_EMPLOYEE' " +
        " AND upper(des.designationName) <>'INTERN' " +
        " order by es.employee.pin"
    )
    List<EmployeeSalary> getContractualSalary(@Param("year") int year, @Param("month") Month month);

    @Query(
        "select es from EmployeeSalary es inner join es.employee emp inner join emp.designation des" +
        " where es.month =:month AND es.year =:year " +
        " AND ( es.employeeCategory = 'CONTRACTUAL_EMPLOYEE' or es.employeeCategory = 'INTERN' )" +
        " AND upper(des.designationName) like '%INTERN%' " +
        " order by es.employee.pin "
    )
    List<EmployeeSalary> getInternSalary(@Param("year") int year, @Param("month") Month month);

    @Query(
        "select es from EmployeeSalary es " +
        " where es.month =:month AND es.year =:year " +
        " AND (es.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' or es.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') " +
        " order by es.employee.pin"
    )
    List<EmployeeSalary> getRegularSalary(@Param("year") int year, @Param("month") Month month);

    @Query(
        "select es from EmployeeSalary es " +
        "where es.month =:month AND es.year =:year AND " +
        "(lower(es.employee.fullName) like lower(concat('%', :searchText, '%')) OR  " +
        "lower(es.employee.pin) like lower(concat('%', :searchText, '%'))) " +
        " order by es.employee.pin "
    )
    Page<EmployeeSalary> findEmployeeSalaryByYearAndMonth(
        @Param("year") int year,
        @Param("month") Month month,
        String searchText,
        Pageable pageable
    );

    // todo: remove
    List<EmployeeSalary> findAllByPinAndYearAndMonth(String pin, int year, Month month);

    @Query("select es from EmployeeSalary es where es.employee.id=:employeeId AND es.month =:month AND es.year =:year")
    List<EmployeeSalary> findByEmployeeIdAndYearAndMonth(Long employeeId, int year, Month month);

    @Query(
        value = "select es from EmployeeSalary es " +
        " WHERE es.employee.id=:employeeId AND es.salaryGenerationDate>=:startDate AND es.salaryGenerationDate<=:endDate"
    )
    List<EmployeeSalary> findAllByEmployeeBetweenGenerationDates(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(value = "select es from EmployeeSalary es WHERE es.pin=:pin")
    List<EmployeeSalary> getAllByEmployeePin(String pin);

    // caution: jpa does not take empty list
    @Query(
        value = "select es from EmployeeSalary es where es in " +
        "(select model from EmployeeSalary model where model.employee.id =:employeeId and model.year=:incomeYearStartYear and model.month IN :incomeYearStartMonthList)" +
        "or es in " +
        "(select model from EmployeeSalary model where model.employee.id =:employeeId and model.year=:incomeYearEndYear and model.month IN :incomeYearEndMonthList)"
    )
    List<EmployeeSalary> getIncomeYearSpecificSalary(
        Long employeeId,
        int incomeYearStartYear,
        int incomeYearEndYear,
        List<Month> incomeYearStartMonthList,
        List<Month> incomeYearEndMonthList
    );

    // caution: jpa does not take empty list
    @Query(
        value = "select es from EmployeeSalary es " +
        " where es.employee.id =:employeeId and es.year=:year " +
        " and es.month IN :monthList "
    )
    Set<EmployeeSalary> getEmployeeSalaryByYearAndListOfMonth(Long employeeId, int year, List<Month> monthList);

    // caution: jpa does not take empty list
    @Query(
        value = "select es from EmployeeSalary es " +
        " where es.employee.id =:employeeId and es.year=:year and es.isHold <> true " +
        " and es.month IN :monthList "
    )
    Set<EmployeeSalary> getNotHoldEmployeeSalaryByYearAndListOfMonth(Long employeeId, int year, List<Month> monthList);

    @Query(
        value = "SELECT DEPARTMENT                                         AS DEPARTMENT,  " +
        "       COALESCE(SUM(HEAD_COUNT), 0)                       AS HEAD_COUNT,  " +
        "       COALESCE(SUM(BASIC), 0.0)                          AS BASIC,  " +
        "       COALESCE(SUM(HOUSE_RENT), 0.0)                     AS HOUSE_RENT,  " +
        "       COALESCE(SUM(MEDICAL), 0.0)                        AS MEDICAL,  " +
        "       COALESCE(SUM(CONVEYANCE), 0.0)                     AS CONVEYANCE,  " +
        "       COALESCE(SUM(ARREAR), 0.0)                         AS ARREAR,  " +
        "       COALESCE(SUM(GROSS), 0.0)                          AS GROSS,  " +
        "       COALESCE(SUM(CONTRACTUAL_SALARY), 0.0)             AS CONTRACTUAL_SALARY,  " +
        "       COALESCE(SUM(INTERN_SALARY), 0.0)                  AS INTERN_SALARY,  " +
        "       COALESCE(SUM(PF_DEDUCTION), 0.0)                   AS PF_DEDUCTION,  " +
        "       COALESCE(SUM(TAX_DEDUCTION), 0.0)                  AS TAX_DEDUCTION,  " +
        "       COALESCE(SUM(WELFARE_FUND_DEDUCTION), 0.0)         AS WELFARE_FUND_DEDUCTION,  " +
        "       COALESCE(SUM(MOBILE_BILL_DEDUCTION), 0.0)          AS MOBILE_BILL_DEDUCTION,  " +
        "       COALESCE(SUM(OTHER_DEDUCTION), 0.0)                AS OTHER_DEDUCTION,  " +
        "       COALESCE(SUM(TOTAL_DEDUCTION), 0.0)                AS TOTAL_DEDUCTION,  " +
        "       COALESCE(SUM(NET_PAY), 0.0)                        AS NET_PAY,  " +
        "       COALESCE(SUM(PF_CONTRIBUTION), 0.0)                AS PF_CONTRIBUTION,  " +
        "       COALESCE(SUM(GF_CONTRIBUTION), 0.0)                AS GF_CONTRIBUTION,  " +
        "       COALESCE(SUM(PROVISION_FOR_FESTIVAL_BONUS), 0.0)   AS PROVISION_FOR_FESTIVAL_BONUS,  " +
        "       COALESCE(SUM(PROVISION_FOR_LEAVE_ENCASHMENT), 0.0) AS PROVISION_FOR_LEAVE_ENCASHMENT  " +
        "FROM (  " +
        "         SELECT DEPARTMENT                              AS DEPARTMENT,  " +
        "                count(id)                               AS HEAD_COUNT,  " +
        "                SUM(PAYABLE_GROSS_BASIC_SALARY)         AS BASIC,  " +
        "                SUM(PAYABLE_GROSS_HOUSE_RENT)           AS HOUSE_RENT,  " +
        "                SUM(PAYABLE_GROSS_MEDICAL_ALLOWANCE)    AS MEDICAL,  " +
        "                SUM(PAYABLE_GROSS_CONVEYANCE_ALLOWANCE) AS CONVEYANCE,  " +
        "                SUM(ARREAR_SALARY)                      AS ARREAR,  " +
        "                SUM(PAYABLE_GROSS_SALARY)               AS GROSS,  " +
        "                0                                       AS CONTRACTUAL_SALARY,  " +
        "                0                                       AS INTERN_SALARY,  " +
        "                SUM(PF_DEDUCTION)                       AS PF_DEDUCTION,  " +
        "                SUM(TAX_DEDUCTION)                      AS TAX_DEDUCTION,  " +
        "                SUM(WELFARE_FUND_DEDUCTION)             AS WELFARE_FUND_DEDUCTION,  " +
        "                SUM(MOBILE_BILL_DEDUCTION)              AS MOBILE_BILL_DEDUCTION,  " +
        "                SUM(OTHER_DEDUCTION)                    AS OTHER_DEDUCTION,  " +
        "                SUM(TOTAL_DEDUCTION)                    AS TOTAL_DEDUCTION,  " +
        "                SUM(NET_PAY)                            AS NET_PAY,  " +
        "                SUM(PF_CONTRIBUTION)                    AS PF_CONTRIBUTION,  " +
        "                SUM(GF_CONTRIBUTION)                    AS GF_CONTRIBUTION,  " +
        "                SUM(PROVISION_FOR_FESTIVAL_BONUS)       AS PROVISION_FOR_FESTIVAL_BONUS,  " +
        "                SUM(PROVISION_FOR_LEAVE_ENCASHMENT)     AS PROVISION_FOR_LEAVE_ENCASHMENT  " +
        "         FROM EMPLOYEE_SALARY  " +
        "         where MONTH = :month  " +
        "           and YEAR = :year  " +
        "           and (UPPER(EMPLOYEE_CATEGORY) =  " +
        "                'REGULAR_CONFIRMED_EMPLOYEE'  " +
        "             OR EMPLOYEE_CATEGORY =  " +
        "                'REGULAR_PROVISIONAL_EMPLOYEE')  " +
        "         group by DEPARTMENT  " +
        "         UNION  " +
        "         SELECT DEPARTMENT                          AS DEPARTMENT,  " +
        "                count(id)                           AS HEAD_COUNT,  " +
        "                0                                   AS BASIC,  " +
        "                0                                   AS HOUSE_RENT,  " +
        "                0                                   AS MEDICAL,  " +
        "                0                                   AS CONVEYANCE,  " +
        "                SUM(ARREAR_SALARY)                  AS ARREAR,  " +
        "                0                                   AS GROSS,  " +
        "                SUM(PAYABLE_GROSS_SALARY)           AS CONTRACTUAL_SALARY,  " +
        "                0                                   AS INTERN_SALARY,  " +
        "                SUM(PF_DEDUCTION)                   AS PF_DEDUCTION,  " +
        "                SUM(TAX_DEDUCTION)                  AS TAX_DEDUCTION,  " +
        "                SUM(WELFARE_FUND_DEDUCTION)         AS WELFARE_FUND_DEDUCTION,  " +
        "                SUM(MOBILE_BILL_DEDUCTION)          AS MOBILE_BILL_DEDUCTION,  " +
        "                SUM(OTHER_DEDUCTION)                AS OTHER_DEDUCTION,  " +
        "                SUM(TOTAL_DEDUCTION)                AS TOTAL_DEDUCTION,  " +
        "                SUM(NET_PAY)                        AS NET_PAY,  " +
        "                SUM(PF_CONTRIBUTION)                AS PF_CONTRIBUTION,  " +
        "                SUM(GF_CONTRIBUTION)                AS GF_CONTRIBUTION,  " +
        "                SUM(PROVISION_FOR_FESTIVAL_BONUS)   AS PROVISION_FOR_FESTIVAL_BONUS,  " +
        "                SUM(PROVISION_FOR_LEAVE_ENCASHMENT) AS PROVISION_FOR_LEAVE_ENCASHMENT  " +
        "         FROM EMPLOYEE_SALARY  " +
        "         where MONTH = :month  " +
        "           and YEAR = :year  " +
        "           and EMPLOYEE_CATEGORY = 'CONTRACTUAL_EMPLOYEE'  " +
        "           AND EMPLOYEE_ID NOT IN (  " +
        "             SELECT ID  " +
        "             FROM EMPLOYEE  " +
        "             WHERE DESIGNATION_ID in  " +
        "                   (  " +
        "                       SELECT ID  " +
        "                       FROM DESIGNATION  " +
        "                       WHERE UPPER(DESIGNATION_NAME) like UPPER('%Intern%'))  " +
        "         )  " +
        "         group by DEPARTMENT  " +
        "         UNION  " +
        "         SELECT DEPARTMENT                          AS DEPARTMENT,  " +
        "                count(id)                           AS HEAD_COUNT,  " +
        "                0                                   AS BASIC,  " +
        "                0                                   AS HOUSE_RENT,  " +
        "                0                                   AS MEDICAL,  " +
        "                0                                   AS CONVEYANCE,  " +
        "                SUM(ARREAR_SALARY)                  AS ARREAR,  " +
        "                0                                   AS GROSS,  " +
        "                0                                   AS CONTRACTUAL_SALARY,  " +
        "                sum(NET_PAY)                        AS INTERN_SALARY,  " +
        "                SUM(PF_DEDUCTION)                   AS PF_DEDUCTION,  " +
        "                SUM(TAX_DEDUCTION)                  AS TAX_DEDUCTION,  " +
        "                SUM(WELFARE_FUND_DEDUCTION)         AS WELFARE_FUND_DEDUCTION,  " +
        "                SUM(MOBILE_BILL_DEDUCTION)          AS MOBILE_BILL_DEDUCTION,  " +
        "                SUM(OTHER_DEDUCTION)                AS OTHER_DEDUCTION,  " +
        "                SUM(TOTAL_DEDUCTION)                AS TOTAL_DEDUCTION,  " +
        "                SUM(NET_PAY)                        AS NET_PAY,  " +
        "                SUM(PF_CONTRIBUTION)                AS PF_CONTRIBUTION,  " +
        "                SUM(GF_CONTRIBUTION)                AS GF_CONTRIBUTION,  " +
        "                SUM(PROVISION_FOR_FESTIVAL_BONUS)   AS PROVISION_FOR_FESTIVAL_BONUS,  " +
        "                SUM(PROVISION_FOR_LEAVE_ENCASHMENT) AS PROVISION_FOR_LEAVE_ENCASHMENT  " +
        "         FROM EMPLOYEE_SALARY  " +
        "         where MONTH = :month  " +
        "           and YEAR = :year  " +
        "           and (EMPLOYEE_CATEGORY =  " +
        "                'INTERN'  " +
        "             OR EMPLOYEE_ID IN (  " +
        "                 SELECT ID  " +
        "                 FROM EMPLOYEE  " +
        "                 WHERE DESIGNATION_ID in  " +
        "                       (  " +
        "                           SELECT ID  " +
        "                           FROM DESIGNATION  " +
        "                           WHERE UPPER(DESIGNATION_NAME) like UPPER('%Intern%')  " +
        "                       )  " +
        "             )  " +
        "             )  " +
        "         group by DEPARTMENT  " +
        "     ) AS RESULT_SET  " +
        "group by DEPARTMENT",
        nativeQuery = true
    )
    List<Object[]> getSalarySummary(String month, int year);

    @Query(
        value = "select COALESCE(SUM(PAYABLE_GROSS_SALARY),0) as totalSalary " +
        "from EMPLOYEE_SALARY " +
        "where EMPLOYEE_SALARY.YEAR = :year " +
        "  and EMPLOYEE_SALARY.MONTH = :month ",
        nativeQuery = true
    )
    Double getSumOfTotalSalary(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(SUM(es.PAYABLE_GROSS_SALARY),0) AS Cash  " +
        "from (select *  " +
        "      from EMPLOYEE_SALARY  " +
        "      where EMPLOYEE_SALARY.YEAR = :year  " +
        "        and EMPLOYEE_SALARY.MONTH = :month) as es  " +
        "         LEFT JOIN EMPLOYEE E on E.ID = ES.EMPLOYEE_ID  " +
        "where e.DISBURSEMENT_METHOD = 'CASH';",
        nativeQuery = true
    )
    double getSumOfCashPayment(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(sum(PAYABLE_GROSS_SALARY),0) as hold  " +
        "from EMPLOYEE_SALARY  " +
        "where EMPLOYEE_SALARY.YEAR = :year  " +
        "  and EMPLOYEE_SALARY.MONTH = :month  " +
        "  and EMPLOYEE_SALARY.IS_HOLD = true;",
        nativeQuery = true
    )
    double getSumOfHoldAmount(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(sum(ALLOWANCE_01),0) " +
        "from EMPLOYEE_SALARY " +
        "where EMPLOYEE_SALARY.YEAR = :year " +
        "  and EMPLOYEE_SALARY.MONTH = :month ",
        nativeQuery = true
    )
    double getSumOfAllowance01(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(sum(ALLOWANCE_02),0)  " +
        "from EMPLOYEE_SALARY  " +
        "where EMPLOYEE_SALARY.YEAR = :year  " +
        "  and EMPLOYEE_SALARY.MONTH = :month ",
        nativeQuery = true
    )
    double getSumOfAllowance02(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(sum(ALLOWANCE_03),0)  " +
        "from EMPLOYEE_SALARY  " +
        "where EMPLOYEE_SALARY.YEAR = :year  " +
        "  and EMPLOYEE_SALARY.MONTH = :month ",
        nativeQuery = true
    )
    double getSumOfAllowance03(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(sum(ALLOWANCE_04),0)  " +
        "from EMPLOYEE_SALARY  " +
        "where EMPLOYEE_SALARY.YEAR = :year  " +
        "  and EMPLOYEE_SALARY.MONTH = :month ",
        nativeQuery = true
    )
    double getSumOfAllowance04(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(sum(ALLOWANCE_05),0)  " +
        "from EMPLOYEE_SALARY  " +
        "where EMPLOYEE_SALARY.YEAR = :year  " +
        "  and EMPLOYEE_SALARY.MONTH = :month ",
        nativeQuery = true
    )
    double getSumOfAllowance05(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(sum(ALLOWANCE_06),0)  " +
        "  from EMPLOYEE_SALARY  " +
        "   where EMPLOYEE_SALARY.YEAR = :year   " +
        "       and EMPLOYEE_SALARY.MONTH = :month ",
        nativeQuery = true
    )
    double getSumOfAllowance06(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(SUM(es.PAYABLE_GROSS_SALARY),0) AS BRAC_BANK  " +
        "from (select *  " +
        "      from EMPLOYEE_SALARY  " +
        "      where EMPLOYEE_SALARY.YEAR = :year  " +
        "        and EMPLOYEE_SALARY.MONTH = :month) as es  " +
        "         LEFT JOIN EMPLOYEE E on E.ID = ES.EMPLOYEE_ID  " +
        "where lower(e.BANK_NAME) = lower('brac bank') and e.DISBURSEMENT_METHOD='BANK'",
        nativeQuery = true
    )
    double getTotalBracBankTransfers(@Param("year") int year, @Param("month") String month);

    @Query(
        value = "select COALESCE(SUM(es.PAYABLE_GROSS_SALARY),0) AS BRAC_BANK  " +
        "from (select *  " +
        "      from EMPLOYEE_SALARY  " +
        "      where EMPLOYEE_SALARY.YEAR = :year  " +
        "        and EMPLOYEE_SALARY.MONTH = :month) as es  " +
        "         LEFT JOIN EMPLOYEE E on E.ID = ES.EMPLOYEE_ID  " +
        "where lower(e.BANK_NAME) <> lower('brac bank') and e.DISBURSEMENT_METHOD='BANK'",
        nativeQuery = true
    )
    double getTotalOtherBankTransfers(@Param("year") int year, @Param("month") String month);

    @Query(value = "select model from EmployeeSalary model where model.employee.id=:employeeId and model.isHold=true")
    List<EmployeeSalary> getHoldSalaryByEmployeeId(long employeeId);

    @Query(
        value = "select model from EmployeeSalary model where " +
        "model.isHold = true AND " +
        "(" +
        "lower(model.employee.fullName) like :searchText OR " +
        "lower(model.employee.pin) like :searchText " +
        ")"
    )
    List<EmployeeSalary> getHoldSalaries(String searchText);

    @Query(value = "select model from EmployeeSalary model where model.employee.id = :employeeId order by model.year desc")
    List<EmployeeSalary> findAllByEmployeeId(long employeeId);

    @Query(value = "select model from EmployeeSalary model " + "where model.employee.id = :employeeId and model.year = :year")
    List<EmployeeSalary> findAllByEmployeeIdAndYear(long employeeId, int year);

    @Query(
        "select es from EmployeeSalary es " +
        "where es.month =:month " +
        "AND es.year =:year " +
        "AND es.isHold <> true  " +
        "AND es.employee.employeeCategory <> 'INTERN'" +
        "order by es.employee.pin"
    )
    List<EmployeeSalary> findDisbursedSalaryByYearAndMonth(@Param("year") int year, @Param("month") Month month);

    @Query(
        value = "select model from EmployeeSalary model " +
        "where model.employee.id = :employeeId " +
        "and model.year = :year " +
        "and model.isVisibleToEmployee = true"
    )
    List<EmployeeSalary> findAllVisibleByEmployeeIdAndYear(long employeeId, int year);

    @Query(
        "select  model from EmployeeSalary model " +
        "where model.employee.id =:employeeId " +
        "and model.year =:year " +
        "and model.month =:month "
    )
    Optional<EmployeeSalary> findEmployeeSalaryByEmployeeIdYearAndMonth(Long employeeId, int year, Month month);

    @Query(
        value = "select model from EmployeeSalary model " +
        "where model.month =:month " +
        "and model.year =:year " +
        "and model.allowance01 > 0 " +
        "and model.isHold <> true " +
        "and model.employee.employeeCategory <> 'INTERN'" +
        "order by model.employee.pin"
    )
    List<EmployeeSalary> findLivingAllowanceByMonthAndYear(int year, Month month);

    @Query(
        value = "select model from EmployeeSalary model " +
        "where model.month =:month " +
        "and model.year =:year " +
        "and model.allowance02 > 0 " +
        "and model.isHold <> true " +
        "and model.employee.employeeCategory <> 'INTERN'" +
        "order by model.employee.pin"
    )
    List<EmployeeSalary> findCarAllowanceByMonthAndYear(int year, Month month);

    @Query(
        value = "select model from EmployeeSalary model " +
        "where model.month =:month " +
        "and model.year =:year " +
        "and model.allowance03 > 0 " +
        "and model.isHold <> true " +
        "and model.employee.employeeCategory <> 'INTERN'" +
        "order by model.employee.pin"
    )
    List<EmployeeSalary> findHouseRentAllowanceByMonthAndYear(int year, Month month);

    @Query(
        value = "select model from EmployeeSalary model " +
        "where model.month =:month " +
        "and model.year =:year " +
        "and model.allowance04 > 0 " +
        "and model.isHold <> true " +
        "and model.employee.employeeCategory <> 'INTERN'" +
        "order by model.employee.pin"
    )
    List<EmployeeSalary> findCompanySecretaryAllowanceByMonthAndYear(int year, Month month);

    @Query(
        value = "select model.employee from EmployeeSalary model " +
        "where model.year =:year " +
        "and model.month=:month " +
        "order by model.employee.pin"
    )
    List<Employee> getEmployeeListForYearlyTaxSubmissionReportBetweenTime(int year, Month month);

    @Query(
        "SELECT model FROM EmployeeSalary model WHERE model.year >= :startYear " +
        "AND model.year <= :endYear " +
        "ORDER BY model.year DESC, model.employee.pin ASC "
    )
    List<EmployeeSalary> findAllSalaryBetweenYears(int startYear, int endYear);
}
