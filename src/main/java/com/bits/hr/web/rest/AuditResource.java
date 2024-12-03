//package com.bits.hr.web.rest;
//
//import com.bits.hr.domain.User;
//import com.bits.hr.domain.enumeration.RequestMethod;
//import com.bits.hr.service.AuditEventService;
//import com.bits.hr.service.EventLog.EventLoggingPublisher;
//import com.bits.hr.service.config.CurrentEmployeeService;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.List;
//import java.util.Optional;
//import org.springframework.boot.actuate.audit.AuditEvent;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//import tech.jhipster.web.util.PaginationUtil;
//import tech.jhipster.web.util.ResponseUtil;
//
///**
// * REST controller for getting the {@link AuditEvent}s.
// */
//@RestController
//@RequestMapping("/management/audits")
//public class AuditResource {
//
//    private final AuditEventService auditEventService;
//
//    private final CurrentEmployeeService currentEmployeeService;
//    private final EventLoggingPublisher eventLoggingPublisher;
//
//    public AuditResource(
//        AuditEventService auditEventService,
//        CurrentEmployeeService currentEmployeeService,
//        EventLoggingPublisher eventLoggingPublisher
//    ) {
//        this.auditEventService = auditEventService;
//        this.currentEmployeeService = currentEmployeeService;
//        this.eventLoggingPublisher = eventLoggingPublisher;
//    }
//
//    /**
//     * {@code GET /audits} : get a page of {@link AuditEvent}s.
//     *
//     * @param pageable the pagination information.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of {@link AuditEvent}s in body.
//     */
//    @GetMapping
//    public ResponseEntity<List<AuditEvent>> getAll(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
//        Page<AuditEvent> page = auditEventService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//
//        User user = currentEmployeeService.getCurrentUser().get();
//        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "Audit");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }
//
//    /**
//     * {@code GET  /audits} : get a page of {@link AuditEvent} between the {@code fromDate} and {@code toDate}.
//     *
//     * @param fromDate the start of the time period of {@link AuditEvent} to get.
//     * @param toDate   the end of the time period of {@link AuditEvent} to get.
//     * @param pageable the pagination information.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of {@link AuditEvent} in body.
//     */
//    @GetMapping(params = { "fromDate", "toDate" })
//    public ResponseEntity<List<AuditEvent>> getByDates(
//        @RequestParam(value = "fromDate") LocalDate fromDate,
//        @RequestParam(value = "toDate") LocalDate toDate,
//        @org.springdoc.api.annotations.ParameterObject Pageable pageable
//    ) {
//        Instant from = fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
//        Instant to = toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant();
//
//        Page<AuditEvent> page = auditEventService.findByDates(from, to, pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }
//
//    /**
//     * {@code GET  /audits/:id} : get an {@link AuditEvent} by id.
//     *
//     * @param id the id of the entity to get.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the {@link AuditEvent} in body, or status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/{id:.+}")
//    public ResponseEntity<AuditEvent> get(@PathVariable Long id) {
//        return ResponseUtil.wrapOrNotFound(auditEventService.find(id));
//    }
//}
