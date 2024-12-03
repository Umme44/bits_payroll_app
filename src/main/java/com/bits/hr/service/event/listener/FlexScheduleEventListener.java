package com.bits.hr.service.event.listener;

import static com.bits.hr.service.communication.email.MailTemplate.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.TimeSlot;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.FlexScheduleApprovalDTO;
import com.bits.hr.service.dto.MovementEntryDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.FlexScheduleApprovalEvent;
import com.bits.hr.util.TimeUtil;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FlexScheduleEventListener {

    private static final Logger log = LoggerFactory.getLogger(FlexScheduleEventListener.class);

    @Value("${jhipster.mail.from}")
    private String from;

    private final EmployeeRepository employeeRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final EmailService emailService;

    public FlexScheduleEventListener(
        EmployeeRepository employeeRepository,
        TimeSlotRepository timeSlotRepository,
        EmailService emailService
    ) {
        this.employeeRepository = employeeRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void handleFlexScheduleEvent(FlexScheduleApprovalEvent event) {
        log.debug("Got flex Schedule approved event with status: " + event.getType());
        FlexScheduleApprovalDTO flexScheduleApprovalDTO = event.getFlexScheduleApprovalDTO();
        EventType eventType = event.getType();

        Employee employee = event.getEmployee();

        if (eventType.equals(EventType.APPROVED)) {
            sendMailForFlexSchedule(employee, flexScheduleApprovalDTO);
        }
    }

    private void sendMailForFlexSchedule(Employee employee, FlexScheduleApprovalDTO flexScheduleApprovalDTO) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Update on flex schedule";
        String templateName = MAIL_TEMPLATE_FLEX_SCHEDULE;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        Optional<TimeSlot> optionalTimeSlot = timeSlotRepository.findById(flexScheduleApprovalDTO.getTimeSlotId());
        if (!optionalTimeSlot.isPresent()) {
            log.error("Time slot not found for ID = " + flexScheduleApprovalDTO.getTimeSlotId());
            log.error("Failed to send mail for FlexSchedule Change");
        }

        variableMap.put("name", employee.getFullName());

        String officeStartTime = TimeUtil.getHourAndMinute(optionalTimeSlot.get().getInTime());
        String officeEndTime = TimeUtil.getHourAndMinute(optionalTimeSlot.get().getOutTime());

        variableMap.put("officeStartTime", officeStartTime);
        variableMap.put("officeEndTime", officeEndTime);

        try {
            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);

            log.info("Mail Sent Successfully of FlexSchedule Change for " + employee.getFullName());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Failed to sent mail of FlexSchedule Change for " + employee.getFullName());
        }
    }
}
