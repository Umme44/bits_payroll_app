package com.bits.hr.web.rest;

import com.bits.hr.domain.Organization;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.OrganizationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.OrganizationDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link Organization}.
 */
@RestController
@RequestMapping("/api/common")
public class OrganizationCommonResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationCommonResource.class);

    private static final String ENTITY_NAME = "organization";
    private static final String RESOURCE_NAME = "organization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationService organizationService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public OrganizationCommonResource(
        OrganizationService organizationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.organizationService = organizationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @GetMapping("/organizations/basic")
    public ResponseEntity<OrganizationDTO> getOrganization() {
        log.debug("REST request to get Organization basic details");
        OrganizationDTO organizationDTO = organizationService.getOrganizationBasic();

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity.ok(organizationDTO);
    }
}
