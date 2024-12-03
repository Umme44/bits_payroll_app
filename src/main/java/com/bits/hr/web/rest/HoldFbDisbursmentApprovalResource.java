package com.bits.hr.web.rest;

import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.HoldFbDisbursement;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.FestivalBonusDetailsService;
import com.bits.hr.service.HoldFbDisbursementService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import com.bits.hr.service.dto.HoldFbDisbursementApprovalDTO;
import com.bits.hr.service.dto.HoldFbDisbursementDTO;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.HoldFbDisbursement}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class HoldFbDisbursmentApprovalResource {

    private final Logger log = LoggerFactory.getLogger(HoldFbDisbursementResource.class);

    private static final String ENTITY_NAME = "holdFbDisbursement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HoldFbDisbursementService holdFbDisbursementService;

    private final FestivalBonusDetailsService festivalBonusDetailsService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    public HoldFbDisbursmentApprovalResource(
        HoldFbDisbursementService holdFbDisbursementService,
        FestivalBonusDetailsService festivalBonusDetailsService
    ) {
        this.holdFbDisbursementService = holdFbDisbursementService;
        this.festivalBonusDetailsService = festivalBonusDetailsService;
    }

    /**
     * {@code POST  /hold-festivalBonus-disbursements} : Create a new holdFbDisbursement.
     *
     * @param holdFbDisbursementApprovalDTO the holdFbDisbursementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new holdFbDisbursementDTO, or with status {@code 400 (Bad Request)} if the holdFbDisbursement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hold-festivalBonus-disbursements")
    public ResponseEntity<List<HoldFbDisbursementDTO>> createHoldFbDisbursement(
        @Valid @RequestBody HoldFbDisbursementApprovalDTO holdFbDisbursementApprovalDTO
    ) throws URISyntaxException {
        List<HoldFbDisbursementDTO> holdFbDisbursementDTOList = holdFbDisbursementService.disburseHoldFestivalBonus(
            holdFbDisbursementApprovalDTO
        );

        return ResponseEntity.ok().body(holdFbDisbursementDTOList);
    }

    /**
     * {@code PUT  /hold-festivalBonus-disbursements} : Updates an existing holdFbDisbursement.
     *
     * @param holdFbDisbursementDTO the holdFbDisbursementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holdFbDisbursementDTO,
     * or with status {@code 400 (Bad Request)} if the holdFbDisbursementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the holdFbDisbursementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hold-festivalBonus-disbursements")
    public ResponseEntity<HoldFbDisbursementDTO> updateHoldFbDisbursement(@Valid @RequestBody HoldFbDisbursementDTO holdFbDisbursementDTO)
        throws URISyntaxException {
        log.debug("REST request to update HoldFbDisbursement : {}", holdFbDisbursementDTO);
        if (holdFbDisbursementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HoldFbDisbursementDTO result = holdFbDisbursementService.save(holdFbDisbursementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, holdFbDisbursementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hold-festivalBonus-disbursements} : get all the holdFbDisbursements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of holdFbDisbursements in body.
     */
    @GetMapping("/hold-festivalBonus-disbursements")
    public ResponseEntity<List<HoldFbDisbursementDTO>> getAllHoldFbDisbursements(
        @RequestParam(required = false) String searchText,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of HoldFbDisbursements");
        Page<HoldFbDisbursementDTO> page = holdFbDisbursementService.findAll(searchText, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hold-festivalBonus-disbursements/:id} : get the "id" holdFbDisbursement.
     *
     * @param id the id of the holdFbDisbursementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the holdFbDisbursementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hold-festivalBonus-disbursements/{id}")
    public ResponseEntity<HoldFbDisbursementDTO> getHoldFbDisbursement(@PathVariable Long id) {
        log.debug("REST request to get HoldFbDisbursement : {}", id);
        Optional<HoldFbDisbursementDTO> holdFbDisbursementDTO = holdFbDisbursementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(holdFbDisbursementDTO);
    }

    /**
     * {@code DELETE  /hold-festivalBonus-disbursements/:id} : delete the "id" holdFbDisbursement.
     *
     * @param id the id of the holdFbDisbursementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hold-festivalBonus-disbursements/{id}")
    public ResponseEntity<Void> deleteHoldFbDisbursement(@PathVariable Long id) {
        log.debug("REST request to delete HoldFbDisbursement : {}", id);
        holdFbDisbursementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
