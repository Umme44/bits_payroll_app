package com.bits.hr.service.impl;

import com.bits.hr.domain.AttendanceSyncCache;
import com.bits.hr.repository.AttendanceSyncCacheRepository;
import com.bits.hr.service.AttendanceSyncCacheService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AttendanceSyncCache}.
 */
@Service
@Transactional
public class AttendanceSyncCacheSyncCacheServiceImpl implements AttendanceSyncCacheService {

    private final Logger log = LoggerFactory.getLogger(AttendanceSyncCacheSyncCacheServiceImpl.class);

    private final AttendanceSyncCacheRepository attendanceSyncCacheRepository;

    public AttendanceSyncCacheSyncCacheServiceImpl(AttendanceSyncCacheRepository attendanceSyncCacheRepository) {
        this.attendanceSyncCacheRepository = attendanceSyncCacheRepository;
    }

    @Override
    public AttendanceSyncCache save(AttendanceSyncCache attendanceSyncCache) {
        log.debug("Request to save AttendanceSyncCache : {}", attendanceSyncCache);
        return attendanceSyncCacheRepository.save(attendanceSyncCache);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceSyncCache> findAll() {
        log.debug("Request to get all AttendanceSyncCache");
        return attendanceSyncCacheRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttendanceSyncCache> findOne(Long id) {
        log.debug("Request to get AttendanceSyncCache : {}", id);
        return attendanceSyncCacheRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AttendanceSyncCache : {}", id);
        attendanceSyncCacheRepository.deleteById(id);
    }
}
