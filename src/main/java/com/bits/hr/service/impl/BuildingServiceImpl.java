package com.bits.hr.service.impl;

import com.bits.hr.domain.Building;
import com.bits.hr.repository.BuildingRepository;
import com.bits.hr.service.BuildingService;
import com.bits.hr.service.dto.BuildingDTO;
import com.bits.hr.service.mapper.BuildingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Building}.
 */
@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {

    private final Logger log = LoggerFactory.getLogger(BuildingServiceImpl.class);

    private final BuildingRepository buildingRepository;

    private final BuildingMapper buildingMapper;

    public BuildingServiceImpl(BuildingRepository buildingRepository, BuildingMapper buildingMapper) {
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
    }

    @Override
    public BuildingDTO save(BuildingDTO buildingDTO) {
        log.debug("Request to save Building : {}", buildingDTO);
        Building building = buildingMapper.toEntity(buildingDTO);
        building = buildingRepository.save(building);
        return buildingMapper.toDto(building);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BuildingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Buildings");
        return buildingRepository.findAll(pageable).map(buildingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BuildingDTO> findOne(Long id) {
        log.debug("Request to get Building : {}", id);
        return buildingRepository.findById(id).map(buildingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Building : {}", id);
        buildingRepository.deleteById(id);
    }
}
