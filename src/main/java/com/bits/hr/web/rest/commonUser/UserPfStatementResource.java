package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.userPfStatement.UserPfStatementService;
import com.bits.hr.service.userPfStatement.dto.UserPfStatementDTO;
import com.bits.hr.web.rest.PfNomineeResource;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/common")
public class UserPfStatementResource {

    private final Logger log = LoggerFactory.getLogger(PfNomineeResource.class);

    @Autowired
    private UserPfStatementService userPfStatementService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @GetMapping("/my-pf-statement/{date}")
    public ResponseEntity<UserPfStatementDTO> getUserPFStatement(@PathVariable LocalDate date) {
        log.debug("REST request to get user pf statement");
        String pin = currentEmployeeService.getCurrentEmployeePin().get();

        Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPin(pin);
        if (!pfAccountOptional.isPresent()) {
            throw new BadRequestAlertException("PFAccount not found", "PFAccount", "notFound");
        }

        UserPfStatementDTO userPfStatementDTO = userPfStatementService.getPfStatement(pfAccountOptional.get(), date);

        eventLoggingPublisher.publishEvent(
            currentEmployeeService.getCurrentUser().get(),
            Optional.empty(),
            RequestMethod.GET,
            "UserPfStatement"
        );

        return ResponseEntity.ok(userPfStatementDTO);
    }

    @GetMapping("/user-pf-statement/validity")
    public boolean checkValidityOfUserPfStatement() {
        log.debug("REST request to check validity for user pf statement");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "UserPfStatement");
        return userPfStatementService.checkValidityOfUserPfStatement();
    }
}
