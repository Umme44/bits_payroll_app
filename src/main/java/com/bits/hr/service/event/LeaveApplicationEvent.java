package com.bits.hr.service.event;

import com.bits.hr.service.dto.LeaveApplicationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LeaveApplicationEvent extends ApplicationEvent {

    LeaveApplicationDTO leaveApplication;
    EventType type;

    public LeaveApplicationEvent(Object source, LeaveApplicationDTO leaveApplication, EventType type) {
        super(source);
        this.leaveApplication = leaveApplication;
        this.type = type;
    }
}
