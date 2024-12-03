package com.bits.hr.service;

import com.bits.hr.service.dto.FestivalBonusConfigDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.FestivalBonusConfig}.
 */
public interface FestivalBonusConfigService {
    /**
     * Save a festivalBonusConfig.
     *
     * @param festivalBonusConfigDTO the entity to save.
     * @return the persisted entity.
     */
    FestivalBonusConfigDTO save(FestivalBonusConfigDTO festivalBonusConfigDTO);

    /**
     * Get all the festivalBonusConfigs.
     *
     * @return the list of entities.
     */
    List<FestivalBonusConfigDTO> findAll();

    /**
     * Get the "id" festivalBonusConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FestivalBonusConfigDTO> findOne(Long id);

    /**
     * Delete the "id" festivalBonusConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
