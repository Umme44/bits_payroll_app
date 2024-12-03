package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Department;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.ProcReqMaster;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.repository.ProcReqMasterRepository;
import com.bits.hr.service.dto.ProcReqMasterDTO;
import com.bits.hr.service.mapper.ProcReqMasterMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ProcReqMasterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcReqMasterResourceIT {

    private static final String DEFAULT_REQUISITION_NO = "AAAAAAAAAA";
    private static final String UPDATED_REQUISITION_NO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REQUESTED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUESTED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_CTO_APPROVAL_REQUIRED = false;
    private static final Boolean UPDATED_IS_CTO_APPROVAL_REQUIRED = true;

    private static final RequisitionStatus DEFAULT_REQUISITION_STATUS = RequisitionStatus.PENDING;
    private static final RequisitionStatus UPDATED_REQUISITION_STATUS = RequisitionStatus.IN_PROGRESS;

    private static final LocalDate DEFAULT_EXPECTED_RECEIVED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPECTED_RECEIVED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASONING = "AAAAAAAAAA";
    private static final String UPDATED_REASONING = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_APPROXIMATE_PRICE = 1D;
    private static final Double UPDATED_TOTAL_APPROXIMATE_PRICE = 2D;

    private static final Instant DEFAULT_RECOMMENDATION_AT_01 = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECOMMENDATION_AT_01 = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RECOMMENDATION_AT_02 = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECOMMENDATION_AT_02 = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RECOMMENDATION_AT_03 = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECOMMENDATION_AT_03 = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RECOMMENDATION_AT_04 = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECOMMENDATION_AT_04 = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RECOMMENDATION_AT_05 = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECOMMENDATION_AT_05 = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NEXT_RECOMMENDATION_ORDER = 1;
    private static final Integer UPDATED_NEXT_RECOMMENDATION_ORDER = 2;

    private static final LocalDate DEFAULT_REJECTED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REJECTED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REJECTION_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REJECTION_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_CLOSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/proc-req-masters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcReqMasterRepository procReqMasterRepository;

    @Autowired
    private ProcReqMasterMapper procReqMasterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcReqMasterMockMvc;

    private ProcReqMaster procReqMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcReqMaster createEntity(EntityManager em) {
        ProcReqMaster procReqMaster = new ProcReqMaster()
            .requisitionNo(DEFAULT_REQUISITION_NO)
            .requestedDate(DEFAULT_REQUESTED_DATE)
            .isCTOApprovalRequired(DEFAULT_IS_CTO_APPROVAL_REQUIRED)
            .requisitionStatus(DEFAULT_REQUISITION_STATUS)
            .expectedReceivedDate(DEFAULT_EXPECTED_RECEIVED_DATE)
            .reasoning(DEFAULT_REASONING)
            .totalApproximatePrice(DEFAULT_TOTAL_APPROXIMATE_PRICE)
            .recommendationAt01(DEFAULT_RECOMMENDATION_AT_01)
            .recommendationAt02(DEFAULT_RECOMMENDATION_AT_02)
            .recommendationAt03(DEFAULT_RECOMMENDATION_AT_03)
            .recommendationAt04(DEFAULT_RECOMMENDATION_AT_04)
            .recommendationAt05(DEFAULT_RECOMMENDATION_AT_05)
            .nextRecommendationOrder(DEFAULT_NEXT_RECOMMENDATION_ORDER)
            .rejectedDate(DEFAULT_REJECTED_DATE)
            .rejectionReason(DEFAULT_REJECTION_REASON)
            .closedAt(DEFAULT_CLOSED_AT)
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
        procReqMaster.setDepartment(department);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        procReqMaster.setRequestedBy(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        procReqMaster.setCreatedBy(user);
        return procReqMaster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcReqMaster createUpdatedEntity(EntityManager em) {
        ProcReqMaster procReqMaster = new ProcReqMaster()
            .requisitionNo(UPDATED_REQUISITION_NO)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .isCTOApprovalRequired(UPDATED_IS_CTO_APPROVAL_REQUIRED)
            .requisitionStatus(UPDATED_REQUISITION_STATUS)
            .expectedReceivedDate(UPDATED_EXPECTED_RECEIVED_DATE)
            .reasoning(UPDATED_REASONING)
            .totalApproximatePrice(UPDATED_TOTAL_APPROXIMATE_PRICE)
            .recommendationAt01(UPDATED_RECOMMENDATION_AT_01)
            .recommendationAt02(UPDATED_RECOMMENDATION_AT_02)
            .recommendationAt03(UPDATED_RECOMMENDATION_AT_03)
            .recommendationAt04(UPDATED_RECOMMENDATION_AT_04)
            .recommendationAt05(UPDATED_RECOMMENDATION_AT_05)
            .nextRecommendationOrder(UPDATED_NEXT_RECOMMENDATION_ORDER)
            .rejectedDate(UPDATED_REJECTED_DATE)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .closedAt(UPDATED_CLOSED_AT)
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
        procReqMaster.setDepartment(department);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        procReqMaster.setRequestedBy(employee);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        procReqMaster.setCreatedBy(user);
        return procReqMaster;
    }

    @BeforeEach
    public void initTest() {
        procReqMaster = createEntity(em);
    }

    @Test
    @Transactional
    void createProcReqMaster() throws Exception {
        int databaseSizeBeforeCreate = procReqMasterRepository.findAll().size();
        // Create the ProcReqMaster
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);
        restProcReqMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeCreate + 1);
        ProcReqMaster testProcReqMaster = procReqMasterList.get(procReqMasterList.size() - 1);
        assertThat(testProcReqMaster.getRequisitionNo()).isEqualTo(DEFAULT_REQUISITION_NO);
        assertThat(testProcReqMaster.getRequestedDate()).isEqualTo(DEFAULT_REQUESTED_DATE);
        assertThat(testProcReqMaster.getIsCTOApprovalRequired()).isEqualTo(DEFAULT_IS_CTO_APPROVAL_REQUIRED);
        assertThat(testProcReqMaster.getRequisitionStatus()).isEqualTo(DEFAULT_REQUISITION_STATUS);
        assertThat(testProcReqMaster.getExpectedReceivedDate()).isEqualTo(DEFAULT_EXPECTED_RECEIVED_DATE);
        assertThat(testProcReqMaster.getReasoning()).isEqualTo(DEFAULT_REASONING);
        assertThat(testProcReqMaster.getTotalApproximatePrice()).isEqualTo(DEFAULT_TOTAL_APPROXIMATE_PRICE);
        assertThat(testProcReqMaster.getRecommendationAt01()).isEqualTo(DEFAULT_RECOMMENDATION_AT_01);
        assertThat(testProcReqMaster.getRecommendationAt02()).isEqualTo(DEFAULT_RECOMMENDATION_AT_02);
        assertThat(testProcReqMaster.getRecommendationAt03()).isEqualTo(DEFAULT_RECOMMENDATION_AT_03);
        assertThat(testProcReqMaster.getRecommendationAt04()).isEqualTo(DEFAULT_RECOMMENDATION_AT_04);
        assertThat(testProcReqMaster.getRecommendationAt05()).isEqualTo(DEFAULT_RECOMMENDATION_AT_05);
        assertThat(testProcReqMaster.getNextRecommendationOrder()).isEqualTo(DEFAULT_NEXT_RECOMMENDATION_ORDER);
        assertThat(testProcReqMaster.getRejectedDate()).isEqualTo(DEFAULT_REJECTED_DATE);
        assertThat(testProcReqMaster.getRejectionReason()).isEqualTo(DEFAULT_REJECTION_REASON);
        assertThat(testProcReqMaster.getClosedAt()).isEqualTo(DEFAULT_CLOSED_AT);
        assertThat(testProcReqMaster.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProcReqMaster.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createProcReqMasterWithExistingId() throws Exception {
        // Create the ProcReqMaster with an existing ID
        procReqMaster.setId(1L);
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        int databaseSizeBeforeCreate = procReqMasterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcReqMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRequisitionNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = procReqMasterRepository.findAll().size();
        // set the field null
        procReqMaster.setRequisitionNo(null);

        // Create the ProcReqMaster, which fails.
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        restProcReqMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = procReqMasterRepository.findAll().size();
        // set the field null
        procReqMaster.setRequestedDate(null);

        // Create the ProcReqMaster, which fails.
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        restProcReqMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequisitionStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = procReqMasterRepository.findAll().size();
        // set the field null
        procReqMaster.setRequisitionStatus(null);

        // Create the ProcReqMaster, which fails.
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        restProcReqMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = procReqMasterRepository.findAll().size();
        // set the field null
        procReqMaster.setCreatedAt(null);

        // Create the ProcReqMaster, which fails.
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        restProcReqMasterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProcReqMasters() throws Exception {
        // Initialize the database
        procReqMasterRepository.saveAndFlush(procReqMaster);

        // Get all the procReqMasterList
        restProcReqMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procReqMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].requisitionNo").value(hasItem(DEFAULT_REQUISITION_NO)))
            .andExpect(jsonPath("$.[*].requestedDate").value(hasItem(DEFAULT_REQUESTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isCTOApprovalRequired").value(hasItem(DEFAULT_IS_CTO_APPROVAL_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].requisitionStatus").value(hasItem(DEFAULT_REQUISITION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expectedReceivedDate").value(hasItem(DEFAULT_EXPECTED_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reasoning").value(hasItem(DEFAULT_REASONING.toString())))
            .andExpect(jsonPath("$.[*].totalApproximatePrice").value(hasItem(DEFAULT_TOTAL_APPROXIMATE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].recommendationAt01").value(hasItem(DEFAULT_RECOMMENDATION_AT_01.toString())))
            .andExpect(jsonPath("$.[*].recommendationAt02").value(hasItem(DEFAULT_RECOMMENDATION_AT_02.toString())))
            .andExpect(jsonPath("$.[*].recommendationAt03").value(hasItem(DEFAULT_RECOMMENDATION_AT_03.toString())))
            .andExpect(jsonPath("$.[*].recommendationAt04").value(hasItem(DEFAULT_RECOMMENDATION_AT_04.toString())))
            .andExpect(jsonPath("$.[*].recommendationAt05").value(hasItem(DEFAULT_RECOMMENDATION_AT_05.toString())))
            .andExpect(jsonPath("$.[*].nextRecommendationOrder").value(hasItem(DEFAULT_NEXT_RECOMMENDATION_ORDER)))
            .andExpect(jsonPath("$.[*].rejectedDate").value(hasItem(DEFAULT_REJECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].rejectionReason").value(hasItem(DEFAULT_REJECTION_REASON.toString())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getProcReqMaster() throws Exception {
        // Initialize the database
        procReqMasterRepository.saveAndFlush(procReqMaster);

        // Get the procReqMaster
        restProcReqMasterMockMvc
            .perform(get(ENTITY_API_URL_ID, procReqMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(procReqMaster.getId().intValue()))
            .andExpect(jsonPath("$.requisitionNo").value(DEFAULT_REQUISITION_NO))
            .andExpect(jsonPath("$.requestedDate").value(DEFAULT_REQUESTED_DATE.toString()))
            .andExpect(jsonPath("$.isCTOApprovalRequired").value(DEFAULT_IS_CTO_APPROVAL_REQUIRED.booleanValue()))
            .andExpect(jsonPath("$.requisitionStatus").value(DEFAULT_REQUISITION_STATUS.toString()))
            .andExpect(jsonPath("$.expectedReceivedDate").value(DEFAULT_EXPECTED_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.reasoning").value(DEFAULT_REASONING.toString()))
            .andExpect(jsonPath("$.totalApproximatePrice").value(DEFAULT_TOTAL_APPROXIMATE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.recommendationAt01").value(DEFAULT_RECOMMENDATION_AT_01.toString()))
            .andExpect(jsonPath("$.recommendationAt02").value(DEFAULT_RECOMMENDATION_AT_02.toString()))
            .andExpect(jsonPath("$.recommendationAt03").value(DEFAULT_RECOMMENDATION_AT_03.toString()))
            .andExpect(jsonPath("$.recommendationAt04").value(DEFAULT_RECOMMENDATION_AT_04.toString()))
            .andExpect(jsonPath("$.recommendationAt05").value(DEFAULT_RECOMMENDATION_AT_05.toString()))
            .andExpect(jsonPath("$.nextRecommendationOrder").value(DEFAULT_NEXT_RECOMMENDATION_ORDER))
            .andExpect(jsonPath("$.rejectedDate").value(DEFAULT_REJECTED_DATE.toString()))
            .andExpect(jsonPath("$.rejectionReason").value(DEFAULT_REJECTION_REASON.toString()))
            .andExpect(jsonPath("$.closedAt").value(DEFAULT_CLOSED_AT.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProcReqMaster() throws Exception {
        // Get the procReqMaster
        restProcReqMasterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcReqMaster() throws Exception {
        // Initialize the database
        procReqMasterRepository.saveAndFlush(procReqMaster);

        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();

        // Update the procReqMaster
        ProcReqMaster updatedProcReqMaster = procReqMasterRepository.findById(procReqMaster.getId()).get();
        // Disconnect from session so that the updates on updatedProcReqMaster are not directly saved in db
        em.detach(updatedProcReqMaster);
        updatedProcReqMaster
            .requisitionNo(UPDATED_REQUISITION_NO)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .isCTOApprovalRequired(UPDATED_IS_CTO_APPROVAL_REQUIRED)
            .requisitionStatus(UPDATED_REQUISITION_STATUS)
            .expectedReceivedDate(UPDATED_EXPECTED_RECEIVED_DATE)
            .reasoning(UPDATED_REASONING)
            .totalApproximatePrice(UPDATED_TOTAL_APPROXIMATE_PRICE)
            .recommendationAt01(UPDATED_RECOMMENDATION_AT_01)
            .recommendationAt02(UPDATED_RECOMMENDATION_AT_02)
            .recommendationAt03(UPDATED_RECOMMENDATION_AT_03)
            .recommendationAt04(UPDATED_RECOMMENDATION_AT_04)
            .recommendationAt05(UPDATED_RECOMMENDATION_AT_05)
            .nextRecommendationOrder(UPDATED_NEXT_RECOMMENDATION_ORDER)
            .rejectedDate(UPDATED_REJECTED_DATE)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .closedAt(UPDATED_CLOSED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(updatedProcReqMaster);

        restProcReqMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, procReqMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
        ProcReqMaster testProcReqMaster = procReqMasterList.get(procReqMasterList.size() - 1);
        assertThat(testProcReqMaster.getRequisitionNo()).isEqualTo(UPDATED_REQUISITION_NO);
        assertThat(testProcReqMaster.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testProcReqMaster.getIsCTOApprovalRequired()).isEqualTo(UPDATED_IS_CTO_APPROVAL_REQUIRED);
        assertThat(testProcReqMaster.getRequisitionStatus()).isEqualTo(UPDATED_REQUISITION_STATUS);
        assertThat(testProcReqMaster.getExpectedReceivedDate()).isEqualTo(UPDATED_EXPECTED_RECEIVED_DATE);
        assertThat(testProcReqMaster.getReasoning()).isEqualTo(UPDATED_REASONING);
        assertThat(testProcReqMaster.getTotalApproximatePrice()).isEqualTo(UPDATED_TOTAL_APPROXIMATE_PRICE);
        assertThat(testProcReqMaster.getRecommendationAt01()).isEqualTo(UPDATED_RECOMMENDATION_AT_01);
        assertThat(testProcReqMaster.getRecommendationAt02()).isEqualTo(UPDATED_RECOMMENDATION_AT_02);
        assertThat(testProcReqMaster.getRecommendationAt03()).isEqualTo(UPDATED_RECOMMENDATION_AT_03);
        assertThat(testProcReqMaster.getRecommendationAt04()).isEqualTo(UPDATED_RECOMMENDATION_AT_04);
        assertThat(testProcReqMaster.getRecommendationAt05()).isEqualTo(UPDATED_RECOMMENDATION_AT_05);
        assertThat(testProcReqMaster.getNextRecommendationOrder()).isEqualTo(UPDATED_NEXT_RECOMMENDATION_ORDER);
        assertThat(testProcReqMaster.getRejectedDate()).isEqualTo(UPDATED_REJECTED_DATE);
        assertThat(testProcReqMaster.getRejectionReason()).isEqualTo(UPDATED_REJECTION_REASON);
        assertThat(testProcReqMaster.getClosedAt()).isEqualTo(UPDATED_CLOSED_AT);
        assertThat(testProcReqMaster.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProcReqMaster.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingProcReqMaster() throws Exception {
        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();
        procReqMaster.setId(count.incrementAndGet());

        // Create the ProcReqMaster
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcReqMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, procReqMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcReqMaster() throws Exception {
        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();
        procReqMaster.setId(count.incrementAndGet());

        // Create the ProcReqMaster
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcReqMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcReqMaster() throws Exception {
        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();
        procReqMaster.setId(count.incrementAndGet());

        // Create the ProcReqMaster
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcReqMasterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcReqMasterWithPatch() throws Exception {
        // Initialize the database
        procReqMasterRepository.saveAndFlush(procReqMaster);

        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();

        // Update the procReqMaster using partial update
        ProcReqMaster partialUpdatedProcReqMaster = new ProcReqMaster();
        partialUpdatedProcReqMaster.setId(procReqMaster.getId());

        partialUpdatedProcReqMaster
            .requisitionStatus(UPDATED_REQUISITION_STATUS)
            .reasoning(UPDATED_REASONING)
            .totalApproximatePrice(UPDATED_TOTAL_APPROXIMATE_PRICE)
            .recommendationAt01(UPDATED_RECOMMENDATION_AT_01)
            .recommendationAt02(UPDATED_RECOMMENDATION_AT_02)
            .recommendationAt03(UPDATED_RECOMMENDATION_AT_03)
            .recommendationAt04(UPDATED_RECOMMENDATION_AT_04)
            .recommendationAt05(UPDATED_RECOMMENDATION_AT_05)
            .nextRecommendationOrder(UPDATED_NEXT_RECOMMENDATION_ORDER)
            .rejectedDate(UPDATED_REJECTED_DATE);

        restProcReqMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcReqMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcReqMaster))
            )
            .andExpect(status().isOk());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
        ProcReqMaster testProcReqMaster = procReqMasterList.get(procReqMasterList.size() - 1);
        assertThat(testProcReqMaster.getRequisitionNo()).isEqualTo(DEFAULT_REQUISITION_NO);
        assertThat(testProcReqMaster.getRequestedDate()).isEqualTo(DEFAULT_REQUESTED_DATE);
        assertThat(testProcReqMaster.getIsCTOApprovalRequired()).isEqualTo(DEFAULT_IS_CTO_APPROVAL_REQUIRED);
        assertThat(testProcReqMaster.getRequisitionStatus()).isEqualTo(UPDATED_REQUISITION_STATUS);
        assertThat(testProcReqMaster.getExpectedReceivedDate()).isEqualTo(DEFAULT_EXPECTED_RECEIVED_DATE);
        assertThat(testProcReqMaster.getReasoning()).isEqualTo(UPDATED_REASONING);
        assertThat(testProcReqMaster.getTotalApproximatePrice()).isEqualTo(UPDATED_TOTAL_APPROXIMATE_PRICE);
        assertThat(testProcReqMaster.getRecommendationAt01()).isEqualTo(UPDATED_RECOMMENDATION_AT_01);
        assertThat(testProcReqMaster.getRecommendationAt02()).isEqualTo(UPDATED_RECOMMENDATION_AT_02);
        assertThat(testProcReqMaster.getRecommendationAt03()).isEqualTo(UPDATED_RECOMMENDATION_AT_03);
        assertThat(testProcReqMaster.getRecommendationAt04()).isEqualTo(UPDATED_RECOMMENDATION_AT_04);
        assertThat(testProcReqMaster.getRecommendationAt05()).isEqualTo(UPDATED_RECOMMENDATION_AT_05);
        assertThat(testProcReqMaster.getNextRecommendationOrder()).isEqualTo(UPDATED_NEXT_RECOMMENDATION_ORDER);
        assertThat(testProcReqMaster.getRejectedDate()).isEqualTo(UPDATED_REJECTED_DATE);
        assertThat(testProcReqMaster.getRejectionReason()).isEqualTo(DEFAULT_REJECTION_REASON);
        assertThat(testProcReqMaster.getClosedAt()).isEqualTo(DEFAULT_CLOSED_AT);
        assertThat(testProcReqMaster.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProcReqMaster.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateProcReqMasterWithPatch() throws Exception {
        // Initialize the database
        procReqMasterRepository.saveAndFlush(procReqMaster);

        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();

        // Update the procReqMaster using partial update
        ProcReqMaster partialUpdatedProcReqMaster = new ProcReqMaster();
        partialUpdatedProcReqMaster.setId(procReqMaster.getId());

        partialUpdatedProcReqMaster
            .requisitionNo(UPDATED_REQUISITION_NO)
            .requestedDate(UPDATED_REQUESTED_DATE)
            .isCTOApprovalRequired(UPDATED_IS_CTO_APPROVAL_REQUIRED)
            .requisitionStatus(UPDATED_REQUISITION_STATUS)
            .expectedReceivedDate(UPDATED_EXPECTED_RECEIVED_DATE)
            .reasoning(UPDATED_REASONING)
            .totalApproximatePrice(UPDATED_TOTAL_APPROXIMATE_PRICE)
            .recommendationAt01(UPDATED_RECOMMENDATION_AT_01)
            .recommendationAt02(UPDATED_RECOMMENDATION_AT_02)
            .recommendationAt03(UPDATED_RECOMMENDATION_AT_03)
            .recommendationAt04(UPDATED_RECOMMENDATION_AT_04)
            .recommendationAt05(UPDATED_RECOMMENDATION_AT_05)
            .nextRecommendationOrder(UPDATED_NEXT_RECOMMENDATION_ORDER)
            .rejectedDate(UPDATED_REJECTED_DATE)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .closedAt(UPDATED_CLOSED_AT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restProcReqMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcReqMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcReqMaster))
            )
            .andExpect(status().isOk());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
        ProcReqMaster testProcReqMaster = procReqMasterList.get(procReqMasterList.size() - 1);
        assertThat(testProcReqMaster.getRequisitionNo()).isEqualTo(UPDATED_REQUISITION_NO);
        assertThat(testProcReqMaster.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testProcReqMaster.getIsCTOApprovalRequired()).isEqualTo(UPDATED_IS_CTO_APPROVAL_REQUIRED);
        assertThat(testProcReqMaster.getRequisitionStatus()).isEqualTo(UPDATED_REQUISITION_STATUS);
        assertThat(testProcReqMaster.getExpectedReceivedDate()).isEqualTo(UPDATED_EXPECTED_RECEIVED_DATE);
        assertThat(testProcReqMaster.getReasoning()).isEqualTo(UPDATED_REASONING);
        assertThat(testProcReqMaster.getTotalApproximatePrice()).isEqualTo(UPDATED_TOTAL_APPROXIMATE_PRICE);
        assertThat(testProcReqMaster.getRecommendationAt01()).isEqualTo(UPDATED_RECOMMENDATION_AT_01);
        assertThat(testProcReqMaster.getRecommendationAt02()).isEqualTo(UPDATED_RECOMMENDATION_AT_02);
        assertThat(testProcReqMaster.getRecommendationAt03()).isEqualTo(UPDATED_RECOMMENDATION_AT_03);
        assertThat(testProcReqMaster.getRecommendationAt04()).isEqualTo(UPDATED_RECOMMENDATION_AT_04);
        assertThat(testProcReqMaster.getRecommendationAt05()).isEqualTo(UPDATED_RECOMMENDATION_AT_05);
        assertThat(testProcReqMaster.getNextRecommendationOrder()).isEqualTo(UPDATED_NEXT_RECOMMENDATION_ORDER);
        assertThat(testProcReqMaster.getRejectedDate()).isEqualTo(UPDATED_REJECTED_DATE);
        assertThat(testProcReqMaster.getRejectionReason()).isEqualTo(UPDATED_REJECTION_REASON);
        assertThat(testProcReqMaster.getClosedAt()).isEqualTo(UPDATED_CLOSED_AT);
        assertThat(testProcReqMaster.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProcReqMaster.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingProcReqMaster() throws Exception {
        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();
        procReqMaster.setId(count.incrementAndGet());

        // Create the ProcReqMaster
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcReqMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, procReqMasterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcReqMaster() throws Exception {
        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();
        procReqMaster.setId(count.incrementAndGet());

        // Create the ProcReqMaster
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcReqMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcReqMaster() throws Exception {
        int databaseSizeBeforeUpdate = procReqMasterRepository.findAll().size();
        procReqMaster.setId(count.incrementAndGet());

        // Create the ProcReqMaster
        ProcReqMasterDTO procReqMasterDTO = procReqMasterMapper.toDto(procReqMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcReqMasterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(procReqMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcReqMaster in the database
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcReqMaster() throws Exception {
        // Initialize the database
        procReqMasterRepository.saveAndFlush(procReqMaster);

        int databaseSizeBeforeDelete = procReqMasterRepository.findAll().size();

        // Delete the procReqMaster
        restProcReqMasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, procReqMaster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcReqMaster> procReqMasterList = procReqMasterRepository.findAll();
        assertThat(procReqMasterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
