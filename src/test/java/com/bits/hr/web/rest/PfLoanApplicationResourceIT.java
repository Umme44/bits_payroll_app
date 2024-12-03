package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.PfLoanApplication;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.PfLoanApplicationRepository;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import com.bits.hr.service.mapper.PfLoanApplicationMapper;
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
 * Integration tests for the {@link PfLoanApplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PfLoanApplicationResourceIT {

    private static final Double DEFAULT_INSTALLMENT_AMOUNT = 1D;
    private static final Double UPDATED_INSTALLMENT_AMOUNT = 2D;

    private static final Integer DEFAULT_NO_OF_INSTALLMENT = 1;
    private static final Integer UPDATED_NO_OF_INSTALLMENT = 2;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_RECOMMENDED = false;
    private static final Boolean UPDATED_IS_RECOMMENDED = true;

    private static final LocalDate DEFAULT_RECOMMENDATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECOMMENDATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_APPROVED = false;
    private static final Boolean UPDATED_IS_APPROVED = true;

    private static final LocalDate DEFAULT_APPROVAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPROVAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_REJECTED = false;
    private static final Boolean UPDATED_IS_REJECTED = true;

    private static final String DEFAULT_REJECTION_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REJECTION_REASON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REJECTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REJECTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DISBURSEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DISBURSEMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_DISBURSEMENT_AMOUNT = 1D;
    private static final Double UPDATED_DISBURSEMENT_AMOUNT = 2D;

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.APPROVED;

    private static final String ENTITY_API_URL = "/api/pf-loan-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PfLoanApplicationRepository pfLoanApplicationRepository;

    @Autowired
    private PfLoanApplicationMapper pfLoanApplicationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPfLoanApplicationMockMvc;

    private PfLoanApplication pfLoanApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfLoanApplication createEntity(EntityManager em) {
        PfLoanApplication pfLoanApplication = new PfLoanApplication()
            .installmentAmount(DEFAULT_INSTALLMENT_AMOUNT)
            .noOfInstallment(DEFAULT_NO_OF_INSTALLMENT)
            .remarks(DEFAULT_REMARKS)
            .isRecommended(DEFAULT_IS_RECOMMENDED)
            .recommendationDate(DEFAULT_RECOMMENDATION_DATE)
            .isApproved(DEFAULT_IS_APPROVED)
            .approvalDate(DEFAULT_APPROVAL_DATE)
            .isRejected(DEFAULT_IS_REJECTED)
            .rejectionReason(DEFAULT_REJECTION_REASON)
            .rejectionDate(DEFAULT_REJECTION_DATE)
            .disbursementDate(DEFAULT_DISBURSEMENT_DATE)
            .disbursementAmount(DEFAULT_DISBURSEMENT_AMOUNT)
            .status(DEFAULT_STATUS);
        return pfLoanApplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfLoanApplication createUpdatedEntity(EntityManager em) {
        PfLoanApplication pfLoanApplication = new PfLoanApplication()
            .installmentAmount(UPDATED_INSTALLMENT_AMOUNT)
            .noOfInstallment(UPDATED_NO_OF_INSTALLMENT)
            .remarks(UPDATED_REMARKS)
            .isRecommended(UPDATED_IS_RECOMMENDED)
            .recommendationDate(UPDATED_RECOMMENDATION_DATE)
            .isApproved(UPDATED_IS_APPROVED)
            .approvalDate(UPDATED_APPROVAL_DATE)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .rejectionDate(UPDATED_REJECTION_DATE)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .status(UPDATED_STATUS);
        return pfLoanApplication;
    }

    @BeforeEach
    public void initTest() {
        pfLoanApplication = createEntity(em);
    }

    @Test
    @Transactional
    void createPfLoanApplication() throws Exception {
        int databaseSizeBeforeCreate = pfLoanApplicationRepository.findAll().size();
        // Create the PfLoanApplication
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(pfLoanApplication);
        restPfLoanApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeCreate + 1);
        PfLoanApplication testPfLoanApplication = pfLoanApplicationList.get(pfLoanApplicationList.size() - 1);
        assertThat(testPfLoanApplication.getInstallmentAmount()).isEqualTo(DEFAULT_INSTALLMENT_AMOUNT);
        assertThat(testPfLoanApplication.getNoOfInstallment()).isEqualTo(DEFAULT_NO_OF_INSTALLMENT);
        assertThat(testPfLoanApplication.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testPfLoanApplication.getIsRecommended()).isEqualTo(DEFAULT_IS_RECOMMENDED);
        assertThat(testPfLoanApplication.getRecommendationDate()).isEqualTo(DEFAULT_RECOMMENDATION_DATE);
        assertThat(testPfLoanApplication.getIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
        assertThat(testPfLoanApplication.getApprovalDate()).isEqualTo(DEFAULT_APPROVAL_DATE);
        assertThat(testPfLoanApplication.getIsRejected()).isEqualTo(DEFAULT_IS_REJECTED);
        assertThat(testPfLoanApplication.getRejectionReason()).isEqualTo(DEFAULT_REJECTION_REASON);
        assertThat(testPfLoanApplication.getRejectionDate()).isEqualTo(DEFAULT_REJECTION_DATE);
        assertThat(testPfLoanApplication.getDisbursementDate()).isEqualTo(DEFAULT_DISBURSEMENT_DATE);
        assertThat(testPfLoanApplication.getDisbursementAmount()).isEqualTo(DEFAULT_DISBURSEMENT_AMOUNT);
        assertThat(testPfLoanApplication.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createPfLoanApplicationWithExistingId() throws Exception {
        // Create the PfLoanApplication with an existing ID
        pfLoanApplication.setId(1L);
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(pfLoanApplication);

        int databaseSizeBeforeCreate = pfLoanApplicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPfLoanApplicationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPfLoanApplications() throws Exception {
        // Initialize the database
        pfLoanApplicationRepository.saveAndFlush(pfLoanApplication);

        // Get all the pfLoanApplicationList
        restPfLoanApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pfLoanApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].installmentAmount").value(hasItem(DEFAULT_INSTALLMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].noOfInstallment").value(hasItem(DEFAULT_NO_OF_INSTALLMENT)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].isRecommended").value(hasItem(DEFAULT_IS_RECOMMENDED.booleanValue())))
            .andExpect(jsonPath("$.[*].recommendationDate").value(hasItem(DEFAULT_RECOMMENDATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].approvalDate").value(hasItem(DEFAULT_APPROVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].isRejected").value(hasItem(DEFAULT_IS_REJECTED.booleanValue())))
            .andExpect(jsonPath("$.[*].rejectionReason").value(hasItem(DEFAULT_REJECTION_REASON)))
            .andExpect(jsonPath("$.[*].rejectionDate").value(hasItem(DEFAULT_REJECTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].disbursementDate").value(hasItem(DEFAULT_DISBURSEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].disbursementAmount").value(hasItem(DEFAULT_DISBURSEMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPfLoanApplication() throws Exception {
        // Initialize the database
        pfLoanApplicationRepository.saveAndFlush(pfLoanApplication);

        // Get the pfLoanApplication
        restPfLoanApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, pfLoanApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pfLoanApplication.getId().intValue()))
            .andExpect(jsonPath("$.installmentAmount").value(DEFAULT_INSTALLMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.noOfInstallment").value(DEFAULT_NO_OF_INSTALLMENT))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.isRecommended").value(DEFAULT_IS_RECOMMENDED.booleanValue()))
            .andExpect(jsonPath("$.recommendationDate").value(DEFAULT_RECOMMENDATION_DATE.toString()))
            .andExpect(jsonPath("$.isApproved").value(DEFAULT_IS_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.approvalDate").value(DEFAULT_APPROVAL_DATE.toString()))
            .andExpect(jsonPath("$.isRejected").value(DEFAULT_IS_REJECTED.booleanValue()))
            .andExpect(jsonPath("$.rejectionReason").value(DEFAULT_REJECTION_REASON))
            .andExpect(jsonPath("$.rejectionDate").value(DEFAULT_REJECTION_DATE.toString()))
            .andExpect(jsonPath("$.disbursementDate").value(DEFAULT_DISBURSEMENT_DATE.toString()))
            .andExpect(jsonPath("$.disbursementAmount").value(DEFAULT_DISBURSEMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPfLoanApplication() throws Exception {
        // Get the pfLoanApplication
        restPfLoanApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPfLoanApplication() throws Exception {
        // Initialize the database
        pfLoanApplicationRepository.saveAndFlush(pfLoanApplication);

        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();

        // Update the pfLoanApplication
        PfLoanApplication updatedPfLoanApplication = pfLoanApplicationRepository.findById(pfLoanApplication.getId()).get();
        // Disconnect from session so that the updates on updatedPfLoanApplication are not directly saved in db
        em.detach(updatedPfLoanApplication);
        updatedPfLoanApplication
            .installmentAmount(UPDATED_INSTALLMENT_AMOUNT)
            .noOfInstallment(UPDATED_NO_OF_INSTALLMENT)
            .remarks(UPDATED_REMARKS)
            .isRecommended(UPDATED_IS_RECOMMENDED)
            .recommendationDate(UPDATED_RECOMMENDATION_DATE)
            .isApproved(UPDATED_IS_APPROVED)
            .approvalDate(UPDATED_APPROVAL_DATE)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .rejectionDate(UPDATED_REJECTION_DATE)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .status(UPDATED_STATUS);
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(updatedPfLoanApplication);

        restPfLoanApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfLoanApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
        PfLoanApplication testPfLoanApplication = pfLoanApplicationList.get(pfLoanApplicationList.size() - 1);
        assertThat(testPfLoanApplication.getInstallmentAmount()).isEqualTo(UPDATED_INSTALLMENT_AMOUNT);
        assertThat(testPfLoanApplication.getNoOfInstallment()).isEqualTo(UPDATED_NO_OF_INSTALLMENT);
        assertThat(testPfLoanApplication.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testPfLoanApplication.getIsRecommended()).isEqualTo(UPDATED_IS_RECOMMENDED);
        assertThat(testPfLoanApplication.getRecommendationDate()).isEqualTo(UPDATED_RECOMMENDATION_DATE);
        assertThat(testPfLoanApplication.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testPfLoanApplication.getApprovalDate()).isEqualTo(UPDATED_APPROVAL_DATE);
        assertThat(testPfLoanApplication.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testPfLoanApplication.getRejectionReason()).isEqualTo(UPDATED_REJECTION_REASON);
        assertThat(testPfLoanApplication.getRejectionDate()).isEqualTo(UPDATED_REJECTION_DATE);
        assertThat(testPfLoanApplication.getDisbursementDate()).isEqualTo(UPDATED_DISBURSEMENT_DATE);
        assertThat(testPfLoanApplication.getDisbursementAmount()).isEqualTo(UPDATED_DISBURSEMENT_AMOUNT);
        assertThat(testPfLoanApplication.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPfLoanApplication() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();
        pfLoanApplication.setId(count.incrementAndGet());

        // Create the PfLoanApplication
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(pfLoanApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfLoanApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfLoanApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPfLoanApplication() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();
        pfLoanApplication.setId(count.incrementAndGet());

        // Create the PfLoanApplication
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(pfLoanApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPfLoanApplication() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();
        pfLoanApplication.setId(count.incrementAndGet());

        // Create the PfLoanApplication
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(pfLoanApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanApplicationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePfLoanApplicationWithPatch() throws Exception {
        // Initialize the database
        pfLoanApplicationRepository.saveAndFlush(pfLoanApplication);

        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();

        // Update the pfLoanApplication using partial update
        PfLoanApplication partialUpdatedPfLoanApplication = new PfLoanApplication();
        partialUpdatedPfLoanApplication.setId(pfLoanApplication.getId());

        partialUpdatedPfLoanApplication
            .noOfInstallment(UPDATED_NO_OF_INSTALLMENT)
            .remarks(UPDATED_REMARKS)
            .isRecommended(UPDATED_IS_RECOMMENDED)
            .recommendationDate(UPDATED_RECOMMENDATION_DATE)
            .isApproved(UPDATED_IS_APPROVED)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .rejectionDate(UPDATED_REJECTION_DATE)
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT);

        restPfLoanApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfLoanApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfLoanApplication))
            )
            .andExpect(status().isOk());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
        PfLoanApplication testPfLoanApplication = pfLoanApplicationList.get(pfLoanApplicationList.size() - 1);
        assertThat(testPfLoanApplication.getInstallmentAmount()).isEqualTo(DEFAULT_INSTALLMENT_AMOUNT);
        assertThat(testPfLoanApplication.getNoOfInstallment()).isEqualTo(UPDATED_NO_OF_INSTALLMENT);
        assertThat(testPfLoanApplication.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testPfLoanApplication.getIsRecommended()).isEqualTo(UPDATED_IS_RECOMMENDED);
        assertThat(testPfLoanApplication.getRecommendationDate()).isEqualTo(UPDATED_RECOMMENDATION_DATE);
        assertThat(testPfLoanApplication.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testPfLoanApplication.getApprovalDate()).isEqualTo(DEFAULT_APPROVAL_DATE);
        assertThat(testPfLoanApplication.getIsRejected()).isEqualTo(DEFAULT_IS_REJECTED);
        assertThat(testPfLoanApplication.getRejectionReason()).isEqualTo(UPDATED_REJECTION_REASON);
        assertThat(testPfLoanApplication.getRejectionDate()).isEqualTo(UPDATED_REJECTION_DATE);
        assertThat(testPfLoanApplication.getDisbursementDate()).isEqualTo(DEFAULT_DISBURSEMENT_DATE);
        assertThat(testPfLoanApplication.getDisbursementAmount()).isEqualTo(UPDATED_DISBURSEMENT_AMOUNT);
        assertThat(testPfLoanApplication.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePfLoanApplicationWithPatch() throws Exception {
        // Initialize the database
        pfLoanApplicationRepository.saveAndFlush(pfLoanApplication);

        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();

        // Update the pfLoanApplication using partial update
        PfLoanApplication partialUpdatedPfLoanApplication = new PfLoanApplication();
        partialUpdatedPfLoanApplication.setId(pfLoanApplication.getId());

        partialUpdatedPfLoanApplication
            .installmentAmount(UPDATED_INSTALLMENT_AMOUNT)
            .noOfInstallment(UPDATED_NO_OF_INSTALLMENT)
            .remarks(UPDATED_REMARKS)
            .isRecommended(UPDATED_IS_RECOMMENDED)
            .recommendationDate(UPDATED_RECOMMENDATION_DATE)
            .isApproved(UPDATED_IS_APPROVED)
            .approvalDate(UPDATED_APPROVAL_DATE)
            .isRejected(UPDATED_IS_REJECTED)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .rejectionDate(UPDATED_REJECTION_DATE)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .status(UPDATED_STATUS);

        restPfLoanApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfLoanApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfLoanApplication))
            )
            .andExpect(status().isOk());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
        PfLoanApplication testPfLoanApplication = pfLoanApplicationList.get(pfLoanApplicationList.size() - 1);
        assertThat(testPfLoanApplication.getInstallmentAmount()).isEqualTo(UPDATED_INSTALLMENT_AMOUNT);
        assertThat(testPfLoanApplication.getNoOfInstallment()).isEqualTo(UPDATED_NO_OF_INSTALLMENT);
        assertThat(testPfLoanApplication.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testPfLoanApplication.getIsRecommended()).isEqualTo(UPDATED_IS_RECOMMENDED);
        assertThat(testPfLoanApplication.getRecommendationDate()).isEqualTo(UPDATED_RECOMMENDATION_DATE);
        assertThat(testPfLoanApplication.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testPfLoanApplication.getApprovalDate()).isEqualTo(UPDATED_APPROVAL_DATE);
        assertThat(testPfLoanApplication.getIsRejected()).isEqualTo(UPDATED_IS_REJECTED);
        assertThat(testPfLoanApplication.getRejectionReason()).isEqualTo(UPDATED_REJECTION_REASON);
        assertThat(testPfLoanApplication.getRejectionDate()).isEqualTo(UPDATED_REJECTION_DATE);
        assertThat(testPfLoanApplication.getDisbursementDate()).isEqualTo(UPDATED_DISBURSEMENT_DATE);
        assertThat(testPfLoanApplication.getDisbursementAmount()).isEqualTo(UPDATED_DISBURSEMENT_AMOUNT);
        assertThat(testPfLoanApplication.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPfLoanApplication() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();
        pfLoanApplication.setId(count.incrementAndGet());

        // Create the PfLoanApplication
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(pfLoanApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfLoanApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pfLoanApplicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPfLoanApplication() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();
        pfLoanApplication.setId(count.incrementAndGet());

        // Create the PfLoanApplication
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(pfLoanApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPfLoanApplication() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanApplicationRepository.findAll().size();
        pfLoanApplication.setId(count.incrementAndGet());

        // Create the PfLoanApplication
        PfLoanApplicationDTO pfLoanApplicationDTO = pfLoanApplicationMapper.toDto(pfLoanApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfLoanApplication in the database
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePfLoanApplication() throws Exception {
        // Initialize the database
        pfLoanApplicationRepository.saveAndFlush(pfLoanApplication);

        int databaseSizeBeforeDelete = pfLoanApplicationRepository.findAll().size();

        // Delete the pfLoanApplication
        restPfLoanApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, pfLoanApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PfLoanApplication> pfLoanApplicationList = pfLoanApplicationRepository.findAll();
        assertThat(pfLoanApplicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
