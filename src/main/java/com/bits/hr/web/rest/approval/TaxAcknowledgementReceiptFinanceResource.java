package com.bits.hr.web.rest.approval;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.AcknowledgementStatus;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.TaxAcknowledgementReceiptRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.NavBarItemAccessControlService;
import com.bits.hr.service.TaxAcknowledgementReceiptService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.TaxAcknowledgementReceiptDTO;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.UploadTaxAcknowledgementReceiptService;
import com.bits.hr.web.rest.util.CopyStreams;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/common/tax-acknowledgement-receipt-finance")
public class TaxAcknowledgementReceiptFinanceResource {

    private final Logger log = LoggerFactory.getLogger(TaxAcknowledgementReceiptFinanceResource.class);

    private static final String ENTITY_NAME = "taxAcknowledgementReceipt";
    private static final String RESOURCE_NAME = "TaxAcknowledgementReceiptFinanceResource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private TaxAcknowledgementReceiptService taxAcknowledgementReceiptService;

    @Autowired
    private TaxAcknowledgementReceiptRepository taxAcknowledgementReceiptRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private NavBarItemAccessControlService navBarItemAccessControlService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Autowired
    private UploadTaxAcknowledgementReceiptService uploadTaxAcknowledgementReceiptService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/received-list")
    public ResponseEntity<List<TaxAcknowledgementReceiptDTO>> getReceivedTaxAcknowledgementReceipts(
        @RequestParam(required = false) Long aitConfigId,
        @RequestParam(required = false) Long employeeId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        validateUserRequest();
        log.debug("REST request to get all TaxAcknowledgementReceipts : {}", ENTITY_NAME);
        Page<TaxAcknowledgementReceiptDTO> result = taxAcknowledgementReceiptService.findAllReceivedByFiscalYearId(
            aitConfigId,
            employeeId,
            pageable
        );

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @GetMapping("/submitted-list")
    public ResponseEntity<List<TaxAcknowledgementReceiptDTO>> getSubmittedTaxAcknowledgementReceipts(
        @RequestParam(required = false) Long aitConfigId,
        @RequestParam(required = false) Long employeeId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        validateUserRequest();
        Page<TaxAcknowledgementReceiptDTO> result = taxAcknowledgementReceiptService.findAllSubmittedByFiscalYearId(
            aitConfigId,
            employeeId,
            pageable
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        log.debug("REST request to get all submitted TaxAcknowledgementReceipts : {}", ENTITY_NAME);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @PostMapping("/turn-into-received")
    public ResponseEntity<Boolean> turnStatusIntoReceived(@RequestBody ApprovalDTO approvalDTO) {
        log.debug("REST request to approved TaxAcknowledgementReceipt : {}", ENTITY_NAME);
        try {
            Instant now = Instant.now();
            User currentUser = currentEmployeeService.getCurrentUser().get();
            taxAcknowledgementReceiptService.changeStatusIntoReceived(currentUser, now, approvalDTO.getListOfIds());
            User user = currentEmployeeService.getCurrentUser().get();
            eventLoggingPublisher.publishEvent(user, true, RequestMethod.PUT, RESOURCE_NAME);
            return ResponseEntity.ok(true);
        } catch (Exception ex) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/getTotalSubmittedReceipts")
    public ResponseEntity<Integer> getTotalSubmittedTaxReceipts() {
        log.debug("REST request to get total pending TaxAcknowledgementReceipt : {}", ENTITY_NAME);
        Integer result = taxAcknowledgementReceiptRepository.getTotalPendingTaxAcknowledgementReceipt();
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.GET, RESOURCE_NAME);
        return ResponseEntity.ok(result);
    }

    private void validateUserRequest() {
        Employee currentEmployee = currentEmployeeService.getCurrentEmployee().get();
        if (currentEmployee.getCanManageTaxAcknowledgementReceipt() == null) {
            throw new BadRequestAlertException("You are not authorized", "TaxAcknowledgementReceipt", "accessForbidden");
        } else if (!currentEmployee.getCanManageTaxAcknowledgementReceipt()) {
            throw new BadRequestAlertException("You are not authorized", "TaxAcknowledgementReceipt", "accessForbidden");
        }
    }

    // File Download
    @GetMapping("/download/{id}")
    public void downloadFileTemplates(HttpServletResponse response, @PathVariable long id) throws IOException {
        log.debug("REST request to download TaxAcknowledgementReceipt : {}", ENTITY_NAME);

        if (!currentEmployeeService.getCurrentEmployee().isPresent()) {
            throw new NoEmployeeProfileException();
        }

        // check Employee access to download report
        boolean access = navBarItemAccessControlService.canManageTacAcknowledgementReceipt(
            currentEmployeeService.getCurrentEmployee().get()
        );
        if (!access) {
            throw new AccessDeniedException("");
        }

        Optional<TaxAcknowledgementReceiptDTO> taxAcknowledgementReceipt = taxAcknowledgementReceiptService.findOne(id);

        if (!taxAcknowledgementReceipt.isPresent()) {
            log.error("File missing for ID: {}", id);
            throw new FileNotFoundException();
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
            User user = currentEmployeeService.getCurrentUser().get();
            eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to download for ID: {}", id);
        }
    }

    @GetMapping("/current-employee-info/{employeeId}")
    public ResponseEntity<EmployeeMinimalDTO> getCurrentEmployeeInfo(@PathVariable Long employeeId) {
        EmployeeMinimalDTO employeeMinimalDTO = employeeService.findEmployeeMinimalById(employeeId);
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
            taxAcknowledgementReceiptDTO.setAcknowledgementStatus(AcknowledgementStatus.RECEIVED);
            File savedTaxDocument = uploadTaxAcknowledgementReceiptService.save(receiptFile);
            taxAcknowledgementReceiptDTO.setFilePath(savedTaxDocument.getAbsolutePath());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BadRequestAlertException("File Failed to saved", "taxDocument", "fileSavedFailed");
        }

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

        try {
            taxAcknowledgementReceiptDTO.setAcknowledgementStatus(AcknowledgementStatus.RECEIVED);
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

        taxAcknowledgementReceiptDTO.setAcknowledgementStatus(AcknowledgementStatus.RECEIVED);
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

    @GetMapping("/{id}/{employeeId}")
    public ResponseEntity<TaxAcknowledgementReceiptDTO> getTaxAcknowledgementReceipt(@PathVariable Long id, @PathVariable Long employeeId) {
        log.debug("REST request to get TaxAcknowledgementReceipt : {}", id);
        Employee currentEmployee = currentEmployeeService.getCurrentEmployee().get();
        Optional<TaxAcknowledgementReceiptDTO> taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptService.findOneByEmployeeId(
            id,
            employeeId
        );
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, RESOURCE_NAME);
        return ResponseUtil.wrapOrNotFound(taxAcknowledgementReceiptDTO);
    }
}
