package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.RecruitmentRequisitionForm;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.repository.RecruitmentRequisitionFormRepository;
import com.bits.hr.service.DepartmentService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.GetRrfConfigService;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.RRFEvent;
import com.bits.hr.service.mapper.RecruitmentRequisitionFormMapper;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RRFApprovalService {

    private final Logger log = LoggerFactory.getLogger(RRFApprovalService.class);

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    @Autowired
    RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository;

    @Autowired
    RecruitmentRequisitionFormMapper recruitmentRequisitionFormMapper;

    @Autowired
    GetRrfConfigService rrfConfigService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * NON-SDLC => CTO is missing in approval flow
     * Pending List flow the approval flow -> (normal sdlc) : requester -> lm > cto > coo > ceo
     * (special sdlc): requester(lm) -> cto > coo > ceo
     * (normal non-sdlc):  requester -> lm > coo > ceo
     * (special non-sdlc):  requester(lm) -> coo > ceo
     *
     * @param currEmpId
     * @return
     */
    public List<RecruitmentRequisitionFormDTO> getPendingList(Long currEmpId) {
        try {
            //Long currEmpId = currentEmployee.getId();
            List<RecruitmentRequisitionFormDTO> recruitmentRequisitionFormList = recruitmentRequisitionFormMapper.toDto(
                recruitmentRequisitionFormRepository.getApprovalList(currEmpId)
            );
            List<RecruitmentRequisitionFormDTO> pendingList = new ArrayList<>();
            for (RecruitmentRequisitionFormDTO rrfDto : recruitmentRequisitionFormList) {

                boolean isL01PendingForCurrEmp = (rrfDto.getRecommendedBy01Id() != null && rrfDto.getRecommendedBy01Id().equals(currEmpId) && rrfDto.getRecommendationDate01() == null);
                boolean isL02PendingForCurrEmp = (rrfDto.getRecommendedBy02Id() != null && rrfDto.getRecommendedBy02Id().equals(currEmpId) && rrfDto.getRecommendationDate02() == null);
                boolean isL03PendingForCurrEmp = (rrfDto.getRecommendedBy03Id() != null && rrfDto.getRecommendedBy03Id().equals(currEmpId) && rrfDto.getRecommendationDate03() == null);
                boolean isL04PendingForCurrEmp = (rrfDto.getRecommendedBy04Id() != null && rrfDto.getRecommendedBy04Id().equals(currEmpId) && rrfDto.getRecommendationDate04() == null);
                boolean isL05PendingForCurrEmp = (rrfDto.getRecommendedBy05Id() != null && rrfDto.getRecommendedBy05Id().equals(currEmpId) && rrfDto.getRecommendationDate05() == null);
                // TODO break down if block into steps so that rec1 -> rec2 -> ...
//                if(isL01PendingForCurrEmp || isL02PendingForCurrEmp || isRec03Pending || isRec04Pending || isRec05Pending){
//                    pendingList.add(rrfDto);
//                }

                // normal flow
                // Pending for LM
                boolean isL1Passed = isPassed(rrfDto.getRecommendedBy01Id(),rrfDto.getRecommendationDate01());
                boolean isL2Passed = isL1Passed && isPassed(rrfDto.getRecommendedBy02Id(),rrfDto.getRecommendationDate02());
                boolean isL3Passed = isL2Passed && isPassed(rrfDto.getRecommendedBy03Id(),rrfDto.getRecommendationDate03());
                boolean isL4Passed = isL3Passed && isPassed(rrfDto.getRecommendedBy04Id(),rrfDto.getRecommendationDate04());
                boolean isL5Passed = isL4Passed && isPassed(rrfDto.getRecommendedBy05Id(),rrfDto.getRecommendationDate05());

                if (!isL1Passed && isL01PendingForCurrEmp) {
                    pendingList.add(rrfDto);
                }
                // Pending from HoD
                else if (!isL2Passed && isL02PendingForCurrEmp && isL1Passed) {
                    pendingList.add(rrfDto);
                }
                // Pending from CTO
                else if (!isL3Passed && isL03PendingForCurrEmp && isL2Passed) {
                    pendingList.add(rrfDto);
                }
                // Pending from HoHR
                else if (!isL4Passed && isL04PendingForCurrEmp && isL2Passed ) {
                    pendingList.add(rrfDto);
                }
                // Pending from CEO
                else if (!isL5Passed && isL05PendingForCurrEmp && isL4Passed) {
                    pendingList.add(rrfDto);
                }
            }
            return pendingList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Transactional
    public boolean approveSelected(List<Long> selectedIds, LocalDate recommendationDate, Employee currentEmployee) {
        // business => after last approval, application will be [closed], other than application will be [on progress]
        try {
            for (Long id : selectedIds) {
                RecruitmentRequisitionForm rrf = recruitmentRequisitionFormRepository.findById(id).get();
                String recommendedStage = "";
                if (rrf.getRecommendedBy01() != null &&
                    rrf.getRecommendedBy01().equals(currentEmployee) &&
                    rrf.getRecommendationDate01() == null ) {
                    rrf.setRecommendationDate01(recommendationDate);
                    rrf.setRequisitionStatus(RequisitionStatus.LM_APPROVED);
                    recommendedStage = "LM";
                } else if (rrf.getRecommendedBy02() != null && rrf.getRecommendedBy02().equals(currentEmployee) && rrf.getRecommendationDate02() == null ) {
                    rrf.setRecommendationDate02(recommendationDate);
                    rrf.setRequisitionStatus(RequisitionStatus.HOD_APPROVED);
                    recommendedStage = "HoD";
                } else if (rrf.getRecommendedBy03() != null && rrf.getRecommendedBy03().equals(currentEmployee) && rrf.getRecommendationDate03() == null ) {
                    rrf.setRecommendationDate03(recommendationDate);
                    rrf.setRequisitionStatus(RequisitionStatus.CTO_APPROVED);
                    recommendedStage = "CTO";
                } else if (rrf.getRecommendedBy04() != null && rrf.getRecommendedBy04().equals(currentEmployee) && rrf.getRecommendationDate04() == null ) {
                    rrf.setRecommendationDate04(recommendationDate);
                    rrf.setRequisitionStatus(RequisitionStatus.HOHR_VETTED);
                    recommendedStage = "HoHr";
                } else if (rrf.getRecommendedBy05() != null && rrf.getRecommendedBy05().equals(currentEmployee) && rrf.getRecommendationDate05() == null ) {
                    rrf.setRecommendationDate05(recommendationDate);
                    rrf.setRequisitionStatus(RequisitionStatus.CEO_APPROVED);
                    recommendedStage = "CEO";
                }

                recruitmentRequisitionFormRepository.save(rrf);
                publishEvent(recruitmentRequisitionFormMapper.toDto(rrf), EventType.APPROVED, recommendedStage);
            }
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean rejectSelected(List<Long> selectedIds, LocalDate date) {
        try {
            Optional<Employee> currentEmployee = currentEmployeeService.getCurrentEmployee();
            for (Long id : selectedIds) {
                RecruitmentRequisitionForm requisitionForm = recruitmentRequisitionFormRepository.findById(id).get();
                requisitionForm.requisitionStatus(RequisitionStatus.NOT_APPROVED)
                    .rejectedAt(date)
                    .rejectedBy(currentEmployee.get());
                RecruitmentRequisitionForm recruitmentRequisitionForm = recruitmentRequisitionFormRepository.save(requisitionForm);
                publishEvent(recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm), EventType.REJECTED);
            }
            return true;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }


    private void publishEvent(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, EventType event, String recommendedStage) {
        long nextRecommendedById = 0;
        if (recommendedStage.equals("1st")) {
            if (recruitmentRequisitionFormDTO.getRecommendedBy02Id() != null) {
                nextRecommendedById = recruitmentRequisitionFormDTO.getRecommendedBy02Id();
            } else if (recruitmentRequisitionFormDTO.getRecommendedBy03Id() != null) {
                nextRecommendedById = recruitmentRequisitionFormDTO.getRecommendedBy03Id();
            }
        } else if (recommendedStage.equals("2nd")) {
            if (recruitmentRequisitionFormDTO.getRecommendedBy03Id() != null) {
                nextRecommendedById = recruitmentRequisitionFormDTO.getRecommendedBy03Id();
            }
        } else if (recommendedStage.equals("coo")) {
            nextRecommendedById = recruitmentRequisitionFormDTO.getRecommendedBy04Id();
        } else if (recommendedStage.equals("ceo")) {
            // -1 means, ceo has approved and closed, HR will notify,
            nextRecommendedById = -1;
        }
        log.info("publishing email event for RRF with : " + event);
        RRFEvent rrfEvent = new RRFEvent(this, recruitmentRequisitionFormDTO, nextRecommendedById, event);
        applicationEventPublisher.publishEvent(rrfEvent);
    }

    // rejected
    private void publishEvent(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, EventType event) {
        log.info("publishing email event for RRF with : " + event);
        RRFEvent rrfEvent = new RRFEvent(this, recruitmentRequisitionFormDTO, event);
        applicationEventPublisher.publishEvent(rrfEvent);
    }


    private boolean isPassed(Long recommenderId, LocalDate recommendationDate) {
        boolean isPassed = false;
        if (recommenderId == null) {
            isPassed = true;
        } else if (recommendationDate != null) {
            isPassed = true;
        }

        return isPassed;
    }

}
