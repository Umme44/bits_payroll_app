package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.SalaryCertificateRepository;
import com.bits.hr.service.SalaryCertificateService;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import com.bits.hr.service.mapper.SalaryCertificateMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link SalaryCertificateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SalaryCertificateResourceIT {

    private static final String DEFAULT_PURPOSE = "AAAAAAAAAA";
    private static final String UPDATED_PURPOSE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SANCTION_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SANCTION_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Month DEFAULT_MONTH = Month.JANUARY;
    private static final Month UPDATED_MONTH = Month.FEBRUARY;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/salary-certificates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalaryCertificateRepository salaryCertificateRepository;

    @Mock
    private SalaryCertificateRepository salaryCertificateRepositoryMock;

    @Autowired
    private SalaryCertificateMapper salaryCertificateMapper;

    @Mock
    private SalaryCertificateService salaryCertificateServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaryCertificateMockMvc;

    private SalaryCertificate salaryCertificate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryCertificate createEntity(EntityManager em) {
        SalaryCertificate salaryCertificate = new SalaryCertificate()
            .purpose(DEFAULT_PURPOSE)
            .remarks(DEFAULT_REMARKS)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .sanctionAt(DEFAULT_SANCTION_AT)
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        salaryCertificate.setCreatedBy(user);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        salaryCertificate.setEmployee(employee);
        return salaryCertificate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryCertificate createUpdatedEntity(EntityManager em) {
        SalaryCertificate salaryCertificate = new SalaryCertificate()
            .purpose(UPDATED_PURPOSE)
            .remarks(UPDATED_REMARKS)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        salaryCertificate.setCreatedBy(user);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        salaryCertificate.setEmployee(employee);
        return salaryCertificate;
    }

    @BeforeEach
    public void initTest() {
        salaryCertificate = createEntity(em);
    }

    @Test
    @Transactional
    void createSalaryCertificate() throws Exception {
        int databaseSizeBeforeCreate = salaryCertificateRepository.findAll().size();
        // Create the SalaryCertificate
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);
        restSalaryCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeCreate + 1);
        SalaryCertificate testSalaryCertificate = salaryCertificateList.get(salaryCertificateList.size() - 1);
        assertThat(testSalaryCertificate.getPurpose()).isEqualTo(DEFAULT_PURPOSE);
        assertThat(testSalaryCertificate.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testSalaryCertificate.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSalaryCertificate.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSalaryCertificate.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSalaryCertificate.getSanctionAt()).isEqualTo(DEFAULT_SANCTION_AT);
        assertThat(testSalaryCertificate.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testSalaryCertificate.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void createSalaryCertificateWithExistingId() throws Exception {
        // Create the SalaryCertificate with an existing ID
        salaryCertificate.setId(1L);
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        int databaseSizeBeforeCreate = salaryCertificateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPurposeIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryCertificateRepository.findAll().size();
        // set the field null
        salaryCertificate.setPurpose(null);

        // Create the SalaryCertificate, which fails.
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        restSalaryCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryCertificateRepository.findAll().size();
        // set the field null
        salaryCertificate.setStatus(null);

        // Create the SalaryCertificate, which fails.
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        restSalaryCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryCertificateRepository.findAll().size();
        // set the field null
        salaryCertificate.setMonth(null);

        // Create the SalaryCertificate, which fails.
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        restSalaryCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = salaryCertificateRepository.findAll().size();
        // set the field null
        salaryCertificate.setYear(null);

        // Create the SalaryCertificate, which fails.
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        restSalaryCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalaryCertificates() throws Exception {
        // Initialize the database
        salaryCertificateRepository.saveAndFlush(salaryCertificate);

        // Get all the salaryCertificateList
        restSalaryCertificateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryCertificate.getId().intValue())))
            .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].sanctionAt").value(hasItem(DEFAULT_SANCTION_AT.toString())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSalaryCertificatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(salaryCertificateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalaryCertificateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(salaryCertificateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSalaryCertificatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(salaryCertificateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalaryCertificateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(salaryCertificateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSalaryCertificate() throws Exception {
        // Initialize the database
        salaryCertificateRepository.saveAndFlush(salaryCertificate);

        // Get the salaryCertificate
        restSalaryCertificateMockMvc
            .perform(get(ENTITY_API_URL_ID, salaryCertificate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salaryCertificate.getId().intValue()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.sanctionAt").value(DEFAULT_SANCTION_AT.toString()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingSalaryCertificate() throws Exception {
        // Get the salaryCertificate
        restSalaryCertificateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalaryCertificate() throws Exception {
        // Initialize the database
        salaryCertificateRepository.saveAndFlush(salaryCertificate);

        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();

        // Update the salaryCertificate
        SalaryCertificate updatedSalaryCertificate = salaryCertificateRepository.findById(salaryCertificate.getId()).get();
        // Disconnect from session so that the updates on updatedSalaryCertificate are not directly saved in db
        em.detach(updatedSalaryCertificate);
        updatedSalaryCertificate
            .purpose(UPDATED_PURPOSE)
            .remarks(UPDATED_REMARKS)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR);
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(updatedSalaryCertificate);

        restSalaryCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryCertificateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isOk());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
        SalaryCertificate testSalaryCertificate = salaryCertificateList.get(salaryCertificateList.size() - 1);
        assertThat(testSalaryCertificate.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testSalaryCertificate.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testSalaryCertificate.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSalaryCertificate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSalaryCertificate.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSalaryCertificate.getSanctionAt()).isEqualTo(UPDATED_SANCTION_AT);
        assertThat(testSalaryCertificate.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testSalaryCertificate.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingSalaryCertificate() throws Exception {
        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();
        salaryCertificate.setId(count.incrementAndGet());

        // Create the SalaryCertificate
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryCertificateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalaryCertificate() throws Exception {
        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();
        salaryCertificate.setId(count.incrementAndGet());

        // Create the SalaryCertificate
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalaryCertificate() throws Exception {
        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();
        salaryCertificate.setId(count.incrementAndGet());

        // Create the SalaryCertificate
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryCertificateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalaryCertificateWithPatch() throws Exception {
        // Initialize the database
        salaryCertificateRepository.saveAndFlush(salaryCertificate);

        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();

        // Update the salaryCertificate using partial update
        SalaryCertificate partialUpdatedSalaryCertificate = new SalaryCertificate();
        partialUpdatedSalaryCertificate.setId(salaryCertificate.getId());

        partialUpdatedSalaryCertificate
            .purpose(UPDATED_PURPOSE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR);

        restSalaryCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryCertificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaryCertificate))
            )
            .andExpect(status().isOk());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
        SalaryCertificate testSalaryCertificate = salaryCertificateList.get(salaryCertificateList.size() - 1);
        assertThat(testSalaryCertificate.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testSalaryCertificate.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testSalaryCertificate.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSalaryCertificate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSalaryCertificate.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSalaryCertificate.getSanctionAt()).isEqualTo(DEFAULT_SANCTION_AT);
        assertThat(testSalaryCertificate.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testSalaryCertificate.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateSalaryCertificateWithPatch() throws Exception {
        // Initialize the database
        salaryCertificateRepository.saveAndFlush(salaryCertificate);

        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();

        // Update the salaryCertificate using partial update
        SalaryCertificate partialUpdatedSalaryCertificate = new SalaryCertificate();
        partialUpdatedSalaryCertificate.setId(salaryCertificate.getId());

        partialUpdatedSalaryCertificate
            .purpose(UPDATED_PURPOSE)
            .remarks(UPDATED_REMARKS)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .sanctionAt(UPDATED_SANCTION_AT)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR);

        restSalaryCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryCertificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaryCertificate))
            )
            .andExpect(status().isOk());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
        SalaryCertificate testSalaryCertificate = salaryCertificateList.get(salaryCertificateList.size() - 1);
        assertThat(testSalaryCertificate.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testSalaryCertificate.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testSalaryCertificate.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSalaryCertificate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSalaryCertificate.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSalaryCertificate.getSanctionAt()).isEqualTo(UPDATED_SANCTION_AT);
        assertThat(testSalaryCertificate.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testSalaryCertificate.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingSalaryCertificate() throws Exception {
        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();
        salaryCertificate.setId(count.incrementAndGet());

        // Create the SalaryCertificate
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salaryCertificateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalaryCertificate() throws Exception {
        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();
        salaryCertificate.setId(count.incrementAndGet());

        // Create the SalaryCertificate
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalaryCertificate() throws Exception {
        int databaseSizeBeforeUpdate = salaryCertificateRepository.findAll().size();
        salaryCertificate.setId(count.incrementAndGet());

        // Create the SalaryCertificate
        SalaryCertificateDTO salaryCertificateDTO = salaryCertificateMapper.toDto(salaryCertificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryCertificateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryCertificate in the database
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalaryCertificate() throws Exception {
        // Initialize the database
        salaryCertificateRepository.saveAndFlush(salaryCertificate);

        int databaseSizeBeforeDelete = salaryCertificateRepository.findAll().size();

        // Delete the salaryCertificate
        restSalaryCertificateMockMvc
            .perform(delete(ENTITY_API_URL_ID, salaryCertificate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalaryCertificate> salaryCertificateList = salaryCertificateRepository.findAll();
        assertThat(salaryCertificateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
