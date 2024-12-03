package com.bits.hr.service.attendanceSync.helperService;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.attendanceTimeSheet.MonthlyAttendanceTimeSheetDTO;
import com.bits.hr.service.dto.AttendanceSummaryDTO;
import com.bits.hr.util.DateUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveSummaryProcessingService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    public AttendanceSummaryDTO processDTO(
        MonthlyAttendanceTimeSheetDTO monthlyAttendanceTimeSheetDTO,
        int month,
        int year,
        LocalDate startDate,
        LocalDate endDate
    ) {
        // todo: if general case --> process with this block of code
        // else : process special
        Optional<Employee> employeeOptional = employeeRepository.findById(monthlyAttendanceTimeSheetDTO.getEmployeeId());

        LocalDate doj = employeeOptional.get().getDateOfJoining();

        List<EmployeeResignation> employeeResignation = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
            monthlyAttendanceTimeSheetDTO.getEmployeeId()
        );

        Optional<LocalDate> lastWorkingDay = Optional.empty();

        if (employeeResignation.size() > 0) {
            lastWorkingDay = Optional.of(employeeResignation.get(0).getLastWorkingDay());
        }

        // check : is date of last working day between this month time range
        boolean isLastWorkingDayBetween = false;
        boolean isDateOfJoiningBetween = false;
        LocalDate monthStartDate = LocalDate.of(year, month, 1);
        LocalDate monthEndDate = LocalDate.of(year, month, monthStartDate.lengthOfMonth());
        if (lastWorkingDay.isPresent() && DateUtil.isBetween(lastWorkingDay.get(), monthStartDate, monthEndDate)) {
            isLastWorkingDayBetween = true;
        }
        if (employeeOptional.isPresent() && DateUtil.isBetween(doj, monthStartDate, monthEndDate)) {
            isDateOfJoiningBetween = true;
        }

        // general case
        if (isDateOfJoiningBetween == false && isLastWorkingDayBetween == false) {
            long employeeId = monthlyAttendanceTimeSheetDTO.getEmployeeId();
            int absentDays = (int) monthlyAttendanceTimeSheetDTO
                .getAttendanceTimeSheetMiniList()
                .stream()
                .filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT)
                .count();

            int leaveDays = (int) monthlyAttendanceTimeSheetDTO
                .getAttendanceTimeSheetMiniList()
                .stream()
                .filter(x -> x.getAttendanceStatus() == AttendanceStatus.LEAVE)
                .count();

            int workingDays = (int) monthlyAttendanceTimeSheetDTO
                .getAttendanceTimeSheetMiniList()
                .stream()
                .filter(x -> x.getAttendanceStatus() == AttendanceStatus.PRESENT)
                .count();

            // handle resigning employee service month
            // handle joining employee service month
            int daysInMonth = monthStartDate.lengthOfMonth(); //monthLengthCalculationService.getDaysInMonth(employeeId, year, month);
            int fractionDays = daysInMonth - absentDays;

            AttendanceSummaryDTO attendanceSummaryDTO = new AttendanceSummaryDTO();

            attendanceSummaryDTO.setEmployeeId(employeeId);
            attendanceSummaryDTO.setMonth(month);
            attendanceSummaryDTO.setYear(year);
            attendanceSummaryDTO.setTotalAbsentDays(absentDays);
            attendanceSummaryDTO.setTotalFractionDays(fractionDays);
            attendanceSummaryDTO.setTotalLeaveDays(leaveDays);
            attendanceSummaryDTO.setTotalWorkingDays(workingDays);
            return attendanceSummaryDTO;
        } else {
            // special cases
            AttendanceSummaryDTO attendanceSummaryDTO = new AttendanceSummaryDTO();

            attendanceSummaryDTO.setEmployeeId(monthlyAttendanceTimeSheetDTO.getEmployeeId());
            attendanceSummaryDTO.setMonth(month);
            attendanceSummaryDTO.setYear(year);

            // case 1 (both)
            if (isDateOfJoiningBetween == true && isLastWorkingDayBetween == true) {
                List<AttendanceTimeSheetDTO> attendanceTimeSheet = attendanceTimeSheetService.getAttendanceTimeSheet(
                    employeeOptional.get().getId(),
                    doj,
                    lastWorkingDay.get(),
                    AtsDataProcessLevel.FULL_FEATURED_USER
                );

                int absentDays = attendanceTimeSheet
                    .stream()
                    .filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT)
                    .collect(Collectors.toList())
                    .size();

                int leaveDays = attendanceTimeSheet
                    .stream()
                    .filter(x ->
                        x.getAttendanceStatus() == AttendanceStatus.LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_MATERNITY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE
                    )
                    .collect(Collectors.toList())
                    .size();

                int workingDays = attendanceTimeSheet
                    .stream()
                    .filter(x -> x.getAttendanceStatus() == AttendanceStatus.PRESENT)
                    .collect(Collectors.toList())
                    .size();

                int fractionDays = (int) ChronoUnit.DAYS.between(doj, lastWorkingDay.get()) + 1 - absentDays;

                attendanceSummaryDTO.setTotalAbsentDays(absentDays);
                attendanceSummaryDTO.setTotalFractionDays(fractionDays);
                attendanceSummaryDTO.setTotalLeaveDays(leaveDays);
                attendanceSummaryDTO.setTotalWorkingDays(workingDays);
                return attendanceSummaryDTO;
            }

            // case 2 (DOJ)
            if (isDateOfJoiningBetween == true && isLastWorkingDayBetween == false) {
                List<AttendanceTimeSheetDTO> attendanceTimeSheet = attendanceTimeSheetService.getAttendanceTimeSheet(
                    employeeOptional.get().getId(),
                    doj,
                    endDate,
                    AtsDataProcessLevel.FULL_FEATURED_USER
                );

                int absentDays = attendanceTimeSheet
                    .stream()
                    .filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT)
                    .collect(Collectors.toList())
                    .size();
                int leaveDays = attendanceTimeSheet
                    .stream()
                    .filter(x ->
                        x.getAttendanceStatus() == AttendanceStatus.LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_MATERNITY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE
                    )
                    .collect(Collectors.toList())
                    .size();
                int workingDays = attendanceTimeSheet
                    .stream()
                    .filter(x -> x.getAttendanceStatus() == AttendanceStatus.PRESENT)
                    .collect(Collectors.toList())
                    .size();

                int fractionDays = (int) ChronoUnit.DAYS.between(doj, monthEndDate) + 1 - absentDays;

                attendanceSummaryDTO.setTotalAbsentDays(absentDays);
                attendanceSummaryDTO.setTotalFractionDays(fractionDays);
                attendanceSummaryDTO.setTotalLeaveDays(leaveDays);
                attendanceSummaryDTO.setTotalWorkingDays(workingDays);
                return attendanceSummaryDTO;
            }

            // case 1 (LWD)
            if (isDateOfJoiningBetween == false && isLastWorkingDayBetween == true) {
                List<AttendanceTimeSheetDTO> attendanceTimeSheet = attendanceTimeSheetService.getAttendanceTimeSheet(
                    employeeOptional.get().getId(),
                    startDate,
                    lastWorkingDay.get(),
                    AtsDataProcessLevel.FULL_FEATURED_USER
                );

                int absentDays = attendanceTimeSheet
                    .stream()
                    .filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT)
                    .collect(Collectors.toList())
                    .size();

                int leaveDays = attendanceTimeSheet
                    .stream()
                    .filter(x ->
                        x.getAttendanceStatus() == AttendanceStatus.LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_MATERNITY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE ||
                        x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE
                    )
                    .collect(Collectors.toList())
                    .size();

                int workingDays = attendanceTimeSheet
                    .stream()
                    .filter(x -> x.getAttendanceStatus() == AttendanceStatus.PRESENT)
                    .collect(Collectors.toList())
                    .size();

                int fractionDays = (int) ChronoUnit.DAYS.between(monthStartDate, lastWorkingDay.get()) + 1 - absentDays;

                attendanceSummaryDTO.setTotalAbsentDays(absentDays);
                attendanceSummaryDTO.setTotalFractionDays(fractionDays);
                attendanceSummaryDTO.setTotalLeaveDays(leaveDays);
                attendanceSummaryDTO.setTotalWorkingDays(workingDays);
                return attendanceSummaryDTO;
            }

            attendanceSummaryDTO.setTotalAbsentDays(0);
            attendanceSummaryDTO.setTotalFractionDays(0);
            attendanceSummaryDTO.setTotalLeaveDays(0);
            attendanceSummaryDTO.setTotalWorkingDays(0);

            return attendanceSummaryDTO;
        }
        /* special processing sudo code:
         *      check DOJ in between startDate-endDate range
         *      Check LWD in between start date - ( endDate || last working day/last day of month ) Range
         *      if both occur
         *           ABSENT LOGIC: doj to lwd -- count absent days from ATS service
         *           DAY COUNT LOGIC: from doj to lwd
         *      else if doj issue ->
         *           ABSENT LOGIC: doj to end date --> calculate absent from ATS service
         *      DAY COUNT LOGIC: from doj to end of month
         *      else if lwd issue ->
         *          ABSENT LOGIC: start date to lwd --> calculate absent from ATS service
         *          DAY COUNT LOGIC: from start of month to last working day
         * */

    }
}
