package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.service.EmployeeResignationService;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.dto.EmployeeResignationDTO;
import com.bits.hr.service.event.EmployeeResignationEvent;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.mapper.EmployeeResignationMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeResignation}.
 */
@Service
@Transactional
public class EmployeeResignationServiceImpl implements EmployeeResignationService {

    private final Logger log = LoggerFactory.getLogger(EmployeeResignationServiceImpl.class);

    private final EmployeeResignationRepository employeeResignationRepository;

    private final EmployeeRepository employeeRepository;

    private final GetConfigValueByKeyService getConfigValueByKeyService;

    private final EmployeeResignationMapper employeeResignationMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    public EmployeeResignationServiceImpl(
        EmployeeResignationRepository employeeResignationRepository,
        EmployeeRepository employeeRepository,
        @Lazy EmployeeResignationMapper employeeResignationMapper,
        GetConfigValueByKeyService getConfigValueByKeyService,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.employeeResignationRepository = employeeResignationRepository;
        this.employeeRepository = employeeRepository;
        this.employeeResignationMapper = employeeResignationMapper;
        this.getConfigValueByKeyService = getConfigValueByKeyService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public EmployeeResignationDTO save(EmployeeResignationDTO employeeResignationDTO) {
        log.debug("Request to save EmployeeResignation : {}", employeeResignationDTO);
        EmployeeResignation employeeResignation = employeeResignationMapper.toEntity(employeeResignationDTO);
        employeeResignation = employeeResignationRepository.save(employeeResignation);
        return employeeResignationMapper.toDto(employeeResignation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResignationDTO> findAll(String searchText, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Request to get all EmployeeResignations");
        return employeeResignationRepository.findAll(searchText, startDate, endDate, pageable).map(employeeResignationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeResignationDTO> findOne(Long id) {
        log.debug("Request to get EmployeeResignation : {}", id);
        return employeeResignationRepository.findById(id).map(employeeResignationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeResignation : {}", id);
        employeeResignationRepository.deleteById(id);
    }

    @Override
    public EmployeeResignationDTO processAndSaveNewResignation(EmployeeResignationDTO employeeResignationDTO) {
        log.debug("Request to initiating new employee resignation EmployeeResignation : {}", employeeResignationDTO);

        if (employeeResignationDTO.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(employeeResignationDTO.getEmployeeId()).get();
            employee.setEmploymentStatus(EmploymentStatus.HOLD);
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
            removeDuplicateEntry(employeeResignationDTO.getEmployeeId());
        }
        employeeResignationDTO.setApprovalStatus(Status.PENDING);
        EmployeeResignation employeeResignation = employeeResignationMapper.toEntity(employeeResignationDTO);
        employeeResignation = employeeResignationRepository.save(employeeResignation);
        return employeeResignationMapper.toDto(employeeResignation);
    }

    @Override
    public EmployeeResignationDTO approveResignation(EmployeeResignationDTO employeeResignationDTO) {
        if (employeeResignationDTO.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(employeeResignationDTO.getEmployeeId()).get();
            employee.setEmploymentStatus(EmploymentStatus.RESIGNED);
            employee.setUpdatedAt(LocalDateTime.now());
            employee.setLastWorkingDay(employeeResignationDTO.getLastWorkingDay());
            employeeRepository.save(employee);
            removeDuplicateEntry(employee.getId(), Status.NOT_APPROVED);
            removeDuplicateEntry(employee.getId(), Status.PENDING);
        }
        employeeResignationDTO.setApprovalStatus(Status.APPROVED);
        EmployeeResignation employeeResignation = employeeResignationMapper.toEntity(employeeResignationDTO);
        removeDuplicateEntry(employeeResignationDTO.getEmployeeId(), employeeResignationDTO.getId());
        employeeResignation = employeeResignationRepository.save(employeeResignation);
        //publishRelieverEvent(employeeResignationDTO,EventType.APPROVED);
        return employeeResignationMapper.toDto(employeeResignation);
    }

    @Override
    public boolean rejectResignation(EmployeeResignationDTO employeeResignationDTO) {
        try {
            employeeResignationDTO.setApprovalStatus(Status.NOT_APPROVED);
            if (employeeResignationDTO.getEmployeeId() != null) {
                Employee employee = employeeRepository.findById(employeeResignationDTO.getEmployeeId()).get();
                employee.setEmploymentStatus(EmploymentStatus.ACTIVE);
                employee.setLastWorkingDay(null);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
            //        EmployeeResignation employeeResignation = employeeResignationMapper.toEntity(employeeResignationDTO);
            //        removeDuplicateEntry(employeeResignationDTO.getEmployeeId(), employeeResignationDTO.getId());
            //        employeeResignation = employeeResignationRepository.save(employeeResignation);

            // delete all resignation entry for that employee
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findEmployeeResignationByEmployeeId(
                employeeResignationDTO.getEmployeeId()
            );
            for (EmployeeResignation er : employeeResignationList) {
                employeeResignationRepository.delete(er);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public int getEmployeeNoticePeriod(long employeeId) {
        EmployeeCategory employeeCategory = employeeRepository.findById(employeeId).get().getEmployeeCategory();
        return getConfigValueByKeyService.getNoticePeriodInDays(employeeCategory);
    }

    @Override
    public Optional<LocalDate> getLastWorkingDay(long employeeId) {
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            employeeId
        );

        if (employeeResignationList.size() > 0) {
            return Optional.of(employeeResignationList.get(0).getLastWorkingDay());
        } else {
            return Optional.empty();
        }
    }

    private boolean removeDuplicateEntry(long employeeId, long exceptionEntryId) {
        try {
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findEmployeeResignationByEmployeeId(
                employeeId
            );
            for (EmployeeResignation employeeResignation : employeeResignationList) {
                if (employeeResignation.getId() != exceptionEntryId) {
                    employeeResignationRepository.deleteById(employeeResignation.getId());
                }
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            return false;
        }
    }

    private boolean removeDuplicateEntry(long employeeId, Status status) {
        try {
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findResignationByEmpIdAndStatus(
                employeeId,
                status
            );
            for (EmployeeResignation employeeResignation : employeeResignationList) {
                employeeResignationRepository.deleteById(employeeResignation.getId());
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            return false;
        }
    }

    private boolean removeDuplicateEntry(long employeeId) {
        try {
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findEmployeeResignationByEmployeeId(
                employeeId
            );
            for (EmployeeResignation employeeResignation : employeeResignationList) {
                employeeResignationRepository.deleteById(employeeResignation.getId());
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            return false;
        }
    }

    // this temp process wil not run after a fixed date for n time based on config
    //
    // it will be responsible to fix all the entity where last working day not exist.
    private void tempProcess() {
        /*
         * todo
         * get all employee resignation
         * if there are resignation date and last working day present --> skip
         * if resignation date missing but last working day present --> resignationDate = last Working Day - 60 days
         * if resignation date exist but last working day not present --> last working day = resignation day + 60 days.
         * if both not exist , delete that entry
         * if for same employee multiple resignation entry exist , keep approved one, delete all others.
         * */
    }

    private void publishRelieverEvent(EmployeeResignationDTO employeeResignationDTO, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        EmployeeResignationEvent employeeResignationEvent = new EmployeeResignationEvent(this, employeeResignationDTO, event);
        applicationEventPublisher.publishEvent(employeeResignationEvent);
    }
}
