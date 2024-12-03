package com.bits.hr.service.attendanceSync;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.DTO.LeaveBalanceDetailViewDTO;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.LeaveBalanceDetailViewService;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AutoLeaveCuttingService {

    private final GetConfigValueByKeyService getConfigValueByKeyService;
    private final LeaveBalanceDetailViewService leaveBalanceDetailViewService;
    private final LeaveApplicationService leaveApplicationService;
    private final EmployeeRepository employeeRepository;
    private final AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    public AutoLeaveCuttingService(
        GetConfigValueByKeyService getConfigValueByKeyService,
        LeaveBalanceDetailViewService leaveBalanceDetailViewService,
        LeaveApplicationService leaveApplicationService,
        EmployeeRepository employeeRepository,
        AttendanceTimeSheetServiceV2 attendanceTimeSheetService
    ) {
        this.getConfigValueByKeyService = getConfigValueByKeyService;
        this.leaveBalanceDetailViewService = leaveBalanceDetailViewService;
        this.leaveApplicationService = leaveApplicationService;
        this.employeeRepository = employeeRepository;
        this.attendanceTimeSheetService = attendanceTimeSheetService;
    }

    public boolean cutAutoLeave(LocalDate startDate, LocalDate endDate) {
        try {
            List<LeaveApplicationDTO> leaveApplicationDTOList = new ArrayList<>();

            // pseudocode
            // get eligible employee for salary generation
            // per employee get ATS
            // get absent days from ats
            // if there are casual leave balance ,
            //      already taken n number of casual leave
            //      cut first (2-n) leave of from casual leave balance
            // if there are annual leave balance available cut other absent days from casual leave

            List<Employee> employeeList = employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(
                LocalDate.of(endDate.getYear(), endDate.getMonth(), 1),
                LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.lengthOfMonth())
            );

            for (Employee employee : employeeList) {
                List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
                    employee.getId(),
                    startDate,
                    endDate,
                    AtsDataProcessLevel.FULL_FEATURED_USER
                );

                List<AttendanceTimeSheetDTO> absent = attendanceTimeSheetDTOList
                    .stream()
                    .filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT)
                    .collect(Collectors.toList());
                Collections.reverse(absent);

                // get this month ATS and previous month ATS if start date and end date are in different months.
                for (AttendanceTimeSheetDTO leaveBalanceDetailViewDTO : absent) {
                    leaveCut(employee, leaveBalanceDetailViewDTO.getDate());
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + ex.getStackTrace());
            return false;
        }
    }

    boolean leaveCut(Employee employee, LocalDate day) {
        try {
            LocalDate monthStart = LocalDate.of(day.getYear(), day.getMonth(), 1);
            LocalDate monthEnd = LocalDate.of(day.getYear(), day.getMonth(), day.lengthOfMonth());
            List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
                employee.getId(),
                monthStart,
                monthEnd,
                AtsDataProcessLevel.FULL_FEATURED_USER
            );

            List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService.getYearlyProcessedLeaveBalance(
                day.getYear(),
                employee.getId()
            );

            int casualLeaveBalance = 0;
            int casualMaxLeaveBalanceAllowed = getConfigValueByKeyService.getCasualLeaveLimitPerMonth();
            int casualLeaveTakenThisMonth = attendanceTimeSheetDTOList
                .stream()
                .filter(x -> x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE)
                .collect(Collectors.toList())
                .size();

            int annualLeaveBalance = 0;
            int annualMaxLeaveBalanceAllowed = getConfigValueByKeyService.getAnnualLeaveLimitPerMonth();
            int annualLeaveTakenThisMonth = attendanceTimeSheetDTOList
                .stream()
                .filter(x -> x.getAttendanceStatus() == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE)
                .collect(Collectors.toList())
                .size();

            if (
                leaveBalanceDetailViewDTOList
                    .stream()
                    .filter(x -> x.getLeaveType() == LeaveType.MENTIONABLE_CASUAL_LEAVE)
                    .collect(Collectors.toList())
                    .size() >
                0
            ) {
                casualLeaveBalance =
                    leaveBalanceDetailViewDTOList
                        .stream()
                        .filter(x -> x.getLeaveType() == LeaveType.MENTIONABLE_CASUAL_LEAVE)
                        .collect(Collectors.toList())
                        .get(0)
                        .getDaysRemaining();
            }

            if (
                leaveBalanceDetailViewDTOList
                    .stream()
                    .filter(x -> x.getLeaveType() == LeaveType.MENTIONABLE_ANNUAL_LEAVE)
                    .collect(Collectors.toList())
                    .size() >
                0
            ) {
                annualLeaveBalance =
                    leaveBalanceDetailViewDTOList
                        .stream()
                        .filter(x -> x.getLeaveType() == LeaveType.MENTIONABLE_ANNUAL_LEAVE)
                        .collect(Collectors.toList())
                        .get(0)
                        .getDaysRemaining();
            }

            int casualLeaveToCut = Math.min(casualMaxLeaveBalanceAllowed - casualLeaveTakenThisMonth, casualLeaveBalance);
            int annualLeaveToCut = Math.min(annualMaxLeaveBalanceAllowed - annualLeaveTakenThisMonth, annualLeaveBalance);

            if (casualLeaveToCut > 0) {
                LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
                leaveApplicationDTO.setEmployeeId(employee.getId());
                leaveApplicationDTO.setLeaveType(LeaveType.MENTIONABLE_CASUAL_LEAVE);
                leaveApplicationDTO.setDurationInDay(1);
                leaveApplicationDTO.setApplicationDate(LocalDate.now());
                leaveApplicationDTO.setIsHRApproved(true);
                leaveApplicationDTO.setIsLineManagerApproved(false);
                leaveApplicationDTO.setStartDate(day);
                leaveApplicationDTO.setEndDate(day);
                leaveApplicationDTO.setReason("System Applied");
                leaveApplicationService.save(leaveApplicationDTO);
                return true;
            }
            if (annualLeaveToCut > 0) {
                LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
                leaveApplicationDTO.setEmployeeId(employee.getId());
                leaveApplicationDTO.setLeaveType(LeaveType.MENTIONABLE_ANNUAL_LEAVE);
                leaveApplicationDTO.setDurationInDay(1);
                leaveApplicationDTO.setApplicationDate(LocalDate.now());
                leaveApplicationDTO.setIsHRApproved(true);
                leaveApplicationDTO.setIsLineManagerApproved(false);
                leaveApplicationDTO.setStartDate(day);
                leaveApplicationDTO.setEndDate(day);
                leaveApplicationDTO.setReason("System Applied");
                leaveApplicationService.save(leaveApplicationDTO);
                return true;
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage() + ex.getStackTrace());
            return false;
        }
    }
}
