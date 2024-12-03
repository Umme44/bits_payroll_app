package com.bits.hr.web.rest;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import com.bits.hr.service.importXL.festivalBonus.FestivalBonusImportService;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/payroll-mgt")
public class FestivalBonusImportResource {

    private final Logger log = LoggerFactory.getLogger(FestivalBonusDetailsResource.class);

    @Autowired
    private FestivalBonusImportService festivalBonusImportService;

    @PostMapping("/import-festival-bonus")
    public ResponseEntity<Boolean> importFestivalBonus(@RequestParam("file") MultipartFile file) throws Exception {
        log.debug("REST request to import FestivalBonus : {}");
        boolean result = festivalBonusImportService.importFile(file);
        return ResponseEntity.ok(result);
    }
}
