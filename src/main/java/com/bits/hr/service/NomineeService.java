package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.NomineeStatus;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.service.dto.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.bits.hr.domain.Nominee}.
 */
public interface NomineeService {
    /**
     * Save a nominee.
     *
     * @param nomineeDTO the entity to save.
     * @return the persisted entity.
     */
    NomineeDTO save(NomineeDTO nomineeDTO);

    NomineeDTO saveWithFile(NomineeDTO nomineeDTO, MultipartFile file);

    /**
     * Get all the nominees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NomineeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" nominee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NomineeDTO> findOne(Long id);

    /**
     * Delete the "id" nominee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    double getRemainingPercentage(NomineeDTO nomineeDTO);

    List<NomineeDTO> getNomineeListByEmployeeId(long employeeId);

    EmployeeDetailsNomineeReportDTO getGfNomineeDetailsByEmployeeId(long employeeId);

    NomineeMasterDTO getNomineesByPin(String pin);
    List<NomineeDTO> getNomineesByEmployeeIdAndNomineeType(Long employeeId, NomineeType nomineeType);

    NomineeValidationDTO validateRemainingSharePercentage(NomineeDTO nomineeDTO);

    NomineeValidationDTO validateRemainingSharePercentageForAdmin(NomineeDTO nomineeDTO);

    NomineeDTO validateNomineeAndGuardianNID(NomineeDTO nomineeDTO);

    boolean isEmployeeEligibleForGF(Employee employee);
    boolean isEmployeeEligibleForGeneralNominee(Employee employee);

    Page<NomineeDTO> getAllApprovedOrPendingNominees(String searchText, NomineeType nomineeType, Status status, Pageable pageable);

    EmployeeDetailsNomineeReportDTO getEmployeeDetailsById(long employeeId);

    List<NomineeDTO> getNomineeListByEmployeeIdAndNomineeType(long employeeId, NomineeType nomineeType);

    Page<EmployeeNomineeInfo> getAllNomineeByDateRange(
        Long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        String nomineeType,
        Pageable pageable
    );

    NomineeStatus checkAllPFNomineeApprovedStatus(String employeePin);

    List<EmployeeNomineeInfo> getEmployeeNomineeInfoList(List<Employee> employeeList);

    NomineeStatus checkNomineeApproveStatusByEmployeeIdAndNomineeType(long employeeId, NomineeType nomineeType);
}
