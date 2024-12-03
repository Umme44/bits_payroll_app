package com.bits.hr.service.impl;

import com.bits.hr.domain.CalenderYear;
import com.bits.hr.repository.CalenderYearRepository;
import com.bits.hr.service.CalenderYearService;
import com.bits.hr.service.dto.CalenderYearDTO;
import com.bits.hr.service.mapper.CalenderYearMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CalenderYear}.
 */
@Service
@Transactional
public class CalenderYearServiceImpl implements CalenderYearService {

    private final Logger log = LoggerFactory.getLogger(CalenderYearServiceImpl.class);

    private final CalenderYearRepository calenderYearRepository;

    private final CalenderYearMapper calenderYearMapper;

    public CalenderYearServiceImpl(CalenderYearRepository calenderYearRepository, CalenderYearMapper calenderYearMapper) {
        this.calenderYearRepository = calenderYearRepository;
        this.calenderYearMapper = calenderYearMapper;
    }

    @Override
    public CalenderYearDTO save(CalenderYearDTO calenderYearDTO) {
        log.debug("Request to save CalenderYear : {}", calenderYearDTO);
        CalenderYear calenderYear = calenderYearMapper.toEntity(calenderYearDTO);
        calenderYear = calenderYearRepository.save(calenderYear);
        return calenderYearMapper.toDto(calenderYear);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CalenderYearDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CalenderYears");
        return calenderYearRepository.findAll(pageable).map(calenderYearMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalenderYearDTO> findOne(Long id) {
        log.debug("Request to get CalenderYear : {}", id);
        return calenderYearRepository.findById(id).map(calenderYearMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CalenderYear : {}", id);
        calenderYearRepository.deleteById(id);
    }
}
