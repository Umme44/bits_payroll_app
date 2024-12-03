package com.bits.hr.service;

import com.bits.hr.service.dto.NationalityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Nationality}.
 */
public interface NationalityService {
    /**
     * Save a nationality.
     *
     * @param nationalityDTO the entity to save.
     * @return the persisted entity.
     */
    NationalityDTO save(NationalityDTO nationalityDTO);

    /**
     * Get all the nationalities.
     *
     * @return the list of entities.
     */
    List<NationalityDTO> findAll();

    /**
     * Get a page of the nationalities.
     *
     * @return the list of entities.
     */
    Page<NationalityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" nationality.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NationalityDTO> findOne(Long id);

    /**
     * Delete the "id" nationality.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
