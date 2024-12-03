package com.bits.hr.service;

import com.bits.hr.domain.LeaveBalance;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.service.dto.LeaveBalanceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.LeaveBalance}.
 */
public interface LeaveBalanceService {
    /**
     * Save a leaveBalance.
     *
     * @param leaveBalanceDTO the entity to save.
     * @return the persisted entity.
     */
    LeaveBalanceDTO save(LeaveBalanceDTO leaveBalanceDTO);

    /**
     * Get all the leaveBalances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveBalanceDTO> findAll(Pageable pageable);

    Page<LeaveBalanceDTO> findAllByFiltering(Pageable pageable, LeaveBalanceDTO leaveBalanceDTO);

    /**
     * Get the "id" leaveBalance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaveBalanceDTO> findOne(Long id);

    /**
     * Delete the "id" leaveBalance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean checkExists(LeaveBalanceDTO leaveBalanceDTO);

    LeaveBalanceDTO getByYearAndEmployeeIdAndLeaveType(Integer year, long employeeId, LeaveType leaveType);

    boolean validForMaternityOrPaternity(LeaveType leaveType, Gender gender);
}
