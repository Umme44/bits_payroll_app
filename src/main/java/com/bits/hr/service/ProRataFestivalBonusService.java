package com.bits.hr.service;

import com.bits.hr.service.dto.ProRataFestivalBonusDTO;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ProRataFestivalBonus}.
 */
public interface ProRataFestivalBonusService {
    /**
     * Save a proRataFestivalBonus.
     *
     * @param proRataFestivalBonusDTO the entity to save.
     * @return the persisted entity.
     */
    ProRataFestivalBonusDTO save(ProRataFestivalBonusDTO proRataFestivalBonusDTO);

    /**
     * Get all the proRataFestivalBonuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProRataFestivalBonusDTO> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Get the "id" proRataFestivalBonus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProRataFestivalBonusDTO> findOne(Long id);

    /**
     * Delete the "id" proRataFestivalBonus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
