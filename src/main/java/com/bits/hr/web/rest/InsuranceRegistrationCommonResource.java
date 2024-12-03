package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.InsuranceRegistrationService;
import com.bits.hr.service.UserInsuranceService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDetailsDTOForInsuranceRegistration;
import com.bits.hr.service.dto.InsuranceRegistrationDTO;
import com.bits.hr.service.dto.InsuranceRelationsDTO;
import com.bits.hr.service.fileOperations.fileService.EmployeeImageService;
import com.bits.hr.service.fileOperations.fileService.InsuranceFileService;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/common")
public class InsuranceRegistrationCommonResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceRegistrationResource.class);

    private static final String ENTITY_NAME = "insuranceRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceRegistrationService insuranceRegistrationService;

    private final UserInsuranceService userInsuranceService;

    private final CurrentEmployeeService currentEmployeeService;

    private final InsuranceFileService insuranceFileService;

    private final EventLoggingPublisher eventLoggingPublisher;

    private final EmployeeImageService employeeImageService;

    public InsuranceRegistrationCommonResource(
        InsuranceRegistrationService insuranceRegistrationService,
        UserInsuranceService userInsuranceService,
        CurrentEmployeeService currentEmployeeService,
        InsuranceFileService insuranceFileService,
        EventLoggingPublisher eventLoggingPublisher,
        EmployeeImageService employeeImageService
    ) {
        this.insuranceRegistrationService = insuranceRegistrationService;
        this.userInsuranceService = userInsuranceService;
        this.currentEmployeeService = currentEmployeeService;
        this.insuranceFileService = insuranceFileService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.employeeImageService = employeeImageService;
    }

    @PostMapping("/insurance-registrations")
    public ResponseEntity<InsuranceRegistrationDTO> createInsuranceRegistration(
        @Valid @RequestBody InsuranceRegistrationDTO insuranceRegistrationDTO
    ) throws URISyntaxException, IOException {
        log.debug("REST request to save InsuranceRegistration : {}", insuranceRegistrationDTO);

        if (insuranceRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuranceRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        if (!employee.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "noEmployee");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        String employeePin = currentEmployeeService.getCurrentEmployeePin().get();
        byte[] employeeImage = employeeImageService.getEmployeeImage(employeePin);

        File savedFile = insuranceFileService.saveAsByteArray(employeeImage);
        insuranceRegistrationDTO.setPhoto(savedFile.getAbsolutePath());

        InsuranceRegistrationDTO result = userInsuranceService.createInsuranceRegistration(insuranceRegistrationDTO, employee.get(), user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "insuranceRegistration");

        return ResponseEntity
            .created(new URI("/api/insurance-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/insurance-registrations/multipart")
    public ResponseEntity<InsuranceRegistrationDTO> createInsuranceRegistrationMultipart(
        @Valid @RequestPart("insuranceRegistration") InsuranceRegistrationDTO insuranceRegistrationDTO,
        @RequestPart("file") MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to save InsuranceRegistration : {}", insuranceRegistrationDTO);

        if (insuranceRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuranceRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        if (!employee.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "noEmployee");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        File savedFile = insuranceFileService.save(file);
        insuranceRegistrationDTO.setPhoto(savedFile.getAbsolutePath());

        InsuranceRegistrationDTO result = userInsuranceService.createInsuranceRegistration(insuranceRegistrationDTO, employee.get(), user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "insuranceRegistration");

        return ResponseEntity
            .created(new URI("/api/insurance-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/insurance-registrations/multipart")
    public ResponseEntity<InsuranceRegistrationDTO> updateInsuranceRegistrationMultipart(
        @Valid @RequestPart("insuranceRegistration") InsuranceRegistrationDTO insuranceRegistrationDTO,
        @RequestPart("file") MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to update InsuranceRegistration : {}", insuranceRegistrationDTO);

        if (insuranceRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (insuranceRegistrationDTO.getEmployeeId() == null) {
            throw new BadRequestAlertException("EmployeeId is not found", ENTITY_NAME, "accessForbidden");
        }
        if (insuranceRegistrationDTO.getInsuranceRelation() == InsuranceRelation.SELF) {
            throw new RuntimeException();
        }
        // pre-authorize
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        if (!employee.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "noEmployee");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        File savedFile = insuranceFileService.save(file);
        insuranceRegistrationDTO.setPhoto(savedFile.getAbsolutePath());

        InsuranceRegistrationDTO result = userInsuranceService.updateInsuranceRegistration(insuranceRegistrationDTO, employee.get(), user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "insuranceRegistration");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceRegistrationDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/insurance-registrations")
    public ResponseEntity<InsuranceRegistrationDTO> updateInsuranceRegistration(
        @Valid @RequestBody InsuranceRegistrationDTO insuranceRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InsuranceRegistration : {}", insuranceRegistrationDTO);
        if (insuranceRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (insuranceRegistrationDTO.getEmployeeId() == null) {
            throw new BadRequestAlertException("EmployeeId is not found", ENTITY_NAME, "accessForbidden");
        }
        if (insuranceRegistrationDTO.getInsuranceRelation() == InsuranceRelation.SELF) {
            throw new RuntimeException();
        }
        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        if (!employee.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "noEmployee");
        }
        User user = currentEmployeeService.getCurrentUser().get();

        InsuranceRegistrationDTO result = userInsuranceService.updateInsuranceRegistration(insuranceRegistrationDTO, employee.get(), user);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "insuranceRegistration");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceRegistrationDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/insurance-registrations")
    public ResponseEntity<List<InsuranceRegistrationDTO>> getAllInsuranceRegistrations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InsuranceRegistrations");
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "accessForbidden");
        }

        List<InsuranceRegistrationDTO> insuranceRegistrationDTOList = userInsuranceService.getAllInsuranceRegistration(
            employeeOptional.get()
        );

        User user = currentEmployeeService.getCurrentUser().get();

        eventLoggingPublisher.publishEvent(user, insuranceRegistrationDTOList, RequestMethod.GET, "insuranceRegistration");

        return ResponseEntity.ok().body(insuranceRegistrationDTOList);
    }

    @GetMapping("/insurance-registrations/{id}")
    public ResponseEntity<InsuranceRegistrationDTO> getInsuranceRegistration(@PathVariable Long id) {
        log.debug("REST request to get InsuranceRegistration : {}", id);

        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        InsuranceRegistrationDTO insuranceRegistrationDTO = userInsuranceService.getInsuranceRegistrationById(id);

        if (!insuranceRegistrationDTO.getEmployeeId().equals(currentEmployeeId.get())) {
            throw new BadRequestAlertException("Access Forbidden", ENTITY_NAME, "accessForbidden");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, insuranceRegistrationDTO, RequestMethod.GET, "insuranceRegistration");

        return ResponseEntity.ok().body(insuranceRegistrationDTO);
    }

    @GetMapping("/insurance-registrations/self-details")
    public ResponseEntity<EmployeeDetailsDTOForInsuranceRegistration> getSelfDetailsForInsuranceRegistration() {
        log.debug("REST request to get self details for insurance registration");

        Long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "insuranceRegistration");

        EmployeeDetailsDTOForInsuranceRegistration selfDetails = userInsuranceService.getEmployeeDetailsForInsuranceRegistrationByEmployeeId(
            employeeId
        );

        eventLoggingPublisher.publishEvent(user, selfDetails, RequestMethod.GET, "insuranceRegistration");

        return ResponseEntity.ok().body(selfDetails);
    }

    @DeleteMapping("/insurance-registrations/{id}")
    public ResponseEntity<Void> deleteInsuranceRegistration(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceRegistration : {}", id);
        InsuranceRegistrationDTO insuranceRegistrationDTO = userInsuranceService.getInsuranceRegistrationById(id);

        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        if (!insuranceRegistrationDTO.getEmployeeId().equals(currentEmployeeId.get())) {
            throw new BadRequestAlertException("Access Forbidden", ENTITY_NAME, "accessForbidden");
        }

        if (
            insuranceRegistrationDTO.getInsuranceStatus() == InsuranceStatus.APPROVED ||
            insuranceRegistrationDTO.getInsuranceStatus() == InsuranceStatus.NOT_APPROVED
        ) {
            log.error("Only pending requests are allowed to edit.", insuranceRegistrationDTO.getInsuranceStatus());
            throw new BadRequestAlertException("Only pending requests are allowed to edit.", ENTITY_NAME, "statusIsNotPending");
        } else {
            userInsuranceService.deleteInsuranceRegistration(id);
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, insuranceRegistrationDTO, RequestMethod.DELETE, "InsuranceRegistrations");

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/insurance-registrations/is-employee-eligible")
    public ResponseEntity<Boolean> isEmployeeEligibleForInsurance() {
        log.debug("REST request to get eligibility for insurance : {}");
        boolean isEligible = userInsuranceService.isEmployeeEligibleForInsurance();
        return ResponseEntity.ok().body(isEligible);
    }

    @GetMapping("/insurance-registrations/all-relations")
    public ResponseEntity<InsuranceRelationsDTO> getAllRelationsByEmployeeId() {
        log.debug("REST request to get all relations : {}");

        InsuranceRelationsDTO dto = insuranceRegistrationService.getAllRelations();
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/insurance-registrations/remaining-relations")
    public ResponseEntity<InsuranceRelationsDTO> getRemainingRelationsByEmployeeId() {
        log.debug("REST request to get remaining relations : {}");

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "noEmployee");
        }

        InsuranceRelationsDTO dto = insuranceRegistrationService.getRemainingRelations(employeeOptional.get().getId());
        return ResponseEntity.ok().body(dto);
    }
}
