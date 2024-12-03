package com.bits.hr.service.communication.sheduledEmail;

import static com.bits.hr.service.communication.email.MailTemplate.MAIL_TEMPLATE_ABSENT_DAYS_REMINDER;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.communication.email.EmailService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@EnableAsync
public class AbsentDaysRemainderService {

    @Value("${jhipster.mail.from}")
    private String from;

    @Autowired
    private EmailService emailService;

    @Autowired
    AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    @Async
    public void absentDaysRemainder(List<Employee> activeEmployeeList, LocalDate startDate, LocalDate endDate) {
        try {
            log.info("Absent days remainder called");

            for (Employee employee : activeEmployeeList) {
                absentDaysRemainder(employee, startDate, endDate);
            }
        } catch (Exception ex) {
            log.info("Absent days remainder failed");
            log.error(ex);
        }
    }

    @Async
    public void absentDaysRemainder(Employee employee, LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(employee.getDateOfJoining()) || startDate.isEqual(employee.getDateOfJoining())) {
            startDate = employee.getDateOfJoining();
        }

        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employee.getId(),
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_USER
        );

        List<AttendanceTimeSheetDTO> absentDays = attendanceTimeSheetDTOList
            .stream()
            .filter(x ->
                x.getAttendanceStatus() == AttendanceStatus.ABSENT &&
                !x.isHasPendingLeaveApplication() &&
                !x.isHasPendingManualAttendance() &&
                !x.isHasPendingMovementEntry()
            )
            .collect(Collectors.toList());

        if (absentDays.size() > 0) {
            List<String> to = new ArrayList<>();
            List<String> cc = new ArrayList<>();
            List<String> bcc = new ArrayList<>();
            String subject = "Notification on absent days";
            String templateName = MAIL_TEMPLATE_ABSENT_DAYS_REMINDER;

            Map<String, Object> variableMap = new HashMap<>();

            to.add(employee.getOfficialEmail().toLowerCase(Locale.ROOT));

            variableMap.put("name", employee.getFullName());
            variableMap.put("numberOfDays", absentDays.size());
            variableMap.put("daysList", getDateListAsString(absentDays));

            emailService.sendEmailFromTemplates(from, to, cc, bcc, subject, templateName, variableMap);
        }
    }

    public String getDateListAsString(List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList) {
        String str = "";

        int count = 1;

        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList1 = attendanceTimeSheetDTOList;
        Collections.reverse(attendanceTimeSheetDTOList1);

        for (AttendanceTimeSheetDTO attendanceTimeSheetDTO : attendanceTimeSheetDTOList1) {
            String dateAsString = attendanceTimeSheetDTO.getDate().format(DateTimeFormatter.ofPattern("dd-MMMM-uuuu", Locale.ENGLISH));

            str += dateAsString;

            if (count < attendanceTimeSheetDTOList.size()) {
                str += ", ";
            }
            count++;
        }

        return str;
    }
}
