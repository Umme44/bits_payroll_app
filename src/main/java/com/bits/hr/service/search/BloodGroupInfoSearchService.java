package com.bits.hr.service.search;

import com.bits.hr.service.dto.AddressBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BloodGroupInfoSearchService {
    Page<AddressBookDTO> employeeBloodGroupInfos(FilterDto filterDto, Pageable pageable);
}
