package com.bits.hr.service;

import com.bits.hr.service.dto.MobileBillDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.MobileBill}.
 */
public interface MobileBillService {
    /**
     * Save a mobileBill.
     *
     * @param mobileBillDTO the entity to save.
     * @return the persisted entity.
     */
    MobileBillDTO save(MobileBillDTO mobileBillDTO);

    /**
     * Get all the mobileBills.
     *
     * @return the list of entities.
     */
    default Page<MobileBillDTO> findAll(Pageable pageable, String searchText) {
        return findAll(pageable, searchText);
    }

    /**
     * Get all the mobileBills.
     *
     * @return the list of entities.
     */
    Page<MobileBillDTO> findAll(Pageable pageable, String searchText, int month, int year);

    /**
     * Get the "id" mobileBill.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MobileBillDTO> findOne(Long id);

    /**
     * Delete the "id" mobileBill.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<MobileBillDTO> findAllByYearAndMonth(int year, int month, Pageable pageable, String searchText);
}
