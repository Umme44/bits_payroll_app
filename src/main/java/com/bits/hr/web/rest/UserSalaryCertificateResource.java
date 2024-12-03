package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryCertificateReportDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import com.bits.hr.service.salaryCertificates.NumberToWord;
import com.bits.hr.service.salaryCertificates.SalaryCertificateUserService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/common/salary-certificate")
public class UserSalaryCertificateResource {

    private final Logger log = LoggerFactory.getLogger(UserSalaryCertificateResource.class);

    private static final String ENTITY_NAME = "salaryCertificate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalaryCertificateUserService salaryCertificateUserService;
    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    public UserSalaryCertificateResource(SalaryCertificateUserService salaryCertificateUserService, CurrentEmployeeService currentEmployeeService, EventLoggingPublisher eventLoggingPublisher) {
        this.salaryCertificateUserService = salaryCertificateUserService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @PostMapping("")
    public ResponseEntity<SalaryCertificateDTO> applySalaryCertificate(
        @RequestBody SalaryCertificateDTO salaryCertificateDTO)
        throws URISyntaxException {
        log.debug("REST request to apply for new SalaryCertificate : {}", salaryCertificateDTO);
        if (salaryCertificateDTO.getId() != null) {
            throw new BadRequestAlertException("A new salaryCertificate cannot already have an ID",
                ENTITY_NAME, "idexists");
        }
        salaryCertificateDTO.setStatus(Status.PENDING);
        salaryCertificateDTO.setCreatedAt(LocalDate.now());
        SalaryCertificateDTO result = salaryCertificateUserService.apply(salaryCertificateDTO).get();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "UserSalaryCertificate");
        return ResponseEntity.created(new URI("/api/common/salary-certificate" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                result.getId().toString()))
            .body(result);
    }

    @PutMapping("")
    public ResponseEntity<SalaryCertificateDTO> editSalaryCertificate(
        @RequestBody SalaryCertificateDTO salaryCertificateDTO) {
        log.debug("REST request to edit applied new Salary Certificate: {}", salaryCertificateDTO);
        if (salaryCertificateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        salaryCertificateDTO.setStatus(Status.PENDING);
        SalaryCertificateDTO result = salaryCertificateUserService.update(salaryCertificateDTO).get();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "UserSalaryCertificate");
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                salaryCertificateDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("")
    public ResponseEntity<List<SalaryCertificateDTO>> getAllAppliedSalaryCertificate() {
        log.debug("REST request to get a list of applied SalaryCertificate");
        List<SalaryCertificateDTO> result = salaryCertificateUserService.getAllApplied();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserSalaryCertificate");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaryCertificateDTO> getSalaryCertificate(@PathVariable Long id){
        log.debug("REST Request to get SalaryCertificateDTO for salary certificate id");
        SalaryCertificateDTO result = salaryCertificateUserService.findSalaryCertificate(id);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalaryCertificate(@PathVariable Long id) throws Exception {
        log.debug("REST request to delete SalaryCertificate : {}", id);



        salaryCertificateUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/list-of-salaries")
    public ResponseEntity<List<EmployeeSalaryDTO>> getEmployeeListOfSalaries() {
        List<EmployeeSalaryDTO> result = salaryCertificateUserService.getSalariesForDropDown();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employee-salary/{id}")
    public ResponseEntity<EmployeeSalaryDTO> getEmployeeSalaryByCertificateId(@PathVariable Long id) {
        log.debug("REST Request get EmployeeSalaryDTO by salary certificate id");
        EmployeeSalaryDTO  result = salaryCertificateUserService.getSalaryForSalaryCertificates(id).get();
        result.setNetPayInWords(NumberToWord.convertNumberToWord(result.getNetPay().longValue()));
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/salary-certificate-report/{id}")
    public ResponseEntity<EmployeeSalaryCertificateReportDTO> getSalaryCertificateReportByCertificateId(@PathVariable Long id) {
        log.debug("REST Request get EmployeeSalaryDTO by salary certificate id");
        EmployeeSalaryCertificateReportDTO result = salaryCertificateUserService.getSalaryCertificateReportByCertificateId(id);
        result.setNetPayInWords(NumberToWord.convertNumberToWord(result.getNetPay().longValue()));
        return ResponseEntity.ok().body(result);
    }
}
