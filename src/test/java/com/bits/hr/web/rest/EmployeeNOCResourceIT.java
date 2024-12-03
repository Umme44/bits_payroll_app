package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeNOC;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.PurposeOfNOC;
import com.bits.hr.repository.EmployeeNOCRepository;
import com.bits.hr.service.EmployeeNOCService;
import com.bits.hr.service.dto.EmployeeNOCDTO;
import com.bits.hr.service.mapper.EmployeeNOCMapper;
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
 * Integration tests for the {@link EmployeeNOCResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeNOCResourceIT {

    private static final String DEFAULT_PASSPORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LEAVE_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEAVE_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LEAVE_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEAVE_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COUNTRY_TO_VISIT = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_TO_VISIT = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_GENERATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GENERATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final PurposeOfNOC DEFAULT_PURPOSE_OF_NOC = PurposeOfNOC.ACADEMIC;
    private static final PurposeOfNOC UPDATED_PURPOSE_OF_NOC = PurposeOfNOC.MEDICAL;

    private static final CertificateStatus DEFAULT_CERTIFICATE_STATUS = CertificateStatus.SENT_FOR_GENERATION;
    private static final CertificateStatus UPDATED_CERTIFICATE_STATUS = CertificateStatus.REJECTED;

    private static final Boolean DEFAULT_IS_REQUIRED_FOR_VISA = false;
    private static final Boolean UPDATED_IS_REQUIRED_FOR_VISA = true;

    private static final String ENTITY_API_URL = "/api/employee-nocs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeNOCRepository employeeNOCRepository;

    @Mock
    private EmployeeNOCRepository employeeNOCRepositoryMock;

    @Autowired
    private EmployeeNOCMapper employeeNOCMapper;

    @Mock
    private EmployeeNOCService employeeNOCServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeNOCMockMvc;

    private EmployeeNOC employeeNOC;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeNOC createEntity(EntityManager em) {
        EmployeeNOC employeeNOC = new EmployeeNOC()
            .passportNumber(DEFAULT_PASSPORT_NUMBER)
            .leaveStartDate(DEFAULT_LEAVE_START_DATE)
            .leaveEndDate(DEFAULT_LEAVE_END_DATE)
            .countryToVisit(DEFAULT_COUNTRY_TO_VISIT)
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .issueDate(DEFAULT_ISSUE_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .generatedAt(DEFAULT_GENERATED_AT)
            .reason(DEFAULT_REASON)
            .purposeOfNOC(DEFAULT_PURPOSE_OF_NOC)
            .certificateStatus(DEFAULT_CERTIFICATE_STATUS)
            .isRequiredForVisa(DEFAULT_IS_REQUIRED_FOR_VISA);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeNOC.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employeeNOC.setCreatedBy(user);
        return employeeNOC;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeNOC createUpdatedEntity(EntityManager em) {
        EmployeeNOC employeeNOC = new EmployeeNOC()
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .leaveStartDate(UPDATED_LEAVE_START_DATE)
            .leaveEndDate(UPDATED_LEAVE_END_DATE)
            .countryToVisit(UPDATED_COUNTRY_TO_VISIT)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .generatedAt(UPDATED_GENERATED_AT)
            .reason(UPDATED_REASON)
            .purposeOfNOC(UPDATED_PURPOSE_OF_NOC)
            .certificateStatus(UPDATED_CERTIFICATE_STATUS)
            .isRequiredForVisa(UPDATED_IS_REQUIRED_FOR_VISA);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        employeeNOC.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employeeNOC.setCreatedBy(user);
        return employeeNOC;
    }

    @BeforeEach
    public void initTest() {
        employeeNOC = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeNOC() throws Exception {
        int databaseSizeBeforeCreate = employeeNOCRepository.findAll().size();
        // Create the EmployeeNOC
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);
        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeNOC testEmployeeNOC = employeeNOCList.get(employeeNOCList.size() - 1);
        assertThat(testEmployeeNOC.getPassportNumber()).isEqualTo(DEFAULT_PASSPORT_NUMBER);
        assertThat(testEmployeeNOC.getLeaveStartDate()).isEqualTo(DEFAULT_LEAVE_START_DATE);
        assertThat(testEmployeeNOC.getLeaveEndDate()).isEqualTo(DEFAULT_LEAVE_END_DATE);
        assertThat(testEmployeeNOC.getCountryToVisit()).isEqualTo(DEFAULT_COUNTRY_TO_VISIT);
        assertThat(testEmployeeNOC.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testEmployeeNOC.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testEmployeeNOC.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployeeNOC.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEmployeeNOC.getGeneratedAt()).isEqualTo(DEFAULT_GENERATED_AT);
        assertThat(testEmployeeNOC.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testEmployeeNOC.getPurposeOfNOC()).isEqualTo(DEFAULT_PURPOSE_OF_NOC);
        assertThat(testEmployeeNOC.getCertificateStatus()).isEqualTo(DEFAULT_CERTIFICATE_STATUS);
        assertThat(testEmployeeNOC.getIsRequiredForVisa()).isEqualTo(DEFAULT_IS_REQUIRED_FOR_VISA);
    }

    @Test
    @Transactional
    void createEmployeeNOCWithExistingId() throws Exception {
        // Create the EmployeeNOC with an existing ID
        employeeNOC.setId(1L);
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        int databaseSizeBeforeCreate = employeeNOCRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPassportNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeNOCRepository.findAll().size();
        // set the field null
        employeeNOC.setPassportNumber(null);

        // Create the EmployeeNOC, which fails.
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeaveStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeNOCRepository.findAll().size();
        // set the field null
        employeeNOC.setLeaveStartDate(null);

        // Create the EmployeeNOC, which fails.
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeaveEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeNOCRepository.findAll().size();
        // set the field null
        employeeNOC.setLeaveEndDate(null);

        // Create the EmployeeNOC, which fails.
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryToVisitIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeNOCRepository.findAll().size();
        // set the field null
        employeeNOC.setCountryToVisit(null);

        // Create the EmployeeNOC, which fails.
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeNOCRepository.findAll().size();
        // set the field null
        employeeNOC.setCreatedAt(null);

        // Create the EmployeeNOC, which fails.
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPurposeOfNOCIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeNOCRepository.findAll().size();
        // set the field null
        employeeNOC.setPurposeOfNOC(null);

        // Create the EmployeeNOC, which fails.
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCertificateStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeNOCRepository.findAll().size();
        // set the field null
        employeeNOC.setCertificateStatus(null);

        // Create the EmployeeNOC, which fails.
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        restEmployeeNOCMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeNOCS() throws Exception {
        // Initialize the database
        employeeNOCRepository.saveAndFlush(employeeNOC);

        // Get all the employeeNOCList
        restEmployeeNOCMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeNOC.getId().intValue())))
            .andExpect(jsonPath("$.[*].passportNumber").value(hasItem(DEFAULT_PASSPORT_NUMBER)))
            .andExpect(jsonPath("$.[*].leaveStartDate").value(hasItem(DEFAULT_LEAVE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaveEndDate").value(hasItem(DEFAULT_LEAVE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].countryToVisit").value(hasItem(DEFAULT_COUNTRY_TO_VISIT)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(DEFAULT_GENERATED_AT.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].purposeOfNOC").value(hasItem(DEFAULT_PURPOSE_OF_NOC.toString())))
            .andExpect(jsonPath("$.[*].certificateStatus").value(hasItem(DEFAULT_CERTIFICATE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isRequiredForVisa").value(hasItem(DEFAULT_IS_REQUIRED_FOR_VISA.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeeNOCSWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeeNOCServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeNOCMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeNOCServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeeNOCSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeeNOCServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeNOCMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeeNOCRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmployeeNOC() throws Exception {
        // Initialize the database
        employeeNOCRepository.saveAndFlush(employeeNOC);

        // Get the employeeNOC
        restEmployeeNOCMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeNOC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeNOC.getId().intValue()))
            .andExpect(jsonPath("$.passportNumber").value(DEFAULT_PASSPORT_NUMBER))
            .andExpect(jsonPath("$.leaveStartDate").value(DEFAULT_LEAVE_START_DATE.toString()))
            .andExpect(jsonPath("$.leaveEndDate").value(DEFAULT_LEAVE_END_DATE.toString()))
            .andExpect(jsonPath("$.countryToVisit").value(DEFAULT_COUNTRY_TO_VISIT))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.generatedAt").value(DEFAULT_GENERATED_AT.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.purposeOfNOC").value(DEFAULT_PURPOSE_OF_NOC.toString()))
            .andExpect(jsonPath("$.certificateStatus").value(DEFAULT_CERTIFICATE_STATUS.toString()))
            .andExpect(jsonPath("$.isRequiredForVisa").value(DEFAULT_IS_REQUIRED_FOR_VISA.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeNOC() throws Exception {
        // Get the employeeNOC
        restEmployeeNOCMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeNOC() throws Exception {
        // Initialize the database
        employeeNOCRepository.saveAndFlush(employeeNOC);

        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();

        // Update the employeeNOC
        EmployeeNOC updatedEmployeeNOC = employeeNOCRepository.findById(employeeNOC.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeNOC are not directly saved in db
        em.detach(updatedEmployeeNOC);
        updatedEmployeeNOC
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .leaveStartDate(UPDATED_LEAVE_START_DATE)
            .leaveEndDate(UPDATED_LEAVE_END_DATE)
            .countryToVisit(UPDATED_COUNTRY_TO_VISIT)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .generatedAt(UPDATED_GENERATED_AT)
            .reason(UPDATED_REASON)
            .purposeOfNOC(UPDATED_PURPOSE_OF_NOC)
            .certificateStatus(UPDATED_CERTIFICATE_STATUS)
            .isRequiredForVisa(UPDATED_IS_REQUIRED_FOR_VISA);
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(updatedEmployeeNOC);

        restEmployeeNOCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeNOCDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
        EmployeeNOC testEmployeeNOC = employeeNOCList.get(employeeNOCList.size() - 1);
        assertThat(testEmployeeNOC.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testEmployeeNOC.getLeaveStartDate()).isEqualTo(UPDATED_LEAVE_START_DATE);
        assertThat(testEmployeeNOC.getLeaveEndDate()).isEqualTo(UPDATED_LEAVE_END_DATE);
        assertThat(testEmployeeNOC.getCountryToVisit()).isEqualTo(UPDATED_COUNTRY_TO_VISIT);
        assertThat(testEmployeeNOC.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testEmployeeNOC.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testEmployeeNOC.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeNOC.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeNOC.getGeneratedAt()).isEqualTo(UPDATED_GENERATED_AT);
        assertThat(testEmployeeNOC.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testEmployeeNOC.getPurposeOfNOC()).isEqualTo(UPDATED_PURPOSE_OF_NOC);
        assertThat(testEmployeeNOC.getCertificateStatus()).isEqualTo(UPDATED_CERTIFICATE_STATUS);
        assertThat(testEmployeeNOC.getIsRequiredForVisa()).isEqualTo(UPDATED_IS_REQUIRED_FOR_VISA);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeNOC() throws Exception {
        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();
        employeeNOC.setId(count.incrementAndGet());

        // Create the EmployeeNOC
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeNOCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeNOCDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeNOC() throws Exception {
        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();
        employeeNOC.setId(count.incrementAndGet());

        // Create the EmployeeNOC
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeNOCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeNOC() throws Exception {
        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();
        employeeNOC.setId(count.incrementAndGet());

        // Create the EmployeeNOC
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeNOCMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeNOCWithPatch() throws Exception {
        // Initialize the database
        employeeNOCRepository.saveAndFlush(employeeNOC);

        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();

        // Update the employeeNOC using partial update
        EmployeeNOC partialUpdatedEmployeeNOC = new EmployeeNOC();
        partialUpdatedEmployeeNOC.setId(employeeNOC.getId());

        partialUpdatedEmployeeNOC
            .leaveEndDate(UPDATED_LEAVE_END_DATE)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .reason(UPDATED_REASON)
            .purposeOfNOC(UPDATED_PURPOSE_OF_NOC);

        restEmployeeNOCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeNOC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeNOC))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
        EmployeeNOC testEmployeeNOC = employeeNOCList.get(employeeNOCList.size() - 1);
        assertThat(testEmployeeNOC.getPassportNumber()).isEqualTo(DEFAULT_PASSPORT_NUMBER);
        assertThat(testEmployeeNOC.getLeaveStartDate()).isEqualTo(DEFAULT_LEAVE_START_DATE);
        assertThat(testEmployeeNOC.getLeaveEndDate()).isEqualTo(UPDATED_LEAVE_END_DATE);
        assertThat(testEmployeeNOC.getCountryToVisit()).isEqualTo(DEFAULT_COUNTRY_TO_VISIT);
        assertThat(testEmployeeNOC.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testEmployeeNOC.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testEmployeeNOC.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeNOC.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeNOC.getGeneratedAt()).isEqualTo(DEFAULT_GENERATED_AT);
        assertThat(testEmployeeNOC.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testEmployeeNOC.getPurposeOfNOC()).isEqualTo(UPDATED_PURPOSE_OF_NOC);
        assertThat(testEmployeeNOC.getCertificateStatus()).isEqualTo(DEFAULT_CERTIFICATE_STATUS);
        assertThat(testEmployeeNOC.getIsRequiredForVisa()).isEqualTo(DEFAULT_IS_REQUIRED_FOR_VISA);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeNOCWithPatch() throws Exception {
        // Initialize the database
        employeeNOCRepository.saveAndFlush(employeeNOC);

        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();

        // Update the employeeNOC using partial update
        EmployeeNOC partialUpdatedEmployeeNOC = new EmployeeNOC();
        partialUpdatedEmployeeNOC.setId(employeeNOC.getId());

        partialUpdatedEmployeeNOC
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .leaveStartDate(UPDATED_LEAVE_START_DATE)
            .leaveEndDate(UPDATED_LEAVE_END_DATE)
            .countryToVisit(UPDATED_COUNTRY_TO_VISIT)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .generatedAt(UPDATED_GENERATED_AT)
            .reason(UPDATED_REASON)
            .purposeOfNOC(UPDATED_PURPOSE_OF_NOC)
            .certificateStatus(UPDATED_CERTIFICATE_STATUS)
            .isRequiredForVisa(UPDATED_IS_REQUIRED_FOR_VISA);

        restEmployeeNOCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeNOC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeNOC))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
        EmployeeNOC testEmployeeNOC = employeeNOCList.get(employeeNOCList.size() - 1);
        assertThat(testEmployeeNOC.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testEmployeeNOC.getLeaveStartDate()).isEqualTo(UPDATED_LEAVE_START_DATE);
        assertThat(testEmployeeNOC.getLeaveEndDate()).isEqualTo(UPDATED_LEAVE_END_DATE);
        assertThat(testEmployeeNOC.getCountryToVisit()).isEqualTo(UPDATED_COUNTRY_TO_VISIT);
        assertThat(testEmployeeNOC.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testEmployeeNOC.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testEmployeeNOC.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployeeNOC.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEmployeeNOC.getGeneratedAt()).isEqualTo(UPDATED_GENERATED_AT);
        assertThat(testEmployeeNOC.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testEmployeeNOC.getPurposeOfNOC()).isEqualTo(UPDATED_PURPOSE_OF_NOC);
        assertThat(testEmployeeNOC.getCertificateStatus()).isEqualTo(UPDATED_CERTIFICATE_STATUS);
        assertThat(testEmployeeNOC.getIsRequiredForVisa()).isEqualTo(UPDATED_IS_REQUIRED_FOR_VISA);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeNOC() throws Exception {
        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();
        employeeNOC.setId(count.incrementAndGet());

        // Create the EmployeeNOC
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeNOCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeNOCDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeNOC() throws Exception {
        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();
        employeeNOC.setId(count.incrementAndGet());

        // Create the EmployeeNOC
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeNOCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeNOC() throws Exception {
        int databaseSizeBeforeUpdate = employeeNOCRepository.findAll().size();
        employeeNOC.setId(count.incrementAndGet());

        // Create the EmployeeNOC
        EmployeeNOCDTO employeeNOCDTO = employeeNOCMapper.toDto(employeeNOC);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeNOCMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeeNOCDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeNOC in the database
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeNOC() throws Exception {
        // Initialize the database
        employeeNOCRepository.saveAndFlush(employeeNOC);

        int databaseSizeBeforeDelete = employeeNOCRepository.findAll().size();

        // Delete the employeeNOC
        restEmployeeNOCMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeNOC.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeNOC> employeeNOCList = employeeNOCRepository.findAll();
        assertThat(employeeNOCList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
