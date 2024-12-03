package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.finalSettlement.dto.DetailedPfStatement;
import com.bits.hr.service.finalSettlement.dto.PfStatement;
import com.bits.hr.service.finalSettlement.helperMethods.DetailedPfStatementService;
import com.bits.hr.service.finalSettlement.helperMethods.PfStatementGenerationService;
import com.bits.hr.service.userPfStatement.UserPfStatementService;
import com.bits.hr.service.userPfStatement.dto.UserPfStatementDTO;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/pf-mgt")
public class PfStatementResource {

    @Autowired
    private UserPfStatementService userPfStatementService;

    private final Logger log = LoggerFactory.getLogger(PfNomineeResource.class);

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private PfStatementGenerationService pfStatementGenerationService;

    @Autowired
    private DetailedPfStatementService detailedPfStatementService;

    @GetMapping("/pf-statement/{pfAccountId}/{date}")
    public ResponseEntity<UserPfStatementDTO> getPFStatementByPfAccountId(@PathVariable long pfAccountId, @PathVariable LocalDate date) {
        log.debug("REST request to get pf statement of ", pfAccountId, date);

        Optional<PfAccount> pfAccountOptional = pfAccountRepository.findById(pfAccountId);
        if (!pfAccountOptional.isPresent()) {
            throw new BadRequestAlertException("PFAccount not found", "PFAccount", "notFound");
        }

        UserPfStatementDTO userPfStatementDTO = userPfStatementService.getPfStatement(pfAccountOptional.get(), date);

        eventLoggingPublisher.publishEvent(
            currentEmployeeService.getCurrentUser().get(),
            Optional.of(userPfStatementDTO),
            RequestMethod.GET,
            "PfStatement"
        );
        return ResponseEntity.ok(userPfStatementDTO);
    }

    @GetMapping("/detailed-pf-amount-report/{pfCode}")
    public ResponseEntity<DetailedPfStatement> getPfStatement(
        @RequestParam int startingYear,
        @RequestParam int startingMonth,
        @RequestParam int endingYear,
        @RequestParam int endingMonth,
        @PathVariable String pfCode
    ) {
        Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPin(pfCode);
        if (!pfAccountOptional.isPresent()) {
            ResponseEntity.notFound();
        }
        Optional<DetailedPfStatement> detailedPfStatement = detailedPfStatementService.generate(
            pfAccountOptional.get(),
            startingYear,
            startingMonth,
            endingYear,
            endingMonth
        );
        return ResponseUtil.wrapOrNotFound(detailedPfStatement);
    }

    @GetMapping("/{pfCode}/get-list-of-years-from-pf-collection")
    public ResponseEntity<List<Integer>> getListOfYearsByEmployeePin(@PathVariable String pfCode) {
        Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPfCode(pfCode);
        if (!pfAccountOptional.isPresent()) {
            ResponseEntity.notFound();
        }

        List<Integer> listOfYears = pfCollectionRepository.getListOfYears(pfAccountOptional.get().getId());
        Collections.reverse(listOfYears);
        return ResponseEntity.ok().body(listOfYears);
    }
}
