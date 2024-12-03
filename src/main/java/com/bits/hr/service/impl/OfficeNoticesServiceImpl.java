package com.bits.hr.service.impl;

import com.bits.hr.domain.OfficeNotices;
import com.bits.hr.repository.OfficeNoticesRepository;
import com.bits.hr.service.OfficeNoticesService;
import com.bits.hr.service.dto.OfficeNoticesDTO;
import com.bits.hr.service.mapper.OfficeNoticesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OfficeNotices}.
 */
@Service
@Transactional
public class OfficeNoticesServiceImpl implements OfficeNoticesService {

    private final Logger log = LoggerFactory.getLogger(OfficeNoticesServiceImpl.class);

    private final OfficeNoticesRepository officeNoticesRepository;

    private final OfficeNoticesMapper officeNoticesMapper;

    public OfficeNoticesServiceImpl(OfficeNoticesRepository officeNoticesRepository, OfficeNoticesMapper officeNoticesMapper) {
        this.officeNoticesRepository = officeNoticesRepository;
        this.officeNoticesMapper = officeNoticesMapper;
    }

    @Override
    public OfficeNoticesDTO save(OfficeNoticesDTO officeNoticesDTO) {
        log.debug("Request to save OfficeNotices : {}", officeNoticesDTO);
        OfficeNotices officeNotices = officeNoticesMapper.toEntity(officeNoticesDTO);
        officeNotices = officeNoticesRepository.save(officeNotices);
        return officeNoticesMapper.toDto(officeNotices);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfficeNoticesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OfficeNotices");
        return officeNoticesRepository.findAll(pageable).map(officeNoticesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OfficeNoticesDTO> findOne(Long id) {
        log.debug("Request to get OfficeNotices : {}", id);
        return officeNoticesRepository.findById(id).map(officeNoticesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OfficeNotices : {}", id);
        officeNoticesRepository.deleteById(id);
    }

    @Override
    public Page<OfficeNoticesDTO> findAllForUserEnd(Pageable pageable) {
        return officeNoticesRepository.findAllNotices(pageable).map(officeNoticesMapper::toDto);
    }
}
