package com.bits.hr.service.scheduler.schedulingService;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_SEPARATION_REMINDER;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.service.communication.email.EmailAddressConfig;
import com.bits.hr.service.communication.email.EmailService;
import java.time.LocalDate;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SeparationReminderMailSchedulerService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    private EmailService emailService;

    @Value("${jhipster.mail.from}")
    private String from;

    public void sendMailForReplacementOfResignedEmployee(LocalDate localDate) {
        try {
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.findAllBeforeLWDEnd(localDate.plusDays(1L));

            List<EmployeeResignation> finalEmployeeResignationList = new ArrayList<>();

            for (EmployeeResignation employeeResignation : employeeResignationList) {
                List<Employee> employeeList = employeeRepository.getMyTeamByReportingToId(employeeResignation.getEmployee().getId());
                if (employeeList.size() > 0) {
                    finalEmployeeResignationList.add(employeeResignation);
                }
            }

            if (finalEmployeeResignationList.size() > 0) {
                for (EmployeeResignation employeeResignation : finalEmployeeResignationList) {
                    sendMail(employeeResignation);
                }
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    public void sendMail(EmployeeResignation employeeResignation) throws Exception {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Replacement Of Resigned Employee";
        String templateName = MAIL_TEMPLATE_SEPARATION_REMINDER;

        Map<String, Object> variableMap = new HashMap<>();
        Optional<Employee> employee = employeeRepository.findById(employeeResignation.getEmployee().getId());

        if (!employee.isPresent()) {
            log.error("No Employee Found");
            throw new Exception("No Employee Found");
        }

        to.add(EmailAddressConfig.TEAM_HR_EMAIL.toLowerCase(Locale.ROOT));

        variableMap.put("employeeName", employee.get().getFullName());
        variableMap.put("pin", employee.get().getPin());
        variableMap.put("designation", employee.get().getDesignation().getDesignationName());
        variableMap.put("lastWorkingDate", employeeResignation.getLastWorkingDay());
        if (employeeResignation.getEmployee().getGender() == Gender.FEMALE) {
            variableMap.put("person", "her");
        } else {
            variableMap.put("person", "his");
        }

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
