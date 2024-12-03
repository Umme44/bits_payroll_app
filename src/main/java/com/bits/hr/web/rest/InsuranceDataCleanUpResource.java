package com.bits.hr.web.rest;

import com.bits.hr.service.InsuranceDataCleanUpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InsuranceDataCleanUpResource {

    private final Logger log = LoggerFactory.getLogger(InsuranceConfigurationResource.class);

    private final InsuranceDataCleanUpService insuranceDataCleanUpService;

    public InsuranceDataCleanUpResource(InsuranceDataCleanUpService insuranceDataCleanUpService) {
        this.insuranceDataCleanUpService = insuranceDataCleanUpService;
    }

    @GetMapping("/api/employee-mgt/delete-insurance-data")
    public ResponseEntity<Boolean> cleanUpInsuranceData() {
        log.debug("REST request to Clear Insurance Data : {}");

        boolean hasDone = insuranceDataCleanUpService.cleanUpInsuranceData();
        return ResponseEntity.ok(hasDone);
    }
}
