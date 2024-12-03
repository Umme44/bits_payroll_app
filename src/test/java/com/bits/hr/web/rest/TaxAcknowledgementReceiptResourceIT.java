package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.AitConfig;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.TaxAcknowledgementReceipt;
import com.bits.hr.domain.enumeration.AcknowledgementStatus;
import com.bits.hr.repository.TaxAcknowledgementReceiptRepository;
import com.bits.hr.service.TaxAcknowledgementReceiptService;
import com.bits.hr.service.dto.TaxAcknowledgementReceiptDTO;
import com.bits.hr.service.mapper.TaxAcknowledgementReceiptMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TaxAcknowledgementReceiptResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TaxAcknowledgementReceiptResourceIT {

    private static final String DEFAULT_TIN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TIN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIPT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TAXES_CIRCLE = "AAAAAAAAAA";
    private static final String UPDATED_TAXES_CIRCLE = "BBBBBBBBBB";

    private static final String DEFAULT_TAXES_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_TAXES_ZONE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_SUBMISSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_SUBMISSION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final AcknowledgementStatus DEFAULT_ACKNOWLEDGEMENT_STATUS = AcknowledgementStatus.SUBMITTED;
    private static final AcknowledgementStatus UPDATED_ACKNOWLEDGEMENT_STATUS = AcknowledgementStatus.RECEIVED;

    private static final Instant DEFAULT_RECEIVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tax-acknowledgement-receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxAcknowledgementReceiptRepository taxAcknowledgementReceiptRepository;

    @Mock
    private TaxAcknowledgementReceiptRepository taxAcknowledgementReceiptRepositoryMock;

    @Autowired
    private TaxAcknowledgementReceiptMapper taxAcknowledgementReceiptMapper;

    @Mock
    private TaxAcknowledgementReceiptService taxAcknowledgementReceiptServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxAcknowledgementReceiptMockMvc;

    private TaxAcknowledgementReceipt taxAcknowledgementReceipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxAcknowledgementReceipt createEntity(EntityManager em) {
        TaxAcknowledgementReceipt taxAcknowledgementReceipt = new TaxAcknowledgementReceipt()
            .tinNumber(DEFAULT_TIN_NUMBER)
            .receiptNumber(DEFAULT_RECEIPT_NUMBER)
            .taxesCircle(DEFAULT_TAXES_CIRCLE)
            .taxesZone(DEFAULT_TAXES_ZONE)
            .dateOfSubmission(DEFAULT_DATE_OF_SUBMISSION)
            .filePath(DEFAULT_FILE_PATH)
            .acknowledgementStatus(DEFAULT_ACKNOWLEDGEMENT_STATUS)
            .receivedAt(DEFAULT_RECEIVED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        AitConfig aitConfig;
        if (TestUtil.findAll(em, AitConfig.class).isEmpty()) {
            aitConfig = AitConfigResourceIT.createEntity(em);
            em.persist(aitConfig);
            em.flush();
        } else {
            aitConfig = TestUtil.findAll(em, AitConfig.class).get(0);
        }
        taxAcknowledgementReceipt.setFiscalYear(aitConfig);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        taxAcknowledgementReceipt.setEmployee(employee);
        return taxAcknowledgementReceipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxAcknowledgementReceipt createUpdatedEntity(EntityManager em) {
        TaxAcknowledgementReceipt taxAcknowledgementReceipt = new TaxAcknowledgementReceipt()
            .tinNumber(UPDATED_TIN_NUMBER)
            .receiptNumber(UPDATED_RECEIPT_NUMBER)
            .taxesCircle(UPDATED_TAXES_CIRCLE)
            .taxesZone(UPDATED_TAXES_ZONE)
            .dateOfSubmission(UPDATED_DATE_OF_SUBMISSION)
            .filePath(UPDATED_FILE_PATH)
            .acknowledgementStatus(UPDATED_ACKNOWLEDGEMENT_STATUS)
            .receivedAt(UPDATED_RECEIVED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        AitConfig aitConfig;
        if (TestUtil.findAll(em, AitConfig.class).isEmpty()) {
            aitConfig = AitConfigResourceIT.createUpdatedEntity(em);
            em.persist(aitConfig);
            em.flush();
        } else {
            aitConfig = TestUtil.findAll(em, AitConfig.class).get(0);
        }
        taxAcknowledgementReceipt.setFiscalYear(aitConfig);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        taxAcknowledgementReceipt.setEmployee(employee);
        return taxAcknowledgementReceipt;
    }

    @BeforeEach
    public void initTest() {
        taxAcknowledgementReceipt = createEntity(em);
    }

    @Test
    @Transactional
    void createTaxAcknowledgementReceipt() throws Exception {
        int databaseSizeBeforeCreate = taxAcknowledgementReceiptRepository.findAll().size();
        // Create the TaxAcknowledgementReceipt
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);
        restTaxAcknowledgementReceiptMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeCreate + 1);
        TaxAcknowledgementReceipt testTaxAcknowledgementReceipt = taxAcknowledgementReceiptList.get(
            taxAcknowledgementReceiptList.size() - 1
        );
        assertThat(testTaxAcknowledgementReceipt.getTinNumber()).isEqualTo(DEFAULT_TIN_NUMBER);
        assertThat(testTaxAcknowledgementReceipt.getReceiptNumber()).isEqualTo(DEFAULT_RECEIPT_NUMBER);
        assertThat(testTaxAcknowledgementReceipt.getTaxesCircle()).isEqualTo(DEFAULT_TAXES_CIRCLE);
        assertThat(testTaxAcknowledgementReceipt.getTaxesZone()).isEqualTo(DEFAULT_TAXES_ZONE);
        assertThat(testTaxAcknowledgementReceipt.getDateOfSubmission()).isEqualTo(DEFAULT_DATE_OF_SUBMISSION);
        assertThat(testTaxAcknowledgementReceipt.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testTaxAcknowledgementReceipt.getAcknowledgementStatus()).isEqualTo(DEFAULT_ACKNOWLEDGEMENT_STATUS);
        assertThat(testTaxAcknowledgementReceipt.getReceivedAt()).isEqualTo(DEFAULT_RECEIVED_AT);
        assertThat(testTaxAcknowledgementReceipt.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTaxAcknowledgementReceipt.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createTaxAcknowledgementReceiptWithExistingId() throws Exception {
        // Create the TaxAcknowledgementReceipt with an existing ID
        taxAcknowledgementReceipt.setId(1L);
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        int databaseSizeBeforeCreate = taxAcknowledgementReceiptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxAcknowledgementReceiptMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTinNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxAcknowledgementReceiptRepository.findAll().size();
        // set the field null
        taxAcknowledgementReceipt.setTinNumber(null);

        // Create the TaxAcknowledgementReceipt, which fails.
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceiptNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxAcknowledgementReceiptRepository.findAll().size();
        // set the field null
        taxAcknowledgementReceipt.setReceiptNumber(null);

        // Create the TaxAcknowledgementReceipt, which fails.
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxesCircleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxAcknowledgementReceiptRepository.findAll().size();
        // set the field null
        taxAcknowledgementReceipt.setTaxesCircle(null);

        // Create the TaxAcknowledgementReceipt, which fails.
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxesZoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxAcknowledgementReceiptRepository.findAll().size();
        // set the field null
        taxAcknowledgementReceipt.setTaxesZone(null);

        // Create the TaxAcknowledgementReceipt, which fails.
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfSubmissionIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxAcknowledgementReceiptRepository.findAll().size();
        // set the field null
        taxAcknowledgementReceipt.setDateOfSubmission(null);

        // Create the TaxAcknowledgementReceipt, which fails.
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAcknowledgementStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxAcknowledgementReceiptRepository.findAll().size();
        // set the field null
        taxAcknowledgementReceipt.setAcknowledgementStatus(null);

        // Create the TaxAcknowledgementReceipt, which fails.
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTaxAcknowledgementReceipts() throws Exception {
        // Initialize the database
        taxAcknowledgementReceiptRepository.saveAndFlush(taxAcknowledgementReceipt);

        // Get all the taxAcknowledgementReceiptList
        restTaxAcknowledgementReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxAcknowledgementReceipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].tinNumber").value(hasItem(DEFAULT_TIN_NUMBER)))
            .andExpect(jsonPath("$.[*].receiptNumber").value(hasItem(DEFAULT_RECEIPT_NUMBER)))
            .andExpect(jsonPath("$.[*].taxesCircle").value(hasItem(DEFAULT_TAXES_CIRCLE)))
            .andExpect(jsonPath("$.[*].taxesZone").value(hasItem(DEFAULT_TAXES_ZONE)))
            .andExpect(jsonPath("$.[*].dateOfSubmission").value(hasItem(DEFAULT_DATE_OF_SUBMISSION.toString())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].acknowledgementStatus").value(hasItem(DEFAULT_ACKNOWLEDGEMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].receivedAt").value(hasItem(DEFAULT_RECEIVED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxAcknowledgementReceiptsWithEagerRelationshipsIsEnabled() throws Exception {
        when(taxAcknowledgementReceiptServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxAcknowledgementReceiptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxAcknowledgementReceiptServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxAcknowledgementReceiptsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(taxAcknowledgementReceiptServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxAcknowledgementReceiptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(taxAcknowledgementReceiptRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTaxAcknowledgementReceipt() throws Exception {
        // Initialize the database
        taxAcknowledgementReceiptRepository.saveAndFlush(taxAcknowledgementReceipt);

        // Get the taxAcknowledgementReceipt
        restTaxAcknowledgementReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, taxAcknowledgementReceipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxAcknowledgementReceipt.getId().intValue()))
            .andExpect(jsonPath("$.tinNumber").value(DEFAULT_TIN_NUMBER))
            .andExpect(jsonPath("$.receiptNumber").value(DEFAULT_RECEIPT_NUMBER))
            .andExpect(jsonPath("$.taxesCircle").value(DEFAULT_TAXES_CIRCLE))
            .andExpect(jsonPath("$.taxesZone").value(DEFAULT_TAXES_ZONE))
            .andExpect(jsonPath("$.dateOfSubmission").value(DEFAULT_DATE_OF_SUBMISSION.toString()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH.toString()))
            .andExpect(jsonPath("$.acknowledgementStatus").value(DEFAULT_ACKNOWLEDGEMENT_STATUS.toString()))
            .andExpect(jsonPath("$.receivedAt").value(DEFAULT_RECEIVED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTaxAcknowledgementReceipt() throws Exception {
        // Get the taxAcknowledgementReceipt
        restTaxAcknowledgementReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTaxAcknowledgementReceipt() throws Exception {
        // Initialize the database
        taxAcknowledgementReceiptRepository.saveAndFlush(taxAcknowledgementReceipt);

        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();

        // Update the taxAcknowledgementReceipt
        TaxAcknowledgementReceipt updatedTaxAcknowledgementReceipt = taxAcknowledgementReceiptRepository
            .findById(taxAcknowledgementReceipt.getId())
            .get();
        // Disconnect from session so that the updates on updatedTaxAcknowledgementReceipt are not directly saved in db
        em.detach(updatedTaxAcknowledgementReceipt);
        updatedTaxAcknowledgementReceipt
            .tinNumber(UPDATED_TIN_NUMBER)
            .receiptNumber(UPDATED_RECEIPT_NUMBER)
            .taxesCircle(UPDATED_TAXES_CIRCLE)
            .taxesZone(UPDATED_TAXES_ZONE)
            .dateOfSubmission(UPDATED_DATE_OF_SUBMISSION)
            .filePath(UPDATED_FILE_PATH)
            .acknowledgementStatus(UPDATED_ACKNOWLEDGEMENT_STATUS)
            .receivedAt(UPDATED_RECEIVED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(updatedTaxAcknowledgementReceipt);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxAcknowledgementReceiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
        TaxAcknowledgementReceipt testTaxAcknowledgementReceipt = taxAcknowledgementReceiptList.get(
            taxAcknowledgementReceiptList.size() - 1
        );
        assertThat(testTaxAcknowledgementReceipt.getTinNumber()).isEqualTo(UPDATED_TIN_NUMBER);
        assertThat(testTaxAcknowledgementReceipt.getReceiptNumber()).isEqualTo(UPDATED_RECEIPT_NUMBER);
        assertThat(testTaxAcknowledgementReceipt.getTaxesCircle()).isEqualTo(UPDATED_TAXES_CIRCLE);
        assertThat(testTaxAcknowledgementReceipt.getTaxesZone()).isEqualTo(UPDATED_TAXES_ZONE);
        assertThat(testTaxAcknowledgementReceipt.getDateOfSubmission()).isEqualTo(UPDATED_DATE_OF_SUBMISSION);
        assertThat(testTaxAcknowledgementReceipt.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testTaxAcknowledgementReceipt.getAcknowledgementStatus()).isEqualTo(UPDATED_ACKNOWLEDGEMENT_STATUS);
        assertThat(testTaxAcknowledgementReceipt.getReceivedAt()).isEqualTo(UPDATED_RECEIVED_AT);
        assertThat(testTaxAcknowledgementReceipt.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaxAcknowledgementReceipt.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingTaxAcknowledgementReceipt() throws Exception {
        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();
        taxAcknowledgementReceipt.setId(count.incrementAndGet());

        // Create the TaxAcknowledgementReceipt
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxAcknowledgementReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxAcknowledgementReceiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxAcknowledgementReceipt() throws Exception {
        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();
        taxAcknowledgementReceipt.setId(count.incrementAndGet());

        // Create the TaxAcknowledgementReceipt
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAcknowledgementReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxAcknowledgementReceipt() throws Exception {
        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();
        taxAcknowledgementReceipt.setId(count.incrementAndGet());

        // Create the TaxAcknowledgementReceipt
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAcknowledgementReceiptMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTaxAcknowledgementReceiptWithPatch() throws Exception {
        // Initialize the database
        taxAcknowledgementReceiptRepository.saveAndFlush(taxAcknowledgementReceipt);

        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();

        // Update the taxAcknowledgementReceipt using partial update
        TaxAcknowledgementReceipt partialUpdatedTaxAcknowledgementReceipt = new TaxAcknowledgementReceipt();
        partialUpdatedTaxAcknowledgementReceipt.setId(taxAcknowledgementReceipt.getId());

        partialUpdatedTaxAcknowledgementReceipt
            .tinNumber(UPDATED_TIN_NUMBER)
            .dateOfSubmission(UPDATED_DATE_OF_SUBMISSION)
            .receivedAt(UPDATED_RECEIVED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxAcknowledgementReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxAcknowledgementReceipt))
            )
            .andExpect(status().isOk());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
        TaxAcknowledgementReceipt testTaxAcknowledgementReceipt = taxAcknowledgementReceiptList.get(
            taxAcknowledgementReceiptList.size() - 1
        );
        assertThat(testTaxAcknowledgementReceipt.getTinNumber()).isEqualTo(UPDATED_TIN_NUMBER);
        assertThat(testTaxAcknowledgementReceipt.getReceiptNumber()).isEqualTo(DEFAULT_RECEIPT_NUMBER);
        assertThat(testTaxAcknowledgementReceipt.getTaxesCircle()).isEqualTo(DEFAULT_TAXES_CIRCLE);
        assertThat(testTaxAcknowledgementReceipt.getTaxesZone()).isEqualTo(DEFAULT_TAXES_ZONE);
        assertThat(testTaxAcknowledgementReceipt.getDateOfSubmission()).isEqualTo(UPDATED_DATE_OF_SUBMISSION);
        assertThat(testTaxAcknowledgementReceipt.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testTaxAcknowledgementReceipt.getAcknowledgementStatus()).isEqualTo(DEFAULT_ACKNOWLEDGEMENT_STATUS);
        assertThat(testTaxAcknowledgementReceipt.getReceivedAt()).isEqualTo(UPDATED_RECEIVED_AT);
        assertThat(testTaxAcknowledgementReceipt.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaxAcknowledgementReceipt.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateTaxAcknowledgementReceiptWithPatch() throws Exception {
        // Initialize the database
        taxAcknowledgementReceiptRepository.saveAndFlush(taxAcknowledgementReceipt);

        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();

        // Update the taxAcknowledgementReceipt using partial update
        TaxAcknowledgementReceipt partialUpdatedTaxAcknowledgementReceipt = new TaxAcknowledgementReceipt();
        partialUpdatedTaxAcknowledgementReceipt.setId(taxAcknowledgementReceipt.getId());

        partialUpdatedTaxAcknowledgementReceipt
            .tinNumber(UPDATED_TIN_NUMBER)
            .receiptNumber(UPDATED_RECEIPT_NUMBER)
            .taxesCircle(UPDATED_TAXES_CIRCLE)
            .taxesZone(UPDATED_TAXES_ZONE)
            .dateOfSubmission(UPDATED_DATE_OF_SUBMISSION)
            .filePath(UPDATED_FILE_PATH)
            .acknowledgementStatus(UPDATED_ACKNOWLEDGEMENT_STATUS)
            .receivedAt(UPDATED_RECEIVED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restTaxAcknowledgementReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxAcknowledgementReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxAcknowledgementReceipt))
            )
            .andExpect(status().isOk());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
        TaxAcknowledgementReceipt testTaxAcknowledgementReceipt = taxAcknowledgementReceiptList.get(
            taxAcknowledgementReceiptList.size() - 1
        );
        assertThat(testTaxAcknowledgementReceipt.getTinNumber()).isEqualTo(UPDATED_TIN_NUMBER);
        assertThat(testTaxAcknowledgementReceipt.getReceiptNumber()).isEqualTo(UPDATED_RECEIPT_NUMBER);
        assertThat(testTaxAcknowledgementReceipt.getTaxesCircle()).isEqualTo(UPDATED_TAXES_CIRCLE);
        assertThat(testTaxAcknowledgementReceipt.getTaxesZone()).isEqualTo(UPDATED_TAXES_ZONE);
        assertThat(testTaxAcknowledgementReceipt.getDateOfSubmission()).isEqualTo(UPDATED_DATE_OF_SUBMISSION);
        assertThat(testTaxAcknowledgementReceipt.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testTaxAcknowledgementReceipt.getAcknowledgementStatus()).isEqualTo(UPDATED_ACKNOWLEDGEMENT_STATUS);
        assertThat(testTaxAcknowledgementReceipt.getReceivedAt()).isEqualTo(UPDATED_RECEIVED_AT);
        assertThat(testTaxAcknowledgementReceipt.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTaxAcknowledgementReceipt.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingTaxAcknowledgementReceipt() throws Exception {
        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();
        taxAcknowledgementReceipt.setId(count.incrementAndGet());

        // Create the TaxAcknowledgementReceipt
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxAcknowledgementReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxAcknowledgementReceiptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxAcknowledgementReceipt() throws Exception {
        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();
        taxAcknowledgementReceipt.setId(count.incrementAndGet());

        // Create the TaxAcknowledgementReceipt
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAcknowledgementReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxAcknowledgementReceipt() throws Exception {
        int databaseSizeBeforeUpdate = taxAcknowledgementReceiptRepository.findAll().size();
        taxAcknowledgementReceipt.setId(count.incrementAndGet());

        // Create the TaxAcknowledgementReceipt
        TaxAcknowledgementReceiptDTO taxAcknowledgementReceiptDTO = taxAcknowledgementReceiptMapper.toDto(taxAcknowledgementReceipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxAcknowledgementReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxAcknowledgementReceiptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxAcknowledgementReceipt in the database
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTaxAcknowledgementReceipt() throws Exception {
        // Initialize the database
        taxAcknowledgementReceiptRepository.saveAndFlush(taxAcknowledgementReceipt);

        int databaseSizeBeforeDelete = taxAcknowledgementReceiptRepository.findAll().size();

        // Delete the taxAcknowledgementReceipt
        restTaxAcknowledgementReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxAcknowledgementReceipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findAll();
        assertThat(taxAcknowledgementReceiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
