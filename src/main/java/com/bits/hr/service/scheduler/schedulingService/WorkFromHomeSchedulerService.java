package com.bits.hr.service.scheduler.schedulingService;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_WFH_REMINDER;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.WorkFromHomeApplicationRepository;
import com.bits.hr.service.communication.email.EmailService;
import com.bits.hr.util.DateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class WorkFromHomeSchedulerService {

    @Value("${jhipster.mail.from}")
    private String from;

    private final WorkFromHomeApplicationRepository workFromHomeApplicationRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    public WorkFromHomeSchedulerService(
        WorkFromHomeApplicationRepository workFromHomeApplicationRepository,
        EmployeeRepository employeeRepository,
        EmailService emailService
    ) {
        this.workFromHomeApplicationRepository = workFromHomeApplicationRepository;
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

    // sending reminder mail for wfh applications duration expired
    public void workFromHomeEndReminder(LocalDate date) {
        try {
            // Find Employees who WFH will end today
            List<WorkFromHomeApplication> workFromHomeApplicationReminderList = workFromHomeApplicationRepository.findApplicationsForReminder(
                date
            );
            for (WorkFromHomeApplication workFromHomeApplication : workFromHomeApplicationReminderList) {
                sendEmailWFHEndReminder(workFromHomeApplication);
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private void sendEmailWFHEndReminder(WorkFromHomeApplication workFromHomeApplication) {
        try {
            List<String> to = new ArrayList<>();
            List<String> cc = new ArrayList<>();
            List<String> bcc = new ArrayList<>();
            String subject = "Requesting to attend Physical Office";
            String templateName = MAIL_TEMPLATE_WFH_REMINDER;
            Map<String, Object> variableMap = new HashMap<>();

            Optional<Employee> employeeOptional = employeeRepository.findById(workFromHomeApplication.getEmployee().getId());
            if (!employeeOptional.isPresent()) {
                return;
            }

            Employee employee = employeeOptional.get();

            to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
            cc.add(employee.getReportingTo().getOfficialEmail().toLowerCase(Locale.ROOT));
            variableMap.put("employeeName", employee.getFullName());
            variableMap.put("endDate", DateUtil.formatDateAsDDDDMMMMYYYY(workFromHomeApplication.getEndDate()));

            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    /**
     * enable web punch for active whf application's employee;
     * disable web punch for no active whf application's employee;
     * @param today
     */
    public void refreshWFHApplicationByScheduler(LocalDate today) {
        // find approved wfh application between WFH start date and WFH end date
        List<Employee> whfActiveEmployeeList = workFromHomeApplicationRepository.findActiveWHFEmployee(today);

        for (Employee employee : whfActiveEmployeeList) {
            if (employee.getIsAllowedToGiveOnlineAttendance() == null || !employee.getIsAllowedToGiveOnlineAttendance()) {
                employee.setIsAllowedToGiveOnlineAttendance(true);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
        }

        LocalDate startDay = LocalDate.of(today.getYear(), today.getMonth(), 1);
        LocalDate endDay = LocalDate.of(today.getYear(), today.getMonth(), today.lengthOfMonth());
        List<Employee> employeeList = employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(startDay, endDay);

        employeeList.removeAll(whfActiveEmployeeList);

        for (Employee employee : employeeList) {
            if (employee.getIsAllowedToGiveOnlineAttendance() == null || employee.getIsAllowedToGiveOnlineAttendance()) {
                employee.setIsAllowedToGiveOnlineAttendance(false);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
        }
    }
}
