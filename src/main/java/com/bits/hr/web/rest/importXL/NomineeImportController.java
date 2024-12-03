package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.nominee.NomineeImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employee-mgt")
public class NomineeImportController {

    @Autowired
    private NomineeImportService nomineeImportService;

    @PostMapping("/import-nominee-entry/")
    public ResponseEntity<Boolean> importNominees(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = nomineeImportService.importFile(file);
        return ResponseEntity.ok(hasDone);
    }

    @PostMapping("/import-nominee-image")
    public ResponseEntity<Boolean> importNomineesImage(@RequestParam("file") MultipartFile[] files) throws Exception {
        boolean hasDone = nomineeImportService.importNomineeImage(files);
        return ResponseEntity.ok(hasDone);
    }
}
