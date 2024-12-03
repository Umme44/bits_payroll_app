package com.bits.hr.service.impl;

import com.bits.hr.domain.EducationDetails;
import com.bits.hr.repository.EducationDetailsRepository;
import com.bits.hr.service.EducationDetailsService;
import com.bits.hr.service.dto.EducationDetailsDTO;
import com.bits.hr.service.mapper.EducationDetailsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EducationDetails}.
 */
@Service
@Transactional
public class EducationDetailsServiceImpl implements EducationDetailsService {

    private final Logger log = LoggerFactory.getLogger(EducationDetailsServiceImpl.class);

    private final EducationDetailsRepository educationDetailsRepository;

    private final EducationDetailsMapper educationDetailsMapper;

    public EducationDetailsServiceImpl(
        EducationDetailsRepository educationDetailsRepository,
        EducationDetailsMapper educationDetailsMapper
    ) {
        this.educationDetailsRepository = educationDetailsRepository;
        this.educationDetailsMapper = educationDetailsMapper;
    }

    @Override
    public EducationDetailsDTO save(EducationDetailsDTO educationDetailsDTO) {
        log.debug("Request to save EducationDetails : {}", educationDetailsDTO);
        EducationDetails educationDetails = educationDetailsMapper.toEntity(educationDetailsDTO);
        educationDetails = educationDetailsRepository.save(educationDetails);
        return educationDetailsMapper.toDto(educationDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationDetailsDTO> findAll() {
        log.debug("Request to get all EducationDetails");
        return educationDetailsRepository
            .findAll()
            .stream()
            .map(educationDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationDetailsDTO> findAllByEmployeeId(long employeeId) {
        log.debug("Request to get all EducationDetails");
        return educationDetailsRepository
            .findAllByEmployeeId(employeeId)
            .stream()
            .map(educationDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EducationDetailsDTO> findOne(Long id) {
        log.debug("Request to get EducationDetails : {}", id);
        return educationDetailsRepository.findById(id).map(educationDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EducationDetails : {}", id);
        educationDetailsRepository.deleteById(id);
    }
}
