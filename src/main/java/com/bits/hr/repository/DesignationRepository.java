package com.bits.hr.repository;

import com.bits.hr.domain.Designation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Designation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    @Query("SELECT model FROM Designation model WHERE model.designationName = :designationName")
    Optional<Designation> findDesignationByDesignationName(@Param("designationName") String designationName);

    @Query("select model from Designation model order by model.designationName asc")
    List<Designation> findAllOrderByName();
}
