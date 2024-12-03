package com.bits.hr.web.rest.scheduler;

import com.bits.hr.service.scheduler.schedulingService.AutoPunchInAndOutService;
import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class AttendanceSchedulerServiceTestResource {

    @Autowired
    private AutoPunchInAndOutService autoPunchInAndOutService;

    @GetMapping("/api/attendance-mgt/auto-punch-in-and-out-for-eligible-employee")
    public boolean autoPunchInAndOutForEligibleEmployeeApi(@RequestParam(required = false) LocalDate date) {
        try {
            if (date == null) date = LocalDate.now();
            autoPunchInAndOutService.autoPunchInAndOutForEligibleEmployee(date.minusDays(1));
            return true;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return false;
        }
    }
}
