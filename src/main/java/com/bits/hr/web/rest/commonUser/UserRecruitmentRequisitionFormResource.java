package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.RecruitmentRequisitionForm;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.RecruitmentRequisitionFormRepository;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.RecruitmentRequisitionFormService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import com.bits.hr.service.dto.RrfRaiseValidityDTO;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.RRFEvent;
import com.bits.hr.service.rrfUser.RecruitmentRequisitionFormUserService;
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

import javax.validation.Valid;

@RestController
@Log4j2
@RequestMapping("/api/common/rrf")
public class UserRecruitmentRequisitionFormResource {

    @Autowired
    private RecruitmentRequisitionFormUserService recruitmentRequisitionFormUserService;

    @Autowired
    private RecruitmentRequisitionFormService recruitmentRequisitionFormService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository;

    private static final String ENTITY_NAME = "recruitmentRequisitionForm";
    private static final String RESOURCE_NAME = UserRecruitmentRequisitionFormResource.class.getSimpleName();

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @GetMapping("")
    public ResponseEntity<List<RecruitmentRequisitionFormDTO>> getAll(
        @RequestParam(required = false) RequisitionStatus requisitionStatus,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        Pageable pageable) {
        Employee requester = currentEmployeeService.getCurrentEmployee().get();
        Page<RecruitmentRequisitionFormDTO> page = recruitmentRequisitionFormUserService.findAllByRequester(requester,requisitionStatus, startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "recruitmentRequisitionForm");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentRequisitionFormDTO> getById(@PathVariable Long id) {
        Employee currentEmployee = currentEmployeeService.getCurrentEmployee().get();
        Optional<RecruitmentRequisitionFormDTO> result = recruitmentRequisitionFormUserService.getById(id, currentEmployee);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "recruitmentRequisitionForm");
        return ResponseUtil.wrapOrNotFound(result);
    }

    @PostMapping("")
    public ResponseEntity<RecruitmentRequisitionFormDTO> create(@RequestBody @Valid RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO)
        throws URISyntaxException {
        log.debug("REST Request to create a Recruitment requisition");
        if (recruitmentRequisitionFormDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentRequisitionForm cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Employee employee = currentEmployeeService.getCurrentEmployee().get();

        if (employee.getEmployeeCategory() == null) {
            throw new BadRequestAlertException("RRF requester \"\"Employment Category\"\"  is null", ENTITY_NAME, "rrfRaiseLimitation");
        }else {
            if (employee.getEmployeeCategory().equals(
                EmployeeCategory.INTERN)) {
                throw new BadRequestAlertException("Oops! You cannot raise RRF", ENTITY_NAME, "rrfRaiseLimitation");
            }
        }

        User user = currentEmployeeService.getCurrentUser().get();
        recruitmentRequisitionFormDTO.setCreatedAt(Instant.now());
        recruitmentRequisitionFormDTO.setCreatedById(user.getId());
        recruitmentRequisitionFormDTO.setRequisitionStatus(RequisitionStatus.PENDING);

        RecruitmentRequisitionFormDTO result = recruitmentRequisitionFormUserService.create(recruitmentRequisitionFormDTO, employee);

        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "recruitmentRequisitionForm");
        publishEvent(result, EventType.CREATED);
        return ResponseEntity.created(new URI("/api/common/rrf" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("")
    public ResponseEntity<RecruitmentRequisitionFormDTO> update(@RequestBody @Valid RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO) {
        log.debug("REST Request to update Recruitment Requisition");

        if (recruitmentRequisitionFormDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<RecruitmentRequisitionFormDTO> previousRecruitmentRequisitionFormDTO = recruitmentRequisitionFormService.findOne(recruitmentRequisitionFormDTO.getId());

        if (!previousRecruitmentRequisitionFormDTO.isPresent()) {
            throw new BadRequestAlertException("RecruitmentRequisitionForm is not found", ENTITY_NAME, "accessForbidden");
        }

        if (previousRecruitmentRequisitionFormDTO.get().isIsDeleted()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        if (previousRecruitmentRequisitionFormDTO.get().getRequesterId() == null) {
            throw new BadRequestAlertException("EmployeeId is not found", ENTITY_NAME, "accessForbidden");
        }

        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();
        if (!employee.isPresent()){
            throw new BadRequestAlertException("No Employee Profile", ENTITY_NAME, "noEmployee");
        }

        // pre-authorize
        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()){
            throw new NoEmployeeProfileException();
        }
        if (!previousRecruitmentRequisitionFormDTO.get().getRequesterId().equals(currentEmployeeId.get())){
            throw new BadRequestAlertException("Access Forbidden", ENTITY_NAME, "accessForbidden");
        }

        // check status
        if(!previousRecruitmentRequisitionFormDTO.get().getRequisitionStatus().equals(RequisitionStatus.PENDING)){
            throw new BadRequestAlertException("Access Forbidden", ENTITY_NAME, "accessForbidden");
        }

        recruitmentRequisitionFormDTO.setRequesterId(employee.get().getId());

        if (employee.get().getEmployeeCategory().equals(
            EmployeeCategory.INTERN)) {
            throw new BadRequestAlertException("Oops! You cannot raise RRF", ENTITY_NAME, "rrfRaiseLimitation");
        }

        RequisitionStatus status = recruitmentRequisitionFormDTO.getRequisitionStatus();
        if (status != null && status != RequisitionStatus.PENDING){
            log.error("You are not authorize to update requisition status");
            throw new BadRequestAlertException("You are not authorize to update requisition status.", ENTITY_NAME, "statusUpdateNotAllowed");
        }else {
            // turn it into pending
            recruitmentRequisitionFormDTO.setRequisitionStatus(RequisitionStatus.PENDING);
        }

        User user = currentEmployeeService.getCurrentUser().get();
        recruitmentRequisitionFormDTO.setUpdatedAt(Instant.now());
        recruitmentRequisitionFormDTO.setUpdatedById(user.getId());

        RecruitmentRequisitionForm requisitionFormSaved = recruitmentRequisitionFormRepository.findById(recruitmentRequisitionFormDTO.getId()).get();

        if (requisitionFormSaved.getCreatedAt() != null){
            recruitmentRequisitionFormDTO.setCreatedAt(requisitionFormSaved.getCreatedAt());
        }
        if (requisitionFormSaved.getCreatedBy() != null) {
            recruitmentRequisitionFormDTO.setCreatedById(requisitionFormSaved.getCreatedBy().getId());
        }

        RecruitmentRequisitionFormDTO result = recruitmentRequisitionFormUserService.update(recruitmentRequisitionFormDTO).get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "recruitmentRequisitionForm");
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recruitmentRequisitionFormDTO.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST Request to delete a Recruitment Requisition by id");

        Optional<RecruitmentRequisitionFormDTO> recruitmentRequisitionFormDTO = recruitmentRequisitionFormService.findOne(id);

        // pre-authorize

        if (recruitmentRequisitionFormDTO.get().isIsDeleted()){
            throw new BadRequestAlertException("RRF not Found!", "RRF", "notFound");
        }

        Optional<Long> currentEmployeeId = currentEmployeeService.getCurrentEmployeeId();
        if (!currentEmployeeId.isPresent()){
            throw new NoEmployeeProfileException();
        }
        if (!recruitmentRequisitionFormDTO.get().getRequesterId().equals(currentEmployeeId.get())){
            throw new BadRequestAlertException("Access Forbidden", ENTITY_NAME, "accessForbidden");
        }

        User user = currentEmployeeService.getCurrentUser().get();
        if (recruitmentRequisitionFormDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, recruitmentRequisitionFormDTO.get(), RequestMethod.DELETE, "recruitmentRequisitionForm");
        }
        recruitmentRequisitionFormUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/can-raise-rrf")
    public ResponseEntity<RrfRaiseValidityDTO> canRaiseRRF(){
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()){
            return ResponseEntity.ok(new RrfRaiseValidityDTO());
        }
        Employee employee = employeeOptional.get();
        RrfRaiseValidityDTO result = recruitmentRequisitionFormUserService.canRaiseRRF(employee);
        return ResponseEntity.ok(result);
    }

    private void publishEvent(RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO, EventType event) {
        log.info("publishing email event for RRF with : " + event);
        RRFEvent rrfEvent = new RRFEvent(this, recruitmentRequisitionFormDTO, event);
        applicationEventPublisher.publishEvent(rrfEvent);
    }
}
