package com.bits.hr.web.rest;

import com.bits.hr.service.DesignationService;
import com.bits.hr.service.dto.DesignationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common/designations")
public class DesignationCommonResource {

    private final Logger log = LoggerFactory.getLogger(DesignationCommonResource.class);

    @Autowired
    private DesignationService designationService;

    @GetMapping("")
    public List<DesignationDTO> getAllDesignations() {
        log.debug("REST request to get all Designations");
        return designationService.findAll();
    }
}
