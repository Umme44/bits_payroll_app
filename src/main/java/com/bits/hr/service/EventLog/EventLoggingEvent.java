package com.bits.hr.service.EventLog;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import org.springframework.context.ApplicationEvent;

public class EventLoggingEvent extends ApplicationEvent {

    private User user;

    private Object object;

    private RequestMethod requestMethod;

    private String resourceName;

    public EventLoggingEvent(Object source, User user, Object object, RequestMethod requestMethod, String resourceName) {
        super(source);
        this.user = user;
        this.object = object;
        this.requestMethod = requestMethod;
        this.resourceName = resourceName;
    }

    public User getUser() {
        return user;
    }

    public Object getObject() {
        return object;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getResourceName() {
        return resourceName;
    }
}
