package com.bits.hr.repository;

import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.domain.enumeration.Status;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InsuranceRegistration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceRegistrationRepository extends JpaRepository<InsuranceRegistration, Long> {
    @Query(
        "select insuranceRegistration from InsuranceRegistration insuranceRegistration " +
        "where insuranceRegistration.approvedBy.login = ?#{principal.username}"
    )
    List<InsuranceRegistration> findByApprovedByIsCurrentUser();

    @Query(
        "select insuranceRegistration from InsuranceRegistration insuranceRegistration " +
        "where insuranceRegistration.createdBy.login = ?#{principal.username}"
    )
    List<InsuranceRegistration> findByCreatedByIsCurrentUser();

    @Query(
        "select insuranceRegistration from InsuranceRegistration insuranceRegistration " +
        "where insuranceRegistration.updatedBy.login = ?#{principal.username}"
    )
    List<InsuranceRegistration> findByUpdatedByIsCurrentUser();

    @Query(
        "select insuranceRegistration from InsuranceRegistration insuranceRegistration " +
        "where insuranceRegistration.employee.id = :employeeId"
    )
    List<InsuranceRegistration> findPreviousRelationsByEmployeeId(Long employeeId);

    @Query(
        "select insuranceRegistration from InsuranceRegistration insuranceRegistration " +
        "where insuranceRegistration.id = :registrationId"
    )
    InsuranceRegistration findByRegistrationId(Long registrationId);

    @Query(
        "select  DISTINCT(insuranceRegistration.employee.id) from InsuranceRegistration insuranceRegistration" +
        " where insuranceRegistration.insuranceStatus = :status " +
        "and ( lower(insuranceRegistration.employee.fullName) like concat('%',lower(:searchText),'%') " +
        "or insuranceRegistration.employee.pin like concat('%',:searchText,'%') )"
    )
    List<Long> getEmployeeListWhoHaveInsuranceRegistrationByStatus(String searchText, Status status);

    @Query(
        value = "select distinct model.employee.pin from InsuranceRegistration model " +
        "where ( " +
        "       (lower(model.name) like concat('%',lower(:searchText) ,'%')) " +
        "       or (model.employee.pin = :searchText) " +
        "      ) " +
        " and (" +
        "       (:isExcluded = true " +
        "           and " +
        "           (" +
        "               (:isCancelled = true and model.insuranceStatus = 'CANCELED') " +
        "               or (:isSeperated = true and model.insuranceStatus = 'SEPARATED') " +
        "               or (:isSeperated = false and :isCancelled = false " +
        "                 and ( model.insuranceStatus = 'CANCELED' or model.insuranceStatus = 'SEPARATED')" +
        "               )" +
        "           ) " +
        "           and " +
        "           ( " +
        "              (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null) " +
        "              or model.updatedAt >= :searchFrom and model.updatedAt <= :searchTo" +
        "           )" +
        "       ) " +
        "       or " +
        "       ( " +
        "             (:isExcluded = false " +
        "           and (:status is null " +
        "               or ( cast(:status as string ) = 'PENDING' " +
        "                   and model.insuranceStatus =:status " +
        "                   and (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null or model.createdAt >= :searchFrom and model.createdAt <= :searchTo) " +
        "                  ) " +
        "               or ( cast(:status as string ) = 'APPROVED' " +
        "                   and model.insuranceStatus =:status " +
        "                   and (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null or model.approvedAt >= :searchFrom and model.approvedAt <= :searchTo) " +
        "                  )" +
        "               or ( cast(:status as string ) = 'NOT_APPROVED' " +
        "                   and model.insuranceStatus =:status " +
        "                   and (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null or model.updatedAt >= :searchFrom and model.updatedAt <= :searchTo) " +
        "                  )" +
        "               )" +
        "             ) " +
        "           )" +
        "   ) " +
        "order by model.employee.pin" +
        ""
    )
    Page<String> getEmployeePINsUsingFilter(
        String searchText,
        Instant searchFrom,
        Instant searchTo,
        InsuranceStatus status,
        boolean isExcluded,
        boolean isCancelled,
        boolean isSeperated,
        Pageable pageable
    );

    @Query(
        value = "select model from InsuranceRegistration model " +
        "where model.employee.pin = :pin " +
        "and (" +
        "       (lower(model.name) like concat('%',lower(:searchText) ,'%')) " +
        "       or (model.employee.pin = :searchText) " +
        "    )" +
        "and " +
        "   (" +
        "     (:isExcluded = true " +
        "        and " +
        "        (" +
        "            (:isCancelled = true and model.insuranceStatus = 'CANCELED') " +
        "            or (:isSeperated = true and model.insuranceStatus = 'SEPARATED') " +
        "            or (:isSeperated = false and :isCancelled = false " +
        "              and ( model.insuranceStatus = 'CANCELED' or model.insuranceStatus = 'SEPARATED')" +
        "            )" +
        "        ) " +
        "        and " +
        "        ( " +
        "           (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null) " +
        "           or model.updatedAt >= :searchFrom and model.updatedAt <= :searchTo" +
        "        )" +
        "    ) " +
        "    or " +
        "      ( " +
        "        (:isExcluded = false " +
        "         and ((:status is null) " +
        "           or ( cast(:status as string ) = 'PENDING' " +
        "                and model.insuranceStatus =:status " +
        "                and (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null or model.createdAt >= :searchFrom and model.createdAt <= :searchTo) " +
        "              ) " +
        "           or ( cast(:status as string ) = 'APPROVED' " +
        "               and model.insuranceStatus =:status " +
        "               and (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null or model.approvedAt >= :searchFrom and model.approvedAt <= :searchTo) " +
        "              )" +
        "           or ( cast(:status as string ) = 'NOT_APPROVED' " +
        "               and model.insuranceStatus =:status " +
        "               and (cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null or model.updatedAt >= :searchFrom and model.updatedAt <= :searchTo) " +
        "              )" +
        "           )" +
        "        ) " +
        "      )" +
        "   )" +
        ""
    )
    List<InsuranceRegistration> getInsuranceRegistrationsUsingFilter(
        String pin,
        String searchText,
        Instant searchFrom,
        Instant searchTo,
        InsuranceStatus status,
        boolean isExcluded,
        boolean isCancelled,
        boolean isSeperated
    );

    @Query(
        "select  DISTINCT(insuranceRegistration.employee.id) from InsuranceRegistration insuranceRegistration" +
        " where ( lower(insuranceRegistration.employee.fullName) like concat('%',lower(:searchText),'%') " +
        "or insuranceRegistration.employee.pin like concat('%',:searchText,'%') )"
    )
    List<Long> getEmployeeListWhoHaveInsuranceRegistration(String searchText);

    @Query(
        "select insuranceRegistration from InsuranceRegistration insuranceRegistration " +
        "where insuranceRegistration.insuranceStatus = 'PENDING'"
    )
    List<InsuranceRegistration> findAllPendingRegistrations();

    @Query(value = "select model from InsuranceRegistration model " + "where model.insuranceId =:insuranceID")
    Optional<InsuranceRegistration> findDuplicate(String insuranceID);

    @Query(value = "select model from InsuranceRegistration model where model.photo = :photo")
    List<InsuranceRegistration> findByPhoto(String photo);

    @Query(value = "select model from InsuranceRegistration model where model.insuranceId =:cardId")
    Optional<InsuranceRegistration> findByInsuranceCardId(String cardId);

    @Query(
        value = "select model from InsuranceRegistration model " +
        "where model.insuranceStatus = 'PENDING' " +
        "and ( " +
        "       (lower(model.name) like concat('%',lower(:searchText) ,'%')) " +
        "       or (model.employee.pin = :searchText) " +
        "    ) " +
        "and (" +
        "       cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null " +
        "       or (model.createdAt >= :searchFrom and model.createdAt <= :searchTo)" +
        "    )" +
        "order by model.employee.pin"
    )
    List<InsuranceRegistration> findAllInclusionRegistration(String searchText, Instant searchFrom, Instant searchTo);

    @Query(
        value = "select model from InsuranceRegistration model " +
        "where model.insuranceStatus = 'APPROVED' " +
        "and ( " +
        "       (lower(model.name) like concat('%',lower(:searchText) ,'%')) " +
        "       or (model.employee.pin = :searchText) " +
        "    ) " +
        "and (" +
        "       cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null " +
        "       or (model.approvedAt >= :searchFrom and model.approvedAt <= :searchTo)" +
        "    )" +
        "order by model.employee.pin"
    )
    List<InsuranceRegistration> findAllApprovedRegistration(String searchText, Instant searchFrom, Instant searchTo);

    @Query(
        value = "select model from InsuranceRegistration model " +
        "where ( " +
        "        (:isCancelled <> false and model.insuranceStatus = 'CANCELED') " +
        "        or (:isSeperated <> false and model.insuranceStatus = 'SEPARATED') " +
        "      )" +
        "and ( " +
        "       (lower(model.name) like concat('%',lower(:searchText) ,'%')) " +
        "       or (model.employee.pin = :searchText) " +
        "    ) " +
        "and (" +
        "       cast(:searchFrom as timestamp) is null and cast(:searchTo as timestamp) is null " +
        "       or (model.updatedAt >= :searchFrom and model.updatedAt <= :searchTo)" +
        "    )" +
        "order by model.employee.pin"
    )
    List<InsuranceRegistration> findAllExcludedRegistration(
        String searchText,
        Instant searchFrom,
        Instant searchTo,
        boolean isCancelled,
        boolean isSeperated
    );

    @Query(
        value = "select distinct model.insuranceRelation from InsuranceRegistration model " +
        "where model.employee.id=:employeeId " +
        "and ( " +
        "       model.insuranceStatus = 'PENDING' or model.insuranceStatus = 'APPROVED'" +
        "    )" +
        ""
    )
    List<String> getAllRegisteredRelationsByEmployeeId(long employeeId);

    @Query(
        value = "select insuranceRegistration from InsuranceRegistration insuranceRegistration " +
        "where insuranceRegistration.employee.id = :employeeId "
    )
    List<InsuranceRegistration> findAllInsuranceRegistrationByEmployeeId(long employeeId);

    @Query(
        value = "select model from InsuranceRegistration model " +
        "where " +
        "   (model.insuranceStatus <> 'CANCELED' or model.insuranceStatus <> 'SEPARATED') " +
        "   and model.employee.id in " +
        "   ( " +
        "       select er.employee.id from EmployeeResignation er " +
        "           where er.lastWorkingDay < :today and er.approvalStatus = 'APPROVED'" +
        "   )" +
        ""
    )
    List<InsuranceRegistration> findAllInsuranceRegistrationsOfResignedEmployees(LocalDate today);

    @Query(
        value = "select model from InsuranceRegistration model " +
        "where model.insuranceRelation = 'SELF' " +
        "and model.employee.id = :employeeId"
    )
    List<InsuranceRegistration> findSelfRegistrationByEmployeeId(long employeeId);

    @Query(
        value = "select case when count(model) > 0 then false else true end " +
        "from InsuranceRegistration model " +
        "where model.insuranceId=:insuranceCardId"
    )
    boolean isInsuranceCardIdUnique(String insuranceCardId);

    @Query(
        value = "select model from InsuranceRegistration model " +
        "where model.insuranceRelation <> 'SELF' " +
        "and model.insuranceRelation <> 'SPOUSE' " +
        "and model.insuranceStatus = 'APPROVED'" +
        "and model.dateOfBirth < :date"
    )
    List<InsuranceRegistration> getListOfChildRelationsWhoHaveExceededMaxAgeLimit(LocalDate date);
}
