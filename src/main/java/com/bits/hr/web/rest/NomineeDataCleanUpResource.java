package com.bits.hr.web.rest;

import com.bits.hr.service.NomineeDataCleanUpService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class NomineeDataCleanUpResource {

    @Autowired
    private NomineeDataCleanUpService nomineeDataCleanUpService;

    @GetMapping("/api/employee-mgt/delete-pf-nominee-data")
    public ResponseEntity<Boolean> cleanUpPfNomineeData() {
        log.debug("REST request to Clear Pf Nominee Data : {}");

        boolean hasDone = nomineeDataCleanUpService.cleanUpPfNomineeData();
        return ResponseEntity.ok(hasDone);
    }

    @GetMapping("/api/employee-mgt/delete-gf-nominee-data")
    public ResponseEntity<Boolean> cleanUpGfNomineeData() {
        log.debug("REST request to Clear GF Nominee Data : {}");

        boolean hasDone = nomineeDataCleanUpService.cleanUpGfNomineeData();
        return ResponseEntity.ok(hasDone);
    }

    @GetMapping("/api/employee-mgt/delete-general-nominee-data")
    public ResponseEntity<Boolean> cleanUpGeneralNomineeData() {
        log.debug("REST request to Clear General Nominee Data : {}");

        boolean hasDone = nomineeDataCleanUpService.cleanUpGeneralNomineeData();
        return ResponseEntity.ok(hasDone);
    }
}
