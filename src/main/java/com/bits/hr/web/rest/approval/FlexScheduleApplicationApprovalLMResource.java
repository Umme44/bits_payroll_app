package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.approvalProcess.FlexScheduleApplicationApprovalLMService;
import com.bits.hr.service.approvalProcess.PendingApplicationService;
import com.bits.hr.service.approvalProcess.WorkFromHomeEnableLMService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.EmployeeApprovalDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@Log4j2
@RequestMapping("api/common/flex-schedule-applications-approval-lm")
public class FlexScheduleApplicationApprovalLMResource {

    @Autowired
    private FlexScheduleApplicationApprovalLMService flexScheduleApplicationApprovalLMService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private EmployeeMinimalMapper employeeMinimalMapper;

    private static final String RESOURCE_NAME = "FlexScheduleApplicationApprovalLMResource";

    @GetMapping("/pending")
    public List<FlexScheduleApplicationDTO> getPendingList() {
        log.debug("REST request to pending flex schedule applications.");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        //pending my subordinate flexScheduleApplications
        List<FlexScheduleApplicationDTO> flexScheduleApplicationDTOList = flexScheduleApplicationApprovalLMService.getAllPending(
            currentEmployeeService.getCurrentEmployeeId().get()
        );

        List<FlexScheduleApplicationDTO> finalFlexScheduleApplicationDTOS = new ArrayList<>();
        finalFlexScheduleApplicationDTOS.addAll(flexScheduleApplicationDTOList);
        return finalFlexScheduleApplicationDTOS;
    }

    @GetMapping("/approved-by-me")
    public ResponseEntity<List<FlexScheduleApplicationDTO>> getApprovedByMeList(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) Long requesterId
    ) {
        log.debug("REST request to approve flex schedule applications by me");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        Page<FlexScheduleApplicationDTO> page = flexScheduleApplicationApprovalLMService.getAllApprovedByUser(pageable, requesterId, user);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/approved-employee-list")
    public List<EmployeeMinimalDTO> getEmployeeListOfApprovedByMe() {
        log.debug("REST request to approve selected flex schedule applications.");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        long sanctionedBy = user.getId();

        List<Employee> employeeList = flexScheduleApplicationRepository.findAllEmployeeOfFlexScheduleApprovedByUser(sanctionedBy);
        return employeeMinimalMapper.toDto(employeeList);
    }

    @PutMapping("/approve-selected")
    public boolean approveSelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approve selected flex schedule applications.");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, approvalDTO, RequestMethod.PUT, RESOURCE_NAME);
        long reportingToId = currentEmployeeService.getCurrentEmployeeId().get();
        long currentEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
        try {
            flexScheduleApplicationApprovalLMService.approveSelected(
                reportingToId,
                approvalDTO.getListOfIds(),
                Instant.now(),
                user,
                currentEmployeeId
            );
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    @PutMapping("/deny-selected")
    public boolean denySelected(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to deny selected flex schedule applications.");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, approvalDTO, RequestMethod.PUT, RESOURCE_NAME);
        long reportingToId = currentEmployeeService.getCurrentEmployeeId().get();
        long currentEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
        try {
            flexScheduleApplicationApprovalLMService.denySelected(
                reportingToId,
                approvalDTO.getListOfIds(),
                Instant.now(),
                user,
                currentEmployeeId
            );
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
