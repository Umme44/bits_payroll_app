package com.bits.hr.repository;

import com.bits.hr.domain.Vehicle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Vehicle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("select vehicle from Vehicle vehicle where vehicle.createdBy.login = ?#{principal.username}")
    List<Vehicle> findByCreatedByIsCurrentUser();

    @Query("select vehicle from Vehicle vehicle where vehicle.updatedBy.login = ?#{principal.username}")
    List<Vehicle> findByUpdatedByIsCurrentUser();

    @Query("select vehicle from Vehicle vehicle where vehicle.approvedBy.login = ?#{principal.username}")
    List<Vehicle> findByApprovedByIsCurrentUser();
}
