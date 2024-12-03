package com.bits.hr.service.arrears.impl;

import com.bits.hr.repository.ArrearSalaryMasterRepository;
import com.bits.hr.service.arrears.ArrearsSalaryMasterService;
import com.bits.hr.service.dto.ArrearSalaryMasterDTO;
import com.bits.hr.service.mapper.ArrearSalaryMasterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArrearsSalaryMasterServiceImpl implements ArrearsSalaryMasterService {

    @Autowired
    private ArrearSalaryMasterRepository arrearSalaryMasterRepository;

    @Autowired
    private ArrearSalaryMasterMapper arrearSalaryMasterMapper;

    @Override
    public Page<ArrearSalaryMasterDTO> findAll(Pageable pageable) {
        return arrearSalaryMasterRepository.findAllActive(pageable).map(arrearSalaryMasterMapper::toDto);
    }
}
