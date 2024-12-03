package com.bits.hr.web.rest;

import com.bits.hr.domain.EmployeeStaticFile;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EmployeeStaticFileService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeStaticFileDTO;
import com.bits.hr.service.fileOperations.fileService.EmployeeIdCardService;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link EmployeeStaticFile}.
 */
@RestController
@RequestMapping("/api/common")
public class EmployeeStaticFileCommonResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeStaticFileCommonResource.class);

    private static final String ENTITY_NAME = "employeeStaticFile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeStaticFileService employeeStaticFileService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    private final EmployeeIdCardService employeeIdCardService;

    public EmployeeStaticFileCommonResource(
        EmployeeStaticFileService employeeStaticFileService,
        EmployeeIdCardService employeeIdCardService1
    ) {
        this.employeeStaticFileService = employeeStaticFileService;
        this.employeeIdCardService = employeeIdCardService1;
    }

    @GetMapping("/my-id-card")
    public ResponseEntity<EmployeeStaticFileDTO> getMyIdCard() throws IOException {
        log.debug("REST request to get ID Card");
        EmployeeStaticFileDTO result = employeeStaticFileService.getCurrentUserIdCard();

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeStaticFile");

        return ResponseEntity.ok(result);
    }
    /*@GetMapping(
        value = "/my-id-card/",
        produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType() throws IOException {
        Optional<String> pin = currentEmployeeService.getCurrentEmployeePin();
        if (!pin.isPresent()) {
            return IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/id-card-placeholder.jpg"));
        }
        String path = employeeStaticFileService.getFilePath(pin.get());
        return fileOperationService.loadAsByte(path);
    }*/

}
