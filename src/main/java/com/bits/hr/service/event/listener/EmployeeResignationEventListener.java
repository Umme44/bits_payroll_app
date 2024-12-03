package com.bits.hr.service.event.listener;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.EmployeeResignationDTO;
import com.bits.hr.service.event.EmployeeResignationEvent;
import com.bits.hr.service.event.EventType;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmployeeResignationEventListener {

    private static final Logger log = LoggerFactory.getLogger(EmployeeResignationEventListener.class);

    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public EmployeeResignationEventListener(EmployeeRepository employeeRepository, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    @Value("${jhipster.mail.from}")
    private String from;

    @EventListener
    public void handleEmployeeResignationEventListenerMail(EmployeeResignationEvent event) throws Exception {
        log.debug("Got EmployeeResignation event with status: " + event.getType());
        EventType eventType = event.getType();

        EmployeeResignationDTO employeeResignationDTO = event.getEmployeeResignationDTO();
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeResignationDTO.getEmployeeId());

        if (!employeeOptional.isPresent()) {
            log.error("Could not find employee from dto");
            return;
        } else {
            if (eventType.equals(EventType.APPROVED)) {}
        }
    }
}
