package com.bits.hr.web.rest;

import com.bits.hr.domain.RecruitmentRequisitionForm;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.RecruitmentRequisitionFormRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.RecruitmentRequisitionFormService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.ExportXL;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.bits.hr.domain.RecruitmentRequisitionForm}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class RecruitmentRequisitionFormResource {

    private final Logger log = LoggerFactory.getLogger(RecruitmentRequisitionFormResource.class);

    private static final String ENTITY_NAME = "recruitmentRequisitionForm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecruitmentRequisitionFormService recruitmentRequisitionFormService;

    @Autowired
    private RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository;

    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    public RecruitmentRequisitionFormResource(RecruitmentRequisitionFormService recruitmentRequisitionFormService, CurrentEmployeeService currentEmployeeService, EventLoggingPublisher eventLoggingPublisher) {
        this.recruitmentRequisitionFormService = recruitmentRequisitionFormService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    @PostMapping("/recruitment-requisition-forms")
    public ResponseEntity<RecruitmentRequisitionFormDTO> createRecruitmentRequisitionForm(@Valid @RequestBody RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO) throws URISyntaxException {
        log.debug("REST request to save RecruitmentRequisitionForm : {}", recruitmentRequisitionFormDTO);
        if (recruitmentRequisitionFormDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentRequisitionForm cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // validation for duplicate RRF number check
        List<RecruitmentRequisitionForm> savedRRF = recruitmentRequisitionFormRepository.findByRrfNumber(recruitmentRequisitionFormDTO.getRrfNumber());
        if (savedRRF.size()>0){
            log.error("RRF number already in use for {}", savedRRF.get(0).getId());
            throw new BadRequestAlertException("RRF Number already in use for " + savedRRF.get(0).getId()
                , "recruitmentRequisitionForm", "duplicateRRFNumber");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        recruitmentRequisitionFormDTO.setCreatedAt(Instant.now());
        recruitmentRequisitionFormDTO.setCreatedById(user.getId());
        RecruitmentRequisitionFormDTO result = recruitmentRequisitionFormService.create(recruitmentRequisitionFormDTO);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "RecruitmentRequisitionForm");
        return ResponseEntity.created(new URI("/api/recruitment-requisition-forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/recruitment-requisition-forms")
    public ResponseEntity<RecruitmentRequisitionFormDTO> updateRecruitmentRequisitionForm(@Valid @RequestBody RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO) throws URISyntaxException {
        log.debug("REST request to update RecruitmentRequisitionForm : {}", recruitmentRequisitionFormDTO);
        if (recruitmentRequisitionFormDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        recruitmentRequisitionFormDTO.setUpdatedAt(Instant.now());
        recruitmentRequisitionFormDTO.setUpdatedById(user.getId());

        RecruitmentRequisitionForm requisitionFormSaved = recruitmentRequisitionFormRepository.findById(recruitmentRequisitionFormDTO.getId()).get();

        if (requisitionFormSaved.isIsDeleted()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        if (requisitionFormSaved.getCreatedAt() != null){
            recruitmentRequisitionFormDTO.setCreatedAt(requisitionFormSaved.getCreatedAt());
        }
        if (requisitionFormSaved.getCreatedBy() != null) {
            recruitmentRequisitionFormDTO.setCreatedById(requisitionFormSaved.getCreatedBy().getId());
        }

        RecruitmentRequisitionFormDTO result = recruitmentRequisitionFormService.update(recruitmentRequisitionFormDTO);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "RecruitmentRequisitionForm");
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recruitmentRequisitionFormDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/recruitment-requisition-forms")
    public ResponseEntity<List<RecruitmentRequisitionFormDTO>> getAllRecruitmentRequisitionForms(
        @RequestParam(required = false) Long requesterId,
        @RequestParam(required = false) Long departmentId,
        @RequestParam(required = false) RequisitionStatus requisitionStatus,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of RecruitmentRequisitionForms");
        Page<RecruitmentRequisitionFormDTO> page = recruitmentRequisitionFormService.findAll(
            requesterId,
            departmentId,
            startDate,
            endDate,
            requisitionStatus,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "RecruitmentRequisitionForm");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recruitment-requisition-forms/:id} : get the "id" recruitmentRequisitionForm.
     *
     * @param id the id of the recruitmentRequisitionFormDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruitmentRequisitionFormDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recruitment-requisition-forms/{id}")
    public ResponseEntity<RecruitmentRequisitionFormDTO> getRecruitmentRequisitionForm(@PathVariable Long id) {
        log.debug("REST request to get RecruitmentRequisitionForm : {}", id);
        Optional<RecruitmentRequisitionFormDTO> recruitmentRequisitionFormDTO = recruitmentRequisitionFormService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (recruitmentRequisitionFormDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, recruitmentRequisitionFormDTO.get(), RequestMethod.GET, "RRF Config");
        }
        return ResponseUtil.wrapOrNotFound(recruitmentRequisitionFormDTO);
    }

    /**
     * {@code DELETE  /recruitment-requisition-forms/:id} : delete the "id" recruitmentRequisitionForm.
     *
     * @param id the id of the recruitmentRequisitionFormDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recruitment-requisition-forms/{id}")
    public ResponseEntity<Void> deleteRecruitmentRequisitionForm(@PathVariable Long id) {
        log.debug("REST request to delete RecruitmentRequisitionForm : {}", id);
        Optional<RecruitmentRequisitionFormDTO> recruitmentRequisitionFormDTO = recruitmentRequisitionFormService.findOne(id);

        if (!recruitmentRequisitionFormDTO.isPresent()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        if (recruitmentRequisitionFormDTO.get().isIsDeleted()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        if (recruitmentRequisitionFormDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, recruitmentRequisitionFormDTO.get(), RequestMethod.GET, "AitConfig");
        }
        recruitmentRequisitionFormService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/recruitment-requisition-forms/close-rrf/{id}")
    public ResponseEntity<RecruitmentRequisitionFormDTO> closeRRF(@PathVariable Long id){
        log.debug("REST request to close RecruitmentRequisitionForm : {}", id);

        Optional<RecruitmentRequisitionFormDTO> recruitmentRequisitionFormDTO = recruitmentRequisitionFormService.findOne(id);

        if (!recruitmentRequisitionFormDTO.isPresent()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        if (recruitmentRequisitionFormDTO.get().isIsDeleted()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        if (recruitmentRequisitionFormDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, recruitmentRequisitionFormDTO.get(), RequestMethod.PUT, "AitConfig");
        }
        RecruitmentRequisitionFormDTO result = recruitmentRequisitionFormService.closeRRF(id, user);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/recruitment-requisition-forms/close-rrf-partially/{id}/{totalOnboard}")
    public ResponseEntity<RecruitmentRequisitionFormDTO> closeRRFPartially(@PathVariable Long id, @PathVariable Integer totalOnboard){
        log.debug("REST request to close RecruitmentRequisitionForm : {}", id);

        Optional<RecruitmentRequisitionFormDTO> recruitmentRequisitionFormDTO = recruitmentRequisitionFormService.findOne(id);

        if (!recruitmentRequisitionFormDTO.isPresent()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        if (recruitmentRequisitionFormDTO.get().isIsDeleted()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        if (recruitmentRequisitionFormDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, recruitmentRequisitionFormDTO.get(), RequestMethod.PUT, "AitConfig");
        }
        RecruitmentRequisitionFormDTO result = recruitmentRequisitionFormService.closeRRFPartially(id, totalOnboard, user);
        return ResponseEntity.ok().body(result);
    }

//    @GetMapping("/recruitment-requisition-forms/change-rrf-status-from-closed-to-open")
//    public ResponseEntity<Boolean> changeRRFStatusFromClosedToOpen(){
//        log.debug("REST request to change RecruitmentRequisitionForm status from closed to open: {}");
//
//        boolean result = recruitmentRequisitionFormService.changeRRFStatusFromClosedToOpen();
//
//        return ResponseEntity.ok().body(result);
//    }


    @GetMapping("/recruitment-requisition-forms/export")
    public void rrfExportToExcel(
        HttpServletResponse response,
        @RequestParam(required = false) Long requesterId,
        @RequestParam(required = false) Long departmentId,
        @RequestParam(required = false) RequisitionStatus requisitionStatus,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        Pageable pageable
    ) throws IOException {
        ExportXLPropertiesDTO result = recruitmentRequisitionFormService.exportRRF(
            requesterId,
            departmentId,
            requisitionStatus,
            startDate,
            endDate,
            pageable
        );

        ExportXL exportXL = new ExportXL(
            result.getSheetName(),
            result.getTitleList(),
            result.getTableHeaderList(),
            result.getTableDataListOfList(),
            result.isHasAutoSummation(),
            result.getAutoSizeColumnUpTo()
        );

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, ENTITY_NAME);
        exportXL.export(response);
    }

    @GetMapping("/recruitment-requisition-forms/{id}/is-delete-allowed")
    public ResponseEntity<Boolean> isRRFAllowedToDelete(@PathVariable Long id){
        log.debug("REST request to check if RRF is allowed to be deleted");

        if(id==null){
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<User> user = currentEmployeeService.getCurrentUser();

        if(!user.isPresent()){
            throw new BadRequestAlertException("User not found!", "User", "notFound");
        }

        return new ResponseEntity<Boolean>(recruitmentRequisitionFormService.isRRFAllowedToDelete(id), HttpStatus.OK);

    }
}
