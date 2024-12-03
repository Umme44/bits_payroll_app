package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.WorkFromHomeApplicationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.scheduler.schedulingService.AutomatedWFHRejectSchedulerService;
import com.bits.hr.service.scheduler.schedulingService.WorkFromHomeSchedulerService;
import com.bits.hr.service.workFromHomeApplication.WorkFromHomeApplicationHRService;
import com.bits.hr.web.rest.WorkFromHomeApplicationCommonResource;
import java.net.URISyntaxException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee-mgt")
public class WFHApplicationApprovalHRResource {

    private final Logger log = LoggerFactory.getLogger(WorkFromHomeApplicationCommonResource.class);

    private static final String ENTITY_NAME = "WFHApplicationApprovalHRResource";

    private WorkFromHomeSchedulerService workFromHomeSchedulerService;

    private AutomatedWFHRejectSchedulerService automatedWFHRejectSchedulerService;
    private final WorkFromHomeApplicationService workFromHomeApplicationService;
    private final WorkFromHomeApplicationHRService workFromHomeApplicationHRService;

    private final CurrentEmployeeService currentEmployeeService;

    private final ApplicationEventPublisher applicationEventPublisher;

    public WFHApplicationApprovalHRResource(
        WorkFromHomeSchedulerService workFromHomeSchedulerService,
        AutomatedWFHRejectSchedulerService automatedWFHRejectSchedulerService,
        WorkFromHomeApplicationService workFromHomeApplicationService,
        WorkFromHomeApplicationHRService workFromHomeApplicationHRService,
        CurrentEmployeeService currentEmployeeService,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        this.workFromHomeSchedulerService = workFromHomeSchedulerService;
        this.automatedWFHRejectSchedulerService = automatedWFHRejectSchedulerService;
        this.workFromHomeApplicationService = workFromHomeApplicationService;
        this.workFromHomeApplicationHRService = workFromHomeApplicationHRService;
        this.currentEmployeeService = currentEmployeeService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PutMapping("/work-from-home-applications/enable-selected")
    public boolean enableSelectedHR(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to enable selected work from home applications.");
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        User currentUser = currentEmployeeService.getCurrentUser().get();
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        return workFromHomeApplicationHRService.enableSelectedHR(approvalDTO.getListOfIds(), currentUser);
    }

    //Line Manager Reject
    @PutMapping("/work-from-home-applications/disable-selected")
    public boolean disableSelectedHR(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        User currentUser = currentEmployeeService.getCurrentUser().get();
        long employeeId;
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        } else {
            throw new NoEmployeeProfileException();
        }
        return workFromHomeApplicationHRService.disableSelectedHR(approvalDTO.getListOfIds(), currentUser);
    }
}
