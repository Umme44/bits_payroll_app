package com.bits.hr.service.impl;

import com.bits.hr.domain.WorkingExperience;
import com.bits.hr.repository.WorkingExperienceRepository;
import com.bits.hr.service.WorkingExperienceService;
import com.bits.hr.service.dto.WorkingExperienceDTO;
import com.bits.hr.service.mapper.WorkingExperienceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkingExperience}.
 */
@Service
@Transactional
public class WorkingExperienceServiceImpl implements WorkingExperienceService {

    private final Logger log = LoggerFactory.getLogger(WorkingExperienceServiceImpl.class);

    private final WorkingExperienceRepository workingExperienceRepository;

    private final WorkingExperienceMapper workingExperienceMapper;

    public WorkingExperienceServiceImpl(
        WorkingExperienceRepository workingExperienceRepository,
        WorkingExperienceMapper workingExperienceMapper
    ) {
        this.workingExperienceRepository = workingExperienceRepository;
        this.workingExperienceMapper = workingExperienceMapper;
    }

    @Override
    public WorkingExperienceDTO save(WorkingExperienceDTO workingExperienceDTO) {
        log.debug("Request to save WorkingExperience : {}", workingExperienceDTO);
        WorkingExperience workingExperience = workingExperienceMapper.toEntity(workingExperienceDTO);
        workingExperience = workingExperienceRepository.save(workingExperience);
        return workingExperienceMapper.toDto(workingExperience);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkingExperienceDTO> findAll() {
        log.debug("Request to get all WorkingExperiences");
        return workingExperienceRepository
            .findAll()
            .stream()
            .map(workingExperienceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<WorkingExperienceDTO> findByEmployee(long employeeId) {
        log.debug("Request to get all WorkingExperiences");
        return workingExperienceRepository
            .findAllByEmployeeId(employeeId)
            .stream()
            .map(workingExperienceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkingExperienceDTO> findOne(Long id) {
        log.debug("Request to get WorkingExperience : {}", id);
        return workingExperienceRepository.findById(id).map(workingExperienceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkingExperience : {}", id);
        workingExperienceRepository.deleteById(id);
    }
}
