package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.InsuranceConfigurationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.InsuranceConfigurationDTO;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.bits.hr.domain.InsuranceConfiguration}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class InsuranceConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceConfigurationResource.class);

    private static final String ENTITY_NAME = "insuranceConfiguration";
    private static final String RESOURCE_NAME = "insuranceConfigurationResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceConfigurationService insuranceConfigurationService;
    private final EventLoggingPublisher eventLoggingPublisher;
    private final CurrentEmployeeService currentEmployeeService;

    public InsuranceConfigurationResource(
        InsuranceConfigurationService insuranceConfigurationService,
        EventLoggingPublisher eventLoggingPublisher,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.insuranceConfigurationService = insuranceConfigurationService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.currentEmployeeService = currentEmployeeService;
    }

    @PostMapping("/insurance-configurations")
    public ResponseEntity<InsuranceConfigurationDTO> createInsuranceConfiguration(
        @Valid @RequestBody InsuranceConfigurationDTO insuranceConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InsuranceConfiguration : {}", insuranceConfigurationDTO);
        if (insuranceConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuranceConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InsuranceConfigurationDTO result = insuranceConfigurationService.save(insuranceConfigurationDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/insurance-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/insurance-configurations")
    public ResponseEntity<InsuranceConfigurationDTO> updateInsuranceConfiguration(
        @Valid @RequestBody InsuranceConfigurationDTO insuranceConfigurationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InsuranceConfiguration : {}", insuranceConfigurationDTO);
        if (insuranceConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InsuranceConfigurationDTO result = insuranceConfigurationService.save(insuranceConfigurationDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceConfigurationDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/insurance-configurations")
    public ResponseEntity<List<InsuranceConfigurationDTO>> getAllInsuranceConfigurations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InsuranceConfigurations");
        Page<InsuranceConfigurationDTO> page = insuranceConfigurationService.findAll(pageable);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page.getContent(), RequestMethod.GET, RESOURCE_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/insurance-configurations/{id}")
    public ResponseEntity<InsuranceConfigurationDTO> getInsuranceConfiguration(@PathVariable Long id) {
        log.debug("REST request to get InsuranceConfiguration : {}", id);
        Optional<InsuranceConfigurationDTO> insuranceConfigurationDTO = insuranceConfigurationService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, insuranceConfigurationDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(insuranceConfigurationDTO);
    }

    @DeleteMapping("/insurance-configurations/{id}")
    public ResponseEntity<Void> deleteInsuranceConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceConfiguration : {}", id);
        insuranceConfigurationService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
