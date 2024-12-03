package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.MobileBillService;
import com.bits.hr.service.dto.MobileBillDTO;
import com.bits.hr.service.importXL.XlsxImportService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.MobileBill}.
 */
@RestController
@RequestMapping("/api/payroll-mgt/mobile-bills")
@RequiredArgsConstructor
public class MobileBillResource {

    private static final String ENTITY_NAME = "mobileBill";
    private final Logger log = LoggerFactory.getLogger(MobileBillResource.class);
    private final MobileBillService mobileBillService;
    private final XlsxImportService mobileBillImportServiceImpl;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST  /mobile-bills} : Create a new mobileBill.
     *
     * @param mobileBillDTO the mobileBillDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mobileBillDTO, or with status {@code 400 (Bad Request)} if the mobileBill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MobileBillDTO> createMobileBill(@RequestBody MobileBillDTO mobileBillDTO) throws URISyntaxException {
        log.debug("REST request to save MobileBill : {}", mobileBillDTO);
        if (mobileBillDTO.getId() != null) {
            throw new BadRequestAlertException("A new mobileBill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MobileBillDTO result = mobileBillService.save(mobileBillDTO);
        return ResponseEntity
            .created(new URI("/api/payroll-mgt/mobile-bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mobile-bills} : Updates an existing mobileBill.
     *
     * @param mobileBillDTO the mobileBillDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mobileBillDTO,
     * or with status {@code 400 (Bad Request)} if the mobileBillDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mobileBillDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<MobileBillDTO> updateMobileBill(@RequestBody MobileBillDTO mobileBillDTO) throws URISyntaxException {
        log.debug("REST request to update MobileBill : {}", mobileBillDTO);
        if (mobileBillDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MobileBillDTO result = mobileBillService.save(mobileBillDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mobileBillDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mobile-bills} : get all the mobileBills.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mobileBills in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MobileBillDTO>> getAllMobileBills(
        @RequestParam(name = "searchText", required = false) String searchText,
        @RequestParam(required = false) int month,
        @RequestParam(required = false) int year,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all MobileBills");
        Page<MobileBillDTO> result = mobileBillService.findAll(pageable, searchText, month, year);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);

        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    /**
     * {@code GET  /mobile-bills/:id} : get the "id" mobileBill.
     *
     * @param id the id of the mobileBillDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mobileBillDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MobileBillDTO> getMobileBill(@PathVariable Long id) {
        log.debug("REST request to get MobileBill : {}", id);
        Optional<MobileBillDTO> mobileBillDTO = mobileBillService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mobileBillDTO);
    }

    /**
     * {@code DELETE  /mobile-bills/:id} : delete the "id" mobileBill.
     *
     * @param id the id of the mobileBillDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMobileBill(@PathVariable Long id) {
        log.debug("REST request to delete MobileBill : {}", id);
        mobileBillService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/xlsx-upload/{year}/{month}")
    public ResponseEntity<Boolean> uploadMobileBill(
        @PathVariable int year,
        @PathVariable int month,
        @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {
        return ResponseEntity.ok(mobileBillImportServiceImpl.importFile(file, year, month));
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<MobileBillDTO>> getAllMobileBillsByYearAndMonth(
        @RequestParam(name = "searchText", required = false) String searchText,
        @PathVariable int year,
        @PathVariable int month,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all MobileBills By year and month");
        Page<MobileBillDTO> page = mobileBillService.findAllByYearAndMonth(year, month, pageable, searchText);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
