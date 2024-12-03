package com.bits.hr.repository;

import com.bits.hr.domain.Location;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(
        "select model " +
        "from Location model " +
        "where model.locationCode=:locationCode " +
        "and model.locationName=:locationName " +
        "and ((model.parent is null and :parentId is null ) " +
        "or (model.parent is not null and :parentId is not null and model.parent.id=:parentId))"
    )
    Optional<Location> findLocation(String locationName, String locationCode, Long parentId);

    @Query("select model " + "from Location model " + "order by model.locationType,model.locationName")
    Page<Location> findAll(Pageable pageable);
}
