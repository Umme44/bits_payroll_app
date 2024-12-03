package com.bits.hr.repository;

import com.bits.hr.domain.InsuranceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InsuranceConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InsuranceConfigurationRepository extends JpaRepository<InsuranceConfiguration, Long> {}
