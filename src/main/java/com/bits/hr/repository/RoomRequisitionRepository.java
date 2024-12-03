package com.bits.hr.repository;

import com.bits.hr.domain.RoomRequisition;
import com.bits.hr.domain.enumeration.Status;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RoomRequisition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomRequisitionRepository extends JpaRepository<RoomRequisition, Long> {
    @Query("select roomRequisition from RoomRequisition roomRequisition where roomRequisition.createdBy.login = ?#{principal.username}")
    List<RoomRequisition> findByCreatedByIsCurrentUser();

    @Query("select roomRequisition from RoomRequisition roomRequisition where roomRequisition.updatedBy.login = ?#{principal.username}")
    List<RoomRequisition> findByUpdatedByIsCurrentUser();

    @Query("select roomRequisition from RoomRequisition roomRequisition where roomRequisition.sanctionedBy.login = ?#{principal.username}")
    List<RoomRequisition> findBySanctionedByIsCurrentUser();

    @Query(
        "select model from RoomRequisition model where model.room.id=:roomId " +
        "and ( " +
        "(model.bookingStartDate<=:bookingStartDate and model.bookingEndDate>=:bookingStartDate) " +
        "or (model.bookingStartDate<=:bookingEndDate and model.bookingEndDate>=:bookingEndDate) " +
        "or (model.bookingStartDate BETWEEN :bookingStartDate and :bookingEndDate) or ( model.bookingEndDate   BETWEEN :bookingStartDate and :bookingEndDate ) " +
        "or( model.bookingStartDate  <= :bookingStartDate and model.bookingEndDate >=:bookingEndDate )" +
        ") "
    )
    List<RoomRequisition> checkRoomIsBookedV2(Long roomId, LocalDate bookingStartDate, LocalDate bookingEndDate);

    @Query("select model from RoomRequisition model where model.requester.id=:requesterId")
    List<RoomRequisition> getAllRoomRequisitionById(Long requesterId);

    @Query(
        "select model from RoomRequisition model where model.room.id=:roomId and model.requester.id<>:requesterId and  model.bookingStartDate>=:bookingStartDate"
    )
    List<RoomRequisition> checkRoomIsBookedForUpdate(Long roomId, Long requesterId, LocalDate bookingStartDate);

    @Query("select model from RoomRequisition model where model.requester.id=:requesterId and model.status=:status")
    List<RoomRequisition> getRoomRequisitionByIdAndStatus(Long requesterId, Status status);

    @Query("select model from RoomRequisition model where model.status=:status")
    List<RoomRequisition> getRoomRequisitionByIdAndStatusV2(Status status);

    @Query("select model from RoomRequisition model order by model.requester.pin")
    List<RoomRequisition> getRoomRequisitionByAll();

    @Query(
        "select model from RoomRequisition model where model.room.id=:roomId " +
        "and model.status=:status and( " +
        "(model.bookingStartDate<=:calendarDate and model.bookingEndDate>=:calendarDate) " +
        "or (model.bookingStartDate<=:calendarDate and model.bookingEndDate=:calendarDate) " +
        "or (model.bookingStartDate=:calendarDate and model.bookingEndDate>=:calendarDate) " +
        "or (model.bookingStartDate=:calendarDate and model.bookingEndDate=:calendarDate) " +
        ") "
    )
    List<RoomRequisition> getAllRoomRequisitionForThatDate(Long roomId, LocalDate calendarDate, Status status);
}
