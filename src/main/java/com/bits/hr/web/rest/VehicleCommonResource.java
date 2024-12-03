package com.bits.hr.web.rest;

import com.bits.hr.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/common")
public class VehicleCommonResource {

    private final Logger log = LoggerFactory.getLogger(VehicleResource.class);

    private static final String ENTITY_NAME = "vehicle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleService vehicleService;

    public VehicleCommonResource(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
}
