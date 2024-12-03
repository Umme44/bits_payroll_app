package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexScheduleApplication;
import com.bits.hr.domain.TimeSlot;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.service.FlexScheduleApplicationService;
import com.bits.hr.service.dto.FlexScheduleApplicationDTO;
import com.bits.hr.service.mapper.FlexScheduleApplicationMapper;
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
 * Integration tests for the {@link FlexScheduleApplicationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FlexScheduleApplicationResourceIT {

    private static final LocalDate DEFAULT_EFFECTIVE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EFFECTIVE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final Instant DEFAULT_SANCTIONED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SANCTIONED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_APPLIED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLIED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/flex-schedule-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    @Mock
    private FlexScheduleApplicationRepository flexScheduleApplicationRepositoryMock;

    @Autowired
    private FlexScheduleApplicationMapper flexScheduleApplicationMapper;

    @Mock
    private FlexScheduleApplicationService flexScheduleApplicationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlexScheduleApplicationMockMvc;

    private FlexScheduleApplication flexScheduleApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexScheduleApplication createEntity(EntityManager em) {
        FlexScheduleApplication flexScheduleApplication = new FlexScheduleApplication()
            .effectiveFrom(DEFAULT_EFFECTIVE_FROM)
            .effectiveTo(DEFAULT_EFFECTIVE_TO)
            .reason(DEFAULT_REASON)
            .status(DEFAULT_STATUS)
            .sanctionedAt(DEFAULT_SANCTIONED_AT)
            .appliedAt(DEFAULT_APPLIED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        flexScheduleApplication.setRequester(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        flexScheduleApplication.setCreatedBy(user);
        // Add required entity
        TimeSlot timeSlot;
        if (TestUtil.findAll(em, TimeSlot.class).isEmpty()) {
            timeSlot = TimeSlotResourceIT.createEntity(em);
            em.persist(timeSlot);
            em.flush();
        } else {
            timeSlot = TestUtil.findAll(em, TimeSlot.class).get(0);
        }
        flexScheduleApplication.setTimeSlot(timeSlot);
        return flexScheduleApplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlexScheduleApplication createUpdatedEntity(EntityManager em) {
        FlexScheduleApplication flexScheduleApplication = new FlexScheduleApplication()
            .effectiveFrom(UPDATED_EFFECTIVE_FROM)
            .effectiveTo(UPDATED_EFFECTIVE_TO)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .sanctionedAt(UPDATED_SANCTIONED_AT)
            .appliedAt(UPDATED_APPLIED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        flexScheduleApplication.setRequester(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        flexScheduleApplication.setCreatedBy(user);
        // Add required entity
        TimeSlot timeSlot;
        if (TestUtil.findAll(em, TimeSlot.class).isEmpty()) {
            timeSlot = TimeSlotResourceIT.createUpdatedEntity(em);
            em.persist(timeSlot);
            em.flush();
        } else {
            timeSlot = TestUtil.findAll(em, TimeSlot.class).get(0);
        }
        flexScheduleApplication.setTimeSlot(timeSlot);
        return flexScheduleApplication;
    }

    @BeforeEach
    public void initTest() {
        flexScheduleApplication = createEntity(em);
    }

    @Test
    @Transactional
    void createFlexScheduleApplication() throws Exception {
        int databaseSizeBeforeCreate = flexScheduleApplicationRepository.findAll().size();
        // Create the FlexScheduleApplication
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);
        restFlexScheduleApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeCreate + 1);
        FlexScheduleApplication testFlexScheduleApplication = flexScheduleApplicationList.get(flexScheduleApplicationList.size() - 1);
        assertThat(testFlexScheduleApplication.getEffectiveFrom()).isEqualTo(DEFAULT_EFFECTIVE_FROM);
        assertThat(testFlexScheduleApplication.getEffectiveTo()).isEqualTo(DEFAULT_EFFECTIVE_TO);
        assertThat(testFlexScheduleApplication.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testFlexScheduleApplication.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFlexScheduleApplication.getSanctionedAt()).isEqualTo(DEFAULT_SANCTIONED_AT);
        assertThat(testFlexScheduleApplication.getAppliedAt()).isEqualTo(DEFAULT_APPLIED_AT);
        assertThat(testFlexScheduleApplication.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testFlexScheduleApplication.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createFlexScheduleApplicationWithExistingId() throws Exception {
        // Create the FlexScheduleApplication with an existing ID
        flexScheduleApplication.setId(1L);
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        int databaseSizeBeforeCreate = flexScheduleApplicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlexScheduleApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEffectiveFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = flexScheduleApplicationRepository.findAll().size();
        // set the field null
        flexScheduleApplication.setEffectiveFrom(null);

        // Create the FlexScheduleApplication, which fails.
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        restFlexScheduleApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEffectiveToIsRequired() throws Exception {
        int databaseSizeBeforeTest = flexScheduleApplicationRepository.findAll().size();
        // set the field null
        flexScheduleApplication.setEffectiveTo(null);

        // Create the FlexScheduleApplication, which fails.
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        restFlexScheduleApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = flexScheduleApplicationRepository.findAll().size();
        // set the field null
        flexScheduleApplication.setStatus(null);

        // Create the FlexScheduleApplication, which fails.
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        restFlexScheduleApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAppliedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = flexScheduleApplicationRepository.findAll().size();
        // set the field null
        flexScheduleApplication.setAppliedAt(null);

        // Create the FlexScheduleApplication, which fails.
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        restFlexScheduleApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = flexScheduleApplicationRepository.findAll().size();
        // set the field null
        flexScheduleApplication.setCreatedAt(null);

        // Create the FlexScheduleApplication, which fails.
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        restFlexScheduleApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFlexScheduleApplications() throws Exception {
        // Initialize the database
        flexScheduleApplicationRepository.saveAndFlush(flexScheduleApplication);

        // Get all the flexScheduleApplicationList
        restFlexScheduleApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flexScheduleApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].effectiveFrom").value(hasItem(DEFAULT_EFFECTIVE_FROM.toString())))
            .andExpect(jsonPath("$.[*].effectiveTo").value(hasItem(DEFAULT_EFFECTIVE_TO.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sanctionedAt").value(hasItem(DEFAULT_SANCTIONED_AT.toString())))
            .andExpect(jsonPath("$.[*].appliedAt").value(hasItem(DEFAULT_APPLIED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlexScheduleApplicationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(flexScheduleApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlexScheduleApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(flexScheduleApplicationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlexScheduleApplicationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(flexScheduleApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlexScheduleApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(flexScheduleApplicationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFlexScheduleApplication() throws Exception {
        // Initialize the database
        flexScheduleApplicationRepository.saveAndFlush(flexScheduleApplication);

        // Get the flexScheduleApplication
        restFlexScheduleApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, flexScheduleApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flexScheduleApplication.getId().intValue()))
            .andExpect(jsonPath("$.effectiveFrom").value(DEFAULT_EFFECTIVE_FROM.toString()))
            .andExpect(jsonPath("$.effectiveTo").value(DEFAULT_EFFECTIVE_TO.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.sanctionedAt").value(DEFAULT_SANCTIONED_AT.toString()))
            .andExpect(jsonPath("$.appliedAt").value(DEFAULT_APPLIED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFlexScheduleApplication() throws Exception {
        // Get the flexScheduleApplication
        restFlexScheduleApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFlexScheduleApplication() throws Exception {
        // Initialize the database
        flexScheduleApplicationRepository.saveAndFlush(flexScheduleApplication);

        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();

        // Update the flexScheduleApplication
        FlexScheduleApplication updatedFlexScheduleApplication = flexScheduleApplicationRepository
            .findById(flexScheduleApplication.getId())
            .get();
        // Disconnect from session so that the updates on updatedFlexScheduleApplication are not directly saved in db
        em.detach(updatedFlexScheduleApplication);
        updatedFlexScheduleApplication
            .effectiveFrom(UPDATED_EFFECTIVE_FROM)
            .effectiveTo(UPDATED_EFFECTIVE_TO)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .sanctionedAt(UPDATED_SANCTIONED_AT)
            .appliedAt(UPDATED_APPLIED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(updatedFlexScheduleApplication);

        restFlexScheduleApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexScheduleApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
        FlexScheduleApplication testFlexScheduleApplication = flexScheduleApplicationList.get(flexScheduleApplicationList.size() - 1);
        assertThat(testFlexScheduleApplication.getEffectiveFrom()).isEqualTo(UPDATED_EFFECTIVE_FROM);
        assertThat(testFlexScheduleApplication.getEffectiveTo()).isEqualTo(UPDATED_EFFECTIVE_TO);
        assertThat(testFlexScheduleApplication.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testFlexScheduleApplication.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFlexScheduleApplication.getSanctionedAt()).isEqualTo(UPDATED_SANCTIONED_AT);
        assertThat(testFlexScheduleApplication.getAppliedAt()).isEqualTo(UPDATED_APPLIED_AT);
        assertThat(testFlexScheduleApplication.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFlexScheduleApplication.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingFlexScheduleApplication() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();
        flexScheduleApplication.setId(count.incrementAndGet());

        // Create the FlexScheduleApplication
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexScheduleApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flexScheduleApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlexScheduleApplication() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();
        flexScheduleApplication.setId(count.incrementAndGet());

        // Create the FlexScheduleApplication
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexScheduleApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlexScheduleApplication() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();
        flexScheduleApplication.setId(count.incrementAndGet());

        // Create the FlexScheduleApplication
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexScheduleApplicationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlexScheduleApplicationWithPatch() throws Exception {
        // Initialize the database
        flexScheduleApplicationRepository.saveAndFlush(flexScheduleApplication);

        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();

        // Update the flexScheduleApplication using partial update
        FlexScheduleApplication partialUpdatedFlexScheduleApplication = new FlexScheduleApplication();
        partialUpdatedFlexScheduleApplication.setId(flexScheduleApplication.getId());

        partialUpdatedFlexScheduleApplication.effectiveTo(UPDATED_EFFECTIVE_TO).createdAt(UPDATED_CREATED_AT);

        restFlexScheduleApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexScheduleApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexScheduleApplication))
            )
            .andExpect(status().isOk());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
        FlexScheduleApplication testFlexScheduleApplication = flexScheduleApplicationList.get(flexScheduleApplicationList.size() - 1);
        assertThat(testFlexScheduleApplication.getEffectiveFrom()).isEqualTo(DEFAULT_EFFECTIVE_FROM);
        assertThat(testFlexScheduleApplication.getEffectiveTo()).isEqualTo(UPDATED_EFFECTIVE_TO);
        assertThat(testFlexScheduleApplication.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testFlexScheduleApplication.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFlexScheduleApplication.getSanctionedAt()).isEqualTo(DEFAULT_SANCTIONED_AT);
        assertThat(testFlexScheduleApplication.getAppliedAt()).isEqualTo(DEFAULT_APPLIED_AT);
        assertThat(testFlexScheduleApplication.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFlexScheduleApplication.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateFlexScheduleApplicationWithPatch() throws Exception {
        // Initialize the database
        flexScheduleApplicationRepository.saveAndFlush(flexScheduleApplication);

        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();

        // Update the flexScheduleApplication using partial update
        FlexScheduleApplication partialUpdatedFlexScheduleApplication = new FlexScheduleApplication();
        partialUpdatedFlexScheduleApplication.setId(flexScheduleApplication.getId());

        partialUpdatedFlexScheduleApplication
            .effectiveFrom(UPDATED_EFFECTIVE_FROM)
            .effectiveTo(UPDATED_EFFECTIVE_TO)
            .reason(UPDATED_REASON)
            .status(UPDATED_STATUS)
            .sanctionedAt(UPDATED_SANCTIONED_AT)
            .appliedAt(UPDATED_APPLIED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restFlexScheduleApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlexScheduleApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlexScheduleApplication))
            )
            .andExpect(status().isOk());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
        FlexScheduleApplication testFlexScheduleApplication = flexScheduleApplicationList.get(flexScheduleApplicationList.size() - 1);
        assertThat(testFlexScheduleApplication.getEffectiveFrom()).isEqualTo(UPDATED_EFFECTIVE_FROM);
        assertThat(testFlexScheduleApplication.getEffectiveTo()).isEqualTo(UPDATED_EFFECTIVE_TO);
        assertThat(testFlexScheduleApplication.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testFlexScheduleApplication.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFlexScheduleApplication.getSanctionedAt()).isEqualTo(UPDATED_SANCTIONED_AT);
        assertThat(testFlexScheduleApplication.getAppliedAt()).isEqualTo(UPDATED_APPLIED_AT);
        assertThat(testFlexScheduleApplication.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFlexScheduleApplication.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingFlexScheduleApplication() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();
        flexScheduleApplication.setId(count.incrementAndGet());

        // Create the FlexScheduleApplication
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlexScheduleApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flexScheduleApplicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlexScheduleApplication() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();
        flexScheduleApplication.setId(count.incrementAndGet());

        // Create the FlexScheduleApplication
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexScheduleApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlexScheduleApplication() throws Exception {
        int databaseSizeBeforeUpdate = flexScheduleApplicationRepository.findAll().size();
        flexScheduleApplication.setId(count.incrementAndGet());

        // Create the FlexScheduleApplication
        FlexScheduleApplicationDTO flexScheduleApplicationDTO = flexScheduleApplicationMapper.toDto(flexScheduleApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlexScheduleApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flexScheduleApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlexScheduleApplication in the database
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlexScheduleApplication() throws Exception {
        // Initialize the database
        flexScheduleApplicationRepository.saveAndFlush(flexScheduleApplication);

        int databaseSizeBeforeDelete = flexScheduleApplicationRepository.findAll().size();

        // Delete the flexScheduleApplication
        restFlexScheduleApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, flexScheduleApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlexScheduleApplication> flexScheduleApplicationList = flexScheduleApplicationRepository.findAll();
        assertThat(flexScheduleApplicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
