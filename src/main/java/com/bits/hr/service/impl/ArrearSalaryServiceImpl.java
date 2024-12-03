package com.bits.hr.service.impl;

import com.bits.hr.domain.ArrearSalary;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.ArrearSalaryRepository;
import com.bits.hr.service.ArrearSalaryService;
import com.bits.hr.service.dto.ArrearSalaryDTO;
import com.bits.hr.service.mapper.ArrearSalaryMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ArrearSalary}.
 */
@Service
@Transactional
public class ArrearSalaryServiceImpl implements ArrearSalaryService {

    private final Logger log = LoggerFactory.getLogger(ArrearSalaryServiceImpl.class);

    private final ArrearSalaryRepository arrearSalaryRepository;

    private final ArrearSalaryMapper arrearSalaryMapper;

    public ArrearSalaryServiceImpl(ArrearSalaryRepository arrearSalaryRepository, ArrearSalaryMapper arrearSalaryMapper) {
        this.arrearSalaryRepository = arrearSalaryRepository;
        this.arrearSalaryMapper = arrearSalaryMapper;
    }

    @Override
    public ArrearSalaryDTO save(ArrearSalaryDTO arrearSalaryDTO) {
        log.debug("Request to save ArrearSalary : {}", arrearSalaryDTO);
        ArrearSalary arrearSalary = arrearSalaryMapper.toEntity(arrearSalaryDTO);

        List<ArrearSalary> arrearSalaryList = arrearSalaryRepository.findByEmployeeIdAndYearAndMonth(
            arrearSalary.getEmployee().getId(),
            arrearSalary.getYear(),
            arrearSalary.getMonth()
        );

        // not empty ==> update
        if (!arrearSalaryList.isEmpty()) {
            ArrearSalary arrearSalaryPrevious = arrearSalaryList.get(0);
            arrearSalaryPrevious.setAmount(arrearSalary.getAmount());
            arrearSalary = arrearSalaryRepository.save(arrearSalaryPrevious);
        }
        // else save
        else {
            arrearSalary = arrearSalaryRepository.save(arrearSalary);
        }
        return arrearSalaryMapper.toDto(arrearSalary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArrearSalaryDTO> findAll(String searchText, int month, int year, Pageable pageable) {
        log.debug("Request to get all ArrearSalaries");
        if (month == 0) {
            return arrearSalaryRepository.findAll(searchText, null, year, pageable).map(arrearSalaryMapper::toDto);
        }
        return arrearSalaryRepository.findAll(searchText, Month.fromInteger(month), year, pageable).map(arrearSalaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArrearSalaryDTO> findOne(Long id) {
        log.debug("Request to get ArrearSalary : {}", id);
        return arrearSalaryRepository.findById(id).map(arrearSalaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArrearSalary : {}", id);
        arrearSalaryRepository.deleteById(id);
    }
}
