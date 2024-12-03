package com.bits.hr.service.impl;

import com.bits.hr.domain.EmployeePin;
import com.bits.hr.domain.EmployeePinConfiguration;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeePinConfigurationRepository;
import com.bits.hr.repository.EmployeePinRepository;
import com.bits.hr.service.EmployeePinConfigurationService;
import com.bits.hr.service.dto.EmployeePinConfigurationDTO;
import com.bits.hr.service.mapper.EmployeePinConfigurationMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeePinConfiguration}.
 */
@Service
@Log4j2
@Transactional
public class EmployeePinConfigurationServiceImpl implements EmployeePinConfigurationService {

    private final EmployeePinConfigurationRepository employeePinConfigurationRepository;
    private final EmployeePinConfigurationMapper employeePinConfigurationMapper;
    private final EmployeePinRepository employeePinRepository;

    public EmployeePinConfigurationServiceImpl(
        EmployeePinConfigurationRepository employeePinConfigurationRepository,
        EmployeePinConfigurationMapper employeePinConfigurationMapper,
        EmployeePinRepository employeePinRepository
    ) {
        this.employeePinConfigurationRepository = employeePinConfigurationRepository;
        this.employeePinConfigurationMapper = employeePinConfigurationMapper;
        this.employeePinRepository = employeePinRepository;
    }

    @Override
    public EmployeePinConfigurationDTO create(EmployeePinConfigurationDTO employeePinConfigurationDTO, User user) {
        log.debug("Request to create EmployeePinConfiguration : {}", employeePinConfigurationDTO);

        List<EmployeePinConfiguration> employeePinConfigurations = employeePinConfigurationRepository.findByEmployeeCategory(
            employeePinConfigurationDTO.getEmployeeCategory()
        );

        if (employeePinConfigurations.size() > 0 && !employeePinConfigurations.get(0).getHasFullFilled()) {
            throw new BadRequestAlertException(
                "The Previous Configuration for this category has not been fulfilled yet.",
                "EmployeePIN",
                ""
            );
        }

        EmployeePinConfiguration config = employeePinConfigurationMapper.toEntity(employeePinConfigurationDTO);
        config.setCreatedAt(Instant.now());
        config.setCreatedBy(user);

        employeePinConfigurationRepository.save(config);
        return employeePinConfigurationMapper.toDto(config);
    }

    @Override
    public EmployeePinConfigurationDTO update(EmployeePinConfigurationDTO employeePinConfigurationDTO, User user) {
        log.debug("Request to update EmployeePinConfiguration : {}", employeePinConfigurationDTO);

        EmployeePinConfiguration config = employeePinConfigurationMapper.toEntity(employeePinConfigurationDTO);
        config.setUpdatedAt(Instant.now());
        config.setUpdatedBy(user);

        employeePinConfigurationRepository.save(config);
        return employeePinConfigurationMapper.toDto(config);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeePinConfigurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeePinConfigurations");
        return employeePinConfigurationRepository.findAllConfigurations(pageable).map(employeePinConfigurationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeePinConfigurationDTO> findOne(Long id) {
        log.debug("Request to get EmployeePinConfiguration : {}", id);
        return employeePinConfigurationRepository.findById(id).map(employeePinConfigurationMapper::toDto);
    }

    @Override
    public List<EmployeePinConfigurationDTO> findByEmployeeCategory(EmployeeCategory employeeCategory) {
        log.debug("Request to get EmployeePinConfiguration by employee category : {}");
        List<EmployeePinConfiguration> employeePinConfigurations = employeePinConfigurationRepository.findByEmployeeCategory(
            employeeCategory
        );

        if (employeePinConfigurations.size() > 1) {
            List<EmployeePinConfiguration> configs = new ArrayList<>();
            configs.add(employeePinConfigurations.get(0));
            return employeePinConfigurationMapper.toDto(configs);
        } else {
            return employeePinConfigurationMapper.toDto(employeePinConfigurations);
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeePinConfiguration : {}", id);
        employeePinConfigurationRepository.deleteById(id);
    }

    @Override
    public boolean isPinSequenceUnique(String startingPin, String endingPin, Long pinConfigurationId) {
        List<EmployeePinConfiguration> employeePinConfigurations = employeePinConfigurationRepository.findByPinSequence(
            startingPin.trim(),
            endingPin.trim()
        );

        if (employeePinConfigurations.size() == 0) {
            return true;
        } else if (employeePinConfigurations.size() == 1) {
            if (employeePinConfigurations.get(0).getId().equals(pinConfigurationId)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isPinSequenceFullFilled(EmployeeCategory category) {
        try {
            List<EmployeePinConfiguration> configurations = employeePinConfigurationRepository.findByEmployeeCategory(category);

            if (configurations.size() == 0) {
                throw new BadRequestAlertException("Pin Configuration Not Found", "EmployeePinConfiguration", "notFound");
            }

            boolean result = isTheSequenceFullFilled(configurations.get(0));
            return result;
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    //    @Override
    //    public Optional<String> getLastCreatedPinFromTheSequence(EmployeeCategory category) {
    //        try{
    //            List<EmployeePinConfiguration> configurations = employeePinConfigurationRepository.findByEmployeeCategory(category);
    //
    //            if(configurations.size() == 0){
    //                throw new BadRequestAlertException("Pin Configuration Not Found", "EmployeePinConfiguration", "notFound");
    //            }
    //
    //            String lastCreatedPin = getLastCreatedPin(configurations.get(configurations.size()-1));
    //
    //            if(lastCreatedPin == null){
    //                return Optional.empty();
    //            } else {
    //                return Optional.of(lastCreatedPin);
    //            }
    //        } catch (Exception e){
    //            log.error(e);
    //            throw new RuntimeException();
    //        }
    //    }

    @Override
    public String getTheLastPinFromTheSequence(EmployeePinConfiguration config) {
        try {
            String start = config.getSequenceStart();
            String end = config.getSequenceEnd();
            Pageable pageable = PageRequest.of(0, 1);

            Page<EmployeePin> employeePinPage = employeePinRepository.getLastPinFromTheSequence(start, end, pageable);

            List<EmployeePin> employeePins = employeePinPage.getContent();
            if (employeePins.size() == 0) {
                return null;
            } else {
                return employeePins.get(0).getPin();
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    @Override
    public String getTheLastCreatedPinFromTheSequence(EmployeePinConfiguration config) {
        try {
            String start = config.getSequenceStart();
            String end = config.getSequenceEnd();
            Pageable pageable = PageRequest.of(0, 1);

            Page<EmployeePin> employeePinPage = employeePinRepository.getLastCreatedPinFromTheSequence(start, end, pageable);

            List<EmployeePin> employeePins = employeePinPage.getContent();
            if (employeePins.size() == 0) {
                return null;
            } else {
                return employeePins.get(0).getPin();
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    @Override
    public boolean isTheSequenceFullFilled(EmployeePinConfiguration config) {
        try {
            String lastPinOfTheSequence = config.getSequenceEnd();
            String lastPinFromTheSequence = getTheLastPinFromTheSequence(config);

            if (lastPinOfTheSequence.equals(lastPinFromTheSequence)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException();
        }
    }

    @Override
    public void updatePinConfigurationInformation(EmployeePinConfiguration employeePinConfiguration) {
        String theLastPinFromTheSequence = getTheLastPinFromTheSequence(employeePinConfiguration);
        String theLastCreatedPinFromTheSequence = getTheLastCreatedPinFromTheSequence(employeePinConfiguration);

        employeePinConfiguration.setLastSequence(theLastPinFromTheSequence);
        employeePinConfiguration.setLastCreatedPin(theLastCreatedPinFromTheSequence);

        boolean result = isTheSequenceFullFilled(employeePinConfiguration);
        employeePinConfiguration.setHasFullFilled(result);

        employeePinConfigurationRepository.save(employeePinConfiguration);
    }
}
