package com.bits.hr.service;

import com.bits.hr.service.dto.AttendanceSummaryDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * Service Interface for managing {@link com.bits.hr.domain.AttendanceSummary}.
 */
public interface AttendanceSummaryService {
    /**
     * Save a attendanceSummary.
     *
     * @param attendanceSummaryDTO the entity to save.
     * @return the persisted entity.
     */
    AttendanceSummaryDTO save(AttendanceSummaryDTO attendanceSummaryDTO);

    /**
     * Get all the attendanceSummaries.
     *
     * @return the list of entities.
     */
    List<AttendanceSummaryDTO> findAll();

    Page<AttendanceSummaryDTO> findAll(Pageable pageable);

    /**
     *
     * @param pageable
     * @param searchText
     * @return Page of AttendanceSummaryDTO
     */
    Page<AttendanceSummaryDTO> findAll(Pageable pageable, String searchText, int month, int year);

    /**
     * Get the "id" attendanceSummary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttendanceSummaryDTO> findOne(Long id);

    /**
     * Delete the "id" attendanceSummary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    //    AttendanceSummary findByYearMonthAndPin(int year, int month, String pin);

    List<AttendanceSummaryDTO> findAllByYearAndMonth(int year, int month);

    Page<AttendanceSummaryDTO> findAllByYearAndMonth(int year, int month, Pageable pageable, String searchText);

    List<AttendanceSummaryDTO> findAllByPinAndName(String searchText);

    Optional<AttendanceSummaryDTO> findByPinYearAndMonth(String pin, int month, int year);
}
