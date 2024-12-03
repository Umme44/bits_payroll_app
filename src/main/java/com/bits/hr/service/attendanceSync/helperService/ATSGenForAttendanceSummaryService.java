package com.bits.hr.service.attendanceSync.helperService;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.*;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetMini;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetMiniService;
import com.bits.hr.service.attendanceTimeSheet.MonthlyAttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.MonthlyAttendanceTimeSheetGenerationService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ATSGenForAttendanceSummaryService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MonthlyAttendanceTimeSheetGenerationService monthlyAttendanceTimeSheetGenerationService;

    @Autowired
    private AttendanceSummaryRepository attendanceSummaryRepository;

    @Autowired
    private HolidaysRepository holidaysRepository;

    @Autowired
    private AttendanceTimeSheetMiniService attendanceTimeSheetMiniService;

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    private MovementEntryRepository movementEntryRepository;

    public List<MonthlyAttendanceTimeSheetDTO> getATS(LocalDate startDate, LocalDate endDate) {
        List<Employee> employeeList = employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(startDate, endDate);
        List<MonthlyAttendanceTimeSheetDTO> monthlyAttendanceTimeSheetDTOList = new ArrayList<>();
        List<LocalDate> dateList = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            dateList.add(date);
        }
        List<Holidays> HolidaysList = holidaysRepository.findHolidaysStartDateBetweenDates(
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );
        HashSet<LocalDate> holidaysHashset = new HashSet<>();
        for (Holidays holidays : HolidaysList) {
            if (holidays.getStartDate() == holidays.getEndDate()) {
                holidaysHashset.add(holidays.getStartDate());
            } else {
                for (LocalDate date = holidays.getStartDate(); date.isBefore(holidays.getEndDate().plusDays(1)); date = date.plusDays(1)) {
                    holidaysHashset.add(date);
                }
            }
        }
        for (Employee employee : employeeList) {
            monthlyAttendanceTimeSheetDTOList.add(getPerEmpATS(employee, dateList, holidaysHashset));
        }
        return monthlyAttendanceTimeSheetDTOList;
    }

    public List<MonthlyAttendanceTimeSheetDTO> getATS(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
        return getATS(startDate, endDate);
    }

    public MonthlyAttendanceTimeSheetDTO getPerEmpATS(Employee employee, List<LocalDate> dateList, HashSet<LocalDate> holidaysHashset) {
        MonthlyAttendanceTimeSheetDTO monthlyAttendanceTimeSheetDTO = new MonthlyAttendanceTimeSheetDTO();
        HashSet<LocalDate> attendanceEntryHashSet = attendanceEntryRepository.getAttendanceEntryByEmployeeIdAndBetweenTwoDates(
            employee.getId(),
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );

        // *** hashtable for movement Entry
        List<MovementEntry> movementEntryList = movementEntryRepository.getApprovedMovementEntriesByEmployeePinBetweenTwoDates(
            employee.getPin(),
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );
        HashSet<LocalDate> movementEntryHashSet = new HashSet<>();
        for (MovementEntry movementEntry : movementEntryList) {
            // single date , single key , no object duplication
            if (movementEntry.getStartDate().equals(movementEntry.getEndDate())) {
                movementEntryHashSet.add(movementEntry.getStartDate());
            }
            // if range , Key => range 0-n , same object
            else {
                for (
                    LocalDate date = movementEntry.getStartDate();
                    date.isBefore(movementEntry.getEndDate().plusDays(1));
                    date = date.plusDays(1)
                ) {
                    movementEntryHashSet.add(date);
                }
            }
        }

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getApprovedLeaveApplicationsByEmployeeIdBetweenTwoDates(
            employee.getId(),
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );
        HashSet<LocalDate> leaveApplicationHashSet = new HashSet<>();
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            if (leaveApplication.getStartDate().equals(leaveApplication.getEndDate())) {
                leaveApplicationHashSet.add(leaveApplication.getStartDate());
            } else {
                for (
                    LocalDate date = leaveApplication.getStartDate();
                    date.isBefore(leaveApplication.getEndDate().plusDays(1));
                    date = date.plusDays(1)
                ) {
                    leaveApplicationHashSet.add(date);
                }
            }
        }

        //==============================================================================================================================================
        // if DOJ is after start date && DOR is before end date ==> regenerate date list;
        LocalDate startDate = dateList.get(0);
        LocalDate endDate = dateList.get(dateList.size() - 1);

        LocalDate newStartDate = startDate;
        LocalDate newEndDate = endDate;

        if (employee.getDateOfJoining().isAfter(startDate)) newStartDate = employee.getDateOfJoining();
        // Month = resigning month and payable gross != mainGross // not full month //
        List<EmployeeResignation> employeeResignationList = employeeResignationRepository
            .findEmployeeResignationByEmployeeId(employee.getId())
            .stream()
            .filter(x -> x.getLastWorkingDay() != null)
            .filter(x -> x.getApprovalStatus() == Status.APPROVED)
            .collect(Collectors.toList());
        if (!employeeResignationList.isEmpty()) {
            LocalDate resignationDate = employeeResignationList.get(0).getLastWorkingDay();
            if (resignationDate.isBefore(endDate)) {
                newEndDate = resignationDate;
            }
        }

        if (!startDate.equals(newStartDate) && !endDate.equals(newEndDate)) {
            dateList.clear();
            for (LocalDate date = newStartDate; date.isBefore(newEndDate.plusDays(1)); date = date.plusDays(1)) {
                dateList.add(date);
            }
        }
        //==============================================================================================================================================

        List<AttendanceTimeSheetMini> attendanceTimeSheetMiniList = attendanceTimeSheetMiniService.getAttendanceTimeSheet(
            dateList,
            holidaysHashset,
            leaveApplicationHashSet,
            attendanceEntryHashSet,
            movementEntryHashSet
        );
        monthlyAttendanceTimeSheetDTO.setEmployeeId(employee.getId());
        monthlyAttendanceTimeSheetDTO.setPin(employee.getPin());
        monthlyAttendanceTimeSheetDTO.setName(employee.getFullName());
        monthlyAttendanceTimeSheetDTO.setAttendanceTimeSheetMiniList(attendanceTimeSheetMiniList);
        return monthlyAttendanceTimeSheetDTO;
    }
}
