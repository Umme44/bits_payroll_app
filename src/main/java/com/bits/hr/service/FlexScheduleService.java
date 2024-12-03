package com.bits.hr.service;

import com.bits.hr.domain.FlexSchedule;
import com.bits.hr.domain.User;
import com.bits.hr.service.dto.FlexScheduleApprovalDTO;
import com.bits.hr.service.dto.FlexScheduleDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.bits.hr.domain.FlexSchedule}.
 */
public interface FlexScheduleService {
    /**
     * Save a flexSchedule.
     *
     * @param flexScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    FlexScheduleDTO save(FlexScheduleDTO flexScheduleDTO);

    /**
     * Get all the flexSchedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FlexScheduleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" flexSchedule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FlexScheduleDTO> findOne(Long id);

    /**
     * Delete the "id" flexSchedule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    FlexSchedule getEffectiveFlexSchedule(long employeeId, LocalDate date);

    @Transactional
    boolean saveChangedFlexSchedules(FlexScheduleApprovalDTO flexScheduleApprovalDTO, User currentUser);

    LocalDateTime getStartTimeByPin(String pin, LocalDate date);

    double getOfficeTimeDurationByPin(String pin);

    List<FlexScheduleDTO> getFlexScheduleByEffectiveDates(Long employeeId, LocalDate startEffectiveDate, LocalDate endEffectiveDate);
}
