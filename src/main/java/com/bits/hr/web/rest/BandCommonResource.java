package com.bits.hr.web.rest;

import com.bits.hr.service.BandService;
import com.bits.hr.service.selecteable.SelectableDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common")
public class BandCommonResource {

    @Autowired
    private BandService bandService;

    @GetMapping("/bands")
    public List<SelectableDTO> getBands() {
        return bandService.findAllForCommon();
    }
}
