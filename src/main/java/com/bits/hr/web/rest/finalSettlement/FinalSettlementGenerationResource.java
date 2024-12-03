package com.bits.hr.web.rest.finalSettlement;

import com.bits.hr.service.dto.FinalSettlementDTO;
import com.bits.hr.service.finalSettlement.FinalSettlementGenService;
import com.bits.hr.service.finalSettlement.PfGfSettlementService;
import com.bits.hr.service.finalSettlement.dto.PfGfStatement;
import com.bits.hr.service.finalSettlement.dto.PfStatement;
import com.bits.hr.service.finalSettlement.helperMethods.PfStatementGenerationService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.FinalSettlement}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class FinalSettlementGenerationResource {

    @Autowired
    private FinalSettlementGenService finalSettlementService;

    @Autowired
    private PfStatementGenerationService pfStatementGenerationService;

    @Autowired
    private PfGfSettlementService pfGfSettlementService;

    @GetMapping("/generate-final-settlement/{employeeId}")
    public ResponseEntity<FinalSettlementDTO> generateAndSaveFinalSettlement(@PathVariable Long employeeId) {
        Optional<FinalSettlementDTO> finalSettlementDTO = finalSettlementService.generateAndSave(employeeId);
        return ResponseUtil.wrapOrNotFound(finalSettlementDTO);
    }

    @GetMapping("/pf-statement/{employeeId}")
    public ResponseEntity<PfStatement> getPfStatement(@PathVariable Long employeeId) {
        Optional<PfStatement> pfStatement = pfStatementGenerationService.generate(employeeId);
        return ResponseUtil.wrapOrNotFound(pfStatement);
    }

    @GetMapping("/pf-gf-statement/{employeeId}")
    public ResponseEntity<PfGfStatement> getPfGfStatement(@PathVariable Long employeeId) {
        Optional<PfGfStatement> pfGfStatement = pfGfSettlementService.getPfGfStatement(employeeId);
        return ResponseUtil.wrapOrNotFound(pfGfStatement);
    }
}
