package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Department;
import com.bits.hr.domain.Designation;
import com.bits.hr.domain.EmployeePin;
import com.bits.hr.domain.Unit;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import com.bits.hr.repository.EmployeePinRepository;
import com.bits.hr.service.EmployeePinService;
import com.bits.hr.service.dto.EmployeePinDTO;
import com.bits.hr.service.mapper.EmployeePinMapper;
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
 * Integration tests for the {@link EmployeePinResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeePinResourceIT {

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final EmployeeCategory DEFAULT_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
    private static final EmployeeCategory UPDATED_EMPLOYEE_CATEGORY = EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;

    private static final EmployeePinStatus DEFAULT_EMPLOYEE_PIN_STATUS = EmployeePinStatus.CREATED;
    private static final EmployeePinStatus UPDATED_EMPLOYEE_PIN_STATUS = EmployeePinStatus.JOINED;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/employee-pins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeePinRepository employeePinRepository;

    @Mock
    private EmployeePinRepository employeePinRepositoryMock;

    @Autowired
    private EmployeePinMapper employeePinMapper;

    @Mock
    private EmployeePinService employeePinServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeePinMockMvc;

    private EmployeePin employeePin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeePin createEntity(EntityManager em) {
        EmployeePin employeePin = new EmployeePin()
            .pin(DEFAULT_PIN)
            .fullName(DEFAULT_FULL_NAME)
            .employeeCategory(DEFAULT_EMPLOYEE_CATEGORY)
            .employeePinStatus(DEFAULT_EMPLOYEE_PIN_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        employeePin.setDepartment(department);
        // Add required entity
        Designation designation;
        if (TestUtil.findAll(em, Designation.class).isEmpty()) {
            designation = DesignationResourceIT.createEntity(em);
            em.persist(designation);
            em.flush();
        } else {
            designation = TestUtil.findAll(em, Designation.class).get(0);
        }
        employeePin.setDesignation(designation);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        employeePin.setUnit(unit);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employeePin.setCreatedBy(user);
        return employeePin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeePin createUpdatedEntity(EntityManager em) {
        EmployeePin employeePin = new EmployeePin()
            .pin(UPDATED_PIN)
            .fullName(UPDATED_FULL_NAME)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .employeePinStatus(UPDATED_EMPLOYEE_PIN_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        employeePin.setDepartment(department);
        // Add required entity
        Designation designation;
        if (TestUtil.findAll(em, Designation.class).isEmpty()) {
            designation = DesignationResourceIT.createUpdatedEntity(em);
            em.persist(designation);
            em.flush();
        } else {
            designation = TestUtil.findAll(em, Designation.class).get(0);
        }
        employeePin.setDesignation(designation);
        // Add required entity
        Unit unit;
        if (TestUtil.findAll(em, Unit.class).isEmpty()) {
            unit = UnitResourceIT.createUpdatedEntity(em);
            em.persist(unit);
            em.flush();
        } else {
            unit = TestUtil.findAll(em, Unit.class).get(0);
        }
        employeePin.setUnit(unit);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employeePin.setCreatedBy(user);
        return employeePin;
    }

    @BeforeEach
    public void initTest() {
        employeePin = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeePin() throws Exception {
        int databaseSizeBeforeCreate = employeePinRepository.findAll().size();
        // Create the EmployeePin
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);
        restEmployeePinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeePin testEmployeePin = employeePinList.get(employeePinList.size() - 1);
        assertThat(testEmployeePin.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testEmployeePin.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testEmployeePin.getEmployeeCategory()).isEqualTo(DEFAULT_EMPLOYEE_CATEGORY);
        assertThat(testEmployeePin.getEmployeePinStatus()).isEqualTo(DEFAULT_EMPLOYEE_PIN_STATUS);
        assertThat(testEmployeePin.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeePin.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createEmployeePinWithExistingId() throws Exception {
        // Create the EmployeePin with an existing ID
        employeePin.setId(1L);
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        int databaseSizeBeforeCreate = employeePinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeePinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeePinRepository.findAll().size();
        // set the field null
        employeePin.setFullName(null);

        // Create the EmployeePin, which fails.
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        restEmployeePinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmployeeCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeePinRepository.findAll().size();
        // set the field null
        employeePin.setEmployeeCategory(null);

        // Create the EmployeePin, which fails.
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        restEmployeePinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmployeePinStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeePinRepository.findAll().size();
        // set the field null
        employeePin.setEmployeePinStatus(null);

        // Create the EmployeePin, which fails.
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        restEmployeePinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeePinRepository.findAll().size();
        // set the field null
        employeePin.setCreatedAt(null);

        // Create the EmployeePin, which fails.
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        restEmployeePinMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeePins() throws Exception {
        // Initialize the database
        employeePinRepository.saveAndFlush(employeePin);

        // Get all the employeePinList
        restEmployeePinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeePin.getId().intValue())))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].employeeCategory").value(hasItem(DEFAULT_EMPLOYEE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].employeePinStatus").value(hasItem(DEFAULT_EMPLOYEE_PIN_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeePinsWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeePinServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeePinMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeePinServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeePinsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeePinServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeePinMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeePinRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployeePin() throws Exception {
        // Initialize the database
        employeePinRepository.saveAndFlush(employeePin);

        // Get the employeePin
        restEmployeePinMockMvc
            .perform(get(ENTITY_API_URL_ID, employeePin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeePin.getId().intValue()))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.employeeCategory").value(DEFAULT_EMPLOYEE_CATEGORY.toString()))
            .andExpect(jsonPath("$.employeePinStatus").value(DEFAULT_EMPLOYEE_PIN_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployeePin() throws Exception {
        // Get the employeePin
        restEmployeePinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeePin() throws Exception {
        // Initialize the database
        employeePinRepository.saveAndFlush(employeePin);

        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();

        // Update the employeePin
        EmployeePin updatedEmployeePin = employeePinRepository.findById(employeePin.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeePin are not directly saved in db
        em.detach(updatedEmployeePin);
        updatedEmployeePin
            .pin(UPDATED_PIN)
            .fullName(UPDATED_FULL_NAME)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .employeePinStatus(UPDATED_EMPLOYEE_PIN_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(updatedEmployeePin);

        restEmployeePinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeePinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
        EmployeePin testEmployeePin = employeePinList.get(employeePinList.size() - 1);
        assertThat(testEmployeePin.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployeePin.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testEmployeePin.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployeePin.getEmployeePinStatus()).isEqualTo(UPDATED_EMPLOYEE_PIN_STATUS);
        assertThat(testEmployeePin.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeePin.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingEmployeePin() throws Exception {
        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();
        employeePin.setId(count.incrementAndGet());

        // Create the EmployeePin
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeePinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeePinDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeePin() throws Exception {
        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();
        employeePin.setId(count.incrementAndGet());

        // Create the EmployeePin
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeePinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeePin() throws Exception {
        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();
        employeePin.setId(count.incrementAndGet());

        // Create the EmployeePin
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeePinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeePinDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeePinWithPatch() throws Exception {
        // Initialize the database
        employeePinRepository.saveAndFlush(employeePin);

        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();

        // Update the employeePin using partial update
        EmployeePin partialUpdatedEmployeePin = new EmployeePin();
        partialUpdatedEmployeePin.setId(employeePin.getId());

        partialUpdatedEmployeePin
            .pin(UPDATED_PIN)
            .fullName(UPDATED_FULL_NAME)
            .employeePinStatus(UPDATED_EMPLOYEE_PIN_STATUS)
            .updatedAt(UPDATED_UPDATED_AT);

        restEmployeePinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeePin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeePin))
            )
            .andExpect(status().isOk());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
        EmployeePin testEmployeePin = employeePinList.get(employeePinList.size() - 1);
        assertThat(testEmployeePin.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployeePin.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testEmployeePin.getEmployeeCategory()).isEqualTo(DEFAULT_EMPLOYEE_CATEGORY);
        assertThat(testEmployeePin.getEmployeePinStatus()).isEqualTo(UPDATED_EMPLOYEE_PIN_STATUS);
        assertThat(testEmployeePin.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeePin.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEmployeePinWithPatch() throws Exception {
        // Initialize the database
        employeePinRepository.saveAndFlush(employeePin);

        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();

        // Update the employeePin using partial update
        EmployeePin partialUpdatedEmployeePin = new EmployeePin();
        partialUpdatedEmployeePin.setId(employeePin.getId());

        partialUpdatedEmployeePin
            .pin(UPDATED_PIN)
            .fullName(UPDATED_FULL_NAME)
            .employeeCategory(UPDATED_EMPLOYEE_CATEGORY)
            .employeePinStatus(UPDATED_EMPLOYEE_PIN_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restEmployeePinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeePin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeePin))
            )
            .andExpect(status().isOk());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
        EmployeePin testEmployeePin = employeePinList.get(employeePinList.size() - 1);
        assertThat(testEmployeePin.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testEmployeePin.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testEmployeePin.getEmployeeCategory()).isEqualTo(UPDATED_EMPLOYEE_CATEGORY);
        assertThat(testEmployeePin.getEmployeePinStatus()).isEqualTo(UPDATED_EMPLOYEE_PIN_STATUS);
        assertThat(testEmployeePin.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeePin.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeePin() throws Exception {
        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();
        employeePin.setId(count.incrementAndGet());

        // Create the EmployeePin
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeePinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeePinDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeePin() throws Exception {
        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();
        employeePin.setId(count.incrementAndGet());

        // Create the EmployeePin
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeePinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeePin() throws Exception {
        int databaseSizeBeforeUpdate = employeePinRepository.findAll().size();
        employeePin.setId(count.incrementAndGet());

        // Create the EmployeePin
        EmployeePinDTO employeePinDTO = employeePinMapper.toDto(employeePin);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeePinMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeePinDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeePin in the database
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeePin() throws Exception {
        // Initialize the database
        employeePinRepository.saveAndFlush(employeePin);

        int databaseSizeBeforeDelete = employeePinRepository.findAll().size();

        // Delete the employeePin
        restEmployeePinMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeePin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeePin> employeePinList = employeePinRepository.findAll();
        assertThat(employeePinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
