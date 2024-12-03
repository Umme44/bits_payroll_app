package com.bits.hr.service;

import com.bits.hr.domain.TimeSlot;
import com.bits.hr.service.dto.TimeSlotDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.TimeSlot}.
 */
public interface TimeSlotService {
    /**
     * Save a timeSlot.
     *
     * @param timeSlotDTO the entity to save.
     * @return the persisted entity.
     */
    TimeSlotDTO save(TimeSlotDTO timeSlotDTO);

    /**
     * Get all the timeSlots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TimeSlotDTO> findAll(Pageable pageable);
    List<TimeSlotDTO> findAll();

    /**
     * Get the "id" timeSlot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimeSlotDTO> findOne(Long id);

    /**
     * Delete the "id" timeSlot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    TimeSlot getDefaultTimeSlot();
}
