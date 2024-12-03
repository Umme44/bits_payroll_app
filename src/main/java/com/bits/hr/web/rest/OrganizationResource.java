package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.OrganizationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.OrganizationDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Organization}.
 */
@RestController
@RequestMapping("/api/configs")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    private static final String ENTITY_NAME = "organization";
    private static final String RESOURCE_NAME = "organization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationService organizationService;
    private final FileOperationService fileOperationService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public OrganizationResource(
        OrganizationService organizationService,
        FileOperationService fileOperationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.organizationService = organizationService;
        this.fileOperationService = fileOperationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /organizations} : Create a new organization.
     *
     * @param organizationDTO the organizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationDTO, or with status {@code 400 (Bad Request)} if the organization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping(value = "/organizations")
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationDTO organizationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Organization : {}", organizationDTO);
        if (organizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new organization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationDTO result = organizationService.save(organizationDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/configs/organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping(value = "/organizations/multipart")
    public ResponseEntity<OrganizationDTO> createOrganizationMultipart(
        @Valid @RequestPart OrganizationDTO organizationDTO,
        @RequestPart List<MultipartFile> files
    ) throws URISyntaxException {
        log.debug("REST request to save Organization : {}", organizationDTO);
        if (organizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new organization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationDTO result = organizationService.createAlongMultipart(organizationDTO, files);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/configs/organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping(value = "/organizations/multipart")
    public ResponseEntity<OrganizationDTO> updateOrganizationMultipart(
        @Valid @RequestPart OrganizationDTO organizationDTO,
        @RequestPart List<MultipartFile> files
    ) throws URISyntaxException {
        log.debug("REST request to update Organization with multipart : {}", organizationDTO);
        if (organizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationDTO result = organizationService.updateAlongMultipart(organizationDTO, files);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organizations} : Updates an existing organization.
     *
     * @param organizationDTO the organizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationDTO,
     * or with status {@code 400 (Bad Request)} if the organizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organizations")
    public ResponseEntity<OrganizationDTO> updateOrganization(@Valid @RequestBody OrganizationDTO organizationDTO)
        throws URISyntaxException {
        log.debug("REST request to update Organization : {}", organizationDTO);
        if (organizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationDTO result = organizationService.save(organizationDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organizations} : get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizations in body.
     */
    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations(Pageable pageable) {
        log.debug("REST request to get a page of Organizations");
        Page<OrganizationDTO> page = organizationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organizations/:id} : get the "id" organization.
     *
     * @param id the id of the organizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organizations/{id}")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable Long id) {
        log.debug("REST request to get Organization : {}", id);
        Optional<OrganizationDTO> organizationDTO = organizationService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(organizationDTO);
    }

    /**
     * {@code DELETE  /organizations/:id} : delete the "id" organization.
     *
     * @param id the id of the organizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organizations/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        log.debug("REST request to delete Organization : {}", id);
        organizationService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping(value = "/organizations/logo", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getLogoFile() throws IOException {
        Page<OrganizationDTO> organizations = organizationService.findAll(PageRequest.of(0, 1, Sort.by("id").ascending()));
        if (organizations.getContent().size() > 0) {
            try {
                String path = organizations.getContent().get(0).getLogo();
                String extension = StringUtils.getFilenameExtension(path);
                MediaType contentType = MediaType.IMAGE_JPEG;
                if (extension.equals("png")) {
                    contentType = MediaType.IMAGE_PNG;
                } else if (extension.equals("svg")) {
                    contentType = MediaType.valueOf("image/svg+xml");
                }
                byte[] result = fileOperationService.loadAsByte(path);
                return ResponseEntity.ok().contentType(contentType).body(result);
            } catch (Exception ex) {
                return ResponseEntity.ok(new byte[] {});
            }
        } else {
            return ResponseEntity.ok(new byte[] {});
        }
    }
}
