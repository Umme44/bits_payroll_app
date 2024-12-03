package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.DashboardItemAccessControlService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.DashboardItemAccessControlDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
public class DashboardItemAccessControlResource {

    private final CurrentEmployeeService currentEmployeeService;

    private final DashboardItemAccessControlService dashboardItemAccessControlService;

    private final Logger log = LoggerFactory.getLogger(DashboardItemAccessControlResource.class);

    public DashboardItemAccessControlResource(
        CurrentEmployeeService currentEmployeeService,
        DashboardItemAccessControlService dashboardItemAccessControlService
    ) {
        this.currentEmployeeService = currentEmployeeService;
        this.dashboardItemAccessControlService = dashboardItemAccessControlService;
    }

    @GetMapping("/dashboard-item-access-control")
    public ResponseEntity<DashboardItemAccessControlDTO> getAllDashboardItemAccessControls() {
        log.debug("REST request to get all dashboardItemAccessControl");
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (employeeOptional.isPresent()) {
            DashboardItemAccessControlDTO result = dashboardItemAccessControlService.getAllAccessControl(employeeOptional.get().getId());
            return ResponseEntity.ok(result);
        } else {
            throw new NoEmployeeProfileException();
        }
    }
}
