package com.bits.hr.web.rest.config;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.ConfigService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.DefinedKeys;
import com.bits.hr.service.dto.ConfigDTO;
import java.util.Arrays;
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
 * REST controller for managing {@link com.bits.hr.domain.Config}.
 */
@RestController
@RequestMapping("/api/procurement-mgt")
public class ConfigProcurementOfficerResource {

    private final Logger log = LoggerFactory.getLogger(ConfigProcurementOfficerResource.class);

    private static final String ENTITY_NAME = "config";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigService configService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public ConfigProcurementOfficerResource(
        ConfigService configService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.configService = configService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @PutMapping("/configs")
    public ResponseEntity<ConfigDTO> updateConfig(@Valid @RequestBody ConfigDTO configDTO) {
        log.debug("REST request to update Config : {}", configDTO);
        if (configDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        final String[] exposedKeys = {
            DefinedKeys.PRF_APPROVAL_FLOW,
            DefinedKeys.PRF_OFFICER_EMPLOYEE_PIN,
            DefinedKeys.PRF_MAX_TOTAL_APPROXIMATE_BDT_AMOUNT,
            DefinedKeys.PRF_TEAM_CONTACT_NO,
        };

        if (!Arrays.asList(exposedKeys).contains(configDTO.getKey())) {
            throw new BadRequestAlertException("Access is forbidden.", ENTITY_NAME, "keyIsLimitedToAccess");
        }

        ConfigDTO result = configService.save(configDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "ConfigProcurementOfficerResource");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/configs/key/{key}")
    public ResponseEntity<ConfigDTO> getConfigByKey(@PathVariable String key) {
        log.debug("REST request to get Config : {}", key);

        final String[] exposedKeys = {
            DefinedKeys.PRF_APPROVAL_FLOW,
            DefinedKeys.PRF_OFFICER_EMPLOYEE_PIN,
            DefinedKeys.PRF_MAX_TOTAL_APPROXIMATE_BDT_AMOUNT,
            DefinedKeys.PRF_TEAM_CONTACT_NO,
        };

        if (!Arrays.asList(exposedKeys).contains(key)) {
            throw new BadRequestAlertException("Access is forbidden.", ENTITY_NAME, "keyIsLimitedToAccess");
        }

        Optional<ConfigDTO> configDTO = configService.findOneByKey(key);

        User user = currentEmployeeService.getCurrentUser().get();

        if (configDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, configDTO.get(), RequestMethod.GET, "Config");
        } else {
            throw new BadRequestAlertException(String.format("no value found for key = %s", key), ENTITY_NAME, "keyMissing");
        }

        return ResponseUtil.wrapOrNotFound(configDTO);
    }
}
