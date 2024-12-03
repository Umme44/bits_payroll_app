package com.bits.hr.service.impl;

import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.TimeSlot;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.repository.SpecialShiftTimingRepository;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.FlexScheduleApplicationService;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import com.bits.hr.service.mapper.FlexScheduleApplicationMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FlexScheduleApplication}.
 */
@Service
@Transactional
public class FlexScheduleApplicationServiceImpl implements FlexScheduleApplicationService {

    private final Logger log = LoggerFactory.getLogger(FlexScheduleApplicationServiceImpl.class);

    private final FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    private final FlexScheduleApplicationMapper flexScheduleApplicationMapper;

    private final EmployeeRepository employeeRepository;

    private final SpecialShiftTimingRepository specialShiftTimingRepository;
    private final TimeSlotRepository timeSlotRepository;

    public FlexScheduleApplicationServiceImpl(
        FlexScheduleApplicationRepository flexScheduleApplicationRepository,
        FlexScheduleApplicationMapper flexScheduleApplicationMapper,
        EmployeeRepository employeeRepository,
        SpecialShiftTimingRepository specialShiftTimingRepository,
        TimeSlotRepository timeSlotRepository
    ) {
        this.flexScheduleApplicationRepository = flexScheduleApplicationRepository;
        this.flexScheduleApplicationMapper = flexScheduleApplicationMapper;
        this.employeeRepository = employeeRepository;
        this.specialShiftTimingRepository = specialShiftTimingRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public FlexScheduleApplicationDTO save(FlexScheduleApplicationDTO flexScheduleApplicationDTO) {
        log.debug("Request to save FlexScheduleApplication : {}", flexScheduleApplicationDTO);

        //find any pending, approved flex schedule application between effective dates
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findFlexScheduleApplicationBetweenEffectiveDate(
            flexScheduleApplicationDTO.getEffectiveFrom(),
            flexScheduleApplicationDTO.getEffectiveTo(),
            flexScheduleApplicationDTO.getRequesterId()
        );

        if (flexScheduleApplicationDTO.getId() != null) {
            Optional<FlexScheduleApplicationDTO> flexScheduleApplicationDTO1 = findOne(flexScheduleApplicationDTO.getId());
            if (!flexScheduleApplicationDTO1.isPresent()) throw new BadRequestAlertException(
                "Flex Schedule Not Found By Id: " + flexScheduleApplicationDTO.getId(),
                "FlexScheduleApplication",
                "notFound"
            );

            if (flexScheduleApplicationList.size() > 1) {
                throw new BadRequestAlertException(
                    "Already applied Flex Schedule between the effective dates.",
                    "FlexScheduleApplication",
                    ""
                );
            }
        } else {
            if (!flexScheduleApplicationList.isEmpty()) {
                throw new BadRequestAlertException(
                    "Already applied Flex Schedule between the effective dates.",
                    "FlexScheduleApplication",
                    ""
                );
            }
        }
        FlexScheduleApplication flexScheduleApplication = flexScheduleApplicationMapper.toEntity(flexScheduleApplicationDTO);
        flexScheduleApplication = flexScheduleApplicationRepository.save(flexScheduleApplication);
        return flexScheduleApplicationMapper.toDto(flexScheduleApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlexScheduleApplicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FlexScheduleApplications");
        return flexScheduleApplicationRepository.findAll(pageable).map(flexScheduleApplicationMapper::toDto);
    }

    @Override
    public Page<FlexScheduleApplicationDTO> findAll(
        Long employeeId,
        List<Long> timeSlotIdList,
        LocalDate startDate,
        LocalDate endDate,
        Status status,
        Pageable pageable
    ) {
        if (timeSlotIdList == null || timeSlotIdList.isEmpty()) {
            timeSlotIdList = timeSlotRepository.findAll().stream().map(TimeSlot::getId).collect(Collectors.toList());
        }
        Page<FlexScheduleApplication> flexScheduleApplicationDTOList = flexScheduleApplicationRepository.findFlexApplicationBetweenDates(
            employeeId,
            timeSlotIdList,
            startDate,
            endDate,
            status,
            pageable
        );

        return flexScheduleApplicationDTOList.map(flexScheduleApplicationMapper::toDto);
    }

    @Override
    public Page<FlexScheduleApplicationDTO> findAllByRequesterId(Pageable pageable, long requesterId) {
        return flexScheduleApplicationRepository.findAllByRequesterId(pageable, requesterId).map(flexScheduleApplicationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FlexScheduleApplicationDTO> findOne(Long id) {
        log.debug("Request to get FlexScheduleApplication : {}", id);
        return flexScheduleApplicationRepository.findById(id).map(flexScheduleApplicationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FlexScheduleApplication : {}", id);
        flexScheduleApplicationRepository.deleteById(id);
    }
}
