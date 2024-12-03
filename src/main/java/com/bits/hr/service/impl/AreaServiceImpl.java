package com.bits.hr.service.impl;

import com.bits.hr.domain.Area;
import com.bits.hr.repository.AreaRepository;
import com.bits.hr.service.AreaService;
import com.bits.hr.service.dto.AreaDTO;
import com.bits.hr.service.mapper.AreaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Area}.
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    private final Logger log = LoggerFactory.getLogger(AreaServiceImpl.class);

    private final AreaRepository areaRepository;

    private final AreaMapper areaMapper;

    public AreaServiceImpl(AreaRepository areaRepository, AreaMapper areaMapper) {
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
    }

    @Override
    public AreaDTO save(AreaDTO areaDTO) {
        log.debug("Request to save Area : {}", areaDTO);
        Area area = areaMapper.toEntity(areaDTO);
        area = areaRepository.save(area);
        return areaMapper.toDto(area);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AreaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Areas");
        return areaRepository.findAll(pageable).map(areaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AreaDTO> findOne(Long id) {
        log.debug("Request to get Area : {}", id);
        return areaRepository.findById(id).map(areaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Area : {}", id);
        areaRepository.deleteById(id);
    }
}
