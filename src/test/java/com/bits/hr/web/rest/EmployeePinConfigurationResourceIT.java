package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EmployeePinConfiguration;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.EmployeePinConfigurationRepository;
import com.bits.hr.service.EmployeePinConfigurationService;
import com.bits.hr.service.dto.EmployeePinConfigurationDTO;
import com.bits.hr.service.mapper.EmployeePinConfigurationMapper;
import java.time.Instant;
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
 * Integration tests for the {@link EmployeePinConfigurationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeePinConfigurationResourceIT {

    private static final EmployeeCategory DEFAULT_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
    private static final EmployeeCategory UPDATED_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;

    private static final String DEFAULT_SEQUENCE_START = "AAAAAAAAAA";
    private static final String UPDATED_SEQUENCE_START = "BBBBBBBBBB";

    private static final String DEFAULT_SEQUENCE_END = "AAAAAAAAAA";
    private static final String UPDATED_SEQUENCE_END = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_SEQUENCE = "AAAAAAAAAA";
    private static final String UPDATED_LAST_SEQUENCE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAS_FULL_FILLED = false;
    private static final Boolean UPDATED_HAS_FULL_FILLED = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_CREATED_PIN = "AAAAAAAAAA";
    private static final String UPDATED_LAST_CREATED_PIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-pin-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeePinConfigurationRepository employeePinConfigurationRepository;

    @Mock
    private EmployeePinConfigurationRepository employeePinConfigurationRepositoryMock;

    @Autowired
    private EmployeePinConfigurationMapper employeePinConfigurationMapper;

    @Mock
    private EmployeePinConfigurationService employeePinConfigurationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeePinConfigurationMockMvc;

    private EmployeePinConfiguration employeePinConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeePinConfiguration createEntity(EntityManager em) {
        EmployeePinConfiguration employeePinConfiguration = new EmployeePinConfiguration()
            .employeeCategory(DEFAULT_EMPLOYEE_CATEGORY)
            .sequenceStart(DEFAULT_SEQUENCE_START)
            .sequenceEnd(DEFAULT_SEQUENCE_END)
            .lastSequence(DEFAULT_LAST_SEQUENCE)
            .hasFullFilled(DEFAULT_HAS_FULL_FILLED)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .lastCreatedPin(DEFAULT_LAST_CREATED_PIN);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employeePinConfiguration.setCreatedBy(user);
        return employeePinConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeePinConfiguration createUpdatedEntity(EntityManager em) {
        EmployeePinConfiguration employeePinConfiguration = new EmployeePinConfiguration()
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .sequenceStart(UPDATED_SEQUENCE_START)
            .sequenceEnd(UPDATED_SEQUENCE_END)
            .lastSequence(UPDATED_LAST_SEQUENCE)
            .hasFullFilled(UPDATED_HAS_FULL_FILLED)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastCreatedPin(UPDATED_LAST_CREATED_PIN);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employeePinConfiguration.setCreatedBy(user);
        return employeePinConfiguration;
    }

    @BeforeEach
    public void initTest() {
        employeePinConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeePinConfiguration() throws Exception {
        int databaseSizeBeforeCreate = employeePinConfigurationRepository.findAll().size();
        // Create the EmployeePinConfiguration
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);
        restEmployeePinConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeePinConfiguration testEmployeePinConfiguration = employeePinConfigurationList.get(employeePinConfigurationList.size() - 1);
        assertThat(testEmployeePinConfiguration.getEmployeeCategory()).isEqualTo(DEFAULT_EMPLOYEE_CATEGORY);
        assertThat(testEmployeePinConfiguration.getSequenceStart()).isEqualTo(DEFAULT_SEQUENCE_START);
        assertThat(testEmployeePinConfiguration.getSequenceEnd()).isEqualTo(DEFAULT_SEQUENCE_END);
        assertThat(testEmployeePinConfiguration.getLastSequence()).isEqualTo(DEFAULT_LAST_SEQUENCE);
        assertThat(testEmployeePinConfiguration.getHasFullFilled()).isEqualTo(DEFAULT_HAS_FULL_FILLED);
        assertThat(testEmployeePinConfiguration.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeePinConfiguration.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeePinConfiguration.getLastCreatedPin()).isEqualTo(DEFAULT_LAST_CREATED_PIN);
    }

    @Test
    @Transactional
    void createEmployeePinConfigurationWithExistingId() throws Exception {
        // Create the EmployeePinConfiguration with an existing ID
        employeePinConfiguration.setId(1L);
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        int databaseSizeBeforeCreate = employeePinConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeePinConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmployeeCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeePinConfigurationRepository.findAll().size();
        // set the field null
        employeePinConfiguration.setEmployeeCategory(null);

        // Create the EmployeePinConfiguration, which fails.
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        restEmployeePinConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeePinConfigurationRepository.findAll().size();
        // set the field null
        employeePinConfiguration.setSequenceStart(null);

        // Create the EmployeePinConfiguration, which fails.
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        restEmployeePinConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceEndIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeePinConfigurationRepository.findAll().size();
        // set the field null
        employeePinConfiguration.setSequenceEnd(null);

        // Create the EmployeePinConfiguration, which fails.
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        restEmployeePinConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeePinConfigurationRepository.findAll().size();
        // set the field null
        employeePinConfiguration.setCreatedAt(null);

        // Create the EmployeePinConfiguration, which fails.
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        restEmployeePinConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeePinConfigurations() throws Exception {
        // Initialize the database
        employeePinConfigurationRepository.saveAndFlush(employeePinConfiguration);

        // Get all the employeePinConfigurationList
        restEmployeePinConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeePinConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeCategory").value(hasItem(DEFAULT_EMPLOYEE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].sequenceStart").value(hasItem(DEFAULT_SEQUENCE_START)))
            .andExpect(jsonPath("$.[*].sequenceEnd").value(hasItem(DEFAULT_SEQUENCE_END)))
            .andExpect(jsonPath("$.[*].lastSequence").value(hasItem(DEFAULT_LAST_SEQUENCE)))
            .andExpect(jsonPath("$.[*].hasFullFilled").value(hasItem(DEFAULT_HAS_FULL_FILLED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].lastCreatedPin").value(hasItem(DEFAULT_LAST_CREATED_PIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeePinConfigurationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeePinConfigurationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeePinConfigurationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeePinConfigurationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeePinConfigurationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeePinConfigurationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeePinConfigurationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeePinConfigurationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployeePinConfiguration() throws Exception {
        // Initialize the database
        employeePinConfigurationRepository.saveAndFlush(employeePinConfiguration);

        // Get the employeePinConfiguration
        restEmployeePinConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, employeePinConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeePinConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.employeeCategory").value(DEFAULT_EMPLOYEE_CATEGORY.toString()))
            .andExpect(jsonPath("$.sequenceStart").value(DEFAULT_SEQUENCE_START))
            .andExpect(jsonPath("$.sequenceEnd").value(DEFAULT_SEQUENCE_END))
            .andExpect(jsonPath("$.lastSequence").value(DEFAULT_LAST_SEQUENCE))
            .andExpect(jsonPath("$.hasFullFilled").value(DEFAULT_HAS_FULL_FILLED.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.lastCreatedPin").value(DEFAULT_LAST_CREATED_PIN));
    }

    @Test
    @Transactional
    void getNonExistingEmployeePinConfiguration() throws Exception {
        // Get the employeePinConfiguration
        restEmployeePinConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeePinConfiguration() throws Exception {
        // Initialize the database
        employeePinConfigurationRepository.saveAndFlush(employeePinConfiguration);

        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();

        // Update the employeePinConfiguration
        EmployeePinConfiguration updatedEmployeePinConfiguration = employeePinConfigurationRepository
            .findById(employeePinConfiguration.getId())
            .get();
        // Disconnect from session so that the updates on updatedEmployeePinConfiguration are not directly saved in db
        em.detach(updatedEmployeePinConfiguration);
        updatedEmployeePinConfiguration
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .sequenceStart(UPDATED_SEQUENCE_START)
            .sequenceEnd(UPDATED_SEQUENCE_END)
            .lastSequence(UPDATED_LAST_SEQUENCE)
            .hasFullFilled(UPDATED_HAS_FULL_FILLED)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastCreatedPin(UPDATED_LAST_CREATED_PIN);
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(updatedEmployeePinConfiguration);

        restEmployeePinConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeePinConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
        EmployeePinConfiguration testEmployeePinConfiguration = employeePinConfigurationList.get(employeePinConfigurationList.size() - 1);
        assertThat(testEmployeePinConfiguration.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployeePinConfiguration.getSequenceStart()).isEqualTo(UPDATED_SEQUENCE_START);
        assertThat(testEmployeePinConfiguration.getSequenceEnd()).isEqualTo(UPDATED_SEQUENCE_END);
        assertThat(testEmployeePinConfiguration.getLastSequence()).isEqualTo(UPDATED_LAST_SEQUENCE);
        assertThat(testEmployeePinConfiguration.getHasFullFilled()).isEqualTo(UPDATED_HAS_FULL_FILLED);
        assertThat(testEmployeePinConfiguration.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeePinConfiguration.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeePinConfiguration.getLastCreatedPin()).isEqualTo(UPDATED_LAST_CREATED_PIN);
    }

    @Test
    @Transactional
    void putNonExistingEmployeePinConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();
        employeePinConfiguration.setId(count.incrementAndGet());

        // Create the EmployeePinConfiguration
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeePinConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeePinConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeePinConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();
        employeePinConfiguration.setId(count.incrementAndGet());

        // Create the EmployeePinConfiguration
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeePinConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeePinConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();
        employeePinConfiguration.setId(count.incrementAndGet());

        // Create the EmployeePinConfiguration
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeePinConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeePinConfigurationWithPatch() throws Exception {
        // Initialize the database
        employeePinConfigurationRepository.saveAndFlush(employeePinConfiguration);

        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();

        // Update the employeePinConfiguration using partial update
        EmployeePinConfiguration partialUpdatedEmployeePinConfiguration = new EmployeePinConfiguration();
        partialUpdatedEmployeePinConfiguration.setId(employeePinConfiguration.getId());

        partialUpdatedEmployeePinConfiguration
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .sequenceStart(UPDATED_SEQUENCE_START)
            .sequenceEnd(UPDATED_SEQUENCE_END)
            .hasFullFilled(UPDATED_HAS_FULL_FILLED)
            .createdAt(UPDATED_CREATED_AT)
            .lastCreatedPin(UPDATED_LAST_CREATED_PIN);

        restEmployeePinConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeePinConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeePinConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
        EmployeePinConfiguration testEmployeePinConfiguration = employeePinConfigurationList.get(employeePinConfigurationList.size() - 1);
        assertThat(testEmployeePinConfiguration.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployeePinConfiguration.getSequenceStart()).isEqualTo(UPDATED_SEQUENCE_START);
        assertThat(testEmployeePinConfiguration.getSequenceEnd()).isEqualTo(UPDATED_SEQUENCE_END);
        assertThat(testEmployeePinConfiguration.getLastSequence()).isEqualTo(DEFAULT_LAST_SEQUENCE);
        assertThat(testEmployeePinConfiguration.getHasFullFilled()).isEqualTo(UPDATED_HAS_FULL_FILLED);
        assertThat(testEmployeePinConfiguration.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeePinConfiguration.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeePinConfiguration.getLastCreatedPin()).isEqualTo(UPDATED_LAST_CREATED_PIN);
    }

    @Test
    @Transactional
    void fullUpdateEmployeePinConfigurationWithPatch() throws Exception {
        // Initialize the database
        employeePinConfigurationRepository.saveAndFlush(employeePinConfiguration);

        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();

        // Update the employeePinConfiguration using partial update
        EmployeePinConfiguration partialUpdatedEmployeePinConfiguration = new EmployeePinConfiguration();
        partialUpdatedEmployeePinConfiguration.setId(employeePinConfiguration.getId());

        partialUpdatedEmployeePinConfiguration
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .sequenceStart(UPDATED_SEQUENCE_START)
            .sequenceEnd(UPDATED_SEQUENCE_END)
            .lastSequence(UPDATED_LAST_SEQUENCE)
            .hasFullFilled(UPDATED_HAS_FULL_FILLED)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .lastCreatedPin(UPDATED_LAST_CREATED_PIN);

        restEmployeePinConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeePinConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeePinConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
        EmployeePinConfiguration testEmployeePinConfiguration = employeePinConfigurationList.get(employeePinConfigurationList.size() - 1);
        assertThat(testEmployeePinConfiguration.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployeePinConfiguration.getSequenceStart()).isEqualTo(UPDATED_SEQUENCE_START);
        assertThat(testEmployeePinConfiguration.getSequenceEnd()).isEqualTo(UPDATED_SEQUENCE_END);
        assertThat(testEmployeePinConfiguration.getLastSequence()).isEqualTo(UPDATED_LAST_SEQUENCE);
        assertThat(testEmployeePinConfiguration.getHasFullFilled()).isEqualTo(UPDATED_HAS_FULL_FILLED);
        assertThat(testEmployeePinConfiguration.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeePinConfiguration.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeePinConfiguration.getLastCreatedPin()).isEqualTo(UPDATED_LAST_CREATED_PIN);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeePinConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();
        employeePinConfiguration.setId(count.incrementAndGet());

        // Create the EmployeePinConfiguration
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeePinConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeePinConfigurationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeePinConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();
        employeePinConfiguration.setId(count.incrementAndGet());

        // Create the EmployeePinConfiguration
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeePinConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeePinConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = employeePinConfigurationRepository.findAll().size();
        employeePinConfiguration.setId(count.incrementAndGet());

        // Create the EmployeePinConfiguration
        EmployeePinConfigurationDTO employeePinConfigurationDTO = employeePinConfigurationMapper.toDto(employeePinConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeePinConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeePinConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeePinConfiguration in the database
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeePinConfiguration() throws Exception {
        // Initialize the database
        employeePinConfigurationRepository.saveAndFlush(employeePinConfiguration);

        int databaseSizeBeforeDelete = employeePinConfigurationRepository.findAll().size();

        // Delete the employeePinConfiguration
        restEmployeePinConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeePinConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeePinConfiguration> employeePinConfigurationList = employeePinConfigurationRepository.findAll();
        assertThat(employeePinConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
