package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.SpecialShiftTiming;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.service.FlexScheduleApplicationService;
import com.bits.hr.service.SpecialShiftTimingService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.FlexScheduleApplicationEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.FlexScheduleApplication}.
 */
@RestController
@RequestMapping("/api/common")
public class UserFlexScheduleApplicationResource {

    private final Logger log = LoggerFactory.getLogger(UserFlexScheduleApplicationResource.class);

    private static final String ENTITY_NAME = "flexScheduleApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlexScheduleApplicationService flexScheduleApplicationService;

    private final SpecialShiftTimingService specialShiftTimingService;

    private final CurrentEmployeeService currentEmployeeService;
    private final FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public UserFlexScheduleApplicationResource(
        FlexScheduleApplicationService flexScheduleApplicationService,
        SpecialShiftTimingService specialShiftTimingService,
        CurrentEmployeeService currentEmployeeService,
        FlexScheduleApplicationRepository flexScheduleApplicationRepository
    ) {
        this.flexScheduleApplicationService = flexScheduleApplicationService;
        this.specialShiftTimingService = specialShiftTimingService;
        this.currentEmployeeService = currentEmployeeService;
        this.flexScheduleApplicationRepository = flexScheduleApplicationRepository;
    }

    /**
     * {@code POST  /flex-schedule-applications} : Create a new flexScheduleApplication.
     *
     * @param flexScheduleApplicationDTO the flexScheduleApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flexScheduleApplicationDTO, or with status {@code 400 (Bad Request)} if the flexScheduleApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flex-schedule-applications")
    public ResponseEntity<FlexScheduleApplicationDTO> createFlexScheduleApplication(
        @Valid @RequestBody FlexScheduleApplicationDTO flexScheduleApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FlexScheduleApplication : {}", flexScheduleApplicationDTO);
        if (flexScheduleApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new flexScheduleApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        flexScheduleApplicationDTO.setAppliedAt(LocalDate.now());
        flexScheduleApplicationDTO.setCreatedAt(Instant.now());
        flexScheduleApplicationDTO.setCreatedById(currentEmployeeService.getCurrentUserId().get());
        flexScheduleApplicationDTO.setStatus(Status.PENDING);
        flexScheduleApplicationDTO.setRequesterId(currentEmployeeService.getCurrentEmployeeId().get());

        // user can apply for a New Flex Schedule application before 07(including) days of previous application expires
        List<FlexScheduleApplication> flexScheduleApplications = flexScheduleApplicationRepository.findAllActiveByRequesterId(
            flexScheduleApplicationDTO.getRequesterId()
        );
        for (FlexScheduleApplication flexScheduleApplication : flexScheduleApplications) {
            LocalDate daysBefore = flexScheduleApplication.getEffectiveTo().minusDays(7);
            if (flexScheduleApplicationDTO.getAppliedAt().isBefore(daysBefore)) {
                throw new BadRequestAlertException(
                    "You can apply flex schedule after previous application expire.",
                    ENTITY_NAME,
                    "applyBeforeEffectiveToDate"
                );
            }
        }

        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingService.findSpecialShiftByDate(
            flexScheduleApplicationDTO.getEffectiveFrom()
        );

        if (specialShiftTimingList.size() > 0) {
            SpecialShiftTiming specialShiftTiming = specialShiftTimingList.get(0);
            String startDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(specialShiftTiming.getStartDate());
            String endDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(specialShiftTiming.getEndDate());

            String validationMessage = String.format(
                "%s is declared from %s to %s. You cannot apply flex during this time frame.",
                specialShiftTiming.getReason(),
                startDate,
                endDate
            );

            throw new BadRequestAlertException(validationMessage, ENTITY_NAME, "effectiveFromDate");
        }

        FlexScheduleApplicationDTO result = flexScheduleApplicationService.save(flexScheduleApplicationDTO);
        publishEmailEvent(result, EventType.CREATED);
        return ResponseEntity
            .created(new URI("/api/flex-schedule-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flex-schedule-applications} : Updates an existing flexScheduleApplication.
     *
     * @param flexScheduleApplicationDTO the flexScheduleApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flexScheduleApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the flexScheduleApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flexScheduleApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flex-schedule-applications")
    public ResponseEntity<FlexScheduleApplicationDTO> updateFlexScheduleApplication(
        @Valid @RequestBody FlexScheduleApplicationDTO flexScheduleApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FlexScheduleApplication : {}", flexScheduleApplicationDTO);
        if (flexScheduleApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<FlexScheduleApplicationDTO> flexScheduleApplicationDTOOptional = flexScheduleApplicationService.findOne(
            flexScheduleApplicationDTO.getId()
        );
        log.info("Checking authorization...");
        if (
            flexScheduleApplicationDTOOptional.isPresent() &&
            !flexScheduleApplicationDTOOptional.get().getRequesterId().equals(currentEmployeeService.getCurrentEmployeeId().get())
        ) {
            throw new RuntimeException("You are not authorize to modify!");
        }
        log.info("Authorization Success!");

        flexScheduleApplicationDTO.setUpdatedAt(Instant.now());
        flexScheduleApplicationDTO.setUpdatedById(currentEmployeeService.getCurrentUserId().get());
        flexScheduleApplicationDTO.setStatus(Status.PENDING);

        // user can apply for a New Flex Schedule application before 07(including) days of previous application expires
        List<FlexScheduleApplication> flexScheduleApplications = flexScheduleApplicationRepository.findAllActiveByRequesterId(
            flexScheduleApplicationDTO.getRequesterId()
        );
        for (FlexScheduleApplication flexScheduleApplication : flexScheduleApplications) {
            LocalDate daysBefore = flexScheduleApplication.getEffectiveTo().minusDays(7);

            if (
                !flexScheduleApplication.getId().equals(flexScheduleApplicationDTO.getId()) &&
                flexScheduleApplicationDTO.getAppliedAt().isBefore(daysBefore)
            ) {
                throw new BadRequestAlertException(
                    "You can apply flex schedule after previous application expire.",
                    ENTITY_NAME,
                    "applyBeforeEffectiveToDate"
                );
            }
        }
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingService.findSpecialShiftByDate(
            flexScheduleApplicationDTO.getEffectiveFrom()
        );

        if (specialShiftTimingList.size() > 0) {
            SpecialShiftTiming specialShiftTiming = specialShiftTimingList.get(0);
            String startDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(specialShiftTiming.getStartDate());
            String endDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(specialShiftTiming.getStartDate());

            String validationMessage = String.format(
                "%s is declared from %s to %s. You cannot apply flex during this time frame.",
                specialShiftTiming.getReason(),
                startDate,
                endDate
            );

            throw new BadRequestAlertException(validationMessage, ENTITY_NAME, "effectiveFromDate");
        }

        FlexScheduleApplicationDTO result = flexScheduleApplicationService.save(flexScheduleApplicationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flexScheduleApplicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /flex-schedule-applications} : get all the flexScheduleApplications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flexScheduleApplications in body.
     */
    @GetMapping("/flex-schedule-applications")
    public ResponseEntity<List<FlexScheduleApplicationDTO>> getAllFlexScheduleApplications(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FlexScheduleApplications");
        long requesterId = currentEmployeeService.getCurrentEmployeeId().get();
        Page<FlexScheduleApplicationDTO> page = flexScheduleApplicationService.findAllByRequesterId(pageable, requesterId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flex-schedule-applications/:id} : get the "id" flexScheduleApplication.
     *
     * @param id the id of the flexScheduleApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flexScheduleApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flex-schedule-applications/{id}")
    public ResponseEntity<FlexScheduleApplicationDTO> getFlexScheduleApplication(@PathVariable Long id) {
        log.debug("REST request to get FlexScheduleApplication : {}", id);

        Optional<FlexScheduleApplicationDTO> flexScheduleApplicationDTO = flexScheduleApplicationService.findOne(id);
        log.info("Checking authorization...");
        if (
            flexScheduleApplicationDTO.isPresent() &&
            !flexScheduleApplicationDTO.get().getRequesterId().equals(currentEmployeeService.getCurrentEmployeeId().get())
        ) {
            throw new RuntimeException("You are not authorize to access!");
        }
        log.info("Authorization Success!");

        return ResponseUtil.wrapOrNotFound(flexScheduleApplicationDTO);
    }

    /**
     * {@code DELETE  /flex-schedule-applications/:id} : delete the "id" flexScheduleApplication.
     *
     * @param id the id of the flexScheduleApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flex-schedule-applications/{id}")
    public ResponseEntity<Void> deleteFlexScheduleApplication(@PathVariable Long id) {
        log.debug("REST request to delete FlexScheduleApplication : {}", id);

        Optional<FlexScheduleApplication> flexScheduleApplicationOptional = flexScheduleApplicationRepository.findById(id);
        log.info("Checking authorization...");
        if (
            flexScheduleApplicationOptional.isPresent() &&
            !flexScheduleApplicationOptional.get().getRequester().equals(currentEmployeeService.getCurrentEmployee().get())
        ) {
            throw new RuntimeException("You are not authorize to delete!");
        }
        log.info("Authorization Success!");

        flexScheduleApplicationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private void publishEmailEvent(FlexScheduleApplicationDTO flexScheduleApplicationDTO, EventType event) {
        log.debug("publishing flex schedule application email event with event: " + event);
        FlexScheduleApplicationEvent flexScheduleApplicationEvent = new FlexScheduleApplicationEvent(
            this,
            flexScheduleApplicationDTO.getId(),
            event
        );
        applicationEventPublisher.publishEvent(flexScheduleApplicationEvent);
    }
}
