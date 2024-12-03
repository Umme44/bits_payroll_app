package com.bits.hr.service.procurementRequisition.impl;

import com.bits.hr.domain.ProcReqMaster;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.ProcReqMasterRepository;
import com.bits.hr.service.ConfigService;
import com.bits.hr.service.config.DefinedKeys;
import com.bits.hr.service.config.GetPRFApprovalConfigService;
import com.bits.hr.service.dto.ConfigDTO;
import com.bits.hr.service.dto.ProcReqDTO;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import com.bits.hr.service.event.PRFEvent;
import com.bits.hr.service.mapper.ProcReqMapper;
import com.bits.hr.service.mapper.ProcReqMasterMapper;
import com.bits.hr.service.procurementRequisition.ProcReqMasterService;
import com.bits.hr.service.procurementRequisition.ProcReqService;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProcReqMaster}.
 */
@Service
@Transactional
public class ProcReqMasterServiceImpl implements ProcReqMasterService {

    private final Logger log = LoggerFactory.getLogger(ProcReqMasterServiceImpl.class);

    private final ProcReqMasterRepository procReqMasterRepository;

    private final ProcReqService procReqService;

    private final ProcReqMasterMapper procReqMasterMapper;

    @Autowired
    private ProcReqMapper procReqMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private GetPRFApprovalConfigService prfApprovalConfigService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public ProcReqMasterServiceImpl(
        ProcReqMasterRepository procReqMasterRepository,
        ProcReqService procReqService,
        ProcReqMasterMapper procReqMasterMapper
    ) {
        this.procReqMasterRepository = procReqMasterRepository;
        this.procReqService = procReqService;
        this.procReqMasterMapper = procReqMasterMapper;
    }

    @Override
    public ProcReqMasterDTO save(ProcReqMasterDTO procReqMasterDTO) {
        log.debug("Request to save ProcReqMaster : {}", procReqMasterDTO);
        ProcReqMaster procReqMaster = procReqMasterMapper.toEntity(procReqMasterDTO);
        procReqMaster = procReqMasterRepository.save(procReqMaster);
        return procReqMasterMapper.toDto(procReqMaster);
    }

    @Override
    public ProcReqMasterDTO create(ProcReqMasterDTO procReqMasterDTO) {
        procReqMasterDTO.setRequisitionNo(generationOfPRFNumber());

        procReqMasterDTO
            .getProcReqs()
            .forEach(procReqDTO -> {
                procReqDTO.setId(null);
            });

        procReqMasterDTO.setRequisitionStatus(RequisitionStatus.PENDING);
        procReqMasterDTO.setRequestedDate(LocalDate.now());
        procReqMasterDTO.setIsCTOApprovalRequired(false); // it can change only 1st approver. (dept.head)

        // fill approvals
        prfApprovalConfigService.fillPRFApprovals(procReqMasterDTO);
        procReqMasterDTO.setNextApprovalFromId(procReqMasterDTO.getRecommendedBy01Id());
        procReqMasterDTO.setNextRecommendationOrder(1);

        ProcReqMasterDTO result = save(procReqMasterDTO);

        procReqService.saveAll(procReqMasterDTO.getProcReqs(), result.getId());
        publishEventOnCreate(result.getId());
        return result;
    }

    @Override
    public ProcReqMasterDTO update(ProcReqMasterDTO procReqMasterDTO) {
        ProcReqMasterDTO savedData = findOne(procReqMasterDTO.getId()).get();
        if (savedData.getRequisitionStatus() != RequisitionStatus.PENDING) {
            throw new BadRequestAlertException("Status is not in pending.", "procReqMaster", "");
        }

        // fill approvals
        prfApprovalConfigService.fillPRFApprovals(procReqMasterDTO);
        procReqMasterDTO.setNextApprovalFromId(procReqMasterDTO.getRecommendedBy01Id());
        procReqMasterDTO.setNextRecommendationOrder(1);

        procReqService.updateFiles(procReqMasterDTO.getProcReqs());

        // firstly, delete old procurement requisitions
        // secondly, save new procurement requisition
        procReqService.deleteAllProcReqByMasterId(procReqMasterDTO.getId(), true);
        procReqService.saveAll(procReqMasterDTO.getProcReqs(), procReqMasterDTO.getId());

        return save(procReqMasterDTO);
    }

    public String generationOfPRFNumber() {
        long total = procReqMasterRepository.count();
        long next = ++total;
        NumberFormat formatter = new DecimalFormat("0000");
        String prfNumber = "PRF-" + formatter.format(next);

        while (procReqMasterRepository.findByPRFNumber(prfNumber).isPresent()) {
            prfNumber = "PRF-" + formatter.format(++next);
        }
        return prfNumber;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcReqMasterDTO> findAll(
        Long employeeId,
        Long departmentId,
        RequisitionStatus requisitionStatus,
        Integer year,
        Integer month,
        Pageable pageable
    ) {
        log.debug("Request to get all ProcReqMasters");
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (year != null) {
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, 31);
            if (month != null) {
                startDate = LocalDate.of(year, month, 1);
                endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
            }
        }
        return procReqMasterRepository
            .findAll(employeeId, departmentId, requisitionStatus, startDate, endDate, pageable)
            .map(procReqMasterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcReqMasterDTO> findAllByEmployeeIdAndDepartmentId(long requesterId, Long departmentId, Pageable pageable) {
        return procReqMasterRepository
            .findAllByEmployeeIdAndDepartmentId(requesterId, departmentId, pageable)
            .map(procReqMasterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcReqMasterDTO> findOne(Long id) {
        log.debug("Request to get ProcReqMaster : {}", id);
        Optional<ProcReqMaster> procReqMaster = procReqMasterRepository.findById(id);
        if (procReqMaster.isPresent()) {
            List<ProcReqDTO> procReqDTOList = procReqMapper.toDto(new ArrayList<>(procReqMaster.get().getProcReqs()));
            Optional<ProcReqMasterDTO> procReqMasterDTO = procReqMaster.map(procReqMasterMapper::toDto);
            procReqMasterDTO.get().setProcReqs((new HashSet<>(procReqDTOList)));
            return procReqMasterDTO;
        }
        return procReqMaster.map(procReqMasterMapper::toDto);
    }

    @Override
    public List<Long> getRecommenderIDList(ProcReqMasterDTO procReqMasterDTO) {
        List<Long> recommenderIdList = new ArrayList<>();
        if (procReqMasterDTO.getRecommendedBy01Id() != null) {
            recommenderIdList.add(procReqMasterDTO.getRecommendedBy01Id());
        }

        if (procReqMasterDTO.getRecommendedBy02Id() != null) {
            recommenderIdList.add(procReqMasterDTO.getRecommendedBy02Id());
        }

        if (procReqMasterDTO.getRecommendedBy03Id() != null) {
            recommenderIdList.add(procReqMasterDTO.getRecommendedBy03Id());
        }

        if (procReqMasterDTO.getRecommendedBy04Id() != null) {
            recommenderIdList.add(procReqMasterDTO.getRecommendedBy04Id());
        }

        if (procReqMasterDTO.getRecommendedBy05Id() != null) {
            recommenderIdList.add(procReqMasterDTO.getRecommendedBy05Id());
        }
        return recommenderIdList;
    }

    @Override
    public Optional<ProcReqMasterDTO> findByIdAndEmployeeId(Long id, long employeeId) {
        log.debug("Request to get ProcReqMaster : {}", id);
        Optional<ProcReqMaster> procReqMaster = procReqMasterRepository.findByIdAndEmployeeId(id, employeeId);
        if (procReqMaster.isPresent()) {
            List<ProcReqDTO> procReqDTOList = procReqMapper.toDto(new ArrayList<>(procReqMaster.get().getProcReqs()));
            Optional<ProcReqMasterDTO> procReqMasterDTO = procReqMaster.map(procReqMasterMapper::toDto);
            procReqMasterDTO.get().setProcReqs((new HashSet<>(procReqDTOList)));
            return procReqMasterDTO;
        }
        return procReqMaster.map(procReqMasterMapper::toDto);
    }

    @Override
    public void delete(Long id, boolean checkPending) {
        log.debug("Request to force delete ProcReqMaster : {}", id);
        if (checkPending) {
            ProcReqMasterDTO procReqMasterDTO = findOne(id).get();
            if (procReqMasterDTO.getRequisitionStatus() != RequisitionStatus.PENDING) {
                throw new BadRequestAlertException("Requisition must be in pending status.", "", "");
            }
        }
        procReqService.deleteAllProcReqByMasterId(id, true);
        procReqMasterRepository.deleteById(id);
    }

    @Override
    public String getProcurementOfficerPin() {
        ConfigDTO configDTO = configService
            .findOneByKey(DefinedKeys.PRF_OFFICER_EMPLOYEE_PIN)
            .orElseThrow(() -> new BadRequestAlertException("Procurement Officer Pin has not setup yet", "ProcReqMasterService", ""));
        return configDTO.getValue();
    }

    @Override
    public void close(Long id, Long closedById) {
        ProcReqMasterDTO procReqMasterDTO = findOne(id).get();
        procReqMasterDTO.setRequisitionStatus(RequisitionStatus.CLOSED);
        procReqMasterDTO.setClosedAt(Instant.now());
        procReqMasterDTO.setClosedById(closedById);

        save(procReqMasterDTO);
    }

    private void publishEventOnCreate(long procReqMasterId) {
        log.info("publishing email event for PRF for creating PRF");
        PRFEvent prfEvent = new PRFEvent(this, procReqMasterId, RequisitionStatus.PENDING);
        applicationEventPublisher.publishEvent(prfEvent);
    }
}
