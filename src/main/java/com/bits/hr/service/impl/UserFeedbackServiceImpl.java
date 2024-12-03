package com.bits.hr.service.impl;

import com.bits.hr.domain.UserFeedback;
import com.bits.hr.repository.UserFeedbackRepository;
import com.bits.hr.service.UserFeedbackService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.UserFeedbackDTO;
import com.bits.hr.service.mapper.UserFeedbackMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserFeedback}.
 */
@Service
@Transactional
public class UserFeedbackServiceImpl implements UserFeedbackService {

    private final Logger log = LoggerFactory.getLogger(UserFeedbackServiceImpl.class);

    private final UserFeedbackRepository userFeedbackRepository;

    private final UserFeedbackMapper userFeedbackMapper;

    public UserFeedbackServiceImpl(UserFeedbackRepository userFeedbackRepository, UserFeedbackMapper userFeedbackMapper) {
        this.userFeedbackRepository = userFeedbackRepository;
        this.userFeedbackMapper = userFeedbackMapper;
    }

    @Override
    public UserFeedbackDTO save(UserFeedbackDTO userFeedbackDTO) {
        log.debug("Request to save UserFeedback : {}", userFeedbackDTO);
        UserFeedback userFeedback = userFeedbackMapper.toEntity(userFeedbackDTO);
        userFeedback = userFeedbackRepository.save(userFeedback);
        return userFeedbackMapper.toDto(userFeedback);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserFeedbackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserFeedbacks");
        return userFeedbackRepository.findAll(pageable).map(userFeedbackMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserFeedbackDTO> findOne(Long id) {
        log.debug("Request to get UserFeedback : {}", id);
        return userFeedbackRepository.findById(id).map(userFeedbackMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserFeedback : {}", id);
        userFeedbackRepository.deleteById(id);
    }
}
