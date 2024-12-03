package com.bits.hr.repository;

import com.bits.hr.domain.Floor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Floor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
    @Query("select floor from Floor floor where floor.createdBy.login = ?#{principal.username}")
    List<Floor> findByCreatedByIsCurrentUser();

    @Query("select floor from Floor floor where floor.updatedBy.login = ?#{principal.username}")
    List<Floor> findByUpdatedByIsCurrentUser();

    @Query(
        "select case when count(model) > 0 then true else false end from Floor model where model.building.id=:buildingId and concat('%',lower(model.floorName) ,'%') = concat('%',lower(:floorName) ,'%')"
    )
    Boolean checkFloorNameIsExists(Long buildingId, String floorName);

    @Query("select model from Floor model where model.building.id=:buildingId")
    List<Floor> getAllFloorsByBuildingId(Long buildingId);
}
