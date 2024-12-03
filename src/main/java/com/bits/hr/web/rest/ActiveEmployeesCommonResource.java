package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.activeEmployees.ActiveEmployeesCommonService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeMinCustomDto;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/common")
public class ActiveEmployeesCommonResource {

    @Autowired
    private ActiveEmployeesCommonService activeEmployeesCommonService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @GetMapping("/active-employees")
    public ResponseEntity<List<EmployeeMinCustomDto>> getAllActiveEmployees() {
        log.debug("Rest request to get all active employees");
        List<EmployeeMinCustomDto> dtoList = activeEmployeesCommonService.getAllActiveEmployees();

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, dtoList, RequestMethod.GET, "ActiveEmployeesCommonResource");

        return ResponseEntity.ok().body(dtoList);
    }
}
