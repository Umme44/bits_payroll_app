package com.bits.hr.service;

import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.FlexScheduleApplication}.
 */
public interface FlexScheduleApplicationService {
    /**
     * Save a flexScheduleApplication.
     *
     * @param flexScheduleApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    FlexScheduleApplicationDTO save(FlexScheduleApplicationDTO flexScheduleApplicationDTO);

    /**
     * Get all the flexScheduleApplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FlexScheduleApplicationDTO> findAll(Pageable pageable);

    Page<FlexScheduleApplicationDTO> findAll(
        Long employeeId,
        List<Long> timeSlotIdList,
        LocalDate startDate,
        LocalDate endDate,
        Status status,
        Pageable pageable
    );

    /**
     * Get the "id" flexScheduleApplication.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FlexScheduleApplicationDTO> findOne(Long id);

    /**
     * Delete the "id" flexScheduleApplication.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<FlexScheduleApplicationDTO> findAllByRequesterId(Pageable pageable, long requesterId);
}
