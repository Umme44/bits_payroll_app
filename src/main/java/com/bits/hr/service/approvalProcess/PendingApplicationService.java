package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.domain.MovementEntry;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.repository.WorkFromHomeApplicationRepository;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import com.bits.hr.service.mapper.FlexScheduleApplicationMapper;
import com.bits.hr.service.mapper.FlexScheduleMapper;
import com.bits.hr.service.mapper.LeaveApplicationMapper;
import com.bits.hr.service.mapper.ManualAttendanceEntryMapper;
import com.bits.hr.service.mapper.WorkFromHomeApplicationMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class PendingApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    @Lazy
    private LeaveApplicationMapper leaveApplicationMapper;

    @Autowired
    private ManualAttendanceEntryRepository manualAttendanceEntryRepository;

    @Autowired
    private ManualAttendanceEntryMapper manualAttendanceEntryMapper;

    @Autowired
    private WorkFromHomeApplicationRepository workFromHomeApplicationRepository;

    @Autowired
    private WorkFromHomeApplicationMapper workFromHomeApplicationMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Autowired
    private FlexScheduleApplicationMapper flexScheduleApplicationMapper;

    public List<LeaveApplicationDTO> getAllPendingLeaveApplicationsHR() {
        return leaveApplicationMapper.toDto(leaveApplicationRepository.getAllPendingLeaveApplications());
    }

    public List<LeaveApplicationDTO> getAllPendingLeaveApplicationsLM(long employeeId) {
        return leaveApplicationMapper.toDto(leaveApplicationRepository.getAllPendingLeaveApplicationsLM(employeeId));
    }

    public List<LeaveApplicationDTO> getPendingLeaveApplicationOfTeam(long lineManagerId) {
        List<Employee> mySubordinateEmployee = employeeRepository.getMyTeamByReportingToId(lineManagerId);
        List<LeaveApplicationDTO> pendingList = new ArrayList<>();
        for (Employee employee : mySubordinateEmployee) {
            pendingList.addAll(
                leaveApplicationMapper.toDto(leaveApplicationRepository.findAllPendingLeaveApplicationsByEmployeeId(employee.getId()))
            );
        }
        return pendingList;
    }

    public List<ManualAttendanceEntryDTO> getAllPendingAttendanceHR() {
        return manualAttendanceEntryMapper.toDto(manualAttendanceEntryRepository.getAllPendingAttendances());
    }

    public List<ManualAttendanceEntryDTO> getAllPendingAttendanceLM(long employeeId) {
        return manualAttendanceEntryMapper.toDto(manualAttendanceEntryRepository.getAllPendingAttendancesLM(employeeId));
    }
}
