package com.bits.hr.repository;

import com.bits.hr.domain.CalenderYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CalenderYear entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalenderYearRepository extends JpaRepository<CalenderYear, Long> {}
