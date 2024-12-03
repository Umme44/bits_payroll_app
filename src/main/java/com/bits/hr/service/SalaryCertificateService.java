package com.bits.hr.service;

import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.service.dto.EmployeeSalaryCertificateReportDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.SalaryCertificateDTO;

import java.util.List;
import java.util.Optional;

import com.bits.hr.service.search.FilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.SalaryCertificate}.
 */
public interface SalaryCertificateService {

    /**
     * Save a salaryCertificate.
     *
     * @param salaryCertificateDTO the entity to save.
     * @return the persisted entity.
     */
    SalaryCertificateDTO save(SalaryCertificateDTO salaryCertificateDTO);

    /**
     * Get all the salaryCertificates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SalaryCertificateDTO> findAll(FilterDto filter, Pageable pageable);


    /**
     * Get the "id" salaryCertificate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SalaryCertificateDTO> findOne(Long id);

    /**
     * Delete the "id" salaryCertificate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<EmployeeSalaryDTO> getSalariesForDropDown(Long id);

    Optional<EmployeeSalaryDTO> getSalaryForSalaryCertificates(Long id);

    Optional<SalaryCertificateDTO> getSalaryCertificateById(Long id);

    EmployeeSalaryCertificateReportDTO getSalaryCertificateReportByCertificateId(long id);

    Page<SalaryCertificateDTO> findAllFilterByStatus(Long id, CertificateStatus status, Pageable pageable);

    Page<SalaryCertificateDTO> findAll(String searchText, CertificateStatus status, Integer selectedYear, Pageable pageable);

    boolean isReferenceNumberUnique(String referenceNumber);

    boolean isSalaryCertificateExistsForEmployee(SalaryCertificateDTO salaryCertificateDTO);
}
