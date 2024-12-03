package com.bits.hr.repository;

import com.bits.hr.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Area entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {}
