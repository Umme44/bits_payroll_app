package com.bits.hr.service.communication.email;

import java.util.List;
import java.util.Map;

public interface EmailService {
    Boolean sendEmailFromTemplates(
        String from,
        List<String> to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String templateName,
        Map<String, Object> variableMap
    );
}
