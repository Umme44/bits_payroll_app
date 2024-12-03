package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentCertificate;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.repository.EmploymentCertificateRepository;
import com.bits.hr.service.EmploymentCertificateService;
import com.bits.hr.service.dto.EmploymentCertificateDTO;
import com.bits.hr.service.mapper.EmploymentCertificateMapper;
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

/**
 * Integration tests for the {@link EmploymentCertificateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmploymentCertificateResourceIT {

    private static final CertificateStatus DEFAULT_CERTIFICATE_STATUS = CertificateStatus.SENT_FOR_GENERATION;
    private static final CertificateStatus UPDATED_CERTIFICATE_STATUS = CertificateStatus.REJECTED;

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_GENERATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GENERATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/employment-certificates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmploymentCertificateRepository employmentCertificateRepository;

    @Mock
    private EmploymentCertificateRepository employmentCertificateRepositoryMock;

    @Autowired
    private EmploymentCertificateMapper employmentCertificateMapper;

    @Mock
    private EmploymentCertificateService employmentCertificateServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentCertificateMockMvc;

    private EmploymentCertificate employmentCertificate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentCertificate createEntity(EntityManager em) {
        EmploymentCertificate employmentCertificate = new EmploymentCertificate()
            .certificateStatus(DEFAULT_CERTIFICATE_STATUS)
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .issueDate(DEFAULT_ISSUE_DATE)
            .reason(DEFAULT_REASON)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .generatedAt(DEFAULT_GENERATED_AT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employmentCertificate.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employmentCertificate.setCreatedBy(user);
        return employmentCertificate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentCertificate createUpdatedEntity(EntityManager em) {
        EmploymentCertificate employmentCertificate = new EmploymentCertificate()
            .certificateStatus(UPDATED_CERTIFICATE_STATUS)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .generatedAt(UPDATED_GENERATED_AT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employmentCertificate.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employmentCertificate.setCreatedBy(user);
        return employmentCertificate;
    }

    @BeforeEach
    public void initTest() {
        employmentCertificate = createEntity(em);
    }

    @Test
    @Transactional
    void createEmploymentCertificate() throws Exception {
        int databaseSizeBeforeCreate = employmentCertificateRepository.findAll().size();
        // Create the EmploymentCertificate
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);
        restEmploymentCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeCreate + 1);
        EmploymentCertificate testEmploymentCertificate = employmentCertificateList.get(employmentCertificateList.size() - 1);
        assertThat(testEmploymentCertificate.getCertificateStatus()).isEqualTo(DEFAULT_CERTIFICATE_STATUS);
        assertThat(testEmploymentCertificate.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testEmploymentCertificate.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testEmploymentCertificate.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testEmploymentCertificate.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmploymentCertificate.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmploymentCertificate.getGeneratedAt()).isEqualTo(DEFAULT_GENERATED_AT);
    }

    @Test
    @Transactional
    void createEmploymentCertificateWithExistingId() throws Exception {
        // Create the EmploymentCertificate with an existing ID
        employmentCertificate.setId(1L);
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        int databaseSizeBeforeCreate = employmentCertificateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCertificateStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = employmentCertificateRepository.findAll().size();
        // set the field null
        employmentCertificate.setCertificateStatus(null);

        // Create the EmploymentCertificate, which fails.
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        restEmploymentCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = employmentCertificateRepository.findAll().size();
        // set the field null
        employmentCertificate.setCreatedAt(null);

        // Create the EmploymentCertificate, which fails.
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        restEmploymentCertificateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmploymentCertificates() throws Exception {
        // Initialize the database
        employmentCertificateRepository.saveAndFlush(employmentCertificate);

        // Get all the employmentCertificateList
        restEmploymentCertificateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentCertificate.getId().intValue())))
            .andExpect(jsonPath("$.[*].certificateStatus").value(hasItem(DEFAULT_CERTIFICATE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(DEFAULT_GENERATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmploymentCertificatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employmentCertificateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmploymentCertificateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employmentCertificateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmploymentCertificatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employmentCertificateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmploymentCertificateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employmentCertificateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmploymentCertificate() throws Exception {
        // Initialize the database
        employmentCertificateRepository.saveAndFlush(employmentCertificate);

        // Get the employmentCertificate
        restEmploymentCertificateMockMvc
            .perform(get(ENTITY_API_URL_ID, employmentCertificate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employmentCertificate.getId().intValue()))
            .andExpect(jsonPath("$.certificateStatus").value(DEFAULT_CERTIFICATE_STATUS.toString()))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.generatedAt").value(DEFAULT_GENERATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmploymentCertificate() throws Exception {
        // Get the employmentCertificate
        restEmploymentCertificateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmploymentCertificate() throws Exception {
        // Initialize the database
        employmentCertificateRepository.saveAndFlush(employmentCertificate);

        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();

        // Update the employmentCertificate
        EmploymentCertificate updatedEmploymentCertificate = employmentCertificateRepository.findById(employmentCertificate.getId()).get();
        // Disconnect from session so that the updates on updatedEmploymentCertificate are not directly saved in db
        em.detach(updatedEmploymentCertificate);
        updatedEmploymentCertificate
            .certificateStatus(UPDATED_CERTIFICATE_STATUS)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .generatedAt(UPDATED_GENERATED_AT);
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(updatedEmploymentCertificate);

        restEmploymentCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentCertificateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
        EmploymentCertificate testEmploymentCertificate = employmentCertificateList.get(employmentCertificateList.size() - 1);
        assertThat(testEmploymentCertificate.getCertificateStatus()).isEqualTo(UPDATED_CERTIFICATE_STATUS);
        assertThat(testEmploymentCertificate.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testEmploymentCertificate.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testEmploymentCertificate.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testEmploymentCertificate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmploymentCertificate.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmploymentCertificate.getGeneratedAt()).isEqualTo(UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEmploymentCertificate() throws Exception {
        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();
        employmentCertificate.setId(count.incrementAndGet());

        // Create the EmploymentCertificate
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentCertificateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploymentCertificate() throws Exception {
        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();
        employmentCertificate.setId(count.incrementAndGet());

        // Create the EmploymentCertificate
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentCertificateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploymentCertificate() throws Exception {
        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();
        employmentCertificate.setId(count.incrementAndGet());

        // Create the EmploymentCertificate
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentCertificateMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentCertificateWithPatch() throws Exception {
        // Initialize the database
        employmentCertificateRepository.saveAndFlush(employmentCertificate);

        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();

        // Update the employmentCertificate using partial update
        EmploymentCertificate partialUpdatedEmploymentCertificate = new EmploymentCertificate();
        partialUpdatedEmploymentCertificate.setId(employmentCertificate.getId());

        partialUpdatedEmploymentCertificate.reason(UPDATED_REASON).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restEmploymentCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentCertificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentCertificate))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
        EmploymentCertificate testEmploymentCertificate = employmentCertificateList.get(employmentCertificateList.size() - 1);
        assertThat(testEmploymentCertificate.getCertificateStatus()).isEqualTo(DEFAULT_CERTIFICATE_STATUS);
        assertThat(testEmploymentCertificate.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testEmploymentCertificate.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testEmploymentCertificate.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testEmploymentCertificate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmploymentCertificate.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmploymentCertificate.getGeneratedAt()).isEqualTo(DEFAULT_GENERATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEmploymentCertificateWithPatch() throws Exception {
        // Initialize the database
        employmentCertificateRepository.saveAndFlush(employmentCertificate);

        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();

        // Update the employmentCertificate using partial update
        EmploymentCertificate partialUpdatedEmploymentCertificate = new EmploymentCertificate();
        partialUpdatedEmploymentCertificate.setId(employmentCertificate.getId());

        partialUpdatedEmploymentCertificate
            .certificateStatus(UPDATED_CERTIFICATE_STATUS)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .reason(UPDATED_REASON)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .generatedAt(UPDATED_GENERATED_AT);

        restEmploymentCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentCertificate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmploymentCertificate))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
        EmploymentCertificate testEmploymentCertificate = employmentCertificateList.get(employmentCertificateList.size() - 1);
        assertThat(testEmploymentCertificate.getCertificateStatus()).isEqualTo(UPDATED_CERTIFICATE_STATUS);
        assertThat(testEmploymentCertificate.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testEmploymentCertificate.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testEmploymentCertificate.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testEmploymentCertificate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmploymentCertificate.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmploymentCertificate.getGeneratedAt()).isEqualTo(UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEmploymentCertificate() throws Exception {
        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();
        employmentCertificate.setId(count.incrementAndGet());

        // Create the EmploymentCertificate
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employmentCertificateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploymentCertificate() throws Exception {
        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();
        employmentCertificate.setId(count.incrementAndGet());

        // Create the EmploymentCertificate
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploymentCertificate() throws Exception {
        int databaseSizeBeforeUpdate = employmentCertificateRepository.findAll().size();
        employmentCertificate.setId(count.incrementAndGet());

        // Create the EmploymentCertificate
        EmploymentCertificateDTO employmentCertificateDTO = employmentCertificateMapper.toDto(employmentCertificate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentCertificateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employmentCertificateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentCertificate in the database
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmploymentCertificate() throws Exception {
        // Initialize the database
        employmentCertificateRepository.saveAndFlush(employmentCertificate);

        int databaseSizeBeforeDelete = employmentCertificateRepository.findAll().size();

        // Delete the employmentCertificate
        restEmploymentCertificateMockMvc
            .perform(delete(ENTITY_API_URL_ID, employmentCertificate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmploymentCertificate> employmentCertificateList = employmentCertificateRepository.findAll();
        assertThat(employmentCertificateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
