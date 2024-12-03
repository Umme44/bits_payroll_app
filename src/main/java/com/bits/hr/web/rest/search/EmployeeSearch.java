package com.bits.hr.web.rest.search;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AddressBookDTO;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.search.EmployeeGeneralSearchService;
import com.bits.hr.service.search.FilterDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class EmployeeSearch {

    @Autowired
    private EmployeeGeneralSearchService employeeGeneralSearchService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @PostMapping("/employee-mgt/employeeSearch")
    public ResponseEntity<List<EmployeeCommonDTO>> search(
        @RequestBody @Valid  FilterDto filterDto,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws Exception {
        Page<EmployeeCommonDTO> page = employeeGeneralSearchService.employeeSearch(filterDto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeSearch");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/common/address-book/employee-search")
    public ResponseEntity<List<AddressBookDTO>> addressBookSearch(
        @RequestBody @Valid FilterDto filterDto,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws Exception {
        Page<AddressBookDTO> page = employeeGeneralSearchService.employeeSearchForAddressBook(filterDto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeSearch");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/employee-mgt/contract-end-employees")
    public ResponseEntity<List<EmployeeCommonDTO>> employeeSearchUpcomingEventContractEnd(
        @RequestBody @Valid FilterDto filterDto,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws Exception {
        LocalDate startDate;
        LocalDate endDate;
        if (filterDto.getYear() != null && filterDto.getMonth() != null) {
            int year = filterDto.getYear();
            int month = filterDto.getMonth().getValue();
            startDate = LocalDate.of(year, month, 1);
            endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
            filterDto.setStartDate(startDate);
            filterDto.setEndDate(endDate);
        } else if (filterDto.getYear() != null) {
            int year = filterDto.getYear();
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, startDate.lengthOfMonth());
            filterDto.setStartDate(startDate);
            filterDto.setEndDate(endDate);
        } else {
            filterDto.setStartDate(null);
            filterDto.setEndDate(null);
        }

        Page<EmployeeCommonDTO> page = employeeGeneralSearchService.employeeSearchUpcomingEventContractEnd(filterDto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeSearch");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/employee-mgt/probation-end-employees")
    public ResponseEntity<List<EmployeeCommonDTO>> employeeSearchUpcomingEventProbationEnd(
        @RequestBody @Valid FilterDto filterDto,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) throws Exception {
        // service in --> Optional<LocalDate> startDate, Optional<LocalDate> endDate
        LocalDate startDate;
        LocalDate endDate;
        if (filterDto.getYear() != null && filterDto.getMonth() != null) {
            int year = filterDto.getYear();
            int month = filterDto.getMonth().getValue();
            startDate = LocalDate.of(year, month, 1);
            endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
            filterDto.setStartDate(startDate);
            filterDto.setEndDate(endDate);
        } else if (filterDto.getYear() != null) {
            int year = filterDto.getYear();
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, startDate.lengthOfMonth());
            filterDto.setStartDate(startDate);
            filterDto.setEndDate(endDate);
        } else {
            filterDto.setStartDate(null);
            filterDto.setEndDate(null);
        }

        Page<EmployeeCommonDTO> page = employeeGeneralSearchService.employeeSearchUpcomingEventProbationEnd(filterDto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeSearch");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
