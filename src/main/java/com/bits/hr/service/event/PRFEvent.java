package com.bits.hr.service.event;

import com.bits.hr.domain.enumeration.RequisitionStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PRFEvent extends ApplicationEvent {

    private long procReqMasterId;
    private RequisitionStatus requisitionStatus;

    public PRFEvent(Object source, long procReqMasterId, RequisitionStatus requisitionStatus) {
        super(source);
        this.procReqMasterId = procReqMasterId;
        this.requisitionStatus = requisitionStatus;
    }
}
