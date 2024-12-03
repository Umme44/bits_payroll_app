package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.TimeSlotService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.TimeSlotDTO;
import com.bits.hr.service.scheduler.schedulingService.TimeSlotSchedulerService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.TimeSlot}.
 */
@RestController
@RequestMapping("/api/attendance-mgt")
public class TimeSlotResource {

    private final Logger log = LoggerFactory.getLogger(TimeSlotResource.class);

    private static final String RESOURCE_NAME = "TimeSlotCommonResource";
    private static final String ENTITY_NAME = "TimeSlot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeSlotService timeSlotService;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private TimeSlotSchedulerService timeSlotSchedulerService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    public TimeSlotResource(TimeSlotService timeSlotService, TimeSlotRepository timeSlotRepository) {
        this.timeSlotService = timeSlotService;
    }

    /**
     * {@code POST  /time-slots} : Create a new timeSlot.
     *
     * @param timeSlotDTO the timeSlotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeSlotDTO, or with status {@code 400 (Bad Request)} if the timeSlot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/time-slots")
    public ResponseEntity<TimeSlotDTO> createTimeSlot(@Valid @RequestBody TimeSlotDTO timeSlotDTO) throws URISyntaxException {
        log.debug("REST request to save TimeSlot : {}", timeSlotDTO);
        if (timeSlotDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeSlot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeSlotDTO result = timeSlotService.save(timeSlotDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity
            .created(new URI("/api/attendance-mgt/time-slots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/time-slots/check-duplicate-title/{title}")
    public ResponseEntity<Boolean> checkDuplicateTitle(@PathVariable String title) {
        log.debug("REST request to check duplicate TimeSlot Title : {}", title);
        Boolean check = false;
        check = timeSlotRepository.checkTitleIsExists(title);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, check, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok(check);
    }

    /**
     * {@code PUT  /time-slots} : Updates an existing timeSlot.
     *
     * @param timeSlotDTO the timeSlotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSlotDTO,
     * or with status {@code 400 (Bad Request)} if the timeSlotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeSlotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/time-slots")
    public ResponseEntity<TimeSlotDTO> updateTimeSlot(@Valid @RequestBody TimeSlotDTO timeSlotDTO) throws URISyntaxException {
        log.debug("REST request to update TimeSlot : {}", timeSlotDTO);
        if (timeSlotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TimeSlotDTO result = timeSlotService.save(timeSlotDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, timeSlotDTO, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timeSlotDTO.getId().toString()))
            .body(result);
    }

    /*    *//**
     * {@code GET  /time-slots} : get all the timeSlots.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeSlots in body.
     */
    @GetMapping("/time-slots")
    public ResponseEntity<List<TimeSlotDTO>> getAllTimeSlots(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TimeSlots");
        Page<TimeSlotDTO> page = timeSlotService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/time-slots/all")
    public ResponseEntity<List<TimeSlotDTO>> findAllTimeSlots() {
        log.debug("REST request to get a page of TimeSlots");
        List<TimeSlotDTO> timeSlotDTOS = timeSlotService.findAll();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().body(timeSlotDTOS);
    }

    /**
     * {@code GET  /time-slots/:id} : get the "id" timeSlot.
     *
     * @param id the id of the timeSlotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeSlotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/time-slots/{id}")
    public ResponseEntity<TimeSlotDTO> getTimeSlot(@PathVariable Long id) {
        log.debug("REST request to get TimeSlot : {}", id);
        Optional<TimeSlotDTO> timeSlotDTO = timeSlotService.findOne(id);
        if (!timeSlotDTO.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, timeSlotDTO, RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(timeSlotDTO);
    }

    /**
     * {@code DELETE  /time-slots/:id} : delete the "id" timeSlot.
     *
     * @param id the id of the timeSlotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/time-slots/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long id) {
        log.debug("REST request to delete TimeSlot : {}", id);
        Optional<TimeSlotDTO> timeSlotDTO = timeSlotService.findOne(id);
        timeSlotService.delete(id);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, timeSlotDTO, RequestMethod.DELETE, RESOURCE_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/time-slots/test-scheduler")
    public void checkEmptyTimeSlotScheduler() {
        log.debug("REST request to check empty time-slot scheduler ");
        timeSlotSchedulerService.checkEmptyTimeSlotCode();
    }
}
