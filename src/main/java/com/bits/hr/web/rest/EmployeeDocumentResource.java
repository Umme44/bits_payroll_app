package com.bits.hr.web.rest;

import com.bits.hr.domain.EmployeeDocument;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeDocumentRepository;
import com.bits.hr.service.EmployeeDocumentService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDocumentDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.util.FileValidationUtil;
import com.bits.hr.web.rest.util.CopyStreams;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link EmployeeDocument}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeeDocumentResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeDocumentResource.class);

    private static final String ENTITY_NAME = "employeeDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeDocumentService employeeDocumentService;

    private final EmployeeDocumentRepository employeeDocumentRepository;

    private final FileOperationService fileOperationService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public EmployeeDocumentResource(
        EmployeeDocumentService employeeDocumentService,
        EmployeeDocumentRepository employeeDocumentRepository,
        FileOperationService fileOperationService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.employeeDocumentService = employeeDocumentService;
        this.employeeDocumentRepository = employeeDocumentRepository;
        this.fileOperationService = fileOperationService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /employee-document} : Create a new employeeDocument.
     *
     * @param employeeDocumentDTO the employeeDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeDocumentDTO, or with status {@code 400 (Bad Request)} if the employeeDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-document/{pin}")
    public ResponseEntity<?> createEmployeeDocument(
        @PathVariable("pin") String pin,
        @Valid @RequestPart("employeeDocument") EmployeeDocumentDTO employeeDocumentDTO,
        @RequestPart("file") MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to save EmployeeDocument : {}", employeeDocumentDTO);
        if (employeeDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (employeeDocumentService.existsByFileNameAndPin(employeeDocumentDTO.getFileName(), pin)) {
            return ResponseEntity
                .badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, null, "File name already exists"))
                .body("File name already exists");
        }

        if (!FileValidationUtil.isFileNameAndTypeValid(file)) {
            return ResponseEntity
                .badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, null, "Uploaded file name is not valid!"))
                .body("File name is not valid!");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        employeeDocumentDTO.setCreatedAt(Instant.now());
        employeeDocumentDTO.setCreatedBy(user.getEmail());
        employeeDocumentDTO.setUpdatedBy(null);
        employeeDocumentDTO.setUpdatedAt(null);

        EmployeeDocumentDTO result = employeeDocumentService.save(pin, employeeDocumentDTO, file);
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "EmployeeDocument");
        return ResponseEntity
            .created(new URI("/api/employee-document/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/employee-document/{pin}")
    public ResponseEntity<EmployeeDocumentDTO> updateEmployeeDocument(
        @PathVariable("pin") String pin,
        @Valid @RequestPart("employeeDocument") EmployeeDocumentDTO employeeDocumentDTO,
        @RequestPart("file") MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeDocument : {}", employeeDocumentDTO);
        if (employeeDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        } else {
            Optional<EmployeeDocumentDTO> existing = employeeDocumentService.findOne(employeeDocumentDTO.getId());
            if (existing.isPresent()) {
                employeeDocumentDTO.setCreatedBy(existing.get().getCreatedBy());
                employeeDocumentDTO.setCreatedAt(existing.get().getCreatedAt());
            } else throw new BadRequestAlertException("Invalid Id", ENTITY_NAME, "id not found");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        employeeDocumentDTO.setUpdatedAt(Instant.now());
        employeeDocumentDTO.setUpdatedBy(user.getEmail());
        EmployeeDocumentDTO result = employeeDocumentService.save(pin, employeeDocumentDTO, file);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "EmployeeDocument");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeDocumentDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/employee-document/{pin}/without-file")
    public ResponseEntity<EmployeeDocumentDTO> updateEmployeeDocumentWithOutFile(
        @PathVariable("pin") String pin,
        @RequestBody EmployeeDocumentDTO employeeDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeDocument : {}", employeeDocumentDTO);
        if (employeeDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        employeeDocumentDTO.setUpdatedAt(Instant.now());
        employeeDocumentDTO.setUpdatedBy(user.getEmail());
        EmployeeDocumentDTO result = employeeDocumentService.update(pin, employeeDocumentDTO);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "EmployeeDocument");
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeDocumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employee-document} : get all the employeeDocuments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeDocuments in body.
     */
    @GetMapping("/employee-document")
    public ResponseEntity<List<EmployeeDocumentDTO>> getAllEmployeeDocument(
        @RequestParam(name = "pin", required = true) String pin,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeeDocument");
        Page<EmployeeDocumentDTO> page = employeeDocumentService.getAllByPinOrderByFileName(pin, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeDocument");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code DELETE  /employee-document/:id} : delete the "id" employeeDocuments.
     *
     * @param id the id of the employeeDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-document/{id}")
    public ResponseEntity<Void> deleteEmployeeDocument(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeDocument : {}", id);
        Optional<EmployeeDocumentDTO> employeeDocumentDTO = employeeDocumentService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (employeeDocumentDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, employeeDocumentDTO.get(), RequestMethod.DELETE, "EmployeeDocument");
        }
        employeeDocumentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /*
     * File Download
     * */
    @GetMapping("/employee-document/download/{id}")
    public void downloadEmployeeDocument(HttpServletResponse response, @PathVariable long id) throws IOException {
        Optional<EmployeeDocument> employeeDocuments = employeeDocumentRepository.findById(id);
        if (employeeDocuments.isEmpty()) {
            log.error("File missing for ID: {}", id);
        }

        String path = employeeDocuments.get().getFilePath();
        File file = fileOperationService.loadAsFile(path);

        String extension = StringUtils.getFilenameExtension(path);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + employeeDocuments.get().getFileName() + "." + extension + "\"";

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

    /*
     * Batch upload file
     * */

//    @PostMapping("/employee-document/batch/files")
//    public ResponseEntity<Boolean> uploadAndExtractZip(@RequestParam("file") MultipartFile file) {
//        return ResponseEntity.ok(employeeDocumentService.uploadAndExtractZip(file));
//    }
//
//    @PostMapping("/employee-document/batch/excel")
//    public ResponseEntity<Boolean> uploadAndProcess(@RequestParam("file") MultipartFile file)  {
//        return ResponseEntity.ok(employeeDocumentService.uploadAndProcess(file));
//    }

    @PostMapping("/employee-document/batch")
    public ResponseEntity<Boolean> batchProcess(@RequestParam("zip") MultipartFile zip, @RequestParam("excel") MultipartFile excel)  {
        boolean zipSuccess, excelSuccess = false;
        zipSuccess = employeeDocumentService.uploadAndExtractZip(zip);
        excelSuccess = employeeDocumentService.uploadAndProcess(excel);
        return ResponseEntity.ok(zipSuccess && excelSuccess);
    }
}
