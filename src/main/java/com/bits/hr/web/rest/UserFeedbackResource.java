package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.UserFeedbackService;
import com.bits.hr.service.dto.UserFeedbackDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.UserFeedback}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class UserFeedbackResource {

    private final Logger log = LoggerFactory.getLogger(UserFeedbackResource.class);

    private static final String ENTITY_NAME = "userFeedback";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserFeedbackService userFeedbackService;

    public UserFeedbackResource(UserFeedbackService userFeedbackService) {
        this.userFeedbackService = userFeedbackService;
    }

    /**
     * {@code POST  /user-feedbacks} : Create a new userFeedback.
     *
     * @param userFeedbackDTO the userFeedbackDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userFeedbackDTO, or with status {@code 400 (Bad Request)} if the userFeedback has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-feedbacks")
    public ResponseEntity<UserFeedbackDTO> createUserFeedback(@Valid @RequestBody UserFeedbackDTO userFeedbackDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserFeedback : {}", userFeedbackDTO);
        if (userFeedbackDTO.getId() != null) {
            throw new BadRequestAlertException("A new userFeedback cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserFeedbackDTO result = userFeedbackService.save(userFeedbackDTO);
        return ResponseEntity
            .created(new URI("/api/user-feedbacks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-feedbacks} : Updates an existing userFeedback.
     *
     * @param userFeedbackDTO the userFeedbackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userFeedbackDTO,
     * or with status {@code 400 (Bad Request)} if the userFeedbackDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userFeedbackDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-feedbacks")
    public ResponseEntity<UserFeedbackDTO> updateUserFeedback(@Valid @RequestBody UserFeedbackDTO userFeedbackDTO)
        throws URISyntaxException {
        log.debug("REST request to update UserFeedback : {}", userFeedbackDTO);
        if (userFeedbackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserFeedbackDTO result = userFeedbackService.save(userFeedbackDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userFeedbackDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-feedbacks} : get all the userFeedbacks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userFeedbacks in body.
     */
    @GetMapping("/user-feedbacks")
    public ResponseEntity<List<UserFeedbackDTO>> getAllUserFeedbacks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserFeedbacks");
        Page<UserFeedbackDTO> page = userFeedbackService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-feedbacks/:id} : get the "id" userFeedback.
     *
     * @param id the id of the userFeedbackDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userFeedbackDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-feedbacks/{id}")
    public ResponseEntity<UserFeedbackDTO> getUserFeedback(@PathVariable Long id) {
        log.debug("REST request to get UserFeedback : {}", id);
        Optional<UserFeedbackDTO> userFeedbackDTO = userFeedbackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userFeedbackDTO);
    }

    /**
     * {@code DELETE  /user-feedbacks/:id} : delete the "id" userFeedback.
     *
     * @param id the id of the userFeedbackDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-feedbacks/{id}")
    public ResponseEntity<Void> deleteUserFeedback(@PathVariable Long id) {
        log.debug("REST request to delete UserFeedback : {}", id);
        userFeedbackService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
