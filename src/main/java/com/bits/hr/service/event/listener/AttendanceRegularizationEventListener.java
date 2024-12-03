package com.bits.hr.service.event.listener;

import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.HolidaysDTO;
import com.bits.hr.service.event.AttendanceRegularizationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AttendanceRegularizationEventListener {

    private static final Logger log = LoggerFactory.getLogger(AttendanceRegularizationEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmailService emailService;

    public AttendanceRegularizationEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleAttendanceRegularizationEventEvent(AttendanceRegularizationEvent event) {
        log.debug("Got attendance regularization event with status: " + event.getType());
        /*HolidaysDTO holidaysDTO = event.getHolidaysDTO();
        EventType eventType = event.getType();

        if (eventType.equals(EventType.CREATED)) {
            sendMailForHolidayReminder(holidaysDTO);
        }*/
    }

    private void sendMailForAttendanceRegularization(HolidaysDTO holidaysDTO) {
        /*List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "";
        String templateName = MAIL_TEMPLATE_HOLIDAY_REMAINDER;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(EmailAddressConfig.TEAM_BRAC_IT_EMAIL);

        List<String> dateListAsString = getDateListAsString(holidaysDTO.getStartDate(), holidaysDTO.getEndDate());

        variableMap.put("holidayName", holidaysDTO.getDescription());
        variableMap.put("dateListAsString", dateListAsString);

        emailService.sendEmailFromTemplates(
            from,
            to,
            cc,
            bcc,
            subject,
            templateName,
            variableMap
        );*/

    }
}
