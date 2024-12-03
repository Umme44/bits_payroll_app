package com.bits.hr.service;

import com.bits.hr.service.dto.UserFeedbackDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.UserFeedback}.
 */
public interface UserFeedbackService {
    /**
     * Save a userFeedback.
     *
     * @param userFeedbackDTO the entity to save.
     * @return the persisted entity.
     */
    UserFeedbackDTO save(UserFeedbackDTO userFeedbackDTO);

    /**
     * Get all the userFeedbacks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserFeedbackDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userFeedback.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserFeedbackDTO> findOne(Long id);

    /**
     * Delete the "id" userFeedback.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
