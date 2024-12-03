package com.bits.hr.repository;

import com.bits.hr.domain.EmployeePinConfiguration;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the EmployeePinConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeePinConfigurationRepository extends JpaRepository<EmployeePinConfiguration, Long> {
    @Query(
        "select employeePinConfiguration from EmployeePinConfiguration employeePinConfiguration where employeePinConfiguration.createdBy.login = ?#{principal.username}"
    )
    List<EmployeePinConfiguration> findByCreatedByIsCurrentUser();

    @Query(
        "select employeePinConfiguration from EmployeePinConfiguration employeePinConfiguration where employeePinConfiguration.updatedBy.login = ?#{principal.username}"
    )
    List<EmployeePinConfiguration> findByUpdatedByIsCurrentUser();

    @Query("select model from EmployeePinConfiguration model order by model.createdAt desc ")
    Page<EmployeePinConfiguration> findAllConfigurations(Pageable pageable);

    @Query(
        "select model from EmployeePinConfiguration model " +
        "where ( " +
        "       CAST(model.sequenceStart AS long ) <= CAST(:startPin AS long ) " +
        "       and CAST(model.sequenceEnd AS long ) >= CAST(:startPin AS long ) " +
        "      )" +
        "or    ( " +
        "       CAST(model.sequenceStart AS long ) <= CAST(:endPin AS long) " +
        "       and CAST(model.sequenceEnd AS long ) >= CAST(:endPin AS long ) " +
        "      ) " +
        "or    ( " +
        "       CAST(model.sequenceStart AS long ) >= CAST(:startPin AS long) " +
        "       and CAST(model.sequenceEnd AS long ) <= CAST(:endPin AS long ) " +
        "      ) " +
        "order by model.createdAt desc" +
        ""
    )
    List<EmployeePinConfiguration> findByPinSequence(String startPin, String endPin);

    @Query(
        "select model from EmployeePinConfiguration model " + "where model.employeeCategory =:category " + "order by model.createdAt desc "
    )
    List<EmployeePinConfiguration> findByEmployeeCategory(EmployeeCategory category);
}
