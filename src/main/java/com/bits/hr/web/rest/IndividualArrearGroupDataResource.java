package com.bits.hr.web.rest;

import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.IndividualArrearSalaryRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.IndividualArrearSalaryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.IndividualArrearSalaryDTO;
import com.bits.hr.service.dto.IndividualArrearSalaryGroupDataDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/payroll-mgt")
public class IndividualArrearGroupDataResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String RESOURCE_NAME = "individual-arrears-group-by-title";
    private static final String ENTITY_NAME = "individualArrearSalary";

    private final IndividualArrearSalaryService individualArrearSalaryService;
    private final IndividualArrearSalaryRepository individualArrearSalaryRepository;
    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public IndividualArrearGroupDataResource(
        IndividualArrearSalaryService individualArrearSalaryService,
        IndividualArrearSalaryRepository individualArrearSalaryRepository,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.individualArrearSalaryService = individualArrearSalaryService;
        this.individualArrearSalaryRepository = individualArrearSalaryRepository;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @GetMapping("/individual-arrears-group-by-title")
    public List<IndividualArrearSalaryGroupDataDTO> getGroupData() {
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return individualArrearSalaryService.getGroupTitles();
    }

    @DeleteMapping("/individual-arrears-group-by-title/{title}")
    public ResponseEntity<Void> deleteIndividualArrearSalaryByTitle(@PathVariable String title) {
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.getAllByTitle(title.trim());
        individualArrearSalaryRepository.deleteAll(individualArrearSalaryList);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.DELETE, RESOURCE_NAME);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, title)).build();
    }

    @GetMapping("/individual-arrears-group-by-title/{title}")
    public ResponseEntity<List<IndividualArrearSalaryDTO>> getByTitle(Pageable pageable, @PathVariable String title) {
        Page<IndividualArrearSalaryDTO> page = individualArrearSalaryService.getAllByTitle(pageable, title);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
