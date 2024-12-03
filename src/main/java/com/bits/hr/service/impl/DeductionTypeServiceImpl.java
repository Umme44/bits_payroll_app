package com.bits.hr.service.impl;

import com.bits.hr.domain.DeductionType;
import com.bits.hr.repository.DeductionTypeRepository;
import com.bits.hr.service.DeductionTypeService;
import com.bits.hr.service.dto.DeductionTypeDTO;
import com.bits.hr.service.mapper.DeductionTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeductionType}.
 */
@Service
@Transactional
public class DeductionTypeServiceImpl implements DeductionTypeService {

    private final Logger log = LoggerFactory.getLogger(DeductionTypeServiceImpl.class);

    private final DeductionTypeRepository deductionTypeRepository;

    private final DeductionTypeMapper deductionTypeMapper;

    public DeductionTypeServiceImpl(DeductionTypeRepository deductionTypeRepository, DeductionTypeMapper deductionTypeMapper) {
        this.deductionTypeRepository = deductionTypeRepository;
        this.deductionTypeMapper = deductionTypeMapper;
    }

    @Override
    public DeductionTypeDTO save(DeductionTypeDTO deductionTypeDTO) {
        log.debug("Request to save DeductionType : {}", deductionTypeDTO);
        DeductionType deductionType = deductionTypeMapper.toEntity(deductionTypeDTO);
        deductionType = deductionTypeRepository.save(deductionType);
        return deductionTypeMapper.toDto(deductionType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeductionTypeDTO> findAll() {
        log.debug("Request to get all DeductionTypes");
        return deductionTypeRepository.findAll().stream().map(deductionTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeductionTypeDTO> findOne(Long id) {
        log.debug("Request to get DeductionType : {}", id);
        return deductionTypeRepository.findById(id).map(deductionTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeductionType : {}", id);
        deductionTypeRepository.deleteById(id);
    }
}
