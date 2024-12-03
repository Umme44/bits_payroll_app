package com.bits.hr.service.impl;

import com.bits.hr.domain.SpecialShiftTiming;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.SpecialShiftTimingRepository;
import com.bits.hr.service.SpecialShiftTimingService;
import com.bits.hr.service.dto.SpecialShiftTimingDTO;
import com.bits.hr.service.mapper.SpecialShiftTimingMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SpecialShiftTiming}.
 */
@Service
@Transactional
public class SpecialShiftTimingServiceImpl implements SpecialShiftTimingService {

    private final Logger log = LoggerFactory.getLogger(SpecialShiftTimingServiceImpl.class);

    private final SpecialShiftTimingRepository specialShiftTimingRepository;

    private final SpecialShiftTimingMapper specialShiftTimingMapper;

    public SpecialShiftTimingServiceImpl(
        SpecialShiftTimingRepository specialShiftTimingRepository,
        SpecialShiftTimingMapper specialShiftTimingMapper
    ) {
        this.specialShiftTimingRepository = specialShiftTimingRepository;
        this.specialShiftTimingMapper = specialShiftTimingMapper;
    }

    @Override
    public SpecialShiftTimingDTO save(SpecialShiftTimingDTO specialShiftTimingDTO) {
        log.debug("Request to save SpecialShiftTiming : {}", specialShiftTimingDTO);
        SpecialShiftTiming specialShiftTiming = specialShiftTimingMapper.toEntity(specialShiftTimingDTO);
        if (specialShiftTiming.getEndDate().isBefore(specialShiftTiming.getStartDate())) {
            throw new BadRequestAlertException("Start Date cannot greater than end date", "SpecialShiftTimingDTO", "invalidDate");
        }
        specialShiftTiming = specialShiftTimingRepository.save(specialShiftTiming);
        return specialShiftTimingMapper.toDto(specialShiftTiming);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialShiftTimingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpecialShiftTimings");
        return specialShiftTimingRepository.findAll(pageable).map(specialShiftTimingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialShiftTimingDTO> findOne(Long id) {
        log.debug("Request to get SpecialShiftTiming : {}", id);
        return specialShiftTimingRepository.findById(id).map(specialShiftTimingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SpecialShiftTiming : {}", id);
        specialShiftTimingRepository.deleteById(id);
    }

    @Override
    public List<SpecialShiftTiming> findSpecialShiftByDate(LocalDate effectiveFromDate) {
        return specialShiftTimingRepository.findByStartAndEndDateBetween(effectiveFromDate);
    }
}
