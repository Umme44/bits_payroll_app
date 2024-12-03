package com.bits.hr.web.rest;

import com.bits.hr.config.YamlConfig;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequestMethod;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.service.AitConfigService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AcknowledgeDTO;
import com.bits.hr.service.dto.AitConfigDTO;
import com.bits.hr.service.salaryGeneration.config.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bits.hr.domain.AitConfig}.
 */
@RestController
@RequestMapping("/api/ait/configs")
public class AitConfigResource {

    private static final String ENTITY_NAME = "aitConfig";
    private final Logger log = LoggerFactory.getLogger(AitConfigResource.class);
    private final AitConfigService aitConfigService;

    // private final EventLoggingService eventLoggingService;
    private final CurrentEmployeeService currentEmployeeService;
    private final EventLoggingPublisher eventLoggingPublisher;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AitConfigResource(
        AitConfigService aitConfigService,
        CurrentEmployeeService currentEmployeeService,
        EventLoggingPublisher eventLoggingPublisher
    ) {
        this.aitConfigService = aitConfigService;
        this.currentEmployeeService = currentEmployeeService;
        this.eventLoggingPublisher = eventLoggingPublisher;
    }

    /**
     * {@code POST  /ait-configs} : Create a new aitConfig.
     *
     * @param aitConfigDTO the aitConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aitConfigDTO, or with status {@code 400 (Bad Request)} if the aitConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AcknowledgeDTO> createAitConfig(@Valid @RequestBody AitConfigDTO aitConfigDTO) throws URISyntaxException {
        log.debug("REST request to save AitConfig : {}", aitConfigDTO);
        if (aitConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new aitConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }

        //        if(aitConfigRepository.findAllBetween(aitConfigDTO.getStartDate()).size()>0 || aitConfigRepository.findAllBetween(aitConfigDTO.getEndDate()).size()>0){
        //            throw new BadRequestAlertException("A new ait config  date range must not overlap others", ENTITY_NAME, "idexists");
        //        }

        AcknowledgeDTO acknowledgeDTO = new AcknowledgeDTO();

        try {
            IncomeTaxConfig taxConfig = YamlConfig.yamlParser().readValue(aitConfigDTO.getTaxConfig(), IncomeTaxConfig.class);

            ArrayList<String> errors = validate(taxConfig);
            if (!errors.isEmpty()) {
                acknowledgeDTO.setErrors(errors);
                acknowledgeDTO.setSuccessful(false);
                return ResponseEntity.badRequest().body(acknowledgeDTO);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AitConfigDTO result = aitConfigService.save(aitConfigDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.POST, "AitConfig");

        return ResponseEntity
            .created(new URI("/api/ait/configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(acknowledgeDTO);
    }

    /**
     * {@code PUT  /ait-configs} : Updates an existing aitConfig.
     *
     * @param aitConfigDTO the aitConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aitConfigDTO,
     * or with status {@code 400 (Bad Request)} if the aitConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aitConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("")
    public ResponseEntity<AcknowledgeDTO> updateAitConfig(@Valid @RequestBody AitConfigDTO aitConfigDTO) throws URISyntaxException {
        log.debug("REST request to update AitConfig : {}", aitConfigDTO);
        if (aitConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        AcknowledgeDTO acknowledgeDTO = new AcknowledgeDTO();

        try {
            IncomeTaxConfig taxConfig = YamlConfig.yamlParser().readValue(aitConfigDTO.getTaxConfig(), IncomeTaxConfig.class);

            ArrayList<String> errors = validate(taxConfig);
            if (!errors.isEmpty()) {
                acknowledgeDTO.setErrors(errors);
                acknowledgeDTO.setSuccessful(false);
                return ResponseEntity.badRequest().body(acknowledgeDTO);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AitConfigDTO result = aitConfigService.save(aitConfigDTO);

        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, result, RequestMethod.PUT, "AitConfig");

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aitConfigDTO.getId().toString()))
            .body(acknowledgeDTO);
    }

    /**
     * {@code GET  /ait-configs} : get all the aitConfigs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aitConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AitConfigDTO>> getAllAitConfigs(Pageable pageable) {
        log.debug("REST request to get a page of AitConfigs");
        Page<AitConfigDTO> page = aitConfigService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        User user = currentEmployeeService.getCurrentUser().get();
        eventLoggingPublisher.publishEvent(user, Optional.empty(), RequestMethod.GET, "AitConfig");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ait-configs/:id} : get the "id" aitConfig.
     *
     * @param id the id of the aitConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aitConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AitConfigDTO> getAitConfig(@PathVariable Long id) {
        log.debug("REST request to get AitConfig : {}", id);
        Optional<AitConfigDTO> aitConfigDTO = aitConfigService.findOne(id);

        User user = currentEmployeeService.getCurrentUser().get();

        if (aitConfigDTO.isPresent()) {
            eventLoggingPublisher.publishEvent(user, aitConfigDTO.get(), RequestMethod.GET, "AitConfig");
        }
        return ResponseUtil.wrapOrNotFound(aitConfigDTO);
    }

    /**
     * {@code DELETE  /ait-configs/:id} : delete the "id" aitConfig.
     *
     * @param id the id of the aitConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAitConfig(@PathVariable Long id) {
        log.debug("REST request to delete AitConfig : {}", id);

        Optional<AitConfigDTO> aitConfigDTOOptional = aitConfigService.findOne(id);
        User user = currentEmployeeService.getCurrentUser().get();
        if (aitConfigDTOOptional.isPresent()) {
            eventLoggingPublisher.publishEvent(user, aitConfigDTOOptional.get(), RequestMethod.DELETE, "AitConfig");
        }
        aitConfigService.delete(id);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    private ArrayList<String> validate(IncomeTaxConfig taxConfig) {
        ArrayList<String> errors = new ArrayList<>();

        TaxReportConfigurations taxReportConfigurations = taxConfig.getTaxReportConfigurations();

        ReportLabels reportLabels = taxReportConfigurations.getReportLabels();
        SalaryIncomeLabels salaryIncomeLabels = taxReportConfigurations.getSalaryIncomeLabels();

        if (nullOrEmpty(reportLabels.getTotalIncomeLabel())) {
            errors.add("TotalIncomeLabel must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getExemptionRemarks())) {
            errors.add("ExemptionRemarks must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getTotalTaxLiabilityLabel())) {
            errors.add("TotalTaxLiabilityLabel must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getInvestmentLabelHeader())) {
            errors.add("InvestmentLabelHeader must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getInvestmentLabelPf())) {
            errors.add("InvestmentLabelPf must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getInvestmentLabelOther())) {
            errors.add("InvestmentLabelOther must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getInvestmentLabelTotal())) {
            errors.add("InvestmentLabelTotal must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getLessRebateLabel())) {
            errors.add("LessRebateLabel must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getNetTaxLiabilityConsideringRebate())) {
            errors.add("NetTaxLiabilityConsideringRebate must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getFinalLabelAit())) {
            errors.add("FinalLabelAit must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getFinalLabelActualTax())) {
            errors.add("FinalLabelActualTax must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getLabelTotalPayableTax())) {
            errors.add("LabelTotalPayableTax must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getCertifyText())) {
            errors.add("CertifyText must not be null or blank");
        }

        if (nullOrEmpty(reportLabels.getNoteText())) {
            errors.add("NoteText must not be null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomeBasic())) {
            errors.add("SalaryIncomeBasic must not be null and head must not null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomeHr())) {
            errors.add("SalaryIncomeHr must not be null and head must not null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomeMedical())) {
            errors.add("SalaryIncomeMedical must not be null and head must not null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomeConveyanceAllowance())) {
            errors.add("SalaryIncomeConveyanceAllowance must not be null and head must not null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomeFestivalBonus())) {
            errors.add("SalaryIncomeFestivalBonus must not be null and head must not null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomePerformanceBonus())) {
            errors.add("SalaryIncomePerformanceBonus must not be null and head must not null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomeIncentive())) {
            errors.add("SalaryIncomeIncentive must not be null and head must not null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomePf())) {
            errors.add("SalaryIncomePf must not be null and head must not null or blank");
        }

        if (isSalaryIncomeLabelInvalid(salaryIncomeLabels.getSalaryIncomeTotal())) {
            errors.add("SalaryIncomeTotal must not be null and head must not null or blank");
        }

        return errors;
    }

    private boolean isSalaryIncomeLabelInvalid(SalaryIncomeLabel salaryIncomeLabel) {
        return salaryIncomeLabel == null || nullOrEmpty(salaryIncomeLabel.getHead());
    }

    private boolean nullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
