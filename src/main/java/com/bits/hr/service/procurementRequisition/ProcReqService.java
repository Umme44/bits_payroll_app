package com.bits.hr.service.procurementRequisition;

import com.bits.hr.domain.ProcReq;
import com.bits.hr.service.dto.ProcReqDTO;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.ProcReq}.
 */
public interface ProcReqService {
    /**
     * Save a procReq.
     *
     * @param procReqDTO the entity to save.
     * @return the persisted entity.
     */
    ProcReqDTO save(ProcReqDTO procReqDTO);

    /**
     * Get all the procReqs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcReqDTO> findAll(Pageable pageable);

    /**
     * Get the "id" procReq.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcReqDTO> findOne(Long id);

    /**
     * Delete the "id" procReq.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ProcReq> saveAll(Collection<ProcReqDTO> procReqs, long id);

    void deleteAllProcReqByMasterId(long procReqMasterId, boolean willDeleteFiles);

    void updateFiles(Collection<ProcReqDTO> procReqDTOList);
}
