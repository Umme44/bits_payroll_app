package com.bits.hr.service;

import com.bits.hr.service.dto.LeaveAllocationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.LeaveAllocation}.
 */
public interface LeaveAllocationService {
    /**
     * Save a leaveAllocation.
     *
     * @param leaveAllocationDTO the entity to save.
     * @return the persisted entity.
     */
    LeaveAllocationDTO save(LeaveAllocationDTO leaveAllocationDTO);

    /**
     * Get all the leaveAllocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveAllocationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leaveAllocation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaveAllocationDTO> findOne(Long id);

    /**
     * Delete the "id" leaveAllocation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
