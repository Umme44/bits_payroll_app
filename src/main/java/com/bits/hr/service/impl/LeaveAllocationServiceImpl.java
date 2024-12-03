package com.bits.hr.service.impl;

import com.bits.hr.domain.LeaveAllocation;
import com.bits.hr.repository.LeaveAllocationRepository;
import com.bits.hr.service.LeaveAllocationService;
import com.bits.hr.service.dto.LeaveAllocationDTO;
import com.bits.hr.service.mapper.LeaveAllocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaveAllocation}.
 */
@Service
@Transactional
public class LeaveAllocationServiceImpl implements LeaveAllocationService {

    private final Logger log = LoggerFactory.getLogger(LeaveAllocationServiceImpl.class);

    private final LeaveAllocationRepository leaveAllocationRepository;

    private final LeaveAllocationMapper leaveAllocationMapper;

    public LeaveAllocationServiceImpl(LeaveAllocationRepository leaveAllocationRepository, LeaveAllocationMapper leaveAllocationMapper) {
        this.leaveAllocationRepository = leaveAllocationRepository;
        this.leaveAllocationMapper = leaveAllocationMapper;
    }

    @Override
    public LeaveAllocationDTO save(LeaveAllocationDTO leaveAllocationDTO) {
        log.debug("Request to save LeaveAllocation : {}", leaveAllocationDTO);
        LeaveAllocation leaveAllocation = leaveAllocationMapper.toEntity(leaveAllocationDTO);
        leaveAllocation = leaveAllocationRepository.save(leaveAllocation);
        return leaveAllocationMapper.toDto(leaveAllocation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveAllocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveAllocations");
        return leaveAllocationRepository.findAll(pageable).map(leaveAllocationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaveAllocationDTO> findOne(Long id) {
        log.debug("Request to get LeaveAllocation : {}", id);
        return leaveAllocationRepository.findById(id).map(leaveAllocationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaveAllocation : {}", id);
        leaveAllocationRepository.deleteById(id);
    }
}
