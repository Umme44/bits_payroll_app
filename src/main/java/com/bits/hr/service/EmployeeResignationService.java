package com.bits.hr.service;

import com.bits.hr.service.dto.EmployeeResignationDTO;
import java.time.LocalDate;
import java.util.Optional;
import javax.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmployeeResignation}.
 */
public interface EmployeeResignationService {
    /**
     * Save a employeeResignation.
     *
     * @param employeeResignationDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeResignationDTO save(EmployeeResignationDTO employeeResignationDTO);

    EmployeeResignationDTO processAndSaveNewResignation(EmployeeResignationDTO employeeResignationDTO);
    EmployeeResignationDTO approveResignation(EmployeeResignationDTO employeeResignationDTO);
    boolean rejectResignation(EmployeeResignationDTO employeeResignationDTO);

    /**
     * Get all the employeeResignations.
     *
     *
     * @param searchText
     * @param startDate
     * @param endDate
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeeResignationDTO> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Get the "id" employeeResignation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeResignationDTO> findOne(Long id);

    /**
     * Delete the "id" employeeResignation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     *
     * @param employeeId
     * @return notice period days
     */
    int getEmployeeNoticePeriod(long employeeId);

    /**
     * @Nullable when, employee has not approved resignation
     * @param employeeId
     * @return
     */
    @Nullable
    Optional<LocalDate> getLastWorkingDay(long employeeId);
}
