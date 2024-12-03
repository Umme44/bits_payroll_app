package com.bits.hr.web.rest.config;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.ConfigService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ConfigDTO;
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
 * REST controller for managing {@link com.bits.hr.domain.Config}.
 */
@RestController
@RequestMapping("/api")
public class ConfigResource {

    private final Logger log = LoggerFactory.getLogger(ConfigResource.class);

    private static final String ENTITY_NAME = "config";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigService configService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public ConfigResource(
        ConfigService configService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.configService = configService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /configs} : Create a new config.
     *
     * @param configDTO the configDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configDTO, or with status {@code 400 (Bad Request)} if the config has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configs")
    public ResponseEntity<ConfigDTO> createConfig(@Valid @RequestBody ConfigDTO configDTO) throws URISyntaxException {
        log.debug("REST request to save Config : {}", configDTO);
        if (configDTO.getId() != null) {
            throw new BadRequestAlertException("A new config cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigDTO result = configService.save(configDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "Config");

        return ResponseEntity
            .created(new URI("/api/configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configs} : Updates an existing config.
     *
     * @param configDTO the configDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configDTO,
     * or with status {@code 400 (Bad Request)} if the configDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configs")
    public ResponseEntity<ConfigDTO> updateConfig(@Valid @RequestBody ConfigDTO configDTO) throws URISyntaxException {
        log.debug("REST request to update Config : {}", configDTO);
        if (configDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigDTO result = configService.save(configDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "Config");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /configs} : get all the configs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configs in body.
     */
    @GetMapping("/configs")
    public List<ConfigDTO> getAllConfigs() {
        log.debug("REST request to get all Configs");
        return configService.findAll();
    }

    /**
     * {@code GET  /configs/:id} : get the "id" config.
     *
     * @param id the id of the configDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configs/{id}")
    public ResponseEntity<ConfigDTO> getConfig(@PathVariable Long id) {
        log.debug("REST request to get Config : {}", id);
        Optional<ConfigDTO> configDTO = configService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (configDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, configDTO.get(), RequestMethod.GET, "Config");
        }

        return ResponseUtil.wrapOrNotFound(configDTO);
    }

    @GetMapping("/configs/key/{key}")
    public ResponseEntity<ConfigDTO> getConfigByKey(@PathVariable String key) {
        log.debug("REST request to get Config : {}", key);
        Optional<ConfigDTO> configDTO = configService.findOneByKey(key);

        User user = currentEmployeeService.getCurrentUser().get();

        if (configDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, configDTO.get(), RequestMethod.GET, "Config");
        } else {
            ConfigDTO newConfigDTO = new ConfigDTO();
            newConfigDTO.setKey(key);
            newConfigDTO.setValue("FALSE");

            ConfigDTO save = configService.save(newConfigDTO);
            return ResponseEntity.ok(save);
        }

        return ResponseUtil.wrapOrNotFound(configDTO);
    }

    /**
     * {@code DELETE  /configs/:id} : delete the "id" config.
     *
     * @param id the id of the configDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configs/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        log.debug("REST request to delete Config : {}", id);
        Optional<ConfigDTO> configDTO = configService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (configDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, configDTO.get(), RequestMethod.DELETE, "Config");
        }
        configService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
