package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.communication.sheduledEmail.AbsentDaysRemainderService;
import com.bits.hr.service.communication.sheduledEmail.HolidayEmailService;
import com.bits.hr.service.communication.sheduledEmail.PendingApprovalRemainderService;
import com.bits.hr.service.communication.sheduledEmail.TaxAcknowledgementReminderService;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MorningSchedulerService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private HolidayEmailService holidayEmailService;

    @Autowired
    private AbsentDaysRemainderService absentDaysRemainderService;

    @Autowired
    private WorkFromHomeSchedulerService workFromHomeSchedulerService;

    @Autowired
    private AutomatedWFHRejectSchedulerService automatedWFHRejectSchedulerService;

    @Autowired
    private PendingApprovalRemainderService pendingApprovalRemainderService;

    @Autowired
    private TaxAcknowledgementReminderService taxAcknowledgementSchedulerService;

    @Autowired
    private AutoPunchInAndOutService autoPunchInAndOutService;

    @Autowired
    private SeparationReminderMailSchedulerService separationReminderMailSchedulerService;

    public void morning() {
        try {
            LocalDate today = LocalDate.now();
            List<Employee> activeEmployeeList = findActiveEmployee(today);

            // WFH remainder
            workFromHomeSchedulerService.workFromHomeEndReminder(today);

            //WFH application reject email
            automatedWFHRejectSchedulerService.workFromHomeRejectScheduler(today.minusDays(3));

            // holiday email
            holidayEmailService.sendHolidayRemainder(today.plusDays(2), activeEmployeeList);

            if (today.getDayOfMonth() == 22) {
                // approve all pending applications
                // regularize attendance
                // .... others
            }

            if (today.getDayOfMonth() >= 15 && today.getDayOfMonth() <= 22) {
                LocalDate previousMonth = today.minusMonths(1);
                LocalDate arStartDate = LocalDate.of(previousMonth.getYear(), previousMonth.getMonth(), 22);
                LocalDate arEndDate = today.minusDays(1);

                // Absent days remainder
                absentDaysRemainderService.absentDaysRemainder(activeEmployeeList, arStartDate, arEndDate);

                // pending Approval remainder (Attendance Application, Leave Application, MovementEntry) Remainder
                pendingApprovalRemainderService.remainder(activeEmployeeList);
            }

            // tax acknowledgement receipt email
            taxAcknowledgementSchedulerService.reminderWhoNotSubmitted(LocalDate.now());

            autoPunchInAndOutService.autoPunchInAndOutForEligibleEmployee(LocalDate.now().minusDays(1)); //previous day auto punch and out

            // reminder mail send to HR to replace new LM for Resigned LM
            separationReminderMailSchedulerService.sendMailForReplacementOfResignedEmployee(LocalDate.now());
        } catch (Exception ex) {
            log.error(ex);
            log.error("Exception occurred:: morning scheduler failed");
        }
    }

    private List<Employee> findActiveEmployee(LocalDate date) {
        LocalDate firstDayOfMonth = LocalDate.of(date.getYear(), date.getMonthValue(), 01);
        LocalDate lastDayOfMonth = LocalDate.of(date.getYear(), date.getMonthValue(), date.lengthOfMonth());
        return employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(firstDayOfMonth, lastDayOfMonth);
    }
}
