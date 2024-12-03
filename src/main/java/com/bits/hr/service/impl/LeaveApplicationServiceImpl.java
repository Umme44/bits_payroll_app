package com.bits.hr.service.impl;

import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO.LeaveBalanceDetailViewDTO;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.LeaveBalanceDetailViewService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.mapper.LeaveApplicationMapper;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaveApplication}.
 */
@Service
@Transactional
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationServiceImpl.class);

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final LeaveDaysCalculationService leaveDaysCalculationService;
    private final LeaveBalanceDetailViewService leaveBalanceDetailViewService;
    private final EmployeeRepository employeeRepository;
    private final LeaveApplicationMapper leaveApplicationMapper;

    public LeaveApplicationServiceImpl(
        LeaveApplicationRepository leaveApplicationRepository,
        LeaveDaysCalculationService leaveDaysCalculationService,
        LeaveBalanceDetailViewService leaveBalanceDetailViewService,
        EmployeeRepository employeeRepository,
        @Lazy LeaveApplicationMapper leaveApplicationMapper
    ) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveDaysCalculationService = leaveDaysCalculationService;
        this.leaveBalanceDetailViewService = leaveBalanceDetailViewService;
        this.employeeRepository = employeeRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
    }

    @Override
    public LeaveApplicationDTO save(LeaveApplicationDTO leaveApplicationDTO) {
        log.debug("Request to save LeaveApplication : {}", leaveApplicationDTO);

        // refresh leave duration days
        int leaveDays = leaveDaysCalculationService.leaveDaysCalculation(
            leaveApplicationDTO.getStartDate(),
            leaveApplicationDTO.getEndDate()
        );
        leaveApplicationDTO.setDurationInDay(leaveDays);

        LeaveApplication leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        leaveApplication = leaveApplicationRepository.save(leaveApplication);
        return leaveApplicationMapper.toDto(leaveApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveApplicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveApplications");
        return leaveApplicationRepository.findAll(pageable).map(leaveApplicationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaveApplicationDTO> findOne(Long id) {
        log.debug("Request to get LeaveApplication : {}", id);
        return leaveApplicationRepository.findById(id).map(leaveApplicationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaveApplication : {}", id);
        leaveApplicationRepository.deleteById(id);
    }

    @Override
    public int getApplicableLeaveBalance(LeaveApplicationDTO leaveApplicationDTO) {
        int year = leaveApplicationDTO.getStartDate().getYear();

        List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService
            .getYearlyProcessedLeaveBalance(year, leaveApplicationDTO.getEmployeeId())
            .stream()
            .filter(x -> x.getLeaveType() == leaveApplicationDTO.getLeaveType())
            .collect(Collectors.toList());

        if (leaveBalanceDetailViewDTOList.size() == 0) {
            return 0;
        } else {
            // int remaining = leaveBalanceDetailViewDTOList.stream().mapToInt(LeaveBalanceDetailViewDTO::getDaysRemaining).sum();
            int daysRequested = leaveBalanceDetailViewDTOList.stream().mapToInt(LeaveBalanceDetailViewDTO::getDaysRequested).sum();
            int totalBalance =
                leaveBalanceDetailViewDTOList.stream().mapToInt(LeaveBalanceDetailViewDTO::getOpeningBalance).sum() +
                leaveBalanceDetailViewDTOList.stream().mapToInt(LeaveBalanceDetailViewDTO::getAmount).sum();
            int daysRejected = leaveBalanceDetailViewDTOList.stream().mapToInt(LeaveBalanceDetailViewDTO::getDaysCancelled).sum();
            return totalBalance - (daysRequested - daysRejected);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveApplicationDTO> findAllLeaveApplicationsByEmployeeBetweenTwoDates(String pin, LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get all LeaveApplications");
        return leaveApplicationRepository
            .findEmployeeLeaveApplicationBetweenDates(pin, startDate, endDate)
            .stream()
            .map(leaveApplicationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveApplicationDTO> findLeaveApplicationsByEmployeeBetweenDatesLeaveType(
        String pin,
        LocalDate startDate,
        LocalDate endDate,
        LeaveType leaveType,
        Pageable pageable
    ) {
        return leaveApplicationRepository
            .findLeaveApplicationsBetweenDatesAndLeaveType(pin, startDate, endDate, leaveType, pageable)
            .map(leaveApplicationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveApplicationDTO> findLeaveApplicationsByEmployeeLeaveType(String pin, LeaveType leaveType) {
        return leaveApplicationRepository
            .findLeaveApplicationByEmployeeAndLeaveType(pin, leaveType)
            .stream()
            .map(leaveApplicationMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveApplicationDTO> findByEmployeePin(String pin) {
        log.debug("Request to get all LeaveApplications");
        return leaveApplicationRepository
            .findEmployeeLeaveApplicationByEmployeePin(pin)
            .stream()
            .map(leaveApplicationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveApplicationDTO> findAllLeaveApplicationsBetweenTwoDates(LocalDate startDate, LocalDate endDate) {
        log.debug("Request to get all LeaveApplications");
        return leaveApplicationRepository
            .findLeaveApplicationBetweenDates(startDate, endDate)
            .stream()
            .map(leaveApplicationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public boolean isInsufficientLeaveBalance(LeaveApplicationDTO leaveApplicationDTO) {
        int remainingBalance = this.getApplicableLeaveBalance(leaveApplicationDTO);

        return false;
    }

    @Override
    public Page<LeaveApplicationDTO> findApprovedLeaveByUserId(Pageable pageable, long employeeId) {
        Page<LeaveApplication> leaveApplicationPage = leaveApplicationRepository.findLeaveApprovedByUserId(pageable, employeeId);
        return leaveApplicationPage.map(leaveApplicationMapper::toDto);
    }

    @Override
    public Page<LeaveApplicationDTO> findApprovedLeaveByFiltering(Pageable pageable, long userId, String searchText) {
        Page<LeaveApplication> leaveApplicationPage = leaveApplicationRepository.findLeaveApprovedByUserIdWithFilter(
            pageable,
            userId,
            searchText
        );
        return leaveApplicationPage.map(leaveApplicationMapper::toDto);
    }

    @Override
    public boolean findPendingAndApprovedLeaveByEmployeeIdAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<LeaveApplication> pendingLeaveApplication = leaveApplicationRepository.findPendingLeaveApplicationByEmployeeIdAndDateRange(
            employeeId,
            startDate,
            endDate
        );
        if (pendingLeaveApplication.size() > 0) {
            return true;
        }

        List<LeaveApplication> approvedLeaveApplications = leaveApplicationRepository.getApprovedLeaveApplicationsByEmployeeIdBetweenTwoDates(
            employeeId,
            startDate,
            endDate
        );
        if (approvedLeaveApplications.size() > 0) {
            return true;
        }

        return false;
    }
}
