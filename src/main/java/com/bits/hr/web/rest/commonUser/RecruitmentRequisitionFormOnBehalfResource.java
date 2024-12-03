package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
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
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.RRFEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@Log4j2
@RequestMapping("/api/common/rrf")
public class RecruitmentRequisitionFormOnBehalfResource {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private RecruitmentRequisitionFormService recruitmentRequisitionFormService;

    @Autowired
    private RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository;

    private final String RESOURCE_NAME = this.getClass().getSimpleName();

    private static final String ENTITY_NAME = "recruitmentRequisitionForm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    @PostMapping("/raise-on-behalf")
    public ResponseEntity<RecruitmentRequisitionFormDTO> raiseRRFOnBehalf(@RequestBody RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO) throws URISyntaxException {

        if (recruitmentRequisitionFormDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentRequisitionForm cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Employee currentEmployee = currentEmployeeService.getCurrentEmployee().get();
        User user = currentEmployeeService.getCurrentUser().get();

        boolean canRaiseRRF = currentEmployee.getCanRaiseRrfOnBehalf() != null ? currentEmployee.getCanRaiseRrfOnBehalf() : false;
        recruitmentRequisitionFormDTO.setCreatedAt(Instant.now());
        recruitmentRequisitionFormDTO.setCreatedById(user.getId());

        RecruitmentRequisitionFormDTO result;
        if (canRaiseRRF){
            result = recruitmentRequisitionFormService.create(recruitmentRequisitionFormDTO);
        }else {
            log.error("You have no permission to modify RRF");
            throw new BadRequestAlertException("You have no permission to raise on behalf RRF", "recruitmentRequisitionForm", "accessForbidden");
        }


        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "recruitmentRequisitionForm");
        publishEvent(result, EventType.CREATED, currentEmployee);
        return ResponseEntity.created(new URI("/api/common/rrf/raise-on-behalf" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/raise-on-behalf")
    public ResponseEntity<RecruitmentRequisitionFormDTO> updateRRFOnBehalf(@RequestBody RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO) throws URISyntaxException {

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

        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        boolean canRaiseRRF = employee.getCanRaiseRrfOnBehalf() != null ? employee.getCanRaiseRrfOnBehalf() : false;
        RecruitmentRequisitionFormDTO result;
        if (canRaiseRRF){
            result = recruitmentRequisitionFormService.update(recruitmentRequisitionFormDTO);
        }else {
            log.error("You have no permission to modify RRF");
            throw new BadRequestAlertException("You have no permission to modify RRF", "recruitmentRequisitionForm", "accessForbidden");
        }

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "recruitmentRequisitionForm");

        return ResponseEntity.created(new URI("/api/common/rrf/raise-on-behalf" + result.getId()))
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/raise-on-behalf")
    public ResponseEntity<List<RecruitmentRequisitionFormDTO>> getAllRecruitmentRequisitionForms(
        @RequestParam(required = false) Long requesterId,
        @RequestParam(required = false) Long departmentId,
        @RequestParam(required = false) RequisitionStatus requisitionStatus,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        Pageable pageable) {

        log.debug("REST request to get a page of RecruitmentRequisitionForms");
        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        boolean canRaiseRRF = employee.getCanRaiseRrfOnBehalf() != null ? employee.getCanRaiseRrfOnBehalf() : false;
        if (!canRaiseRRF){
            log.error("You have no permission to modify RRF");
            throw new BadRequestAlertException("You have no permission to modify RRF", "recruitmentRequisitionForm", "accessForbidden");
        }
        User user = currentEmployeeService.getCurrentUser().get();
        Page<RecruitmentRequisitionFormDTO> page = recruitmentRequisitionFormService.findAllRaisedByUser(
            requesterId,
            departmentId,
            startDate,
            endDate,
            requisitionStatus,
            pageable,
            user,
            employee);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "RecruitmentRequisitionForm");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/raise-on-behalf/{id}")
    public ResponseEntity<RecruitmentRequisitionFormDTO> getRRFOnBehalf(@PathVariable Long id) {
        log.debug("REST request to get RecruitmentRequisitionForm : {}", id);

        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        boolean canRaiseRRF = employee.getCanRaiseRrfOnBehalf() != null ? employee.getCanRaiseRrfOnBehalf() : false;
        if (!canRaiseRRF){
            log.error("You have no permission to modify RRF");
            throw new BadRequestAlertException("You have no permission to access RRF", "recruitmentRequisitionForm", "accessForbidden");
        }

        Optional<RecruitmentRequisitionFormDTO> recruitmentRequisitionFormDTO = recruitmentRequisitionFormService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();

        if (recruitmentRequisitionFormDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, recruitmentRequisitionFormDTO.get(), RequestMethod.GET, RESOURCE_NAME);
        }
        return ResponseUtil.wrapOrNotFound(recruitmentRequisitionFormDTO);
    }


    @DeleteMapping("/raise-on-behalf/{id}")
    public ResponseEntity<Void> deleteRRFOnBehalf(@PathVariable Long id) {
        log.debug("REST request to delete RecruitmentRequisitionForm : {}", id);

        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        boolean canRaiseRRF = employee.getCanRaiseRrfOnBehalf() != null ? employee.getCanRaiseRrfOnBehalf() : false;
        if (!canRaiseRRF){
            log.error("You have no permission to modify RRF");
            throw new BadRequestAlertException("You have no permission to modify RRF", "recruitmentRequisitionForm", "accessForbidden");
        }

        Optional<RecruitmentRequisitionFormDTO> recruitmentRequisitionFormDTO = recruitmentRequisitionFormService.findOne(id);

        if (!recruitmentRequisitionFormDTO.isPresent()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        if (recruitmentRequisitionFormDTO.get().isIsDeleted()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        User user = currentEmployeeService.getCurrentUser().get();

        if (recruitmentRequisitionFormDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, recruitmentRequisitionFormDTO.get(), RequestMethod.GET, RESOURCE_NAME);
        }
        recruitmentRequisitionFormService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    private void publishEvent(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, EventType event, Employee createdBy) {
        log.info("publishing email event for RRF  with : " + event);
        RRFEvent rrfEvent = new RRFEvent(this, recruitmentRequisitionFormDTO, event, true,  createdBy);
        applicationEventPublisher.publishEvent(rrfEvent);
    }
}
