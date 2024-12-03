package com.bits.hr.web.rest.commonUser;

import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.EmployeeCommonService;
import com.bits.hr.service.FileService;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.UserEditAccountDTO;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.Employee}.
 */
@RestController
@RequestMapping("/api/common/edit-account")
public class UserEditAccountInformation {

    private static final String ENTITY_NAME = "employee";
    private final Logger log = LoggerFactory.getLogger(UserEditAccountInformation.class);
    private final EmployeeCommonService employeeCommonService;
    private final FileService fileService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public UserEditAccountInformation(EmployeeCommonService employeeCommonService, FileService fileService) {
        this.employeeCommonService = employeeCommonService;
        this.fileService = fileService;
    }

    /**
     * {@code PUT  /employees} : Updates an existing employee.
     *
     * @param userEditAccountDTO the employeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeDTO,
     * or with status {@code 400 (Bad Request)} if the employeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<UserEditAccountDTO> updateEmployee(@RequestBody @Valid UserEditAccountDTO userEditAccountDTO) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", userEditAccountDTO);
        if (userEditAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserEditAccountDTO result = employeeCommonService.update(userEditAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEditAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body.
     */
    @GetMapping("")
    public ResponseEntity<UserEditAccountDTO> getEmployee() {
        String employeePin = SecurityUtils.getCurrentEmployeePin();
        log.debug("REST request to get Employee : {}", employeePin);
        UserEditAccountDTO userEditAccountDTO = employeeCommonService.findEmployeeByPin(employeePin);
        return ResponseEntity.ok(userEditAccountDTO);
    }

    //    @PostMapping("/photo")
    //    public ResponseEntity<?> uploadPhoto(@RequestParam("photo") MultipartFile file) {
    //        String pin = SecurityUtils.getCurrentEmployeePin();
    //        try {
    //            String fileName = employeeCommonService.uploadPhoto(file, pin);
    //        } catch (Exception e) {
    //            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    //        }
    //        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    //    }

    @GetMapping("/sync")
    public DeferredResult<ResponseEntity<?>> syncImage() {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();
        String pin = SecurityUtils.getCurrentEmployeePin();

        ForkJoinPool
            .commonPool()
            .submit(() -> {
                boolean result = fileService.syncEmployeeImage(pin);
                if (result) {
                    output.setResult(ResponseEntity.ok().build());
                } else {
                    output.setResult(ResponseEntity.badRequest().build());
                }
            });

        return output;
    }
}
