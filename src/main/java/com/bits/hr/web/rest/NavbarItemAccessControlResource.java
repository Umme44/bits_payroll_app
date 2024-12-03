package com.bits.hr.web.rest;

import com.bits.hr.domain.Employee;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.NavBarItemAccessControlService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.DashboardItemAccessControlDTO;
import com.bits.hr.service.dto.NavBarItemAccessControlDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
public class NavbarItemAccessControlResource {

    private final CurrentEmployeeService currentEmployeeService;

    private final NavBarItemAccessControlService navBarItemAccessControlService;

    private final Logger log = LoggerFactory.getLogger(DashboardItemAccessControlResource.class);

    public NavbarItemAccessControlResource(
        CurrentEmployeeService currentEmployeeService,
        NavBarItemAccessControlService navBarItemAccessControlService
    ) {
        this.currentEmployeeService = currentEmployeeService;
        this.navBarItemAccessControlService = navBarItemAccessControlService;
    }

    @GetMapping("/nav-bar-item-access-control")
    public ResponseEntity<NavBarItemAccessControlDTO> getAllNavBarItemAccessControls() {
        log.debug("REST request to get all navBarItemAccessControl");
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (employeeOptional.isPresent()) {
            NavBarItemAccessControlDTO result = navBarItemAccessControlService.getAllAccessControl(employeeOptional.get().getId());
            return ResponseEntity.ok(result);
        } else {
            throw new NoEmployeeProfileException();
        }
    }
}
