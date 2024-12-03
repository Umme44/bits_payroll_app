package com.bits.hr.web.rest;

import com.bits.hr.domain.TimeSlot;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.TimeSlotService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TimeSlotDTO;
import com.bits.hr.service.mapper.TimeSlotMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
public class TimeSlotCommonResource {

    private final TimeSlotService timeSlotService;
    private final TimeSlotMapper timeSlotMapper;

    private final Logger log = LoggerFactory.getLogger(TimeSlotCommonResource.class);

    private static final String RESOURCE_NAME = "TimeSlotCommonResource";
    private static final String ENTITY_NAME = "TimeSlot";

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public TimeSlotCommonResource(
        TimeSlotService timeSlotService,
        TimeSlotMapper timeSlotMapper,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.timeSlotService = timeSlotService;
        this.timeSlotMapper = timeSlotMapper;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @GetMapping("/time-slots")
    public ResponseEntity<List<TimeSlotDTO>> getAllTimeSlots() {
        log.debug("REST request to get a page of TimeSlots ");
        List<TimeSlot> timeSlotList = timeSlotRepository.getTimeSlotsByIsApplicableByEmployeeAndOrderByInTimeAsc();
        List<TimeSlotDTO> timeSlotDTOList = timeSlotMapper.toDto(timeSlotList);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok(timeSlotDTOList);
    }
}
