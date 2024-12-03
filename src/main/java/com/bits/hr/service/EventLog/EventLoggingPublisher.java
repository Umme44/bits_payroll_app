package com.bits.hr.service.EventLog;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventLoggingPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final User user, final Object object, RequestMethod requestMethod, String resourceName) {
        System.out.println("Publishing custom event. ");
        EventLoggingEvent event = new EventLoggingEvent(this, user, object, requestMethod, resourceName);
        applicationEventPublisher.publishEvent(event);
    }
}
