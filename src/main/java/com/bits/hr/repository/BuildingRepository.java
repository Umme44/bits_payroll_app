package com.bits.hr.repository;

import com.bits.hr.domain.Building;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Building entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    @Query("select building from Building building where building.createdBy.login = ?#{principal.username}")
    List<Building> findByCreatedByIsCurrentUser();

    @Query("select building from Building building where building.updatedBy.login = ?#{principal.username}")
    List<Building> findByUpdatedByIsCurrentUser();

    @Query(
        "select case when count(model) > 0 then true else false end from Building model where concat('%',lower(model.buildingName),'%') = concat('%',lower(:buildingName),'%')"
    )
    Boolean checkTypeNameIsExists(String buildingName);
}
