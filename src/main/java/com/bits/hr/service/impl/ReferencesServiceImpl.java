package com.bits.hr.service.impl;

import com.bits.hr.domain.References;
import com.bits.hr.repository.ReferencesRepository;
import com.bits.hr.service.ReferencesService;
import com.bits.hr.service.dto.ReferencesDTO;
import com.bits.hr.service.mapper.ReferencesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link References}.
 */
@Service
@Transactional
public class ReferencesServiceImpl implements ReferencesService {

    private final Logger log = LoggerFactory.getLogger(ReferencesServiceImpl.class);

    private final ReferencesRepository referencesRepository;

    private final ReferencesMapper referencesMapper;

    public ReferencesServiceImpl(ReferencesRepository referencesRepository, ReferencesMapper referencesMapper) {
        this.referencesRepository = referencesRepository;
        this.referencesMapper = referencesMapper;
    }

    @Override
    public ReferencesDTO save(ReferencesDTO referencesDTO) {
        log.debug("Request to save References : {}", referencesDTO);
        References references = referencesMapper.toEntity(referencesDTO);
        references = referencesRepository.save(references);
        return referencesMapper.toDto(references);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReferencesDTO> findAll() {
        log.debug("Request to get all References");
        return referencesRepository.findAll().stream().map(referencesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReferencesDTO> findOne(Long id) {
        log.debug("Request to get References : {}", id);
        return referencesRepository.findById(id).map(referencesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete References : {}", id);
        referencesRepository.deleteById(id);
    }

    @Override
    public List<ReferencesDTO> findAllByEmployee(long employeeId) {
        return referencesMapper.toDto(referencesRepository.findAllByEmployee(employeeId));
    }
}
