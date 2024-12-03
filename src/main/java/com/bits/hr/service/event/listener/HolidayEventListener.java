package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_HOLIDAY_REMINDER;

import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.HolidaysDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.HolidayEvent;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HolidayEventListener {

    private static final Logger log = LoggerFactory.getLogger(HolidayEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmailService emailService;

    public HolidayEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleHolidayEvent(HolidayEvent event) {
        log.debug("Got holiday remainder event with status: " + event.getType());
        HolidaysDTO holidaysDTO = event.getHolidaysDTO();
        EventType eventType = event.getType();

        if (eventType.equals(EventType.CREATED)) {
            sendMailForHolidayReminder(holidaysDTO);
        }
    }

    private void sendMailForHolidayReminder(HolidaysDTO holidaysDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Holiday Notice on the Occasion of " + holidaysDTO.getDescription();
        String templateName = MAIL_TEMPLATE_HOLIDAY_REMINDER;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(EmailAddressConfig.TEAM_BRAC_IT_EMAIL);

        List<String> dateListAsString = getDateListAsString(holidaysDTO.getStartDate(), holidaysDTO.getEndDate());

        variableMap.put("holidayName", holidaysDTO.getDescription());
        variableMap.put("dateListAsString", dateListAsString);
        log.info(String.valueOf(dateListAsString));

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }

    private List<String> getDateListAsString(LocalDate startDate, LocalDate endDate) {
        List<String> dateList = new ArrayList<>();
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            if (endDate.isEqual(startDate)) {
                dateList.add(startDate + "");
            } else {
                dateList.add(startDate + ", ");
            }
            startDate = startDate.plusDays(1);
        }
        return dateList;
    }
}
