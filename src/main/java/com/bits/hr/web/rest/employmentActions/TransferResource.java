package com.bits.hr.web.rest.employmentActions;

import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.EmploymentHistoryService;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import com.bits.hr.service.employmentHistory.TransferHistoryService;
import com.bits.hr.service.importXL.batchOperations.employmentActions.BatchOperationsViaXlsx;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * REST controller for managing increment operations
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class TransferResource {

    private static final String ENTITY_NAME = "transfer";
    private final Logger log = LoggerFactory.getLogger(com.bits.hr.web.rest.EmploymentHistoryResource.class);
    private final EmploymentHistoryService employmentHistoryService;
    private final BatchOperationsViaXlsx batchOperationsViaXlsx;
    private final TransferHistoryService transferHistoryService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public TransferResource(
        EmploymentHistoryService employmentHistoryService,
        @Qualifier("Transfer") BatchOperationsViaXlsx batchOperationsViaXlsx,
        TransferHistoryService transferHistoryService
    ) {
        this.employmentHistoryService = employmentHistoryService;
        this.batchOperationsViaXlsx = batchOperationsViaXlsx;
        this.transferHistoryService = transferHistoryService;
    }

    /**
     * {@code POST  /increment} : Create a new promotion actions.
     * employmentHistory emtity will keep track of every employment actions.
     *
     * @param employmentHistoryDTO the employmentHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employmentHistoryDTO, or with status {@code 400 (Bad Request)} if the employmentHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transfer")
    public ResponseEntity<EmploymentHistoryDTO> createEmploymentHistory(@RequestBody EmploymentHistoryDTO employmentHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to create new increment");
        if (employmentHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new employmentHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }

        EmploymentHistoryDTO result = transferHistoryService.createTransfer(employmentHistoryDTO);

        return ResponseEntity
            .created(new URI("/api/employee-mgt/increment" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employment-histories} : Updates an existing employmentHistory.
     *
     * @param employmentHistoryDTO the employmentHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the employmentHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employmentHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transfer")
    public ResponseEntity<EmploymentHistoryDTO> updateEmploymentHistory(@RequestBody EmploymentHistoryDTO employmentHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to update EmploymentHistory : {}", employmentHistoryDTO);
        if (employmentHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        EmploymentHistoryDTO result = transferHistoryService.updateTransfer(employmentHistoryDTO);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code DELETE  /employment-histories/:id} : delete the "id" employmentHistory.
     *
     * @param id the id of the employmentHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transfer/{id}")
    public ResponseEntity<Void> deleteEmploymentHistory(@PathVariable Long id) {
        log.debug("REST request to delete EmploymentHistory : {}", id);
        transferHistoryService.deleteTransfer(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/transfer")
    public ResponseEntity<List<EmploymentHistoryDTO>> getAllTransfers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get all transfers");
        Page<EmploymentHistoryDTO> page = employmentHistoryService.findAll(EventType.TRANSFER, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employment-histories/:id} : get the "id" employmentHistory.
     *
     * @param id the id of the employmentHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employmentHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transfer/{id}")
    public ResponseEntity<EmploymentHistoryDTO> getEmploymentHistory(@PathVariable Long id) {
        log.debug("REST request to get transfer : {}", id);
        Optional<EmploymentHistoryDTO> employmentHistoryDTO = employmentHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employmentHistoryDTO);
    }

    @PostMapping("/batch-transfer")
    public ResponseEntity<Boolean> upload(@RequestParam("file") MultipartFile file) throws Exception {
        boolean hasDone = batchOperationsViaXlsx.batchOperations(file);
        return ResponseEntity.ok(hasDone);
    }

    @GetMapping("/transfer-search/{id}")
    public List<EmploymentHistoryDTO> getAllTransferHistoriesByEmployee(@PathVariable Long id) {
        log.debug("REST request to get all promotions");
        return transferHistoryService.getAllTransferHistoriesByEmployee(id);
    }

    @GetMapping("/transfer-search/{id}/{fromdate}/{todate}")
    public List<EmploymentHistoryDTO> getAllTransferHistoriesByEmployeeBetweenTwoDates(
        @PathVariable Long id,
        @PathVariable LocalDate fromdate,
        @PathVariable LocalDate todate
    ) {
        log.debug("REST request to get all promotions");
        return transferHistoryService.getAllTransferHistoriesByEmployeeBetweenTwoDates(id, fromdate, todate);
    }

    @GetMapping("/transfer-search/{fromdate}/{todate}")
    public List<EmploymentHistoryDTO> getAllTransferHistoriesBetweenTwoDates(
        @PathVariable LocalDate fromdate,
        @PathVariable LocalDate todate
    ) {
        log.debug("REST request to get all promotions");
        return transferHistoryService.getAllTransferHistoriesBetweenTwoDates(fromdate, todate);
    }
}
