package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Location;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.ExternalAddressBookService;
import com.bits.hr.service.dto.AddressBookExternalDTO;
import com.bits.hr.service.mapper.AddressBookExternalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalAddressBookServiceImpl implements ExternalAddressBookService {

    private final EmployeeRepository employeeRepository;
    private final AddressBookExternalMapper addressBookExternalMapper;

    @Override
    public Page<AddressBookExternalDTO> allActiveEmployeeProfiles(String search, Pageable pageable) {
        log.debug("Get all active employee profiles");
        LocalDate selectedDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), 1);
        LocalDate endDate = LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.lengthOfMonth());
        return employeeRepository.getAllActiveEmployeeProfiles(startDate, endDate, search, pageable)
            .map(employee -> mapEmployeeToDto(employee));
    }

    @Override
    public Page<AddressBookExternalDTO> getAllEmployeesTillDate(String search, EmploymentStatus status, Pageable pageable) {
        log.debug("Get all employee profiles till date");
        return employeeRepository
            .getAllEmployeesTillDate(search, status, pageable)
            .map(employee -> mapEmployeeToDto(employee));
    }

    private AddressBookExternalDTO mapEmployeeToDto(Employee employee){
        AddressBookExternalDTO addressBookDTO= addressBookExternalMapper.toDto(employee);
        if(employee.getReportingTo()!=null){
            addressBookDTO.setReportingToName(employee.getReportingTo().getFullName());
            addressBookDTO.setReportingToPin(employee.getReportingTo().getPin());
            Location location=employee.getOfficeLocation();
            HashMap<String,String> officeLocation=new HashMap<>();
            while(location!=null){
                officeLocation.put(location.getLocationType().toString().toLowerCase(), location.getLocationName());
                location=location.getParent();
            }
            addressBookDTO.setOfficeLocations(officeLocation);
        }
        return addressBookDTO;
    }
}
