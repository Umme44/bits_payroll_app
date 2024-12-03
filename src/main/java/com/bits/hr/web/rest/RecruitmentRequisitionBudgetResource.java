package com.bits.hr.web.rest;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.RecruitmentRequisitionBudgetService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.bits.hr.domain.RecruitmentRequisitionBudget}.
 */
@RestController
@RequestMapping("/api/employee-mgt")
public class RecruitmentRequisitionBudgetResource {

    private final Logger log = LoggerFactory.getLogger(RecruitmentRequisitionBudgetResource.class);

    private static final String ENTITY_NAME = "recruitmentRequisitionBudget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecruitmentRequisitionBudgetService recruitmentRequisitionBudgetService;
    private final CurrentEmployeeService currentEmployeeService;

    public RecruitmentRequisitionBudgetResource(RecruitmentRequisitionBudgetService recruitmentRequisitionBudgetService, CurrentEmployeeService currentEmployeeService) {
        this.recruitmentRequisitionBudgetService = recruitmentRequisitionBudgetService;
        this.currentEmployeeService = currentEmployeeService;
    }

    @PostMapping("/recruitment-requisition-budgets")
    public ResponseEntity<RecruitmentRequisitionBudgetDTO> createRecruitmentRequisitionBudget(@Valid @RequestBody RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO) throws URISyntaxException {
        log.debug("REST request to save RecruitmentRequisitionBudget : {}", recruitmentRequisitionBudgetDTO);
        if (recruitmentRequisitionBudgetDTO.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentRequisitionBudget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<RecruitmentRequisitionBudgetDTO> existingBudget = recruitmentRequisitionBudgetService.findByEmployeeAndYearAndDepartment(recruitmentRequisitionBudgetDTO);
        if(existingBudget.isPresent()){
            throw new BadRequestAlertException("Budget already allocated!", ENTITY_NAME, "yearAndBudgetExists");
        }

        RecruitmentRequisitionBudgetDTO result = recruitmentRequisitionBudgetService.save(recruitmentRequisitionBudgetDTO);
        return ResponseEntity.created(new URI("/api/recruitment-requisition-budgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/recruitment-requisition-budgets")
    public ResponseEntity<RecruitmentRequisitionBudgetDTO> updateRecruitmentRequisitionBudget(@Valid @RequestBody RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO) throws URISyntaxException {
        log.debug("REST request to update RecruitmentRequisitionBudget : {}", recruitmentRequisitionBudgetDTO);
        if (recruitmentRequisitionBudgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional<RecruitmentRequisitionBudgetDTO> existingBudget = recruitmentRequisitionBudgetService.findByEmployeeAndYearAndDepartment(recruitmentRequisitionBudgetDTO);
        if(existingBudget.isPresent()
            && Objects.equals(existingBudget.get().getDepartmentId(), recruitmentRequisitionBudgetDTO.getDepartmentId())
            && Objects.equals(existingBudget.get().getEmployeeId(), recruitmentRequisitionBudgetDTO.getEmployeeId())
            && Objects.equals(existingBudget.get().getYear(), recruitmentRequisitionBudgetDTO.getYear())){
            if(!Objects.equals(existingBudget.get().getId(), recruitmentRequisitionBudgetDTO.getId())){
                throw new BadRequestAlertException("Budget already allocated!", ENTITY_NAME, "yearAndBudgetExists");
            }
        }
        RecruitmentRequisitionBudgetDTO result = recruitmentRequisitionBudgetService.save(recruitmentRequisitionBudgetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recruitmentRequisitionBudgetDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/recruitment-requisition-budgets")
    public ResponseEntity<List<RecruitmentRequisitionBudgetDTO>> getAllRecruitmentRequisitionBudgets(Pageable pageable) {
        log.debug("REST request to get a page of RecruitmentRequisitionBudgets");
        Page<RecruitmentRequisitionBudgetDTO> page = recruitmentRequisitionBudgetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/recruitment-requisition-budgets/{id}")
    public ResponseEntity<RecruitmentRequisitionBudgetDTO> getRecruitmentRequisitionBudget(@PathVariable Long id) {
        log.debug("REST request to get RecruitmentRequisitionBudget : {}", id);
        Optional<RecruitmentRequisitionBudgetDTO> recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recruitmentRequisitionBudgetDTO);
    }

//    @GetMapping("/recruitment-requisition-budgets/{employeeId}")
//    public ResponseEntity<List<RecruitmentRequisitionBudgetDTO>> getRecruitmentRequisitionBudgetByEmployeeId(@PathVariable(name = "employeeId") Long employeeId) {
//        log.debug("REST request to get RecruitmentRequisitionBudget by Employee Id : {}", employeeId);
//        List<RecruitmentRequisitionBudgetDTO> recruitmentRequisitionBudgetDTOList = recruitmentRequisitionBudgetService.findByEmployeeId(employeeId);
//        if(recruitmentRequisitionBudgetDTOList.isEmpty() ){
//            return ResponseEntity.notFound().build();
//        }
//        else{
//            return ResponseEntity.ok().body(recruitmentRequisitionBudgetDTOList);
//        }
//    }

    @DeleteMapping("/recruitment-requisition-budgets/{id}")
    public ResponseEntity<Void> deleteRecruitmentRequisitionBudget(@PathVariable Long id) {
        log.debug("REST request to delete RecruitmentRequisitionBudget : {}", id);
        recruitmentRequisitionBudgetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
