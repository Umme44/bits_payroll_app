package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.rrf.RRFImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employee-mgt")
public class RrfImportController {

    @Autowired
    private RRFImportService rrfImportService;

    @PostMapping("/rrf/import-rrf-xlsx")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = rrfImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }
}
