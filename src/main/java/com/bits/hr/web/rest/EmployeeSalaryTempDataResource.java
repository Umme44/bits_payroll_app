package com.bits.hr.web.rest;

import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeSalaryTempDataRepository;
import com.bits.hr.service.EmployeeSalaryTempDataService;
import com.bits.hr.service.dto.EmployeeSalaryGroupDataDTO;
import com.bits.hr.service.dto.EmployeeSalaryTempDataDTO;
import com.bits.hr.service.mapper.EmployeeSalaryTempDataMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
 * REST controller for managing {@link com.bits.hr.domain.EmployeeSalaryTempData}.
 */
@RestController
@RequestMapping("/api/payroll-mgt")
public class EmployeeSalaryTempDataResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeSalaryTempDataResource.class);

    private static final String ENTITY_NAME = "employeeSalaryTempData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeSalaryTempDataService employeeSalaryTempDataService;

    private final EmployeeSalaryTempDataRepository employeeSalaryTempDataRepository;

    private final EmployeeSalaryTempDataMapper employeeSalaryTempDataMapper;

    public EmployeeSalaryTempDataResource(
        EmployeeSalaryTempDataService employeeSalaryTempDataService,
        EmployeeSalaryTempDataRepository employeeSalaryTempDataRepository,
        EmployeeSalaryTempDataMapper employeeSalaryTempDataMapper
    ) {
        this.employeeSalaryTempDataService = employeeSalaryTempDataService;
        this.employeeSalaryTempDataRepository = employeeSalaryTempDataRepository;
        this.employeeSalaryTempDataMapper = employeeSalaryTempDataMapper;
    }

    /**
     * {@code POST  /employee-salary-temp-data} : Create a new employeeSalaryTempData.
     *
     * @param employeeSalaryTempDataDTO the employeeSalaryTempDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeSalaryTempDataDTO, or with status {@code 400 (Bad Request)} if the employeeSalaryTempData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employee-salary-temp-data")
    public ResponseEntity<EmployeeSalaryTempDataDTO> createEmployeeSalaryTempData(
        @RequestBody EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to save EmployeeSalaryTempData : {}", employeeSalaryTempDataDTO);
        if (employeeSalaryTempDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeSalaryTempData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeSalaryTempDataDTO result = employeeSalaryTempDataService.save(employeeSalaryTempDataDTO);
        return ResponseEntity
            .created(new URI("/api/employee-salary-temp-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employee-salary-temp-data} : Updates an existing employeeSalaryTempData.
     *
     * @param employeeSalaryTempDataDTO the employeeSalaryTempDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeSalaryTempDataDTO,
     * or with status {@code 400 (Bad Request)} if the employeeSalaryTempDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeSalaryTempDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employee-salary-temp-data")
    public ResponseEntity<EmployeeSalaryTempDataDTO> updateEmployeeSalaryTempData(
        @RequestBody EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeSalaryTempData : {}", employeeSalaryTempDataDTO);
        if (employeeSalaryTempDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeSalaryTempDataDTO result = employeeSalaryTempDataService.save(employeeSalaryTempDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeSalaryTempDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employee-salary-temp-data} : get all the employeeSalaryTempData.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeSalaryTempData in body.
     */
    @GetMapping("/employee-salary-temp-data")
    public ResponseEntity<List<EmployeeSalaryTempDataDTO>> getAllEmployeeSalaryTempData(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EmployeeSalaryTempData");
        Page<EmployeeSalaryTempDataDTO> page = employeeSalaryTempDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/employee-salary-temp-data/{month}/{year}")
    public ResponseEntity<List<EmployeeSalaryTempDataDTO>> getAllEmployeeSalaryTempDataByYearMonth(
        @PathVariable Month month,
        @PathVariable Integer year,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<EmployeeSalaryTempDataDTO> page = employeeSalaryTempDataService.findAllByYearMonth(year, month, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/employee-salary-temp-data/group-data")
    public List<EmployeeSalaryGroupDataDTO> getGroupData() {
        List<Object[]> resultList = employeeSalaryTempDataRepository.getListGroupByMonth();
        List<EmployeeSalaryGroupDataDTO> employeeSalaryGroupDataDTOList = new ArrayList<>();
        for (Object[] objList : resultList) {
            EmployeeSalaryGroupDataDTO employeeSalaryTempDataDTO = new EmployeeSalaryGroupDataDTO();
            String month = (String) objList[0];
            month = month.toUpperCase(Locale.ROOT).trim();
            Integer year = (Integer) objList[1];

            employeeSalaryTempDataDTO.setMonth(Month.valueOf(month));
            employeeSalaryTempDataDTO.setYear(year);
            employeeSalaryGroupDataDTOList.add(employeeSalaryTempDataDTO);
        }
        return employeeSalaryGroupDataDTOList;
    }

    /**
     * {@code GET  /employee-salary-temp-data/:id} : get the "id" employeeSalaryTempData.
     *
     * @param id the id of the employeeSalaryTempDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeSalaryTempDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employee-salary-temp-data/{id}")
    public ResponseEntity<EmployeeSalaryTempDataDTO> getEmployeeSalaryTempData(@PathVariable Long id) {
        log.debug("REST request to get EmployeeSalaryTempData : {}", id);
        Optional<EmployeeSalaryTempDataDTO> employeeSalaryTempDataDTO = employeeSalaryTempDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeSalaryTempDataDTO);
    }

    /**
     * {@code DELETE  /employee-salary-temp-data/:id} : delete the "id" employeeSalaryTempData.
     *
     * @param id the id of the employeeSalaryTempDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employee-salary-temp-data/{id}")
    public ResponseEntity<Void> deleteEmployeeSalaryTempData(@PathVariable Long id) {
        log.debug("REST request to delete EmployeeSalaryTempData : {}", id);
        employeeSalaryTempDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @DeleteMapping("/employee-salary-temp-data/group-data/{month}/{year}")
    public Boolean deleteEmployeeSalaryTempDataByYearMonth(@PathVariable Month month, @PathVariable Integer year) {
        log.debug("REST request to delete EmployeeSalaryTempData by Year and Month");
        employeeSalaryTempDataService.deleteByMonthYear(month, year);
        return true;
    }
}
