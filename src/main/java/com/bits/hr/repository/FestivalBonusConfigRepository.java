package com.bits.hr.repository;

import com.bits.hr.domain.FestivalBonusConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FestivalBonusConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FestivalBonusConfigRepository extends JpaRepository<FestivalBonusConfig, Long> {}
