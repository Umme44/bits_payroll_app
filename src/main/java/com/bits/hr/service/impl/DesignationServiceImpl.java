package com.bits.hr.service.impl;

import com.bits.hr.domain.Designation;
import com.bits.hr.repository.DesignationRepository;
import com.bits.hr.service.DesignationService;
import com.bits.hr.service.dto.DesignationDTO;
import com.bits.hr.service.mapper.DesignationMapper;
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
 * Service Implementation for managing {@link Designation}.
 */
@Service
@Transactional
public class DesignationServiceImpl implements DesignationService {

    private final Logger log = LoggerFactory.getLogger(DesignationServiceImpl.class);

    private final DesignationRepository designationRepository;

    private final DesignationMapper designationMapper;

    public DesignationServiceImpl(DesignationRepository designationRepository, DesignationMapper designationMapper) {
        this.designationRepository = designationRepository;
        this.designationMapper = designationMapper;
    }

    @Override
    public DesignationDTO save(DesignationDTO designationDTO) {
        log.debug("Request to save Designation : {}", designationDTO);
        Designation designation = designationMapper.toEntity(designationDTO);
        designation = designationRepository.save(designation);
        return designationMapper.toDto(designation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignationDTO> findAll() {
        log.debug("Request to get all Designations");
        return designationRepository
            .findAllOrderByName()
            .stream()
            .map(designationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DesignationDTO> findAll(Pageable pageable) {
        log.debug("Request to get a page of Designations");
        return designationRepository
            .findAll(pageable)
            .map(designationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DesignationDTO> findOne(Long id) {
        log.debug("Request to get Designation : {}", id);
        return designationRepository.findById(id).map(designationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Designation : {}", id);
        designationRepository.deleteById(id);
    }
}
