package com.bits.hr.service.search;

import com.bits.hr.service.dto.AddressBookDTO;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeGeneralSearchService {
    Page<EmployeeCommonDTO> employeeSearch(FilterDto filterDto, Pageable pageable);
    Page<AddressBookDTO> employeeSearchForAddressBook(FilterDto filterDto, Pageable pageable);
    Page<EmployeeCommonDTO> employeeSearchForFinalSettlement(FilterDto filterDto, Pageable pageable);
    Page<EmployeeCommonDTO> employeeSearchUpcomingEventContractEnd(FilterDto filterDto, Pageable pageable);
    Page<EmployeeCommonDTO> employeeSearchUpcomingEventProbationEnd(FilterDto filterDto, Pageable pageable);
}
