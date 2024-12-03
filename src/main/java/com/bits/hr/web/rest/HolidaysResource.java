package com.bits.hr.web.rest;

import com.bits.hr.domain.Holidays;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.HolidaysRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.HolidaysService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.HolidaysDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Holidays}.
 */
@RestController
@RequestMapping("/api/attendance-mgt/holidays")
public class HolidaysResource {

    private static final String ENTITY_NAME = "holidays";
    private final Logger log = LoggerFactory.getLogger(HolidaysResource.class);
    private final HolidaysService holidaysService;
    private final HolidaysRepository holidaysRepository;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public HolidaysResource(
        HolidaysService holidaysService,
        HolidaysRepository holidaysRepository,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.holidaysService = holidaysService;
        this.holidaysRepository = holidaysRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /holidays} : Create a new holidays.
     *
     * @param holidaysDTO the holidaysDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new holidaysDTO, or with status {@code 400 (Bad Request)} if the holidays has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HolidaysDTO> createHolidays(@Valid @RequestBody HolidaysDTO holidaysDTO) throws URISyntaxException {
        log.debug("REST request to save Holidays : {}", holidaysDTO);

        //find duplicate holiday //TODO Is it mandatory to check description should be same for finding duplicate?
        Optional<Holidays> holidays = holidaysRepository.findDuplicate(
            holidaysDTO.getStartDate(),
            holidaysDTO.getEndDate(),
            holidaysDTO.getDescription(),
            holidaysDTO.getHolidayType()
        );
        if (holidays.isPresent()) {
            throw new BadRequestAlertException("Holiday Already Exists", ENTITY_NAME, "entryExists");
        }

        if (holidaysDTO.getId() != null) {
            throw new BadRequestAlertException("A new holidays cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HolidaysDTO result = holidaysService.create(holidaysDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "Holidays");
        return ResponseEntity
            .created(new URI("/api/attendance-mgt/holidays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /holidays} : Updates an existing holidays.
     *
     * @param holidaysDTO the holidaysDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holidaysDTO,
     * or with status {@code 400 (Bad Request)} if the holidaysDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the holidaysDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<HolidaysDTO> updateHolidays(@Valid @RequestBody HolidaysDTO holidaysDTO) throws URISyntaxException {
        log.debug("REST request to update Holidays : {}", holidaysDTO);

        //find duplicate holiday //TODO Is it mandatory to check description should be same for finding duplicate?
        Optional<Holidays> holidays = holidaysRepository.findDuplicate(
            holidaysDTO.getStartDate(),
            holidaysDTO.getEndDate(),
            holidaysDTO.getDescription(),
            holidaysDTO.getHolidayType()
        );
        if (holidays.isPresent()) {
            if (!holidays.get().getId().equals(holidaysDTO.getId())) {
                throw new BadRequestAlertException("Holiday Already Exists", ENTITY_NAME, "entryExists");
            }
        }

        if (holidaysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HolidaysDTO result = holidaysService.update(holidaysDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "Holidays");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, holidaysDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /holidays} : get all the holidays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of holidays in body.
     */
    @GetMapping("")
    public List<HolidaysDTO> getAllHolidays() {
        log.debug("REST request to get all Holidays");
        return holidaysService.findAll();
    }

    @GetMapping("/get-by-year/{year}")
    public List<HolidaysDTO> getAllHolidaysByYear(@PathVariable int year) {
        log.debug("REST request to get all Holidays");
        if (year == 0) {
            return holidaysService.findAll();
        } else {
            return holidaysService.findAllOfAYear(year);
        }
    }

    /**
     * {@code GET  /holidays/:id} : get the "id" holidays.
     *
     * @param id the id of the holidaysDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the holidaysDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HolidaysDTO> getHolidays(@PathVariable Long id) {
        log.debug("REST request to get Holidays : {}", id);
        Optional<HolidaysDTO> holidaysDTO = holidaysService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (holidaysDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, holidaysDTO.get(), RequestMethod.GET, "Holidays");
        }
        return ResponseUtil.wrapOrNotFound(holidaysDTO);
    }

    /**
     * {@code DELETE  /holidays/:id} : delete the "id" holidays.
     *
     * @param id the id of the holidaysDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHolidays(@PathVariable Long id) {
        log.debug("REST request to delete Holidays : {}", id);
        Optional<HolidaysDTO> holidaysDTO = holidaysService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (holidaysDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, holidaysDTO.get(), RequestMethod.DELETE, "Holidays");
        }
        holidaysService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
