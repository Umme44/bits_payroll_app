package com.bits.hr.repository;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Religion;
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

/**
 * Spring Data  repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Long countByIdIn(List<Long> idList);

    @Query("SELECT model FROM Employee model WHERE model.religion = :religion")
    List<Employee> findAllByReligion(Religion religion);

    @Query("SELECT model FROM Employee model WHERE model.officialContactNo = :officeContactNo")
    Optional<Employee> findByOfficialContactNo(@Param("officeContactNo") String officeContactNo);

    @Query("SELECT model FROM Employee model WHERE model.pin = :pin")
    Optional<Employee> findByPin(@Param("pin") String pin);

    Optional<Employee> findEmployeeByPin(String pin);

    @Query("SELECT  COALESCE(model.pin,'') FROM Employee model WHERE model.id = :employeeId")
    String getPinByEmployeeId(@Param("employeeId") long employeeId);

    @Query("SELECT  COALESCE(model.pin,'') FROM Employee model WHERE lower(trim(model.officialEmail)) = lower(trim(:officialEmail))")
    String getPinByOfficialEmail(@Param("officialEmail") String officialEmail);

    @Query("SELECT  COALESCE(model.fullName,'') FROM Employee model WHERE model.id = :employeeId")
    String getNameByEmployeeId(@Param("employeeId") long employeeId);

    @Query("SELECT model.id FROM Employee model WHERE model.pin = :pin")
    long getIdByPin(@Param("pin") String pin);

    @Query("SELECT model.id FROM Employee model WHERE model.pin = :pin")
    Optional<Long> findIdByPin(@Param("pin") String pin);

    @Query(value = "SELECT u FROM Employee u order by u.pin")
    Page<Employee> findAll(Pageable pageable);

    @Query(value = "SELECT u FROM Employee u order by u.pin")
    List<Employee> findAll();

    Page<Employee> findEmployeesByDepartmentAndDesignationAndUnitAndFullName(
        Department department,
        Designation designation,
        Unit unit,
        String searchString,
        Pageable pageable
    );

    Page<Employee> findEmployeesByDepartmentId(Long DepartmentId, Pageable pageable);

    Page<Employee> findEmployeesByDesignationId(Long DesignationId, Pageable pageable);

    Page<Employee> findEmployeesByUnitId(Long UnitId, Pageable pageable);

    Page<Employee> findEmployeesByFullNameLike(String searchString, Pageable pageable);

    @Query(
        value = "SELECT em FROM Employee em " +
        "WHERE em.employeeCategory =:employeeCategory " +
        " AND em.religion=:religion " +
        " AND em.employmentStatus = :employmentStatus " +
        " order by em.pin "
    )
    List<Employee> findByCategoryReligionAndActiveStatus(
        EmployeeCategory employeeCategory,
        Religion religion,
        EmploymentStatus employmentStatus
    );

    @Query(
        value = "SELECT u FROM Employee u " +
        "where" +
        "( lower(u.fullName) like lower(:searchStringWild) " +
        " OR u.skypeId like :searchString" +
        " OR u.officialEmail = :searchString" +
        " OR u.pin = :searchString" +
        " OR u.whatsappId = :searchStringPrefix" +
        " OR u.whatsappId = :searchStringPrefixPlus" +
        " OR u.whatsappId = :searchString" +
        " OR u.emergencyContactPersonContactNumber = :searchStringPrefix" +
        " OR u.emergencyContactPersonContactNumber = :searchStringPrefixPlus" +
        " OR u.emergencyContactPersonContactNumber = :searchString" +
        " OR u.officialContactNo = :searchStringPrefix" +
        " OR u.officialContactNo = :searchStringPrefixPlus" +
        " OR u.officialContactNo = :searchString)" +
        "and (:designationId = 0L OR u.designation.id = :designationId)" +
        "and (:departmentId = 0L OR u.department.id = :departmentId)" +
        "and (:unitId = 0L OR u.unit.id = :unitId)" +
        "and (:BloodGroup = '' OR u.bloodGroup = :BloodGroup)" +
        "and (:gender = '' OR u.gender = :gender)" +
        " order by u.pin "
    )
    Page<Employee> searchEmployee(
        String searchString,
        String searchStringPrefix,
        String searchStringPrefixPlus,
        String searchStringWild,
        Long designationId,
        Long departmentId,
        Long unitId,
        String BloodGroup,
        String gender,
        Pageable pageable
    );

    @Query(
        value = "select model " +
        "from Employee model " +
        "where model.id not in (select resignation.employee.id " +
        "                       from EmployeeResignation resignation " +
        "                       where resignation.approvalStatus = 'APPROVED' " +
        "                         AND resignation.lastWorkingDay <= :today) " +
        "  and model.id not in (select contractual.id " +
        "                       from Employee contractual " +
        "                       where (contractual.employeeCategory = 'INTERN' OR " +
        "                              contractual.employeeCategory = 'CONTRACTUAL_EMPLOYEE' " +
        "                           ) " +
        "                         AND ( " +
        "                               (contractual.contractPeriodEndDate < :today AND " +
        "                                contractual.contractPeriodExtendedTo is null) " +
        "                               OR (contractual.contractPeriodExtendedTo is not null AND " +
        "                                   contractual.contractPeriodExtendedTo < :today) " +
        "                           ) " +
        "    )" +
        " AND ( " +
        "(lower(model.fullName) like concat('%',:searchText,'%')) OR " +
        "(lower(model.pin) like concat('%',:searchText,'%')) OR " +
        "(lower(model.department.departmentName) like concat('%',:searchText,'%')) OR " +
        "(lower(model.unit.unitName) like concat('%',:searchText,'%')) OR " +
        "(lower(model.designation.designationName) like concat('%',:searchText,'%')) OR " +
        "(lower(model.officialContactNo) like concat('%',:searchText,'%')) OR " +
        "(lower(model.officialEmail) like concat('%',:searchText,'%'))" +
        ")"
    )
    Page<Employee> getAllActiveEmployeesTillDate(String searchText, LocalDate today, Pageable pageable);

    @Query(
        value = "select model " +
        "from Employee model " +
        "where model.id not in (select resignation.employee.id " +
        "                       from EmployeeResignation resignation " +
        "                       where resignation.approvalStatus = 'APPROVED' " +
        "                         AND resignation.lastWorkingDay <= :today) " +
        "  and model.id not in (select contractual.id " +
        "                       from Employee contractual " +
        "                       where (contractual.employeeCategory = 'INTERN' OR " +
        "                              contractual.employeeCategory = 'CONTRACTUAL_EMPLOYEE' " +
        "                           ) " +
        "                         AND ( " +
        "                               (contractual.contractPeriodEndDate < :today AND " +
        "                                contractual.contractPeriodExtendedTo is null) " +
        "                               OR (contractual.contractPeriodExtendedTo is not null AND " +
        "                                   contractual.contractPeriodExtendedTo < :today) " +
        "                           ) " +
        "    )" +
        ""
    )
    List<Employee> getAllActiveEmployeesTillDate(LocalDate today);

    @Query(
        value = "select model " +
        "from Employee model " +
        "where (" +
        "(lower(model.fullName) like concat('%',:searchText,'%')) OR " +
        "(lower(model.pin) like concat('%',:searchText,'%')) OR " +
        "(lower(model.department.departmentName) like concat('%',:searchText,'%')) OR " +
        "(lower(model.unit.unitName) like concat('%',:searchText,'%')) OR " +
        "(lower(model.designation.designationName) like concat('%',:searchText,'%')) OR " +
        "(lower(model.officialContactNo) like concat('%',:searchText,'%')) OR " +
        "(lower(model.officialEmail) like concat('%',:searchText,'%'))" +
        ") AND (:status is null OR model.employmentStatus = :status)"
    )
    Page<Employee> getAllEmployeesTillDate(String searchText, EmploymentStatus status, Pageable pageable);

    @Query(
        value = "SELECT u FROM Employee u " +
        "where" +
        " u.employeeCategory = 'CONTRACTUAL_EMPLOYEE' AND " +
        " u.employmentStatus <> com.bits.hr.domain.enumeration.EmploymentStatus.RESIGNED AND " +
        "( lower(u.fullName) like lower(:searchStringWild) " +
        " OR u.skypeId like :searchString" +
        " OR u.officialEmail = :searchString" +
        " OR u.pin = :searchString" +
        " OR u.whatsappId = :searchStringPrefix" +
        " OR u.whatsappId = :searchStringPrefixPlus" +
        " OR u.whatsappId = :searchString" +
        " OR u.emergencyContactPersonContactNumber = :searchStringPrefix" +
        " OR u.emergencyContactPersonContactNumber = :searchStringPrefixPlus" +
        " OR u.emergencyContactPersonContactNumber = :searchString" +
        " OR u.officialContactNo = :searchStringPrefix" +
        " OR u.officialContactNo = :searchStringPrefixPlus" +
        " OR u.officialContactNo = :searchString)" +
        "and (:designationId = 0L OR u.designation.id = :designationId)" +
        "and (:departmentId = 0L OR u.department.id = :departmentId)" +
        "and (:unitId = 0L OR u.unit.id = :unitId)" +
        "and (:BloodGroup = '' OR u.bloodGroup = :BloodGroup) " +
        "and (:gender = '' OR u.gender = :gender) " +
        "and(" +
        "(u.contractPeriodEndDate >=:startDate and u.contractPeriodEndDate <=:endDate) or " +
        "( cast(:startDate as date) is null OR cast(:endDate as date) is null)" +
        ")" +
        " order by u.pin "
    )
    Page<Employee> employeeSearchUpcomingEventContractEnd(
        String searchString,
        String searchStringPrefix,
        String searchStringPrefixPlus,
        String searchStringWild,
        Long designationId,
        Long departmentId,
        Long unitId,
        String BloodGroup,
        String gender,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    );

    @Query(
        value = "SELECT u FROM Employee u " +
        "where" +
        " u.employeeCategory = 'REGULAR_PROVISIONAL_EMPLOYEE' AND " +
        " u.employmentStatus <> com.bits.hr.domain.enumeration.EmploymentStatus.RESIGNED AND " +
        "( lower(u.fullName) like lower(:searchStringWild) " +
        " OR u.skypeId like :searchString" +
        " OR u.officialEmail = :searchString" +
        " OR u.pin = :searchString" +
        " OR u.whatsappId = :searchStringPrefix" +
        " OR u.whatsappId = :searchStringPrefixPlus" +
        " OR u.whatsappId = :searchString" +
        " OR u.emergencyContactPersonContactNumber = :searchStringPrefix" +
        " OR u.emergencyContactPersonContactNumber = :searchStringPrefixPlus" +
        " OR u.emergencyContactPersonContactNumber = :searchString" +
        " OR u.officialContactNo = :searchStringPrefix" +
        " OR u.officialContactNo = :searchStringPrefixPlus" +
        " OR u.officialContactNo = :searchString)" +
        "and (:designationId = 0L OR u.designation.id = :designationId)" +
        "and (:departmentId = 0L OR u.department.id = :departmentId)" +
        "and (:unitId = 0L OR u.unit.id = :unitId)" +
        "and (:BloodGroup = '' OR u.bloodGroup = :BloodGroup)" +
        "and (:gender = '' OR u.gender = :gender) " +
        "and(" +
        "(u.probationPeriodEndDate >=:startDate and u.probationPeriodEndDate <=:endDate) or " +
        "    (cast(:startDate as date) is null OR cast(:endDate as date) is null)" +
        ")" +
        " order by u.pin "
    )
    Page<Employee> employeeSearchUpcomingEventProbationEnd(
        String searchString,
        String searchStringPrefix,
        String searchStringPrefixPlus,
        String searchStringWild,
        Long designationId,
        Long departmentId,
        Long unitId,
        String BloodGroup,
        String gender,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    );

    /**
     * -- for Regular confirmed employee OR regular probational employee
     * -- take all which is not resigned
     * union
     * -- for Regular confirmed employee OR regular probational employee
     * -- take resigned in which resignation date > =1st Aug. { resignation date >= }
     * union
     * ok test***
     * -- for contractual employee OR Intern
     * -- take all whose contract end date passes 1st aug  { contractEndDate >= 1st aug }
     **/
    @Query(
        value = "SELECT employee FROM Employee employee where " +
        "( " +
        " employee.id in " +
        "       ( select employee.id FROM Employee employee " +
        "       where ( employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' OR employee.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') AND employee.employmentStatus <> 'RESIGNED' " +
        "       ) " +
        " OR employee.id in " +
        "       ( select employee FROM Employee employee " +
        "       where ( employee.employeeCategory='CONTRACTUAL_EMPLOYEE' OR employee.employeeCategory='INTERN') AND employee.employmentStatus <> 'RESIGNED' AND ( employee.contractPeriodEndDate >=:firstDayOfMonth  OR employee.contractPeriodExtendedTo>=:firstDayOfMonth)" +
        "       ) " +
        " OR employee.id in " +
        "       ( select employeeResignation.employee.id FROM EmployeeResignation employeeResignation inner join employeeResignation.employee employee " +
        "       where ( ( employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' OR employee.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') ) AND employeeResignation.lastWorkingDay >=:firstDayOfMonth AND  employeeResignation.approvalStatus='APPROVED'" +
        "       ) " +
        " OR employee.id in " +
        "       ( select employeeResignation.employee.id FROM EmployeeResignation employeeResignation inner join employeeResignation.employee employee " +
        "       where ( ( employee.employeeCategory='CONTRACTUAL_EMPLOYEE' OR employee.employeeCategory='INTERN') AND ( employee.contractPeriodEndDate >=:firstDayOfMonth  OR employee.contractPeriodExtendedTo>=:firstDayOfMonth) ) AND employeeResignation.lastWorkingDay >=:firstDayOfMonth AND  employeeResignation.approvalStatus='APPROVED' " +
        "       ) " +
        " ) " +
        " AND employee.dateOfJoining <= :lastDateOfMonth " +
        " AND employee.mainGrossSalary > 0 " +
        " order by employee.pin "
    )
    List<Employee> getEligibleEmployeeForSalaryGeneration_v2(LocalDate firstDayOfMonth, LocalDate lastDateOfMonth);

    @Query(
        value = "SELECT employee FROM Employee employee where " +
        "( " +
        " employee.id in " +
        "       ( select employee.id FROM Employee employee " +
        "       where ( employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' OR employee.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') AND employee.employmentStatus <> 'RESIGNED' " +
        "       ) " +
        " OR employee.id in " +
        "       ( select employee FROM Employee employee " +
        "       where ( employee.employeeCategory='CONTRACTUAL_EMPLOYEE' OR employee.employeeCategory='INTERN') AND employee.employmentStatus <> 'RESIGNED' AND ( employee.contractPeriodEndDate >=:firstDayOfMonth  OR employee.contractPeriodExtendedTo>=:firstDayOfMonth)" +
        "       ) " +
        " OR employee.id in " +
        "       ( select employeeResignation.employee.id FROM EmployeeResignation employeeResignation inner join employeeResignation.employee employee " +
        "       where ( ( employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' OR employee.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') ) AND employeeResignation.lastWorkingDay >=:firstDayOfMonth AND  employeeResignation.approvalStatus='APPROVED'" +
        "       ) " +
        " OR employee.id in " +
        "       ( select employeeResignation.employee.id FROM EmployeeResignation employeeResignation inner join employeeResignation.employee employee " +
        "       where ( ( employee.employeeCategory='CONTRACTUAL_EMPLOYEE' OR employee.employeeCategory='INTERN') AND ( employee.contractPeriodEndDate >=:firstDayOfMonth  OR employee.contractPeriodExtendedTo>=:firstDayOfMonth) ) AND employeeResignation.lastWorkingDay >=:firstDayOfMonth AND  employeeResignation.approvalStatus='APPROVED' " +
        "       ) " +
        " ) " +
        " AND employee.dateOfJoining <= :lastDateOfMonth " +
        " AND employee.mainGrossSalary > 0 " +
        " AND (" +
        "(lower(employee.fullName) like concat('%',:searchText,'%')) OR " +
        "(lower(employee.pin) like concat('%',:searchText,'%')) OR " +
        "(lower(employee.department.departmentName) like concat('%',:searchText,'%')) OR " +
        "(lower(employee.unit.unitName) like concat('%',:searchText,'%')) OR " +
        "(lower(employee.designation.designationName) like concat('%',:searchText,'%')) OR " +
        "(lower(employee.officialContactNo) like concat('%',:searchText,'%')) OR " +
        "(lower(employee.officialEmail) like concat('%',:searchText,'%'))" +
        ")" +
        " order by employee.pin "
    )
    Page<Employee> getAllActiveEmployeeProfiles(LocalDate firstDayOfMonth, LocalDate lastDateOfMonth, String searchText, Pageable pageable);

    @Query(
        "select distinct(employee) from Employee employee " +
        "where employee.id not in " +
        "       (select resignation.employee.id from EmployeeResignation resignation " +
        "           where resignation.approvalStatus = 'APPROVED' " +
        "           and (resignation.lastWorkingDay < :aitConfigStartDate " +
        "                   or resignation.lastWorkingDay > :aitConfigEndDate)" +
        "       )" +
        "and employee.employeeCategory <> 'INTERN' " +
        "and employee.id not in ( " +
        "   select employee.id From Employee employee " +
        "where (employee.employeeCategory='CONTRACTUAL_EMPLOYEE' " +
        "and employee.contractPeriodEndDate is not null " +
        "and employee.contractPeriodEndDate <:currentDate " +
        "and employee.contractPeriodExtendedTo is null) " +
        "or ( " +
        "   employee.employeeCategory='CONTRACTUAL_EMPLOYEE' " +
        "   and employee.contractPeriodExtendedTo is not null " +
        "   and employee.contractPeriodExtendedTo <:currentDate)" +
        ") " +
        "and employee.dateOfJoining <= :aitConfigEndDate " +
        "and employee.mainGrossSalary > 0 " +
        "order by employee.pin"
    )
    List<Employee> getEligibleEmployeeForTaxAcknowledgementReceipt(
        LocalDate aitConfigStartDate,
        LocalDate aitConfigEndDate,
        LocalDate currentDate
    );

    // Mehedi
    @Query(
        value = "SELECT employee FROM Employee employee where " +
        "( " +
        " employee.id in " +
        "       ( select employee.id FROM Employee employee " +
        "       where ( employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' OR employee.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') AND employee.employmentStatus <> 'RESIGNED' " +
        "       ) " +
        " OR employee.id in " +
        "       ( select employee FROM Employee employee " +
        "       where ( employee.employeeCategory='CONTRACTUAL_EMPLOYEE' OR employee.employeeCategory='INTERN') AND employee.employmentStatus <> 'RESIGNED' AND ( employee.contractPeriodEndDate >=:startDate  OR employee.contractPeriodExtendedTo>=:startDate)" +
        "       ) " +
        " OR employee.id in " +
        "       ( select employeeResignation.employee.id FROM EmployeeResignation employeeResignation inner join employeeResignation.employee employee " +
        "       where ( ( employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' OR employee.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') ) AND employeeResignation.lastWorkingDay >=:startDate AND  employeeResignation.approvalStatus='APPROVED'" +
        "       ) " +
        " OR employee.id in " +
        "       ( select employeeResignation.employee.id FROM EmployeeResignation employeeResignation inner join employeeResignation.employee employee " +
        "       where ( ( employee.employeeCategory='CONTRACTUAL_EMPLOYEE' OR employee.employeeCategory='INTERN') AND ( employee.contractPeriodEndDate >=:startDate  OR employee.contractPeriodExtendedTo>=:startDate) ) AND employeeResignation.lastWorkingDay >=:startDate AND  employeeResignation.approvalStatus='APPROVED' " +
        "       ) " +
        " ) " +
        " AND employee.dateOfJoining <= :endDate " +
        " AND employee.mainGrossSalary > 0 " +
        " AND ( lower(employee.fullName) LIKE CONCAT('%',lower(:searchText),'%') OR employee.pin LIKE CONCAT('%',:searchText,'%') )" +
        " AND (:departmentId = 0L OR employee.department.id = :departmentId)" +
        " AND (:designationId = 0L OR employee.designation.id = :designationId)" +
        " AND (:unitId = 0L OR employee.unit.id = :unitId)" +
        " order by employee.pin "
    )
    List<Employee> getEligibleEmployeeForMonthlyAttendanceUsingFilter(
        LocalDate startDate,
        LocalDate endDate,
        String searchText,
        Long departmentId,
        Long designationId,
        Long unitId
    );

    @Query(
        value = "SELECT employee FROM Employee employee where " +
        "( " +
        " employee.id in " +
        "       ( select employee.id FROM Employee employee " +
        "       where ( employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' OR employee.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') AND employee.employmentStatus <> 'RESIGNED' " +
        "       ) " +
        " OR employee.id in " +
        "       ( select employee FROM Employee employee " +
        "       where ( employee.employeeCategory='CONTRACTUAL_EMPLOYEE' OR employee.employeeCategory='INTERN') AND employee.employmentStatus <> 'RESIGNED' AND ( employee.contractPeriodEndDate >=:firstDayOfMonth  OR employee.contractPeriodExtendedTo>=:firstDayOfMonth)" +
        "       ) " +
        " OR employee.id in " +
        "       ( select employeeResignation.employee.id FROM EmployeeResignation employeeResignation inner join employeeResignation.employee employee " +
        "       where ( ( employee.employeeCategory='REGULAR_CONFIRMED_EMPLOYEE' OR employee.employeeCategory='REGULAR_PROVISIONAL_EMPLOYEE') ) AND employeeResignation.lastWorkingDay >=:firstDayOfMonth AND  employeeResignation.approvalStatus='APPROVED'" +
        "       ) " +
        " OR employee.id in " +
        "       ( select employeeResignation.employee.id FROM EmployeeResignation employeeResignation inner join employeeResignation.employee employee " +
        "       where ( ( employee.employeeCategory='CONTRACTUAL_EMPLOYEE' OR employee.employeeCategory='INTERN') AND ( employee.contractPeriodEndDate >=:firstDayOfMonth  OR employee.contractPeriodExtendedTo>=:firstDayOfMonth) ) AND employeeResignation.lastWorkingDay >=:firstDayOfMonth AND  employeeResignation.approvalStatus='APPROVED' " +
        "       ) " +
        " ) " +
        " AND employee.dateOfJoining <= :lastDateOfMonth " +
        " AND employee.mainGrossSalary > 0 " +
        " order by employee.pin "
    )
    Set<Employee> getEligibleEmployeeSetForSalaryGeneration_v2(LocalDate firstDayOfMonth, LocalDate lastDateOfMonth);

    Optional<Employee> findOneByOfficialEmail(String officialEmail);

    @Query(
        value = "SELECT u FROM Employee u where " +
        " u.employmentStatus <> com.bits.hr.domain.enumeration.EmploymentStatus.RESIGNED AND " +
        " u.mainGrossSalary > 0 " +
        " order by u.pin "
    )
    Set<Employee> getEligibleEmployeeSetForSalaryGeneration();

    @Query("SELECT model.pin FROM Employee model WHERE model.user.id = :userId")
    Optional<String> findEmployeePinByUserId(@Param("userId") Long userId);

    @Query("SELECT model FROM Employee model WHERE model.user.id = :userId and model.pin=:pin")
    List<Employee> findAllByUserIdExceptPin(Long userId, String pin);

    @Query("SELECT model FROM Employee model WHERE model.reportingTo.id = :reportingToId")
    List<Employee> getMyTeamByReportingToId(Long reportingToId);

    @Query(
        value = "select model " +
        "from Employee model " +
        "where model.reportingTo.id = :reportingToId " +
        "  and model not in (select contractual_em " +
        "                    from Employee contractual_em " +
        "                    where  contractual_em.contractPeriodEndDate is not null and contractual_em.contractPeriodEndDate < :date" +
        "                           and (" +
        "                               ( contractual_em.contractPeriodExtendedTo is null )" +
        "                            or (contractual_em.contractPeriodExtendedTo is not null and contractual_em.contractPeriodExtendedTo < :date)" +
        "                           )" +
        "                       and ( contractual_em.employeeCategory='CONTRACTUAL_EMPLOYEE' or contractual_em.employeeCategory='INTERN'))" +
        "  and model not in (select resignation.employee " +
        "                    from EmployeeResignation resignation " +
        "                    where resignation.approvalStatus = 'APPROVED' " +
        "                      and resignation.lastWorkingDay < :date)"
    )
    List<Employee> getActiveReportingToById(Long reportingToId, LocalDate date);

    @Query("SELECT model FROM Employee model WHERE model.reportingTo.id = :reportingToId")
    Set<Employee> getMyTeam(Long reportingToId);

    @Query(
        value = "SELECT u FROM Employee u " +
        "WHERE " +
        " u.employmentStatus = 'RESIGNED' " +
        " AND ( " +
        "       lower(u.fullName) like lower(:searchStringWild) " +
        "       OR u.skypeId like :searchString " +
        "       OR u.officialEmail = :searchString " +
        "       OR u.pin = :searchString " +
        "       OR u.whatsappId = :searchStringPrefix " +
        "       OR u.whatsappId = :searchStringPrefixPlus " +
        "       OR u.whatsappId = :searchString " +
        "       OR u.emergencyContactPersonContactNumber = :searchStringPrefix " +
        "       OR u.emergencyContactPersonContactNumber = :searchStringPrefixPlus " +
        "       OR u.emergencyContactPersonContactNumber = :searchString " +
        "       OR u.officialContactNo = :searchStringPrefix " +
        "       OR u.officialContactNo = :searchStringPrefixPlus " +
        "       OR u.officialContactNo = :searchString " +
        "     )  " +
        " AND (:designationId = 0L OR u.designation.id = :designationId) " +
        " AND (:departmentId = 0L OR u.department.id = :departmentId) " +
        " AND (:unitId = 0L OR u.unit.id = :unitId) " +
        " AND (:BloodGroup = '' OR u.bloodGroup = :BloodGroup) " +
        " AND (:gender = '' OR u.gender = :gender) " +
        " AND u.id in ( SELECT model.id FROM EmployeeResignation model WHERE model.lastWorkingDay <> null AND model.resignationDate <> null ) " +
        " order by u.pin "
    )
    Page<Employee> searchEmployeeForFinalSettlement(
        String searchString,
        String searchStringPrefix,
        String searchStringPrefixPlus,
        String searchStringWild,
        Long designationId,
        Long departmentId,
        Long unitId,
        String BloodGroup,
        String gender,
        Pageable pageable
    );

    @Query(
        value = "SELECT u FROM Employee u " +
        "WHERE " +
        " u.employmentStatus = 'RESIGNED' " +
        " AND ( " +
        "       lower(u.fullName) like lower(:searchStringWild) " +
        "       OR u.skypeId like :searchString " +
        "       OR u.officialEmail = :searchString " +
        "       OR u.pin = :searchString " +
        "       OR u.whatsappId = :searchStringPrefix " +
        "       OR u.whatsappId = :searchStringPrefixPlus " +
        "       OR u.whatsappId = :searchString " +
        "       OR u.emergencyContactPersonContactNumber = :searchStringPrefix " +
        "       OR u.emergencyContactPersonContactNumber = :searchStringPrefixPlus " +
        "       OR u.emergencyContactPersonContactNumber = :searchString " +
        "       OR u.officialContactNo = :searchStringPrefix " +
        "       OR u.officialContactNo = :searchStringPrefixPlus " +
        "       OR u.officialContactNo = :searchString " +
        "     )  " +
        " AND (:designationId = 0L OR u.designation.id = :designationId) " +
        " AND (:departmentId = 0L OR u.department.id = :departmentId) " +
        " AND (:unitId = 0L OR u.unit.id = :unitId) " +
        " AND (:BloodGroup = '' OR u.bloodGroup = :BloodGroup) " +
        " AND (:gender = '' OR u.gender = :gender) " +
        " AND u.id NOT in ( SELECT model.employee.id FROM FinalSettlement model ) " +
        " AND u.id in ( SELECT model.employee.id FROM EmployeeResignation model WHERE model.lastWorkingDay is not null AND model.resignationDate is not null ) " +
        " order by u.pin "
    )
    Page<Employee> getPendingFinalSettlement(
        String searchString,
        String searchStringPrefix,
        String searchStringPrefixPlus,
        String searchStringWild,
        Long designationId,
        Long departmentId,
        Long unitId,
        String BloodGroup,
        String gender,
        Pageable pageable
    );

    @Query(
        value = "SELECT u FROM Employee u " +
        "WHERE " +
        " u.employmentStatus = 'RESIGNED' " +
        " AND ( " +
        "       lower(u.fullName) like lower(:searchStringWild) " +
        "       OR u.skypeId like :searchString " +
        "       OR u.officialEmail = :searchString " +
        "       OR u.pin = :searchString " +
        "       OR u.whatsappId = :searchStringPrefix " +
        "       OR u.whatsappId = :searchStringPrefixPlus " +
        "       OR u.whatsappId = :searchString " +
        "       OR u.emergencyContactPersonContactNumber = :searchStringPrefix " +
        "       OR u.emergencyContactPersonContactNumber = :searchStringPrefixPlus " +
        "       OR u.emergencyContactPersonContactNumber = :searchString " +
        "       OR u.officialContactNo = :searchStringPrefix " +
        "       OR u.officialContactNo = :searchStringPrefixPlus " +
        "       OR u.officialContactNo = :searchString " +
        "     )  " +
        " AND (:designationId = 0L OR u.designation.id = :designationId) " +
        " AND (:departmentId = 0L OR u.department.id = :departmentId) " +
        " AND (:unitId = 0L OR u.unit.id = :unitId) " +
        " AND (:BloodGroup = '' OR u.bloodGroup = :BloodGroup) " +
        " AND (:gender = '' OR u.gender = :gender) " +
        " AND u.id in ( SELECT model.id FROM FinalSettlement model ) " +
        " order by u.pin "
    )
    Page<Employee> getSettledFinalSettlement(
        String searchString,
        String searchStringPrefix,
        String searchStringPrefixPlus,
        String searchStringWild,
        Long designationId,
        Long departmentId,
        Long unitId,
        String BloodGroup,
        String gender,
        Pageable pageable
    );

    @Query("SELECT model FROM Employee model WHERE model.isAllowedToGiveOnlineAttendance is null ")
    List<Employee> getEmployeeWhereAttendanceEligibilityNull();

    @Query(
        value = "select model " +
        "from Employee model " +
        "where ( lower(model.fullName) like lower(:searchText) " +
        "   OR model.pin like :searchText " +
        "   OR lower(model.bloodGroup) like lower(:searchText) ) " +
        "    and model not in (select contractual_em " +
        "                      from Employee contractual_em " +
        "                      where contractual_em.contractPeriodEndDate is not null " +
        "                        and contractual_em.contractPeriodEndDate < :currentDate) " +
        "    and model not in (select contractual_em " +
        "                      from Employee contractual_em " +
        "                      where contractual_em.contractPeriodExtendedTo is not null " +
        "                        and contractual_em.contractPeriodExtendedTo < :currentDate) " +
        "    and model not in (select resignation.employee " +
        "                      from EmployeeResignation resignation " +
        "                      where resignation.approvalStatus = 'APPROVED' " +
        "                        and resignation.lastWorkingDay < :currentDate) " +
        " order by model.pin"
    )
    Page<Employee> searchAllByFullNameAndPinAndBloodGroup(String searchText, LocalDate currentDate, Pageable pageable);

    //@Query(value = "select model from Employee model where lower(model.bloodGroup) = lower(:searchText) ")
    @Query(
        value = "select model " +
        "from Employee model " +
        "where lower(model.bloodGroup) = lower(:searchText) " +
        "    and model not in (select contractual_em " +
        "                      from Employee contractual_em " +
        "                      where contractual_em.contractPeriodEndDate is not null " +
        "                        and contractual_em.contractPeriodEndDate < :currentDate) " +
        "    and model not in (select contractual_em " +
        "                      from Employee contractual_em " +
        "                      where contractual_em.contractPeriodExtendedTo is not null " +
        "                        and contractual_em.contractPeriodExtendedTo < :currentDate) " +
        "    and model not in (select resignation.employee " +
        "                      from EmployeeResignation resignation " +
        "                      where resignation.approvalStatus = 'APPROVED' " +
        "                        and resignation.lastWorkingDay < :currentDate) " +
        "order by model.pin"
    )
    Page<Employee> findByBloodGroup(String searchText, LocalDate currentDate, Pageable pageable);

    @Query(
        value = " select model from Employee model " +
        " where" +
        " ( model.employeeCategory = 'REGULAR_PROVISIONAL_EMPLOYEE' or model.employeeCategory = 'REGULAR_CONFIRMED_EMPLOYEE' ) and " +
        " ( ( model.dateOfJoining >=:startDate and model.dateOfJoining <=:endDate) or " +
        "   ( model.dateOfConfirmation >=:startDate and model.dateOfConfirmation <=:endDate) ) order by model.pin"
    )
    List<Employee> getEmployeeProbationBetweenTimeRange(LocalDate startDate, LocalDate endDate);

    @Override
    long count();

    @Query(" SELECT model FROM Employee model " + " WHERE model.isNidVerified is null OR model.isNidVerified = false ")
    List<Employee> findEmployeeByUnapprovedNid();

    @Query(
        " select model from Employee model " +
        " where lower(model.designation.designationName) like concat('%',lower('Intern'),'%') " +
        " and model.employeeCategory <> 'INTERN'"
    )
    List<Employee> findAllInternProfileMarkedAsContractual();

    @Query(
        value = "select model from Employee model " +
        "where lower(model.fullName) like concat('%',:searchText,'%') " +
        "OR lower(model.pin) like concat('%',:searchText,'%')"
    )
    List<Employee> searchAllByFullNameAndPin(String searchText);

    @Query(
        "select es from Employee es " +
        "where lower(es.fullName) like lower(concat('%', :searchText, '%')) " +
        "or es.pin like lower(concat('%', :searchText, '%')) "
    )
    Page<Employee> findAllEmployeesByPinOrFullName(String searchText, Pageable pageable);

    @Query(
        value = "select employee from Employee employee " +
        "   where employee.id in (   " +
        "       select distinct model.employee.id from EmployeeSalary model " +
        "           where (" +
        "               model.year = :startYear and " +
        "                   (" +
        "                       model.month ='JULY'" +
        "                       or model.month ='AUGUST'" +
        "                       or model.month ='SEPTEMBER'" +
        "                       or model.month ='OCTOBER'" +
        "                       or model.month ='NOVEMBER'" +
        "                       or model.month ='DECEMBER'" +
        "                   )" +
        "               ) " +
        "           or (" +
        "               model.year= :endYear and " +
        "                   (" +
        "                       model.month = 'JANUARY'" +
        "                       or model.month = 'FEBRUARY'" +
        "                       or model.month = 'MARCH'" +
        "                       or model.month = 'APRIL'" +
        "                       or model.month = 'MAY'" +
        "                       or model.month = 'JUNE'" +
        "                   )" +
        "               )" +
        "   ) " +
        "   and employee.employeeCategory <> 'INTERN' " +
        "   ORDER BY employee.pin" +
        ""
    )
    List<Employee> getAllActiveEmployeeInFiscalYear(int startYear, int endYear);

    @Query(value = "select model.pin, model.fullName, model.tinNumber from Employee model")
    List<Employee> findAllPinAndTinNumber();

    @Query(
        value = "select case when count(model) > 0 then true else false end " + "from Employee model " + "where model.pin = :employeePin"
    )
    boolean isExistByPin(String employeePin);

    @Query(
        value = "select model from Employee model " +
        "where ( model.employeeCategory = 'REGULAR_CONFIRMED_EMPLOYEE' or model.employeeCategory = 'CONTRACTUAL_EMPLOYEE' ) " +
        "and model.id not in " +
        "       ( " +
        "           select resignation.employee.id from EmployeeResignation resignation " +
        "           where resignation.approvalStatus = 'APPROVED' and resignation.lastWorkingDay < :today" +
        "       )" +
        "order by model.pin"
    )
    List<Employee> getAllEligibleEmployeesForInsuranceRegistration(LocalDate today);

    @Query(value = " select model from Employee model where model.employeeCategory <> 'INTERN'")
    List<Employee> getAllEmployeeExceptInterns();

    @Query(
        value = " select model from Employee model " +
        "where (:employeeId is null or model.id=:employeeId) " +
        "and (" +
        "(cast(:startDate as date) is null AND cast(:endDate as date) is null) " +
        " or (model.dateOfJoining >= :startDate and model.dateOfJoining <= :endDate)" +
        ") " +
        "order by model.pin"
    )
    Page<Employee> getAllGeneralNomineeByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(
        value = "select employee From Employee employee " +
        "   where (employee.id=:employeeId or :employeeId is null) " +
        "and (" +
        "(cast(:startDate as date) is null OR cast(:endDate as date) is null) " +
        "or (employee.dateOfConfirmation >= :startDate and employee.dateOfConfirmation <= :endDate)" +
        ") " +
        "order by employee.pin"
    )
    Page<Employee> getAllGFAndPFNomineeByDateRange(Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(
        "select model from Employee model " +
        "   where model.isEligibleForAutomatedAttendance=true  " +
        "       and model.id not in " +
        "           (select resignation.employee.id from EmployeeResignation resignation where resignation.approvalStatus = 'APPROVED' and resignation.lastWorkingDay < :date) " +
        "       and model.id not in " +
        "           (select contractual.id from Employee contractual " +
        "                   where (contractual.employeeCategory = 'CONTRACTUAL_EMPLOYEE' or contractual.employeeCategory = 'INTERN') " +
        "                           and ( (contractual.contractPeriodEndDate < :date and contractual.contractPeriodExtendedTo is null) " +
        "                                   or (contractual.contractPeriodExtendedTo is not null and contractual.contractPeriodExtendedTo < :date)))"
    )
    List<Employee> findAllActiveAndEligibleForAutomatedAttendance(LocalDate date);

    @Query(
        "select model from Employee model " +
        "       where model.id in " +
        "               (select contractual.id from Employee contractual " +
        "                           where (contractual.employeeCategory = 'CONTRACTUAL_EMPLOYEE' or contractual.employeeCategory = 'INTERN') " +
        "                                   and ( (contractual.contractPeriodEndDate < :date and contractual.contractPeriodExtendedTo is null) " +
        "                                           or (contractual.contractPeriodExtendedTo is not null and contractual.contractPeriodExtendedTo < :date)))" +
        "         or model.id in " +
        "                   (select resignation.employee.id from EmployeeResignation resignation " +
        "                       where resignation.approvalStatus = 'APPROVED' and resignation.lastWorkingDay < :date)"
    )
    List<Employee> findInActiveEmployee(LocalDate date);

    @Query(
        "SELECT model FROM Employee model WHERE model.isAllowedToGiveOnlineAttendance is null or model.isAllowedToGiveOnlineAttendance = false " +
        "and (lower(model.fullName) like lower(concat('%', :searchText, '%')) or model.pin like lower(concat('%', :searchText, '%')))"
    )
    Page<Employee> getEmployeeWhereOnlineAttendanceDisable(String searchText, Pageable pageable);

    @Query(
        "SELECT model FROM Employee model WHERE model.isAllowedToGiveOnlineAttendance = true " +
        "and (lower(model.fullName) like lower(concat('%', :searchText, '%')) or model.pin like lower(concat('%', :searchText, '%')))"
    )
    Page<Employee> getEmployeeWhereOnlineAttendanceEnable(String searchText, Pageable pageable);

    @Query(
        "select es from Employee es " +
        "where lower(es.fullName) like lower(concat('%', :searchText, '%')) " +
        "or es.pin like lower(concat('%', :searchText, '%')) and es.isAllowedToGiveOnlineAttendance=:onlineAttendance"
    )
    Page<Employee> findAllEmployeesByPinOrFullNameAndOnlineAttendance(String searchText, boolean onlineAttendance, Pageable pageable);

    @Query("select count(es) from Employee es where es.isAllowedToGiveOnlineAttendance = true")
    int totalOnlineAttendanceEnabled();

    @Query(
        "select count(es) from Employee es where es.isAllowedToGiveOnlineAttendance = false or es.isAllowedToGiveOnlineAttendance is null"
    )
    int totalOnlineAttendanceDisabled();

    @Query(
        value = "select model " +
        "from Employee model " +
        "where model.reportingTo.id = :reportingToId " +
        "  and model not in (select contractual_em " +
        "                    from Employee contractual_em " +
        "                    where  contractual_em.contractPeriodEndDate is not null and contractual_em.contractPeriodEndDate < :date" +
        "                           and (" +
        "                               ( contractual_em.contractPeriodExtendedTo is null )" +
        "                            or (contractual_em.contractPeriodExtendedTo is not null and contractual_em.contractPeriodExtendedTo < :date)" +
        "                           )" +
        "                       and ( contractual_em.employeeCategory='CONTRACTUAL_EMPLOYEE' or contractual_em.employeeCategory='INTERN'))" +
        "  and model not in (select resignation.employee " +
        "                    from EmployeeResignation resignation " +
        "                    where resignation.approvalStatus = 'APPROVED' " +
        "                      and resignation.lastWorkingDay < :date) " +
        "and (lower(model.fullName) like lower(concat('%', :searchText , '%')) " +
        "or model.pin like lower(concat('%', :searchText , '%')))"
    )
    Page<Employee> getActiveReportingToByIdAndByPinOrByFullName(String searchText, Long reportingToId, LocalDate date, Pageable pageable);

    @Query(value = "select model from Employee model where model.referenceId <> null")
    List<Employee> getAllReferencePin();

    @Query(
        value = "select model from Employee model " +
        "where (" +
        "           lower(trim(model.officialEmail)) = lower(trim(:email))" +
        "           or lower(trim(model.personalEmail)) = lower(trim(:email))" +
        "      )" +
        ""
    )
    List<Employee> findByEmail(String email);

    @Modifying
    @Query(
        value = "update Employee e set e.isCurrentlyResigned =true where e.id in (SELECT e.id FROM Employee e JOIN EmployeeResignation er ON e.id = er.employee.id WHERE er.lastWorkingDay <= :now )"
    )
    void updateIsCurrentlyResignedByDate(LocalDate now);

    @Query(value = "select model from Employee model WHERE model.reportingTo = :hodId ")
    List<Long> findEmployeesReportingToHod(Long hodId);
}
