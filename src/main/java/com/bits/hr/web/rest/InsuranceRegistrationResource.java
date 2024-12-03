package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.InsuranceRegistrationService;
import com.bits.hr.service.UserInsuranceService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.*;
import com.bits.hr.service.fileOperations.fileService.EmployeeImageService;
import com.bits.hr.service.fileOperations.fileService.InsuranceFileService;
import com.bits.hr.service.scheduler.schedulingService.ScheduledInsuranceService;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidateNumeric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.InsuranceRegistration}.
 */
@Validated
@RestController
@RequestMapping("/api/employee-mgt")
public class InsuranceRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceRegistrationResource.class);

    private static final String ENTITY_NAME = "insuranceRegistration";

    private static final String RESOURCE_NAME = "InsuranceRegistrationResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceRegistrationService insuranceRegistrationService;

    private final InsuranceFileService insuranceFileService;

    private final UserInsuranceService userInsuranceService;

    private final CurrentEmployeeService currentEmployeeService;

    private final EmployeeRepository employeeRepository;

    private final ScheduledInsuranceService scheduledInsuranceService;

    private final EventLoggingPublisher eventLoggingPublisher;
    private final EmployeeImageService employeeImageService;

    public InsuranceRegistrationResource(
        InsuranceRegistrationService insuranceRegistrationService,
        InsuranceFileService insuranceFileService,
        UserInsuranceService userInsuranceService,
        CurrentEmployeeService currentEmployeeService,
        EmployeeRepository employeeRepository,
        ScheduledInsuranceService scheduledInsuranceService,
        EventLoggingPublisher eventLoggingPublisher,
        EmployeeImageService employeeImageService
    ) {
        this.insuranceRegistrationService = insuranceRegistrationService;
        this.insuranceFileService = insuranceFileService;
        this.userInsuranceService = userInsuranceService;
        this.currentEmployeeService = currentEmployeeService;
        this.employeeRepository = employeeRepository;
        this.scheduledInsuranceService = scheduledInsuranceService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.employeeImageService = employeeImageService;
    }

    @PostMapping("/insurance-registrations")
    public ResponseEntity<InsuranceRegistrationDTO> createInsuranceRegistration(
        @Valid @RequestBody InsuranceRegistrationDTO insuranceRegistrationDTO
    ) throws URISyntaxException, IOException {
        log.debug("REST request to create InsuranceRegistration : {}", insuranceRegistrationDTO);

        if (insuranceRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuranceRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<Employee> employee = employeeRepository.findById(insuranceRegistrationDTO.getEmployeeId());

        if (!employee.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "noEmployee");
        }

        byte[] employeeImage = employeeImageService.getEmployeeImage(employee.get().getPin());

        File savedFile = insuranceFileService.saveAsByteArray(employeeImage);
        insuranceRegistrationDTO.setPhoto(savedFile.getAbsolutePath());

        User user = currentEmployeeService.getCurrentUser().get();
        InsuranceRegistrationDTO result = insuranceRegistrationService.createInsuranceRegistration(user.getId(), insuranceRegistrationDTO);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);

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
        log.debug("REST request to create InsuranceRegistration : {}", insuranceRegistrationDTO);
        if (insuranceRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new insuranceRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Optional<Employee> employee = employeeRepository.findById(insuranceRegistrationDTO.getEmployeeId());

        if (!employee.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "noEmployee");
        }

        File savedFile = insuranceFileService.save(file);
        insuranceRegistrationDTO.setPhoto(savedFile.getAbsolutePath());

        User user = currentEmployeeService.getCurrentUser().get();
        InsuranceRegistrationDTO result = insuranceRegistrationService.createInsuranceRegistration(user.getId(), insuranceRegistrationDTO);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);

        return ResponseEntity
            .created(new URI("/api/insurance-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
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

        User user = currentEmployeeService.getCurrentUser().get();

        InsuranceRegistrationDTO result = insuranceRegistrationService.updateInsuranceRegistration(user.getId(), insuranceRegistrationDTO);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceRegistrationDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/insurance-registrations/multipart")
    public ResponseEntity<InsuranceRegistrationDTO> updateInsuranceRegistrationMultipart(
        @Valid @RequestPart("insuranceRegistration") InsuranceRegistrationDTO insuranceRegistrationDTO,
        @RequestPart("file") MultipartFile file
    ) throws URISyntaxException {
        log.debug("REST request to update InsuranceRegistration : {}", insuranceRegistrationDTO);

        File savedFile = insuranceFileService.save(file);
        insuranceRegistrationDTO.setPhoto(savedFile.getAbsolutePath());

        if (insuranceRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        InsuranceRegistrationDTO result = insuranceRegistrationService.updateInsuranceRegistration(user.getId(), insuranceRegistrationDTO);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insuranceRegistrationDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/insurance-registrations")
    public ResponseEntity<List<InsuranceRegistrationAdminDTO>> getAllInsuranceRegistrationsForAdmin(
        @RequestParam @ValidateNaturalText String searchText,
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam(required = false) InsuranceStatus status,
        @RequestParam Boolean isCancelled,
        @RequestParam Boolean isSeperated,
        @RequestParam Boolean isExcluded,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all InsuranceRegistrations for admin");

        Page<InsuranceRegistrationAdminDTO> insuranceRegistrationDTOList = insuranceRegistrationService.findAllInsuranceRegistrationUsingFilter(
            searchText,
            year,
            month,
            status,
            isExcluded,
            isCancelled,
            isSeperated,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            insuranceRegistrationDTOList
        );

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, insuranceRegistrationDTOList.getContent(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(insuranceRegistrationDTOList.getContent());
    }

    @GetMapping("/insurance-registrations/{employeeId}/previous-registrations")
    public ResponseEntity<List<InsuranceRegistrationDTO>> getPreviousRegistrationsByEmployeeId(@PathVariable Long employeeId) {
        log.debug("REST request to get InsuranceRegistration by id");
        List<InsuranceRegistrationDTO> dtoList = insuranceRegistrationService.getInsuranceRegistrationListByEmployeeId(employeeId);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, dtoList, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/insurance-registrations/employee/{id}")
    public ResponseEntity<EmployeeDetailsDTOForInsuranceRegistration> getEmployeeDetailsForInsuranceRegistration(@PathVariable Long id) {
        log.debug("REST request to get employee details for InsuranceRegistration");
        EmployeeDetailsDTOForInsuranceRegistration employeeDetails = userInsuranceService.getEmployeeDetailsForInsuranceRegistrationByEmployeeId(
            id
        );

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeeDetails, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(employeeDetails);
    }

    @GetMapping("/insurance-registrations/find-by-insurance-card-id/{insuranceCardId}")
    public ResponseEntity<List<InsuranceRegistrationDTO>> getInsuranceRegistrationByCardId(@PathVariable @ValidateNumeric String insuranceCardId) {
        log.debug("REST request to get employee details for InsuranceRegistration");
        List<InsuranceRegistrationDTO> dtos = insuranceRegistrationService.findOneByInsuranceCardId(insuranceCardId);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, dtos, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/insurance-registrations/{id}")
    public ResponseEntity<InsuranceRegistrationDTO> getInsuranceRegistration(@PathVariable Long id) {
        log.debug("REST request to get InsuranceRegistration : {}", id);
        Optional<InsuranceRegistrationDTO> insuranceRegistrationDTO = insuranceRegistrationService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, insuranceRegistrationDTO, RequestMethod.GET, RESOURCE_NAME);

        return ResponseUtil.wrapOrNotFound(insuranceRegistrationDTO);
    }

    @DeleteMapping("/insurance-registrations/{id}")
    public ResponseEntity<Void> deleteInsuranceRegistration(@PathVariable Long id) {
        log.debug("REST request to delete InsuranceRegistration : {}", id);
        insuranceRegistrationService.delete(id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/insurance-registrations/approve")
    public ResponseEntity<InsuranceRegistrationDTO> approveInsuranceRegistration(@RequestBody InsuranceApprovalDTO insuranceApprovalDTO) {
        log.debug("REST request to approve InsuranceRegistration : {}");

        if (insuranceApprovalDTO.getStatus() == InsuranceStatus.PENDING) {
            User user = currentEmployeeService.getCurrentUser().get();

            InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationService.approveInsuranceRegistration(
                user,
                insuranceApprovalDTO
            );

            eventLoggingPublisher.publishEvent(user, insuranceRegistrationDTO, RequestMethod.POST, RESOURCE_NAME);

            return ResponseEntity.ok().body(insuranceRegistrationDTO);
        } else {
            throw new BadRequestAlertException(
                "Only pending requests are allowed to get approval",
                "InsuranceRegistration",
                "statusIsNotPending"
            );
        }
    }

    @GetMapping("/insurance-registrations/{insuranceCardId}/is-unique")
    public ResponseEntity<Boolean> isInsuranceCardIdUnique(@PathVariable String insuranceCardId, @RequestParam(required = false) Long id) {
        log.debug("REST request to get remaining relations bt employee Id : {}");

        boolean result = insuranceRegistrationService.isCardIdUnique(insuranceCardId, id);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/insurance-registrations/reject")
    public ResponseEntity<InsuranceRegistrationDTO> rejectInsuranceRegistration(@RequestBody InsuranceApprovalDTO insuranceApprovalDTO) {
        log.debug("REST request to reject InsuranceRegistration : {}");

        if (insuranceApprovalDTO.getStatus() == InsuranceStatus.PENDING) {
            User user = currentEmployeeService.getCurrentUser().get();

            InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationService.rejectInsuranceRegistration(
                user,
                insuranceApprovalDTO
            );

            eventLoggingPublisher.publishEvent(user, insuranceRegistrationDTO, RequestMethod.POST, RESOURCE_NAME);

            return ResponseEntity.ok().body(insuranceRegistrationDTO);
        } else {
            throw new BadRequestAlertException(
                "Only pending requests are allowed to get approval",
                "InsuranceRegistration",
                "statusIsNotPending"
            );
        }
    }

    @PostMapping("/insurance-registrations/cancel")
    public ResponseEntity<InsuranceRegistrationDTO> cancelInsuranceRegistration(@RequestBody InsuranceApprovalDTO insuranceApprovalDTO) {
        log.debug("REST request to cancel InsuranceRegistration : {}");

        if (insuranceApprovalDTO.getStatus() == InsuranceStatus.PENDING || insuranceApprovalDTO.getStatus() == InsuranceStatus.APPROVED) {
            User user = currentEmployeeService.getCurrentUser().get();
            InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationService.cancelInsuranceRegistration(
                user,
                insuranceApprovalDTO
            );

            eventLoggingPublisher.publishEvent(user, insuranceRegistrationDTO, RequestMethod.POST, RESOURCE_NAME);

            return ResponseEntity.ok().body(insuranceRegistrationDTO);
        } else {
            throw new RuntimeException();
        }
    }

    @GetMapping("/insurance-registrations/eligible-employees")
    public List<EmployeeMinimalDTO> getAllEligibleEmployeeForInsurance() {
        log.debug("REST request to get eligible employees for insurance registration : {}");

        List<EmployeeMinimalDTO> employeeMinimalDTOList = insuranceRegistrationService.getAllEligibleEmployees();

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeeMinimalDTOList, RequestMethod.GET, RESOURCE_NAME);

        return employeeMinimalDTOList;
    }

    @GetMapping("/insurance-registrations/{employeeId}/remaining-relations")
    public ResponseEntity<InsuranceRelationsDTO> getRemainingRelationsByEmployeeId(@PathVariable Long employeeId) {
        log.debug("REST request to get remaining relations bt employee Id : {}");

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee not found!", "Employee", "noEmployee");
        }

        InsuranceRelationsDTO dto = insuranceRegistrationService.getRemainingRelations(employeeId);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, dto, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/insurance-registrations/all-relations")
    public ResponseEntity<InsuranceRelationsDTO> getAllRelationsByEmployeeId() {
        log.debug("REST request to get remaining relations bt employee Id : {}");

        InsuranceRelationsDTO dto = insuranceRegistrationService.getAllRelations();

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, dto, RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/insurance-registrations/insurance-scheduled-service")
    public ResponseEntity<Boolean> runChildExceedMaxLimitAndEmployeeResignsCheckingService() {
        log.debug("REST request to run insurance scheduled service : {}");

        scheduledInsuranceService.excludeFromInsuranceIfEmployeeResigns();
        scheduledInsuranceService.excludeFromInsuranceIfChileAgeExceedMaxAgeLimit();

        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/insurance-registrations/reset-insurance-balance")
    public ResponseEntity<Boolean> resetInsuranceBalance() {
        log.debug("REST request to resetInsurance Balance : {}");

        scheduledInsuranceService.resetInsuranceClaimBalanceAtTheVeryFirstDayOfTheYear();

        return ResponseEntity.ok().body(true);
    }
}
