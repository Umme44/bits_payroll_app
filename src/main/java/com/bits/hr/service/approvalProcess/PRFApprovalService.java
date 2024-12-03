package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.ProcReqMaster;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.ProcReqMasterRepository;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import com.bits.hr.service.event.PRFEvent;
import com.bits.hr.service.mapper.ProcReqMasterMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PRFApprovalService {

    private final Logger log = LoggerFactory.getLogger(PRFApprovalService.class);

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ProcReqMasterRepository procReqMasterRepository;

    @Autowired
    ProcReqMasterMapper procReqMasterMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public List<ProcReqMasterDTO> getPendingList(long currentEmployeeId) {
        return procReqMasterMapper.toDto(procReqMasterRepository.findPendingList(currentEmployeeId));
    }

    public void approvedByDepartmentHead(List<Long> selectedIds, long recommendationFrom, Boolean isCTOApprovalRequired) {
        for (Long id : selectedIds) {
            ProcReqMaster procReqMaster = procReqMasterRepository.findById(id).get();
            // must check current next approval is 01 (means dept. head is approving)
            if (
                procReqMaster.getNextApprovalFrom().getId().equals(recommendationFrom) &&
                procReqMaster.getNextRecommendationOrder().equals(1)
            ) {
                if (isCTOApprovalRequired != null && isCTOApprovalRequired) {
                    procReqMaster.setNextApprovalFrom(procReqMaster.getRecommendedBy02());
                    procReqMaster.setNextRecommendationOrder(2);
                } else {
                    procReqMaster.setRecommendedBy02(null);
                    procReqMaster.setNextApprovalFrom(procReqMaster.getRecommendedBy03());
                    procReqMaster.setNextRecommendationOrder(3);
                }
                procReqMaster.setIsCTOApprovalRequired(isCTOApprovalRequired);
                procReqMaster.setRecommendationAt01(Instant.now());
                procReqMaster.setRequisitionStatus(RequisitionStatus.IN_PROGRESS);

                ProcReqMaster savedProcReqMaster = procReqMasterRepository.save(procReqMaster);
                publishEventOnApprove(savedProcReqMaster.getId(), savedProcReqMaster.getRequisitionStatus());
            }
        }
    }

    @Transactional
    public void approvedByOthersExceptDeptHead(List<Long> selectedIds, long recommendationFrom) {
        // business => after last approval, application will be [open], other than application will be [on progress]
        // after opening prf officer will [close] by fulfilling the requisition

        for (Long id : selectedIds) {
            ProcReqMaster procReqMaster = procReqMasterRepository.findById(id).get();

            // verify
            if (procReqMaster.getNextApprovalFrom().getId().equals(recommendationFrom)) {
                switch (procReqMaster.getNextRecommendationOrder()) {
                    case 2: // cto
                        procReqMaster.setNextRecommendationOrder(3);
                        procReqMaster.setNextApprovalFrom(procReqMaster.getRecommendedBy03());
                        procReqMaster.setRecommendationAt02(Instant.now());
                        procReqMaster.setRequisitionStatus(RequisitionStatus.IN_PROGRESS);
                        break;
                    case 3: // coo
                        procReqMaster.setNextRecommendationOrder(4);
                        procReqMaster.setNextApprovalFrom(procReqMaster.getRecommendedBy04());
                        procReqMaster.setRecommendationAt03(Instant.now());
                        procReqMaster.setRequisitionStatus(RequisitionStatus.IN_PROGRESS);
                        break;
                    case 4: // finance manager
                        procReqMaster.setNextRecommendationOrder(5);
                        procReqMaster.setNextApprovalFrom(procReqMaster.getRecommendedBy05());
                        procReqMaster.setRecommendationAt04(Instant.now());
                        procReqMaster.setRequisitionStatus(RequisitionStatus.IN_PROGRESS);
                        break;
                    case 5: // ceo
                        procReqMaster.setNextRecommendationOrder(null);
                        procReqMaster.setNextApprovalFrom(null);
                        procReqMaster.setRecommendationAt05(Instant.now());
                        procReqMaster.setRequisitionStatus(RequisitionStatus.OPEN);
                        break;
                }

                ProcReqMaster savedProcReqMaster = procReqMasterRepository.save(procReqMaster);
                publishEventOnApprove(savedProcReqMaster.getId(), savedProcReqMaster.getRequisitionStatus());
            } else {
                throw new RuntimeException("You cannot approve this PRF.");
            }
        }
    }

    @Transactional
    public void rejectSelected(Long selectedId, Long rejectedBy, String rejectionReason) {
        ProcReqMaster procReqMaster = procReqMasterRepository.findById(selectedId).get();
        if (procReqMaster.getNextApprovalFrom().getId().equals(rejectedBy)) {
            procReqMaster.setRejectedBy(employeeRepository.findById(rejectedBy).get());
            procReqMaster.rejectedDate(LocalDate.now());
            procReqMaster.setRequisitionStatus(RequisitionStatus.NOT_APPROVED);
            procReqMaster.setRejectionReason(rejectionReason);
        } else {
            throw new RuntimeException("You cannot reject this PRF.");
        }
        ProcReqMaster savedProcReqMaster = procReqMasterRepository.save(procReqMaster);
        publishEventOnReject(savedProcReqMaster.getId());
    }

    // mail after approval
    private void publishEventOnApprove(long procReqMasterId, RequisitionStatus requisitionStatus) {
        log.info("publishing email event for PRF for Passing Approval Layer");
        PRFEvent prfEvent = new PRFEvent(this, procReqMasterId, requisitionStatus);
        applicationEventPublisher.publishEvent(prfEvent);
    }

    // mail after rejection
    private void publishEventOnReject(long procReqMasterId) {
        log.info("publishing email event for PRF reject");
        PRFEvent prfEvent = new PRFEvent(this, procReqMasterId, RequisitionStatus.NOT_APPROVED);
        applicationEventPublisher.publishEvent(prfEvent);
    }
}
