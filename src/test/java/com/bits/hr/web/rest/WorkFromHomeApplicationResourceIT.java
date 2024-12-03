package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.WorkFromHomeApplication;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.WorkFromHomeApplicationRepository;
import com.bits.hr.service.WorkFromHomeApplicationService;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import com.bits.hr.service.mapper.WorkFromHomeApplicationMapper;
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
 * Integration tests for the {@link WorkFromHomeApplicationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkFromHomeApplicationResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final LocalDate DEFAULT_APPLIED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLIED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SANCTIONED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SANCTIONED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/work-from-home-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkFromHomeApplicationRepository workFromHomeApplicationRepository;

    @Mock
    private WorkFromHomeApplicationRepository workFromHomeApplicationRepositoryMock;

    @Autowired
    private WorkFromHomeApplicationMapper workFromHomeApplicationMapper;

    @Mock
    private WorkFromHomeApplicationService workFromHomeApplicationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkFromHomeApplicationMockMvc;

    private WorkFromHomeApplication workFromHomeApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkFromHomeApplication createEntity(EntityManager em) {
        WorkFromHomeApplication workFromHomeApplication = new WorkFromHomeApplication()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .reason(DEFAULT_REASON)
            .duration(DEFAULT_DURATION)
            .status(DEFAULT_STATUS)
            .appliedAt(DEFAULT_APPLIED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .sanctionedAt(DEFAULT_SANCTIONED_AT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        workFromHomeApplication.setEmployee(employee);
        return workFromHomeApplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkFromHomeApplication createUpdatedEntity(EntityManager em) {
        WorkFromHomeApplication workFromHomeApplication = new WorkFromHomeApplication()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .duration(UPDATED_DURATION)
            .status(UPDATED_STATUS)
            .appliedAt(UPDATED_APPLIED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .sanctionedAt(UPDATED_SANCTIONED_AT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        workFromHomeApplication.setEmployee(employee);
        return workFromHomeApplication;
    }

    @BeforeEach
    public void initTest() {
        workFromHomeApplication = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkFromHomeApplication() throws Exception {
        int databaseSizeBeforeCreate = workFromHomeApplicationRepository.findAll().size();
        // Create the WorkFromHomeApplication
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);
        restWorkFromHomeApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeCreate + 1);
        WorkFromHomeApplication testWorkFromHomeApplication = workFromHomeApplicationList.get(workFromHomeApplicationList.size() - 1);
        assertThat(testWorkFromHomeApplication.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWorkFromHomeApplication.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testWorkFromHomeApplication.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testWorkFromHomeApplication.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testWorkFromHomeApplication.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWorkFromHomeApplication.getAppliedAt()).isEqualTo(DEFAULT_APPLIED_AT);
        assertThat(testWorkFromHomeApplication.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testWorkFromHomeApplication.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testWorkFromHomeApplication.getSanctionedAt()).isEqualTo(DEFAULT_SANCTIONED_AT);
    }

    @Test
    @Transactional
    void createWorkFromHomeApplicationWithExistingId() throws Exception {
        // Create the WorkFromHomeApplication with an existing ID
        workFromHomeApplication.setId(1L);
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        int databaseSizeBeforeCreate = workFromHomeApplicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkFromHomeApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = workFromHomeApplicationRepository.findAll().size();
        // set the field null
        workFromHomeApplication.setStartDate(null);

        // Create the WorkFromHomeApplication, which fails.
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        restWorkFromHomeApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = workFromHomeApplicationRepository.findAll().size();
        // set the field null
        workFromHomeApplication.setEndDate(null);

        // Create the WorkFromHomeApplication, which fails.
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        restWorkFromHomeApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = workFromHomeApplicationRepository.findAll().size();
        // set the field null
        workFromHomeApplication.setReason(null);

        // Create the WorkFromHomeApplication, which fails.
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        restWorkFromHomeApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = workFromHomeApplicationRepository.findAll().size();
        // set the field null
        workFromHomeApplication.setStatus(null);

        // Create the WorkFromHomeApplication, which fails.
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        restWorkFromHomeApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkFromHomeApplications() throws Exception {
        // Initialize the database
        workFromHomeApplicationRepository.saveAndFlush(workFromHomeApplication);

        // Get all the workFromHomeApplicationList
        restWorkFromHomeApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workFromHomeApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].appliedAt").value(hasItem(DEFAULT_APPLIED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].sanctionedAt").value(hasItem(DEFAULT_SANCTIONED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkFromHomeApplicationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(workFromHomeApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkFromHomeApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workFromHomeApplicationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkFromHomeApplicationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workFromHomeApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkFromHomeApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(workFromHomeApplicationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWorkFromHomeApplication() throws Exception {
        // Initialize the database
        workFromHomeApplicationRepository.saveAndFlush(workFromHomeApplication);

        // Get the workFromHomeApplication
        restWorkFromHomeApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, workFromHomeApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workFromHomeApplication.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.appliedAt").value(DEFAULT_APPLIED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.sanctionedAt").value(DEFAULT_SANCTIONED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkFromHomeApplication() throws Exception {
        // Get the workFromHomeApplication
        restWorkFromHomeApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkFromHomeApplication() throws Exception {
        // Initialize the database
        workFromHomeApplicationRepository.saveAndFlush(workFromHomeApplication);

        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();

        // Update the workFromHomeApplication
        WorkFromHomeApplication updatedWorkFromHomeApplication = workFromHomeApplicationRepository
            .findById(workFromHomeApplication.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkFromHomeApplication are not directly saved in db
        em.detach(updatedWorkFromHomeApplication);
        updatedWorkFromHomeApplication
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .duration(UPDATED_DURATION)
            .status(UPDATED_STATUS)
            .appliedAt(UPDATED_APPLIED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .sanctionedAt(UPDATED_SANCTIONED_AT);
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(updatedWorkFromHomeApplication);

        restWorkFromHomeApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workFromHomeApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
        WorkFromHomeApplication testWorkFromHomeApplication = workFromHomeApplicationList.get(workFromHomeApplicationList.size() - 1);
        assertThat(testWorkFromHomeApplication.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkFromHomeApplication.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testWorkFromHomeApplication.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testWorkFromHomeApplication.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testWorkFromHomeApplication.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWorkFromHomeApplication.getAppliedAt()).isEqualTo(UPDATED_APPLIED_AT);
        assertThat(testWorkFromHomeApplication.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testWorkFromHomeApplication.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWorkFromHomeApplication.getSanctionedAt()).isEqualTo(UPDATED_SANCTIONED_AT);
    }

    @Test
    @Transactional
    void putNonExistingWorkFromHomeApplication() throws Exception {
        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();
        workFromHomeApplication.setId(count.incrementAndGet());

        // Create the WorkFromHomeApplication
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkFromHomeApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workFromHomeApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkFromHomeApplication() throws Exception {
        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();
        workFromHomeApplication.setId(count.incrementAndGet());

        // Create the WorkFromHomeApplication
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkFromHomeApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkFromHomeApplication() throws Exception {
        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();
        workFromHomeApplication.setId(count.incrementAndGet());

        // Create the WorkFromHomeApplication
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkFromHomeApplicationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkFromHomeApplicationWithPatch() throws Exception {
        // Initialize the database
        workFromHomeApplicationRepository.saveAndFlush(workFromHomeApplication);

        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();

        // Update the workFromHomeApplication using partial update
        WorkFromHomeApplication partialUpdatedWorkFromHomeApplication = new WorkFromHomeApplication();
        partialUpdatedWorkFromHomeApplication.setId(workFromHomeApplication.getId());

        partialUpdatedWorkFromHomeApplication.startDate(UPDATED_START_DATE).duration(UPDATED_DURATION);

        restWorkFromHomeApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkFromHomeApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkFromHomeApplication))
            )
            .andExpect(status().isOk());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
        WorkFromHomeApplication testWorkFromHomeApplication = workFromHomeApplicationList.get(workFromHomeApplicationList.size() - 1);
        assertThat(testWorkFromHomeApplication.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkFromHomeApplication.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testWorkFromHomeApplication.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testWorkFromHomeApplication.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testWorkFromHomeApplication.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWorkFromHomeApplication.getAppliedAt()).isEqualTo(DEFAULT_APPLIED_AT);
        assertThat(testWorkFromHomeApplication.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testWorkFromHomeApplication.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testWorkFromHomeApplication.getSanctionedAt()).isEqualTo(DEFAULT_SANCTIONED_AT);
    }

    @Test
    @Transactional
    void fullUpdateWorkFromHomeApplicationWithPatch() throws Exception {
        // Initialize the database
        workFromHomeApplicationRepository.saveAndFlush(workFromHomeApplication);

        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();

        // Update the workFromHomeApplication using partial update
        WorkFromHomeApplication partialUpdatedWorkFromHomeApplication = new WorkFromHomeApplication();
        partialUpdatedWorkFromHomeApplication.setId(workFromHomeApplication.getId());

        partialUpdatedWorkFromHomeApplication
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .duration(UPDATED_DURATION)
            .status(UPDATED_STATUS)
            .appliedAt(UPDATED_APPLIED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .sanctionedAt(UPDATED_SANCTIONED_AT);

        restWorkFromHomeApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkFromHomeApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkFromHomeApplication))
            )
            .andExpect(status().isOk());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
        WorkFromHomeApplication testWorkFromHomeApplication = workFromHomeApplicationList.get(workFromHomeApplicationList.size() - 1);
        assertThat(testWorkFromHomeApplication.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkFromHomeApplication.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testWorkFromHomeApplication.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testWorkFromHomeApplication.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testWorkFromHomeApplication.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWorkFromHomeApplication.getAppliedAt()).isEqualTo(UPDATED_APPLIED_AT);
        assertThat(testWorkFromHomeApplication.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testWorkFromHomeApplication.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testWorkFromHomeApplication.getSanctionedAt()).isEqualTo(UPDATED_SANCTIONED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingWorkFromHomeApplication() throws Exception {
        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();
        workFromHomeApplication.setId(count.incrementAndGet());

        // Create the WorkFromHomeApplication
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkFromHomeApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workFromHomeApplicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkFromHomeApplication() throws Exception {
        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();
        workFromHomeApplication.setId(count.incrementAndGet());

        // Create the WorkFromHomeApplication
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkFromHomeApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkFromHomeApplication() throws Exception {
        int databaseSizeBeforeUpdate = workFromHomeApplicationRepository.findAll().size();
        workFromHomeApplication.setId(count.incrementAndGet());

        // Create the WorkFromHomeApplication
        WorkFromHomeApplicationDTO workFromHomeApplicationDTO = workFromHomeApplicationMapper.toDto(workFromHomeApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkFromHomeApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workFromHomeApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkFromHomeApplication in the database
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkFromHomeApplication() throws Exception {
        // Initialize the database
        workFromHomeApplicationRepository.saveAndFlush(workFromHomeApplication);

        int databaseSizeBeforeDelete = workFromHomeApplicationRepository.findAll().size();

        // Delete the workFromHomeApplication
        restWorkFromHomeApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, workFromHomeApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkFromHomeApplication> workFromHomeApplicationList = workFromHomeApplicationRepository.findAll();
        assertThat(workFromHomeApplicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
