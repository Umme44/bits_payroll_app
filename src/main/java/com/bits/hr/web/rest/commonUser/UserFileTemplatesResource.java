package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.FileTemplates;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.FileTemplatesRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.FileTemplatesService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FileTemplatesDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidateSearchText;
import com.bits.hr.web.rest.FileTemplatesResource;
import com.bits.hr.web.rest.util.CopyStreams;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/common/file-templates")
@Validated
public class UserFileTemplatesResource {

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
    private FileOperationService fileOperationService;

    public UserFileTemplatesResource(
        FileTemplatesService fileTemplatesService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.fileTemplatesService = fileTemplatesService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * REST request to get all type files for common users and
     * filter by file type(forms, templates, policies)
     */
    @GetMapping("")
    public ResponseEntity<List<FileTemplatesDTO>> getAllFileTemplates(
        @RequestParam(required = false) @ValidateNaturalText String searchText,
        @RequestParam(required = false) String fileType,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws IOException {
        log.debug("REST request to get page of FileTemplates with search by title and file type");
        Page<FileTemplatesDTO> page = fileTemplatesService.findAllTemplatesOfUser(searchText, fileType, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "FileTemplatesUser");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/user-suggestion-titles")
    public ResponseEntity<List<String>> getAllTitlesForUser() throws IOException {
        List<String> titles = fileTemplatesRepository.findAllTitlesCommon();
        return ResponseEntity.ok().body(titles);
    }

    // File Download
    @GetMapping("/download/{id}")
    public void downloadFileTemplatesCommon(HttpServletResponse response, @PathVariable long id) {
        Optional<FileTemplates> fileTemplates = fileTemplatesRepository.findByIdUser(id);
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
