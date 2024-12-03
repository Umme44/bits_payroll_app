package com.bits.hr.service.impl;

import static com.bits.hr.config.Constants.DAYS_IN_WEEK;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Holidays;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.HolidayType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.HolidaysRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.MyTeamService;
import com.bits.hr.service.WeekendService;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.dto.MyTeamMemberAttendancesDTO;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class MyTeamServiceImpl implements MyTeamService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMinimalMapper employeeMinimalMapper;
    private final AttendanceTimeSheetServiceV2 attendanceTimeSheetService;
    private final WeekendService weekendService;
    private final HolidaysRepository holidaysRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final EmployeeService employeeService;

    public MyTeamServiceImpl(
        EmployeeRepository employeeRepository,
        EmployeeMinimalMapper employeeMinimalMapper,
        AttendanceTimeSheetServiceV2 attendanceTimeSheetService,
        WeekendService weekendService,
        HolidaysRepository holidaysRepository,
        LeaveApplicationRepository leaveApplicationRepository,
        EmployeeService employeeService
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeMinimalMapper = employeeMinimalMapper;
        this.attendanceTimeSheetService = attendanceTimeSheetService;
        this.weekendService = weekendService;
        this.holidaysRepository = holidaysRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.employeeService = employeeService;
    }

    @Override
    public List<MyTeamMemberAttendancesDTO> getMyTeamAttendance(Employee currentEmployee) {
        List<MyTeamMemberAttendancesDTO> teamAttendance = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(DAYS_IN_WEEK - 1);
        List<Employee> team = employeeService.getAllReportingTo(currentEmployee);
        for (Employee employee : team) {
            List<AttendanceTimeSheetDTO> attendanceList = attendanceTimeSheetService.getAttendanceTimeSheet(
                employee.getId(),
                startDate,
                today,
                AtsDataProcessLevel.FULL_FEATURED_USER
            );

            MyTeamMemberAttendancesDTO myTeamEmployee = new MyTeamMemberAttendancesDTO();
            myTeamEmployee.setEmployee(employeeMinimalMapper.toDto(employee));

            attendanceList.addAll(getForFuture(employee.getId(), today.plusDays(1), today.plusDays(DAYS_IN_WEEK)));
            attendanceList.sort(Comparator.comparing(AttendanceTimeSheetDTO::getDate));
            myTeamEmployee.setAttendances(attendanceList);

            teamAttendance.add(myTeamEmployee);
            //teamAttendance.addAll(getMyTeamAttendance(employee));
        }

        return teamAttendance;
    }

    private List<AttendanceTimeSheetDTO> getForFuture(long employeeId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = new ArrayList<>();

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getApprovedLeaveApplicationsByEmployeeIdBetweenTwoDates(
            employeeId,
            startDate,
            endDate
        );
        Map<LocalDate, LeaveApplication> dateLeaveApplicationMap = new HashMap<>();
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            for (
                LocalDate date = leaveApplication.getStartDate();
                date.compareTo(leaveApplication.getEndDate()) <= 0;
                date = date.plusDays(1)
            ) {
                dateLeaveApplicationMap.put(date, leaveApplication);
            }
        }

        for (LocalDate date = startDate; date.compareTo(endDate) <= 0; date = date.plusDays(1)) {
            AttendanceTimeSheetDTO attendanceTimeSheetDTO = new AttendanceTimeSheetDTO();
            attendanceTimeSheetDTO.setDate(date);

            List<Holidays> holidaysInDate = holidaysRepository.findAllHolidaysBetweenDates(date, date);

            if (weekendService.checkWeekendByLocalDate(date)) {
                attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.WEEKLY_OFFDAY);
            } else if (holidaysInDate.size() > 0) {
                Holidays holidays = holidaysInDate.get(0);
                attendanceTimeSheetDTO.setAttendanceStatus(
                    holidays.getHolidayType() == HolidayType.Govt ? AttendanceStatus.GOVT_HOLIDAY : AttendanceStatus.SPECIAL_HOLIDAY
                );
            } else if (dateLeaveApplicationMap.containsKey(date)) {
                LeaveApplication application = dateLeaveApplicationMap.get(date);
                switch (application.getLeaveType()) {
                    case MENTIONABLE_ANNUAL_LEAVE:
                        attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE);
                        break;
                    case MENTIONABLE_CASUAL_LEAVE:
                        attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.MENTIONABLE_CASUAL_LEAVE);
                        break;
                    case NON_MENTIONABLE_PANDEMIC_LEAVE:
                        attendanceTimeSheetDTO.setAttendanceStatus(AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE);
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
            attendanceTimeSheetDTOList.add(attendanceTimeSheetDTO);
        }

        return attendanceTimeSheetDTOList;
    }

    @Override
    public boolean isMyTeamMember(Employee lineManager, long teamMemberId) {
        List<Employee> employeeList = employeeService.getAllReportingTo(lineManager);
        for (Employee employee : employeeList) {
            if (employee.getId().equals(teamMemberId)) {
                return true;
            }
        }
        return false;
    }
}
