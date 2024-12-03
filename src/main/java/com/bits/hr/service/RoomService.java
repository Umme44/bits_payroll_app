package com.bits.hr.service;

import com.bits.hr.service.dto.RoomDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Room}.
 */
public interface RoomService {
    /**
     * Save a room.
     *
     * @param roomDTO the entity to save.
     * @return the persisted entity.
     */
    RoomDTO save(RoomDTO roomDTO);

    /**
     * Get all the rooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoomDTO> findAll(Pageable pageable);

    /**
     * Get the "id" room.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomDTO> findOne(Long id);

    /**
     * Delete the "id" room.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
