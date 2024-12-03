package com.bits.hr.service;

import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.service.dto.PfCollectionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfCollection}.
 */
public interface PfCollectionService {
    /**
     * Save a pfCollection.
     *
     * @param pfCollectionDTO the entity to save.
     * @return the persisted entity.
     */
    PfCollectionDTO create(PfCollectionDTO pfCollectionDTO);

    PfCollectionDTO update(PfCollectionDTO pfCollectionDTO);

    /**
     * Get all the pfCollections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfCollectionDTO> findAll(Pageable pageable);

    Page<PfCollectionDTO> findAll(Pageable pageable, Long pfAccountId, Integer year, Integer month, PfCollectionType collectionType);

    /**
     * Get the "id" pfCollection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PfCollectionDTO> findOne(Long id);

    /**
     * Delete the "id" pfCollection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
