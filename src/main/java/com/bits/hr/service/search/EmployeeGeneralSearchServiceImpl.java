package com.bits.hr.service.search;

import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.dto.AddressBookDTO;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.finalSettlement.EligibleEmployeeForFinalSettlement;
import com.bits.hr.service.mapper.AddressBookMapper;
import com.bits.hr.service.mapper.EmployeeCommonMapper;
import com.bits.hr.service.mapper.EmployeeMapper;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeGeneralSearchServiceImpl implements EmployeeGeneralSearchService {

    @Autowired
    EligibleEmployeeForFinalSettlement eligibleEmployeeForFinalSettlement;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    EmployeeCommonMapper employeeCommonMapper;

    @Autowired
    AddressBookMapper addressBookMapper;

    @Override
    public Page<EmployeeCommonDTO> employeeSearchForFinalSettlement(FilterDto filterDto, Pageable pageable) {
        // pending , settled , all
        return eligibleEmployeeForFinalSettlement.getEligibleEmployeesForFinalSettlement(filterDto, pageable);
    }

    @Override
    public Page<EmployeeCommonDTO> employeeSearch(FilterDto filterDto, Pageable pageable) {
        return employeeRepository
            .searchEmployee(
                filterDto.getSearchText(),
                "88" + filterDto.getSearchText(),
                "+88" + filterDto.getSearchText(),
                "%" + filterDto.getSearchText() + "%",
                filterDto.getDestinationId(),
                filterDto.getDepartmentId(),
                filterDto.getUnitId(),
                filterDto.getBloodGroup(),
                filterDto.getGender(),
                pageable
            )
            .map(employeeCommonMapper::toDto);
    }

    @Override
    public Page<AddressBookDTO> employeeSearchForAddressBook(FilterDto filterDto, Pageable pageable) {
        return employeeRepository
            .getAllActiveEmployeesTillDate(filterDto.getSearchText().toLowerCase(), LocalDate.now(), pageable)
            .map(addressBookMapper::toDto);
    }

    @Override
    public Page<EmployeeCommonDTO> employeeSearchUpcomingEventContractEnd(FilterDto filterDto, Pageable pageable) {
        return employeeRepository
            .employeeSearchUpcomingEventContractEnd(
                filterDto.getSearchText(),
                "88" + filterDto.getSearchText(),
                "+88" + filterDto.getSearchText(),
                "%" + filterDto.getSearchText() + "%",
                filterDto.getDestinationId(),
                filterDto.getDepartmentId(),
                filterDto.getUnitId(),
                filterDto.getBloodGroup(),
                filterDto.getGender(),
                filterDto.getStartDate(),
                filterDto.getEndDate(),
                pageable
            )
            .map(employeeCommonMapper::toDto);
    }

    @Override
    public Page<EmployeeCommonDTO> employeeSearchUpcomingEventProbationEnd(FilterDto filterDto, Pageable pageable) {
        return employeeRepository
            .employeeSearchUpcomingEventProbationEnd(
                filterDto.getSearchText(),
                "88" + filterDto.getSearchText(),
                "+88" + filterDto.getSearchText(),
                "%" + filterDto.getSearchText() + "%",
                filterDto.getDestinationId(),
                filterDto.getDepartmentId(),
                filterDto.getUnitId(),
                filterDto.getBloodGroup(),
                filterDto.getGender(),
                filterDto.getStartDate(),
                filterDto.getEndDate(),
                pageable
            )
            .map(employeeCommonMapper::toDto);
    }
}
