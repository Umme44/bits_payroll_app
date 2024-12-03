package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.ImportEmployeeImageUploadService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeImageUploadDTO;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/employee-mgt")
public class EmployeeImageUploadFileResource {

    private static final String RESOURCE_NAME = "EmployeeImageUpload";

    private final Logger log = LoggerFactory.getLogger(EmployeeStaticFileResource.class);

    private static final String ENTITY_NAME = "EmployeeImageUpload";

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    private final ImportEmployeeImageUploadService importEmployeeImageUploadService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public EmployeeImageUploadFileResource(ImportEmployeeImageUploadService importEmployeeImageUploadService) {
        this.importEmployeeImageUploadService = importEmployeeImageUploadService;
    }

    @DeleteMapping("/import-image-upload/{pin}")
    public ResponseEntity<Void> deleteEmployeeImage(@PathVariable String pin) {
        log.debug("REST request to delete EmployeeImageUpload : {}", pin);
        importEmployeeImageUploadService.delete(pin);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/import-image-upload")
    public ResponseEntity<Boolean> createEmployeeImage(@RequestParam("file") MultipartFile[] files) throws URISyntaxException {
        log.debug("REST request to save image");
        Boolean isSavedFile = importEmployeeImageUploadService.imageBatchSave(files);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity.ok(isSavedFile);
    }

    /*    @GetMapping("/import-image-upload")
    public ResponseEntity<List<EmployeeImageUploadDTO>>getEmployeesImages(Pageable pageable) throws URISyntaxException, IOException {
        log.debug("REST request to save image");
        Page<EmployeeImageUploadDTO> page = importEmployeeImageUploadService.findAllImages(pageable);
        //Page<EmployeeStaticFileDTO> page = employeeStaticFileService.findAllIDCards(pageable, searchText);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().body(page.getContent());

    }*/

    @GetMapping("/import-image-upload")
    public ResponseEntity<List<EmployeeImageUploadDTO>> getEmployeesImages(
        Pageable pageable,
        @RequestParam("searchText") String searchText
    ) throws URISyntaxException, IOException {
        log.debug("REST request to images");
        Page<EmployeeImageUploadDTO> page = importEmployeeImageUploadService.findAllImages(pageable, searchText);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PutMapping("/import-image-upload/{id}")
    public ResponseEntity<EmployeeImageUploadDTO> updateEmployeesImages(@PathVariable Long id, @RequestPart("file") MultipartFile file)
        throws URISyntaxException, IOException {
        log.debug("REST request to images");
        EmployeeImageUploadDTO employeeImageUploadDTO = importEmployeeImageUploadService.updateImage(id, file);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity.ok().body(employeeImageUploadDTO);
    }
}
