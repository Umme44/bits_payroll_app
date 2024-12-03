package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.enumeration.AttendanceStatus;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Deprecated
public class AttendanceTimeSheetProcessingServiceImpl implements AttendanceTimeSheetProcessingService {

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    public int getAbsentDaysByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeId,
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_USER
        );

        int absentDays;
        absentDays = (int) attendanceTimeSheetDTOList.stream().filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT).count();

        return absentDays;
    }

    public int getFractionDaysByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeId,
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_USER
        );

        int absentDays;
        absentDays = (int) attendanceTimeSheetDTOList.stream().filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT).count();

        int daysBetween;
        daysBetween = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

        int fractionDays;
        fractionDays = daysBetween - absentDays;

        return fractionDays;
    }

    public int getLateDaysByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeId,
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_USER
        );

        int lateDays;
        lateDays = (int) attendanceTimeSheetDTOList.stream().filter(x -> x.getAttendanceStatus() == AttendanceStatus.LATE).count();

        return lateDays;
    }

    public int getTotalWorkingDaysByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeId,
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_USER
        );

        int workingDays;
        workingDays =
            (int) attendanceTimeSheetDTOList
                .stream()
                .filter(x -> x.getAttendanceStatus() == AttendanceStatus.PRESENT || x.getAttendanceStatus() == AttendanceStatus.LATE)
                .count();

        return workingDays;
    }

    public int getTotalHolidaysConsumedIncludingWeekendByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeId,
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_USER
        );

        int holidaysConsumed;
        holidaysConsumed =
            (int) attendanceTimeSheetDTOList
                .stream()
                .filter(x ->
                    x.getAttendanceStatus() == AttendanceStatus.GOVT_HOLIDAY ||
                    x.getAttendanceStatus() == AttendanceStatus.SPECIAL_HOLIDAY ||
                    x.getAttendanceStatus() == AttendanceStatus.WEEKLY_OFFDAY
                )
                .count();

        return holidaysConsumed;
    }

    public int getGainedCompensatoryLeaveByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeId,
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_USER
        );

        int gainedCompensatoryLeave;
        gainedCompensatoryLeave =
            (int) attendanceTimeSheetDTOList.stream().filter(AttendanceTimeSheetDTO::isGainedCompensatoryLeave).count();

        return gainedCompensatoryLeave;
    }

    public int getTotalTakenLeaveByPinBetweenDates(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeId,
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_USER
        );

        int totalTakenLeave;
        totalTakenLeave =
            (int) attendanceTimeSheetDTOList
                .stream()
                .filter(x ->
                    x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE ||
                    x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE ||
                    x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE ||
                    x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_MATERNITY_LEAVE ||
                    x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE ||
                    x.getAttendanceStatus() == AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE
                )
                .count();

        return totalTakenLeave;
    }
}
