package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.HolidayType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.*;
import com.bits.hr.service.EmployeeResignationService;
import com.bits.hr.service.FlexScheduleService;
import com.bits.hr.service.dto.DateRangeDTO;
import com.bits.hr.util.ConstantUtil;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.MathRoundUtil;
import java.lang.Math;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@Deprecated
public class AttendanceTimeSheetService {

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
    private FlexScheduleService flexScheduleService;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    private EmployeeResignationService employeeResignationService;

    //todo: flag-> minimal, full for ats data processing
    // todo minimal -> only process ats data
    // todo: full -> ats + applications processing

    // get list of attendance by pin in a hashtable
    // key will be LocalDate , one employee will have one unique date in attendance Table
    // now mapping will be efficient as hashtable data can be retrieved at O(1) complexity
    public List<AttendanceTimeSheetDTO> getAttendanceTimeSheet(String pin, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = new ArrayList<>();

        Optional<Employee> employeeOptional = employeeRepository.findByPin(pin);
        if (!employeeOptional.isPresent()) {
            log.error("Employee Not Found By PIN:" + pin);
            throw new BadRequestAlertException("Employee Not Found By PIN:" + pin, "employee", "noEmployeeFound");
        }

        Employee employee = employeeOptional.get();

        // *** hashtable for attendance
        // data retrieved between date range too for memory optimization purposes
        List<AttendanceEntry> attendanceEntryList = attendanceEntryRepository.getAttendanceEntryByEmployeePinAndBetweenTwoDates(
            pin,
            startDate,
            endDate
        );
        Hashtable<LocalDate, AttendanceEntry> attendanceEntryHashtable = new Hashtable<>();
        for (AttendanceEntry attendanceEntry : attendanceEntryList) {
            /* do not consider the seconds, due to duration rounding issue */

            if (attendanceEntry.getInNote() != null) {
                int inTimeSecond = attendanceEntry.getInTime().atZone(ZoneId.systemDefault()).getSecond();
                attendanceEntry.setInTime((attendanceEntry.getInTime()).minusSeconds(inTimeSecond));
            }

            if (attendanceEntry.getOutTime() != null) {
                int outTimeSecond = attendanceEntry.getOutTime().atZone(ZoneId.systemDefault()).getSecond();
                attendanceEntry.setOutTime((attendanceEntry.getOutTime()).minusSeconds(outTimeSecond));
            }

            attendanceEntryHashtable.put(attendanceEntry.getDate(), attendanceEntry);
        }

        // *** hashtable for LeaveApplication
        // data retrieved between date range too for memory optimization purposes
        // unapproved leave will not be taken
        List<LeaveApplication> leaveApplicationListList = leaveApplicationRepository.getApprovedLeaveApplicationsByEmployeePinBetweenTwoDates(
            pin,
            startDate,
            endDate
        );
        Hashtable<LocalDate, LeaveApplication> leaveApplicationHashtable = new Hashtable<>();
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

        // *** hashtable for movementEntry
        // data retrieved between date range too for memory optimization purposes
        // unapproved leave will not be taken
        List<MovementEntry> movementEntryList = movementEntryRepository.getApprovedMovementEntriesByEmployeePinBetweenTwoDates(
            pin,
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

        // *** hashtable for holidays
        // data retrieved between date range too for memory optimization purposes
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

        // get service tenure for BLANK status checking
        DateRangeDTO dateRangeDTO = getServiceTenureStartAndEndDate(employee);
        LocalDate serviceTenureStartDate = dateRangeDTO.getStartDate();
        LocalDate serviceTenureEndDate = dateRangeDTO.getEndDate();

        // main loop between start date and end date
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            AttendanceTimeSheetDTO attendanceTimeSheetDTO = new AttendanceTimeSheetDTO();

            attendanceTimeSheetDTO.setDate(date);

            // first check , if present
            // if contains date key in attendanceEntryHashtable,  present , calculate according to logic
            // calculate if gained Compensatory leave at the end

            /* More than 4 Hr work in holiday = 1 compulsory leave gain
             *Compensatory Leave can’t be carried forward to the next year.
             */
            //blank in effective status
            if (
                (serviceTenureStartDate != null && date.isBefore(serviceTenureStartDate)) ||
                (serviceTenureEndDate != null && date.isAfter(serviceTenureEndDate))
            ) {
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.BLANK_INEFFECTIVE);
            } else if (
                attendanceEntryHashtable.containsKey(date) &&
                (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY)
            ) {
                AttendanceEntry attendanceEntry = attendanceEntryHashtable.get(date);
                attendanceTimeSheetDTO.setInTime(attendanceEntry.getInTime());
                attendanceTimeSheetDTO.setOutTime(attendanceEntry.getOutTime());
                attendanceTimeSheetDTO.setInNote(attendanceEntry.getInNote());
                attendanceTimeSheetDTO.setOutNote(attendanceEntry.getOutNote());
                attendanceTimeSheetDTO.setStatus(attendanceEntry.getStatus());
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

                //Total working Hour Calculation || not calculating if not out yet (out time = = null)
                if (attendanceEntry.getInTime() != null && attendanceEntry.getOutTime() != null) {
                    long totalWorkingMinutes = ChronoUnit.MINUTES.between(attendanceEntry.getInTime(), attendanceEntry.getOutTime());
                    double totalWorkingHour = MathRoundUtil.roundUpToTwoDecimal(
                        Math.floor((double) totalWorkingMinutes / 60d) + (((int) totalWorkingMinutes % 60d) / 100)
                    );
                    attendanceTimeSheetDTO.setTotalWorkingHour(totalWorkingHour);
                    if (totalWorkingHour < flexScheduleService.getOfficeTimeDurationByPin(pin)) {
                        attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.NON_FULFILLED_OFFICE_HOURS);
                    }
                }

                // late duration calculation
                if (attendanceEntry.getInTime() != null) {
                    LocalDateTime startTime = flexScheduleService.getStartTimeByPin(pin, date);
                    int startTimeInMinutes = (startTime.getHour() * 60) + startTime.getMinute();
                    // startTimeInMinutes += 360;
                    // convert instant to LocalDateTime with zero offset (+0)
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
                        //todo: code refactor needed for more readable code
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
                if (
                    holidaysHashtable.containsKey(date) ||
                    date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.FRIDAY
                ) {
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
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.MOVEMENT);
            }
            // Holiday check
            else if (holidaysHashtable.containsKey(date)) {
                Holidays holidays = holidaysHashtable.get(date);
                if (holidays.getHolidayType() == HolidayType.Govt) {
                    attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.GOVT_HOLIDAY);
                } else if (holidays.getHolidayType() == HolidayType.General) {
                    attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.GENERAL_HOLIDAY);
                }
            }
            // weekly OFFDAY check
            else if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.WEEKLY_OFFDAY);
            }
            // fourth check , if in Leave
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

            // find any pending
            /* 1. Leave Application
               2. Manual Attendance
               3. Movement Entry
             */
            // Warning: function order is important here //
            // order 1.
            attendanceTimeSheetDTO = findPendingApplication(pin, attendanceTimeSheetDTO, date, date);
            /* find editable AttendanceStatus in attendanceTimeSheet */
            // order 2.
            attendanceTimeSheetDTO = checkIsEditable(attendanceTimeSheetDTO, date);

            attendanceTimeSheetDTOList.add(attendanceTimeSheetDTO);
        }
        Collections.reverse(attendanceTimeSheetDTOList);
        return attendanceTimeSheetDTOList;
    }

    private boolean isFutureDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            return true;
        } else {
            return false;
        }
    }

    private DateRangeDTO getServiceTenureStartAndEndDate(Employee employee) {
        DateRangeDTO dateRangeDTO = new DateRangeDTO();

        if (employee.getDateOfJoining() == null) {
            throw new RuntimeException("Date of joining is missing");
        }
        LocalDate doj = employee.getDateOfJoining();
        Optional<LocalDate> lwd = employeeResignationService.getLastWorkingDay(employee.getId());
        LocalDate contractPeriodEndDate = employee.getContractPeriodEndDate();
        LocalDate contractPeriodExtendedTo = employee.getContractPeriodExtendedTo();

        dateRangeDTO.setStartDate(doj);

        boolean isFixedTermContact = employee.getIsFixedTermContract() != null ? employee.getIsFixedTermContract() : false;

        if (employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE && isFixedTermContact) {
            if (contractPeriodEndDate != null) {
                dateRangeDTO.setEndDate(contractPeriodEndDate);
            }
            if (contractPeriodExtendedTo != null) {
                dateRangeDTO.setEndDate(contractPeriodEndDate);
            }
        }

        if (lwd.isPresent()) {
            dateRangeDTO.setEndDate(lwd.get());
        }

        return dateRangeDTO;
    }

    private AttendanceTimeSheetDTO findPendingApplication(
        String pin,
        AttendanceTimeSheetDTO attendanceTimeSheetDTO,
        LocalDate startDate,
        LocalDate endDate
    ) {
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

        return attendanceTimeSheetDTO;
    }

    private AttendanceTimeSheetDTO checkIsEditable(AttendanceTimeSheetDTO attendanceTimeSheetDTO, LocalDate selectedDate) {
        if (attendanceTimeSheetDTO.getAttendanceStatus().equals(AttendanceStatus.BLANK_INEFFECTIVE)) {
            attendanceTimeSheetDTO.setCanApplyAttendanceEntry(false);
            attendanceTimeSheetDTO.setCanApplyMovementEntry(false);
            attendanceTimeSheetDTO.setCanApplyLeaveApplication(false);
            attendanceTimeSheetDTO.setCanEditAttendanceEntry(false);
            return attendanceTimeSheetDTO;
        }

        LocalDate today = LocalDate.now();
        LocalDate monthFromToday = today.minusDays(30);

        /* last 30 days attendance application is allowed */
        if (
            (selectedDate.isEqual(monthFromToday) || selectedDate.isAfter(monthFromToday)) &&
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

        /* last 30 days and future date is allowed (Movement Entry, Leave Application) */
        if (selectedDate.isEqual(monthFromToday) || selectedDate.isAfter(monthFromToday)) {
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
        return attendanceTimeSheetDTO;
    }

    /**
     * Special for Admin ATS page
     *
     * @param attendanceTimeSheetDTOList
     * @return
     */
    public List<AttendanceTimeSheetDTO> checkIsEditableAdmin(List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList) {
        for (int i = 0; i < attendanceTimeSheetDTOList.size(); i++) {
            final AttendanceTimeSheetDTO attendanceTimeSheetDTO = attendanceTimeSheetDTOList.get(i);

            /* admin can modify all dates attendance application except blank */
            if (attendanceTimeSheetDTO.getAttendanceStatus().equals(AttendanceStatus.BLANK_INEFFECTIVE)) {
                attendanceTimeSheetDTO.setCanApplyAttendanceEntry(false);
                attendanceTimeSheetDTO.setCanApplyMovementEntry(false);
                attendanceTimeSheetDTO.setCanApplyLeaveApplication(false);
                attendanceTimeSheetDTO.setCanEditAttendanceEntry(false);
                continue;
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

        return attendanceTimeSheetDTOList;
    }

    public AttendanceTimeSheetDTO findAnyApplicationsBetweenDateRange(String pin, LocalDate startDate, LocalDate endDate) {
        AttendanceTimeSheetDTO attendanceTimeSheetDTO = new AttendanceTimeSheetDTO();

        /* find pending applications */
        attendanceTimeSheetDTO = this.findPendingApplication(pin, attendanceTimeSheetDTO, startDate, endDate);

        /* find approved attendance application */
        attendanceTimeSheetDTO = findAttendanceStatusBetweenDateRange(pin, attendanceTimeSheetDTO, startDate, endDate);

        return attendanceTimeSheetDTO;
    }

    private AttendanceTimeSheetDTO findAttendanceStatusBetweenDateRange(
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
        return attendanceTimeSheetDTO;
    }

    private boolean isPresent(AttendanceStatus attendanceStatus) {
        if (
            attendanceStatus != null &&
            attendanceStatus.equals(AttendanceStatus.PRESENT) ||
            attendanceStatus == AttendanceStatus.PRESENT_GOVT_HOLIDAY ||
            attendanceStatus == AttendanceStatus.PRESENT_WEEKLY_OFFDAY ||
            attendanceStatus == attendanceStatus.MOVEMENT
        ) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLeave(AttendanceStatus attendanceStatus) {
        if (
            attendanceStatus != null &&
            attendanceStatus == AttendanceStatus.LEAVE ||
            attendanceStatus == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE ||
            attendanceStatus == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE ||
            attendanceStatus == AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE ||
            attendanceStatus == AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE ||
            attendanceStatus == AttendanceStatus.NON_MENTIONABLE_MATERNITY_LEAVE ||
            attendanceStatus == AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE
        ) {
            return true;
        } else {
            return false;
        }
    }
}
