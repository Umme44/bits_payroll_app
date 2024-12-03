package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ProcReqDTO;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.procurementRequisition.ProcReqMasterService;
import com.bits.hr.service.procurementRequisition.ProcReqService;
import com.bits.hr.web.rest.util.CopyStreams;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.ProcReqMaster}.
 */
@RestController
@RequestMapping("/api/common")
public class ProcReqUserResource {

    private final Logger log = LoggerFactory.getLogger(ProcReqUserResource.class);

    private static final String ENTITY_NAME = "procReqUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcReqMasterService procReqMasterService;
    private final ProcReqService procReqService;

    private final FileOperationService fileOperationService;
    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public ProcReqUserResource(
        ProcReqMasterService procReqMasterService,
        ProcReqService procReqService,
        FileOperationService fileOperationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.procReqMasterService = procReqMasterService;
        this.procReqService = procReqService;
        this.fileOperationService = fileOperationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /proc-req-user} : Create a new procReqMaster.
     *
     * @param procReqMasterDTO the procReqMasterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new procReqMasterDTO, or with status {@code 400 (Bad Request)} if the procReqMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proc-req-user")
    public ResponseEntity<ProcReqMasterDTO> createProcReqMaster(@RequestBody @Valid ProcReqMasterDTO procReqMasterDTO) throws URISyntaxException {
        log.debug("REST request to save ProcReqMaster : {}", procReqMasterDTO);
        if (procReqMasterDTO.getId() != null) {
            throw new BadRequestAlertException("A new procReqMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        procReqMasterDTO.setCreatedById(currentEmployeeService.getCurrentUserId().get());

        procReqMasterDTO.setRequestedById(currentEmployeeService.getCurrentEmployeeId().get());

        ProcReqMasterDTO result = procReqMasterService.create(procReqMasterDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "ProcReqUserResource");
        return ResponseEntity
            .created(new URI("/api/proc-req-user/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proc-req-user} : Updates an existing procReqMaster.
     *
     * @param procReqMasterDTO the procReqMasterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procReqMasterDTO,
     * or with status {@code 400 (Bad Request)} if the procReqMasterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the procReqMasterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proc-req-user")
    public ResponseEntity<ProcReqMasterDTO> updateProcReqMaster(@Valid @RequestBody ProcReqMasterDTO procReqMasterDTO)
        throws URISyntaxException {
        log.debug("REST request to update ProcReqMaster : {}", procReqMasterDTO);
        if (procReqMasterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        Optional<ProcReqMasterDTO> savedData = procReqMasterService.findByIdAndEmployeeId(procReqMasterDTO.getId(), employeeId);
        if (!savedData.isPresent()) {
            throw new BadRequestAlertException("You are not able to modify other's entry", "prcoReqUser", "youAreNotAuthorised");
        }

        procReqMasterDTO.setUpdatedById(currentEmployeeService.getCurrentUserId().get());
        ProcReqMasterDTO result = procReqMasterService.update(procReqMasterDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "ProcReqUserResource");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, procReqMasterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /proc-req-user} : get all the procReqMasters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of procReqMasters in body.
     */
    @GetMapping("/proc-req-user")
    public ResponseEntity<List<ProcReqMasterDTO>> getAllProcReqMasters(
        @RequestParam(required = false) Long departmentId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProcReqMasters");
        long currentEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
        Page<ProcReqMasterDTO> page = procReqMasterService.findAllByEmployeeIdAndDepartmentId(currentEmployeeId, departmentId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, "ProcReqUserResource");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proc-req-user/:id} : get the "id" procReqMaster.
     *
     * @param id the id of the procReqMasterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the procReqMasterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proc-req-user/{id}")
    public ResponseEntity<ProcReqMasterDTO> getProcReqMaster(@PathVariable Long id) {
        log.debug("REST request to get ProcReqMaster : {}", id);

        // checking authorization =>
        // a) prf authority, b) requester, c) recommenders
        //boolean hasPRFAuthority = currentEmployeeService.getCurrentUser().get().getAuthorities().contains(AuthoritiesConstants.PROCUREMENT_MANAGEMENT);
        boolean hasPRFAuthority = currentEmployeeService.hasPRFAuthority();
        Optional<ProcReqMasterDTO> procReqMasterDTO = procReqMasterService.findOne(id);
        Optional<ProcReqMasterDTO> result = Optional.empty();

        if (!hasPRFAuthority) {
            List<Long> eligibleEmployeeIDList = new ArrayList<>();
            Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();

            if (currentEmployeeId.isPresent() && procReqMasterDTO.isPresent()) {
                eligibleEmployeeIDList.add(procReqMasterDTO.get().getRequestedById());
                eligibleEmployeeIDList.addAll(procReqMasterService.getRecommenderIDList(procReqMasterDTO.get()));

                if (eligibleEmployeeIDList.contains(currentEmployeeId.get())) {
                    result = procReqMasterDTO;
                }
            }
        } else {
            result = procReqMasterDTO;
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, "ProcReqUserResource");

        return ResponseUtil.wrapOrNotFound(result);
    }

    /**
     * {@code DELETE  /proc-req-user/:id} : delete the "id" procReqMaster.
     *
     * @param id the id of the procReqMasterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proc-req-user/{id}")
    public ResponseEntity<Void> deleteProcReqMaster(@PathVariable Long id) {
        log.debug("REST request to delete ProcReqMaster : {}", id);

        long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        Optional<ProcReqMasterDTO> procReqMasterDTO = procReqMasterService.findByIdAndEmployeeId(id, employeeId);
        if (!procReqMasterDTO.isPresent()) {
            throw new BadRequestAlertException("You are not able to delete other's entry", "prcoReqUser", "youAreNotAuthorised");
        }

        procReqMasterService.delete(id, true);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, procReqMasterDTO, RequestMethod.DELETE, "ProcReqUserResource");
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    // File Download
    @GetMapping("/proc-req-user/download/{id}")
    public void downloadFileTemplatesCommon(HttpServletResponse response, @PathVariable long id) {
        Optional<ProcReqDTO> procReqDTO = procReqService.findOne(id);
        if (!procReqDTO.isPresent()) {
            log.error("File missing for ID: {}", id);
        }

        String path = procReqDTO.get().getReferenceFilePath();
        File file = fileOperationService.loadAsFile(path);

        String extension = StringUtils.getFilenameExtension(path);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + procReqDTO.get().getItemInformationName() + "." + extension + "\"";

        response.setHeader(headerKey, headerValue);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        try {
            InputStream targetStream = new FileInputStream(file);
            CopyStreams.copy(targetStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to download for ID: {}", id);
        }
    }
}
