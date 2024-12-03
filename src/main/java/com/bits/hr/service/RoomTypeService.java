package com.bits.hr.service;

import com.bits.hr.service.dto.RoomTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.RoomType}.
 */
public interface RoomTypeService {
    /**
     * Save a roomType.
     *
     * @param roomTypeDTO the entity to save.
     * @return the persisted entity.
     */
    RoomTypeDTO save(RoomTypeDTO roomTypeDTO);

    /**
     * Get all the roomTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoomTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" roomType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomTypeDTO> findOne(Long id);

    /**
     * Delete the "id" roomType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
