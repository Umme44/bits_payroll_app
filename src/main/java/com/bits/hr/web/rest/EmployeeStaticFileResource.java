package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EmployeeStaticFileService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeIdCardSummaryDTO;
import com.bits.hr.service.dto.EmployeeStaticFileDTO;
import com.bits.hr.service.fileOperations.fileService.EmployeeIdCardService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.EmployeeStaticFile}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeeStaticFileResource {

    private static final String RESOURCE_NAME = "employeeStaticFile";
    private final Logger log = LoggerFactory.getLogger(EmployeeStaticFileResource.class);

    private static final String ENTITY_NAME = "employeeStaticFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeStaticFileService employeeStaticFileService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EmployeeIdCardService employeeIdCardService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public EmployeeStaticFileResource(
        EmployeeStaticFileService employeeStaticFileService,
        CurrentEmployeeService currentEmployeeService,
        EmployeeIdCardService employeeIdCardService1,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.employeeStaticFileService = employeeStaticFileService;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeIdCardService = employeeIdCardService1;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /employee-static-files} : Create a new employeeStaticFile.
     *
     * @param employeeStaticFileDTO the employeeStaticFileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeStaticFileDTO, or with status {@code 400 (Bad Request)} if the employeeStaticFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-static-files")
    public ResponseEntity<EmployeeStaticFileDTO> createEmployeeStaticFile(@Valid @RequestBody EmployeeStaticFileDTO employeeStaticFileDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeStaticFile : {}", employeeStaticFileDTO);
        if (employeeStaticFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeStaticFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeStaticFileDTO result = employeeStaticFileService.save(employeeStaticFileDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/employee-static-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-static-files} : Updates an existing employeeStaticFile.
     *
     * @param employeeStaticFileDTO the employeeStaticFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeStaticFileDTO,
     * or with status {@code 400 (Bad Request)} if the employeeStaticFileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeStaticFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-static-files")
    public ResponseEntity<EmployeeStaticFileDTO> updateEmployeeStaticFile(@Valid @RequestBody EmployeeStaticFileDTO employeeStaticFileDTO)
        throws URISyntaxException {
        log.debug("REST request to update EmployeeStaticFile : {}", employeeStaticFileDTO);
        if (employeeStaticFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeStaticFileDTO result = employeeStaticFileService.save(employeeStaticFileDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeStaticFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employee-static-files} : get all the employeeStaticFiles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeStaticFiles in body.
     */
    @GetMapping("/employee-static-files")
    public ResponseEntity<List<EmployeeStaticFileDTO>> getAllEmployeeStaticFiles(Pageable pageable) {
        log.debug("REST request to get a page of EmployeeStaticFiles");
        Page<EmployeeStaticFileDTO> page = employeeStaticFileService.findAll(pageable);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-static-files/:id} : get the "id" employeeStaticFile.
     *
     * @param id the id of the employeeStaticFileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeStaticFileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-static-files/{id}")
    public ResponseEntity<EmployeeStaticFileDTO> getEmployeeStaticFile(@PathVariable Long id) {
        log.debug("REST request to get EmployeeStaticFile : {}", id);
        Optional<EmployeeStaticFileDTO> employeeStaticFileDTO = employeeStaticFileService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(employeeStaticFileDTO);
    }

    /**
     * {@code DELETE  /employee-static-files/:id} : delete the "id" employeeStaticFile.
     *
     * @param id the id of the employeeStaticFileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-static-files/{id}")
    public ResponseEntity<Void> deleteEmployeeStaticFile(@PathVariable Long id) {
        log.debug("REST request to delete EmemployeestatployeeStaticFile : {}", id);
        employeeStaticFileService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/employee-static-files/id-card")
    public ResponseEntity<Boolean> createEmployeeStaticFileIdCard(@RequestParam("file") MultipartFile[] files) throws URISyntaxException {
        log.debug("REST request to save id-card");
        Boolean isSavedFile = employeeStaticFileService.idCardBatchSave(files);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity.ok(isSavedFile);
    }

    /**
     *
     * @param pageable
     * @return
     * @throws IOException
     */
    @GetMapping("/employee-static-files/id-card-list")
    public ResponseEntity<List<EmployeeStaticFileDTO>> getEmployeesIDCardList(
        Pageable pageable,
        @RequestParam("searchText") String searchText
    ) throws IOException {
        log.debug("REST request to get a page of Employee ID Cards");
        Page<EmployeeStaticFileDTO> page = employeeStaticFileService.findAllIDCards(pageable, searchText);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/employee-id-card-summary")
    public ResponseEntity<EmployeeIdCardSummaryDTO> getEmployeeIdCardSortSummary() {
        log.debug("REST request to get short summary about employee id card");
        EmployeeIdCardSummaryDTO employeeIdCardSummaryDTO = employeeStaticFileService.getEmployeeIdCardShortSummary();

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok(employeeIdCardSummaryDTO);
    }

    //TODO:
    @PutMapping("/employee-static-files/id-card")
    public ResponseEntity<EmployeeStaticFileDTO> updateExistingEmployeeStaticFile(
        @RequestPart("file") MultipartFile file,
        @RequestPart("employeeStaticFile") EmployeeStaticFileDTO employeeStaticFileDTO
    ) throws URISyntaxException {
        EmployeeStaticFileDTO employeeStaticFileDTO1 = employeeStaticFileService.updateExistingEmployeeStaticFile(
            employeeStaticFileDTO,
            file
        );

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeStaticFileDTO.getId().toString()))
            .body(employeeStaticFileDTO1);
    }
}
