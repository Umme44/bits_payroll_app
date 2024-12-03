package com.bits.hr.service;

import com.bits.hr.service.dto.EmployeeDetailsNomineeReportDTO;
import com.bits.hr.service.dto.NomineeValidationDTO;
import com.bits.hr.service.dto.PfAccountDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.bits.hr.domain.PfNominee}.
 */
public interface PfNomineeFormService {
    /**
     * Get the "id" pfNominee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PfNomineeDTO> findOne(Long id) throws IOException;

    PfNomineeDTO save(MultipartFile file, PfNomineeDTO pfNominee, boolean hasMultipartChanged);

    PfNomineeDTO save(PfNomineeDTO pfNominee);

    PfNomineeDTO validateIsApproved(PfNomineeDTO pfNomineeDTO);

    /**
     * Get all the pfAccounts by pin.
     *
     * @return the list of entities.
     */
    List<PfAccountDTO> findPfAccountsByPin();

    /**
     * Get all the pfNominees by pin.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PfNomineeDTO> findPfNomineesOfCurrentUser(Pageable pageable) throws Exception;

    EmployeeDetailsNomineeReportDTO getEmployeeDetailsByPin(String pin);

    List<PfNomineeDTO> findAll();

    NomineeValidationDTO validateNominee(PfNomineeDTO pfNomineeDTO);
    PfNomineeDTO validateNomineeAndGuardianNID(PfNomineeDTO pfNomineeDTO);

    NomineeValidationDTO validateRemainingSharePercentage(PfNomineeDTO pfNomineeDTO);

    double getTotalConsumedSharedPercentageOfCurrentUser();
}
