package com.bits.hr.service.attendanceSync;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmployeeResignationRepository;
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

@Service
@Log4j2
public class AutoLeaveCuttingSummaryService {

    private final GetConfigValueByKeyService getConfigValueByKeyService;
    private final LeaveBalanceDetailViewService leaveBalanceDetailViewService;
    private final EmployeeRepository employeeRepository;
    private final AttendanceTimeSheetServiceV2 attendanceTimeSheetService;
    private final EmployeeResignationRepository employeeResignationRepository;

    public AutoLeaveCuttingSummaryService(
        GetConfigValueByKeyService getConfigValueByKeyService,
        LeaveBalanceDetailViewService leaveBalanceDetailViewService,
        EmployeeRepository employeeRepository,
        AttendanceTimeSheetServiceV2 attendanceTimeSheetService,
        EmployeeResignationRepository employeeResignationRepository
    ) {
        this.getConfigValueByKeyService = getConfigValueByKeyService;
        this.leaveBalanceDetailViewService = leaveBalanceDetailViewService;
        this.employeeRepository = employeeRepository;
        this.attendanceTimeSheetService = attendanceTimeSheetService;
        this.employeeResignationRepository = employeeResignationRepository;
    }

    public List<LeaveApplicationDTO> GenerateSummary(LocalDate startDate, LocalDate endDate) {
        // modify start date and end date according to today , dor => employee specific , doj => employee specific

        List<LeaveApplicationDTO> result = new ArrayList<>();
        if (endDate.isBefore(LocalDate.now())) {
            endDate = LocalDate.now();
        }
        try {
            // pseudocode
            // get eligible employee for salary generation
            // per employee get ATS
            // get absent days from ats
            // if there are casual leave balance ,
            //      already taken n number of casual leave
            //      cut first (2-n) leave of from casual leave balance
            // if there are annual leave balance available cut other absent days from casual leave

            List<Employee> employeeList = employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(
                LocalDate.of(startDate.getYear(), startDate.getMonth(), 1),
                LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.lengthOfMonth())
            );

            // taking max amount per month configuration
            int casualLeaveLimit = getConfigValueByKeyService.getCasualLeaveLimitPerMonth();
            int annualLeaveLimit = getConfigValueByKeyService.getAnnualLeaveLimitPerMonth();

            for (Employee employee : employeeList) {
                LocalDate effectiveStartDate = startDate;
                LocalDate effectiveEndDate = endDate;

                if (employee.getDateOfJoining() != null && effectiveStartDate.isBefore(employee.getDateOfJoining())) {
                    effectiveStartDate = employee.getDateOfJoining();
                }

                // effective end date should not cross last working day
                boolean isResigned = false;
                List<EmployeeResignation> employeeResignations = employeeResignationRepository.findApprovedEmployeeResignationByEmployeeId(
                    employee.getId()
                );
                if (employeeResignations.size() > 0) {
                    isResigned = true;
                    EmployeeResignation employeeResignation = employeeResignations.get(0);
                    if (employeeResignation.getLastWorkingDay() != null) {
                        if (effectiveEndDate.isAfter(employeeResignation.getLastWorkingDay())) {
                            effectiveEndDate = employeeResignation.getLastWorkingDay();
                        }
                    }
                }

                // for contractual employee
                // effective end date should not cross date of contact end

                if (employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
                    if (employee.getContractPeriodEndDate() != null && employee.getContractPeriodExtendedTo() != null) {
                        if (effectiveEndDate.isAfter(employee.getContractPeriodExtendedTo())) {
                            effectiveEndDate = employee.getContractPeriodExtendedTo();
                        }
                    } else if (employee.getContractPeriodEndDate() != null) {
                        if (effectiveEndDate.isAfter(employee.getContractPeriodEndDate())) {
                            effectiveEndDate = employee.getContractPeriodEndDate();
                        }
                    }
                }

                // taking leave balance
                List<LeaveBalanceDetailViewDTO> leaveBalanceDetailViewDTOList = leaveBalanceDetailViewService.getYearlyProcessedLeaveBalance(
                    effectiveStartDate.getYear(),
                    employee.getId()
                );

                int casualLeaveBalance = 0;
                int annualLeaveBalance = 0;
                for (LeaveBalanceDetailViewDTO leaveBalanceDetailViewDTO : leaveBalanceDetailViewDTOList) {
                    if (leaveBalanceDetailViewDTO.getLeaveType().equals(LeaveType.MENTIONABLE_ANNUAL_LEAVE)) {
                        annualLeaveBalance = leaveBalanceDetailViewDTO.getDaysRemaining();
                    }
                    if (leaveBalanceDetailViewDTO.getLeaveType().equals(LeaveType.MENTIONABLE_CASUAL_LEAVE)) {
                        casualLeaveBalance = leaveBalanceDetailViewDTO.getDaysRemaining();
                    }
                }
                if (casualLeaveBalance == 0 && annualLeaveBalance == 0) {
                    continue;
                }
                // split into ATS list of list if start date and end date are in different month
                ArrayList<ArrayList<AttendanceTimeSheetDTO>> atsDTOListOfList = new ArrayList<ArrayList<AttendanceTimeSheetDTO>>();

                for (int i = 0; i < 5; i++) {
                    if (effectiveStartDate.getMonth().equals(effectiveEndDate.getMonth())) {
                        List<AttendanceTimeSheetDTO> list = attendanceTimeSheetService.getAttendanceTimeSheet(
                            employee.getId(),
                            effectiveStartDate,
                            effectiveEndDate,
                            AtsDataProcessLevel.FULL_FEATURED_USER
                        );
                        atsDTOListOfList.add(new ArrayList<>(list));
                        break;
                    } else {
                        LocalDate tmpStartDate = effectiveStartDate;
                        LocalDate tmpEndDate = effectiveEndDate;
                        while (tmpStartDate.isBefore(tmpEndDate)) {
                            LocalDate rangeEnd = LocalDate.of(
                                tmpStartDate.getYear(),
                                tmpStartDate.getMonth(),
                                tmpStartDate.lengthOfMonth()
                            );

                            if (rangeEnd.isAfter(effectiveEndDate)) {
                                rangeEnd = effectiveEndDate;
                            }
                            List<AttendanceTimeSheetDTO> list = attendanceTimeSheetService.getAttendanceTimeSheet(
                                employee.getId(),
                                tmpStartDate,
                                rangeEnd,
                                AtsDataProcessLevel.FULL_FEATURED_USER
                            );
                            atsDTOListOfList.add(new ArrayList<>(list));
                            tmpStartDate = rangeEnd.plusDays(1);
                        }
                    }
                }

                for (ArrayList<AttendanceTimeSheetDTO> attendanceTimeSheetDTOArrayList : atsDTOListOfList) {
                    List<AttendanceTimeSheetDTO> absent = attendanceTimeSheetDTOArrayList
                        .stream()
                        .filter(x -> x.getAttendanceStatus() == AttendanceStatus.ABSENT)
                        .collect(Collectors.toList());

                    // Taken AL CL from here
                    int month = attendanceTimeSheetDTOArrayList.get(0).getDate().getMonth().getValue();
                    int year = attendanceTimeSheetDTOArrayList.get(0).getDate().getYear();
                    LocalDate monthStart = LocalDate.of(year, month, 1);
                    LocalDate monthEnd = LocalDate.of(year, month, monthStart.lengthOfMonth());
                    List<AttendanceTimeSheetDTO> monthlyATS = attendanceTimeSheetService.getAttendanceTimeSheet(
                        employee.getId(),
                        monthStart,
                        monthEnd,
                        AtsDataProcessLevel.FULL_FEATURED_USER
                    );
                    int monthlyTakenAnnualLeave = 0;
                    int monthlyTakenCasualLeave = 0;
                    for (AttendanceTimeSheetDTO attendanceTimeSheetDTO : monthlyATS) {
                        if (attendanceTimeSheetDTO.getAttendanceStatus() != null) {
                            if (attendanceTimeSheetDTO.getAttendanceStatus().equals(AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE)) {
                                ++monthlyTakenAnnualLeave;
                            }
                            if (attendanceTimeSheetDTO.getAttendanceStatus().equals(AttendanceStatus.MENTIONABLE_CASUAL_LEAVE)) {
                                ++monthlyTakenCasualLeave;
                            }
                        }
                    }
                    Collections.reverse(absent);

                    int numberOfAbsentDays = absent.size();

                    int casualLeaveToCut = min(casualLeaveBalance, casualLeaveLimit - monthlyTakenCasualLeave, numberOfAbsentDays);
                    int annualLeaveToCut = min(
                        annualLeaveBalance,
                        annualLeaveLimit - monthlyTakenAnnualLeave,
                        (numberOfAbsentDays - casualLeaveToCut)
                    );

                    for (AttendanceTimeSheetDTO attendanceTimeSheetDTO : absent) {
                        if (casualLeaveToCut > 0) {
                            result.add(makeCasualLeave(employee, attendanceTimeSheetDTO.getDate()));
                            --casualLeaveToCut;
                            continue;
                        }
                        if (annualLeaveToCut > 0) {
                            result.add(makeAnnualLeave(employee, attendanceTimeSheetDTO.getDate()));
                            --annualLeaveToCut;
                            continue;
                        }
                    }
                }
            }
            return result;
        } catch (Exception ex) {
            log.error(ex.getMessage() + ex.getStackTrace());
            return result;
        }
    }

    private LeaveApplicationDTO makeAnnualLeave(Employee employee, LocalDate day) {
        LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
        leaveApplicationDTO.setEmployeeId(employee.getId());
        leaveApplicationDTO.setPin(employee.getPin().trim());
        leaveApplicationDTO.setFullName(employee.getFullName());
        leaveApplicationDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
        leaveApplicationDTO.setDesignationName(employee.getDesignation().getDesignationName());
        leaveApplicationDTO.setUnitName(employee.getUnit().getUnitName());
        leaveApplicationDTO.setLeaveType(LeaveType.MENTIONABLE_ANNUAL_LEAVE);
        leaveApplicationDTO.setDurationInDay(1);
        leaveApplicationDTO.setApplicationDate(LocalDate.now());
        leaveApplicationDTO.setIsHRApproved(true);
        leaveApplicationDTO.setIsLineManagerApproved(false);
        leaveApplicationDTO.setStartDate(day);
        leaveApplicationDTO.setEndDate(day);
        leaveApplicationDTO.setReason("System Applied");

        return leaveApplicationDTO;
    }

    private LeaveApplicationDTO makeCasualLeave(Employee employee, LocalDate day) {
        LeaveApplicationDTO leaveApplicationDTO = new LeaveApplicationDTO();
        leaveApplicationDTO.setEmployeeId(employee.getId());
        leaveApplicationDTO.setPin(employee.getPin().trim());
        leaveApplicationDTO.setFullName(employee.getFullName());
        leaveApplicationDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
        leaveApplicationDTO.setDesignationName(employee.getDesignation().getDesignationName());
        leaveApplicationDTO.setUnitName(employee.getUnit().getUnitName());
        leaveApplicationDTO.setLeaveType(LeaveType.MENTIONABLE_CASUAL_LEAVE);
        leaveApplicationDTO.setDurationInDay(1);
        leaveApplicationDTO.setApplicationDate(LocalDate.now());
        leaveApplicationDTO.setIsHRApproved(true);
        leaveApplicationDTO.setIsLineManagerApproved(false);
        leaveApplicationDTO.setStartDate(day);
        leaveApplicationDTO.setEndDate(day);
        leaveApplicationDTO.setReason("System Applied");

        return leaveApplicationDTO;
    }

    private int min(int a, int b, int c) {
        return min(Math.min(a, b), c);
    }

    private int min(int a, int b) {
        return Math.min(a, b);
    }
}
