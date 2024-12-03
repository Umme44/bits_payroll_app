package com.bits.hr.service.md;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ReleaseNoteService {

    @Autowired
    private HtmlService htmlService;

    public String getReleaseNotes() {
        try {
            InputStream inputStream = new ClassPathResource("static/release-notes/release-notes.md").getInputStream();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = inputStream.read(buffer)) != -1;) {
                result.write(buffer, 0, length);
            }
            String markdown = result.toString("UTF-8");
            log.info("Release Notes from markdown file:");
            log.info(markdown);
            return htmlService.markdownToHtml(markdown);
        } catch (Exception ex) {
            log.error(ex);
            return "Exception Occurred";
        }
    }
}
