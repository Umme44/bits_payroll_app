package com.bits.hr.service.impl;

import com.bits.hr.domain.Department;
import com.bits.hr.repository.DepartmentRepository;
import com.bits.hr.service.DepartmentService;
import com.bits.hr.service.dto.DepartmentDTO;
import com.bits.hr.service.mapper.DepartmentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Department}.
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentDTO save(DepartmentDTO departmentDTO) {
        log.debug("Request to save Department : {}", departmentDTO);
        Department department = departmentMapper.toEntity(departmentDTO);
        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> findAll() {
        log.debug("Request to get all Departments");
        return departmentRepository
            .findAllOrderByName()
            .stream()
            .map(departmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get a page of Departments");
        return departmentRepository
            .findAll(pageable)
            .map(departmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentDTO> findOne(Long id) {
        log.debug("Request to get Department : {}", id);
        return departmentRepository.findById(id).map(departmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Department : {}", id);
        departmentRepository.deleteById(id);
    }
}
