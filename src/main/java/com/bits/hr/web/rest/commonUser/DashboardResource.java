package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.OfficeNotices;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.OfficeNoticesRepository;
import com.bits.hr.service.MyTeamService;
import com.bits.hr.service.OfficeNoticesService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.OfficeNoticesDTO;
import com.bits.hr.service.mapper.OfficeNoticesMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@Log4j2
@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class DashboardResource {

    @Autowired
    OfficeNoticesRepository officeNoticesRepository;

    @Autowired
    MyTeamService myTeamService;

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    @Autowired
    OfficeNoticesMapper officeNoticesMapper;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    OfficeNoticesService officeNoticesService;

    @GetMapping("/office-notices")
    public List<OfficeNoticesDTO> getNoticeForToday() {
        List<OfficeNotices> officeNoticesList = officeNoticesRepository.findEffectiveNoticeForToday();
        return officeNoticesMapper.toDto(officeNoticesList);
    }

    @GetMapping("/recent-office-notices")
    public List<OfficeNoticesDTO> getRecentOfficeNotices() {
        log.debug("REST request to get last 05 office notices");
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<OfficeNotices> result = officeNoticesRepository.findRecentOfficeNotices(pageable);
        return officeNoticesMapper.toDto(result.getContent());
    }

    @GetMapping("/archive-office-notices")
    public ResponseEntity<List<OfficeNoticesDTO>> getAllOfficeNotices(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of OfficeNotices For User End");
        Page<OfficeNoticesDTO> page = officeNoticesService.findAllForUserEnd(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/has-subordinate")
    public boolean haveIAnySubOrdinate() {
        try {
            Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
            if (!employeeOptional.isPresent()) {
                throw new NoEmployeeProfileException();
            }
            List<Employee> employeeList = employeeRepository.getMyTeamByReportingToId(employeeOptional.get().getId());
            if (employeeList.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}
