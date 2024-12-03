package com.bits.hr.repository;

import com.bits.hr.domain.Config;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Config entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    Optional<Config> findConfigByKey(String configKey);
}
