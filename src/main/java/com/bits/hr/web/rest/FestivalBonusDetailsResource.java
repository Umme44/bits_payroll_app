package com.bits.hr.web.rest;

import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FestivalBonusDetailsService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import com.bits.hr.service.mapper.FestivalBonusDetailsMapper;
import com.bits.hr.service.search.FilterDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.bits.hr.util.annotation.ValidateNaturalText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.FestivalBonusDetails}.
 */
@Validated
@RestController
@RequestMapping("/api/payroll-mgt")
public class FestivalBonusDetailsResource {

    private final Logger log = LoggerFactory.getLogger(FestivalBonusDetailsResource.class);

    private static final String ENTITY_NAME = "festivalBonusDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FestivalBonusDetailsService festivalBonusDetailsService;

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    private FestivalBonusDetailsMapper festivalBonusDetailsMapper;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public FestivalBonusDetailsResource(
        FestivalBonusDetailsService festivalBonusDetailsService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.festivalBonusDetailsService = festivalBonusDetailsService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /festival-bonus-details} : Create a new festivalBonusDetails.
     *
     * @param festivalBonusDetailsDTO the festivalBonusDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new festivalBonusDetailsDTO, or with status {@code 400 (Bad Request)} if the festivalBonusDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/festival-bonus-details")
    public ResponseEntity<FestivalBonusDetailsDTO> createFestivalBonusDetails(
        @Valid @RequestBody FestivalBonusDetailsDTO festivalBonusDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FestivalBonusDetails : {}", festivalBonusDetailsDTO);
        if (festivalBonusDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new festivalBonusDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FestivalBonusDetailsDTO result = festivalBonusDetailsService.save(festivalBonusDetailsDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "FestivalBonusDetails");

        return ResponseEntity
            .created(new URI("/api/festival-bonus-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /festival-bonus-details} : Updates an existing festivalBonusDetails.
     *
     * @param festivalBonusDetailsDTO the festivalBonusDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated festivalBonusDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the festivalBonusDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the festivalBonusDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/festival-bonus-details")
    public ResponseEntity<FestivalBonusDetailsDTO> updateFestivalBonusDetails(
        @Valid @RequestBody FestivalBonusDetailsDTO festivalBonusDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FestivalBonusDetails : {}", festivalBonusDetailsDTO);
        if (festivalBonusDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FestivalBonusDetailsDTO result = festivalBonusDetailsService.update(festivalBonusDetailsDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "FestivalBonusDetails");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, festivalBonusDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /festival-bonus-details} : get all the festivalBonusDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of festivalBonusDetails in body.
     */
    @GetMapping("/festival-bonus-details")
    public ResponseEntity<List<FestivalBonusDetailsDTO>> getAllFestivalBonusDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FestivalBonusDetails");
        Page<FestivalBonusDetailsDTO> page = festivalBonusDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "FestivalBonusDetails");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /festival-bonus-details/:id} : get the "id" festivalBonusDetails.
     *
     * @param id the id of the festivalBonusDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the festivalBonusDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/festival-bonus-details/{id}")
    public ResponseEntity<FestivalBonusDetailsDTO> getFestivalBonusDetails(@PathVariable Long id) {
        log.debug("REST request to get FestivalBonusDetails : {}", id);
        Optional<FestivalBonusDetailsDTO> festivalBonusDetailsDTO = festivalBonusDetailsService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (festivalBonusDetailsDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, festivalBonusDetailsDTO.get(), RequestMethod.GET, "FestivalBonusDetails");
        }
        return ResponseUtil.wrapOrNotFound(festivalBonusDetailsDTO);
    }

    /**
     * {@code DELETE  /festival-bonus-details/:id} : delete the "id" festivalBonusDetails.
     *
     * @param id the id of the festivalBonusDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/festival-bonus-details/{id}")
    public ResponseEntity<Void> deleteFestivalBonusDetails(@PathVariable Long id) {
        log.debug("REST request to delete FestivalBonusDetails : {}", id);
        Optional<FestivalBonusDetailsDTO> festivalBonusDetailsDTO = festivalBonusDetailsService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (festivalBonusDetailsDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, festivalBonusDetailsDTO.get(), RequestMethod.DELETE, "FestivalBonusDetails");
        }
        festivalBonusDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/festival-bonus-details/get-by-festival/{id}")
    public ResponseEntity<List<FestivalBonusDetailsDTO>> getByFestivalId(@PathVariable long id, @RequestBody FilterDto filterDto) {
        log.debug("REST request to get list of festival details by festival Id");
        List<FestivalBonusDetails> festivalBonusDetails = festivalBonusDetailsRepository.getByFestivalId(
            id,
            "%" + filterDto.getSearchText().toLowerCase() + "%"
        );
        return ResponseEntity.ok(festivalBonusDetailsMapper.toDto(festivalBonusDetails));
    }

    @GetMapping("/festival-bonus-details/festival-bonus-hold")
    public ResponseEntity<List<FestivalBonusDetailsDTO>> getBonusHoldList(@RequestParam(required = false) @ValidateNaturalText String searchText) {
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.getFestivalBonusOnHoldList(searchText);
        return ResponseEntity.ok(festivalBonusDetailsMapper.toDto(festivalBonusDetailsList));
    }

    @GetMapping("/festival-bonus-details/hold/{fbDetailsId}")
    public ResponseEntity<Boolean> holdFestivalBonus(@PathVariable long fbDetailsId) {
        log.debug("REST request to hold festival details by festival Id");
        boolean result = festivalBonusDetailsService.holdOrUnHoldFestivalBonusDetail(fbDetailsId, true);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/festival-bonus-details/unHold/{fbDetailsId}")
    public ResponseEntity<Boolean> unHoldFestivalBonus(@PathVariable long fbDetailsId) {
        log.debug("REST request to hold festival details by festival Id");
        boolean result = festivalBonusDetailsService.holdOrUnHoldFestivalBonusDetail(fbDetailsId, false);
        return ResponseEntity.ok(result);
    }
}
