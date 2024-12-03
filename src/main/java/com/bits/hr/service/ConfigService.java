package com.bits.hr.service;

import com.bits.hr.service.dto.ConfigDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Config}.
 */
public interface ConfigService {
    /**
     * Save a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigDTO save(ConfigDTO configDTO);

    /**
     * Get all the configs.
     *
     * @return the list of entities.
     */
    List<ConfigDTO> findAll();

    /**
     * Get the "id" config.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigDTO> findOne(Long id);

    /**
     * Delete the "id" config.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ConfigDTO> findOneByKey(String key);
    boolean isNIDVerificationEnabled(String key);
}
