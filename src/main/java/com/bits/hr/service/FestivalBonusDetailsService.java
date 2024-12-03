package com.bits.hr.service;

import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.FestivalBonusDetails}.
 */
public interface FestivalBonusDetailsService {
    /**
     * Save a festivalBonusDetails.
     *
     * @param festivalBonusDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    FestivalBonusDetailsDTO save(FestivalBonusDetailsDTO festivalBonusDetailsDTO);

    FestivalBonusDetailsDTO update(FestivalBonusDetailsDTO festivalBonusDetailsDTO);

    /**
     * Get all the festivalBonusDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FestivalBonusDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" festivalBonusDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FestivalBonusDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" festivalBonusDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    EmployeeSalaryDTO prepareFestivalBonusPayslip(long employeeId, int year, int festivalNo);

    Set<Integer> getDisbursedFestivalYearsByEmployeeId(long employeeId, LocalDate disbursementDate);

    List<FestivalBonusDetailsDTO> getYearWiseFestivalBonusDetailsList(long employeeId, int year);

    boolean holdOrUnHoldFestivalBonusDetail(long fbDetailId, boolean isHold);
}
