package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.ArrearPayment;
import com.bits.hr.domain.ArrearSalaryItem;
import com.bits.hr.domain.enumeration.ArrearPaymentType;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.ArrearPaymentRepository;
import com.bits.hr.service.dto.ArrearPaymentDTO;
import com.bits.hr.service.mapper.ArrearPaymentMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link ArrearPaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArrearPaymentResourceIT {

    private static final ArrearPaymentType DEFAULT_PAYMENT_TYPE = ArrearPaymentType.INDIVIDUAL;
    private static final ArrearPaymentType UPDATED_PAYMENT_TYPE = ArrearPaymentType.ALONGSIDE_MONTHLY_SALARY;

    private static final LocalDate DEFAULT_DISBURSEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DISBURSEMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Month DEFAULT_SALARY_MONTH = Month.JANUARY;
    private static final Month UPDATED_SALARY_MONTH = Month.FEBRUARY;

    private static final Integer DEFAULT_SALARY_YEAR = 1900;
    private static final Integer UPDATED_SALARY_YEAR = 1901;

    private static final Status DEFAULT_APPROVAL_STATUS = Status.PENDING;
    private static final Status UPDATED_APPROVAL_STATUS = Status.APPROVED;

    private static final Double DEFAULT_DISBURSEMENT_AMOUNT = 1D;
    private static final Double UPDATED_DISBURSEMENT_AMOUNT = 2D;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Double DEFAULT_ARREAR_PF = 0D;
    private static final Double UPDATED_ARREAR_PF = 1D;

    private static final Double DEFAULT_TAX_DEDUCTION = 0D;
    private static final Double UPDATED_TAX_DEDUCTION = 1D;

    private static final Boolean DEFAULT_DEDUCT_TAX_UPON_PAYMENT = false;
    private static final Boolean UPDATED_DEDUCT_TAX_UPON_PAYMENT = true;

    private static final String ENTITY_API_URL = "/api/arrear-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArrearPaymentRepository arrearPaymentRepository;

    @Autowired
    private ArrearPaymentMapper arrearPaymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArrearPaymentMockMvc;

    private ArrearPayment arrearPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArrearPayment createEntity(EntityManager em) {
        ArrearPayment arrearPayment = new ArrearPayment()
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .disbursementDate(DEFAULT_DISBURSEMENT_DATE)
            .salaryMonth(DEFAULT_SALARY_MONTH)
            .salaryYear(DEFAULT_SALARY_YEAR)
            .approvalStatus(DEFAULT_APPROVAL_STATUS)
            .disbursementAmount(DEFAULT_DISBURSEMENT_AMOUNT)
            .isDeleted(DEFAULT_IS_DELETED)
            .arrearPF(DEFAULT_ARREAR_PF)
            .taxDeduction(DEFAULT_TAX_DEDUCTION)
            .deductTaxUponPayment(DEFAULT_DEDUCT_TAX_UPON_PAYMENT);
        // Add required entity
        ArrearSalaryItem arrearSalaryItem;
        if (TestUtil.findAll(em, ArrearSalaryItem.class).isEmpty()) {
            arrearSalaryItem = ArrearSalaryItemResourceIT.createEntity(em);
            em.persist(arrearSalaryItem);
            em.flush();
        } else {
            arrearSalaryItem = TestUtil.findAll(em, ArrearSalaryItem.class).get(0);
        }
        arrearPayment.setArrearSalaryItem(arrearSalaryItem);
        return arrearPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArrearPayment createUpdatedEntity(EntityManager em) {
        ArrearPayment arrearPayment = new ArrearPayment()
            .paymentType(UPDATED_PAYMENT_TYPE)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .salaryMonth(UPDATED_SALARY_MONTH)
            .salaryYear(UPDATED_SALARY_YEAR)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .isDeleted(UPDATED_IS_DELETED)
            .arrearPF(UPDATED_ARREAR_PF)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .deductTaxUponPayment(UPDATED_DEDUCT_TAX_UPON_PAYMENT);
        // Add required entity
        ArrearSalaryItem arrearSalaryItem;
        if (TestUtil.findAll(em, ArrearSalaryItem.class).isEmpty()) {
            arrearSalaryItem = ArrearSalaryItemResourceIT.createUpdatedEntity(em);
            em.persist(arrearSalaryItem);
            em.flush();
        } else {
            arrearSalaryItem = TestUtil.findAll(em, ArrearSalaryItem.class).get(0);
        }
        arrearPayment.setArrearSalaryItem(arrearSalaryItem);
        return arrearPayment;
    }

    @BeforeEach
    public void initTest() {
        arrearPayment = createEntity(em);
    }

    @Test
    @Transactional
    void createArrearPayment() throws Exception {
        int databaseSizeBeforeCreate = arrearPaymentRepository.findAll().size();
        // Create the ArrearPayment
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);
        restArrearPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        ArrearPayment testArrearPayment = arrearPaymentList.get(arrearPaymentList.size() - 1);
        assertThat(testArrearPayment.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testArrearPayment.getDisbursementDate()).isEqualTo(DEFAULT_DISBURSEMENT_DATE);
        assertThat(testArrearPayment.getSalaryMonth()).isEqualTo(DEFAULT_SALARY_MONTH);
        assertThat(testArrearPayment.getSalaryYear()).isEqualTo(DEFAULT_SALARY_YEAR);
        assertThat(testArrearPayment.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testArrearPayment.getDisbursementAmount()).isEqualTo(DEFAULT_DISBURSEMENT_AMOUNT);
        assertThat(testArrearPayment.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testArrearPayment.getArrearPF()).isEqualTo(DEFAULT_ARREAR_PF);
        assertThat(testArrearPayment.getTaxDeduction()).isEqualTo(DEFAULT_TAX_DEDUCTION);
        assertThat(testArrearPayment.getDeductTaxUponPayment()).isEqualTo(DEFAULT_DEDUCT_TAX_UPON_PAYMENT);
    }

    @Test
    @Transactional
    void createArrearPaymentWithExistingId() throws Exception {
        // Create the ArrearPayment with an existing ID
        arrearPayment.setId(1L);
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        int databaseSizeBeforeCreate = arrearPaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArrearPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPaymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearPaymentRepository.findAll().size();
        // set the field null
        arrearPayment.setPaymentType(null);

        // Create the ArrearPayment, which fails.
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        restArrearPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDisbursementAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearPaymentRepository.findAll().size();
        // set the field null
        arrearPayment.setDisbursementAmount(null);

        // Create the ArrearPayment, which fails.
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        restArrearPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearPaymentRepository.findAll().size();
        // set the field null
        arrearPayment.setIsDeleted(null);

        // Create the ArrearPayment, which fails.
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        restArrearPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArrearPFIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearPaymentRepository.findAll().size();
        // set the field null
        arrearPayment.setArrearPF(null);

        // Create the ArrearPayment, which fails.
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        restArrearPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxDeductionIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearPaymentRepository.findAll().size();
        // set the field null
        arrearPayment.setTaxDeduction(null);

        // Create the ArrearPayment, which fails.
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        restArrearPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeductTaxUponPaymentIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearPaymentRepository.findAll().size();
        // set the field null
        arrearPayment.setDeductTaxUponPayment(null);

        // Create the ArrearPayment, which fails.
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        restArrearPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArrearPayments() throws Exception {
        // Initialize the database
        arrearPaymentRepository.saveAndFlush(arrearPayment);

        // Get all the arrearPaymentList
        restArrearPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arrearPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].disbursementDate").value(hasItem(DEFAULT_DISBURSEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].salaryMonth").value(hasItem(DEFAULT_SALARY_MONTH.toString())))
            .andExpect(jsonPath("$.[*].salaryYear").value(hasItem(DEFAULT_SALARY_YEAR)))
            .andExpect(jsonPath("$.[*].approvalStatus").value(hasItem(DEFAULT_APPROVAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].disbursementAmount").value(hasItem(DEFAULT_DISBURSEMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].arrearPF").value(hasItem(DEFAULT_ARREAR_PF.doubleValue())))
            .andExpect(jsonPath("$.[*].taxDeduction").value(hasItem(DEFAULT_TAX_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].deductTaxUponPayment").value(hasItem(DEFAULT_DEDUCT_TAX_UPON_PAYMENT.booleanValue())));
    }

    @Test
    @Transactional
    void getArrearPayment() throws Exception {
        // Initialize the database
        arrearPaymentRepository.saveAndFlush(arrearPayment);

        // Get the arrearPayment
        restArrearPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, arrearPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arrearPayment.getId().intValue()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.disbursementDate").value(DEFAULT_DISBURSEMENT_DATE.toString()))
            .andExpect(jsonPath("$.salaryMonth").value(DEFAULT_SALARY_MONTH.toString()))
            .andExpect(jsonPath("$.salaryYear").value(DEFAULT_SALARY_YEAR))
            .andExpect(jsonPath("$.approvalStatus").value(DEFAULT_APPROVAL_STATUS.toString()))
            .andExpect(jsonPath("$.disbursementAmount").value(DEFAULT_DISBURSEMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.arrearPF").value(DEFAULT_ARREAR_PF.doubleValue()))
            .andExpect(jsonPath("$.taxDeduction").value(DEFAULT_TAX_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.deductTaxUponPayment").value(DEFAULT_DEDUCT_TAX_UPON_PAYMENT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingArrearPayment() throws Exception {
        // Get the arrearPayment
        restArrearPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArrearPayment() throws Exception {
        // Initialize the database
        arrearPaymentRepository.saveAndFlush(arrearPayment);

        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();

        // Update the arrearPayment
        ArrearPayment updatedArrearPayment = arrearPaymentRepository.findById(arrearPayment.getId()).get();
        // Disconnect from session so that the updates on updatedArrearPayment are not directly saved in db
        em.detach(updatedArrearPayment);
        updatedArrearPayment
            .paymentType(UPDATED_PAYMENT_TYPE)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .salaryMonth(UPDATED_SALARY_MONTH)
            .salaryYear(UPDATED_SALARY_YEAR)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .isDeleted(UPDATED_IS_DELETED)
            .arrearPF(UPDATED_ARREAR_PF)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .deductTaxUponPayment(UPDATED_DEDUCT_TAX_UPON_PAYMENT);
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(updatedArrearPayment);

        restArrearPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arrearPaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
        ArrearPayment testArrearPayment = arrearPaymentList.get(arrearPaymentList.size() - 1);
        assertThat(testArrearPayment.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testArrearPayment.getDisbursementDate()).isEqualTo(UPDATED_DISBURSEMENT_DATE);
        assertThat(testArrearPayment.getSalaryMonth()).isEqualTo(UPDATED_SALARY_MONTH);
        assertThat(testArrearPayment.getSalaryYear()).isEqualTo(UPDATED_SALARY_YEAR);
        assertThat(testArrearPayment.getApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testArrearPayment.getDisbursementAmount()).isEqualTo(UPDATED_DISBURSEMENT_AMOUNT);
        assertThat(testArrearPayment.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testArrearPayment.getArrearPF()).isEqualTo(UPDATED_ARREAR_PF);
        assertThat(testArrearPayment.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testArrearPayment.getDeductTaxUponPayment()).isEqualTo(UPDATED_DEDUCT_TAX_UPON_PAYMENT);
    }

    @Test
    @Transactional
    void putNonExistingArrearPayment() throws Exception {
        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();
        arrearPayment.setId(count.incrementAndGet());

        // Create the ArrearPayment
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArrearPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arrearPaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArrearPayment() throws Exception {
        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();
        arrearPayment.setId(count.incrementAndGet());

        // Create the ArrearPayment
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArrearPayment() throws Exception {
        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();
        arrearPayment.setId(count.incrementAndGet());

        // Create the ArrearPayment
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearPaymentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArrearPaymentWithPatch() throws Exception {
        // Initialize the database
        arrearPaymentRepository.saveAndFlush(arrearPayment);

        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();

        // Update the arrearPayment using partial update
        ArrearPayment partialUpdatedArrearPayment = new ArrearPayment();
        partialUpdatedArrearPayment.setId(arrearPayment.getId());

        partialUpdatedArrearPayment
            .paymentType(UPDATED_PAYMENT_TYPE)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .salaryMonth(UPDATED_SALARY_MONTH)
            .salaryYear(UPDATED_SALARY_YEAR)
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .arrearPF(UPDATED_ARREAR_PF)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .deductTaxUponPayment(UPDATED_DEDUCT_TAX_UPON_PAYMENT);

        restArrearPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArrearPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArrearPayment))
            )
            .andExpect(status().isOk());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
        ArrearPayment testArrearPayment = arrearPaymentList.get(arrearPaymentList.size() - 1);
        assertThat(testArrearPayment.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testArrearPayment.getDisbursementDate()).isEqualTo(UPDATED_DISBURSEMENT_DATE);
        assertThat(testArrearPayment.getSalaryMonth()).isEqualTo(UPDATED_SALARY_MONTH);
        assertThat(testArrearPayment.getSalaryYear()).isEqualTo(UPDATED_SALARY_YEAR);
        assertThat(testArrearPayment.getApprovalStatus()).isEqualTo(DEFAULT_APPROVAL_STATUS);
        assertThat(testArrearPayment.getDisbursementAmount()).isEqualTo(UPDATED_DISBURSEMENT_AMOUNT);
        assertThat(testArrearPayment.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testArrearPayment.getArrearPF()).isEqualTo(UPDATED_ARREAR_PF);
        assertThat(testArrearPayment.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testArrearPayment.getDeductTaxUponPayment()).isEqualTo(UPDATED_DEDUCT_TAX_UPON_PAYMENT);
    }

    @Test
    @Transactional
    void fullUpdateArrearPaymentWithPatch() throws Exception {
        // Initialize the database
        arrearPaymentRepository.saveAndFlush(arrearPayment);

        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();

        // Update the arrearPayment using partial update
        ArrearPayment partialUpdatedArrearPayment = new ArrearPayment();
        partialUpdatedArrearPayment.setId(arrearPayment.getId());

        partialUpdatedArrearPayment
            .paymentType(UPDATED_PAYMENT_TYPE)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .salaryMonth(UPDATED_SALARY_MONTH)
            .salaryYear(UPDATED_SALARY_YEAR)
            .approvalStatus(UPDATED_APPROVAL_STATUS)
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .isDeleted(UPDATED_IS_DELETED)
            .arrearPF(UPDATED_ARREAR_PF)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .deductTaxUponPayment(UPDATED_DEDUCT_TAX_UPON_PAYMENT);

        restArrearPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArrearPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArrearPayment))
            )
            .andExpect(status().isOk());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
        ArrearPayment testArrearPayment = arrearPaymentList.get(arrearPaymentList.size() - 1);
        assertThat(testArrearPayment.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testArrearPayment.getDisbursementDate()).isEqualTo(UPDATED_DISBURSEMENT_DATE);
        assertThat(testArrearPayment.getSalaryMonth()).isEqualTo(UPDATED_SALARY_MONTH);
        assertThat(testArrearPayment.getSalaryYear()).isEqualTo(UPDATED_SALARY_YEAR);
        assertThat(testArrearPayment.getApprovalStatus()).isEqualTo(UPDATED_APPROVAL_STATUS);
        assertThat(testArrearPayment.getDisbursementAmount()).isEqualTo(UPDATED_DISBURSEMENT_AMOUNT);
        assertThat(testArrearPayment.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testArrearPayment.getArrearPF()).isEqualTo(UPDATED_ARREAR_PF);
        assertThat(testArrearPayment.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testArrearPayment.getDeductTaxUponPayment()).isEqualTo(UPDATED_DEDUCT_TAX_UPON_PAYMENT);
    }

    @Test
    @Transactional
    void patchNonExistingArrearPayment() throws Exception {
        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();
        arrearPayment.setId(count.incrementAndGet());

        // Create the ArrearPayment
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArrearPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arrearPaymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArrearPayment() throws Exception {
        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();
        arrearPayment.setId(count.incrementAndGet());

        // Create the ArrearPayment
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArrearPayment() throws Exception {
        int databaseSizeBeforeUpdate = arrearPaymentRepository.findAll().size();
        arrearPayment.setId(count.incrementAndGet());

        // Create the ArrearPayment
        ArrearPaymentDTO arrearPaymentDTO = arrearPaymentMapper.toDto(arrearPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearPaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArrearPayment in the database
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArrearPayment() throws Exception {
        // Initialize the database
        arrearPaymentRepository.saveAndFlush(arrearPayment);

        int databaseSizeBeforeDelete = arrearPaymentRepository.findAll().size();

        // Delete the arrearPayment
        restArrearPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, arrearPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArrearPayment> arrearPaymentList = arrearPaymentRepository.findAll();
        assertThat(arrearPaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
