package com.bits.hr.service;

import com.bits.hr.service.dto.AitConfigDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.AitConfig}.
 */
public interface AitConfigService {
    /**
     * Save a aitConfig.
     *
     * @param aitConfigDTO the entity to save.
     * @return the persisted entity.
     */
    AitConfigDTO save(AitConfigDTO aitConfigDTO);

    /**
     * Get all the aitConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AitConfigDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aitConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AitConfigDTO> findOne(Long id);

    /**
     * Delete the "id" aitConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<AitConfigDTO> findByYearAndMonth(int year, int month);
}
