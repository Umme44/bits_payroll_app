package com.bits.hr.web.rest;

import com.bits.hr.service.RecruitmentRequisitionBudgetService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
@Slf4j
public class RecruitmentRequisitionBudgetCommonResource {

    private final CurrentEmployeeService currentEmployeeService;
    private final RecruitmentRequisitionBudgetService recruitmentRequisitionBudgetService;

    @GetMapping("/recruitment-requisition-budgets")
    public ResponseEntity<?> findByLoggedInUserId(){
        log.debug("REST request to get budget by current logged in user");
        if(currentEmployeeService.getCurrentEmployeeId().isPresent()){
            Long currentEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
            List<RecruitmentRequisitionBudgetDTO> budgetList = recruitmentRequisitionBudgetService.findByEmployeeId(currentEmployeeId);

            if(budgetList.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            else return ResponseEntity.ok().body(budgetList);
        }
        else return ResponseEntity.badRequest().body("Current user not found!");
    }
}

