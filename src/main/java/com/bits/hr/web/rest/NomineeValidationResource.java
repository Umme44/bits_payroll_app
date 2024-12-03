package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.NomineeService;
import com.bits.hr.service.PfNomineeFormService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.NomineeDTO;
import com.bits.hr.service.dto.NomineeValidationDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import java.net.URISyntaxException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.bits.hr.domain.Nominee}.
 */
@RestController
@RequestMapping("/api/common")
public class NomineeValidationResource {

    private final Logger log = LoggerFactory.getLogger(NomineeValidationResource.class);

    private final NomineeService nomineeService;
    private final PfNomineeFormService pfNomineeFormService;

    private final EventLoggingPublisher eventLoggingPublisher;
    private final CurrentEmployeeService currentEmployeeService;

    public NomineeValidationResource(
        NomineeService nomineeService,
        PfNomineeFormService pfNomineeFormService,
        EventLoggingPublisher eventLoggingPublisher,
        CurrentEmployeeService currentEmployeeService
    ) {
        this.nomineeService = nomineeService;
        this.pfNomineeFormService = pfNomineeFormService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.currentEmployeeService = currentEmployeeService;
    }

    @PostMapping("/validate-nominee")
    public ResponseEntity<NomineeValidationDTO> nomineeValidation(@RequestBody NomineeDTO nomineeDTO) throws URISyntaxException {
        log.debug("REST request to validate Nominee : {}", nomineeDTO);

        Optional<Long> employeeIdOptional = currentEmployeeService.getCurrentEmployeeId();
        if (!employeeIdOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        nomineeDTO.setEmployeeId(employeeIdOptional.get());
        NomineeValidationDTO result = nomineeService.validateRemainingSharePercentage(nomineeDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "NomineeValidation");
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/validate-pf-nominee")
    public ResponseEntity<NomineeValidationDTO> createNominee(@RequestBody PfNomineeDTO pfNomineeDTO) throws URISyntaxException {
        log.debug("REST request to validate PF Nominee : {}", pfNomineeDTO);

        NomineeValidationDTO result = pfNomineeFormService.validateRemainingSharePercentage(pfNomineeDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "NomineeValidation");
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/validate-nominee-admin")
    public ResponseEntity<NomineeValidationDTO> nomineeValidationForAdmin(@RequestBody NomineeDTO nomineeDTO) throws URISyntaxException {
        log.debug("REST request to validate Nominee : {}", nomineeDTO);

        NomineeValidationDTO result = nomineeService.validateRemainingSharePercentageForAdmin(nomineeDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "NomineeValidation");
        return ResponseEntity.ok().body(result);
    }
}
