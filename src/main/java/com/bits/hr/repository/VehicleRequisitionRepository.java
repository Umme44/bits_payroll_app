package com.bits.hr.repository;

import com.bits.hr.domain.VehicleRequisition;
import com.bits.hr.domain.enumeration.Status;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the VehicleRequisition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRequisitionRepository extends JpaRepository<VehicleRequisition, Long> {
    @Query(
        "select vehicleRequisition from VehicleRequisition vehicleRequisition where vehicleRequisition.createdBy.login = ?#{principal.username}"
    )
    List<VehicleRequisition> findByCreatedByIsCurrentUser();

    @Query(
        "select vehicleRequisition from VehicleRequisition vehicleRequisition where vehicleRequisition.updatedBy.login = ?#{principal.username}"
    )
    List<VehicleRequisition> findByUpdatedByIsCurrentUser();

    @Query(
        "select vehicleRequisition from VehicleRequisition vehicleRequisition where vehicleRequisition.approvedBy.login = ?#{principal.username}"
    )
    List<VehicleRequisition> findByApprovedByIsCurrentUser();

    @Query("select vehicleRequisition from VehicleRequisition vehicleRequisition where vehicleRequisition.requester.id = :employeeId")
    List<VehicleRequisition> findAllVehicleRequisitionByEmployeeID(Long employeeId);

    @Query(
        "select vehicleRequisition from VehicleRequisition vehicleRequisition" +
        " where vehicleRequisition.status = :status " +
        "and " +
        "(( vehicleRequisition.startDate <= :startDate and vehicleRequisition.endDate >= :startDate )" +
        "or (vehicleRequisition.startDate <= :endDate and vehicleRequisition.endDate >= :endDate ))"
    )
    List<VehicleRequisition> findAllPendingVehicleRequisition(LocalDate startDate, LocalDate endDate, Status status);

    @Query(
        "select vehicleRequisition from VehicleRequisition vehicleRequisition" +
        " where vehicleRequisition.status = :status " +
        "and " +
        "(( vehicleRequisition.startDate <=:startDate and vehicleRequisition.endDate >=:startDate )" +
        "or (vehicleRequisition.startDate <= :endDate and vehicleRequisition.endDate >= :endDate ))"
    )
    List<VehicleRequisition> findAllApprovedRequisitionBetweenTwoDate(LocalDate startDate, LocalDate endDate, Status status);

    @Query(
        "select vehicleRequisition from VehicleRequisition vehicleRequisition" +
        " where vehicleRequisition.startDate <=: date and vehicleRequisition.endDate >= :date "
    )
    List<VehicleRequisition> findAllRequisitionByDate(LocalDate date);
}
