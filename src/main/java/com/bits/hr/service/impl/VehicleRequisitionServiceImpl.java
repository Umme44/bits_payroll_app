package com.bits.hr.service.impl;

import com.bits.hr.domain.VehicleRequisition;
import com.bits.hr.repository.VehicleRequisitionRepository;
import com.bits.hr.service.VehicleRequisitionService;
import com.bits.hr.service.dto.VehicleRequisitionDTO;
import com.bits.hr.service.mapper.VehicleRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VehicleRequisition}.
 */
@Service
@Transactional
public class VehicleRequisitionServiceImpl implements VehicleRequisitionService {

    private final Logger log = LoggerFactory.getLogger(VehicleRequisitionServiceImpl.class);

    private final VehicleRequisitionRepository vehicleRequisitionRepository;

    private final VehicleRequisitionMapper vehicleRequisitionMapper;

    public VehicleRequisitionServiceImpl(
        VehicleRequisitionRepository vehicleRequisitionRepository,
        VehicleRequisitionMapper vehicleRequisitionMapper
    ) {
        this.vehicleRequisitionRepository = vehicleRequisitionRepository;
        this.vehicleRequisitionMapper = vehicleRequisitionMapper;
    }

    @Override
    public VehicleRequisitionDTO save(VehicleRequisitionDTO vehicleRequisitionDTO) {
        log.debug("Request to save VehicleRequisition : {}", vehicleRequisitionDTO);
        VehicleRequisition vehicleRequisition = vehicleRequisitionMapper.toEntity(vehicleRequisitionDTO);
        vehicleRequisition = vehicleRequisitionRepository.save(vehicleRequisition);
        return vehicleRequisitionMapper.toDto(vehicleRequisition);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleRequisitions");
        return vehicleRequisitionRepository.findAll(pageable).map(vehicleRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleRequisitionDTO> findOne(Long id) {
        log.debug("Request to get VehicleRequisition : {}", id);
        return vehicleRequisitionRepository.findById(id).map(vehicleRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleRequisition : {}", id);
        vehicleRequisitionRepository.deleteById(id);
    }
}
