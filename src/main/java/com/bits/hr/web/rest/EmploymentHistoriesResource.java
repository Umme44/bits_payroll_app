package com.bits.hr.web.rest;

import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import com.bits.hr.service.employmentHistories.EmploymentHistoriesService;
import com.bits.hr.service.mapper.EmploymentHistoryMapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee-mgt/employment-histories-by-user")
public class EmploymentHistoriesResource {

    private static final String ENTITY_NAME = "employmentHistory";
    private static final String RESOURCE_NAME = "EmploymentHistoryResource";

    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    @Autowired
    private EmploymentHistoriesService employmentHistoriesService;

    @Autowired
    private EmploymentHistoryMapper employmentHistoryMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    private final Logger log = LoggerFactory.getLogger(EmploymentHistoryResource.class);

    @GetMapping("{employeeId}")
    public List<EmploymentHistoryDTO> getEmploymentHistoryByEmployeeID(@PathVariable Long employeeId) {
        log.debug("REST request to get employment histories by empoyeeID" + employeeId);
        // check join confirm and resignation

        List<EmploymentHistory> employmentHistoryList = employmentHistoriesService.getEmploymentHistories(employeeId);
        Collections.reverse(employmentHistoryList);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);

        return employmentHistoryMapper.toDto(employmentHistoryList);
    }
}
