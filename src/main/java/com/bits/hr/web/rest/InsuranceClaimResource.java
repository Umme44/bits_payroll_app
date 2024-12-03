package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.ClaimStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.InsuranceClaimService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.InsuranceClaimDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.bits.hr.util.annotation.ValidateNaturalText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.bits.hr.domain.InsuranceClaim}.
 */
@Validated
@RestController
@RequestMapping("/api/employee-mgt/")
public class InsuranceClaimResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceClaimResource.class);

    private static final String ENTITY_NAME = "insuranceClaim";

    private static final String RESOURCE_NAME = "InsuranceClaimCommonResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceClaimService insuranceClaimService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public InsuranceClaimResource(
        InsuranceClaimService insuranceClaimService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.insuranceClaimService = insuranceClaimService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @PostMapping("/insurance-claims")
    public ResponseEntity<InsuranceClaimDTO> createInsuranceClaim(@Valid @RequestBody InsuranceClaimDTO insuranceClaimDTO)
        throws URISyntaxException {
        log.debug("REST request to save InsuranceClaim : {}", insuranceClaimDTO);
        if (insuranceClaimDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuranceClaim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        InsuranceClaimDTO result = insuranceClaimService.createInsuranceClaim(insuranceClaimDTO, user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/insurance-claims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/insurance-claims")
    public ResponseEntity<InsuranceClaimDTO> updateInsuranceClaim(@Valid @RequestBody InsuranceClaimDTO insuranceClaimDTO)
        throws URISyntaxException {
        log.debug("REST request to update InsuranceClaim : {}", insuranceClaimDTO);
        if (insuranceClaimDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        InsuranceClaimDTO result = insuranceClaimService.updateInsuranceClaim(insuranceClaimDTO, user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceClaimDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/insurance-claims")
    public ResponseEntity<List<InsuranceClaimDTO>> getAllInsuranceClaims(
        @RequestParam(required = false) @ValidateNaturalText String searchText,
        @RequestParam(required = false) ClaimStatus status,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InsuranceClaims");
        Page<InsuranceClaimDTO> page = insuranceClaimService.findAll(searchText, status, year, month, pageable);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page.getContent(), RequestMethod.GET, RESOURCE_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/insurance-claims/{id}")
    public ResponseEntity<InsuranceClaimDTO> getInsuranceClaim(@PathVariable Long id) {
        log.debug("REST request to get InsuranceClaim : {}", id);
        Optional<InsuranceClaimDTO> insuranceClaimDTO = insuranceClaimService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, insuranceClaimDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(insuranceClaimDTO);
    }

    @DeleteMapping("/insurance-claims/{id}")
    public ResponseEntity<Void> deleteInsuranceClaim(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceClaim : {}", id);
        insuranceClaimService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
