package com.bits.hr.service.communication.email;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public Boolean sendEmailFromTemplates(
        String from,
        List<String> to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String templateName,
        Map<String, Object> variableMap
    ) {
        try {
            Context context = new Context();
            for (String key : variableMap.keySet()) {
                context.setVariable(key, variableMap.get(key));
            }
            String content = templateEngine.process(templateName, context);
            sendEmail(from, to, cc, bcc, subject, content, false, true);
            return true;
        } catch (Exception ex) {
            log.error("Failed to send mail");
            log.error(ex);
            return false;
        }
    }

    @Async
    void sendEmail(
        String from,
        List<String> to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String content,
        boolean isMultipart,
        boolean isHtml
    ) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setFrom(from);
            if (to == null || to.isEmpty()) {
                throw new RuntimeException("Email To Recipient(s) is empty");
            }
            message.setTo(to.toArray(new String[0]));
            message.setCc(cc.toArray(new String[0]));
            message.setBcc(bcc.toArray(new String[0]));
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.info("Mail Subject: {}, Recipients: To={}, CC={}", subject, to, cc);
            log.info("Mail sent successfully");
        } catch (MailException | MessagingException ex) {
            log.error(ex);
        }
    }
}
