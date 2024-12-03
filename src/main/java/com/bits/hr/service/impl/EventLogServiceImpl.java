package com.bits.hr.service.impl;

import com.bits.hr.domain.EventLog;
import com.bits.hr.repository.EventLogRepository;
import com.bits.hr.service.EventLogService;
import com.bits.hr.service.dto.EventLogDTO;
import com.bits.hr.service.mapper.EventLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventLog}.
 */
@Service
@Transactional
public class EventLogServiceImpl implements EventLogService {

    private final Logger log = LoggerFactory.getLogger(EventLogServiceImpl.class);

    private final EventLogRepository eventLogRepository;

    private final EventLogMapper eventLogMapper;

    public EventLogServiceImpl(EventLogRepository eventLogRepository, EventLogMapper eventLogMapper) {
        this.eventLogRepository = eventLogRepository;
        this.eventLogMapper = eventLogMapper;
    }

    @Override
    public EventLogDTO save(EventLogDTO eventLogDTO) {
        log.debug("Request to save EventLog : {}", eventLogDTO);
        EventLog eventLog = eventLogMapper.toEntity(eventLogDTO);
        eventLog = eventLogRepository.save(eventLog);
        return eventLogMapper.toDto(eventLog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventLogs");
        return eventLogRepository.findAll(pageable).map(eventLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventLogDTO> findOne(Long id) {
        log.debug("Request to get EventLog : {}", id);
        return eventLogRepository.findById(id).map(eventLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventLog : {}", id);
        eventLogRepository.deleteById(id);
    }
}
