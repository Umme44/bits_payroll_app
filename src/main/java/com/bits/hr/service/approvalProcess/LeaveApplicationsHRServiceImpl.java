package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.User;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.LeaveManagement.leaveBalanceDetail.LeaveBalanceDetailViewService;
import com.bits.hr.service.approvalProcess.helperMethods.LeaveApplicationApproval;
import com.bits.hr.service.approvalProcess.helperMethods.SaveAttendanceEntry;
import com.bits.hr.service.config.CurrentEmployeeService;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class LeaveApplicationsHRServiceImpl implements ApprovalProcessService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private SaveAttendanceEntry saveAttendanceEntry;

    @Autowired
    private LeaveBalanceDetailViewService leaveBalanceDetailViewService;

    @Autowired
    private LeaveDaysCalculationService leaveDaysCalculationService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Override
    public boolean approveSelected(List<Long> selectedIds) {
        try {
            List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getPendingLeaveApplicationsByIds(selectedIds);

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

                    leaveApplicationRepository.save(LeaveApplicationApproval.processToApproveHR(leaveApplication));
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
            List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getAllPendingLeaveApplications();

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
                    leaveApplicationRepository.save(LeaveApplicationApproval.processToApproveHR(leaveApplication));
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
            List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getPendingLeaveApplicationsByIds(selectedIds);

            for (LeaveApplication leaveApplication : leaveApplicationList) {
                Optional<User> currentUser = currentEmployeeService.getCurrentUser();
                leaveApplication = LeaveApplicationApproval.processForSanction(leaveApplication, currentUser);

                leaveApplicationRepository.save(LeaveApplicationApproval.processToRejectHR(leaveApplication));
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
            List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getAllPendingLeaveApplications();

            for (LeaveApplication leaveApplication : leaveApplicationList) {
                leaveApplicationRepository.save(LeaveApplicationApproval.processToRejectHR(leaveApplication));
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }
}
