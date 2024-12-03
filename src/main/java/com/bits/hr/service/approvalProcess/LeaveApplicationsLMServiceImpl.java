package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.User;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.LeaveBalanceDetailViewService;
import com.bits.hr.service.approvalProcess.helperMethods.LeaveApplicationApproval;
import com.bits.hr.service.approvalProcess.helperMethods.SaveAttendanceEntry;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import com.bits.hr.service.mapper.LeaveApplicationMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class LeaveApplicationsLMServiceImpl implements ApprovalProcessService {

    @Autowired
    LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    SaveAttendanceEntry saveAttendanceEntry;

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    @Autowired
    LeaveDaysCalculationService leaveDaysCalculationService;

    @Autowired
    LeaveBalanceDetailViewService leaveBalanceDetailViewService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    @Lazy
    private LeaveApplicationMapper leaveApplicationMapper;

    @Override
    public boolean approveSelected(List<Long> selectedIds) {
        try {
            List<LeaveApplication> leaveApplicationList = this.getVerifiedTeamMembersLeaveApplication(selectedIds);

            for (LeaveApplication leaveApplication : leaveApplicationList) {
                // refresh number of days in leave applications.
                int leaveDays = leaveDaysCalculationService.leaveDaysCalculation(
                    leaveApplication.getStartDate(),
                    leaveApplication.getEndDate()
                );
                leaveApplication.setDurationInDay(leaveDays);
                leaveApplicationRepository.save(leaveApplication);

                int remainingBalance = leaveBalanceDetailViewService.getRemainingBalance(
                    leaveApplication.getStartDate().getYear(),
                    leaveApplication.getEmployee().getId(),
                    leaveApplication.getLeaveType()
                );
                if ((remainingBalance - leaveDays) >= 0) {
                    Optional<User> currentUser = currentEmployeeService.getCurrentUser();
                    leaveApplication = LeaveApplicationApproval.processForSanction(leaveApplication, currentUser);

                    LeaveApplication leaveApplication1 = leaveApplicationRepository.save(
                        LeaveApplicationApproval.processToApproveLM(leaveApplication)
                    );
                    publishEvent(leaveApplicationMapper.toDto(leaveApplication1), EventType.APPROVED);
                }
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean approveAll() {
        try {
            long id = currentEmployeeService.getCurrentEmployeeId().get();
            List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getAllPendingLeaveApplicationsLM(id);

            for (LeaveApplication leaveApplication : leaveApplicationList) {
                // refresh number of days in leave applications.
                int leaveDays = leaveDaysCalculationService.leaveDaysCalculation(
                    leaveApplication.getStartDate(),
                    leaveApplication.getEndDate()
                );
                leaveApplication.setDurationInDay(leaveDays);
                leaveApplicationRepository.save(leaveApplication);

                int remainingBalance = leaveBalanceDetailViewService.getRemainingBalance(
                    leaveApplication.getStartDate().getYear(),
                    leaveApplication.getEmployee().getId(),
                    leaveApplication.getLeaveType()
                );
                if ((remainingBalance - leaveDays) > 0) {
                    LeaveApplication leaveApplication1 = leaveApplicationRepository.save(
                        LeaveApplicationApproval.processToApproveLM(leaveApplication)
                    );
                    publishEvent(leaveApplicationMapper.toDto(leaveApplication1), EventType.APPROVED);
                }
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    @Override
    public boolean denySelected(List<Long> selectedIds) {
        try {
            List<LeaveApplication> leaveApplicationList = this.getVerifiedTeamMembersLeaveApplication(selectedIds);

            for (LeaveApplication leaveApplication : leaveApplicationList) {
                Optional<User> currentUser = currentEmployeeService.getCurrentUser();
                leaveApplication = LeaveApplicationApproval.processForSanction(leaveApplication, currentUser);

                LeaveApplication leaveApplication1 = leaveApplicationRepository.save(
                    LeaveApplicationApproval.processToRejectLM(leaveApplication)
                );
                publishEvent(leaveApplicationMapper.toDto(leaveApplication1), EventType.REJECTED);
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    @Override
    public boolean denyAll() {
        try {
            long id = currentEmployeeService.getCurrentEmployeeId().get();
            List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getAllPendingLeaveApplicationsLM(id);

            for (LeaveApplication leaveApplication : leaveApplicationList) {
                LeaveApplication leaveApplication1 = leaveApplicationRepository.save(
                    LeaveApplicationApproval.processToRejectLM(leaveApplication)
                );

                publishEvent(leaveApplicationMapper.toDto(leaveApplication1), EventType.REJECTED);
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    private List<LeaveApplication> getVerifiedTeamMembersLeaveApplication(List<Long> selectedId) {
        List<Employee> mySubordinateEmployee = employeeRepository.getMyTeamByReportingToId(
            currentEmployeeService.getCurrentEmployee().get().getId()
        );

        List<LeaveApplication> verifiedList = new ArrayList<>();
        selectedId.forEach(leaveApplicationId -> {
            Optional<LeaveApplication> leaveApplication = leaveApplicationRepository.findById(leaveApplicationId);
            if (leaveApplication.isPresent()) {
                Employee employee = leaveApplication.get().getEmployee();
                if (mySubordinateEmployee.contains(employee)) {
                    verifiedList.add(leaveApplication.get());
                }
            }
        });
        return verifiedList;
    }

    private void publishEvent(LeaveApplicationDTO leaveApplication, EventType event) {
        log.debug("publishing leave application event with event: " + event);
        LeaveApplicationEvent leaveApplicationEvent = new LeaveApplicationEvent(this, leaveApplication, event);
        applicationEventPublisher.publishEvent(leaveApplicationEvent);
    }
}
