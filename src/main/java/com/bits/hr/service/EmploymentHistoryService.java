package com.bits.hr.service;

import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmploymentHistory}.
 */
public interface EmploymentHistoryService {
    /**
     * Save a employmentHistory.
     *
     * @param employmentHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    EmploymentHistoryDTO save(EmploymentHistoryDTO employmentHistoryDTO);

    /**
     * Get all the employmentHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmploymentHistoryDTO> findAll(Pageable pageable);

    Page<EmploymentHistoryDTO> findAll(EventType eventType, Pageable pageable);
    Page<EmploymentHistoryDTO> findAll(EventType eventType, Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Get the "id" employmentHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmploymentHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" employmentHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<EmploymentHistoryDTO> getEmploymentHistoryByEmployeePinBetweenTwoDates(
        String pin,
        LocalDate startDate,
        LocalDate endDate,
        EventType eventType
    );

    List<EmploymentHistoryDTO> getEmploymentHistoryByEmployeePin(String pin, EventType eventType);

    List<EmploymentHistoryDTO> getEmploymentHistoryBetweenTwoDates(LocalDate startDate, LocalDate endDate, EventType eventType);

    Optional<EmploymentHistoryDTO> findOneByIdAndEvent(Long id, EventType eventType);
}
