package com.bits.hr.service;

import com.bits.hr.service.dto.EventLogDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EventLog}.
 */
public interface EventLogService {
    /**
     * Save a eventLog.
     *
     * @param eventLogDTO the entity to save.
     * @return the persisted entity.
     */
    EventLogDTO save(EventLogDTO eventLogDTO);

    /**
     * Get all the eventLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventLogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventLogDTO> findOne(Long id);

    /**
     * Delete the "id" eventLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
