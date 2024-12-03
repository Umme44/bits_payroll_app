package com.bits.hr.service.communication.sheduledEmail;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_TAX_ACKNOWLEDGEMENT_RECEIPT;

import com.bits.hr.domain.AitConfig;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.repository.AitConfigRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.TaxAcknowledgementReceiptRepository;
import com.bits.hr.service.communication.email.DomainConfig;
import com.bits.hr.service.communication.email.EmailService;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TaxAcknowledgementReminderService {

    private final EmailService emailService;

    @Autowired
    Environment env;

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private TaxAcknowledgementReceiptRepository taxAcknowledgementReceiptRepository;

    @Autowired
    private AitConfigRepository aitConfigRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public TaxAcknowledgementReminderService(EmailService emailService) {
        this.emailService = emailService;
    }

    private LocalDate getReminderMailStartDate(int year) {
        return LocalDate.of(year + 1, Month.NOVEMBER, 30);
    }

    private LocalDate getReminderMailEndDate(int year) {
        return LocalDate.of(year + 1, Month.MARCH, 31);
    }

    public void reminderWhoNotSubmitted(LocalDate currentDate) {
        try {
            Optional<AitConfig> optionalAitConfig = getCurrentAitConfig(currentDate);
            if (!optionalAitConfig.isPresent()) {
                log.error("Failed to send email, no AitConfig found in , {}", currentDate);
                return;
            }

            AitConfig aitConfig = optionalAitConfig.get();
            LocalDate mailStartDate = getReminderMailStartDate(aitConfig.getStartDate().getYear());
            LocalDate mailEndDate = getReminderMailEndDate(aitConfig.getStartDate().getYear());

            boolean dateIsValid =
                (currentDate.isEqual(mailStartDate) || currentDate.isAfter(mailStartDate)) &&
                (currentDate.isEqual(mailEndDate) || currentDate.isBefore(mailEndDate));

            // who joined before fiscal year end, => yes
            // lwd before fiscal year end, if => yes

            // before fiscal year => no
            // contract end in before fiscal year start => no

            if (dateIsValid) {
                List<Employee> activeEmployeeList = employeeRepository.getEligibleEmployeeForTaxAcknowledgementReceipt(
                    aitConfig.getStartDate(),
                    aitConfig.getEndDate(),
                    currentDate
                );

                List<Employee> employeeListSubmitted = taxAcknowledgementReceiptRepository.findEmployeeListByAitConfigId(aitConfig.getId());
                activeEmployeeList.removeAll(employeeListSubmitted);
                for (Employee employee : activeEmployeeList) {
                    if (employee.getOfficialEmail() != null) {
                        sendEmail(employee);
                    }
                }
            } else {
                log.info("Date is not in range to send email");
            }
        } catch (Exception e) {
            log.error("Failed to send tax acknowledgement reminder mail");
        }
    }

    public void reminderWhoNotSubmittedForSchedulerTest(String pin, LocalDate currentDate) {
        Optional<AitConfig> optionalAitConfig = getCurrentAitConfig(currentDate);
        if (!optionalAitConfig.isPresent()) {
            log.error("Failed to send email, no AitConfig found in , {}", currentDate);
        }

        AitConfig aitConfig = optionalAitConfig.get();
        LocalDate mailStartDate = getReminderMailStartDate(aitConfig.getStartDate().getYear());
        LocalDate mailEndDate = getReminderMailEndDate(aitConfig.getStartDate().getYear());

        boolean dateIsValid =
            (currentDate.isEqual(mailStartDate) || currentDate.isAfter(mailStartDate)) &&
            (currentDate.isEqual(currentDate) || currentDate.isBefore(mailEndDate));

        // who joined before fiscal year end, => yes
        // lwd before fiscal year end, if => yes

        // before fiscal year => no
        // contract end in before fiscal year start => no

        if (dateIsValid) {
            Employee employeeTest = employeeRepository.findByPin(pin).get();

            List<Employee> activeEmployeeList = employeeRepository.getEligibleEmployeeForTaxAcknowledgementReceipt(
                aitConfig.getStartDate(),
                aitConfig.getEndDate(),
                currentDate
            );

            for (Employee employee : activeEmployeeList) {
                if (Objects.equals(employee.getId(), employeeTest.getId())) {
                    activeEmployeeList.clear();
                    activeEmployeeList.add(employeeTest);
                    break;
                }
            }
            if (activeEmployeeList.size() > 2) {
                return;
            }

            //List<Employee> activeEmployeeList = employeeRepository.getEligibleEmployeeForTaxAcknowledgementReceipt(aitConfig.getStartDate(), aitConfig.getEndDate());
            List<Employee> employeeListSubmitted = taxAcknowledgementReceiptRepository.findEmployeeListByAitConfigId(aitConfig.getId());
            activeEmployeeList.removeAll(employeeListSubmitted);
            for (Employee employee : activeEmployeeList) {
                if (employee.getOfficialEmail() != null) {
                    sendEmail(employee);
                }
            }
        } else {
            log.info("Date is not in range to send email");
        }
    }

    private Optional<AitConfig> getCurrentAitConfig(LocalDate currentDate) {
        currentDate = currentDate.minusYears(1);
        List<AitConfig> aitConfigList = aitConfigRepository.findAllBetweenOneDate(currentDate);
        if (aitConfigList.size() > 0) return Optional.of(aitConfigList.get(0)); else return Optional.empty();
    }

    @Async
    public void sendEmail(Employee employee) {
        List<String> to = new ArrayList<>();
        List<String> cc = new ArrayList<>();
        List<String> bcc = new ArrayList<>();
        String subject = "Requesting to Upload Tax Acknowledgement Receipt";
        String templateName = MAIL_TEMPLATE_TAX_ACKNOWLEDGEMENT_RECEIPT;

        Map<String, Object> variableMap = new HashMap<>();

        to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));
        cc.add("rownaqul.hossain@bracits.com");

        if (employee.getGender() == Gender.MALE) {
            variableMap.put("employee", "Mr. " + employee.getFullName());
        } else if (employee.getGender() == Gender.FEMALE) {
            variableMap.put("employee", "Ms. " + employee.getFullName());
        } else {
            variableMap.put("employee", employee.getFullName());
        }

        //         Arrays.stream(env.getActiveProfiles()).anyMatch(s -> {
        //            return s.equals("dev");
        //        });

        boolean isDev = Arrays.asList(env.getActiveProfiles()).contains("dev");
        if (isDev) {
            variableMap.put("website", "http://localhost:8080" + "/user-tax-acknowledgement-receipt");
        } else variableMap.put("website", DomainConfig.BASE_URL + "/user-tax-acknowledgement-receipt");

        emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
    }
}
