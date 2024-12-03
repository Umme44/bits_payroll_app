package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.PfNomineeFormService;
import com.bits.hr.service.PfNomineeService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDetailsNomineeReportDTO;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.NomineeUpdateEvent;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.NomineeFileService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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

@RestController
@RequestMapping("/api/common/pf-nominees-form")
public class PfNomineeFormResource {

    private static final String ENTITY_NAME = "pfNominee";

    private final Logger log = LoggerFactory.getLogger(PfNomineeResource.class);
    private final PfNomineeService pfNomineeService;
    private final PfNomineeFormService pfNomineeFormService;
    private final EmployeeRepository employeeRepository;
    private final CurrentEmployeeService currentEmployeeService;
    private final PfAccountRepository pfAccountRepository;
    private final FileOperationService fileOperationService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NomineeFileService nomineeFileService;

    public PfNomineeFormResource(
        PfNomineeService pfNomineeService,
        PfNomineeFormService pfNomineeFormService,
        EmployeeRepository employeeRepository,
        CurrentEmployeeService currentEmployeeService,
        PfAccountRepository pfAccountRepository,
        NomineeFileService nomineeFileService,
        FileOperationService fileOperationService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.pfNomineeService = pfNomineeService;
        this.pfNomineeFormService = pfNomineeFormService;
        this.employeeRepository = employeeRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.pfAccountRepository = pfAccountRepository;
        this.nomineeFileService = nomineeFileService;
        this.fileOperationService = fileOperationService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @GetMapping("/current-employee-details")
    public ResponseEntity<EmployeeDetailsNomineeReportDTO> getCurrentEmployeeDetails() {
        String pin = currentEmployeeService.getCurrentEmployeePin().get();
        EmployeeDetailsNomineeReportDTO employeeDetailsDTO = pfNomineeFormService.getEmployeeDetailsByPin(pin);
        return ResponseEntity.ok(employeeDetailsDTO);
    }

    @PostMapping("")
    public ResponseEntity<PfNomineeDTO> createPfNominee(
        @RequestPart(name = "file") MultipartFile file,
        @RequestPart(name = "pfNominee") PfNomineeDTO pfNomineeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save :{}", pfNomineeDTO);
        if (pfNomineeDTO.getId() != null) {
            throw new BadRequestAlertException("A new pfNominee cannot have an ID", ENTITY_NAME, "idexists");
        }

        pfNomineeDTO = pfNomineeFormService.validateNomineeAndGuardianNID(pfNomineeDTO);

        pfNomineeDTO = pfNomineeFormService.validateIsApproved(pfNomineeDTO);

        pfNomineeDTO.setNominationDate(LocalDate.now());

        PfNomineeDTO result = pfNomineeFormService.save(file, pfNomineeDTO, true);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "PfNomineeForm");
        return ResponseEntity
            .created(new URI("/api/common/pf/pf-nominees-form/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/multipart")
    public ResponseEntity<PfNomineeDTO> updateNominee(
        @RequestPart(name = "file") MultipartFile file,
        @RequestPart(name = "pfNominee") PfNomineeDTO pfNomineeDTO
    ) {
        log.debug("REST request to update PfNominee: {}", pfNomineeDTO);
        if (pfNomineeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        pfNomineeDTO = pfNomineeFormService.validateNomineeAndGuardianNID(pfNomineeDTO);

        pfNomineeDTO = pfNomineeFormService.validateIsApproved(pfNomineeDTO);

        PfNomineeDTO result = pfNomineeFormService.save(file, pfNomineeDTO, true);
        User user = currentEmployeeService.getCurrentUser().get();

        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.PUT, "PfNomineeForm");

        // publish event for email
        publishEvent(result.getPin(), "provident fund", EventType.UPDATED);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfNomineeDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("")
    public ResponseEntity<PfNomineeDTO> updateNomineeWithoutFile(@RequestBody PfNomineeDTO pfNomineeDTO) throws URISyntaxException {
        log.debug("REST request to update PfNominee: {}", pfNomineeDTO);

        if (pfNomineeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        pfNomineeDTO = pfNomineeFormService.validateNomineeAndGuardianNID(pfNomineeDTO);

        pfNomineeDTO = pfNomineeFormService.validateIsApproved(pfNomineeDTO);

        PfNomineeDTO result = pfNomineeFormService.save(pfNomineeDTO);

        publishEvent(result.getPin(), "provident fund", EventType.UPDATED);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pfNomineeDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("")
    public ResponseEntity<List<PfNomineeDTO>> getAllPfNominees(@org.springdoc.api.annotations.ParameterObject Pageable pageable)
        throws Exception {
        log.debug("REST request to get a page of PfNominees");
        Page<PfNomineeDTO> page = pfNomineeFormService.findPfNomineesOfCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfNomineeForm");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/list")
    public ResponseEntity<List<PfNomineeDTO>> getAllPfNominees() throws Exception {
        log.debug("REST request to get list of PfNominees of current user");
        List<PfNomineeDTO> pfNomineeDTOList = pfNomineeFormService.findAll();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfNomineeForm");
        return ResponseEntity.ok(pfNomineeDTOList);
    }

    @GetMapping("/consumed-share-percentage")
    public ResponseEntity<Double> getTotalConsumedSharedPercentage() throws Exception {
        log.debug("REST request to get list of PfNominees of current user");

        double consumedSharedPercentage = pfNomineeFormService.getTotalConsumedSharedPercentageOfCurrentUser();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfNomineeForm");
        return ResponseEntity.ok(consumedSharedPercentage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PfNomineeDTO> getPfNominee(@PathVariable Long id) throws IOException {
        log.debug("REST request to get PfNominee: {}", id);
        Optional<PfNomineeDTO> pfNomineeDTO = pfNomineeFormService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (pfNomineeDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, pfNomineeDTO.get(), RequestMethod.GET, "PfNomineeForm");
        }
        return ResponseUtil.wrapOrNotFound(pfNomineeDTO);
    }

    @GetMapping("/pf-accounts")
    public ResponseEntity<List<PfAccountDTO>> getCurrentEmployeePfAccount(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get current employee pf accounts:");

        List<PfAccountDTO> pfAccountList = pfNomineeFormService.findPfAccountsByPin();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "PfNomineeForm");
        return ResponseEntity.ok().body(pfAccountList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePfNominee(@PathVariable Long id) throws IOException {
        log.debug("REST request to delete PfNominee : {}", id);
        Optional<PfNomineeDTO> pfNomineeDTO = pfNomineeFormService.findOne(id);
        if (pfNomineeDTO.isPresent()) {
            User user = currentEmployeeService.getCurrentUser().get();
            eventLoggingPublisher.publishEvent(user, pfNomineeDTO.get(), RequestMethod.DELETE, "PfNomineeForm");
        }

        pfNomineeService.deleteForCommonUserNominee(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private void publishEvent(String pin, String nomineeType, EventType event) {
        Employee employee = employeeRepository.findByPin(pin).get();

        log.info("publishing email event for nominee update with : " + event);
        NomineeUpdateEvent nomineeUpdateEvent = new NomineeUpdateEvent(this, employee, nomineeType, event);
        applicationEventPublisher.publishEvent(nomineeUpdateEvent);
    }

    @GetMapping("/is-employee-eligible")
    public ResponseEntity<Boolean> isEmployeeEligibleForPF() {
        log.debug("REST request to check employee eligibility for PF : {}");

        boolean result = pfNomineeService.isEmployeeEligibleForPF();
        return ResponseEntity.ok().body(result);
    }
}
