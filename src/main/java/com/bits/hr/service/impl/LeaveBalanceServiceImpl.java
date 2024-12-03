package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveBalance;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveBalanceRepository;
import com.bits.hr.service.LeaveBalanceService;
import com.bits.hr.service.dto.LeaveBalanceDTO;
import com.bits.hr.service.mapper.LeaveBalanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaveBalance}.
 */
@Service
@Transactional
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final Logger log = LoggerFactory.getLogger(LeaveBalanceServiceImpl.class);

    private final LeaveBalanceRepository leaveBalanceRepository;

    private final LeaveBalanceMapper leaveBalanceMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    public LeaveBalanceServiceImpl(LeaveBalanceRepository leaveBalanceRepository, LeaveBalanceMapper leaveBalanceMapper) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveBalanceMapper = leaveBalanceMapper;
    }

    @Override
    public LeaveBalanceDTO save(LeaveBalanceDTO leaveBalanceDTO) {
        log.debug("Request to save LeaveBalance : {}", leaveBalanceDTO);
        LeaveBalance leaveBalance = leaveBalanceMapper.toEntity(leaveBalanceDTO);
        leaveBalance = leaveBalanceRepository.save(leaveBalance);
        return leaveBalanceMapper.toDto(leaveBalance);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveBalanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveBalances");
        return leaveBalanceRepository.findAll(pageable).map(leaveBalanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveBalanceDTO> findAllByFiltering(Pageable pageable, LeaveBalanceDTO leaveBalanceDTO) {
        log.debug("Request to get all LeaveBalances");
        String employeePin = "";
        if (leaveBalanceDTO.getEmployeeId() != null) {
            Optional<Employee> employee = employeeRepository.findById(leaveBalanceDTO.getEmployeeId());
            if (employee.isPresent()) {
                employeePin = employee.get().getPin();
            }
        }

        Page<LeaveBalance> leaveBalanceDTOS = leaveBalanceRepository.findAllByFiltering(
            pageable,
            employeePin,
            leaveBalanceDTO.getLeaveType(),
            leaveBalanceDTO.getYear()
        );
        return leaveBalanceDTOS.map(leaveBalanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaveBalanceDTO> findOne(Long id) {
        log.debug("Request to get LeaveBalance : {}", id);
        return leaveBalanceRepository.findById(id).map(leaveBalanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaveBalance : {}", id);
        leaveBalanceRepository.deleteById(id);
    }

    //    @Override
    //    @Transactional(readOnly = true)
    //    public List<LeaveBalanceUserViewDTO> findAllByYearAndPin(Integer year, String pin) {
    //        log.debug("Request to get all LeaveBalances");
    //        return leaveBalanceRepository.findAllByPinAndYear(year,pin)
    //            .stream()
    //            .map(leaveBalanceMapper::toDto)
    //            .collect(Collectors.toCollection(LinkedList::new));
    //    }

    @Override
    public boolean checkExists(LeaveBalanceDTO leaveBalanceDTO) {
        return leaveBalanceRepository.existsByEmployeeIdAndLeaveTypeAndYear(
            leaveBalanceDTO.getEmployeeId(),
            leaveBalanceDTO.getLeaveType(),
            leaveBalanceDTO.getYear()
        );
    }

    @Override
    public LeaveBalanceDTO getByYearAndEmployeeIdAndLeaveType(Integer year, long employeeId, LeaveType leaveType) {
        LeaveBalance leaveBalance = leaveBalanceRepository.findAllByYearAndEmployeeIdAndLeaveType(year, employeeId, leaveType).get();
        return leaveBalanceMapper.toDto(leaveBalance);
    }

    @Override
    public boolean validForMaternityOrPaternity(LeaveType leaveType, Gender gender) {
        if (gender == Gender.FEMALE && leaveType == LeaveType.NON_MENTIONABLE_PATERNITY_LEAVE) {
            return false;
        } else if (gender == Gender.MALE && leaveType == LeaveType.NON_MENTIONABLE_MATERNITY_LEAVE) {
            return false;
        } else {
            return true;
        }
    }
}
