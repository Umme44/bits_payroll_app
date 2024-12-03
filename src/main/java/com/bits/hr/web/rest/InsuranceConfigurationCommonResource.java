package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.InsuranceConfigurationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.InsuranceConfigurationDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/common")
public class InsuranceConfigurationCommonResource {

    private static final String RESOURCE_NAME = "insuranceConfigurationResource";
    private final Logger log = LoggerFactory.getLogger(InsuranceConfigurationResource.class);
    public final InsuranceConfigurationService insuranceConfigurationService;
    private final EventLoggingPublisher eventLoggingPublisher;
    private final CurrentEmployeeService currentEmployeeService;

    public InsuranceConfigurationCommonResource(
        InsuranceConfigurationService insuranceConfigurationService,
        EventLoggingPublisher eventLoggingPublisher,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.insuranceConfigurationService = insuranceConfigurationService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.currentEmployeeService = currentEmployeeService;
    }

    @GetMapping("/insurance-configurations")
    public ResponseEntity<InsuranceConfigurationDTO> getAllInsuranceConfigurations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InsuranceConfigurations");
        Page<InsuranceConfigurationDTO> page = insuranceConfigurationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, page.getContent(), RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent().get(0));
    }
}
