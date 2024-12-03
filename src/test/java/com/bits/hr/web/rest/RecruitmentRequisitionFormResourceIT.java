package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Band;
import com.bits.hr.domain.Designation;
import com.bits.hr.domain.RecruitmentRequisitionForm;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.RecruitmentNature;
import com.bits.hr.domain.enumeration.RequisitionResourceType;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.repository.RecruitmentRequisitionFormRepository;
import com.bits.hr.service.RecruitmentRequisitionFormService;
import com.bits.hr.service.dto.RecruitmentRequisitionFormDTO;
import com.bits.hr.service.mapper.RecruitmentRequisitionFormMapper;
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
 * Integration tests for the {@link RecruitmentRequisitionFormResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecruitmentRequisitionFormResourceIT {

    private static final LocalDate DEFAULT_EXPECTED_JOINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPECTED_JOINING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PROJECT = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_VACANCIES = 1;
    private static final Integer UPDATED_NUMBER_OF_VACANCIES = 2;

    private static final EmployeeCategory DEFAULT_EMPLOYMENT_TYPE = EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
    private static final EmployeeCategory UPDATED_EMPLOYMENT_TYPE = EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;

    private static final RequisitionResourceType DEFAULT_RESOURCE_TYPE = RequisitionResourceType.BUDGET;
    private static final RequisitionResourceType UPDATED_RESOURCE_TYPE = RequisitionResourceType.NON_BUDGET;

    private static final String DEFAULT_RRF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RRF_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERRED_EDUCATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_EDUCATION_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_REQUISITION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_REQUISITION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_REQUESTED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUESTED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RECOMMENDATION_DATE_01 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECOMMENDATION_DATE_01 = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RECOMMENDATION_DATE_02 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECOMMENDATION_DATE_02 = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RECOMMENDATION_DATE_03 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECOMMENDATION_DATE_03 = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RECOMMENDATION_DATE_04 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECOMMENDATION_DATE_04 = LocalDate.now(ZoneId.systemDefault());

    private static final RequisitionStatus DEFAULT_REQUISITION_STATUS = RequisitionStatus.PENDING;
    private static final RequisitionStatus UPDATED_REQUISITION_STATUS = RequisitionStatus.IN_PROGRESS;

    private static final LocalDate DEFAULT_REJECTED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REJECTED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Integer DEFAULT_TOTAL_ONBOARD = 1;
    private static final Integer UPDATED_TOTAL_ONBOARD = 2;

    private static final String DEFAULT_PREFERRED_SKILL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_SKILL_TYPE = "BBBBBBBBBB";

    private static final RecruitmentNature DEFAULT_RECRUITMENT_NATURE = RecruitmentNature.PLANNED_ADDITION;
    private static final RecruitmentNature UPDATED_RECRUITMENT_NATURE = RecruitmentNature.NEW_RECRUITMENT;

    private static final String DEFAULT_SPECIAL_REQUIREMENT = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_REQUIREMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RECOMMENDATION_DATE_05 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECOMMENDATION_DATE_05 = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/recruitment-requisition-forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepository;

    @Mock
    private RecruitmentRequisitionFormRepository recruitmentRequisitionFormRepositoryMock;

    @Autowired
    private RecruitmentRequisitionFormMapper recruitmentRequisitionFormMapper;

    @Mock
    private RecruitmentRequisitionFormService recruitmentRequisitionFormServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecruitmentRequisitionFormMockMvc;

    private RecruitmentRequisitionForm recruitmentRequisitionForm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisitionForm createEntity(EntityManager em) {
        RecruitmentRequisitionForm recruitmentRequisitionForm = new RecruitmentRequisitionForm()
            .expectedJoiningDate(DEFAULT_EXPECTED_JOINING_DATE)
            .project(DEFAULT_PROJECT)
            .numberOfVacancies(DEFAULT_NUMBER_OF_VACANCIES)
            .employmentType(DEFAULT_EMPLOYMENT_TYPE)
            .resourceType(DEFAULT_RESOURCE_TYPE)
            .rrfNumber(DEFAULT_RRF_NUMBER)
            .preferredEducationType(DEFAULT_PREFERRED_EDUCATION_TYPE)
            .dateOfRequisition(DEFAULT_DATE_OF_REQUISITION)
            .requestedDate(DEFAULT_REQUESTED_DATE)
            .recommendationDate01(DEFAULT_RECOMMENDATION_DATE_01)
            .recommendationDate02(DEFAULT_RECOMMENDATION_DATE_02)
            .recommendationDate03(DEFAULT_RECOMMENDATION_DATE_03)
            .recommendationDate04(DEFAULT_RECOMMENDATION_DATE_04)
            .requisitionStatus(DEFAULT_REQUISITION_STATUS)
            .rejectedAt(DEFAULT_REJECTED_AT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .isDeleted(DEFAULT_IS_DELETED)
            .totalOnboard(DEFAULT_TOTAL_ONBOARD)
            .preferredSkillType(DEFAULT_PREFERRED_SKILL_TYPE)
            .recruitmentNature(DEFAULT_RECRUITMENT_NATURE)
            .specialRequirement(DEFAULT_SPECIAL_REQUIREMENT)
            .recommendationDate05(DEFAULT_RECOMMENDATION_DATE_05);
        // Add required entity
        Designation designation;
        if (TestUtil.findAll(em, Designation.class).isEmpty()) {
            designation = DesignationResourceIT.createEntity(em);
            em.persist(designation);
            em.flush();
        } else {
            designation = TestUtil.findAll(em, Designation.class).get(0);
        }
        recruitmentRequisitionForm.setFunctionalDesignation(designation);
        // Add required entity
        Band band;
        if (TestUtil.findAll(em, Band.class).isEmpty()) {
            band = BandResourceIT.createEntity(em);
            em.persist(band);
            em.flush();
        } else {
            band = TestUtil.findAll(em, Band.class).get(0);
        }
        recruitmentRequisitionForm.setBand(band);
        return recruitmentRequisitionForm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisitionForm createUpdatedEntity(EntityManager em) {
        RecruitmentRequisitionForm recruitmentRequisitionForm = new RecruitmentRequisitionForm()
            .expectedJoiningDate(UPDATED_EXPECTED_JOINING_DATE)
            .project(UPDATED_PROJECT)
            .numberOfVacancies(UPDATED_NUMBER_OF_VACANCIES)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .rrfNumber(UPDATED_RRF_NUMBER)
            .preferredEducationType(UPDATED_PREFERRED_EDUCATION_TYPE)
            .dateOfRequisition(UPDATED_DATE_OF_REQUISITION)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .recommendationDate01(UPDATED_RECOMMENDATION_DATE_01)
            .recommendationDate02(UPDATED_RECOMMENDATION_DATE_02)
            .recommendationDate03(UPDATED_RECOMMENDATION_DATE_03)
            .recommendationDate04(UPDATED_RECOMMENDATION_DATE_04)
            .requisitionStatus(UPDATED_REQUISITION_STATUS)
            .rejectedAt(UPDATED_REJECTED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isDeleted(UPDATED_IS_DELETED)
            .totalOnboard(UPDATED_TOTAL_ONBOARD)
            .preferredSkillType(UPDATED_PREFERRED_SKILL_TYPE)
            .recruitmentNature(UPDATED_RECRUITMENT_NATURE)
            .specialRequirement(UPDATED_SPECIAL_REQUIREMENT)
            .recommendationDate05(UPDATED_RECOMMENDATION_DATE_05);
        // Add required entity
        Designation designation;
        if (TestUtil.findAll(em, Designation.class).isEmpty()) {
            designation = DesignationResourceIT.createUpdatedEntity(em);
            em.persist(designation);
            em.flush();
        } else {
            designation = TestUtil.findAll(em, Designation.class).get(0);
        }
        recruitmentRequisitionForm.setFunctionalDesignation(designation);
        // Add required entity
        Band band;
        if (TestUtil.findAll(em, Band.class).isEmpty()) {
            band = BandResourceIT.createUpdatedEntity(em);
            em.persist(band);
            em.flush();
        } else {
            band = TestUtil.findAll(em, Band.class).get(0);
        }
        recruitmentRequisitionForm.setBand(band);
        return recruitmentRequisitionForm;
    }

    @BeforeEach
    public void initTest() {
        recruitmentRequisitionForm = createEntity(em);
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionForm() throws Exception {
        int databaseSizeBeforeCreate = recruitmentRequisitionFormRepository.findAll().size();
        // Create the RecruitmentRequisitionForm
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);
        restRecruitmentRequisitionFormMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeCreate + 1);
        RecruitmentRequisitionForm testRecruitmentRequisitionForm = recruitmentRequisitionFormList.get(
            recruitmentRequisitionFormList.size() - 1
        );
        assertThat(testRecruitmentRequisitionForm.getExpectedJoiningDate()).isEqualTo(DEFAULT_EXPECTED_JOINING_DATE);
        assertThat(testRecruitmentRequisitionForm.getProject()).isEqualTo(DEFAULT_PROJECT);
        assertThat(testRecruitmentRequisitionForm.getNumberOfVacancies()).isEqualTo(DEFAULT_NUMBER_OF_VACANCIES);
        assertThat(testRecruitmentRequisitionForm.getEmploymentType()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE);
        assertThat(testRecruitmentRequisitionForm.getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
        assertThat(testRecruitmentRequisitionForm.getRrfNumber()).isEqualTo(DEFAULT_RRF_NUMBER);
        assertThat(testRecruitmentRequisitionForm.getPreferredEducationType()).isEqualTo(DEFAULT_PREFERRED_EDUCATION_TYPE);
        assertThat(testRecruitmentRequisitionForm.getDateOfRequisition()).isEqualTo(DEFAULT_DATE_OF_REQUISITION);
        assertThat(testRecruitmentRequisitionForm.getRequestedDate()).isEqualTo(DEFAULT_REQUESTED_DATE);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate01()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_01);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate02()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_02);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate03()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_03);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate04()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_04);
        assertThat(testRecruitmentRequisitionForm.getRequisitionStatus()).isEqualTo(DEFAULT_REQUISITION_STATUS);
        assertThat(testRecruitmentRequisitionForm.getRejectedAt()).isEqualTo(DEFAULT_REJECTED_AT);
        assertThat(testRecruitmentRequisitionForm.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRecruitmentRequisitionForm.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testRecruitmentRequisitionForm.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testRecruitmentRequisitionForm.getTotalOnboard()).isEqualTo(DEFAULT_TOTAL_ONBOARD);
        assertThat(testRecruitmentRequisitionForm.getPreferredSkillType()).isEqualTo(DEFAULT_PREFERRED_SKILL_TYPE);
        assertThat(testRecruitmentRequisitionForm.getRecruitmentNature()).isEqualTo(DEFAULT_RECRUITMENT_NATURE);
        assertThat(testRecruitmentRequisitionForm.getSpecialRequirement()).isEqualTo(DEFAULT_SPECIAL_REQUIREMENT);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate05()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_05);
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionFormWithExistingId() throws Exception {
        // Create the RecruitmentRequisitionForm with an existing ID
        recruitmentRequisitionForm.setId(1L);
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        int databaseSizeBeforeCreate = recruitmentRequisitionFormRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruitmentRequisitionFormMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExpectedJoiningDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionFormRepository.findAll().size();
        // set the field null
        recruitmentRequisitionForm.setExpectedJoiningDate(null);

        // Create the RecruitmentRequisitionForm, which fails.
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        restRecruitmentRequisitionFormMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfVacanciesIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionFormRepository.findAll().size();
        // set the field null
        recruitmentRequisitionForm.setNumberOfVacancies(null);

        // Create the RecruitmentRequisitionForm, which fails.
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        restRecruitmentRequisitionFormMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmploymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionFormRepository.findAll().size();
        // set the field null
        recruitmentRequisitionForm.setEmploymentType(null);

        // Create the RecruitmentRequisitionForm, which fails.
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        restRecruitmentRequisitionFormMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResourceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionFormRepository.findAll().size();
        // set the field null
        recruitmentRequisitionForm.setResourceType(null);

        // Create the RecruitmentRequisitionForm, which fails.
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        restRecruitmentRequisitionFormMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequisitionStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionFormRepository.findAll().size();
        // set the field null
        recruitmentRequisitionForm.setRequisitionStatus(null);

        // Create the RecruitmentRequisitionForm, which fails.
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        restRecruitmentRequisitionFormMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionForms() throws Exception {
        // Initialize the database
        recruitmentRequisitionFormRepository.saveAndFlush(recruitmentRequisitionForm);

        // Get all the recruitmentRequisitionFormList
        restRecruitmentRequisitionFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentRequisitionForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].expectedJoiningDate").value(hasItem(DEFAULT_EXPECTED_JOINING_DATE.toString())))
            .andExpect(jsonPath("$.[*].project").value(hasItem(DEFAULT_PROJECT)))
            .andExpect(jsonPath("$.[*].numberOfVacancies").value(hasItem(DEFAULT_NUMBER_OF_VACANCIES)))
            .andExpect(jsonPath("$.[*].employmentType").value(hasItem(DEFAULT_EMPLOYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].resourceType").value(hasItem(DEFAULT_RESOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rrfNumber").value(hasItem(DEFAULT_RRF_NUMBER)))
            .andExpect(jsonPath("$.[*].preferredEducationType").value(hasItem(DEFAULT_PREFERRED_EDUCATION_TYPE)))
            .andExpect(jsonPath("$.[*].dateOfRequisition").value(hasItem(DEFAULT_DATE_OF_REQUISITION.toString())))
            .andExpect(jsonPath("$.[*].requestedDate").value(hasItem(DEFAULT_REQUESTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].recommendationDate01").value(hasItem(DEFAULT_RECOMMENDATION_DATE_01.toString())))
            .andExpect(jsonPath("$.[*].recommendationDate02").value(hasItem(DEFAULT_RECOMMENDATION_DATE_02.toString())))
            .andExpect(jsonPath("$.[*].recommendationDate03").value(hasItem(DEFAULT_RECOMMENDATION_DATE_03.toString())))
            .andExpect(jsonPath("$.[*].recommendationDate04").value(hasItem(DEFAULT_RECOMMENDATION_DATE_04.toString())))
            .andExpect(jsonPath("$.[*].requisitionStatus").value(hasItem(DEFAULT_REQUISITION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].rejectedAt").value(hasItem(DEFAULT_REJECTED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].totalOnboard").value(hasItem(DEFAULT_TOTAL_ONBOARD)))
            .andExpect(jsonPath("$.[*].preferredSkillType").value(hasItem(DEFAULT_PREFERRED_SKILL_TYPE)))
            .andExpect(jsonPath("$.[*].recruitmentNature").value(hasItem(DEFAULT_RECRUITMENT_NATURE.toString())))
            .andExpect(jsonPath("$.[*].specialRequirement").value(hasItem(DEFAULT_SPECIAL_REQUIREMENT)))
            .andExpect(jsonPath("$.[*].recommendationDate05").value(hasItem(DEFAULT_RECOMMENDATION_DATE_05.toString())));
    }


    @Test
    @Transactional
    void getRecruitmentRequisitionForm() throws Exception {
        // Initialize the database
        recruitmentRequisitionFormRepository.saveAndFlush(recruitmentRequisitionForm);

        // Get the recruitmentRequisitionForm
        restRecruitmentRequisitionFormMockMvc
            .perform(get(ENTITY_API_URL_ID, recruitmentRequisitionForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recruitmentRequisitionForm.getId().intValue()))
            .andExpect(jsonPath("$.expectedJoiningDate").value(DEFAULT_EXPECTED_JOINING_DATE.toString()))
            .andExpect(jsonPath("$.project").value(DEFAULT_PROJECT))
            .andExpect(jsonPath("$.numberOfVacancies").value(DEFAULT_NUMBER_OF_VACANCIES))
            .andExpect(jsonPath("$.employmentType").value(DEFAULT_EMPLOYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.resourceType").value(DEFAULT_RESOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.rrfNumber").value(DEFAULT_RRF_NUMBER))
            .andExpect(jsonPath("$.preferredEducationType").value(DEFAULT_PREFERRED_EDUCATION_TYPE))
            .andExpect(jsonPath("$.dateOfRequisition").value(DEFAULT_DATE_OF_REQUISITION.toString()))
            .andExpect(jsonPath("$.requestedDate").value(DEFAULT_REQUESTED_DATE.toString()))
            .andExpect(jsonPath("$.recommendationDate01").value(DEFAULT_RECOMMENDATION_DATE_01.toString()))
            .andExpect(jsonPath("$.recommendationDate02").value(DEFAULT_RECOMMENDATION_DATE_02.toString()))
            .andExpect(jsonPath("$.recommendationDate03").value(DEFAULT_RECOMMENDATION_DATE_03.toString()))
            .andExpect(jsonPath("$.recommendationDate04").value(DEFAULT_RECOMMENDATION_DATE_04.toString()))
            .andExpect(jsonPath("$.requisitionStatus").value(DEFAULT_REQUISITION_STATUS.toString()))
            .andExpect(jsonPath("$.rejectedAt").value(DEFAULT_REJECTED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.totalOnboard").value(DEFAULT_TOTAL_ONBOARD))
            .andExpect(jsonPath("$.preferredSkillType").value(DEFAULT_PREFERRED_SKILL_TYPE))
            .andExpect(jsonPath("$.recruitmentNature").value(DEFAULT_RECRUITMENT_NATURE.toString()))
            .andExpect(jsonPath("$.specialRequirement").value(DEFAULT_SPECIAL_REQUIREMENT))
            .andExpect(jsonPath("$.recommendationDate05").value(DEFAULT_RECOMMENDATION_DATE_05.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRecruitmentRequisitionForm() throws Exception {
        // Get the recruitmentRequisitionForm
        restRecruitmentRequisitionFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecruitmentRequisitionForm() throws Exception {
        // Initialize the database
        recruitmentRequisitionFormRepository.saveAndFlush(recruitmentRequisitionForm);

        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();

        // Update the recruitmentRequisitionForm
        RecruitmentRequisitionForm updatedRecruitmentRequisitionForm = recruitmentRequisitionFormRepository
            .findById(recruitmentRequisitionForm.getId())
            .get();
        // Disconnect from session so that the updates on updatedRecruitmentRequisitionForm are not directly saved in db
        em.detach(updatedRecruitmentRequisitionForm);
        updatedRecruitmentRequisitionForm
            .expectedJoiningDate(UPDATED_EXPECTED_JOINING_DATE)
            .project(UPDATED_PROJECT)
            .numberOfVacancies(UPDATED_NUMBER_OF_VACANCIES)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .rrfNumber(UPDATED_RRF_NUMBER)
            .preferredEducationType(UPDATED_PREFERRED_EDUCATION_TYPE)
            .dateOfRequisition(UPDATED_DATE_OF_REQUISITION)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .recommendationDate01(UPDATED_RECOMMENDATION_DATE_01)
            .recommendationDate02(UPDATED_RECOMMENDATION_DATE_02)
            .recommendationDate03(UPDATED_RECOMMENDATION_DATE_03)
            .recommendationDate04(UPDATED_RECOMMENDATION_DATE_04)
            .requisitionStatus(UPDATED_REQUISITION_STATUS)
            .rejectedAt(UPDATED_REJECTED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isDeleted(UPDATED_IS_DELETED)
            .totalOnboard(UPDATED_TOTAL_ONBOARD)
            .preferredSkillType(UPDATED_PREFERRED_SKILL_TYPE)
            .recruitmentNature(UPDATED_RECRUITMENT_NATURE)
            .specialRequirement(UPDATED_SPECIAL_REQUIREMENT)
            .recommendationDate05(UPDATED_RECOMMENDATION_DATE_05);
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(
            updatedRecruitmentRequisitionForm
        );

        restRecruitmentRequisitionFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionFormDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
        RecruitmentRequisitionForm testRecruitmentRequisitionForm = recruitmentRequisitionFormList.get(
            recruitmentRequisitionFormList.size() - 1
        );
        assertThat(testRecruitmentRequisitionForm.getExpectedJoiningDate()).isEqualTo(UPDATED_EXPECTED_JOINING_DATE);
        assertThat(testRecruitmentRequisitionForm.getProject()).isEqualTo(UPDATED_PROJECT);
        assertThat(testRecruitmentRequisitionForm.getNumberOfVacancies()).isEqualTo(UPDATED_NUMBER_OF_VACANCIES);
        assertThat(testRecruitmentRequisitionForm.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testRecruitmentRequisitionForm.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
        assertThat(testRecruitmentRequisitionForm.getRrfNumber()).isEqualTo(UPDATED_RRF_NUMBER);
        assertThat(testRecruitmentRequisitionForm.getPreferredEducationType()).isEqualTo(UPDATED_PREFERRED_EDUCATION_TYPE);
        assertThat(testRecruitmentRequisitionForm.getDateOfRequisition()).isEqualTo(UPDATED_DATE_OF_REQUISITION);
        assertThat(testRecruitmentRequisitionForm.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate01()).isEqualTo(UPDATED_RECOMMENDATION_DATE_01);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate02()).isEqualTo(UPDATED_RECOMMENDATION_DATE_02);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate03()).isEqualTo(UPDATED_RECOMMENDATION_DATE_03);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate04()).isEqualTo(UPDATED_RECOMMENDATION_DATE_04);
        assertThat(testRecruitmentRequisitionForm.getRequisitionStatus()).isEqualTo(UPDATED_REQUISITION_STATUS);
        assertThat(testRecruitmentRequisitionForm.getRejectedAt()).isEqualTo(UPDATED_REJECTED_AT);
        assertThat(testRecruitmentRequisitionForm.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRecruitmentRequisitionForm.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testRecruitmentRequisitionForm.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testRecruitmentRequisitionForm.getTotalOnboard()).isEqualTo(UPDATED_TOTAL_ONBOARD);
        assertThat(testRecruitmentRequisitionForm.getPreferredSkillType()).isEqualTo(UPDATED_PREFERRED_SKILL_TYPE);
        assertThat(testRecruitmentRequisitionForm.getRecruitmentNature()).isEqualTo(UPDATED_RECRUITMENT_NATURE);
        assertThat(testRecruitmentRequisitionForm.getSpecialRequirement()).isEqualTo(UPDATED_SPECIAL_REQUIREMENT);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate05()).isEqualTo(UPDATED_RECOMMENDATION_DATE_05);
    }

    @Test
    @Transactional
    void putNonExistingRecruitmentRequisitionForm() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();
        recruitmentRequisitionForm.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionForm
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionFormDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecruitmentRequisitionForm() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();
        recruitmentRequisitionForm.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionForm
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecruitmentRequisitionForm() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();
        recruitmentRequisitionForm.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionForm
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionFormMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecruitmentRequisitionFormWithPatch() throws Exception {
        // Initialize the database
        recruitmentRequisitionFormRepository.saveAndFlush(recruitmentRequisitionForm);

        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();

        // Update the recruitmentRequisitionForm using partial update
        RecruitmentRequisitionForm partialUpdatedRecruitmentRequisitionForm = new RecruitmentRequisitionForm();
        partialUpdatedRecruitmentRequisitionForm.setId(recruitmentRequisitionForm.getId());

        partialUpdatedRecruitmentRequisitionForm
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .preferredEducationType(UPDATED_PREFERRED_EDUCATION_TYPE)
            .recommendationDate01(UPDATED_RECOMMENDATION_DATE_01)
            .rejectedAt(UPDATED_REJECTED_AT)
            .totalOnboard(UPDATED_TOTAL_ONBOARD)
            .recruitmentNature(UPDATED_RECRUITMENT_NATURE)
            .specialRequirement(UPDATED_SPECIAL_REQUIREMENT);

        restRecruitmentRequisitionFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisitionForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecruitmentRequisitionForm))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
        RecruitmentRequisitionForm testRecruitmentRequisitionForm = recruitmentRequisitionFormList.get(
            recruitmentRequisitionFormList.size() - 1
        );
        assertThat(testRecruitmentRequisitionForm.getExpectedJoiningDate()).isEqualTo(DEFAULT_EXPECTED_JOINING_DATE);
        assertThat(testRecruitmentRequisitionForm.getProject()).isEqualTo(DEFAULT_PROJECT);
        assertThat(testRecruitmentRequisitionForm.getNumberOfVacancies()).isEqualTo(DEFAULT_NUMBER_OF_VACANCIES);
        assertThat(testRecruitmentRequisitionForm.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testRecruitmentRequisitionForm.getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
        assertThat(testRecruitmentRequisitionForm.getRrfNumber()).isEqualTo(DEFAULT_RRF_NUMBER);
        assertThat(testRecruitmentRequisitionForm.getPreferredEducationType()).isEqualTo(UPDATED_PREFERRED_EDUCATION_TYPE);
        assertThat(testRecruitmentRequisitionForm.getDateOfRequisition()).isEqualTo(DEFAULT_DATE_OF_REQUISITION);
        assertThat(testRecruitmentRequisitionForm.getRequestedDate()).isEqualTo(DEFAULT_REQUESTED_DATE);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate01()).isEqualTo(UPDATED_RECOMMENDATION_DATE_01);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate02()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_02);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate03()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_03);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate04()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_04);
        assertThat(testRecruitmentRequisitionForm.getRequisitionStatus()).isEqualTo(DEFAULT_REQUISITION_STATUS);
        assertThat(testRecruitmentRequisitionForm.getRejectedAt()).isEqualTo(UPDATED_REJECTED_AT);
        assertThat(testRecruitmentRequisitionForm.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRecruitmentRequisitionForm.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testRecruitmentRequisitionForm.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testRecruitmentRequisitionForm.getTotalOnboard()).isEqualTo(UPDATED_TOTAL_ONBOARD);
        assertThat(testRecruitmentRequisitionForm.getPreferredSkillType()).isEqualTo(DEFAULT_PREFERRED_SKILL_TYPE);
        assertThat(testRecruitmentRequisitionForm.getRecruitmentNature()).isEqualTo(UPDATED_RECRUITMENT_NATURE);
        assertThat(testRecruitmentRequisitionForm.getSpecialRequirement()).isEqualTo(UPDATED_SPECIAL_REQUIREMENT);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate05()).isEqualTo(DEFAULT_RECOMMENDATION_DATE_05);
    }

    @Test
    @Transactional
    void fullUpdateRecruitmentRequisitionFormWithPatch() throws Exception {
        // Initialize the database
        recruitmentRequisitionFormRepository.saveAndFlush(recruitmentRequisitionForm);

        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();

        // Update the recruitmentRequisitionForm using partial update
        RecruitmentRequisitionForm partialUpdatedRecruitmentRequisitionForm = new RecruitmentRequisitionForm();
        partialUpdatedRecruitmentRequisitionForm.setId(recruitmentRequisitionForm.getId());

        partialUpdatedRecruitmentRequisitionForm
            .expectedJoiningDate(UPDATED_EXPECTED_JOINING_DATE)
            .project(UPDATED_PROJECT)
            .numberOfVacancies(UPDATED_NUMBER_OF_VACANCIES)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .resourceType(UPDATED_RESOURCE_TYPE)
            .rrfNumber(UPDATED_RRF_NUMBER)
            .preferredEducationType(UPDATED_PREFERRED_EDUCATION_TYPE)
            .dateOfRequisition(UPDATED_DATE_OF_REQUISITION)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .recommendationDate01(UPDATED_RECOMMENDATION_DATE_01)
            .recommendationDate02(UPDATED_RECOMMENDATION_DATE_02)
            .recommendationDate03(UPDATED_RECOMMENDATION_DATE_03)
            .recommendationDate04(UPDATED_RECOMMENDATION_DATE_04)
            .requisitionStatus(UPDATED_REQUISITION_STATUS)
            .rejectedAt(UPDATED_REJECTED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .isDeleted(UPDATED_IS_DELETED)
            .totalOnboard(UPDATED_TOTAL_ONBOARD)
            .preferredSkillType(UPDATED_PREFERRED_SKILL_TYPE)
            .recruitmentNature(UPDATED_RECRUITMENT_NATURE)
            .specialRequirement(UPDATED_SPECIAL_REQUIREMENT)
            .recommendationDate05(UPDATED_RECOMMENDATION_DATE_05);

        restRecruitmentRequisitionFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisitionForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecruitmentRequisitionForm))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
        RecruitmentRequisitionForm testRecruitmentRequisitionForm = recruitmentRequisitionFormList.get(
            recruitmentRequisitionFormList.size() - 1
        );
        assertThat(testRecruitmentRequisitionForm.getExpectedJoiningDate()).isEqualTo(UPDATED_EXPECTED_JOINING_DATE);
        assertThat(testRecruitmentRequisitionForm.getProject()).isEqualTo(UPDATED_PROJECT);
        assertThat(testRecruitmentRequisitionForm.getNumberOfVacancies()).isEqualTo(UPDATED_NUMBER_OF_VACANCIES);
        assertThat(testRecruitmentRequisitionForm.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testRecruitmentRequisitionForm.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
        assertThat(testRecruitmentRequisitionForm.getRrfNumber()).isEqualTo(UPDATED_RRF_NUMBER);
        assertThat(testRecruitmentRequisitionForm.getPreferredEducationType()).isEqualTo(UPDATED_PREFERRED_EDUCATION_TYPE);
        assertThat(testRecruitmentRequisitionForm.getDateOfRequisition()).isEqualTo(UPDATED_DATE_OF_REQUISITION);
        assertThat(testRecruitmentRequisitionForm.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate01()).isEqualTo(UPDATED_RECOMMENDATION_DATE_01);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate02()).isEqualTo(UPDATED_RECOMMENDATION_DATE_02);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate03()).isEqualTo(UPDATED_RECOMMENDATION_DATE_03);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate04()).isEqualTo(UPDATED_RECOMMENDATION_DATE_04);
        assertThat(testRecruitmentRequisitionForm.getRequisitionStatus()).isEqualTo(UPDATED_REQUISITION_STATUS);
        assertThat(testRecruitmentRequisitionForm.getRejectedAt()).isEqualTo(UPDATED_REJECTED_AT);
        assertThat(testRecruitmentRequisitionForm.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRecruitmentRequisitionForm.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testRecruitmentRequisitionForm.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testRecruitmentRequisitionForm.getTotalOnboard()).isEqualTo(UPDATED_TOTAL_ONBOARD);
        assertThat(testRecruitmentRequisitionForm.getPreferredSkillType()).isEqualTo(UPDATED_PREFERRED_SKILL_TYPE);
        assertThat(testRecruitmentRequisitionForm.getRecruitmentNature()).isEqualTo(UPDATED_RECRUITMENT_NATURE);
        assertThat(testRecruitmentRequisitionForm.getSpecialRequirement()).isEqualTo(UPDATED_SPECIAL_REQUIREMENT);
        assertThat(testRecruitmentRequisitionForm.getRecommendationDate05()).isEqualTo(UPDATED_RECOMMENDATION_DATE_05);
    }

    @Test
    @Transactional
    void patchNonExistingRecruitmentRequisitionForm() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();
        recruitmentRequisitionForm.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionForm
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recruitmentRequisitionFormDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecruitmentRequisitionForm() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();
        recruitmentRequisitionForm.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionForm
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecruitmentRequisitionForm() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionFormRepository.findAll().size();
        recruitmentRequisitionForm.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionForm
        RecruitmentRequisitionFormDTO recruitmentRequisitionFormDTO = recruitmentRequisitionFormMapper.toDto(recruitmentRequisitionForm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionFormMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionFormDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisitionForm in the database
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecruitmentRequisitionForm() throws Exception {
        // Initialize the database
        recruitmentRequisitionFormRepository.saveAndFlush(recruitmentRequisitionForm);

        int databaseSizeBeforeDelete = recruitmentRequisitionFormRepository.findAll().size();

        // Delete the recruitmentRequisitionForm
        restRecruitmentRequisitionFormMockMvc
            .perform(delete(ENTITY_API_URL_ID, recruitmentRequisitionForm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecruitmentRequisitionForm> recruitmentRequisitionFormList = recruitmentRequisitionFormRepository.findAll();
        assertThat(recruitmentRequisitionFormList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
