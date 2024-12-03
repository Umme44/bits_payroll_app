package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.PfLoanRepayment;
import com.bits.hr.domain.enumeration.PfRepaymentStatus;
import com.bits.hr.repository.PfLoanRepaymentRepository;
import com.bits.hr.service.dto.PfLoanRepaymentDTO;
import com.bits.hr.service.mapper.PfLoanRepaymentMapper;
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
 * Integration tests for the {@link PfLoanRepaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PfLoanRepaymentResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final PfRepaymentStatus DEFAULT_STATUS = PfRepaymentStatus.SUBMITTED;
    private static final PfRepaymentStatus UPDATED_STATUS = PfRepaymentStatus.PROVISIONED;

    private static final Integer DEFAULT_DEDUCTION_MONTH = 1;
    private static final Integer UPDATED_DEDUCTION_MONTH = 2;

    private static final Integer DEFAULT_DEDUCTION_YEAR = 1;
    private static final Integer UPDATED_DEDUCTION_YEAR = 2;

    private static final LocalDate DEFAULT_DEDUCTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEDUCTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/pf-loan-repayments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PfLoanRepaymentRepository pfLoanRepaymentRepository;

    @Autowired
    private PfLoanRepaymentMapper pfLoanRepaymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPfLoanRepaymentMockMvc;

    private PfLoanRepayment pfLoanRepayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfLoanRepayment createEntity(EntityManager em) {
        PfLoanRepayment pfLoanRepayment = new PfLoanRepayment()
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS)
            .deductionMonth(DEFAULT_DEDUCTION_MONTH)
            .deductionYear(DEFAULT_DEDUCTION_YEAR)
            .deductionDate(DEFAULT_DEDUCTION_DATE);
        return pfLoanRepayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfLoanRepayment createUpdatedEntity(EntityManager em) {
        PfLoanRepayment pfLoanRepayment = new PfLoanRepayment()
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .deductionMonth(UPDATED_DEDUCTION_MONTH)
            .deductionYear(UPDATED_DEDUCTION_YEAR)
            .deductionDate(UPDATED_DEDUCTION_DATE);
        return pfLoanRepayment;
    }

    @BeforeEach
    public void initTest() {
        pfLoanRepayment = createEntity(em);
    }

    @Test
    @Transactional
    void createPfLoanRepayment() throws Exception {
        int databaseSizeBeforeCreate = pfLoanRepaymentRepository.findAll().size();
        // Create the PfLoanRepayment
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(pfLoanRepayment);
        restPfLoanRepaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeCreate + 1);
        PfLoanRepayment testPfLoanRepayment = pfLoanRepaymentList.get(pfLoanRepaymentList.size() - 1);
        assertThat(testPfLoanRepayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPfLoanRepayment.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPfLoanRepayment.getDeductionMonth()).isEqualTo(DEFAULT_DEDUCTION_MONTH);
        assertThat(testPfLoanRepayment.getDeductionYear()).isEqualTo(DEFAULT_DEDUCTION_YEAR);
        assertThat(testPfLoanRepayment.getDeductionDate()).isEqualTo(DEFAULT_DEDUCTION_DATE);
    }

    @Test
    @Transactional
    void createPfLoanRepaymentWithExistingId() throws Exception {
        // Create the PfLoanRepayment with an existing ID
        pfLoanRepayment.setId(1L);
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(pfLoanRepayment);

        int databaseSizeBeforeCreate = pfLoanRepaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPfLoanRepaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPfLoanRepayments() throws Exception {
        // Initialize the database
        pfLoanRepaymentRepository.saveAndFlush(pfLoanRepayment);

        // Get all the pfLoanRepaymentList
        restPfLoanRepaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pfLoanRepayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].deductionMonth").value(hasItem(DEFAULT_DEDUCTION_MONTH)))
            .andExpect(jsonPath("$.[*].deductionYear").value(hasItem(DEFAULT_DEDUCTION_YEAR)))
            .andExpect(jsonPath("$.[*].deductionDate").value(hasItem(DEFAULT_DEDUCTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getPfLoanRepayment() throws Exception {
        // Initialize the database
        pfLoanRepaymentRepository.saveAndFlush(pfLoanRepayment);

        // Get the pfLoanRepayment
        restPfLoanRepaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, pfLoanRepayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pfLoanRepayment.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.deductionMonth").value(DEFAULT_DEDUCTION_MONTH))
            .andExpect(jsonPath("$.deductionYear").value(DEFAULT_DEDUCTION_YEAR))
            .andExpect(jsonPath("$.deductionDate").value(DEFAULT_DEDUCTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPfLoanRepayment() throws Exception {
        // Get the pfLoanRepayment
        restPfLoanRepaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPfLoanRepayment() throws Exception {
        // Initialize the database
        pfLoanRepaymentRepository.saveAndFlush(pfLoanRepayment);

        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();

        // Update the pfLoanRepayment
        PfLoanRepayment updatedPfLoanRepayment = pfLoanRepaymentRepository.findById(pfLoanRepayment.getId()).get();
        // Disconnect from session so that the updates on updatedPfLoanRepayment are not directly saved in db
        em.detach(updatedPfLoanRepayment);
        updatedPfLoanRepayment
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .deductionMonth(UPDATED_DEDUCTION_MONTH)
            .deductionYear(UPDATED_DEDUCTION_YEAR)
            .deductionDate(UPDATED_DEDUCTION_DATE);
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(updatedPfLoanRepayment);

        restPfLoanRepaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfLoanRepaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
        PfLoanRepayment testPfLoanRepayment = pfLoanRepaymentList.get(pfLoanRepaymentList.size() - 1);
        assertThat(testPfLoanRepayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPfLoanRepayment.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPfLoanRepayment.getDeductionMonth()).isEqualTo(UPDATED_DEDUCTION_MONTH);
        assertThat(testPfLoanRepayment.getDeductionYear()).isEqualTo(UPDATED_DEDUCTION_YEAR);
        assertThat(testPfLoanRepayment.getDeductionDate()).isEqualTo(UPDATED_DEDUCTION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPfLoanRepayment() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();
        pfLoanRepayment.setId(count.incrementAndGet());

        // Create the PfLoanRepayment
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(pfLoanRepayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfLoanRepaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfLoanRepaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPfLoanRepayment() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();
        pfLoanRepayment.setId(count.incrementAndGet());

        // Create the PfLoanRepayment
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(pfLoanRepayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanRepaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPfLoanRepayment() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();
        pfLoanRepayment.setId(count.incrementAndGet());

        // Create the PfLoanRepayment
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(pfLoanRepayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanRepaymentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePfLoanRepaymentWithPatch() throws Exception {
        // Initialize the database
        pfLoanRepaymentRepository.saveAndFlush(pfLoanRepayment);

        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();

        // Update the pfLoanRepayment using partial update
        PfLoanRepayment partialUpdatedPfLoanRepayment = new PfLoanRepayment();
        partialUpdatedPfLoanRepayment.setId(pfLoanRepayment.getId());

        partialUpdatedPfLoanRepayment
            .amount(UPDATED_AMOUNT)
            .deductionMonth(UPDATED_DEDUCTION_MONTH)
            .deductionYear(UPDATED_DEDUCTION_YEAR)
            .deductionDate(UPDATED_DEDUCTION_DATE);

        restPfLoanRepaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfLoanRepayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfLoanRepayment))
            )
            .andExpect(status().isOk());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
        PfLoanRepayment testPfLoanRepayment = pfLoanRepaymentList.get(pfLoanRepaymentList.size() - 1);
        assertThat(testPfLoanRepayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPfLoanRepayment.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPfLoanRepayment.getDeductionMonth()).isEqualTo(UPDATED_DEDUCTION_MONTH);
        assertThat(testPfLoanRepayment.getDeductionYear()).isEqualTo(UPDATED_DEDUCTION_YEAR);
        assertThat(testPfLoanRepayment.getDeductionDate()).isEqualTo(UPDATED_DEDUCTION_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePfLoanRepaymentWithPatch() throws Exception {
        // Initialize the database
        pfLoanRepaymentRepository.saveAndFlush(pfLoanRepayment);

        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();

        // Update the pfLoanRepayment using partial update
        PfLoanRepayment partialUpdatedPfLoanRepayment = new PfLoanRepayment();
        partialUpdatedPfLoanRepayment.setId(pfLoanRepayment.getId());

        partialUpdatedPfLoanRepayment
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .deductionMonth(UPDATED_DEDUCTION_MONTH)
            .deductionYear(UPDATED_DEDUCTION_YEAR)
            .deductionDate(UPDATED_DEDUCTION_DATE);

        restPfLoanRepaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfLoanRepayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfLoanRepayment))
            )
            .andExpect(status().isOk());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
        PfLoanRepayment testPfLoanRepayment = pfLoanRepaymentList.get(pfLoanRepaymentList.size() - 1);
        assertThat(testPfLoanRepayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPfLoanRepayment.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPfLoanRepayment.getDeductionMonth()).isEqualTo(UPDATED_DEDUCTION_MONTH);
        assertThat(testPfLoanRepayment.getDeductionYear()).isEqualTo(UPDATED_DEDUCTION_YEAR);
        assertThat(testPfLoanRepayment.getDeductionDate()).isEqualTo(UPDATED_DEDUCTION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPfLoanRepayment() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();
        pfLoanRepayment.setId(count.incrementAndGet());

        // Create the PfLoanRepayment
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(pfLoanRepayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfLoanRepaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pfLoanRepaymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPfLoanRepayment() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();
        pfLoanRepayment.setId(count.incrementAndGet());

        // Create the PfLoanRepayment
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(pfLoanRepayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanRepaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPfLoanRepayment() throws Exception {
        int databaseSizeBeforeUpdate = pfLoanRepaymentRepository.findAll().size();
        pfLoanRepayment.setId(count.incrementAndGet());

        // Create the PfLoanRepayment
        PfLoanRepaymentDTO pfLoanRepaymentDTO = pfLoanRepaymentMapper.toDto(pfLoanRepayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfLoanRepaymentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfLoanRepaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfLoanRepayment in the database
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePfLoanRepayment() throws Exception {
        // Initialize the database
        pfLoanRepaymentRepository.saveAndFlush(pfLoanRepayment);

        int databaseSizeBeforeDelete = pfLoanRepaymentRepository.findAll().size();

        // Delete the pfLoanRepayment
        restPfLoanRepaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, pfLoanRepayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PfLoanRepayment> pfLoanRepaymentList = pfLoanRepaymentRepository.findAll();
        assertThat(pfLoanRepaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
