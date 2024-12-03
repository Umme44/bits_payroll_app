package com.bits.hr.service.rrfUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import com.bits.hr.service.dto.RrfRaiseValidityDTO;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentRequisitionFormUserService {

    Optional<RecruitmentRequisitionFormDTO> getById(Long id, Employee currentEmployee);

    RecruitmentRequisitionFormDTO create(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, Employee currentEmployee);

    Optional<RecruitmentRequisitionFormDTO> update(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO);

    boolean delete(Long id);

    Page<RecruitmentRequisitionFormDTO> findAllByRequester(Employee requester, RequisitionStatus requisitionStatus, LocalDate startDate, LocalDate endDate, Pageable pageable);

    void loadApprovalFlow(RecruitmentRequisitionFormDTO rrfDTO);

    RrfRaiseValidityDTO canRaiseRRF(Employee employee);

}
