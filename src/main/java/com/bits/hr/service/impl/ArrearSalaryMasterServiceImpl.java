package com.bits.hr.service.impl;

import com.bits.hr.domain.ArrearSalaryMaster;
import com.bits.hr.repository.ArrearSalaryMasterRepository;
import com.bits.hr.service.ArrearSalaryMasterService;
import com.bits.hr.service.dto.ArrearSalaryMasterDTO;
import com.bits.hr.service.mapper.ArrearSalaryMasterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ArrearSalaryMaster}.
 */
@Service
@Transactional
public class ArrearSalaryMasterServiceImpl implements ArrearSalaryMasterService {

    private final Logger log = LoggerFactory.getLogger(ArrearSalaryMasterServiceImpl.class);

    private final ArrearSalaryMasterRepository arrearSalaryMasterRepository;

    private final ArrearSalaryMasterMapper arrearSalaryMasterMapper;

    public ArrearSalaryMasterServiceImpl(
        ArrearSalaryMasterRepository arrearSalaryMasterRepository,
        ArrearSalaryMasterMapper arrearSalaryMasterMapper
    ) {
        this.arrearSalaryMasterRepository = arrearSalaryMasterRepository;
        this.arrearSalaryMasterMapper = arrearSalaryMasterMapper;
    }

    @Override
    public ArrearSalaryMasterDTO save(ArrearSalaryMasterDTO arrearSalaryMasterDTO) {
        log.debug("Request to save ArrearSalaryMaster : {}", arrearSalaryMasterDTO);
        ArrearSalaryMaster arrearSalaryMaster = arrearSalaryMasterMapper.toEntity(arrearSalaryMasterDTO);
        arrearSalaryMaster = arrearSalaryMasterRepository.save(arrearSalaryMaster);
        return arrearSalaryMasterMapper.toDto(arrearSalaryMaster);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArrearSalaryMasterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ArrearSalaryMasters");
        return arrearSalaryMasterRepository.findAllActive(pageable).map(arrearSalaryMasterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArrearSalaryMasterDTO> findOne(Long id) {
        log.debug("Request to get ArrearSalaryMaster : {}", id);
        return arrearSalaryMasterRepository.findById(id).map(arrearSalaryMasterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArrearSalaryMaster : {}", id);
        arrearSalaryMasterRepository.deleteById(id);
    }
}
