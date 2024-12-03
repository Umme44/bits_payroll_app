package com.bits.hr.service.communication.sheduledEmail;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_APPROVAL_REMINDER;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.domain.MovementEntry;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.repository.MovementEntryRepository;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.approvalProcess.PendingApplicationService;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import java.time.LocalDate;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@EnableAsync
public class PendingApprovalRemainderService {

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private ManualAttendanceEntryRepository manualAttendanceEntryRepository;

    @Autowired
    private MovementEntryRepository movementEntryRepository;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private PendingApplicationService pendingApplicationService;

    @Async
    public void remainder(List<Employee> employeeList) {
        log.info("Pending Attendance, Leave Application, Movement remainder service called ");

        pendingAttendanceRemainder(employeeList);
        pendingLeaveApplicationRemainder(employeeList);
        pendingMovementApplicationRemainder(employeeList);
    }

    @Async
    public void pendingAttendanceRemainder(List<Employee> employeeList) {
        for (Employee employee : employeeList) {
            pendingAttendanceRemainder(employee);
        }
    }

    @Async
    public void pendingLeaveApplicationRemainder(List<Employee> employeeList) {
        for (Employee employee : employeeList) {
            pendingLeaveApplicationRemainder(employee);
        }
    }

    @Async
    public void pendingMovementApplicationRemainder(List<Employee> employeeList) {
        for (Employee employee : employeeList) {
            pendingMovementApplicationRemainder(employee);
        }
    }

    @Async
    public void pendingAttendanceRemainder(Employee employee) {
        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.getAllPendingAttendancesLM(
            employee.getId()
        );
        if (manualAttendanceEntryList.size() > 0) {
            List<String> to = new ArrayList<>();
            List<String> cc = new ArrayList<>();
            List<String> bcc = new ArrayList<>();
            String subject = "Notification on Pending Attendance Approvals";
            String templateName = MAIL_TEMPLATE_APPROVAL_REMINDER;

            Map<String, Object> variableMap = new HashMap<>();

            to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

            variableMap.put("name", employee.getFullName());
            variableMap.put("numberOfPendingApplication", manualAttendanceEntryList.size());
            variableMap.put("applicationType", "Attendance");

            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
        }
    }

    @Async
    public void pendingLeaveApplicationRemainder(Employee employee) {
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getAllPendingLeaveApplicationsLM(employee.getId());

        // my subordinate list
        List<LeaveApplicationDTO> leaveApplicationMySubordinateList = pendingApplicationService.getAllPendingLeaveApplicationsLM(
            employee.getId()
        );

        List<LeaveApplicationDTO> finalPendingList = new ArrayList<>();
        finalPendingList.addAll(leaveApplicationMySubordinateList);

        if (finalPendingList.size() > 0) {
            List<String> to = new ArrayList<>();
            List<String> cc = new ArrayList<>();
            List<String> bcc = new ArrayList<>();
            String subject = "Notification on Pending Leave Approvals";
            String templateName = MAIL_TEMPLATE_APPROVAL_REMINDER;

            Map<String, Object> variableMap = new HashMap<>();

            to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

            variableMap.put("name", employee.getFullName());
            variableMap.put("numberOfPendingApplication", finalPendingList.size());
            variableMap.put("applicationType", "Leave");

            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
        }
    }

    @Async
    public void pendingMovementApplicationRemainder(Employee employee) {
        List<MovementEntry> movementEntryList = movementEntryRepository.getAllPendingLM(employee.getId());
        if (movementEntryList.size() > 0) {
            List<String> to = new ArrayList<>();
            List<String> cc = new ArrayList<>();
            List<String> bcc = new ArrayList<>();
            String subject = "Notification on Pending Movement Approvals";
            String templateName = MAIL_TEMPLATE_APPROVAL_REMINDER;

            Map<String, Object> variableMap = new HashMap<>();

            to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

            variableMap.put("name", employee.getFullName());
            variableMap.put("numberOfPendingApplication", movementEntryList.size());
            variableMap.put("applicationType", "Movement");

            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
        }
    }
}
