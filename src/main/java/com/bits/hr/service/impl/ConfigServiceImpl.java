package com.bits.hr.service.impl;

import com.bits.hr.domain.Config;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.service.ConfigService;
import com.bits.hr.service.dto.ConfigDTO;
import com.bits.hr.service.mapper.ConfigMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Config}.
 */
@Service
@Transactional
public class ConfigServiceImpl implements ConfigService {

    private final Logger log = LoggerFactory.getLogger(ConfigServiceImpl.class);

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    public ConfigServiceImpl(ConfigRepository configRepository, ConfigMapper configMapper) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
    }

    @Override
    public ConfigDTO save(ConfigDTO configDTO) {
        log.debug("Request to save Config : {}", configDTO);
        Config config = configMapper.toEntity(configDTO);
        config = configRepository.save(config);
        return configMapper.toDto(config);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConfigDTO> findAll() {
        log.debug("Request to get all Configs");
        return configRepository.findAll().stream().map(configMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfigDTO> findOne(Long id) {
        log.debug("Request to get Config : {}", id);
        return configRepository.findById(id).map(configMapper::toDto);
    }

    @Override
    public Optional<ConfigDTO> findOneByKey(String key) {
        return configRepository.findConfigByKey(key).map(configMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Config : {}", id);
        configRepository.deleteById(id);
    }

    @Override
    public boolean isNIDVerificationEnabled(String key){
        Optional<ConfigDTO> configDTO = findOneByKey(key);
        if(!configDTO.isPresent()){
            ConfigDTO newConfigDTO = new ConfigDTO();
            newConfigDTO.setKey(key);
            newConfigDTO.setValue("FALSE");

            Config config = configMapper.toEntity(newConfigDTO);
            configRepository.save(config);
            return true;
        }
        else{
            if(configDTO.get().getValue().equals("FALSE")){
                return false;
            }
            else return true;
        }
    }
}
