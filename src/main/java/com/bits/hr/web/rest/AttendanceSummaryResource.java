package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.AttendanceSummaryService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.dto.AttendanceSummaryDTO;
import com.bits.hr.service.importXL.payroll.LeaveAttandanceImportService;
import com.bits.hr.service.search.AttendanceSummaryFilterDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
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
 * REST controller for managing {@link com.bits.hr.domain.AttendanceSummary}.
 */
@RestController
@RequestMapping("/api/attendance-mgt/attendance-summaries")
@RequiredArgsConstructor
public class AttendanceSummaryResource {

    private static final String ENTITY_NAME = "attendanceSummary";
    private final Logger log = LoggerFactory.getLogger(AttendanceSummaryResource.class);
    private final AttendanceSummaryService attendanceSummaryService;
    private final LeaveAttandanceImportService leaveAttandanceImportService;
    private final EmployeeRepository employeeRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST  /attendance-summaries} : Create a new attendanceSummary.
     *
     * @param attendanceSummaryDTO the attendanceSummaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attendanceSummaryDTO, or with status {@code 400 (Bad Request)} if the attendanceSummary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AttendanceSummaryDTO> createAttendanceSummary(@Valid @RequestBody AttendanceSummaryDTO attendanceSummaryDTO)
        throws URISyntaxException {
        log.debug("REST request to save AttendanceSummary : {}", attendanceSummaryDTO);
        if (attendanceSummaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new attendanceSummary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String pin = employeeRepository.findById(attendanceSummaryDTO.getEmployeeId()).get().getPin();
        Optional<AttendanceSummaryDTO> attendanceSummaryDTO1 = attendanceSummaryService.findByPinYearAndMonth(
            pin,
            attendanceSummaryDTO.getMonth(),
            attendanceSummaryDTO.getYear()
        );
        AttendanceSummaryDTO result;
        /*if exists then update with new values*/
        if (attendanceSummaryDTO1.isPresent()) {
            //TODO: this update process move to a method
            attendanceSummaryDTO1.get().setTotalWorkingDays(attendanceSummaryDTO.getTotalWorkingDays());
            attendanceSummaryDTO1.get().setTotalLeaveDays(attendanceSummaryDTO.getTotalLeaveDays());
            attendanceSummaryDTO1.get().setTotalAbsentDays(attendanceSummaryDTO.getTotalAbsentDays());
            attendanceSummaryDTO1.get().setTotalFractionDays(attendanceSummaryDTO.getTotalFractionDays());
            result = attendanceSummaryService.save(attendanceSummaryDTO1.get());
        } else {
            result = attendanceSummaryService.save(attendanceSummaryDTO);
        }
        return ResponseEntity
            .created(new URI("/api/attendance-mgt/attendance-summaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attendance-summaries} : Updates an existing attendanceSummary.
     *
     * @param attendanceSummaryDTO the attendanceSummaryDTO to update.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @retuturn the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendanceSummaryDTO,
     * or with status {@code 400 (Bad Request)} if the attendanceSummaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendanceSummaryDTO couldn't be updated.
     */
    @PutMapping("")
    public ResponseEntity<AttendanceSummaryDTO> updateAttendanceSummary(@RequestBody AttendanceSummaryDTO attendanceSummaryDTO)
        throws URISyntaxException {
        log.debug("REST request to update AttendanceSummary : {}", attendanceSummaryDTO);
        if (attendanceSummaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttendanceSummaryDTO result = attendanceSummaryService.save(attendanceSummaryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendanceSummaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attendance-summaries} : get all the attendanceSummaries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attendanceSummaries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AttendanceSummaryDTO>> getAllAttendanceSummariesWithSearch(
        @RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
        @RequestParam(required = false, defaultValue = "0") int month,
        @RequestParam(required = false, defaultValue = "0") int year,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AttendanceSummaries" + "with searchText=" + searchText);
        Page<AttendanceSummaryDTO> page = attendanceSummaryService.findAll(pageable, searchText, month, year);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attendance-summaries/:id} : get the "id" attendanceSummary.
     *
     * @param id the id of the attendanceSummaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attendanceSummaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceSummaryDTO> getAttendanceSummary(@PathVariable Long id) {
        log.debug("REST request to get AttendanceSummary : {}", id);
        Optional<AttendanceSummaryDTO> attendanceSummaryDTO = attendanceSummaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attendanceSummaryDTO);
    }

    /**
     * {@code DELETE  /attendance-summaries/:id} : delete the "id" attendanceSummary.
     *
     * @param id the id of the attendanceSummaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendanceSummary(@PathVariable Long id) {
        log.debug("REST request to delete AttendanceSummary : {}", id);
        attendanceSummaryService.delete(id);
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
        return ResponseEntity.ok(leaveAttandanceImportService.importFile(file, year, month));
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<AttendanceSummaryDTO>> getAllAttendanceSummaries(
        @RequestParam(name = "searchText", required = false) String searchText,
        @PathVariable int year,
        @PathVariable int month,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all AttendanceSummaries by year and month");
        Page<AttendanceSummaryDTO> page = attendanceSummaryService.findAllByYearAndMonth(year, month, pageable, searchText);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/attendanceSummarySearch")
    public List<AttendanceSummaryDTO> searchAttendanceSummary(@RequestBody AttendanceSummaryFilterDto filterDto) throws Exception {
        log.debug("REST request to get all AttendanceSummaries");
        return attendanceSummaryService.findAllByPinAndName(filterDto.getText());
    }
}
