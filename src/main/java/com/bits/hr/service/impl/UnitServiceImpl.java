package com.bits.hr.service.impl;

import com.bits.hr.domain.Unit;
import com.bits.hr.repository.UnitRepository;
import com.bits.hr.service.UnitService;
import com.bits.hr.service.dto.UnitDTO;
import com.bits.hr.service.mapper.UnitMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Unit}.
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService {

    private final Logger log = LoggerFactory.getLogger(UnitServiceImpl.class);

    private final UnitRepository unitRepository;

    private final UnitMapper unitMapper;

    public UnitServiceImpl(UnitRepository unitRepository, UnitMapper unitMapper) {
        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    @Override
    public UnitDTO save(UnitDTO unitDTO) {
        log.debug("Request to save Unit : {}", unitDTO);
        Unit unit = unitMapper.toEntity(unitDTO);
        unit = unitRepository.save(unit);
        return unitMapper.toDto(unit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitDTO> findAll() {
        log.debug("Request to get all Units");
        return unitRepository.findAllOrderByName().stream().map(unitMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get a page of Departments");
        return unitRepository
            .findAll(pageable)
            .map(unitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitDTO> findOne(Long id) {
        log.debug("Request to get Unit : {}", id);
        return unitRepository.findById(id).map(unitMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Unit : {}", id);
        unitRepository.deleteById(id);
    }
}
