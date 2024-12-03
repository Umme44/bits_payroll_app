package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.PfLoan;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import com.bits.hr.repository.PfLoanRepository;
import com.bits.hr.service.dto.PfLoanDTO;
import com.bits.hr.service.mapper.PfLoanMapper;
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
 * Integration tests for the {@link PfLoanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PfLoanResourceIT {

    private static final Double DEFAULT_DISBURSEMENT_AMOUNT = 1D;
    private static final Double UPDATED_DISBURSEMENT_AMOUNT = 2D;

    private static final LocalDate DEFAULT_DISBURSEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DISBURSEMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_BANK_BRANCH = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CHEQUE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CHEQUE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_INSTALMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INSTALMENT_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_INSTALLMENT_AMOUNT = 1D;
    private static final Double UPDATED_INSTALLMENT_AMOUNT = 2D;

    private static final LocalDate DEFAULT_INSTALMENT_START_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INSTALMENT_START_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final PfLoanStatus DEFAULT_STATUS = PfLoanStatus.OPEN_REPAYING;
    private static final PfLoanStatus UPDATED_STATUS = PfLoanStatus.PAID_OFF;

    private static final String ENTITY_API_URL = "/api/pf-loans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PfLoanRepository pfLoanRepository;

    @Autowired
    private PfLoanMapper pfLoanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPfLoanMockMvc;

    private PfLoan pfLoan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfLoan createEntity(EntityManager em) {
        PfLoan pfLoan = new PfLoan()
            .disbursementAmount(DEFAULT_DISBURSEMENT_AMOUNT)
            .disbursementDate(DEFAULT_DISBURSEMENT_DATE)
            .bankName(DEFAULT_BANK_NAME)
            .bankBranch(DEFAULT_BANK_BRANCH)
            .bankAccountNumber(DEFAULT_BANK_ACCOUNT_NUMBER)
            .chequeNumber(DEFAULT_CHEQUE_NUMBER)
            .instalmentNumber(DEFAULT_INSTALMENT_NUMBER)
            .installmentAmount(DEFAULT_INSTALLMENT_AMOUNT)
            .instalmentStartFrom(DEFAULT_INSTALMENT_START_FROM)
            .status(DEFAULT_STATUS);
        return pfLoan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfLoan createUpdatedEntity(EntityManager em) {
        PfLoan pfLoan = new PfLoan()
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .bankName(UPDATED_BANK_NAME)
            .bankBranch(UPDATED_BANK_BRANCH)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .chequeNumber(UPDATED_CHEQUE_NUMBER)
            .instalmentNumber(UPDATED_INSTALMENT_NUMBER)
            .installmentAmount(UPDATED_INSTALLMENT_AMOUNT)
            .instalmentStartFrom(UPDATED_INSTALMENT_START_FROM)
            .status(UPDATED_STATUS);
        return pfLoan;
    }

    @BeforeEach
    public void initTest() {
        pfLoan = createEntity(em);
    }

    @Test
    @Transactional
    void createPfLoan() throws Exception {
        int databaseSizeBeforeCreate = pfLoanRepository.findAll().size();
        // Create the PfLoan
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(pfLoan);
        restPfLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfLoanDTO)))
            .andExpect(status().isCreated());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeCreate + 1);
        PfLoan testPfLoan = pfLoanList.get(pfLoanList.size() - 1);
        assertThat(testPfLoan.getDisbursementAmount()).isEqualTo(DEFAULT_DISBURSEMENT_AMOUNT);
        assertThat(testPfLoan.getDisbursementDate()).isEqualTo(DEFAULT_DISBURSEMENT_DATE);
        assertThat(testPfLoan.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testPfLoan.getBankBranch()).isEqualTo(DEFAULT_BANK_BRANCH);
        assertThat(testPfLoan.getBankAccountNumber()).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER);
        assertThat(testPfLoan.getChequeNumber()).isEqualTo(DEFAULT_CHEQUE_NUMBER);
        assertThat(testPfLoan.getInstalmentNumber()).isEqualTo(DEFAULT_INSTALMENT_NUMBER);
        assertThat(testPfLoan.getInstallmentAmount()).isEqualTo(DEFAULT_INSTALLMENT_AMOUNT);
        assertThat(testPfLoan.getInstalmentStartFrom()).isEqualTo(DEFAULT_INSTALMENT_START_FROM);
        assertThat(testPfLoan.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createPfLoanWithExistingId() throws Exception {
        // Create the PfLoan with an existing ID
        pfLoan.setId(1L);
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(pfLoan);

        int databaseSizeBeforeCreate = pfLoanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPfLoanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfLoanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPfLoans() throws Exception {
        // Initialize the database
        pfLoanRepository.saveAndFlush(pfLoan);

        // Get all the pfLoanList
        restPfLoanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pfLoan.getId().intValue())))
            .andExpect(jsonPath("$.[*].disbursementAmount").value(hasItem(DEFAULT_DISBURSEMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].disbursementDate").value(hasItem(DEFAULT_DISBURSEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].bankBranch").value(hasItem(DEFAULT_BANK_BRANCH)))
            .andExpect(jsonPath("$.[*].bankAccountNumber").value(hasItem(DEFAULT_BANK_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].chequeNumber").value(hasItem(DEFAULT_CHEQUE_NUMBER)))
            .andExpect(jsonPath("$.[*].instalmentNumber").value(hasItem(DEFAULT_INSTALMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].installmentAmount").value(hasItem(DEFAULT_INSTALLMENT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].instalmentStartFrom").value(hasItem(DEFAULT_INSTALMENT_START_FROM.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPfLoan() throws Exception {
        // Initialize the database
        pfLoanRepository.saveAndFlush(pfLoan);

        // Get the pfLoan
        restPfLoanMockMvc
            .perform(get(ENTITY_API_URL_ID, pfLoan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pfLoan.getId().intValue()))
            .andExpect(jsonPath("$.disbursementAmount").value(DEFAULT_DISBURSEMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.disbursementDate").value(DEFAULT_DISBURSEMENT_DATE.toString()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.bankBranch").value(DEFAULT_BANK_BRANCH))
            .andExpect(jsonPath("$.bankAccountNumber").value(DEFAULT_BANK_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.chequeNumber").value(DEFAULT_CHEQUE_NUMBER))
            .andExpect(jsonPath("$.instalmentNumber").value(DEFAULT_INSTALMENT_NUMBER))
            .andExpect(jsonPath("$.installmentAmount").value(DEFAULT_INSTALLMENT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.instalmentStartFrom").value(DEFAULT_INSTALMENT_START_FROM.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPfLoan() throws Exception {
        // Get the pfLoan
        restPfLoanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPfLoan() throws Exception {
        // Initialize the database
        pfLoanRepository.saveAndFlush(pfLoan);

        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();

        // Update the pfLoan
        PfLoan updatedPfLoan = pfLoanRepository.findById(pfLoan.getId()).get();
        // Disconnect from session so that the updates on updatedPfLoan are not directly saved in db
        em.detach(updatedPfLoan);
        updatedPfLoan
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .bankName(UPDATED_BANK_NAME)
            .bankBranch(UPDATED_BANK_BRANCH)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .chequeNumber(UPDATED_CHEQUE_NUMBER)
            .instalmentNumber(UPDATED_INSTALMENT_NUMBER)
            .installmentAmount(UPDATED_INSTALLMENT_AMOUNT)
            .instalmentStartFrom(UPDATED_INSTALMENT_START_FROM)
            .status(UPDATED_STATUS);
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(updatedPfLoan);

        restPfLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfLoanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanDTO))
            )
            .andExpect(status().isOk());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
        PfLoan testPfLoan = pfLoanList.get(pfLoanList.size() - 1);
        assertThat(testPfLoan.getDisbursementAmount()).isEqualTo(UPDATED_DISBURSEMENT_AMOUNT);
        assertThat(testPfLoan.getDisbursementDate()).isEqualTo(UPDATED_DISBURSEMENT_DATE);
        assertThat(testPfLoan.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testPfLoan.getBankBranch()).isEqualTo(UPDATED_BANK_BRANCH);
        assertThat(testPfLoan.getBankAccountNumber()).isEqualTo(UPDATED_BANK_ACCOUNT_NUMBER);
        assertThat(testPfLoan.getChequeNumber()).isEqualTo(UPDATED_CHEQUE_NUMBER);
        assertThat(testPfLoan.getInstalmentNumber()).isEqualTo(UPDATED_INSTALMENT_NUMBER);
        assertThat(testPfLoan.getInstallmentAmount()).isEqualTo(UPDATED_INSTALLMENT_AMOUNT);
        assertThat(testPfLoan.getInstalmentStartFrom()).isEqualTo(UPDATED_INSTALMENT_START_FROM);
        assertThat(testPfLoan.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPfLoan() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();
        pfLoan.setId(count.incrementAndGet());

        // Create the PfLoan
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(pfLoan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfLoanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPfLoan() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();
        pfLoan.setId(count.incrementAndGet());

        // Create the PfLoan
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(pfLoan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPfLoan() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();
        pfLoan.setId(count.incrementAndGet());

        // Create the PfLoan
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(pfLoan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfLoanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePfLoanWithPatch() throws Exception {
        // Initialize the database
        pfLoanRepository.saveAndFlush(pfLoan);

        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();

        // Update the pfLoan using partial update
        PfLoan partialUpdatedPfLoan = new PfLoan();
        partialUpdatedPfLoan.setId(pfLoan.getId());

        partialUpdatedPfLoan
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .bankBranch(UPDATED_BANK_BRANCH)
            .installmentAmount(UPDATED_INSTALLMENT_AMOUNT)
            .instalmentStartFrom(UPDATED_INSTALMENT_START_FROM);

        restPfLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfLoan))
            )
            .andExpect(status().isOk());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
        PfLoan testPfLoan = pfLoanList.get(pfLoanList.size() - 1);
        assertThat(testPfLoan.getDisbursementAmount()).isEqualTo(DEFAULT_DISBURSEMENT_AMOUNT);
        assertThat(testPfLoan.getDisbursementDate()).isEqualTo(UPDATED_DISBURSEMENT_DATE);
        assertThat(testPfLoan.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testPfLoan.getBankBranch()).isEqualTo(UPDATED_BANK_BRANCH);
        assertThat(testPfLoan.getBankAccountNumber()).isEqualTo(DEFAULT_BANK_ACCOUNT_NUMBER);
        assertThat(testPfLoan.getChequeNumber()).isEqualTo(DEFAULT_CHEQUE_NUMBER);
        assertThat(testPfLoan.getInstalmentNumber()).isEqualTo(DEFAULT_INSTALMENT_NUMBER);
        assertThat(testPfLoan.getInstallmentAmount()).isEqualTo(UPDATED_INSTALLMENT_AMOUNT);
        assertThat(testPfLoan.getInstalmentStartFrom()).isEqualTo(UPDATED_INSTALMENT_START_FROM);
        assertThat(testPfLoan.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePfLoanWithPatch() throws Exception {
        // Initialize the database
        pfLoanRepository.saveAndFlush(pfLoan);

        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();

        // Update the pfLoan using partial update
        PfLoan partialUpdatedPfLoan = new PfLoan();
        partialUpdatedPfLoan.setId(pfLoan.getId());

        partialUpdatedPfLoan
            .disbursementAmount(UPDATED_DISBURSEMENT_AMOUNT)
            .disbursementDate(UPDATED_DISBURSEMENT_DATE)
            .bankName(UPDATED_BANK_NAME)
            .bankBranch(UPDATED_BANK_BRANCH)
            .bankAccountNumber(UPDATED_BANK_ACCOUNT_NUMBER)
            .chequeNumber(UPDATED_CHEQUE_NUMBER)
            .instalmentNumber(UPDATED_INSTALMENT_NUMBER)
            .installmentAmount(UPDATED_INSTALLMENT_AMOUNT)
            .instalmentStartFrom(UPDATED_INSTALMENT_START_FROM)
            .status(UPDATED_STATUS);

        restPfLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfLoan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfLoan))
            )
            .andExpect(status().isOk());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
        PfLoan testPfLoan = pfLoanList.get(pfLoanList.size() - 1);
        assertThat(testPfLoan.getDisbursementAmount()).isEqualTo(UPDATED_DISBURSEMENT_AMOUNT);
        assertThat(testPfLoan.getDisbursementDate()).isEqualTo(UPDATED_DISBURSEMENT_DATE);
        assertThat(testPfLoan.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testPfLoan.getBankBranch()).isEqualTo(UPDATED_BANK_BRANCH);
        assertThat(testPfLoan.getBankAccountNumber()).isEqualTo(UPDATED_BANK_ACCOUNT_NUMBER);
        assertThat(testPfLoan.getChequeNumber()).isEqualTo(UPDATED_CHEQUE_NUMBER);
        assertThat(testPfLoan.getInstalmentNumber()).isEqualTo(UPDATED_INSTALMENT_NUMBER);
        assertThat(testPfLoan.getInstallmentAmount()).isEqualTo(UPDATED_INSTALLMENT_AMOUNT);
        assertThat(testPfLoan.getInstalmentStartFrom()).isEqualTo(UPDATED_INSTALMENT_START_FROM);
        assertThat(testPfLoan.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPfLoan() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();
        pfLoan.setId(count.incrementAndGet());

        // Create the PfLoan
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(pfLoan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pfLoanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPfLoan() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();
        pfLoan.setId(count.incrementAndGet());

        // Create the PfLoan
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(pfLoan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPfLoan() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepository.findAll().size();
        pfLoan.setId(count.incrementAndGet());

        // Create the PfLoan
        PfLoanDTO pfLoanDTO = pfLoanMapper.toDto(pfLoan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pfLoanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfLoan in the database
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePfLoan() throws Exception {
        // Initialize the database
        pfLoanRepository.saveAndFlush(pfLoan);

        int databaseSizeBeforeDelete = pfLoanRepository.findAll().size();

        // Delete the pfLoan
        restPfLoanMockMvc
            .perform(delete(ENTITY_API_URL_ID, pfLoan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PfLoan> pfLoanList = pfLoanRepository.findAll();
        assertThat(pfLoanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
