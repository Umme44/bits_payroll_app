package com.bits.hr.web.rest;

import com.bits.hr.service.BillableAugmentedIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/employee-mgt")
public class BillableAugmentedIntegrationResource {

    @Autowired
    private final BillableAugmentedIntegrationService billableAugmentedIntegrationService;

    @PostMapping("/import-billable-augmented-data-xlsx")
    public ResponseEntity<Boolean> importBillableAugmentedIntegrationFromXlsx(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(billableAugmentedIntegrationService.importBillableAugmentedIntegrationXl(file));
    }
}
