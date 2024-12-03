package com.bits.hr.service.event;

import com.bits.hr.service.dto.PfLoanApplicationDTO;
import com.bits.hr.service.dto.PfLoanRepaymentDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PFLoanApplyEvent extends ApplicationEvent {

    PfLoanApplicationDTO pfLoanApplicationDTO;
    EventType type;

    public PFLoanApplyEvent(Object source, PfLoanApplicationDTO pfLoanApplicationDTO, EventType type) {
        super(source);
        this.pfLoanApplicationDTO = pfLoanApplicationDTO;
        this.type = type;
    }
}
