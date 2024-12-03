package com.bits.hr.service.EventLog;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.repository.EventLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EventLoggingListener implements ApplicationListener<EventLoggingEvent> {

    @Autowired
    EventLogRepository eventLogRepository;

    @Override
    public void onApplicationEvent(EventLoggingEvent event) {
        logEvent(event.getUser(), event.getObject(), event.getRequestMethod(), event.getResourceName());
    }

    public void logEvent(User user, Object object, RequestMethod requestMethod, String resourceName) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.UK)
                .withZone(ZoneId.systemDefault());

            Instant currentTime = Instant.now();

            String currentTimeStr = dateTimeFormatter.format(currentTime);

            com.bits.hr.domain.EventLog eventLog = new com.bits.hr.domain.EventLog();

            String className = resourceName; //object.getClass().getSimpleName();
            eventLog.setEntityName(className);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonData = "";
            if (requestMethod != RequestMethod.GET) {
                jsonData = ow.writeValueAsString(object);
            }

            eventLog.setData(jsonData);
            eventLog.setPerformedAt(currentTime);
            eventLog.setPerformedBy(user);

            eventLog.setRequestMethod(requestMethod);

            eventLog.setTitle(
                requestMethod.toString() +
                " operation performed by " +
                user.getFirstName() +
                "  " +
                user.getLastName() +
                " in " +
                className +
                "  " +
                " at" +
                currentTimeStr
            );

            eventLogRepository.save(eventLog);
        } catch (Exception ex) {
            log.error("Failed To save Event");
        }
    }
}
