package com.bits.hr.service.event;

import com.bits.hr.domain.WorkFromHomeApplication;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WorkFromHomeApplicationEvent extends ApplicationEvent {

    WorkFromHomeApplication workFromHomeApplication;
    EventType type;

    ApprovalVia approvalVia;

    public WorkFromHomeApplicationEvent(Object source, WorkFromHomeApplication workFromHomeApplication, EventType type) {
        super(source);
        this.workFromHomeApplication = workFromHomeApplication;
        this.type = type;
    }

    public WorkFromHomeApplicationEvent(
        Object source,
        WorkFromHomeApplication workFromHomeApplication,
        EventType type,
        ApprovalVia approvalVia
    ) {
        super(source);
        this.workFromHomeApplication = workFromHomeApplication;
        this.approvalVia = approvalVia;
        this.type = type;
    }
}
