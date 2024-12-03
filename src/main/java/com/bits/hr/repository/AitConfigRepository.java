package com.bits.hr.repository;

import com.bits.hr.domain.AitConfig;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AitConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AitConfigRepository extends JpaRepository<AitConfig, Long> {
    @Query(value = "select model from AitConfig  model " + "where model.startDate >=:startDate and model.endDate <=:endDate")
    List<AitConfig> findAllBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = "select model from AitConfig  model " + "where model.startDate <=:date and model.endDate >=:date")
    List<AitConfig> findAllBetweenOneDate(LocalDate date);
}
