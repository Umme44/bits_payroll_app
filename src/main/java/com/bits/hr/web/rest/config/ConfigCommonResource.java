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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Config}.
 */
@RestController
@RequestMapping("/api/common")
public class ConfigCommonResource {

    private final Logger log = LoggerFactory.getLogger(ConfigCommonResource.class);

    private static final String ENTITY_NAME = "config";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigService configService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public ConfigCommonResource(
        ConfigService configService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.configService = configService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @GetMapping("/configs/key/{key}")
    public ResponseEntity<ConfigDTO> getConfigByKey(@PathVariable String key) {
        log.debug("REST request to get Config : {}", key);

        final String[] exposedKeys = {
            DefinedKeys.max_duration_in_days_for_attendance_data_load,
            DefinedKeys.is_leave_application_enabled_for_user_end,
            DefinedKeys.PRF_MAX_TOTAL_APPROXIMATE_BDT_AMOUNT,
            DefinedKeys.PRF_TEAM_CONTACT_NO,
            DefinedKeys.is_income_tax_visibility_enabled_for_user_end,
            DefinedKeys.is_rrf_enabled_for_user_end,
            DefinedKeys.is_nominee_nid_verification_enabled_for_user_end,
            DefinedKeys.is_pf_nominee_nid_verification_enabled_for_user_end,
            DefinedKeys.is_gf_nominee_nid_verification_enabled_for_user_end
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
