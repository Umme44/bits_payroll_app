package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.WorkFromHomeApplication}.
 */
public interface WorkFromHomeApplicationService {
    /**
     * Save a workFromHomeApplication.
     *
     * @param workFromHomeApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    WorkFromHomeApplicationDTO save(WorkFromHomeApplicationDTO workFromHomeApplicationDTO);

    /**
     * Get all the workFromHomeApplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkFromHomeApplicationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workFromHomeApplication.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkFromHomeApplicationDTO> findOne(Long id);

    /**
     * Delete the "id" workFromHomeApplication.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    boolean checkIsApplied(Long employeeId, LocalDate bookingStartDate, LocalDate bookingEndDate);

    boolean checkIsAppliedForUpdate(Long employeeId, Long workApplicationId, LocalDate bookingStartDate, LocalDate bookingEndDate);

    Page<WorkFromHomeApplicationDTO> getAllAppliedApplicationsByEmployeeId(long employeeId, Pageable pageable);
}
