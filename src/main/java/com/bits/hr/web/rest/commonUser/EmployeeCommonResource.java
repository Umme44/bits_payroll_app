package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.EmployeeCommonService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Employee}.
 */
@RestController
@RequestMapping("/api/common/employees")
@RequiredArgsConstructor
public class EmployeeCommonResource {

    private static final String ENTITY_NAME = "employeeCommon";
    private final Logger log = LoggerFactory.getLogger(EmployeeCommonResource.class);
    private final EmployeeCommonService employeeCommonService;
    private final EmployeeService employeeService;
    private final CurrentEmployeeService currentEmployeeService;

    private final EventLoggingPublisher eventLoggingPublisher;

    private final EmployeeMinimalMapper employeeMinimalMapper;

    private final EmployeeRepository employeeRepository;

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body.
     */
    /*@GetMapping("")
    public ResponseEntity<List<EmployeeCommonDTO>> getAllEmployees(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get all Employees");
        Page<EmployeeCommonDTO> page = employeeCommonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/

    @GetMapping("/minimal")
    public List<EmployeeMinimalDTO> getAllEmployeesMinimal() {
        log.debug("REST request to get all Employees");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return employeeCommonService.findAllMinimal();
    }

    /**
     * Get Minimal Active Employee List
     */
    @GetMapping("/minimal-active")
    public List<EmployeeMinimalDTO> getAllMinimalEmploymentActive() {
        log.debug("REST request to get all Employees");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return employeeCommonService.findAllMinimalEmploymentActive();
    }

    /**
     * {@code GET  /employees/:id} : get the "id" employee.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeDTO, or with status {@code 404 (Not Found)}.
     */
    /*@GetMapping("/{id}")
    public ResponseEntity<EmployeeCommonDTO> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        Optional<EmployeeCommonDTO> employeeDTO = employeeCommonService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return ResponseUtil.wrapOrNotFound(employeeDTO);
    }*/

    @GetMapping("/current")
    public ResponseEntity<EmployeeDTO> getCurrentEmployee() {
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return ResponseUtil.wrapOrNotFound(currentEmployeeService.getCurrentEmployeeDTO());
    }

    @GetMapping("/current-employee/minimal-info")
    public ResponseEntity<EmployeeMinimalDTO> getCurrentEmployeeMinimal() {
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return ResponseEntity.ok(employeeMinimalMapper.toDto(currentEmployeeService.getCurrentEmployee().get()));
    }

    @GetMapping("/current/my-team")
    public List<EmployeeMinimalDTO> getMyTeam() {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return employeeMinimalMapper.toDto(employeeService.getDirectReportingTo(employeeOptional.get()));
    }

    @GetMapping("/current/photo")
    public ResponseEntity<Resource> getPhoto() throws IOException {
        String pin = SecurityUtils.getCurrentEmployeePin();
        Path file = employeeCommonService.getPhoto(pin);
        if (file == null) return ResponseEntity.noContent().build();

        Resource resource = new ByteArrayResource(Files.readAllBytes(file));

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return ResponseEntity
            .ok()
            .contentLength(resource.contentLength())
            .contentType(MediaType.parseMediaType(Files.probeContentType(file)))
            .body(resource);
    }

    @GetMapping("/address-book-suggestions")
    public ResponseEntity<Set<String>> getSuggestions() {
        log.debug("REST Request to get suggestions for Address Book");
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return ResponseEntity.ok(employeeCommonService.getSuggestions());
    }

    @GetMapping("/all-employees")
    public ResponseEntity<List<EmployeeMinimalDTO>> getAllEmployeesForRoomRequisition() {
        List<Employee> employeeList = employeeRepository.findAll();
        List<EmployeeMinimalDTO> employeeDTOList = employeeMinimalMapper.toDto(employeeList);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "EmployeeCommonResource");
        return ResponseEntity.ok(employeeDTOList);
    }

    /***
     * Get Current Employee phone number for populate leave application form
     */
    @GetMapping("/current-employee/info/leave-apply")
    public ResponseEntity<EmployeeMinimalDTO> getCurrentEmployeePhone() {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        EmployeeMinimalDTO employeeMinimalDTO = new EmployeeMinimalDTO();
        if (employeeOptional.isPresent()) {
            employeeMinimalDTO.setOfficialContactNo(employeeOptional.get().getOfficialContactNo());
            employeeMinimalDTO.setGender(employeeOptional.get().getGender());
        }
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, employeeOptional, RequestMethod.GET, "EmployeeCommonResource");
        return ResponseEntity.ok(employeeMinimalDTO);
    }

    @GetMapping("/total-working-days")
    public ResponseEntity<Long> getTotalWorkingDays() {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        long totalWorkingDays = -1;
        if (!employeeOptional.isPresent()) {
            ResponseEntity.ok().body(totalWorkingDays);
        }

        totalWorkingDays = employeeService.getTotalWorkingDays(employeeOptional.get());

        return ResponseEntity.ok().body(totalWorkingDays);
    }
}
