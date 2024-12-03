package com.bits.hr.web.rest.approval;

import com.bits.hr.service.approvalProcess.PfLoanApplicationHrServiceImpl;
import com.bits.hr.service.dto.EmployeeBankDetailsDTO;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/pf-mgt/pf-loan-applications")
public class PfLoanApplicationApprovalHR {

    @Autowired
    PfLoanApplicationHrServiceImpl pfLoanApplicationHrService;

    @GetMapping("hr")
    public List<PfLoanApplicationDTO> getAllPending() {
        log.debug("REST request to get all pending pf application");
        return pfLoanApplicationHrService.pendingPfLoanApplication();
    }

    @GetMapping("bank-details/employee/{pin}")
    public EmployeeBankDetailsDTO getBankDetailsByEmployeePin(@PathVariable String pin) {
        log.debug("REST request to get bank detail by employee pin");
        return pfLoanApplicationHrService.getBankDetailsByEmployeePin(pin);
    }
}
