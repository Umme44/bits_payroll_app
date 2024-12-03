package com.bits.hr.service.impl;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.EmployeeSalaryTempData;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.EmployeeSalaryTempDataRepository;
import com.bits.hr.service.EmployeeSalaryTempDataService;
import com.bits.hr.service.dto.EmployeeSalaryTempDataDTO;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import com.bits.hr.service.mapper.EmployeeSalaryTempDataMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeSalaryTempData}.
 */
@Service
@Transactional
public class EmployeeSalaryTempDataServiceImpl implements EmployeeSalaryTempDataService {

    private final Logger log = LoggerFactory.getLogger(EmployeeSalaryTempDataServiceImpl.class);

    private final EmployeeSalaryTempDataRepository employeeSalaryTempDataRepository;

    private final EmployeeSalaryRepository employeeSalaryRepository;

    private final EmployeeSalaryTempDataMapper employeeSalaryTempDataMapper;

    private final EmployeeSalaryMapper employeeSalaryMapper;

    public EmployeeSalaryTempDataServiceImpl(
        EmployeeSalaryTempDataRepository employeeSalaryTempDataRepository,
        EmployeeSalaryRepository employeeSalaryRepository,
        EmployeeSalaryTempDataMapper employeeSalaryTempDataMapper,
        EmployeeSalaryMapper employeeSalaryMapper
    ) {
        this.employeeSalaryTempDataRepository = employeeSalaryTempDataRepository;
        this.employeeSalaryRepository = employeeSalaryRepository;
        this.employeeSalaryTempDataMapper = employeeSalaryTempDataMapper;
        this.employeeSalaryMapper = employeeSalaryMapper;
    }

    @Override
    public EmployeeSalaryTempDataDTO save(EmployeeSalaryTempDataDTO employeeSalaryTempDataDTO) {
        log.debug("Request to save EmployeeSalaryTempData : {}", employeeSalaryTempDataDTO);
        EmployeeSalaryTempData employeeSalaryTempData = employeeSalaryTempDataMapper.toEntity(employeeSalaryTempDataDTO);
        employeeSalaryTempData = employeeSalaryTempDataRepository.save(employeeSalaryTempData);
        return employeeSalaryTempDataMapper.toDto(employeeSalaryTempData);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeSalaryTempDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeSalaryTempData");
        List<EmployeeSalaryTempDataDTO> employeeSalaryTempDataDTOList = employeeSalaryTempDataRepository
            .findAll(pageable)
            .map(employeeSalaryTempDataMapper::toDto)
            .getContent();

        for (EmployeeSalaryTempDataDTO salaryTempDataDTO : employeeSalaryTempDataDTOList) {
            List<EmployeeSalary> generatedSalary = employeeSalaryRepository.findByEmployeeIdAndYearAndMonth(
                salaryTempDataDTO.getEmployeeId(),
                salaryTempDataDTO.getYear(),
                salaryTempDataDTO.getMonth()
            );
            if (!generatedSalary.isEmpty()) {
                salaryTempDataDTO.setSystemGeneratedSalary(employeeSalaryMapper.toDto(generatedSalary.get(0)));
            }
        }
        Page<EmployeeSalaryTempDataDTO> page = new PageImpl<EmployeeSalaryTempDataDTO>(
            employeeSalaryTempDataDTOList,
            pageable,
            employeeSalaryTempDataDTOList.size()
        );
        return page;
    }

    @Override
    public Page<EmployeeSalaryTempDataDTO> findAllByYearMonth(Integer year, Month month, Pageable pageable) {
        log.debug("Request to get all EmployeeSalaryTempData");
        List<EmployeeSalaryTempDataDTO> employeeSalaryTempDataDTOList = employeeSalaryTempDataRepository
            .findAllByYearMonth(year, month, pageable)
            .map(employeeSalaryTempDataMapper::toDto)
            .getContent();

        for (EmployeeSalaryTempDataDTO salaryTempDataDTO : employeeSalaryTempDataDTOList) {
            List<EmployeeSalary> generatedSalary = employeeSalaryRepository.findByEmployeeIdAndYearAndMonth(
                salaryTempDataDTO.getEmployeeId(),
                salaryTempDataDTO.getYear(),
                salaryTempDataDTO.getMonth()
            );
            if (!generatedSalary.isEmpty()) {
                salaryTempDataDTO.setSystemGeneratedSalary(employeeSalaryMapper.toDto(generatedSalary.get(0)));
            }
        }
        Page<EmployeeSalaryTempDataDTO> page = new PageImpl<EmployeeSalaryTempDataDTO>(
            employeeSalaryTempDataDTOList,
            pageable,
            employeeSalaryTempDataDTOList.size()
        );
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeSalaryTempDataDTO> findOne(Long id) {
        log.debug("Request to get EmployeeSalaryTempData : {}", id);
        return employeeSalaryTempDataRepository.findById(id).map(employeeSalaryTempDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeSalaryTempData : {}", id);
        employeeSalaryTempDataRepository.deleteById(id);
    }

    @Override
    public void deleteByMonthYear(Month month, Integer year) {
        employeeSalaryTempDataRepository.deleteAllByYearAndMonth(year, month);
    }
}
