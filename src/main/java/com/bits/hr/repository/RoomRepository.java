package com.bits.hr.repository;

import com.bits.hr.domain.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Room entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select room from Room room where room.createdBy.login = ?#{principal.username}")
    List<Room> findByCreatedByIsCurrentUser();

    @Query("select room from Room room where room.updatedBy.login = ?#{principal.username}")
    List<Room> findByUpdatedByIsCurrentUser();

    @Query(
        "select case when count(model) > 0 then true else false end from Room model where model.building.id=:buildingId and  model.floor.id=:floorId and concat('%',lower(model.roomName) ,'%') = concat('%',lower(:roomName) ,'%')"
    )
    Boolean checkRoomNameIsExists(Long buildingId, Long floorId, String roomName);
}
