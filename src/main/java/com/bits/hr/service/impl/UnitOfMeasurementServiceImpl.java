package com.bits.hr.service.impl;

import com.bits.hr.domain.UnitOfMeasurement;
import com.bits.hr.repository.UnitOfMeasurementRepository;
import com.bits.hr.service.UnitOfMeasurementService;
import com.bits.hr.service.dto.UnitOfMeasurementDTO;
import com.bits.hr.service.mapper.UnitOfMeasurementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UnitOfMeasurement}.
 */
@Service
@Transactional
public class UnitOfMeasurementServiceImpl implements UnitOfMeasurementService {

    private final Logger log = LoggerFactory.getLogger(UnitOfMeasurementServiceImpl.class);

    private final UnitOfMeasurementRepository unitOfMeasurementRepository;

    private final UnitOfMeasurementMapper unitOfMeasurementMapper;

    public UnitOfMeasurementServiceImpl(
        UnitOfMeasurementRepository unitOfMeasurementRepository,
        UnitOfMeasurementMapper unitOfMeasurementMapper
    ) {
        this.unitOfMeasurementRepository = unitOfMeasurementRepository;
        this.unitOfMeasurementMapper = unitOfMeasurementMapper;
    }

    @Override
    public UnitOfMeasurementDTO save(UnitOfMeasurementDTO unitOfMeasurementDTO) {
        log.debug("Request to save UnitOfMeasurement : {}", unitOfMeasurementDTO);
        UnitOfMeasurement unitOfMeasurement = unitOfMeasurementMapper.toEntity(unitOfMeasurementDTO);
        unitOfMeasurement = unitOfMeasurementRepository.save(unitOfMeasurement);
        return unitOfMeasurementMapper.toDto(unitOfMeasurement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitOfMeasurementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UnitOfMeasurements");
        return unitOfMeasurementRepository.findAll(pageable).map(unitOfMeasurementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitOfMeasurementDTO> findOne(Long id) {
        log.debug("Request to get UnitOfMeasurement : {}", id);
        return unitOfMeasurementRepository.findById(id).map(unitOfMeasurementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UnitOfMeasurement : {}", id);
        unitOfMeasurementRepository.deleteById(id);
    }

    @Override
    public boolean isNameUnique(Long id, String name) {
        Optional<UnitOfMeasurement> unitOfMeasurement = unitOfMeasurementRepository.findByNameIgnoreCase(name);
        if (!unitOfMeasurement.isPresent()) {
            return true;
        } else {
            if (id == null) {
                return false;
            } else {
                return unitOfMeasurement.get().getId().equals(id);
            }
        }
    }
}
