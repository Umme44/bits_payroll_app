package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;

import java.time.LocalDate;
import java.util.Optional;

import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.RecruitmentRequisitionForm}.
 */
public interface RecruitmentRequisitionFormService {

    RecruitmentRequisitionFormDTO create(RecruitmentRequisitionFormDTO rrf);

    /**
     * Save a recruitmentRequisitionForm.
     *
     * @param recruitmentRequisitionFormDTO the entity to save.
     * @return the persisted entity.
     */
    RecruitmentRequisitionFormDTO update(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO);

    /**
     * Get all the recruitmentRequisitionForms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecruitmentRequisitionFormDTO> findAll(
        Long requesterId,
        Long departmentId,
        LocalDate startDate,
        LocalDate endDate,
        RequisitionStatus requisitionStatus,
        Pageable pageable
    );

    Page<RecruitmentRequisitionFormDTO> findAllRaisedOnBehalf(User createdBy, Employee requester, Pageable pageable);

    Page<RecruitmentRequisitionFormDTO> findAllRaisedByUser(
        Long requesterId,
        Long departmentId,
        LocalDate startDate,
        LocalDate endDate,
        RequisitionStatus requisitionStatus,
        Pageable pageable,
        User createdBy,
        Employee requester
    );

    /**
     * Get the "id" recruitmentRequisitionForm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecruitmentRequisitionFormDTO> findOne(Long id);

    /**
     * Delete the "id" recruitmentRequisitionForm.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    RecruitmentRequisitionFormDTO closeRRF(Long id, User user);

    RecruitmentRequisitionFormDTO closeRRFPartially(Long id, Integer totalOnboard, User user);

//    boolean changeRRFStatusFromClosedToOpen();

    ExportXLPropertiesDTO exportRRF(
        Long requesterId,
        Long departmentId,
        RequisitionStatus requisitionStatus,
        LocalDate startDate,
        LocalDate endDate,
        Pageable pageable
    );

    Boolean isRRFAllowedToDelete(Long id);
}
