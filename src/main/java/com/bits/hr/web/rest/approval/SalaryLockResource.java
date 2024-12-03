package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.service.approvalProcess.SalaryLockService;
import com.bits.hr.service.dto.ApprovalDTO;
import java.net.URISyntaxException;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/payroll-mgt/salary-lock")
public class SalaryLockResource {

    @Autowired
    private SalaryLockService salaryLockService;

    @GetMapping("/salaries")
    public List<SalaryGeneratorMaster> getListOfGeneratedSalaries() throws URISyntaxException {
        return salaryLockService.findAll();
    }

    @PutMapping("lock-selected")
    public boolean approveSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to lock selected salaries.");
        return salaryLockService.lockSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("unlock-selected")
    public boolean rejectSelected(@RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to unlock selected salaries.");
        return salaryLockService.unlockSelected(approvalDTO.getListOfIds());
    }

    @PutMapping("lock-all")
    public boolean approveAll() throws URISyntaxException {
        log.debug("REST request to lock all salaries.");
        return salaryLockService.lockAll();
    }

    @PutMapping("unlock-all")
    public boolean rejectAll() throws URISyntaxException {
        log.debug("REST request to unlock all salaries.");
        return salaryLockService.unlockAll();
    }

    @GetMapping("is-salary-locked/{month}/{year}")
    public boolean isSalaryLocked(@PathVariable String month, @PathVariable String year) {
        log.debug("REST request to check salary lock status for this month");
        return salaryLockService.isLocked(year, month);
    }
}
