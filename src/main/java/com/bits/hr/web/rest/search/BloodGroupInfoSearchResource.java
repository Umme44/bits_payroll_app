package com.bits.hr.web.rest.search;

import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AddressBookDTO;
import com.bits.hr.service.search.BloodGroupInfoSearchService;
import com.bits.hr.service.search.FilterDto;
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
@RequestMapping("/api/common/blood-group-information")
public class BloodGroupInfoSearchResource {

    @Autowired
    BloodGroupInfoSearchService bloodGroupInfoSearchService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @PostMapping("/search")
    public ResponseEntity<List<AddressBookDTO>> getEmployeesBloodGroupInfo(
        @RequestBody @Valid FilterDto filterDto,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<AddressBookDTO> bloodGroupInfos = bloodGroupInfoSearchService.employeeBloodGroupInfos(filterDto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            bloodGroupInfos
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "BloodGroupInfoSearch");
        return ResponseEntity.ok().headers(headers).body(bloodGroupInfos.getContent());
    }
}
