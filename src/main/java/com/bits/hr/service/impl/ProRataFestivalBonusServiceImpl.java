package com.bits.hr.service.impl;

import com.bits.hr.domain.ProRataFestivalBonus;
import com.bits.hr.repository.ProRataFestivalBonusRepository;
import com.bits.hr.service.ProRataFestivalBonusService;
import com.bits.hr.service.dto.ProRataFestivalBonusDTO;
import com.bits.hr.service.mapper.ProRataFestivalBonusMapper;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProRataFestivalBonus}.
 */
@Service
@Transactional
public class ProRataFestivalBonusServiceImpl implements ProRataFestivalBonusService {

    private final Logger log = LoggerFactory.getLogger(ProRataFestivalBonusServiceImpl.class);

    private final ProRataFestivalBonusRepository proRataFestivalBonusRepository;

    private final ProRataFestivalBonusMapper proRataFestivalBonusMapper;

    public ProRataFestivalBonusServiceImpl(
        ProRataFestivalBonusRepository proRataFestivalBonusRepository,
        ProRataFestivalBonusMapper proRataFestivalBonusMapper
    ) {
        this.proRataFestivalBonusRepository = proRataFestivalBonusRepository;
        this.proRataFestivalBonusMapper = proRataFestivalBonusMapper;
    }

    @Override
    public ProRataFestivalBonusDTO save(ProRataFestivalBonusDTO proRataFestivalBonusDTO) {
        log.debug("Request to save ProRataFestivalBonus : {}", proRataFestivalBonusDTO);
        ProRataFestivalBonus proRataFestivalBonus = proRataFestivalBonusMapper.toEntity(proRataFestivalBonusDTO);
        proRataFestivalBonus = proRataFestivalBonusRepository.save(proRataFestivalBonus);
        return proRataFestivalBonusMapper.toDto(proRataFestivalBonus);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProRataFestivalBonusDTO> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Request to get all ProRataFestivalBonuses");
        return proRataFestivalBonusRepository.findAll(searchText, startDate, endDate, pageable).map(proRataFestivalBonusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProRataFestivalBonusDTO> findOne(Long id) {
        log.debug("Request to get ProRataFestivalBonus : {}", id);
        return proRataFestivalBonusRepository.findById(id).map(proRataFestivalBonusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProRataFestivalBonus : {}", id);
        proRataFestivalBonusRepository.deleteById(id);
    }
}
