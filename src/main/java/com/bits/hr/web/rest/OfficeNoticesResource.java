package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.NoticeStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.OfficeNoticesService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.OfficeNoticesDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.OfficeNotices}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class OfficeNoticesResource {

    private final Logger log = LoggerFactory.getLogger(OfficeNoticesResource.class);

    private static final String ENTITY_NAME = "officeNotices";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfficeNoticesService officeNoticesService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public OfficeNoticesResource(
        OfficeNoticesService officeNoticesService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.officeNoticesService = officeNoticesService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /office-notices} : Create a new officeNotices.
     *
     * @param officeNoticesDTO the officeNoticesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new officeNoticesDTO, or with status {@code 400 (Bad Request)} if the officeNotices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/office-notices")
    public ResponseEntity<OfficeNoticesDTO> createOfficeNotices(@Valid @RequestBody OfficeNoticesDTO officeNoticesDTO)
        throws URISyntaxException {
        log.debug("REST request to save OfficeNotices : {}", officeNoticesDTO);
        if (officeNoticesDTO.getId() != null) {
            throw new BadRequestAlertException("A new officeNotices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (officeNoticesDTO.getCreatedAt() == null) {
            officeNoticesDTO.setCreatedAt(LocalDate.now());
        }
        officeNoticesDTO.setStatus(NoticeStatus.PUBLISHED);
        OfficeNoticesDTO result = officeNoticesService.save(officeNoticesDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "OfficeNotices");
        return ResponseEntity
            .created(new URI("/api/employee-mgt/office-notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /office-notices} : Updates an existing officeNotices.
     *
     * @param officeNoticesDTO the officeNoticesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated officeNoticesDTO,
     * or with status {@code 400 (Bad Request)} if the officeNoticesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the officeNoticesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/office-notices")
    public ResponseEntity<OfficeNoticesDTO> updateOfficeNotices(@Valid @RequestBody OfficeNoticesDTO officeNoticesDTO)
        throws URISyntaxException {
        log.debug("REST request to update OfficeNotices : {}", officeNoticesDTO);
        if (officeNoticesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        officeNoticesDTO.setUpdatedAt(LocalDate.now());
        OfficeNoticesDTO result = officeNoticesService.save(officeNoticesDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "OfficeNotices");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, officeNoticesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /office-notices} : get all the officeNotices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of officeNotices in body.
     */
    @GetMapping("/office-notices")
    public ResponseEntity<List<OfficeNoticesDTO>> getAllOfficeNotices(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OfficeNotices");
        Page<OfficeNoticesDTO> page = officeNoticesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "OfficeNotices");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /office-notices/:id} : get the "id" officeNotices.
     *
     * @param id the id of the officeNoticesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the officeNoticesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/office-notices/{id}")
    public ResponseEntity<OfficeNoticesDTO> getOfficeNotices(@PathVariable Long id) {
        log.debug("REST request to get OfficeNotices : {}", id);
        Optional<OfficeNoticesDTO> officeNoticesDTO = officeNoticesService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (officeNoticesDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, officeNoticesDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(officeNoticesDTO);
    }

    /**
     * {@code DELETE  /office-notices/:id} : delete the "id" officeNotices.
     *
     * @param id the id of the officeNoticesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/office-notices/{id}")
    public ResponseEntity<Void> deleteOfficeNotices(@PathVariable Long id) {
        log.debug("REST request to delete OfficeNotices : {}", id);
        Optional<OfficeNoticesDTO> officeNoticesDTO = officeNoticesService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (officeNoticesDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, officeNoticesDTO.get(), RequestMethod.DELETE, "AitConfig");
        }
        officeNoticesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
