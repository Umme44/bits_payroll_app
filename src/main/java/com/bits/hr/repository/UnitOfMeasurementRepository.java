package com.bits.hr.repository;

import com.bits.hr.domain.UnitOfMeasurement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the UnitOfMeasurement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitOfMeasurementRepository extends JpaRepository<UnitOfMeasurement, Long> {
    @Query(
        "select unitOfMeasurement from UnitOfMeasurement unitOfMeasurement where unitOfMeasurement.createdBy.login = ?#{principal.username}"
    )
    List<UnitOfMeasurement> findByCreatedByIsCurrentUser();

    @Query(
        "select unitOfMeasurement from UnitOfMeasurement unitOfMeasurement where unitOfMeasurement.updatedBy.login = ?#{principal.username}"
    )
    List<UnitOfMeasurement> findByUpdatedByIsCurrentUser();

    Optional<UnitOfMeasurement> findByNameIgnoreCase(String name);
}
