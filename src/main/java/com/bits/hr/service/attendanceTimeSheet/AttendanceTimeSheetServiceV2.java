package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.HolidayType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.*;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.TimeSlotService;
import com.bits.hr.service.dto.DateRangeDTO;
import com.bits.hr.util.MathRoundUtil;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AttendanceTimeSheetServiceV2 {

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private MovementEntryRepository movementEntryRepository;

    @Autowired
    private HolidaysRepository holidaysRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManualAttendanceEntryRepository manualAttendanceEntryRepository;

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private SpecialShiftTimingRepository specialShiftTimingRepository;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceSharedService attendanceSharedService;

    public List<AttendanceTimeSheetDTO> getAttendanceTimeSheet(
        long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        AtsDataProcessLevel atsDataProcessLevel
    ) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = new ArrayList<>();

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (!employeeOptional.isPresent()) {
            log.error("Employee Not Found By EmployeeId:" + employeeId);
            throw new BadRequestAlertException("Employee Not Found By Employee Id:" + employeeId, "employee", "noEmployeeFound");
        }

        Employee employee = employeeOptional.get();

        // for editable checking
        LocalDate maxAllowedPreviousDate = attendanceSharedService.getMaxAllowedPreviousDate();

        // *** hashtable for LeaveApplication(Pending)
        Hashtable<LocalDate, LeaveApplication> pendingLeaveApplicationHashtable = new Hashtable<>();
        // *** hashtable for MovementEntry(Pending)
        Hashtable<LocalDate, MovementEntry> pendingMovementEntryHashtable = new Hashtable<>();
        // *** hashtable for ManualAttendanceEntry(Pending)
        Hashtable<LocalDate, ManualAttendanceEntry> pendingManualAttendanceEntryHashTable = new Hashtable<>();

        if (
            atsDataProcessLevel.equals(AtsDataProcessLevel.FULL_FEATURED_USER) ||
            atsDataProcessLevel.equals(AtsDataProcessLevel.FULL_FEATURED_ADMIN)
        ) {
            pendingLeaveApplicationHashtable = getPendingLeaveApplicationByDate(employee.getId(), startDate, endDate);
            pendingMovementEntryHashtable = getPendingMovementEntryByDate(employee.getId(), startDate, endDate);
            pendingManualAttendanceEntryHashTable = getPendingManualAttendanceEntryByDate(employee.getId(), startDate, endDate);
        }

        // *** hashtable for flex schedule application (approved)
        Hashtable<LocalDate, FlexScheduleApplication> flexScheduleApplicationHashtable = getApprovedFlexScheduleApplicationByDate(
            employee.getId(),
            startDate,
            endDate
        );
        TimeSlot defaultTimeSlot = timeSlotService.getDefaultTimeSlot();

        /* Ramadan Office Time has higher priority than Flex Schedule Application*/
        // getRamadan Schedule HashTable from date.
        Hashtable<LocalDate, SpecialShiftTiming> ramadanScheduleHashtable = getRamadanScheduleByDate(startDate, endDate);

        // *** hashtable for attendance
        // data retrieved between date range too for memory optimization purposes
        Hashtable<LocalDate, AttendanceEntry> attendanceEntryHashtable = getAttendanceEntryByDate(employeeId, startDate, endDate);

        // *** hashtable for LeaveApplication
        // data retrieved between date range too for memory optimization purposes
        // unapproved leave will not be taken
        Hashtable<LocalDate, LeaveApplication> leaveApplicationHashtable = getApprovedLeaveApplicationByDate(
            employeeId,
            startDate,
            endDate
        );

        // *** hashtable for movementEntry
        // data retrieved between date range too for memory optimization purposes
        // unapproved leave will not be taken
        Hashtable<LocalDate, MovementEntry> movementEntryHashtable = getApprovedMovementEntryByDate(employeeId, startDate, endDate);

        // *** hashtable for holidays
        // data retrieved between date range too for memory optimization purposes
        Hashtable<LocalDate, Holidays> holidaysHashtable = getHolidaysByDate(startDate, endDate);

        // get service tenure for BLANK status checking
        DateRangeDTO dateRangeDTO = employeeService.getServiceTenureDateRange(employee);
        LocalDate serviceTenureStartDate = dateRangeDTO.getStartDate();
        LocalDate serviceTenureEndDate = dateRangeDTO.getEndDate();

        // main loop between start date and end date
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            AttendanceTimeSheetDTO attendanceTimeSheetDTO = new AttendanceTimeSheetDTO();

            attendanceTimeSheetDTO.setDate(date);

            boolean isWeekend = isWeekend(date, flexScheduleApplicationHashtable);

            // first check , if present
            // if date key contains in attendanceEntryHashtable,  present , calculate according to logic
            // calculate if gained Compensatory leave at the end

            /* More than 4 Hr work in holiday = 1 compulsory leave gain
             *Compensatory Leave can’t be carried forward to the next year.
             */
            //blank in effective status

            // priority: BLANK_INEFFECTIVE > PRESENT_WEEKLY_OFFDAY(*) > PRESENT_GOVT_HOLIDAY(*) >
            // NON_FULFILLED_OFFICE_HOURS(*) >

            if (
                (serviceTenureStartDate != null && date.isBefore(serviceTenureStartDate)) ||
                (serviceTenureEndDate != null && date.isAfter(serviceTenureEndDate))
            ) {
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.BLANK_INEFFECTIVE);
            } else if (attendanceEntryHashtable.containsKey(date) && isWeekend) {
                AttendanceEntry attendanceEntry = attendanceEntryHashtable.get(date);
                attendanceTimeSheetDTO.setInTime(attendanceEntry.getInTime());
                attendanceTimeSheetDTO.setOutTime(attendanceEntry.getOutTime());
                attendanceTimeSheetDTO.setInNote(attendanceEntry.getInNote());
                attendanceTimeSheetDTO.setOutNote(attendanceEntry.getOutNote());
                attendanceTimeSheetDTO.setStatus(attendanceEntry.getStatus());
                attendanceTimeSheetDTO.setPunchInDeviceOrigin(attendanceEntry.getPunchInDeviceOrigin());
                attendanceTimeSheetDTO.setPunchOutDeviceOrigin(attendanceEntry.getPunchOutDeviceOrigin());
                attendanceTimeSheetDTO.setIsAutoPunchOut(attendanceEntry.isIsAutoPunchOut());
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.PRESENT_WEEKLY_OFFDAY);

                //Total working Hour Calculation || not calculating if not out yet (out time = = null)
                if (attendanceEntry.getInTime() != null && attendanceEntry.getOutTime() != null) {
                    long totalWorkingMinutes = ChronoUnit.MINUTES.between(attendanceEntry.getInTime(), attendanceEntry.getOutTime());
                    double totalWorkingHour = MathRoundUtil.roundUpToTwoDecimal(
                        Math.floor((double) totalWorkingMinutes / 60d) + (((int) totalWorkingMinutes % 60d) / 100)
                    );
                    attendanceTimeSheetDTO.setTotalWorkingHour(totalWorkingHour);
                }
            } else if (attendanceEntryHashtable.containsKey(date) && (holidaysHashtable.containsKey(date))) {
                AttendanceEntry attendanceEntry = attendanceEntryHashtable.get(date);
                attendanceTimeSheetDTO.setInTime(attendanceEntry.getInTime());
                attendanceTimeSheetDTO.setOutTime(attendanceEntry.getOutTime());
                attendanceTimeSheetDTO.setInNote(attendanceEntry.getInNote());
                attendanceTimeSheetDTO.setOutNote(attendanceEntry.getOutNote());
                attendanceTimeSheetDTO.setStatus(attendanceEntry.getStatus());
                attendanceTimeSheetDTO.setPunchInDeviceOrigin(attendanceEntry.getPunchInDeviceOrigin());
                attendanceTimeSheetDTO.setPunchOutDeviceOrigin(attendanceEntry.getPunchOutDeviceOrigin());
                attendanceTimeSheetDTO.setIsAutoPunchOut(attendanceEntry.isIsAutoPunchOut());
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.PRESENT_GOVT_HOLIDAY);

                //Total working Hour Calculation || not calculating if not out yet (out time = = null)
                if (attendanceEntry.getInTime() != null && attendanceEntry.getOutTime() != null) {
                    long totalWorkingMinutes = ChronoUnit.MINUTES.between(attendanceEntry.getInTime(), attendanceEntry.getOutTime());
                    double totalWorkingHour = MathRoundUtil.roundUpToTwoDecimal(
                        Math.floor((double) totalWorkingMinutes / 60d) + (((int) totalWorkingMinutes % 60d) / 100)
                    );
                    attendanceTimeSheetDTO.setTotalWorkingHour(totalWorkingHour);
                }
            } else if (attendanceEntryHashtable.containsKey(date)) {
                AttendanceEntry attendanceEntry = attendanceEntryHashtable.get(date);
                attendanceTimeSheetDTO.setInTime(attendanceEntry.getInTime());
                attendanceTimeSheetDTO.setOutTime(attendanceEntry.getOutTime());
                attendanceTimeSheetDTO.setInNote(attendanceEntry.getInNote());
                attendanceTimeSheetDTO.setOutNote(attendanceEntry.getOutNote());
                attendanceTimeSheetDTO.setStatus(attendanceEntry.getStatus());
                attendanceTimeSheetDTO.setPunchInDeviceOrigin(attendanceEntry.getPunchInDeviceOrigin());
                attendanceTimeSheetDTO.setPunchOutDeviceOrigin(attendanceEntry.getPunchOutDeviceOrigin());
                attendanceTimeSheetDTO.setIsAutoPunchOut(attendanceEntry.isIsAutoPunchOut());

                //Total working Hour Calculation || not calculating if not out yet (out time = = null)
                if (attendanceEntry.getInTime() != null && attendanceEntry.getOutTime() != null) {
                    long totalWorkingMinutes = ChronoUnit.MINUTES.between(attendanceEntry.getInTime(), attendanceEntry.getOutTime());
                    double totalWorkingHour = MathRoundUtil.roundUpToTwoDecimal(
                        Math.floor((double) totalWorkingMinutes / 60d) + (((int) totalWorkingMinutes % 60d) / 100)
                    );
                    attendanceTimeSheetDTO.setTotalWorkingHour(totalWorkingHour);

                    TimeSlot officeTime;
                    if (ramadanScheduleHashtable.containsKey(date) && ramadanScheduleHashtable.get(date).getOverrideFlexSchedule()) {
                        officeTime = ramadanScheduleHashtable.get(date).getTimeSlot();
                    } else if (flexScheduleApplicationHashtable.containsKey(date)) {
                        officeTime = flexScheduleApplicationHashtable.get(date).getTimeSlot();
                    } else {
                        officeTime = defaultTimeSlot;
                    }

                    long officeTimeDuration = ChronoUnit.HOURS.between(officeTime.getInTime(), officeTime.getOutTime());
                    if (totalWorkingHour < officeTimeDuration) {
                        attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.NON_FULFILLED_OFFICE_HOURS);
                    }
                }

                // late duration calculation
                if (attendanceEntry.getInTime() != null) {
                    TimeSlot officeTime;
                    if (ramadanScheduleHashtable.containsKey(date) && ramadanScheduleHashtable.get(date).getOverrideFlexSchedule()) {
                        officeTime = ramadanScheduleHashtable.get(date).getTimeSlot();
                    } else if (flexScheduleApplicationHashtable.containsKey(date)) {
                        officeTime = flexScheduleApplicationHashtable.get(date).getTimeSlot();
                    } else {
                        officeTime = defaultTimeSlot;
                    }

                    LocalDateTime startTime = LocalDateTime.ofInstant(officeTime.getInTime(), ZoneOffset.systemDefault());
                    int startTimeInMinutes = (startTime.getHour() * 60) + startTime.getMinute();
                    LocalDateTime inTime = LocalDateTime.ofInstant(attendanceEntry.getInTime(), ZoneOffset.systemDefault());

                    int reportingTimeInMinutes = (inTime.getHour() * 60) + inTime.getMinute();
                    // if late, calculate late time
                    // else late time is null
                    if (reportingTimeInMinutes > startTimeInMinutes) {
                        double lateDurationInMinutes = reportingTimeInMinutes - startTimeInMinutes;
                        double lateDuration = MathRoundUtil.roundUpToTwoDecimal(
                            Math.floor(lateDurationInMinutes / 60) + ((lateDurationInMinutes % 60) / 100)
                        );
                        attendanceTimeSheetDTO.setLateDuration(lateDuration);
                        attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.LATE);
                        if (attendanceTimeSheetDTO.getAttendanceStatus() != AttendanceStatus.NON_FULFILLED_OFFICE_HOURS) {
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.LATE);
                        }
                    } else {
                        attendanceTimeSheetDTO.setLateDuration(0d);
                        if (attendanceTimeSheetDTO.getAttendanceStatus() != AttendanceStatus.NON_FULFILLED_OFFICE_HOURS) {
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.PRESENT);
                        }
                    }
                }
                // Compensatory Leave calculation , allowed for working in holiday or weekday
                if (holidaysHashtable.containsKey(date) || isWeekend) {
                    if (attendanceTimeSheetDTO.getTotalWorkingHour() != null && attendanceTimeSheetDTO.getTotalWorkingHour() >= 4) {
                        attendanceTimeSheetDTO.setGainedCompensatoryLeave(true);
                    }
                } else {
                    attendanceTimeSheetDTO.setGainedCompensatoryLeave(false);
                }
            }
            // else ( not present ),
            //        first check if it's holiday or not ..2
            //        then check it's weekday offday or not,..3
            //        then check if it's leave (approved) , unapproved will not be counted .. 4
            //        if not Leave && not weekday && not offday then it's Absent

            // movement entry check
            else if (movementEntryHashtable.containsKey(date)) {
                MovementEntry movementEntry = movementEntryHashtable.get(date);
                attendanceTimeSheetDTO.setInTime(movementEntry.getStartTime());
                attendanceTimeSheetDTO.setOutTime(movementEntry.getEndTime());
                attendanceTimeSheetDTO.setPunchInDeviceOrigin(AttendanceDeviceOrigin.WEB);
                attendanceTimeSheetDTO.setPunchOutDeviceOrigin(AttendanceDeviceOrigin.WEB);
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.MOVEMENT);
            }
            // Holiday check
            else if (holidaysHashtable.containsKey(date)) {
                Holidays holidays = holidaysHashtable.get(date);
                if (holidays.getHolidayType().equals(HolidayType.Govt)) {
                    attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.GOVT_HOLIDAY);
                } else if (holidays.getHolidayType().equals(HolidayType.General)) {
                    attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.GENERAL_HOLIDAY);
                }
            }
            // weekly OFF-DAY check
            else if (isWeekend) {
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.WEEKLY_OFFDAY);
            }
            // check , if in Leave
            else if (leaveApplicationHashtable.containsKey(date)) {
                LeaveApplication leaveApplication = leaveApplicationHashtable.get(date);

                if (
                    (leaveApplication.getIsLineManagerApproved() != null && leaveApplication.getIsLineManagerApproved()) ||
                    (leaveApplication.getIsHRApproved() != null && leaveApplication.getIsHRApproved())
                ) {
                    switch (leaveApplication.getLeaveType()) {
                        case MENTIONABLE_ANNUAL_LEAVE:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE);
                            break;
                        case MENTIONABLE_CASUAL_LEAVE:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.MENTIONABLE_CASUAL_LEAVE);
                            break;
                        case NON_MENTIONABLE_PANDEMIC_LEAVE:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE);
                            break;
                        case NON_MENTIONABLE_MATERNITY_LEAVE:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.NON_MENTIONABLE_MATERNITY_LEAVE);
                            break;
                        case NON_MENTIONABLE_PATERNITY_LEAVE:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE);
                            break;
                        case NON_MENTIONABLE_COMPENSATORY_LEAVE:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE);
                            break;
                        case LEAVE_WITHOUT_PAY:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.LEAVE_WITHOUT_PAY);
                            break;
                        case LEAVE_WITHOUT_PAY_SANDWICH:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.LEAVE_WITHOUT_PAY);
                            break;
                        default:
                            attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.OTHER);
                    }
                }
            } else if (isFutureDate(date)) {
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.BLANK);
            }
            // absent if not approved
            else {
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.ABSENT);
            }

            if (
                atsDataProcessLevel == AtsDataProcessLevel.FULL_FEATURED_USER ||
                atsDataProcessLevel == AtsDataProcessLevel.FULL_FEATURED_ADMIN
            ) {
                // find any pending
                /* 1. Leave Application
                   2. Manual Attendance
                   3. Movement Entry
                */
                if (pendingLeaveApplicationHashtable.containsKey(date)) {
                    attendanceTimeSheetDTO.setHasPendingLeaveApplication(true);
                } else {
                    attendanceTimeSheetDTO.setHasPendingLeaveApplication(false);
                }

                if (pendingMovementEntryHashtable.containsKey(date)) {
                    attendanceTimeSheetDTO.setHasPendingMovementEntry(true);
                } else {
                    attendanceTimeSheetDTO.setHasPendingMovementEntry(false);
                }

                if (pendingManualAttendanceEntryHashTable.containsKey(date)) {
                    attendanceTimeSheetDTO.setHasPendingManualAttendance(true);
                } else {
                    attendanceTimeSheetDTO.setHasPendingManualAttendance(false);
                }
            }

            if (atsDataProcessLevel == AtsDataProcessLevel.FULL_FEATURED_USER) {
                /* find editable AttendanceStatus in attendanceTimeSheet */
                // note: java is reference based no need to reassign
                checkIsEditable(attendanceTimeSheetDTO, date, maxAllowedPreviousDate);
            } else if (atsDataProcessLevel == AtsDataProcessLevel.FULL_FEATURED_ADMIN) {
                /* find editable AttendanceStatus in attendanceTimeSheet */
                // note: java is reference based no need to reassign
                checkIsEditableAdmin(attendanceTimeSheetDTO);
            }

            attendanceTimeSheetDTOList.add(attendanceTimeSheetDTO);
        }
        Collections.reverse(attendanceTimeSheetDTOList);
        return attendanceTimeSheetDTOList;
    }

    private static boolean isWeekend(LocalDate date, Hashtable<LocalDate, FlexScheduleApplication> flexScheduleApplicationHashtable) {
        boolean isWeekend = date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY;
        if (flexScheduleApplicationHashtable != null && flexScheduleApplicationHashtable.containsKey(date)) {
            FlexScheduleApplication flexScheduleApplication = flexScheduleApplicationHashtable.get(date);
            if (flexScheduleApplication != null && flexScheduleApplication.getTimeSlot() != null
                && flexScheduleApplication.getTimeSlot().getWeekEnds() != null) {
                if (flexScheduleApplication.getTimeSlot().getWeekEnds().toLowerCase().contains(date.getDayOfWeek().toString().toLowerCase())) {
                    ;
                    isWeekend = true;
                } else {
                    isWeekend = false;
                }
            }
        }
        return isWeekend;
    }

    // get list of attendance by pin in a hashtable
    // key will be LocalDate , one employee will have one unique date in attendance Table
    // now mapping will be efficient as hashtable data can be retrieved at O(1) complexity

    private Hashtable<LocalDate, LeaveApplication> getApprovedLeaveApplicationByDate(
        long employeeId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        Hashtable<LocalDate, LeaveApplication> leaveApplicationHashtable = new Hashtable<>();
        List<LeaveApplication> leaveApplicationListList = leaveApplicationRepository.getApprovedLeaveApplicationsByEmployeeIdBetweenTwoDates(
            employeeId,
            startDate,
            endDate
        );
        for (LeaveApplication leaveApplication : leaveApplicationListList) {
            // if range , Key => range 0-n , same object
            if (ChronoUnit.DAYS.between(leaveApplication.getStartDate(), leaveApplication.getEndDate()) + 1 > 1) {
                for (
                    LocalDate date = leaveApplication.getStartDate();
                    date.isBefore(leaveApplication.getEndDate().plusDays(1));
                    date = date.plusDays(1)
                ) {
                    leaveApplicationHashtable.put(date, leaveApplication);
                }
            }
            // single date , single key , no object duplication
            else {
                leaveApplicationHashtable.put(leaveApplication.getStartDate(), leaveApplication);
            }
        }
        return leaveApplicationHashtable;
    }

    private Hashtable<LocalDate, MovementEntry> getApprovedMovementEntryByDate(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<MovementEntry> movementEntryList = movementEntryRepository.getApprovedMovementEntriesByEmployeeIdBetweenTwoDates(
            employeeId,
            startDate,
            endDate
        );
        Hashtable<LocalDate, MovementEntry> movementEntryHashtable = new Hashtable<>();
        for (MovementEntry movementEntry : movementEntryList) {
            // if range , Key => range 0-n , same object
            if (ChronoUnit.DAYS.between(movementEntry.getStartDate(), movementEntry.getEndDate()) + 1 > 1) {
                for (
                    LocalDate date = movementEntry.getStartDate();
                    date.isBefore(movementEntry.getEndDate().plusDays(1));
                    date = date.plusDays(1)
                ) {
                    movementEntryHashtable.put(date, movementEntry);
                }
            }
            // single date , single key , no object duplication
            else {
                movementEntryHashtable.put(movementEntry.getStartDate(), movementEntry);
            }
        }
        return movementEntryHashtable;
    }

    private Hashtable<LocalDate, FlexScheduleApplication> getApprovedFlexScheduleApplicationByDate(
        long employeeId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        // *** hashtable for flex schedule application (approved)
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findApprovedApplicationsBetweenEffectiveDate(
            startDate,
            endDate,
            employeeId
        );
        Hashtable<LocalDate, FlexScheduleApplication> flexScheduleApplicationHashtable = new Hashtable<>();
        for (FlexScheduleApplication flexScheduleApplication : flexScheduleApplicationList) {
            for (
                LocalDate date = flexScheduleApplication.getEffectiveFrom();
                date.isBefore(flexScheduleApplication.getEffectiveTo().plusDays(1));
                date = date.plusDays(1)
            ) {
                flexScheduleApplicationHashtable.put(date, flexScheduleApplication);
            }
        }
        return flexScheduleApplicationHashtable;
    }

    private Hashtable<LocalDate, SpecialShiftTiming> getRamadanScheduleByDate(LocalDate startDate, LocalDate endDate) {
        // *** hashtable for ramadan schedule application
        List<SpecialShiftTiming> specialShiftTimingList = specialShiftTimingRepository.findByStartAndEndDateBetween(startDate);
        List<SpecialShiftTiming> specialShiftTimingList1 = specialShiftTimingRepository.findByStartAndEndDateBetween(endDate);
        Hashtable<LocalDate, SpecialShiftTiming> ramadanScheduleHashtable = new Hashtable<>();
        for (SpecialShiftTiming specialShiftTiming : specialShiftTimingList) {
            for (
                LocalDate date = specialShiftTiming.getStartDate();
                date.isBefore(specialShiftTiming.getEndDate().plusDays(1));
                date = date.plusDays(1)
            ) {
                ramadanScheduleHashtable.put(date, specialShiftTiming);
            }
        }
        for (SpecialShiftTiming specialShiftTiming : specialShiftTimingList1) {
            for (
                LocalDate date = specialShiftTiming.getStartDate();
                date.isBefore(specialShiftTiming.getEndDate().plusDays(1));
                date = date.plusDays(1)
            ) {
                ramadanScheduleHashtable.put(date, specialShiftTiming);
            }
        }
        return ramadanScheduleHashtable;
    }

    private Hashtable<LocalDate, LeaveApplication> getPendingLeaveApplicationByDate(
        long employeeId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        Hashtable<LocalDate, LeaveApplication> pendingLeaveApplicationHashtable = new Hashtable<>();
        List<LeaveApplication> leaveApplicationPendingList = leaveApplicationRepository.findPendingLeaveApplicationsByEmployeeId(
            employeeId
        );
        for (LeaveApplication leaveApplication : leaveApplicationPendingList) {
            for (
                LocalDate date = leaveApplication.getStartDate();
                date.isBefore(leaveApplication.getEndDate().plusDays(1));
                date = date.plusDays(1)
            ) {
                pendingLeaveApplicationHashtable.put(date, leaveApplication);
            }
        }
        return pendingLeaveApplicationHashtable;
    }

    private Hashtable<LocalDate, AttendanceEntry> getAttendanceEntryByDate(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.getAttendanceEntryListByEmployeeIdAndBetweenTwoDates(
            employeeId,
            startDate,
            endDate
        );
        Hashtable<LocalDate, AttendanceEntry> attendanceEntryHashtable = new Hashtable<>();
        for (AttendanceEntry attendanceEntry : attendanceEntryList) {
            /* do not consider the seconds, due to duration rounding issue */

            if (attendanceEntry.getInTime() != null) {
                int inTimeSecond = attendanceEntry.getInTime().atZone(ZoneId.systemDefault()).getSecond();
                attendanceEntry.setInTime((attendanceEntry.getInTime()).minusSeconds(inTimeSecond));
            }

            if (attendanceEntry.getOutTime() != null) {
                int outTimeSecond = attendanceEntry.getOutTime().atZone(ZoneId.systemDefault()).getSecond();
                attendanceEntry.setOutTime((attendanceEntry.getOutTime()).minusSeconds(outTimeSecond));
            }

            attendanceEntryHashtable.put(attendanceEntry.getDate(), attendanceEntry);
        }
        return attendanceEntryHashtable;
    }

    private Hashtable<LocalDate, MovementEntry> getPendingMovementEntryByDate(long employeeId, LocalDate startDate, LocalDate endDate) {
        Hashtable<LocalDate, MovementEntry> pendingMovementEntryHashtable = new Hashtable<>();
        List<MovementEntry> movementEntryPendingList = movementEntryRepository.findPendingMovementEntriesByEmployeeIdBetweenDates(
            employeeId,
            startDate,
            endDate
        );
        for (MovementEntry movementEntry : movementEntryPendingList) {
            for (
                LocalDate date = movementEntry.getStartDate();
                date.isBefore(movementEntry.getEndDate().plusDays(1));
                date = date.plusDays(1)
            ) {
                pendingMovementEntryHashtable.put(date, movementEntry);
            }
        }
        return pendingMovementEntryHashtable;
    }

    private Hashtable<LocalDate, ManualAttendanceEntry> getPendingManualAttendanceEntryByDate(
        long employeeId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        Hashtable<LocalDate, ManualAttendanceEntry> pendingManualAttendanceEntryHashTable = new Hashtable<>();
        List<ManualAttendanceEntry> pendingManualAttendanceEntryList = manualAttendanceEntryRepository.findAllPendingByEmployeeIdAndDateRange(
            employeeId,
            startDate,
            endDate
        );
        for (ManualAttendanceEntry manualAttendanceEntry : pendingManualAttendanceEntryList) {
            for (
                LocalDate date = manualAttendanceEntry.getDate();
                date.isBefore(manualAttendanceEntry.getDate().plusDays(1));
                date = date.plusDays(1)
            ) {
                pendingManualAttendanceEntryHashTable.put(date, manualAttendanceEntry);
            }
        }
        return pendingManualAttendanceEntryHashTable;
    }

    private Hashtable<LocalDate, Holidays> getHolidaysByDate(LocalDate startDate, LocalDate endDate) {
        List<Holidays> HolidaysList = holidaysRepository.findHolidaysStartDateBetweenDates(startDate, endDate);
        Hashtable<LocalDate, Holidays> holidaysHashtable = new Hashtable<>();
        for (Holidays holidays : HolidaysList) {
            // if range , Key => range 0-n dates , same object
            if (ChronoUnit.DAYS.between(holidays.getStartDate(), holidays.getEndDate()) + 1 > 1) {
                for (LocalDate date = holidays.getStartDate(); date.isBefore(holidays.getEndDate().plusDays(1)); date = date.plusDays(1)) {
                    holidaysHashtable.put(date, holidays);
                }
            }
            // single date , single key , no object duplication
            else {
                holidaysHashtable.put(holidays.getStartDate(), holidays);
            }
        }
        return holidaysHashtable;
    }

    private boolean isFutureDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }

    private void findPendingApplication(String pin, AttendanceTimeSheetDTO attendanceTimeSheetDTO, LocalDate startDate, LocalDate endDate) {
        // Find any pending
        /*
            1. Leave Application 2. Manual Attendance 3. Movement Entry
         */
        Optional<Employee> employeeOptional = employeeRepository.findByPin(pin);
        if (!employeeOptional.isPresent()) {
            log.error("Employee not found for PIN: {}", pin);
            throw new NoEmployeeProfileException();
        }
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findPendingLeaveApplicationByEmployeeIdAndDateRange(
            employeeOptional.get().getId(),
            startDate,
            endDate
        );
        if (leaveApplications.size() > 0) {
            attendanceTimeSheetDTO.setHasPendingLeaveApplication(true);
        } else {
            attendanceTimeSheetDTO.setHasPendingLeaveApplication(false);
        }

        List<ManualAttendanceEntry> manualAttendanceEntryList = manualAttendanceEntryRepository.findAllPendingByEmployeeIdAndDateRange(
            employeeOptional.get().getId(),
            startDate,
            endDate
        );

        if (manualAttendanceEntryList.size() > 0) {
            attendanceTimeSheetDTO.setHasPendingManualAttendance(true);
        } else {
            attendanceTimeSheetDTO.setHasPendingManualAttendance(false);
        }

        List<MovementEntry> movementEntryList = movementEntryRepository.findPendingMovementEntriesByEmployeeIdBetweenDates(
            employeeOptional.get().getId(),
            startDate,
            endDate
        );

        if (movementEntryList.size() > 0) {
            attendanceTimeSheetDTO.setHasPendingMovementEntry(true);
        } else {
            attendanceTimeSheetDTO.setHasPendingMovementEntry(false);
        }
    }

    private void checkIsEditable(AttendanceTimeSheetDTO attendanceTimeSheetDTO, LocalDate selectedDate, LocalDate maxAllowedPreviousDate) {
        if (AttendanceStatus.BLANK_INEFFECTIVE.equals(attendanceTimeSheetDTO.getAttendanceStatus())) {
            attendanceTimeSheetDTO.setCanApplyAttendanceEntry(false);
            attendanceTimeSheetDTO.setCanApplyMovementEntry(false);
            attendanceTimeSheetDTO.setCanApplyLeaveApplication(false);
            attendanceTimeSheetDTO.setCanEditAttendanceEntry(false);
            return;
        }

        LocalDate today = LocalDate.now();

        /* last max_allowed_previous_days attendance application is allowed */
        if (
            (selectedDate.isEqual(maxAllowedPreviousDate) || selectedDate.isAfter(maxAllowedPreviousDate)) &&
            (selectedDate.isEqual(today) || selectedDate.isBefore(today)) &&
            attendanceTimeSheetDTO.getAttendanceStatus() != null &&
            !attendanceTimeSheetDTO.getAttendanceStatus().equals(AttendanceStatus.MOVEMENT) &&
            !isLeave(attendanceTimeSheetDTO.getAttendanceStatus())
        ) {
            // apply for attendance for absent
            if (
                !isPresent(attendanceTimeSheetDTO.getAttendanceStatus()) &&
                !isLeave(attendanceTimeSheetDTO.getAttendanceStatus()) &&
                attendanceTimeSheetDTO.getInTime() == null &&
                attendanceTimeSheetDTO.getOutTime() == null &&
                !attendanceTimeSheetDTO.isHasPendingManualAttendance() &&
                !attendanceTimeSheetDTO.isHasPendingLeaveApplication() &&
                !attendanceTimeSheetDTO.isHasPendingMovementEntry()
            ) {
                attendanceTimeSheetDTO.setCanApplyAttendanceEntry(true);
            }

            // edit manual attendance entry
            if (attendanceTimeSheetDTO.getInTime() != null || attendanceTimeSheetDTO.getOutTime() != null) {
                if (!attendanceTimeSheetDTO.isHasPendingManualAttendance()) {
                    attendanceTimeSheetDTO.setCanEditAttendanceEntry(true);
                } else {
                    attendanceTimeSheetDTO.setCanEditAttendanceEntry(false);
                }
            }
        } else {
            attendanceTimeSheetDTO.setCanApplyAttendanceEntry(false);
        }

        /* last max_allowed_previous_days and future date is allowed (Movement Entry, Leave Application) */
        if (selectedDate.isEqual(maxAllowedPreviousDate) || selectedDate.isAfter(maxAllowedPreviousDate)) {
            if (
                attendanceTimeSheetDTO.getAttendanceStatus() == AttendanceStatus.ABSENT &&
                !attendanceTimeSheetDTO.isHasPendingLeaveApplication() &&
                !attendanceTimeSheetDTO.isHasPendingManualAttendance() &&
                !attendanceTimeSheetDTO.isHasPendingMovementEntry()
            ) {
                attendanceTimeSheetDTO.setCanApplyLeaveApplication(true);
            }

            if (
                !isPresent(attendanceTimeSheetDTO.getAttendanceStatus()) &&
                !isLeave(attendanceTimeSheetDTO.getAttendanceStatus()) &&
                attendanceTimeSheetDTO.getInTime() == null &&
                attendanceTimeSheetDTO.getOutTime() == null &&
                !attendanceTimeSheetDTO.isHasPendingMovementEntry() &&
                !attendanceTimeSheetDTO.isHasPendingLeaveApplication() &&
                !attendanceTimeSheetDTO.isHasPendingManualAttendance()
            ) {
                attendanceTimeSheetDTO.setCanApplyMovementEntry(true);
            }
        } else {
            attendanceTimeSheetDTO.setCanApplyMovementEntry(false);
            attendanceTimeSheetDTO.setCanApplyLeaveApplication(false);
        }
    }

    private void checkIsEditableAdmin(AttendanceTimeSheetDTO attendanceTimeSheetDTO) {
        /* admin can modify all dates attendance application except blank */
        if (attendanceTimeSheetDTO.getAttendanceStatus().equals(AttendanceStatus.BLANK_INEFFECTIVE)) {
            attendanceTimeSheetDTO.setCanApplyAttendanceEntry(false);
            attendanceTimeSheetDTO.setCanApplyMovementEntry(false);
            attendanceTimeSheetDTO.setCanApplyLeaveApplication(false);
            attendanceTimeSheetDTO.setCanEditAttendanceEntry(false);
            return;
        }

        if (
            !attendanceTimeSheetDTO.getAttendanceStatus().equals(AttendanceStatus.MOVEMENT) &&
            !isLeave(attendanceTimeSheetDTO.getAttendanceStatus())
        ) {
            // apply for attendance for absent
            if (
                !isPresent(attendanceTimeSheetDTO.getAttendanceStatus()) &&
                !isLeave(attendanceTimeSheetDTO.getAttendanceStatus()) &&
                attendanceTimeSheetDTO.getInTime() == null &&
                attendanceTimeSheetDTO.getOutTime() == null &&
                !attendanceTimeSheetDTO.isHasPendingManualAttendance() &&
                !attendanceTimeSheetDTO.isHasPendingLeaveApplication() &&
                !attendanceTimeSheetDTO.isHasPendingMovementEntry()
            ) {
                attendanceTimeSheetDTO.setCanApplyAttendanceEntry(true);
            }

            // edit manual attendance entry
            if (attendanceTimeSheetDTO.getInTime() != null || attendanceTimeSheetDTO.getOutTime() != null) {
                if (!attendanceTimeSheetDTO.isHasPendingManualAttendance()) {
                    attendanceTimeSheetDTO.setCanEditAttendanceEntry(true);
                } else {
                    attendanceTimeSheetDTO.setCanEditAttendanceEntry(false);
                }
            }
        } else {
            attendanceTimeSheetDTO.setCanApplyAttendanceEntry(false);
        }

        if (
            attendanceTimeSheetDTO.getAttendanceStatus() != null &&
            attendanceTimeSheetDTO.getAttendanceStatus().equals(AttendanceStatus.ABSENT) &&
            !attendanceTimeSheetDTO.isHasPendingLeaveApplication() &&
            !attendanceTimeSheetDTO.isHasPendingManualAttendance() &&
            !attendanceTimeSheetDTO.isHasPendingMovementEntry()
        ) {
            attendanceTimeSheetDTO.setCanApplyLeaveApplication(true);
        }

        if (
            !isPresent(attendanceTimeSheetDTO.getAttendanceStatus()) &&
            !isLeave(attendanceTimeSheetDTO.getAttendanceStatus()) &&
            attendanceTimeSheetDTO.getInTime() == null &&
            attendanceTimeSheetDTO.getOutTime() == null &&
            !attendanceTimeSheetDTO.isHasPendingMovementEntry() &&
            !attendanceTimeSheetDTO.isHasPendingLeaveApplication() &&
            !attendanceTimeSheetDTO.isHasPendingManualAttendance()
        ) {
            attendanceTimeSheetDTO.setCanApplyMovementEntry(true);
        }
    }

    public AttendanceTimeSheetDTO findAnyApplicationsBetweenDateRange(String pin, LocalDate startDate, LocalDate endDate) {
        AttendanceTimeSheetDTO attendanceTimeSheetDTO = new AttendanceTimeSheetDTO();

        /* find pending applications */
        // note: java is reference based no need to reassign
        this.findPendingApplication(pin, attendanceTimeSheetDTO, startDate, endDate);

        /* find approved attendance application */
        // note: java is reference based no need to reassign
        this.findAttendanceStatusBetweenDateRange(pin, attendanceTimeSheetDTO, startDate, endDate);

        return attendanceTimeSheetDTO;
    }

    private void findAttendanceStatusBetweenDateRange(
        String pin,
        AttendanceTimeSheetDTO attendanceTimeSheetDTO,
        LocalDate startDate,
        LocalDate endDate
    ) {
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.getAttendanceEntryByEmployeePinAndBetweenTwoDates(
            pin,
            startDate,
            endDate
        );
        if (attendanceEntryList.size() > 0) {
            attendanceTimeSheetDTO.setHasPresentStatus(true);
            attendanceTimeSheetDTO.setInTime(attendanceEntryList.get(0).getInTime());
            attendanceTimeSheetDTO.setOutTime(attendanceEntryList.get(0).getOutTime());
        } else {
            attendanceTimeSheetDTO.setHasPresentStatus(false);
        }

        List<MovementEntry> movementEntryList = movementEntryRepository.getApprovedMovementEntriesByEmployeePinBetweenTwoDates(
            pin,
            startDate,
            endDate
        );
        if (movementEntryList.size() > 0) {
            attendanceTimeSheetDTO.setHasMovementStatus(true);
        } else {
            attendanceTimeSheetDTO.setHasMovementStatus(false);
        }

        List<LeaveApplication> applicationList = leaveApplicationRepository.getApprovedLeaveApplicationsByEmployeePinBetweenTwoDates(
            pin,
            startDate,
            endDate
        );
        if (applicationList.size() > 0) {
            attendanceTimeSheetDTO.setHasLeaveStatus(true);
        } else {
            attendanceTimeSheetDTO.setHasLeaveStatus(false);
        }
    }

    private boolean isPresent(AttendanceStatus attendanceStatus) {
        return (
            attendanceStatus != null &&
            (
                attendanceStatus.equals(AttendanceStatus.PRESENT) ||
                attendanceStatus == AttendanceStatus.PRESENT_GOVT_HOLIDAY ||
                attendanceStatus == AttendanceStatus.PRESENT_WEEKLY_OFFDAY ||
                attendanceStatus == AttendanceStatus.MOVEMENT
            )
        );
    }

    private boolean isLeave(AttendanceStatus attendanceStatus) {
        return (
            attendanceStatus != null &&
            (
                (attendanceStatus == AttendanceStatus.LEAVE) ||
                (attendanceStatus == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE) ||
                (attendanceStatus == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE) ||
                (attendanceStatus == AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE) ||
                (attendanceStatus == AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE) ||
                (attendanceStatus == AttendanceStatus.NON_MENTIONABLE_MATERNITY_LEAVE) ||
                (attendanceStatus == AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE) ||
                (attendanceStatus == AttendanceStatus.LEAVE_WITHOUT_PAY)
            )
        );
    }
}
