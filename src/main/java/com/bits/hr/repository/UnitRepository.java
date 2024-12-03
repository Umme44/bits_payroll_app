package com.bits.hr.repository;

import com.bits.hr.domain.Unit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Unit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findUnitByUnitNameIgnoreCase(String unitName);

    @Query("select model from Unit model order by model.unitName asc ")
    List<Unit> findAllOrderByName();
}
