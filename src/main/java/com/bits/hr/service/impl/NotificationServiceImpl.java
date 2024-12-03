package com.bits.hr.service.impl;

import com.bits.hr.repository.*;
import com.bits.hr.service.NotificationService;
import com.bits.hr.service.approvalProcess.RRFApprovalService;
import com.bits.hr.service.dto.NotificationDTO;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final ManualAttendanceEntryRepository manualAttendanceEntryRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final MovementEntryRepository movementEntryRepository;
    private final FlexScheduleApplicationRepository flexScheduleApplicationRepository;
    private final RRFApprovalService rrfApprovalService;
    private final RecruitmentRequisitionFormRepository rrfRepository;
    private final ProcReqMasterRepository procReqMasterRepository;
    private final WorkFromHomeApplicationRepository workFromHomeApplicationRepository;

    public NotificationServiceImpl(
        ManualAttendanceEntryRepository manualAttendanceEntryRepository,
        LeaveApplicationRepository leaveApplicationRepository,
        MovementEntryRepository movementEntryRepository,
        FlexScheduleApplicationRepository flexScheduleApplicationRepository,
        RecruitmentRequisitionFormRepository rrfRepository,
        RRFApprovalService rrfApprovalService,
        ProcReqMasterRepository procReqMasterRepository,
        WorkFromHomeApplicationRepository workFromHomeApplicationRepository
    ) {
        this.manualAttendanceEntryRepository = manualAttendanceEntryRepository;
        this.movementEntryRepository = movementEntryRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.flexScheduleApplicationRepository = flexScheduleApplicationRepository;
        this.rrfRepository = rrfRepository;
        this.rrfApprovalService = rrfApprovalService;
        this.procReqMasterRepository = procReqMasterRepository;
        this.workFromHomeApplicationRepository = workFromHomeApplicationRepository;
    }

    @Override
    public NotificationDTO getApprovalNotificationsCommon(long employeeId) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setPendingManualAttendanceApprovals(manualAttendanceEntryRepository.getTotalPendingAttendancesLM(employeeId));
        notificationDTO.setPendingLeaveApprovals(leaveApplicationRepository.getTotalPendingLeaveApplicationsLM(employeeId));
        notificationDTO.setPendingMovementEntriesApprovals(movementEntryRepository.getAllPendingMovementEntriesLM(employeeId));
        notificationDTO.setPendingFlexScheduleApprovals(flexScheduleApplicationRepository.getCountingOfPendingByReportingTo(employeeId));
        notificationDTO.setPendingRRFApprovals(rrfApprovalService.getPendingList(employeeId).size());
        notificationDTO.setPendingWorkFromHomeApprovals(workFromHomeApplicationRepository.getTotalPendingWFHApplicationsLM(employeeId));
        notificationDTO.setPendingPRFApprovals(procReqMasterRepository.findPendingList(employeeId).size());
        return notificationDTO;
    }

    @Override
    public NotificationDTO getApprovalNotificationsHR() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setPendingManualAttendanceApprovals(manualAttendanceEntryRepository.getTotalPendingAttendancesHR());
        notificationDTO.setPendingLeaveApprovals(leaveApplicationRepository.getTotalPendingLeaveApplications());
        notificationDTO.setPendingMovementEntriesApprovals(movementEntryRepository.getAllPendingMovementEntriesHR());
        notificationDTO.setPendingRRFApprovals(rrfRepository.getPendingCount());
        notificationDTO.setPendingFlexScheduleApprovals(flexScheduleApplicationRepository.getCountingOfAllPending());
        notificationDTO.setPendingWorkFromHomeApprovals(workFromHomeApplicationRepository.getTotalPendingWFHApplicationsHR());
        return notificationDTO;
    }
}
