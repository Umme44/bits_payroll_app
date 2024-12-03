package com.bits.hr.service;

import com.bits.hr.service.dto.FestivalDTO;
import com.bits.hr.service.search.FilterDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Festival}.
 */
public interface FestivalService {
    /**
     * Save a festival.
     *
     * @param festivalDTO the entity to save.
     * @return the persisted entity.
     */
    FestivalDTO save(FestivalDTO festivalDTO);

    /**
     * Get all the festivals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FestivalDTO> findAll(FilterDto filterDto, Pageable pageable);

    List<FestivalDTO> findAll();

    /**
     * Get the "id" festival.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FestivalDTO> findOne(Long id);

    /**
     * Delete the "id" festival.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
