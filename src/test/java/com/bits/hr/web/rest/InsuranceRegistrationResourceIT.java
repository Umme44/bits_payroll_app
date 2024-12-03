package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.domain.enumeration.InsuranceStatus;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.InsuranceRegistrationService;
import com.bits.hr.service.dto.InsuranceRegistrationDTO;
import com.bits.hr.service.mapper.InsuranceRegistrationMapper;
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
 * Integration tests for the {@link InsuranceRegistrationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InsuranceRegistrationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final InsuranceRelation DEFAULT_INSURANCE_RELATION = InsuranceRelation.SELF;
    private static final InsuranceRelation UPDATED_INSURANCE_RELATION = InsuranceRelation.SPOUSE;

    private static final InsuranceStatus DEFAULT_INSURANCE_STATUS = InsuranceStatus.PENDING;
    private static final InsuranceStatus UPDATED_INSURANCE_STATUS = InsuranceStatus.APPROVED;

    private static final String DEFAULT_UNAPPROVAL_REASON = "AAAAAAAAAA";
    private static final String UPDATED_UNAPPROVAL_REASON = "BBBBBBBBBB";

    private static final Double DEFAULT_AVAILABLE_BALANCE = 1D;
    private static final Double UPDATED_AVAILABLE_BALANCE = 2D;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_APPROVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPROVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INSURANCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_INSURANCE_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/insurance-registrations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Mock
    private InsuranceRegistrationRepository insuranceRegistrationRepositoryMock;

    @Autowired
    private InsuranceRegistrationMapper insuranceRegistrationMapper;

    @Mock
    private InsuranceRegistrationService insuranceRegistrationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsuranceRegistrationMockMvc;

    private InsuranceRegistration insuranceRegistration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceRegistration createEntity(EntityManager em) {
        InsuranceRegistration insuranceRegistration = new InsuranceRegistration()
            .name(DEFAULT_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .photo(DEFAULT_PHOTO)
            .insuranceRelation(DEFAULT_INSURANCE_RELATION)
            .insuranceStatus(DEFAULT_INSURANCE_STATUS)
            .unapprovalReason(DEFAULT_UNAPPROVAL_REASON)
            .availableBalance(DEFAULT_AVAILABLE_BALANCE)
            .updatedAt(DEFAULT_UPDATED_AT)
            .approvedAt(DEFAULT_APPROVED_AT)
            .insuranceId(DEFAULT_INSURANCE_ID)
            .createdAt(DEFAULT_CREATED_AT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        insuranceRegistration.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        insuranceRegistration.setCreatedBy(user);
        return insuranceRegistration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsuranceRegistration createUpdatedEntity(EntityManager em) {
        InsuranceRegistration insuranceRegistration = new InsuranceRegistration()
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .photo(UPDATED_PHOTO)
            .insuranceRelation(UPDATED_INSURANCE_RELATION)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .unapprovalReason(UPDATED_UNAPPROVAL_REASON)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .updatedAt(UPDATED_UPDATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .insuranceId(UPDATED_INSURANCE_ID)
            .createdAt(UPDATED_CREATED_AT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        insuranceRegistration.setEmployee(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        insuranceRegistration.setCreatedBy(user);
        return insuranceRegistration;
    }

    @BeforeEach
    public void initTest() {
        insuranceRegistration = createEntity(em);
    }

    @Test
    @Transactional
    void createInsuranceRegistration() throws Exception {
        int databaseSizeBeforeCreate = insuranceRegistrationRepository.findAll().size();
        // Create the InsuranceRegistration
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);
        restInsuranceRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeCreate + 1);
        InsuranceRegistration testInsuranceRegistration = insuranceRegistrationList.get(insuranceRegistrationList.size() - 1);
        assertThat(testInsuranceRegistration.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsuranceRegistration.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testInsuranceRegistration.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testInsuranceRegistration.getInsuranceRelation()).isEqualTo(DEFAULT_INSURANCE_RELATION);
        assertThat(testInsuranceRegistration.getInsuranceStatus()).isEqualTo(DEFAULT_INSURANCE_STATUS);
        assertThat(testInsuranceRegistration.getUnapprovalReason()).isEqualTo(DEFAULT_UNAPPROVAL_REASON);
        assertThat(testInsuranceRegistration.getAvailableBalance()).isEqualTo(DEFAULT_AVAILABLE_BALANCE);
        assertThat(testInsuranceRegistration.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testInsuranceRegistration.getApprovedAt()).isEqualTo(DEFAULT_APPROVED_AT);
        assertThat(testInsuranceRegistration.getInsuranceId()).isEqualTo(DEFAULT_INSURANCE_ID);
        assertThat(testInsuranceRegistration.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createInsuranceRegistrationWithExistingId() throws Exception {
        // Create the InsuranceRegistration with an existing ID
        insuranceRegistration.setId(1L);
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        int databaseSizeBeforeCreate = insuranceRegistrationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsuranceRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRegistrationRepository.findAll().size();
        // set the field null
        insuranceRegistration.setDateOfBirth(null);

        // Create the InsuranceRegistration, which fails.
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        restInsuranceRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInsuranceRelationIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRegistrationRepository.findAll().size();
        // set the field null
        insuranceRegistration.setInsuranceRelation(null);

        // Create the InsuranceRegistration, which fails.
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        restInsuranceRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInsuranceStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRegistrationRepository.findAll().size();
        // set the field null
        insuranceRegistration.setInsuranceStatus(null);

        // Create the InsuranceRegistration, which fails.
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        restInsuranceRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRegistrationRepository.findAll().size();
        // set the field null
        insuranceRegistration.setAvailableBalance(null);

        // Create the InsuranceRegistration, which fails.
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        restInsuranceRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = insuranceRegistrationRepository.findAll().size();
        // set the field null
        insuranceRegistration.setCreatedAt(null);

        // Create the InsuranceRegistration, which fails.
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        restInsuranceRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInsuranceRegistrations() throws Exception {
        // Initialize the database
        insuranceRegistrationRepository.saveAndFlush(insuranceRegistration);

        // Get all the insuranceRegistrationList
        restInsuranceRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insuranceRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO.toString())))
            .andExpect(jsonPath("$.[*].insuranceRelation").value(hasItem(DEFAULT_INSURANCE_RELATION.toString())))
            .andExpect(jsonPath("$.[*].insuranceStatus").value(hasItem(DEFAULT_INSURANCE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].unapprovalReason").value(hasItem(DEFAULT_UNAPPROVAL_REASON)))
            .andExpect(jsonPath("$.[*].availableBalance").value(hasItem(DEFAULT_AVAILABLE_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(DEFAULT_APPROVED_AT.toString())))
            .andExpect(jsonPath("$.[*].insuranceId").value(hasItem(DEFAULT_INSURANCE_ID)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsuranceRegistrationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(insuranceRegistrationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsuranceRegistrationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(insuranceRegistrationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsuranceRegistrationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(insuranceRegistrationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsuranceRegistrationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(insuranceRegistrationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInsuranceRegistration() throws Exception {
        // Initialize the database
        insuranceRegistrationRepository.saveAndFlush(insuranceRegistration);

        // Get the insuranceRegistration
        restInsuranceRegistrationMockMvc
            .perform(get(ENTITY_API_URL_ID, insuranceRegistration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insuranceRegistration.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO.toString()))
            .andExpect(jsonPath("$.insuranceRelation").value(DEFAULT_INSURANCE_RELATION.toString()))
            .andExpect(jsonPath("$.insuranceStatus").value(DEFAULT_INSURANCE_STATUS.toString()))
            .andExpect(jsonPath("$.unapprovalReason").value(DEFAULT_UNAPPROVAL_REASON))
            .andExpect(jsonPath("$.availableBalance").value(DEFAULT_AVAILABLE_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.approvedAt").value(DEFAULT_APPROVED_AT.toString()))
            .andExpect(jsonPath("$.insuranceId").value(DEFAULT_INSURANCE_ID))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInsuranceRegistration() throws Exception {
        // Get the insuranceRegistration
        restInsuranceRegistrationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsuranceRegistration() throws Exception {
        // Initialize the database
        insuranceRegistrationRepository.saveAndFlush(insuranceRegistration);

        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();

        // Update the insuranceRegistration
        InsuranceRegistration updatedInsuranceRegistration = insuranceRegistrationRepository.findById(insuranceRegistration.getId()).get();
        // Disconnect from session so that the updates on updatedInsuranceRegistration are not directly saved in db
        em.detach(updatedInsuranceRegistration);
        updatedInsuranceRegistration
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .photo(UPDATED_PHOTO)
            .insuranceRelation(UPDATED_INSURANCE_RELATION)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .unapprovalReason(UPDATED_UNAPPROVAL_REASON)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .updatedAt(UPDATED_UPDATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .insuranceId(UPDATED_INSURANCE_ID)
            .createdAt(UPDATED_CREATED_AT);
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(updatedInsuranceRegistration);

        restInsuranceRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceRegistrationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
        InsuranceRegistration testInsuranceRegistration = insuranceRegistrationList.get(insuranceRegistrationList.size() - 1);
        assertThat(testInsuranceRegistration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsuranceRegistration.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testInsuranceRegistration.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testInsuranceRegistration.getInsuranceRelation()).isEqualTo(UPDATED_INSURANCE_RELATION);
        assertThat(testInsuranceRegistration.getInsuranceStatus()).isEqualTo(UPDATED_INSURANCE_STATUS);
        assertThat(testInsuranceRegistration.getUnapprovalReason()).isEqualTo(UPDATED_UNAPPROVAL_REASON);
        assertThat(testInsuranceRegistration.getAvailableBalance()).isEqualTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testInsuranceRegistration.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testInsuranceRegistration.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testInsuranceRegistration.getInsuranceId()).isEqualTo(UPDATED_INSURANCE_ID);
        assertThat(testInsuranceRegistration.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingInsuranceRegistration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();
        insuranceRegistration.setId(count.incrementAndGet());

        // Create the InsuranceRegistration
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insuranceRegistrationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsuranceRegistration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();
        insuranceRegistration.setId(count.incrementAndGet());

        // Create the InsuranceRegistration
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsuranceRegistration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();
        insuranceRegistration.setId(count.incrementAndGet());

        // Create the InsuranceRegistration
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInsuranceRegistrationWithPatch() throws Exception {
        // Initialize the database
        insuranceRegistrationRepository.saveAndFlush(insuranceRegistration);

        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();

        // Update the insuranceRegistration using partial update
        InsuranceRegistration partialUpdatedInsuranceRegistration = new InsuranceRegistration();
        partialUpdatedInsuranceRegistration.setId(insuranceRegistration.getId());

        partialUpdatedInsuranceRegistration
            .insuranceRelation(UPDATED_INSURANCE_RELATION)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .createdAt(UPDATED_CREATED_AT);

        restInsuranceRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsuranceRegistration))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
        InsuranceRegistration testInsuranceRegistration = insuranceRegistrationList.get(insuranceRegistrationList.size() - 1);
        assertThat(testInsuranceRegistration.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInsuranceRegistration.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testInsuranceRegistration.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testInsuranceRegistration.getInsuranceRelation()).isEqualTo(UPDATED_INSURANCE_RELATION);
        assertThat(testInsuranceRegistration.getInsuranceStatus()).isEqualTo(DEFAULT_INSURANCE_STATUS);
        assertThat(testInsuranceRegistration.getUnapprovalReason()).isEqualTo(DEFAULT_UNAPPROVAL_REASON);
        assertThat(testInsuranceRegistration.getAvailableBalance()).isEqualTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testInsuranceRegistration.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testInsuranceRegistration.getApprovedAt()).isEqualTo(DEFAULT_APPROVED_AT);
        assertThat(testInsuranceRegistration.getInsuranceId()).isEqualTo(DEFAULT_INSURANCE_ID);
        assertThat(testInsuranceRegistration.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateInsuranceRegistrationWithPatch() throws Exception {
        // Initialize the database
        insuranceRegistrationRepository.saveAndFlush(insuranceRegistration);

        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();

        // Update the insuranceRegistration using partial update
        InsuranceRegistration partialUpdatedInsuranceRegistration = new InsuranceRegistration();
        partialUpdatedInsuranceRegistration.setId(insuranceRegistration.getId());

        partialUpdatedInsuranceRegistration
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .photo(UPDATED_PHOTO)
            .insuranceRelation(UPDATED_INSURANCE_RELATION)
            .insuranceStatus(UPDATED_INSURANCE_STATUS)
            .unapprovalReason(UPDATED_UNAPPROVAL_REASON)
            .availableBalance(UPDATED_AVAILABLE_BALANCE)
            .updatedAt(UPDATED_UPDATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .insuranceId(UPDATED_INSURANCE_ID)
            .createdAt(UPDATED_CREATED_AT);

        restInsuranceRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsuranceRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInsuranceRegistration))
            )
            .andExpect(status().isOk());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
        InsuranceRegistration testInsuranceRegistration = insuranceRegistrationList.get(insuranceRegistrationList.size() - 1);
        assertThat(testInsuranceRegistration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInsuranceRegistration.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testInsuranceRegistration.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testInsuranceRegistration.getInsuranceRelation()).isEqualTo(UPDATED_INSURANCE_RELATION);
        assertThat(testInsuranceRegistration.getInsuranceStatus()).isEqualTo(UPDATED_INSURANCE_STATUS);
        assertThat(testInsuranceRegistration.getUnapprovalReason()).isEqualTo(UPDATED_UNAPPROVAL_REASON);
        assertThat(testInsuranceRegistration.getAvailableBalance()).isEqualTo(UPDATED_AVAILABLE_BALANCE);
        assertThat(testInsuranceRegistration.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testInsuranceRegistration.getApprovedAt()).isEqualTo(UPDATED_APPROVED_AT);
        assertThat(testInsuranceRegistration.getInsuranceId()).isEqualTo(UPDATED_INSURANCE_ID);
        assertThat(testInsuranceRegistration.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingInsuranceRegistration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();
        insuranceRegistration.setId(count.incrementAndGet());

        // Create the InsuranceRegistration
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsuranceRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insuranceRegistrationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsuranceRegistration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();
        insuranceRegistration.setId(count.incrementAndGet());

        // Create the InsuranceRegistration
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsuranceRegistration() throws Exception {
        int databaseSizeBeforeUpdate = insuranceRegistrationRepository.findAll().size();
        insuranceRegistration.setId(count.incrementAndGet());

        // Create the InsuranceRegistration
        InsuranceRegistrationDTO insuranceRegistrationDTO = insuranceRegistrationMapper.toDto(insuranceRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsuranceRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(insuranceRegistrationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsuranceRegistration in the database
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInsuranceRegistration() throws Exception {
        // Initialize the database
        insuranceRegistrationRepository.saveAndFlush(insuranceRegistration);

        int databaseSizeBeforeDelete = insuranceRegistrationRepository.findAll().size();

        // Delete the insuranceRegistration
        restInsuranceRegistrationMockMvc
            .perform(delete(ENTITY_API_URL_ID, insuranceRegistration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findAll();
        assertThat(insuranceRegistrationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
