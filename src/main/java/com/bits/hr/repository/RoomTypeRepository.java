package com.bits.hr.repository;

import com.bits.hr.domain.RoomType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RoomType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    @Query("select roomType from RoomType roomType where roomType.createdBy.login = ?#{principal.username}")
    List<RoomType> findByCreatedByIsCurrentUser();

    @Query("select roomType from RoomType roomType where roomType.updatedBy.login = ?#{principal.username}")
    List<RoomType> findByUpdatedByIsCurrentUser();

    @Query(
        "select case when count(model) > 0 then true else false end from RoomType model where concat('%',lower(model.typeName) ,'%') = concat('%',lower(:typeName) ,'%')"
    )
    Boolean checkTypeNameIsExists(String typeName);
}
