package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.ams.HolidaysImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attendance-mgt")
public class HolidaysImportXlsxController {

    @Autowired
    private HolidaysImportService holidaysImportServiceImpl; // autowired with named convention

    @PostMapping("/holidays/import-holidays-xlsx/")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = holidaysImportServiceImpl.importFile(file);
        return ResponseEntity.ok(hasDone);
    }
}
