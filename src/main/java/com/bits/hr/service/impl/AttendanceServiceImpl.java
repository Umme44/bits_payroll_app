package com.bits.hr.service.impl;

import com.bits.hr.domain.Attendance;
import com.bits.hr.repository.AttendanceRepository;
import com.bits.hr.service.AttendanceService;
import com.bits.hr.service.dto.AttendanceDTO;
import com.bits.hr.service.mapper.AttendanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Attendance}.
 */
@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final Logger log = LoggerFactory.getLogger(AttendanceServiceImpl.class);

    private final AttendanceRepository attendanceRepository;

    private final AttendanceMapper attendanceMapper;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public AttendanceDTO save(AttendanceDTO attendanceDTO) {
        log.debug("Request to save Attendance : {}", attendanceDTO);
        Attendance attendance = attendanceMapper.toEntity(attendanceDTO);
        attendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attendances");
        return attendanceRepository.findAll(pageable).map(attendanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttendanceDTO> findOne(Long id) {
        log.debug("Request to get Attendance : {}", id);
        return attendanceRepository.findById(id).map(attendanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attendance : {}", id);
        attendanceRepository.deleteById(id);
    }
}
