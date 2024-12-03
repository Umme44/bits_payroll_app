package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.InsuranceClaimService;
import com.bits.hr.service.UserInsuranceService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.InsuranceClaimDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/common")
public class InsuranceClaimCommonResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceClaimResource.class);

    private static final String ENTITY_NAME = "insuranceClaim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsuranceClaimService insuranceClaimService;
    private final UserInsuranceService userInsuranceService;
    private final EventLoggingPublisher eventLoggingPublisher;
    private final CurrentEmployeeService currentEmployeeService;
    private final InsuranceRegistrationRepository insuranceRegistrationRepository;

    public InsuranceClaimCommonResource(
        InsuranceClaimService insuranceClaimService,
        UserInsuranceService userInsuranceService,
        EventLoggingPublisher eventLoggingPublisher,
        CurrentEmployeeService currentEmployeeService,
        InsuranceRegistrationRepository insuranceRegistrationRepository
    ) {
        this.insuranceClaimService = insuranceClaimService;
        this.userInsuranceService = userInsuranceService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.currentEmployeeService = currentEmployeeService;
        this.insuranceRegistrationRepository = insuranceRegistrationRepository;
    }

    @GetMapping("/insurance-claims")
    public ResponseEntity<List<InsuranceClaimDTO>> getAllInsuranceClaims() {
        log.debug("REST request to get a page of InsuranceClaims");

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "Employee", "noEmployee");
        }

        List<InsuranceClaimDTO> insuranceClaimDTOList = userInsuranceService.getAllInsuranceClaims(employeeOptional.get().getPin());

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "insuranceClaim");
        return ResponseEntity.ok().body(insuranceClaimDTOList);
    }

    @GetMapping("/insurance-claims/{claimId}")
    public ResponseEntity<InsuranceClaimDTO> getInsuranceClaim(@PathVariable Long claimId) {
        log.debug("REST request to get InsuranceClaim : {}", claimId);
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("Employee Not Found", "Employee", "noEmployee");
        }

        InsuranceClaimDTO insuranceClaimDTO = userInsuranceService.getInsuranceClaimById(claimId, employeeOptional.get().getPin());

        Optional<InsuranceRegistration> insuranceRegistration = insuranceRegistrationRepository.findById(
            insuranceClaimDTO.getInsuranceRegistrationId()
        );

        if (!insuranceRegistration.isPresent()) {
            throw new BadRequestAlertException("insuranceRegistration is not found", ENTITY_NAME, "accessForbidden");
        }

        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        if (!insuranceRegistration.get().getEmployee().getId().equals(currentEmployeeId.get())) {
            throw new BadRequestAlertException("Access Forbidden", ENTITY_NAME, "accessForbidden");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "insuranceClaim");
        return ResponseEntity.ok().body(insuranceClaimDTO);
    }
}
