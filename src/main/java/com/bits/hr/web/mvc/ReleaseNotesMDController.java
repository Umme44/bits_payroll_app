package com.bits.hr.web.mvc;

import com.bits.hr.service.md.ReleaseNoteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class ReleaseNotesMDController {

    @Autowired
    private ReleaseNoteService releaseNoteService;

    @GetMapping("/release-notes")
    public String view(Model model) {
        log.info("Release note controller called.");
        try {
            model.addAttribute("releaseNotes", releaseNoteService.getReleaseNotes());
            return "release-notes";
        } catch (Exception ex) {
            log.error(ex);
            return "release-notes";
        }
    }
}
