package com.bits.hr.web.rest;

import com.bits.hr.service.UnitService;
import com.bits.hr.service.dto.UnitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common/units")
public class UnitCommonResource {

    private final Logger log = LoggerFactory.getLogger(UnitResource.class);

    @Autowired
    private UnitService unitService;
    @GetMapping("")
    public List<UnitDTO> getAllUnits() {
        log.debug("REST request to get all Units");
        return unitService.findAll();
    }
}
