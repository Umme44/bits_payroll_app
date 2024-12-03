package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.AcknowledgementStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.TaxAcknowledgementReceiptService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.TaxAcknowledgementReceiptDTO;
import com.bits.hr.service.dto.UserTaxAcknowledgementValidationDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.UploadTaxAcknowledgementReceiptService;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import com.bits.hr.web.rest.util.CopyStreams;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.TaxAcknowledgementReceipt}.
 */
@RestController
@RequestMapping("/api/common")
public class UserTaxAcknowledgementReceiptResource {

    private final Logger log = LoggerFactory.getLogger(UserTaxAcknowledgementReceiptResource.class);

    private static final String ENTITY_NAME = "taxAcknowledgementReceipt";

    private static final String RESOURCE_NAME = "UserTaxAcknowledgementReceiptResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxAcknowledgementReceiptService taxAcknowledgementReceiptService;
    private final CurrentEmployeeService currentEmployeeService;
    private final UploadTaxAcknowledgementReceiptService uploadTaxAcknowledgementReceiptService;

    private final FileOperationService fileOperationService;

    private final EventLoggingPublisher eventLoggingPublisher;

    private final EmployeeMinimalMapper employeeMinimalMapper;

    public UserTaxAcknowledgementReceiptResource(
        TaxAcknowledgementReceiptService taxAcknowledgementReceiptService,
        CurrentEmployeeService currentEmployeeService,
        UploadTaxAcknowledgementReceiptService uploadTaxAcknowledgementReceiptService,
        FileOperationService fileOperationService,
        EventLoggingPublisher eventLoggingPublisher,
        EmployeeMinimalMapper employeeMinimalMapper
    ) {
        this.taxAcknowledgementReceiptService = taxAcknowledgementReceiptService;
        this.currentEmployeeService = currentEmployeeService;
        this.uploadTaxAcknowledgementReceiptService = uploadTaxAcknowledgementReceiptService;
        this.fileOperationService = fileOperationService;
        this.eventLoggingPublisher = eventLoggingPublisher;
        this.employeeMinimalMapper = employeeMinimalMapper;
    }

    @GetMapping("/tax-acknowledgement-receipts/current-employee-info")
    public ResponseEntity<EmployeeMinimalDTO> getCurrentEmployeeInfo() {
        Employee employee = currentEmployeeService.getCurrentEmployee().get();
        EmployeeMinimalDTO employeeMinimalDTO = employeeMinimalMapper.toDto(employee);
        employeeMinimalDTO.setPin(employee.getPin());
        employeeMinimalDTO.setTaxesCircle(employee.getTaxesCircle());
        employeeMinimalDTO.setTaxesZone(employee.getTaxesZone());
        employeeMinimalDTO.setFullName(employee.getFullName());
        employeeMinimalDTO.setDesignationName(employee.getDesignation().getDesignationName());
        employeeMinimalDTO.setTinNumber(employee.getTinNumber());
        User user = currentEmployeeService.getCurrentUser().get();
        log.debug("REST request to get EmployeeInfo For taxAcknowledgementReceipts : {}", employeeMinimalDTO);
        eventLoggingPublisher.publishEvent(user, employeeMinimalDTO, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok(employeeMinimalDTO);
    }

    /**
     * {@code POST  /tax-acknowledgement-receipts} : Create a new taxAcknowledgementReceipt.
     *
     * @param taxAcknowledgementReceiptDTO the taxAcknowledgementReceiptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxAcknowledgementReceiptDTO, or with status {@code 400 (Bad Request)} if the taxAcknowledgementReceipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tax-acknowledgement-receipts")
    public ResponseEntity<TaxAcknowledgementReceiptDTO> createTaxAcknowledgementReceipt(
        @Valid @RequestPart("tax-acknowledgement") TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO,
        @RequestPart("file") MultipartFile receiptFile
    ) throws URISyntaxException {
        log.debug("REST request to save TaxAcknowledgementReceipt : {}", taxAcknowledgementReceiptDTO);
        if (taxAcknowledgementReceiptDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxAcknowledgementReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (taxAcknowledgementReceiptDTO.getAcknowledgementStatus() == AcknowledgementStatus.RECEIVED) throw new BadRequestAlertException(
            "",
            "",
            ""
        );

        try {
            taxAcknowledgementReceiptDTO.setAcknowledgementStatus(AcknowledgementStatus.SUBMITTED);
            File savedTaxDocument = uploadTaxAcknowledgementReceiptService.save(receiptFile);
            taxAcknowledgementReceiptDTO.setFilePath(savedTaxDocument.getAbsolutePath());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BadRequestAlertException("File Failed to saved", "taxDocument", "fileSavedFailed");
        }

        long currenEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
        taxAcknowledgementReceiptDTO.setEmployeeId(currenEmployeeId);

        taxAcknowledgementReceiptDTO.setDateOfSubmission(LocalDate.now());
        TaxAcknowledgementReceiptDTO result = taxAcknowledgementReceiptService.save(taxAcknowledgementReceiptDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, RESOURCE_NAME);
        return ResponseEntity
            .created(new URI("/api/tax-acknowledgement-receipts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tax-acknowledgement-receipts} : Updates an existing taxAcknowledgementReceipt.
     *
     * @param taxAcknowledgementReceiptDTO the taxAcknowledgementReceiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxAcknowledgementReceiptDTO,
     * or with status {@code 400 (Bad Request)} if the taxAcknowledgementReceiptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxAcknowledgementReceiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-acknowledgement-receipts/multipart")
    public ResponseEntity<TaxAcknowledgementReceiptDTO> updateTaxAcknowledgementReceiptWithFile(
        @Valid @RequestPart("tax-acknowledgement") TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO,
        @RequestPart("file") MultipartFile receiptFile
    ) throws URISyntaxException {
        log.debug("REST request to update TaxAcknowledgementReceipt : {}", taxAcknowledgementReceiptDTO);
        if (taxAcknowledgementReceiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        validateUserRequest(taxAcknowledgementReceiptDTO.getId());

        long currenEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
        taxAcknowledgementReceiptDTO.setEmployeeId(currenEmployeeId);

        if (taxAcknowledgementReceiptDTO.getAcknowledgementStatus() == AcknowledgementStatus.RECEIVED) throw new BadRequestAlertException(
            "",
            "",
            ""
        );

        try {
            taxAcknowledgementReceiptDTO.setAcknowledgementStatus(AcknowledgementStatus.SUBMITTED);
            File savedTaxDocument = uploadTaxAcknowledgementReceiptService.save(receiptFile);
            taxAcknowledgementReceiptDTO.setFilePath(savedTaxDocument.getAbsolutePath());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BadRequestAlertException("File Failed to saved", "taxDocument", "fileSavedFailed");
        }

        TaxAcknowledgementReceiptDTO result = taxAcknowledgementReceiptService.save(taxAcknowledgementReceiptDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAcknowledgementReceiptDTO.getId().toString())
            )
            .body(result);
    }

    @PutMapping("/tax-acknowledgement-receipts")
    public ResponseEntity<TaxAcknowledgementReceiptDTO> updateTaxAcknowledgementReceiptWithoutFile(
        @Valid @RequestBody TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxAcknowledgementReceipt : {}", taxAcknowledgementReceiptDTO);
        if (taxAcknowledgementReceiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        validateUserRequest(taxAcknowledgementReceiptDTO.getId());

        long currenEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
        taxAcknowledgementReceiptDTO.setEmployeeId(currenEmployeeId);

        taxAcknowledgementReceiptDTO.setAcknowledgementStatus(AcknowledgementStatus.SUBMITTED);
        // due to file path not exposing in front-end
        String savedPath = taxAcknowledgementReceiptService.findOne(taxAcknowledgementReceiptDTO.getId()).get().getFilePath();
        taxAcknowledgementReceiptDTO.setFilePath(savedPath);
        TaxAcknowledgementReceiptDTO result = taxAcknowledgementReceiptService.save(taxAcknowledgementReceiptDTO);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, RESOURCE_NAME);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxAcknowledgementReceiptDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code GET  /tax-acknowledgement-receipts} : get all the taxAcknowledgementReceipts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxAcknowledgementReceipts in body.
     */
    @GetMapping("/tax-acknowledgement-receipts")
    public ResponseEntity<List<TaxAcknowledgementReceiptDTO>> getAllTaxAcknowledgementReceipts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TaxAcknowledgementReceipts");
        Optional<Employee> currentEmployee = currentEmployeeService.getCurrentEmployee();
        if (!currentEmployee.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        Page<TaxAcknowledgementReceiptDTO> page = taxAcknowledgementReceiptService.findAllByEmployeeId(
            pageable,
            currentEmployee.get().getId()
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-acknowledgement-receipts/:id} : get the "id" taxAcknowledgementReceipt.
     *
     * @param id the id of the taxAcknowledgementReceiptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxAcknowledgementReceiptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tax-acknowledgement-receipts/{id}")
    public ResponseEntity<TaxAcknowledgementReceiptDTO> getTaxAcknowledgementReceipt(@PathVariable Long id) {
        log.debug("REST request to get TaxAcknowledgementReceipt : {}", id);
        Employee currentEmployee = currentEmployeeService.getCurrentEmployee().get();
        Optional<TaxAcknowledgementReceiptDTO> taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptService.findOneByEmployeeId(
            id,
            currentEmployee.getId()
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(taxAcknowledgementReceiptDTO);
    }

    /**
     * {@code DELETE  /tax-acknowledgement-receipts/:id} : delete the "id" taxAcknowledgementReceipt.
     *
     * @param id the id of the taxAcknowledgementReceiptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tax-acknowledgement-receipts/{id}")
    public ResponseEntity<Void> deleteTaxAcknowledgementReceipt(@PathVariable Long id) {
        log.debug("REST request to delete TaxAcknowledgementReceipt : {}", id);
        validateUserRequest(id);
        Employee currentEmployee = currentEmployeeService.getCurrentEmployee().get();
        Optional<TaxAcknowledgementReceiptDTO> taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptService.findOneByEmployeeId(
            id,
            currentEmployee.getId()
        );
        // delete file
        String savedPath = taxAcknowledgementReceiptService.findOne(id).get().getFilePath();
        fileOperationService.delete(savedPath);
        taxAcknowledgementReceiptService.delete(id);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, taxAcknowledgementReceiptDTO, RequestMethod.DELETE, RESOURCE_NAME);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private void validateUserRequest(long receiptId) {
        long currentEmployeeId = currentEmployeeService.getCurrentEmployeeId().get();
        Optional<TaxAcknowledgementReceiptDTO> taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptService.findOneByEmployeeId(
            receiptId,
            currentEmployeeId
        );
        if (!taxAcknowledgementReceiptDTO.isPresent()) {
            throw new BadRequestAlertException("You are not authorized", "TaxAcknowledgementReceipt", "accessForbidden");
        }
    }

    // File Download
    @GetMapping("/tax-acknowledgement-receipts/download/{id}")
    public void downloadFileTemplates(HttpServletResponse response, @PathVariable long id) throws IOException {
        Optional<Employee> currentEmployee = currentEmployeeService.getCurrentEmployee();

        if (!currentEmployee.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        Optional<TaxAcknowledgementReceiptDTO> taxAcknowledgementReceipt = taxAcknowledgementReceiptService.findOne(id);

        if (!taxAcknowledgementReceipt.isPresent()) {
            log.error("File missing for ID: {}", id);
            throw new FileNotFoundException();
        }

        Long currentEmployeeId = currentEmployee.get().getId();

        if (!Objects.equals(currentEmployeeId, taxAcknowledgementReceipt.get().getEmployeeId())) {
            throw new BadRequestAlertException("You are not authorized", "TaxAcknowledgementReceipt", "accessForbidden");
        }

        File file = fileOperationService.loadAsFile(taxAcknowledgementReceipt.get().getFilePath());

        String extension = StringUtils.getFilenameExtension(taxAcknowledgementReceipt.get().getFilePath());

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + taxAcknowledgementReceipt.get().getPin() + "." + extension;

        response.setHeader(headerKey, headerValue);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        try {
            InputStream targetStream = new FileInputStream(file);
            CopyStreams.copy(targetStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to download for ID: {}", id);
        }
    }

    @GetMapping("/tax-acknowledgement-receipts/check-user-tax-info-validation")
    public UserTaxAcknowledgementValidationDTO checkUserTaxAcknowledgementReceiptValidation() {
        log.debug("REST request to check validity for user tax-acknowledgement-receipt statement");
        User user = currentEmployeeService.getCurrentUser().get();
        Employee currentEmployee = currentEmployeeService.getCurrentEmployee().get();

        if (currentEmployee.getTinNumber() == null || Objects.equals(currentEmployee.getTinNumber(), "")) {
            return new UserTaxAcknowledgementValidationDTO(false, "Oops! Your TIN No. is missing. Please contact HR.");
        } /*else if(currentEmployee.getTaxesZone() == null || Objects.equals(currentEmployee.getTaxesZone(), "")){
            return new UserTaxAcknowledgementValidationDTO(false,"Oops! Your Tax zone is missing. Please contact HR.");
        }else if(currentEmployee.getTaxesCircle() == null || Objects.equals(currentEmployee.getTaxesCircle(), "")){
            return new UserTaxAcknowledgementValidationDTO(false,"Oops! Your Tax circle is missing. Please contact HR.");
        }*/else {
            return new UserTaxAcknowledgementValidationDTO(true, "ok");
        }
    }
}
