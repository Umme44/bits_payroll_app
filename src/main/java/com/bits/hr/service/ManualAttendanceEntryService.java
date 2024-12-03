package com.bits.hr.service;

import com.bits.hr.domain.ManualAttendanceEntry;
import com.bits.hr.service.dto.ManualAttendanceEntryDTO;
import com.bits.hr.service.search.FilterDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ManualAttendanceEntry}.
 */
public interface ManualAttendanceEntryService {
    ManualAttendanceEntryDTO save(ManualAttendanceEntryDTO manualAttendanceEntryDTO);

    List<ManualAttendanceEntryDTO> findAllPendingManualAttendance(Long employeeId, LocalDate startDate, LocalDate endDate);

    /**
     * Save a manualAttendanceEntry.
     *
     * @param manualAttendanceEntryDTO the entity to save.
     * @return the persisted entity.
     */
    ManualAttendanceEntryDTO create(ManualAttendanceEntryDTO manualAttendanceEntryDTO);

    ManualAttendanceEntryDTO update(ManualAttendanceEntryDTO manualAttendanceEntryDTO);

    /**
     * Get all the manualAttendanceEntries.
     *
     * @param pageable the pagination information.
     * @param employeeId find for employeeId
     * @return the list of entities.
     */
    Page<ManualAttendanceEntryDTO> findAllDTOs(Pageable pageable, long employeeId);

    List<ManualAttendanceEntryDTO> findAllDTOs();

    List<ManualAttendanceEntry> findAll();

    Page<ManualAttendanceEntryDTO> findAll(Pageable pageable);
    /**
     * Get the "id" manualAttendanceEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManualAttendanceEntryDTO> findOne(Long id);

    /**
     * Delete the "id" manualAttendanceEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean checkExitingAttendanceEntry(ManualAttendanceEntryDTO manualAttendanceEntry);

    List<ManualAttendanceEntryDTO> findAllByPinAndDateRange(String pin, FilterDto filterDto);

    Page<ManualAttendanceEntryDTO> findAllOfCommonUser(Pageable pageable);

    Boolean findDuplicateEntryForDate(long employeeId, LocalDate date);

    ManualAttendanceEntryDTO applyAndApproveByHR(ManualAttendanceEntryDTO manualAttendanceEntryDTO);
}
