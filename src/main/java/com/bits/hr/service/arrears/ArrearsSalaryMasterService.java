package com.bits.hr.service.arrears;

import com.bits.hr.service.dto.ArrearSalaryMasterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArrearsSalaryMasterService {
    Page<ArrearSalaryMasterDTO> findAll(Pageable pageable);
}
