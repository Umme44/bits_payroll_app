package com.bits.hr.service;

import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.service.dto.AddressBookExternalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExternalAddressBookService {
    Page<AddressBookExternalDTO> allActiveEmployeeProfiles(String search, Pageable pageable);
    Page<AddressBookExternalDTO> getAllEmployeesTillDate(String search, EmploymentStatus status, Pageable pageable);
}
