package com.bits.hr.service.procurementRequisition;

import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ProcReqMaster}.
 */
public interface ProcReqMasterService {
    /**
     * Save a procReqMaster.
     *
     * @param procReqMasterDTO the entity to save.
     * @return the persisted entity.
     */
    ProcReqMasterDTO save(ProcReqMasterDTO procReqMasterDTO);

    /**
     * Get all the procReqMasters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcReqMasterDTO> findAll(
        Long employeeId,
        Long departmentId,
        RequisitionStatus requisitionStatus,
        Integer year,
        Integer month,
        Pageable pageable
    );

    /**
     * Get the "id" procReqMaster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcReqMasterDTO> findOne(Long id);

    List<Long> getRecommenderIDList(ProcReqMasterDTO procReqMasterDTO);

    Optional<ProcReqMasterDTO> findByIdAndEmployeeId(Long id, long employeeId);

    ProcReqMasterDTO create(ProcReqMasterDTO procReqMasterDTO);

    ProcReqMasterDTO update(ProcReqMasterDTO procReqMasterDTO);

    void delete(Long id, boolean checkPending);

    Page<ProcReqMasterDTO> findAllByEmployeeIdAndDepartmentId(long currentEmployeeId, Long departmentId, Pageable pageable);

    String getProcurementOfficerPin();

    void close(Long id, Long closedById);
}
