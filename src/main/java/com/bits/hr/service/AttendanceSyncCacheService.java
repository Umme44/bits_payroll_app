package com.bits.hr.service;

import com.bits.hr.domain.AttendanceSyncCache;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AttendanceSyncCache}.
 */
public interface AttendanceSyncCacheService {
    /**
     * Save a AttendanceSyncCache.
     *
     * @param attendanceSyncCache the entity to save.
     * @return the persisted entity.
     */
    AttendanceSyncCache save(AttendanceSyncCache attendanceSyncCache);

    /**
     * Get all the AttendanceSyncCache.
     *
     * @return the list of entities.
     */
    List<AttendanceSyncCache> findAll();

    /**
     * Get the "id" AttendanceSyncCache.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttendanceSyncCache> findOne(Long id);

    /**
     * Delete the "id" AttendanceSyncCache.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
