package com.bits.hr.service.impl;

import com.bits.hr.domain.RoomType;
import com.bits.hr.repository.RoomTypeRepository;
import com.bits.hr.service.RoomTypeService;
import com.bits.hr.service.dto.RoomTypeDTO;
import com.bits.hr.service.mapper.RoomTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RoomType}.
 */
@Service
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {

    private final Logger log = LoggerFactory.getLogger(RoomTypeServiceImpl.class);

    private final RoomTypeRepository roomTypeRepository;

    private final RoomTypeMapper roomTypeMapper;

    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository, RoomTypeMapper roomTypeMapper) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomTypeMapper = roomTypeMapper;
    }

    @Override
    public RoomTypeDTO save(RoomTypeDTO roomTypeDTO) {
        log.debug("Request to save RoomType : {}", roomTypeDTO);
        RoomType roomType = roomTypeMapper.toEntity(roomTypeDTO);
        roomType = roomTypeRepository.save(roomType);
        return roomTypeMapper.toDto(roomType);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomTypes");
        return roomTypeRepository.findAll(pageable).map(roomTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomTypeDTO> findOne(Long id) {
        log.debug("Request to get RoomType : {}", id);
        return roomTypeRepository.findById(id).map(roomTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoomType : {}", id);
        roomTypeRepository.deleteById(id);
    }
}
