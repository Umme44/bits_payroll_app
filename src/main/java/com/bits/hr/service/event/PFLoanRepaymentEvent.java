package com.bits.hr.service.event;

import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.dto.PfLoanRepaymentDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PFLoanRepaymentEvent extends ApplicationEvent {

    PfLoanRepaymentDTO pfLoanRepaymentDTO;
    EventType type;

    public PFLoanRepaymentEvent(Object source, PfLoanRepaymentDTO pfLoanRepaymentDTO, EventType type) {
        super(source);
        this.pfLoanRepaymentDTO = pfLoanRepaymentDTO;
        this.type = type;
    }
}
