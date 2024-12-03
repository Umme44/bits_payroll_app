package com.bits.hr.web.rest;

import com.bits.hr.domain.FileTemplates;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.FileTemplatesRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FileTemplatesService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FileTemplatesDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.util.MimeTypeToFileType;
import com.bits.hr.web.rest.util.CopyStreams;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.FileTemplates}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class FileTemplatesResource {

    private final Logger log = LoggerFactory.getLogger(FileTemplatesResource.class);

    private static final String ENTITY_NAME = "fileTemplates";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileTemplatesService fileTemplatesService;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private FileTemplatesRepository fileTemplatesRepository;

    @Autowired
    private MimeTypeToFileType mimeTypeToFileType;

    @Autowired
    private FileOperationService fileOperationService;

    public FileTemplatesResource(
        FileTemplatesService fileTemplatesService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.fileTemplatesService = fileTemplatesService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /file-templates} : Create a new fileTemplates.
     *
     * @param fileTemplatesDTO the fileTemplatesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileTemplatesDTO, or with status {@code 400 (Bad Request)} if the fileTemplates has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-templates")
    public ResponseEntity<FileTemplatesDTO> createFileTemplates(
        @Valid @RequestPart("fileTemplates") FileTemplatesDTO fileTemplatesDTO,
        @RequestPart("file") MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to save FileTemplates : {}", fileTemplatesDTO);
        if (fileTemplatesDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileTemplates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileTemplatesDTO result = fileTemplatesService.save(fileTemplatesDTO, file);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "FileTemplates");
        return ResponseEntity
            .created(new URI("/api/file-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-templates} : Updates an existing fileTemplates.
     *
     * @param fileTemplatesDTO the fileTemplatesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileTemplatesDTO,
     * or with status {@code 400 (Bad Request)} if the fileTemplatesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileTemplatesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping(value = "/file-templates")
    public ResponseEntity<FileTemplatesDTO> updateFileTemplates(
        @Valid @RequestPart("fileTemplates") FileTemplatesDTO fileTemplatesDTO,
        @RequestPart("file") MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to update FileTemplates : {}", fileTemplatesDTO);
        if (fileTemplatesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileTemplatesDTO result = fileTemplatesService.save(fileTemplatesDTO, file);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "FileTemplates");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileTemplatesDTO.getId().toString()))
            .body(result);
    }

    @PutMapping(value = "/file-templates/without-file")
    public ResponseEntity<FileTemplatesDTO> updateFileTemplatesWithoutFile(@RequestBody FileTemplatesDTO fileTemplatesDTO)
        throws URISyntaxException {
        log.debug("REST request to update FileTemplates : {}", fileTemplatesDTO);
        if (fileTemplatesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FileTemplatesDTO result = fileTemplatesService.update(fileTemplatesDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "FileTemplates");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileTemplatesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /file-templates} : get all the fileTemplates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileTemplates in body.
     */
    @GetMapping("/file-templates")
    public ResponseEntity<List<FileTemplatesDTO>> getAllFormsByType(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "") String searchText,
        @RequestParam(required = false, defaultValue = "") String fileType
    ) throws IOException {
        log.debug("REST request to get a page of FileTemplates");
        Page<FileTemplatesDTO> page = fileTemplatesService.findAll(searchText, fileType, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "FileTemplates");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-templates/:id} : get the "id" fileTemplates.
     *
     * @param id the id of the fileTemplatesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileTemplatesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-templates/{id}")
    public ResponseEntity<FileTemplatesDTO> getFileTemplates(@PathVariable Long id) {
        log.debug("REST request to get FileTemplates : {}", id);
        Optional<FileTemplatesDTO> fileTemplatesDTO = fileTemplatesService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (fileTemplatesDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, fileTemplatesDTO.get(), RequestMethod.GET, "FileTemplates");
        }
        return ResponseUtil.wrapOrNotFound(fileTemplatesDTO);
    }

    /**
     * {@code DELETE  /file-templates/:id} : delete the "id" fileTemplates.
     *
     * @param id the id of the fileTemplatesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-templates/{id}")
    public ResponseEntity<Void> deleteFileTemplates(@PathVariable Long id) {
        log.debug("REST request to delete FileTemplates : {}", id);
        Optional<FileTemplatesDTO> fileTemplatesDTO = fileTemplatesService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (fileTemplatesDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, fileTemplatesDTO.get(), RequestMethod.DELETE, "FileTemplates");
        }
        fileTemplatesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/file-templates/admin-suggestion-titles")
    public ResponseEntity<List<String>> getAllTitlesForAdmin() throws IOException {
        List<String> titles = fileTemplatesRepository.findAllTitlesAdmin();
        return ResponseEntity.ok().body(titles);
    }

    // File Download
    @GetMapping("/file-templates/download/{id}")
    public void downloadFileTemplates(HttpServletResponse response, @PathVariable long id) throws IOException {
        Optional<FileTemplates> fileTemplates = fileTemplatesRepository.findById(id);
        if (!fileTemplates.isPresent()) {
            log.error("File missing for ID: {}", id);
        }

        String path = fileTemplates.get().getFilePath();
        File file = fileOperationService.loadAsFile(path);

        String extension = StringUtils.getFilenameExtension(path);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileTemplates.get().getTitle() + "." + extension + "\"";

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
