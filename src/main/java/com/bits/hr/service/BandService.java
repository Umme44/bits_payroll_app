package com.bits.hr.service;

import com.bits.hr.service.dto.BandDTO;
import com.bits.hr.service.selecteable.SelectableDTO;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Band}.
 */
public interface BandService {
    /**
     * Save a band.
     *
     * @param bandDTO the entity to save.
     * @return the persisted entity.
     */
    BandDTO save(BandDTO bandDTO);

    /**
     * Get all the bands.
     *
     * @return the list of entities.
     */
    List<BandDTO> findAll();


    /**
     * Get a page of the nationalities.
     *
     * @return the list of entities.
     */
    Page<BandDTO> findAll(Pageable pageable);

    /**
     * Get the "id" band.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BandDTO> findOne(Long id);

    /**
     * Delete the "id" band.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    @Transactional(readOnly = true)
    List<SelectableDTO> findAllForCommon();
}
