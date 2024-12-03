package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.SalaryCertificateRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.SalaryCertificateService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryCertificateReportDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import com.bits.hr.service.event.EmployeeSalaryCertificateApplicationEvent;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.mapper.SalaryCertificateMapper;
import com.bits.hr.service.salaryCertificates.NumberToWord;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/common/")
public class SalaryCertificateCommonResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(SalaryCertificateCommonResource.class);

    private static final String ENTITY_NAME = "salaryCertificate";

    @Autowired
    private SalaryCertificateService salaryCertificateService;

    @Autowired
    private SalaryCertificateRepository salaryCertificateRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SalaryCertificateMapper salaryCertificateMapper;


    @GetMapping("/salary-certificates/list-of-salaries")
    public ResponseEntity<List<EmployeeSalaryDTO>> getEmployeeListOfSalaries() {
        log.debug("REST request to get list of salary years");

        Optional<Long> employeeIdOptional=currentEmployeeService.getCurrentEmployeeId();
        if (!employeeIdOptional.isPresent()){
            throw new BadRequestAlertException("User Not Fount", "SalaryCertificate", "internalServerError");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "SalaryCertificate");

        List<EmployeeSalaryDTO> result = salaryCertificateService.getSalariesForDropDown(employeeIdOptional.get());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/salary-certificates/all")
    public ResponseEntity<List<SalaryCertificateDTO>> getAllSalaryCertificates(
        @RequestParam(required = false) CertificateStatus status,
        Pageable pageable
    ) {
        log.debug("REST request to get current employee SalaryCertificates");

        User user = currentEmployeeService.getCurrentUser().get();

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if(!employeeOptional.isPresent()){
            throw new NoEmployeeProfileException();
        }

        Page<SalaryCertificateDTO> page = salaryCertificateService.findAllFilterByStatus(employeeOptional.get().getId(), status,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "SalaryCertificate");

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/salary-certificates")
    public ResponseEntity<?> createSalaryCertificate(@Valid @RequestBody SalaryCertificateDTO salaryCertificateDTO) throws URISyntaxException {
        log.debug("REST request to save SalaryCertificate : {}", salaryCertificateDTO);

        if (salaryCertificateDTO.getId() != null) {
            throw new BadRequestAlertException("A new salaryCertificate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<User> currentUser = currentEmployeeService.getCurrentUser();
        if(!currentUser.isPresent()){
            throw new BadRequestAlertException("User Not Fount", "SalaryCertificate", "internalServerError");
        }
        Optional<Employee> currentEmployee = currentEmployeeService.getCurrentEmployee();
        if(!currentEmployee.isPresent()){
            throw new BadRequestAlertException("Employee Not Fount", "SalaryCertificate", "internalServerError");
        }

        salaryCertificateDTO.setEmployeeId(currentEmployee.get().getId());
        salaryCertificateDTO.setEmployeeName(currentEmployee.get().getFullName());
        salaryCertificateDTO.setStatus(Status.PENDING);
        salaryCertificateDTO.setCreatedById(currentUser.get().getId());
        salaryCertificateDTO.setCreatedAt(LocalDate.now());

        if(salaryCertificateService.isSalaryCertificateExistsForEmployee(salaryCertificateDTO)){
            return ResponseEntity.badRequest().body("Salary Certificate already exists!");
        }

        SalaryCertificateDTO result = salaryCertificateService.save(salaryCertificateDTO);

        SalaryCertificate salaryCertificate = salaryCertificateRepository.findById(result.getId()).get();

        eventLoggingPublisher.publishEvent(currentUser.get(), result, RequestMethod.POST, "SalaryCertificate");
        publishEvent(salaryCertificate, EventType.CREATED);

        return ResponseEntity.created(new URI("/api/salary-certificates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/salary-certificates")
    public ResponseEntity<SalaryCertificateDTO> updateSalaryCertificate(@Valid @RequestBody SalaryCertificateDTO salaryCertificateDTO) throws URISyntaxException {
        log.debug("REST request to update SalaryCertificate : {}", salaryCertificateDTO);
        if (salaryCertificateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<User> currentUser = currentEmployeeService.getCurrentUser();
        if(!currentUser.isPresent()){
            throw new BadRequestAlertException("User Not Fount", "SalaryCertificate", "internalServerError");
        }

        salaryCertificateDTO.setUpdatedById(currentUser.get().getId());
        salaryCertificateDTO.setUpdatedAt(LocalDate.now());

        SalaryCertificateDTO result = salaryCertificateService.save(salaryCertificateDTO);

        User user = currentEmployeeService.getCurrentUser().get();

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "SalaryCertificate");

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salaryCertificateDTO.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/salary-certificates/{id}")
    public ResponseEntity<Void> deleteSalaryCertificate(@PathVariable Long id) {

        log.debug("REST request to delete SalaryCertificate : {}", id);

        Optional<SalaryCertificateDTO> salaryCertificateDTO = salaryCertificateService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (salaryCertificateDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, salaryCertificateDTO.get(), RequestMethod.DELETE, "SalaryCertificate");
        }

        salaryCertificateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/salary-certificates/{id}")
    public ResponseEntity<SalaryCertificateDTO> getSalaryCertificate(@PathVariable Long id) {

        log.debug("REST request to get SalaryCertificate : {}", id);

        Optional<SalaryCertificateDTO> salaryCertificateDTO = salaryCertificateService.getSalaryCertificateById(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (salaryCertificateDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, salaryCertificateDTO.get(), RequestMethod.GET, "SalaryCertificate");
        }
        return ResponseUtil.wrapOrNotFound(salaryCertificateDTO);
    }

    @GetMapping("/salary-certificates/{id}/employee-salary")
    public ResponseEntity<EmployeeSalaryDTO> getEmployeeSalaryFormCertificate(@PathVariable Long id) {
        log.debug("REST Request get EmployeeSalaryDTO by salary certificate id");
        EmployeeSalaryDTO  result = salaryCertificateService.getSalaryForSalaryCertificates(id).get();
        result.setNetPayInWords(NumberToWord.convertNumberToWord(result.getNetPay().longValue()));

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "SalaryCertificate");

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/salary-certificates/{id}/salary-certificate-report")
    public ResponseEntity<EmployeeSalaryCertificateReportDTO> getSalaryCertificateReportByCertificateId(@PathVariable Long id) {
        log.debug("REST Request get EmployeeSalaryDTO by salary certificate id");
        EmployeeSalaryCertificateReportDTO result = salaryCertificateService.getSalaryCertificateReportByCertificateId(id);
        result.setNetPayInWords(NumberToWord.convertNumberToWord(result.getNetPay().longValue()));

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "SalaryCertificate");

        return ResponseEntity.ok().body(result);
    }

    private void publishEvent(SalaryCertificate salaryCertificate, EventType eventType){
        log.debug("publishing employee salary certificate application event with event: " + eventType);
        EmployeeSalaryCertificateApplicationEvent event = new EmployeeSalaryCertificateApplicationEvent(this, salaryCertificate, eventType);
        applicationEventPublisher.publishEvent(event);
    }

}
