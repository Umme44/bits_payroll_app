package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.SalaryGeneratorMaster;
import com.bits.hr.domain.enumeration.Visibility;
import com.bits.hr.repository.SalaryGeneratorMasterRepository;
import com.bits.hr.service.dto.SalaryGeneratorMasterDTO;
import com.bits.hr.service.mapper.SalaryGeneratorMasterMapper;
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
 * Integration tests for the {@link SalaryGeneratorMasterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalaryGeneratorMasterResourceIT {

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_GENERATED = false;
    private static final Boolean UPDATED_IS_GENERATED = true;

    private static final Boolean DEFAULT_IS_MOBILE_BILL_IMPORTED = false;
    private static final Boolean UPDATED_IS_MOBILE_BILL_IMPORTED = true;

    private static final Boolean DEFAULT_IS_PF_LOAN_REPAYMENT_IMPORTED = false;
    private static final Boolean UPDATED_IS_PF_LOAN_REPAYMENT_IMPORTED = true;

    private static final Boolean DEFAULT_IS_ATTENDANCE_IMPORTED = false;
    private static final Boolean UPDATED_IS_ATTENDANCE_IMPORTED = true;

    private static final Boolean DEFAULT_IS_SALARY_DEDUCTION_IMPORTED = false;
    private static final Boolean UPDATED_IS_SALARY_DEDUCTION_IMPORTED = true;

    private static final Boolean DEFAULT_IS_FINALIZED = false;
    private static final Boolean UPDATED_IS_FINALIZED = true;

    private static final Visibility DEFAULT_VISIBILITY = Visibility.NOT_VISIBLE_TO_EMPLOYEE;
    private static final Visibility UPDATED_VISIBILITY = Visibility.VISIBLE_TO_EMPLOYEE;

    private static final String ENTITY_API_URL = "/api/salary-generator-masters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalaryGeneratorMasterRepository salaryGeneratorMasterRepository;

    @Autowired
    private SalaryGeneratorMasterMapper salaryGeneratorMasterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalaryGeneratorMasterMockMvc;

    private SalaryGeneratorMaster salaryGeneratorMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryGeneratorMaster createEntity(EntityManager em) {
        SalaryGeneratorMaster salaryGeneratorMaster = new SalaryGeneratorMaster()
            .year(DEFAULT_YEAR)
            .month(DEFAULT_MONTH)
            .isGenerated(DEFAULT_IS_GENERATED)
            .isMobileBillImported(DEFAULT_IS_MOBILE_BILL_IMPORTED)
            .isPFLoanRepaymentImported(DEFAULT_IS_PF_LOAN_REPAYMENT_IMPORTED)
            .isAttendanceImported(DEFAULT_IS_ATTENDANCE_IMPORTED)
            .isSalaryDeductionImported(DEFAULT_IS_SALARY_DEDUCTION_IMPORTED)
            .isFinalized(DEFAULT_IS_FINALIZED)
            .visibility(DEFAULT_VISIBILITY);
        return salaryGeneratorMaster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalaryGeneratorMaster createUpdatedEntity(EntityManager em) {
        SalaryGeneratorMaster salaryGeneratorMaster = new SalaryGeneratorMaster()
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .isGenerated(UPDATED_IS_GENERATED)
            .isMobileBillImported(UPDATED_IS_MOBILE_BILL_IMPORTED)
            .isPFLoanRepaymentImported(UPDATED_IS_PF_LOAN_REPAYMENT_IMPORTED)
            .isAttendanceImported(UPDATED_IS_ATTENDANCE_IMPORTED)
            .isSalaryDeductionImported(UPDATED_IS_SALARY_DEDUCTION_IMPORTED)
            .isFinalized(UPDATED_IS_FINALIZED)
            .visibility(UPDATED_VISIBILITY);
        return salaryGeneratorMaster;
    }

    @BeforeEach
    public void initTest() {
        salaryGeneratorMaster = createEntity(em);
    }

    @Test
    @Transactional
    void createSalaryGeneratorMaster() throws Exception {
        int databaseSizeBeforeCreate = salaryGeneratorMasterRepository.findAll().size();
        // Create the SalaryGeneratorMaster
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(salaryGeneratorMaster);
        restSalaryGeneratorMasterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeCreate + 1);
        SalaryGeneratorMaster testSalaryGeneratorMaster = salaryGeneratorMasterList.get(salaryGeneratorMasterList.size() - 1);
        assertThat(testSalaryGeneratorMaster.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testSalaryGeneratorMaster.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testSalaryGeneratorMaster.getIsGenerated()).isEqualTo(DEFAULT_IS_GENERATED);
        assertThat(testSalaryGeneratorMaster.getIsMobileBillImported()).isEqualTo(DEFAULT_IS_MOBILE_BILL_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsPFLoanRepaymentImported()).isEqualTo(DEFAULT_IS_PF_LOAN_REPAYMENT_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsAttendanceImported()).isEqualTo(DEFAULT_IS_ATTENDANCE_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsSalaryDeductionImported()).isEqualTo(DEFAULT_IS_SALARY_DEDUCTION_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsFinalized()).isEqualTo(DEFAULT_IS_FINALIZED);
        assertThat(testSalaryGeneratorMaster.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
    }

    @Test
    @Transactional
    void createSalaryGeneratorMasterWithExistingId() throws Exception {
        // Create the SalaryGeneratorMaster with an existing ID
        salaryGeneratorMaster.setId(1L);
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(salaryGeneratorMaster);

        int databaseSizeBeforeCreate = salaryGeneratorMasterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalaryGeneratorMasterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSalaryGeneratorMasters() throws Exception {
        // Initialize the database
        salaryGeneratorMasterRepository.saveAndFlush(salaryGeneratorMaster);

        // Get all the salaryGeneratorMasterList
        restSalaryGeneratorMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaryGeneratorMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].isGenerated").value(hasItem(DEFAULT_IS_GENERATED.booleanValue())))
            .andExpect(jsonPath("$.[*].isMobileBillImported").value(hasItem(DEFAULT_IS_MOBILE_BILL_IMPORTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPFLoanRepaymentImported").value(hasItem(DEFAULT_IS_PF_LOAN_REPAYMENT_IMPORTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isAttendanceImported").value(hasItem(DEFAULT_IS_ATTENDANCE_IMPORTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isSalaryDeductionImported").value(hasItem(DEFAULT_IS_SALARY_DEDUCTION_IMPORTED.booleanValue())))
            .andExpect(jsonPath("$.[*].isFinalized").value(hasItem(DEFAULT_IS_FINALIZED.booleanValue())))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }

    @Test
    @Transactional
    void getSalaryGeneratorMaster() throws Exception {
        // Initialize the database
        salaryGeneratorMasterRepository.saveAndFlush(salaryGeneratorMaster);

        // Get the salaryGeneratorMaster
        restSalaryGeneratorMasterMockMvc
            .perform(get(ENTITY_API_URL_ID, salaryGeneratorMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salaryGeneratorMaster.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.isGenerated").value(DEFAULT_IS_GENERATED.booleanValue()))
            .andExpect(jsonPath("$.isMobileBillImported").value(DEFAULT_IS_MOBILE_BILL_IMPORTED.booleanValue()))
            .andExpect(jsonPath("$.isPFLoanRepaymentImported").value(DEFAULT_IS_PF_LOAN_REPAYMENT_IMPORTED.booleanValue()))
            .andExpect(jsonPath("$.isAttendanceImported").value(DEFAULT_IS_ATTENDANCE_IMPORTED.booleanValue()))
            .andExpect(jsonPath("$.isSalaryDeductionImported").value(DEFAULT_IS_SALARY_DEDUCTION_IMPORTED.booleanValue()))
            .andExpect(jsonPath("$.isFinalized").value(DEFAULT_IS_FINALIZED.booleanValue()))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSalaryGeneratorMaster() throws Exception {
        // Get the salaryGeneratorMaster
        restSalaryGeneratorMasterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalaryGeneratorMaster() throws Exception {
        // Initialize the database
        salaryGeneratorMasterRepository.saveAndFlush(salaryGeneratorMaster);

        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();

        // Update the salaryGeneratorMaster
        SalaryGeneratorMaster updatedSalaryGeneratorMaster = salaryGeneratorMasterRepository.findById(salaryGeneratorMaster.getId()).get();
        // Disconnect from session so that the updates on updatedSalaryGeneratorMaster are not directly saved in db
        em.detach(updatedSalaryGeneratorMaster);
        updatedSalaryGeneratorMaster
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .isGenerated(UPDATED_IS_GENERATED)
            .isMobileBillImported(UPDATED_IS_MOBILE_BILL_IMPORTED)
            .isPFLoanRepaymentImported(UPDATED_IS_PF_LOAN_REPAYMENT_IMPORTED)
            .isAttendanceImported(UPDATED_IS_ATTENDANCE_IMPORTED)
            .isSalaryDeductionImported(UPDATED_IS_SALARY_DEDUCTION_IMPORTED)
            .isFinalized(UPDATED_IS_FINALIZED)
            .visibility(UPDATED_VISIBILITY);
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(updatedSalaryGeneratorMaster);

        restSalaryGeneratorMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryGeneratorMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isOk());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
        SalaryGeneratorMaster testSalaryGeneratorMaster = salaryGeneratorMasterList.get(salaryGeneratorMasterList.size() - 1);
        assertThat(testSalaryGeneratorMaster.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testSalaryGeneratorMaster.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testSalaryGeneratorMaster.getIsGenerated()).isEqualTo(UPDATED_IS_GENERATED);
        assertThat(testSalaryGeneratorMaster.getIsMobileBillImported()).isEqualTo(UPDATED_IS_MOBILE_BILL_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsPFLoanRepaymentImported()).isEqualTo(UPDATED_IS_PF_LOAN_REPAYMENT_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsAttendanceImported()).isEqualTo(UPDATED_IS_ATTENDANCE_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsSalaryDeductionImported()).isEqualTo(UPDATED_IS_SALARY_DEDUCTION_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsFinalized()).isEqualTo(UPDATED_IS_FINALIZED);
        assertThat(testSalaryGeneratorMaster.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    void putNonExistingSalaryGeneratorMaster() throws Exception {
        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();
        salaryGeneratorMaster.setId(count.incrementAndGet());

        // Create the SalaryGeneratorMaster
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(salaryGeneratorMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryGeneratorMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salaryGeneratorMasterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalaryGeneratorMaster() throws Exception {
        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();
        salaryGeneratorMaster.setId(count.incrementAndGet());

        // Create the SalaryGeneratorMaster
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(salaryGeneratorMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryGeneratorMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalaryGeneratorMaster() throws Exception {
        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();
        salaryGeneratorMaster.setId(count.incrementAndGet());

        // Create the SalaryGeneratorMaster
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(salaryGeneratorMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryGeneratorMasterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalaryGeneratorMasterWithPatch() throws Exception {
        // Initialize the database
        salaryGeneratorMasterRepository.saveAndFlush(salaryGeneratorMaster);

        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();

        // Update the salaryGeneratorMaster using partial update
        SalaryGeneratorMaster partialUpdatedSalaryGeneratorMaster = new SalaryGeneratorMaster();
        partialUpdatedSalaryGeneratorMaster.setId(salaryGeneratorMaster.getId());

        partialUpdatedSalaryGeneratorMaster
            .year(UPDATED_YEAR)
            .isGenerated(UPDATED_IS_GENERATED)
            .isMobileBillImported(UPDATED_IS_MOBILE_BILL_IMPORTED)
            .isPFLoanRepaymentImported(UPDATED_IS_PF_LOAN_REPAYMENT_IMPORTED)
            .isFinalized(UPDATED_IS_FINALIZED);

        restSalaryGeneratorMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryGeneratorMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaryGeneratorMaster))
            )
            .andExpect(status().isOk());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
        SalaryGeneratorMaster testSalaryGeneratorMaster = salaryGeneratorMasterList.get(salaryGeneratorMasterList.size() - 1);
        assertThat(testSalaryGeneratorMaster.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testSalaryGeneratorMaster.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testSalaryGeneratorMaster.getIsGenerated()).isEqualTo(UPDATED_IS_GENERATED);
        assertThat(testSalaryGeneratorMaster.getIsMobileBillImported()).isEqualTo(UPDATED_IS_MOBILE_BILL_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsPFLoanRepaymentImported()).isEqualTo(UPDATED_IS_PF_LOAN_REPAYMENT_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsAttendanceImported()).isEqualTo(DEFAULT_IS_ATTENDANCE_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsSalaryDeductionImported()).isEqualTo(DEFAULT_IS_SALARY_DEDUCTION_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsFinalized()).isEqualTo(UPDATED_IS_FINALIZED);
        assertThat(testSalaryGeneratorMaster.getVisibility()).isEqualTo(DEFAULT_VISIBILITY);
    }

    @Test
    @Transactional
    void fullUpdateSalaryGeneratorMasterWithPatch() throws Exception {
        // Initialize the database
        salaryGeneratorMasterRepository.saveAndFlush(salaryGeneratorMaster);

        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();

        // Update the salaryGeneratorMaster using partial update
        SalaryGeneratorMaster partialUpdatedSalaryGeneratorMaster = new SalaryGeneratorMaster();
        partialUpdatedSalaryGeneratorMaster.setId(salaryGeneratorMaster.getId());

        partialUpdatedSalaryGeneratorMaster
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .isGenerated(UPDATED_IS_GENERATED)
            .isMobileBillImported(UPDATED_IS_MOBILE_BILL_IMPORTED)
            .isPFLoanRepaymentImported(UPDATED_IS_PF_LOAN_REPAYMENT_IMPORTED)
            .isAttendanceImported(UPDATED_IS_ATTENDANCE_IMPORTED)
            .isSalaryDeductionImported(UPDATED_IS_SALARY_DEDUCTION_IMPORTED)
            .isFinalized(UPDATED_IS_FINALIZED)
            .visibility(UPDATED_VISIBILITY);

        restSalaryGeneratorMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalaryGeneratorMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalaryGeneratorMaster))
            )
            .andExpect(status().isOk());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
        SalaryGeneratorMaster testSalaryGeneratorMaster = salaryGeneratorMasterList.get(salaryGeneratorMasterList.size() - 1);
        assertThat(testSalaryGeneratorMaster.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testSalaryGeneratorMaster.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testSalaryGeneratorMaster.getIsGenerated()).isEqualTo(UPDATED_IS_GENERATED);
        assertThat(testSalaryGeneratorMaster.getIsMobileBillImported()).isEqualTo(UPDATED_IS_MOBILE_BILL_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsPFLoanRepaymentImported()).isEqualTo(UPDATED_IS_PF_LOAN_REPAYMENT_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsAttendanceImported()).isEqualTo(UPDATED_IS_ATTENDANCE_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsSalaryDeductionImported()).isEqualTo(UPDATED_IS_SALARY_DEDUCTION_IMPORTED);
        assertThat(testSalaryGeneratorMaster.getIsFinalized()).isEqualTo(UPDATED_IS_FINALIZED);
        assertThat(testSalaryGeneratorMaster.getVisibility()).isEqualTo(UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    void patchNonExistingSalaryGeneratorMaster() throws Exception {
        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();
        salaryGeneratorMaster.setId(count.incrementAndGet());

        // Create the SalaryGeneratorMaster
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(salaryGeneratorMaster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalaryGeneratorMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salaryGeneratorMasterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalaryGeneratorMaster() throws Exception {
        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();
        salaryGeneratorMaster.setId(count.incrementAndGet());

        // Create the SalaryGeneratorMaster
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(salaryGeneratorMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryGeneratorMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalaryGeneratorMaster() throws Exception {
        int databaseSizeBeforeUpdate = salaryGeneratorMasterRepository.findAll().size();
        salaryGeneratorMaster.setId(count.incrementAndGet());

        // Create the SalaryGeneratorMaster
        SalaryGeneratorMasterDTO salaryGeneratorMasterDTO = salaryGeneratorMasterMapper.toDto(salaryGeneratorMaster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalaryGeneratorMasterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salaryGeneratorMasterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalaryGeneratorMaster in the database
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalaryGeneratorMaster() throws Exception {
        // Initialize the database
        salaryGeneratorMasterRepository.saveAndFlush(salaryGeneratorMaster);

        int databaseSizeBeforeDelete = salaryGeneratorMasterRepository.findAll().size();

        // Delete the salaryGeneratorMaster
        restSalaryGeneratorMasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, salaryGeneratorMaster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalaryGeneratorMaster> salaryGeneratorMasterList = salaryGeneratorMasterRepository.findAll();
        assertThat(salaryGeneratorMasterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
