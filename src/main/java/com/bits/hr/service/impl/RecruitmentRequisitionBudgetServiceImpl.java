package com.bits.hr.service.impl;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.Employee;
import com.bits.hr.service.DepartmentService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.RecruitmentRequisitionBudgetService;
import com.bits.hr.domain.RecruitmentRequisitionBudget;
import com.bits.hr.repository.RecruitmentRequisitionBudgetRepository;
import com.bits.hr.service.dto.DepartmentDTO;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;
import com.bits.hr.service.mapper.DepartmentMapper;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.RecruitmentRequisitionBudgetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link RecruitmentRequisitionBudget}.
 */
@Service
@Transactional
public class RecruitmentRequisitionBudgetServiceImpl implements RecruitmentRequisitionBudgetService {

    private final Logger log = LoggerFactory.getLogger(RecruitmentRequisitionBudgetServiceImpl.class);

    @Autowired
    private RecruitmentRequisitionBudgetRepository recruitmentRequisitionBudgetRepository;

    @Autowired
    private RecruitmentRequisitionBudgetMapper recruitmentRequisitionBudgetMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public RecruitmentRequisitionBudgetDTO save(RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO) {
        log.debug("Request to save RecruitmentRequisitionBudget : {}", recruitmentRequisitionBudgetDTO);
        if(recruitmentRequisitionBudgetDTO.getId() != null ){
            Optional<RecruitmentRequisitionBudgetDTO> existingBudget = findOne(recruitmentRequisitionBudgetDTO.getId());
            existingBudget.ifPresent(requisitionBudgetDTO -> recruitmentRequisitionBudgetDTO.setId(requisitionBudgetDTO.getId()));
        }

        RecruitmentRequisitionBudget recruitmentRequisitionBudget = recruitmentRequisitionBudgetMapper.toEntity(recruitmentRequisitionBudgetDTO);
        recruitmentRequisitionBudget = recruitmentRequisitionBudgetRepository.save(recruitmentRequisitionBudget);
        return recruitmentRequisitionBudgetMapper.toDto(recruitmentRequisitionBudget);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecruitmentRequisitionBudgetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RecruitmentRequisitionBudgets");
        return recruitmentRequisitionBudgetRepository.findAll(pageable)
            .map(recruitmentRequisitionBudgetMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RecruitmentRequisitionBudgetDTO> findOne(Long id) {
        log.debug("Request to get RecruitmentRequisitionBudget : {}", id);
        return recruitmentRequisitionBudgetRepository.findById(id)
            .map(recruitmentRequisitionBudgetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RecruitmentRequisitionBudget : {}", id);
        recruitmentRequisitionBudgetRepository.deleteById(id);
    }

    @Override
    public Optional<RecruitmentRequisitionBudgetDTO> findByEmployeeAndYearAndDepartment(RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO){
        Optional<EmployeeDTO> employee = employeeService.findOne(recruitmentRequisitionBudgetDTO.getEmployeeId());
        Optional<DepartmentDTO> department = departmentService.findOne(recruitmentRequisitionBudgetDTO.getDepartmentId());
        if(employee.isPresent() && department.isPresent()){
            Employee existingEmployee = employeeMapper.toEntity(employee.get());
            Department existingDepartment = departmentMapper.toEntity(department.get());
            Optional<RecruitmentRequisitionBudget> budget = recruitmentRequisitionBudgetRepository.findByEmployeeAndYearAndDepartment(existingEmployee, recruitmentRequisitionBudgetDTO.getYear(), existingDepartment);
            if(budget.isPresent()){
                return Optional.ofNullable(recruitmentRequisitionBudgetMapper.toDto(budget.get()));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<RecruitmentRequisitionBudgetDTO> findByEmployeeId(Long employeeId) {
        log.debug("Request to get RecruitmentRequisitionBudget by EmployeeId : {}", employeeId);
        return recruitmentRequisitionBudgetRepository.findByEmployeeId(employeeId).stream().map(recruitmentRequisitionBudgetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<RecruitmentRequisitionBudgetDTO> findByEmployeeAndYearAndDepartmentValues(Long employeeId, Long year, Long departmentId){
        log.debug("Request to get RecruitmentRequisitionBudget by EmployeeId : {}", employeeId);
        Optional<EmployeeDTO> employee = employeeService.findOne(employeeId);
        Optional<DepartmentDTO> department = departmentService.findOne(departmentId);
        if(employee.isPresent() && department.isPresent()){
            Employee existingEmployee = employeeMapper.toEntity(employee.get());
            Department existingDepartment = departmentMapper.toEntity(department.get());
            Optional<RecruitmentRequisitionBudget> budget = recruitmentRequisitionBudgetRepository.findByEmployeeAndYearAndDepartment(existingEmployee, year, existingDepartment);
            if(budget.isPresent()){
                return Optional.ofNullable(recruitmentRequisitionBudgetMapper.toDto(budget.get()));
            }
        }
        return Optional.empty();
    }
}
