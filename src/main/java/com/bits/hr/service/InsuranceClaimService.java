package com.bits.hr.service;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.ClaimStatus;
import com.bits.hr.service.dto.InsuranceClaimDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.InsuranceClaim}.
 */
public interface InsuranceClaimService {
    InsuranceClaimDTO createInsuranceClaim(InsuranceClaimDTO insuranceClaimDTO, User user);
    InsuranceClaimDTO updateInsuranceClaim(InsuranceClaimDTO insuranceClaimDTO, User user);

    /**
     * Get all the insuranceClaims.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InsuranceClaimDTO> findAll(String searchText, ClaimStatus status, Integer year, Integer month, Pageable pageable);

    /**
     * Get the "id" insuranceClaim.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InsuranceClaimDTO> findOne(Long id);

    /**
     * Delete the "id" insuranceClaim.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
