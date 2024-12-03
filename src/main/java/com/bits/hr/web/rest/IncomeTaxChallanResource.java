package com.bits.hr.web.rest;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.IncomeTaxChallanRepository;
import com.bits.hr.service.IncomeTaxChallanService;
import com.bits.hr.service.dto.IncomeTaxChallanDTO;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.IncomeTaxChallan}.
 */
@RestController
@RequestMapping("/api/employee-mgt/")
public class IncomeTaxChallanResource {

    private final Logger log = LoggerFactory.getLogger(IncomeTaxChallanResource.class);

    private static final String ENTITY_NAME = "incomeTaxChallan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncomeTaxChallanService incomeTaxChallanService;

    @Autowired
    private IncomeTaxChallanRepository incomeTaxChallanRepository;

    public IncomeTaxChallanResource(IncomeTaxChallanService incomeTaxChallanService) {
        this.incomeTaxChallanService = incomeTaxChallanService;
    }

    /**
     * {@code POST  /income-tax-challans} : Create a new incomeTaxChallan.
     *
     * @param incomeTaxChallanDTO the incomeTaxChallanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incomeTaxChallanDTO, or with status {@code 400 (Bad Request)} if the incomeTaxChallan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/income-tax-challans")
    public ResponseEntity<IncomeTaxChallanDTO> createIncomeTaxChallan(@Valid @RequestBody IncomeTaxChallanDTO incomeTaxChallanDTO)
        throws URISyntaxException {
        log.debug("REST request to save IncomeTaxChallan : {}", incomeTaxChallanDTO);
        if (incomeTaxChallanDTO.getId() != null) {
            throw new BadRequestAlertException("A new incomeTaxChallan cannot already have an ID", ENTITY_NAME, "idexists");
        }

        IncomeTaxChallanDTO result = incomeTaxChallanService.save(incomeTaxChallanDTO);
        return ResponseEntity
            .created(new URI("/api/income-tax-challans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /income-tax-challans} : Updates an existing incomeTaxChallan.
     *
     * @param incomeTaxChallanDTO the incomeTaxChallanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incomeTaxChallanDTO,
     * or with status {@code 400 (Bad Request)} if the incomeTaxChallanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incomeTaxChallanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/income-tax-challans")
    public ResponseEntity<IncomeTaxChallanDTO> updateIncomeTaxChallan(@Valid @RequestBody IncomeTaxChallanDTO incomeTaxChallanDTO)
        throws URISyntaxException {
        log.debug("REST request to update IncomeTaxChallan : {}", incomeTaxChallanDTO);
        if (incomeTaxChallanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IncomeTaxChallanDTO result = incomeTaxChallanService.save(incomeTaxChallanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomeTaxChallanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /income-tax-challans} : get all the incomeTaxChallans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incomeTaxChallans in body.
     */
    @GetMapping("/income-tax-challans")
    public ResponseEntity<List<IncomeTaxChallanDTO>> getAllIncomeTaxChallans(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) Long aitConfigId
    ) {
        log.debug("REST request to get a page of IncomeTaxChallans");
        Page<IncomeTaxChallanDTO> page = incomeTaxChallanService.findAll(pageable, aitConfigId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /income-tax-challans/:id} : get the "id" incomeTaxChallan.
     *
     * @param id the id of the incomeTaxChallanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incomeTaxChallanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/income-tax-challans/{id}")
    public ResponseEntity<IncomeTaxChallanDTO> getIncomeTaxChallan(@PathVariable Long id) {
        log.debug("REST request to get IncomeTaxChallan : {}", id);
        Optional<IncomeTaxChallanDTO> incomeTaxChallanDTO = incomeTaxChallanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incomeTaxChallanDTO);
    }

    /**
     * {@code DELETE  /income-tax-challans/:id} : delete the "id" incomeTaxChallan.
     *
     * @param id the id of the incomeTaxChallanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/income-tax-challans/{id}")
    public ResponseEntity<Void> deleteIncomeTaxChallan(@PathVariable Long id) {
        log.debug("REST request to delete IncomeTaxChallan : {}", id);
        incomeTaxChallanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/income-tax-challans/check-duplicate-challanNo/{challanNo}")
    public ResponseEntity<Boolean> checkDuplicateChallanNo(@PathVariable String challanNo) {
        log.debug("REST request to check duplicate TimeSlot Title : {}", challanNo);
        Boolean check = false;
        check = incomeTaxChallanRepository.checkChallanNoIsExists(challanNo);
        return ResponseEntity.ok(check);
    }
}
