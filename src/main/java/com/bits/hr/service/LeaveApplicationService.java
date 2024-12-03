package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.LeaveApplication}.
 */
public interface LeaveApplicationService {
    /**
     * Save a leaveApplication.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    LeaveApplicationDTO save(LeaveApplicationDTO leaveApplicationDTO);

    /**
     * Get all the leaveApplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveApplicationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leaveApplication.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaveApplicationDTO> findOne(Long id);

    /**
     * Delete the "id" leaveApplication.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    int getApplicableLeaveBalance(LeaveApplicationDTO leaveApplicationDTO);

    List<LeaveApplicationDTO> findAllLeaveApplicationsByEmployeeBetweenTwoDates(String pin, LocalDate startDate, LocalDate endDate);

    Page<LeaveApplicationDTO> findLeaveApplicationsByEmployeeBetweenDatesLeaveType(
        String pin,
        LocalDate startDate,
        LocalDate endDate,
        LeaveType leaveType,
        Pageable pageable
    );

    List<LeaveApplicationDTO> findLeaveApplicationsByEmployeeLeaveType(String pin, LeaveType leaveType);

    List<LeaveApplicationDTO> findByEmployeePin(String pin);

    List<LeaveApplicationDTO> findAllLeaveApplicationsBetweenTwoDates(LocalDate startDate, LocalDate endDate);

    boolean isInsufficientLeaveBalance(LeaveApplicationDTO leaveApplicationDTO);

    Page<LeaveApplicationDTO> findApprovedLeaveByUserId(Pageable pageable, long userId);

    Page<LeaveApplicationDTO> findApprovedLeaveByFiltering(Pageable pageable, long userId, String searchText);

    boolean findPendingAndApprovedLeaveByEmployeeIdAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate);
}
