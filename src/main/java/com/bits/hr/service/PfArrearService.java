package com.bits.hr.service;

import com.bits.hr.service.dto.PfArrearDTO;
import com.bits.hr.service.search.QuickFilterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfArrear}.
 */
public interface PfArrearService {
    /**
     * Save a pfArrear.
     *
     * @param pfArrearDTO the entity to save.
     * @return the persisted entity.
     */
    PfArrearDTO save(PfArrearDTO pfArrearDTO);

    /**
     * Get all the pfArrears.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfArrearDTO> findAll(Pageable pageable);

    Page<PfArrearDTO> findAll(QuickFilterDTO quickFilterDTO, Pageable pageable);

    /**
     * Get the "id" pfArrear.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PfArrearDTO> findOne(Long id);

    /**
     * Delete the "id" pfArrear.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
