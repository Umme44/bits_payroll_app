package com.bits.hr.service.event;

import com.bits.hr.service.dto.EmployeeResignationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmployeeResignationEvent extends ApplicationEvent {

    EmployeeResignationDTO employeeResignationDTO;
    EventType type;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public EmployeeResignationEvent(Object source, EmployeeResignationDTO employeeResignationDTO, EventType type) {
        super(source);
        this.employeeResignationDTO = employeeResignationDTO;
        this.type = type;
    }
}
