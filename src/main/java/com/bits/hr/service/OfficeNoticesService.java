package com.bits.hr.service;

import com.bits.hr.service.dto.OfficeNoticesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.OfficeNotices}.
 */
public interface OfficeNoticesService {
    /**
     * Save a officeNotices.
     *
     * @param officeNoticesDTO the entity to save.
     * @return the persisted entity.
     */
    OfficeNoticesDTO save(OfficeNoticesDTO officeNoticesDTO);

    /**
     * Get all the officeNotices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OfficeNoticesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" officeNotices.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OfficeNoticesDTO> findOne(Long id);

    /**
     * Delete the "id" officeNotices.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<OfficeNoticesDTO> findAllForUserEnd(Pageable pageable);
}
