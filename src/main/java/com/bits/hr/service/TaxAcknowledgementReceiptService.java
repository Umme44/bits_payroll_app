package com.bits.hr.service;

import com.bits.hr.domain.User;
import com.bits.hr.service.dto.TaxAcknowledgementReceiptDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.TaxAcknowledgementReceipt}.
 */
public interface TaxAcknowledgementReceiptService {
    /**
     * Save a taxAcknowledgementReceipt.
     *
     * @param taxAcknowledgementReceiptDTO the entity to save.
     * @return the persisted entity.
     */
    TaxAcknowledgementReceiptDTO save(TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO);

    /**
     * Get all the taxAcknowledgementReceipts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxAcknowledgementReceiptDTO> findAll(Pageable pageable);

    /**
     * Get the "id" taxAcknowledgementReceipt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaxAcknowledgementReceiptDTO> findOne(Long id);

    Optional<TaxAcknowledgementReceiptDTO> findOneByEmployeeId(long id, long employeeId);

    /**
     * Delete the "id" taxAcknowledgementReceipt.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void changeStatusIntoReceived(User receivedBy, Instant receivedAt, List<Long> listOfIds) throws Exception;

    Page<TaxAcknowledgementReceiptDTO> findAllByEmployeeId(Pageable pageable, long employeeId);

    Page<TaxAcknowledgementReceiptDTO> findAllReceivedByFiscalYearId(Long aitConfigId, Long employeeId, Pageable pageable);

    Page<TaxAcknowledgementReceiptDTO> findAllSubmittedByFiscalYearId(Long aitConfigId, Long employeeId, Pageable pageable);
}
