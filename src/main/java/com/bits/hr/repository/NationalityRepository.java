package com.bits.hr.repository;

import com.bits.hr.domain.Nationality;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Nationality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Long> {
    @Query("SELECT model FROM Nationality model WHERE model.nationalityName = :nationalityName")
    Optional<Nationality> findNationalityByNationalityName(@Param("nationalityName") String nationalityName);
}
