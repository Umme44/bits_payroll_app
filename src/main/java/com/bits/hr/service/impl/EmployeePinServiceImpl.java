package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeePin;
import com.bits.hr.domain.EmployeePinConfiguration;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeePinConfigurationRepository;
import com.bits.hr.repository.EmployeePinRepository;
import com.bits.hr.service.EmployeePinConfigurationService;
import com.bits.hr.service.EmployeePinService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeePinDTO;
import com.bits.hr.service.mapper.EmployeePinMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeePin}.
 */
@Service
@Transactional
public class EmployeePinServiceImpl implements EmployeePinService {

    private final Logger log = LoggerFactory.getLogger(EmployeePinServiceImpl.class);

    private final EmployeePinRepository employeePinRepository;

    private final EmployeePinMapper employeePinMapper;

    private final CurrentEmployeeService currentEmployeeService;

    private final EmployeePinConfigurationService employeePinConfigurationService;

    private final EmployeePinConfigurationRepository employeePinConfigurationRepository;

    public EmployeePinServiceImpl(
        EmployeePinRepository employeePinRepository,
        EmployeePinMapper employeePinMapper,
        CurrentEmployeeService currentEmployeeService,
        EmployeePinConfigurationService employeePinConfigurationService,
        EmployeePinConfigurationRepository employeePinConfigurationRepository
    ) {
        this.employeePinRepository = employeePinRepository;
        this.employeePinMapper = employeePinMapper;
        this.currentEmployeeService = currentEmployeeService;
        this.employeePinConfigurationService = employeePinConfigurationService;
        this.employeePinConfigurationRepository = employeePinConfigurationRepository;
    }

    @Override
    public EmployeePinDTO create(EmployeePinDTO employeePinDTO, User user) {
        log.debug("Request to save EmployeePin : {}", employeePinDTO);

        Optional<EmployeePin> employeePinOptional = employeePinRepository.findByPin(employeePinDTO.getPin().trim());
        List<EmployeePinConfiguration> configurations = employeePinConfigurationRepository.findByEmployeeCategory(
            employeePinDTO.getEmployeeCategory()
        );

        if (employeePinOptional.isPresent()) {
            throw new BadRequestAlertException("PIN must be unique", "Employee Pin", "pinAlreadyExists");
        }

        employeePinDTO.setCreatedAt(Instant.now());
        employeePinDTO.setCreatedById(user.getId());

        EmployeePin employeePin = employeePinMapper.toEntity(employeePinDTO);
        employeePin = employeePinRepository.save(employeePin);

        Long sequenceStart = Long.parseLong(configurations.get(0).getSequenceStart().trim());
        Long sequenceEnd = Long.parseLong(configurations.get(0).getSequenceEnd().trim());

        if (Long.parseLong(employeePinDTO.getPin()) >= sequenceStart && Long.parseLong(employeePinDTO.getPin()) <= sequenceEnd) {
            employeePinConfigurationService.updatePinConfigurationInformation(configurations.get(0));
        }

        return employeePinMapper.toDto(employeePin);
    }

    @Override
    public EmployeePinDTO update(EmployeePinDTO employeePinDTO, User user) {
        log.debug("Request to save EmployeePin : {}", employeePinDTO);

        Optional<EmployeePin> employeePinOptional = employeePinRepository.findById(employeePinDTO.getId());
        List<EmployeePinConfiguration> configurations = employeePinConfigurationRepository.findByEmployeeCategory(
            employeePinDTO.getEmployeeCategory()
        );

        if (!employeePinOptional.isPresent()) {
            throw new BadRequestAlertException("Employee PIN Not Found!", "Employee Pin", "idnull");
        }

        if (!employeePinDTO.getEmployeePinStatus().equals(EmployeePinStatus.CREATED)) {
            throw new BadRequestAlertException("Pin status should be CREATED", "Employee Pin", "accessForbidden");
        }

        employeePinDTO.setUpdatedAt(Instant.now());
        employeePinDTO.setUpdatedById(user.getId());

        EmployeePin employeePin = employeePinMapper.toEntity(employeePinDTO);

        Long sequenceStart = Long.parseLong(configurations.get(0).getSequenceStart().trim());
        Long sequenceEnd = Long.parseLong(configurations.get(0).getSequenceEnd().trim());

        if (Long.parseLong(employeePinDTO.getPin()) >= sequenceStart && Long.parseLong(employeePinDTO.getPin()) <= sequenceEnd) {
            employeePinConfigurationService.updatePinConfigurationInformation(configurations.get(0));
        }

        employeePin = employeePinRepository.save(employeePin);
        return employeePinMapper.toDto(employeePin);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeePinDTO> findAll(
        String searchText,
        EmployeeCategory selectedCategory,
        EmployeePinStatus pinStatus,
        Pageable pageable
    ) {
        log.debug("Request to get all EmployeePins");
        return employeePinRepository
            .findEmployeePinsUsingFilter(searchText, selectedCategory, pinStatus, pageable)
            .map(employeePinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeePinDTO> findOne(Long id) {
        log.debug("Request to get EmployeePin : {}", id);
        return employeePinRepository.findById(id).map(employeePinMapper::toDto);
    }

    @Override
    public Optional<EmployeePinDTO> findOneByPin(String pin) {
        return employeePinRepository.findOneByPin(pin).map(employeePinMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeePin : {}", id);

        Optional<EmployeePin> employeePinOptional = employeePinRepository.findById(id);
        if (!employeePinOptional.isPresent()) {
            throw new BadRequestAlertException("Employee PIN not found", "EmployeePIN", "notFound");
        }

        List<EmployeePinConfiguration> employeePinConfigurations = employeePinConfigurationRepository.findByEmployeeCategory(
            employeePinOptional.get().getEmployeeCategory()
        );

        employeePinRepository.deleteById(id);

        employeePinConfigurationService.updatePinConfigurationInformation(employeePinConfigurations.get(0));
    }

    public EmployeePinDTO declineEmployeeOnboard(Long id, User user) {
        log.debug("Request to decline Employee onboarding : {}", id);

        Optional<EmployeePin> employeePinOptional = employeePinRepository.findById(id);
        if (!employeePinOptional.isPresent()) {
            throw new BadRequestAlertException("Employee PIN not found", "EmployeePIN", "notFound");
        }

        employeePinOptional.get().setEmployeePinStatus(EmployeePinStatus.NOT_JOINED);
        employeePinOptional.get().setUpdatedAt(Instant.now());
        employeePinOptional.get().setUpdatedBy(user);

        return employeePinMapper.toDto(employeePinRepository.save(employeePinOptional.get()));
    }

    @Override
    public boolean isPinUnique(String pin, Long employeePinId) {
        Optional<EmployeePin> employeePinOptional = employeePinRepository.findByPin(pin.trim());

        if (employeePinOptional.isPresent()) {
            if (employeePinId != null) {
                if (employeePinOptional.get().getId().equals(employeePinId)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void updateEmployeePinStatus(Employee employee) {
        Optional<EmployeePin> employeePinOptional = employeePinRepository.findByPin(employee.getPin());
        if (!employeePinOptional.isPresent()) {
            return;
        }
        User user = currentEmployeeService.getCurrentUser().get();
        if (employeePinOptional.get().getEmployeePinStatus().equals(EmployeePinStatus.CREATED)) {
            employeePinOptional.get().setEmployeePinStatus(EmployeePinStatus.JOINED);
            employeePinOptional.get().setFullName(employee.getFullName());
            employeePinOptional.get().setDepartment(employee.getDepartment());
            employeePinOptional.get().setDesignation(employee.getDesignation());
            employeePinOptional.get().setUnit(employee.getUnit());

            employeePinOptional.get().setUpdatedAt(Instant.now());
            employeePinOptional.get().setUpdatedBy(user);

            employeePinRepository.save(employeePinOptional.get());
        } else if (employeePinOptional.get().getEmployeePinStatus().equals(EmployeePinStatus.JOINED)) {
            employeePinOptional.get().setFullName(employee.getFullName());
            employeePinOptional.get().setDepartment(employee.getDepartment());
            employeePinOptional.get().setDesignation(employee.getDesignation());
            employeePinOptional.get().setUnit(employee.getUnit());

            employeePinOptional.get().setUpdatedAt(Instant.now());
            employeePinOptional.get().setUpdatedBy(user);

            employeePinRepository.save(employeePinOptional.get());
        }
    }

    @Override
    public String getThePreviousPin(String pin) {
        Pageable pageable = PageRequest.of(0, 5);

        List<EmployeePin> thePreviousPinList = employeePinRepository.getThePreviousPin(pin.trim(), pageable);

        if (thePreviousPinList.size() > 0) {
            return thePreviousPinList.get(0).getPin();
        } else {
            return "XX";
        }
    }

    @Override
    public boolean isEmployeeExistByRRF(String rrfNumber) {
        try{
            return employeePinRepository.isEmployeePinExistByRrfNumber(rrfNumber);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return true;
        }
    }
}
