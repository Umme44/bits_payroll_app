package com.bits.hr.service.communication.sheduledEmail;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_HOLIDAY_REMINDER;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Holidays;
import com.bits.hr.repository.HolidaysRepository;
import com.bits.hr.service.communication.email.EmailService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class HolidayEmailService {

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private HolidaysRepository holidaysRepository;

    @Autowired
    private EmailService emailService;

    @Async
    public void sendHolidayRemainder(LocalDate holidayStartDate, List<Employee> employeeList) {
        log.info("Holiday notification service called");
        List<Holidays> holidaysList = holidaysRepository.findNonMoonDependentHolidaysByStartDate(holidayStartDate);

        if (holidaysList.size() > 0) {
            for (Holidays holidays : holidaysList) {
                List<String> to = new ArrayList<>();
                List<String> cc = new ArrayList<>();
                List<String> bcc = new ArrayList<>();
                String subject = "Notification on upcoming holidays";
                String templateName = MAIL_TEMPLATE_HOLIDAY_REMINDER;

                Map<String, Object> variableMap = new HashMap<>();

                to.add("all@bracits.com");

                variableMap.put("holidayName", holidays.getDescription());
                variableMap.put("dateListAsString", getDateListAsString(holidays.getStartDate(), holidays.getEndDate()));

                emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
            }
        }
    }

    public String getDateListAsString(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        String str = "";
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            dateList.add(date);
        }

        int count = 0;
        for (LocalDate date : dateList) {
            String dateAsString = date.format(DateTimeFormatter.ofPattern("dd-MMMM-uuuu(EEEE)", Locale.ENGLISH));
            str += dateAsString;
            count++;
            if (count != dateList.size()) {
                str += ", ";
            }
        }
        return str;
    }
}
