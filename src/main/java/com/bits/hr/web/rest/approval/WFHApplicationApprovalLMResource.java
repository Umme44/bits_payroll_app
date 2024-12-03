package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.WorkFromHomeApplicationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeApplicationLMService;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.web.rest.WorkFromHomeApplicationCommonResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/common")
@Validated
public class WFHApplicationApprovalLMResource {

    private final Logger log = LoggerFactory.getLogger(WorkFromHomeApplicationCommonResource.class);

    private static final String ENTITY_NAME = "WFHApplicationApprovalLMResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkFromHomeApplicationService workFromHomeApplicationService;

    private final CurrentEmployeeService currentEmployeeService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final EventLoggingPublisher eventLoggingPublisher;

    private final WorkFromHomeApplicationLMService workFromHomeApplicationLMService;

    public WFHApplicationApprovalLMResource(
        WorkFromHomeApplicationService workFromHomeApplicationService,
        CurrentEmployeeService currentEmployeeService,
        ApplicationEventPublisher applicationEventPublisher,
        EventLoggingPublisher eventLoggingPublisher,
        WorkFromHomeApplicationLMService workFromHomeApplicationLMService
    ) {
        this.workFromHomeApplicationService = workFromHomeApplicationService;
        this.currentEmployeeService = currentEmployeeService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.workFromHomeApplicationLMService = workFromHomeApplicationLMService;
    }

    @PutMapping("/work-from-home-applications/enable-selected")
    public boolean enableSelectedLM(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to enable selected online attendances.");
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        User user = currentEmployeeService.getCurrentUser().get();
        long currentEmployeeId;
        if (employee.isPresent()) {
            currentEmployeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        return workFromHomeApplicationLMService.enableSelectedLM(approvalDTO.getListOfIds(), currentEmployeeId);
    }

    //Line Manager Reject
    @PutMapping("/work-from-home-applications/disable-selected")
    public boolean disableSelectedLM(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        User currentUser = currentEmployeeService.getCurrentUser().get();
        long currentEmployeeId;
        if (employee.isPresent()) {
            currentEmployeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        return workFromHomeApplicationLMService.disableSelectedLM(approvalDTO.getListOfIds(), currentEmployeeId);
    }

    // get all subordinate employee List for work-from-home admin approval page(LM)
    @GetMapping("/work-from-home-applications/all-sub-ordinate-employees")
    public ResponseEntity<List<WorkFromHomeApplicationDTO>> getAllSubOrdinateEmployees(
        @RequestParam(required = false) String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws URISyntaxException {
        User user = currentEmployeeService.getCurrentUser().get();
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        long currentEmployeeId;
        if (employee.isPresent()) {
            currentEmployeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        if (searchText == null) {
            searchText = "";
        }
        Page<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOS = workFromHomeApplicationLMService.getAllSubOrdinateApplications(
            searchText,
            employee.get(),
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            workFromHomeApplicationDTOS
        );
        return ResponseEntity.ok().headers(headers).body(workFromHomeApplicationDTOS.getContent());
    }

    // get all  pending subordinate employee List for work-from-home admin approval page(LM)
    @GetMapping("/work-from-home-applications/pending-sub-ordinate-employees")
    public ResponseEntity<?> getPendingSubOrdinateEmployees(
        @RequestParam(required = false) @ValidateNaturalText String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws URISyntaxException {
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        User user = currentEmployeeService.getCurrentUser().get();
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        if (searchText == null) {
            searchText = "";
        }
        Page<WorkFromHomeApplicationDTO> workFromHomeApplicationDTOS = workFromHomeApplicationLMService.getAllPendingSubOrdinateApplications(
            employeeId,
            searchText,
            pageable
        );

        List<WorkFromHomeApplicationDTO> finalWorkFromHomeApplicationList = new ArrayList<>(workFromHomeApplicationDTOS.getContent());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            workFromHomeApplicationDTOS
        );
        return ResponseEntity.ok().headers(headers).body(finalWorkFromHomeApplicationList);
    }
}
