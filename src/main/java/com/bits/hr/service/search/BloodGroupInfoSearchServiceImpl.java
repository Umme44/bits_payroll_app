package com.bits.hr.service.search;

import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.dto.AddressBookDTO;
import com.bits.hr.service.mapper.AddressBookMapper;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BloodGroupInfoSearchServiceImpl implements BloodGroupInfoSearchService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AddressBookMapper addressBookMapper;

    @Override
    public Page<AddressBookDTO> employeeBloodGroupInfos(FilterDto filterDto, Pageable pageable) {
        if (filterDto.getSearchText().contains("+")) {
            filterDto.setSearchText(filterDto.getSearchText().replace("+", "_POSITIVE"));
            return employeeRepository.findByBloodGroup(filterDto.getSearchText(), LocalDate.now(), pageable).map(addressBookMapper::toDto);
        }
        if (filterDto.getSearchText().contains("-")) {
            filterDto.setSearchText(filterDto.getSearchText().replace("-", "_NEGATIVE"));
            return employeeRepository.findByBloodGroup(filterDto.getSearchText(), LocalDate.now(), pageable).map(addressBookMapper::toDto);
        }
        return employeeRepository
            .searchAllByFullNameAndPinAndBloodGroup("%" + filterDto.getSearchText() + "%", LocalDate.now(), pageable)
            .map(addressBookMapper::toDto);
    }
}
