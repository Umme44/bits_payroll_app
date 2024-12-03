package com.bits.hr.service.impl;

import com.bits.hr.domain.TimeSlot;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.TimeSlotService;
import com.bits.hr.service.dto.TimeSlotDTO;
import com.bits.hr.service.mapper.TimeSlotMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Service Implementation for managing {@link TimeSlot}.
 */
@Service
@Transactional
public class TimeSlotServiceImpl implements TimeSlotService {

    private final Logger log = LoggerFactory.getLogger(TimeSlotServiceImpl.class);

    private final TimeSlotRepository timeSlotRepository;

    private final TimeSlotMapper timeSlotMapper;

    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository, TimeSlotMapper timeSlotMapper) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotMapper = timeSlotMapper;
    }

    @Override
    public TimeSlotDTO save(TimeSlotDTO timeSlotDTO) {
        log.debug("Request to save TimeSlot : {}", timeSlotDTO);
        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDTO);

        if (timeSlot.getIsDefaultShift() != null && timeSlot.getIsDefaultShift()) {
            Optional<TimeSlot> defaultTimeSlot = timeSlotRepository.findDefaultTimeSlot();
            if (timeSlot.getId() != null) {
                if (defaultTimeSlot.isPresent() && !defaultTimeSlot.get().getId().equals(timeSlot.getId())) {
                    throw new BadRequestAlertException(
                        "Already Default Time Slot assigned to " +
                        defaultTimeSlot.get().getInTime() +
                        " - " +
                        defaultTimeSlot.get().getOutTime() +
                        "(" +
                        defaultTimeSlot.get().getTitle() +
                        ")",
                        "",
                        ""
                    );
                }
            } else {
                if (defaultTimeSlot.isPresent()) {
                    throw new BadRequestAlertException(
                        "Already Default Time Slot assigned to " +
                        defaultTimeSlot.get().getInTime() +
                        " - " +
                        defaultTimeSlot.get().getOutTime() +
                        "(" +
                        defaultTimeSlot.get().getTitle() +
                        ")",
                        "",
                        ""
                    );
                }
            }
        }

        List<String> timeSlotDTOWeekEndList = timeSlotDTO.getWeekEndList();
        if (timeSlotDTOWeekEndList != null && timeSlotDTOWeekEndList.size() > 0) {
            String participantIdList = timeSlotDTOWeekEndList.stream().map(String::valueOf).collect(Collectors.joining(","));
            timeSlot.setWeekEnds(participantIdList);
        } else {
            timeSlot.setWeekEnds(null);
        }

        if (timeSlot.getId() != null) {
            if (timeSlot.getCode() == null || timeSlotDTO.getCode().equals("")) {
                timeSlot = timeSlotRepository.save(timeSlot);
                String code = "TS-" + timeSlot.getId();
                timeSlot.setCode(code);
                timeSlot = timeSlotRepository.save(timeSlot);
            } else {
                timeSlot = timeSlotRepository.save(timeSlot);
            }
        } else {
            timeSlot = timeSlotRepository.save(timeSlot);
            String code = "TS-" + timeSlot.getId();
            timeSlot.setCode(code);
            timeSlot = timeSlotRepository.save(timeSlot);
        }

        return timeSlotMapper.toDto(timeSlot);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TimeSlotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TimeSlots");
        return timeSlotRepository.findAll(pageable).map(timeSlotMapper::toDto);
    }

    @Override
    public List<TimeSlotDTO> findAll() {
        log.debug("Request to get all TimeSlots");
        return timeSlotMapper.toDto(timeSlotRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeSlotDTO> findOne(Long id) {
        log.debug("Request to get TimeSlot : {}", id);

        Optional<TimeSlotDTO> timeSlotDTO = timeSlotRepository.findById(id).map(timeSlotMapper::toDto);

        if (timeSlotDTO.isPresent()) {
            if (timeSlotDTO.get().getWeekEnds() != null) {
                String[] weekEndList = timeSlotDTO.get().getWeekEnds().split(",");
                timeSlotDTO.get().setWeekEndList(Arrays.asList(weekEndList));
            } else {
                List<String> strings = new ArrayList<>();
                timeSlotDTO.get().setWeekEndList(strings);
            }
        }

        return timeSlotDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimeSlot : {}", id);
        timeSlotRepository.deleteById(id);
    }

    @Override
    public TimeSlot getDefaultTimeSlot() {
        Optional<TimeSlot> optionalTimeSlot = timeSlotRepository.findDefaultTimeSlot();
        if (optionalTimeSlot.isPresent()) {
            return optionalTimeSlot.get();
        } else {
            TimeSlot timeSlot = new TimeSlot();
            timeSlot
                .inTime(Instant.now().atZone(ZoneId.systemDefault()).withHour(10).withMinute(0).withSecond(0).toInstant())
                .outTime(Instant.now().atZone(ZoneId.systemDefault()).withHour(6).withMinute(0).withSecond(0).toInstant())
                .weekEnds("Friday,Saturday");
            return timeSlot;
        }
    }
}
