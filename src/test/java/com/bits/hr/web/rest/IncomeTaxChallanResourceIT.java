package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.IncomeTaxChallan;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.IncomeTaxChallanRepository;
import com.bits.hr.service.dto.IncomeTaxChallanDTO;
import com.bits.hr.service.mapper.IncomeTaxChallanMapper;
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
 * Integration tests for the {@link IncomeTaxChallanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncomeTaxChallanResourceIT {

    private static final String DEFAULT_CHALLAN_NO = "AAAAAAAAAA";
    private static final String UPDATED_CHALLAN_NO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CHALLAN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHALLAN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Month DEFAULT_MONTH = Month.JANUARY;
    private static final Month UPDATED_MONTH = Month.FEBRUARY;

    private static final Integer DEFAULT_YEAR = 1990;
    private static final Integer UPDATED_YEAR = 1991;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/income-tax-challans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IncomeTaxChallanRepository incomeTaxChallanRepository;

    @Autowired
    private IncomeTaxChallanMapper incomeTaxChallanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncomeTaxChallanMockMvc;

    private IncomeTaxChallan incomeTaxChallan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeTaxChallan createEntity(EntityManager em) {
        IncomeTaxChallan incomeTaxChallan = new IncomeTaxChallan()
            .challanNo(DEFAULT_CHALLAN_NO)
            .challanDate(DEFAULT_CHALLAN_DATE)
            .amount(DEFAULT_AMOUNT)
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR)
            .remarks(DEFAULT_REMARKS);
        return incomeTaxChallan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IncomeTaxChallan createUpdatedEntity(EntityManager em) {
        IncomeTaxChallan incomeTaxChallan = new IncomeTaxChallan()
            .challanNo(UPDATED_CHALLAN_NO)
            .challanDate(UPDATED_CHALLAN_DATE)
            .amount(UPDATED_AMOUNT)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .remarks(UPDATED_REMARKS);
        return incomeTaxChallan;
    }

    @BeforeEach
    public void initTest() {
        incomeTaxChallan = createEntity(em);
    }

    @Test
    @Transactional
    void createIncomeTaxChallan() throws Exception {
        int databaseSizeBeforeCreate = incomeTaxChallanRepository.findAll().size();
        // Create the IncomeTaxChallan
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);
        restIncomeTaxChallanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeCreate + 1);
        IncomeTaxChallan testIncomeTaxChallan = incomeTaxChallanList.get(incomeTaxChallanList.size() - 1);
        assertThat(testIncomeTaxChallan.getChallanNo()).isEqualTo(DEFAULT_CHALLAN_NO);
        assertThat(testIncomeTaxChallan.getChallanDate()).isEqualTo(DEFAULT_CHALLAN_DATE);
        assertThat(testIncomeTaxChallan.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testIncomeTaxChallan.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testIncomeTaxChallan.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testIncomeTaxChallan.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createIncomeTaxChallanWithExistingId() throws Exception {
        // Create the IncomeTaxChallan with an existing ID
        incomeTaxChallan.setId(1L);
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        int databaseSizeBeforeCreate = incomeTaxChallanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncomeTaxChallanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChallanNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeTaxChallanRepository.findAll().size();
        // set the field null
        incomeTaxChallan.setChallanNo(null);

        // Create the IncomeTaxChallan, which fails.
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        restIncomeTaxChallanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChallanDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeTaxChallanRepository.findAll().size();
        // set the field null
        incomeTaxChallan.setChallanDate(null);

        // Create the IncomeTaxChallan, which fails.
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        restIncomeTaxChallanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeTaxChallanRepository.findAll().size();
        // set the field null
        incomeTaxChallan.setAmount(null);

        // Create the IncomeTaxChallan, which fails.
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        restIncomeTaxChallanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeTaxChallanRepository.findAll().size();
        // set the field null
        incomeTaxChallan.setMonth(null);

        // Create the IncomeTaxChallan, which fails.
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        restIncomeTaxChallanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeTaxChallanRepository.findAll().size();
        // set the field null
        incomeTaxChallan.setYear(null);

        // Create the IncomeTaxChallan, which fails.
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        restIncomeTaxChallanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIncomeTaxChallans() throws Exception {
        // Initialize the database
        incomeTaxChallanRepository.saveAndFlush(incomeTaxChallan);

        // Get all the incomeTaxChallanList
        restIncomeTaxChallanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incomeTaxChallan.getId().intValue())))
            .andExpect(jsonPath("$.[*].challanNo").value(hasItem(DEFAULT_CHALLAN_NO)))
            .andExpect(jsonPath("$.[*].challanDate").value(hasItem(DEFAULT_CHALLAN_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getIncomeTaxChallan() throws Exception {
        // Initialize the database
        incomeTaxChallanRepository.saveAndFlush(incomeTaxChallan);

        // Get the incomeTaxChallan
        restIncomeTaxChallanMockMvc
            .perform(get(ENTITY_API_URL_ID, incomeTaxChallan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incomeTaxChallan.getId().intValue()))
            .andExpect(jsonPath("$.challanNo").value(DEFAULT_CHALLAN_NO))
            .andExpect(jsonPath("$.challanDate").value(DEFAULT_CHALLAN_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getNonExistingIncomeTaxChallan() throws Exception {
        // Get the incomeTaxChallan
        restIncomeTaxChallanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIncomeTaxChallan() throws Exception {
        // Initialize the database
        incomeTaxChallanRepository.saveAndFlush(incomeTaxChallan);

        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();

        // Update the incomeTaxChallan
        IncomeTaxChallan updatedIncomeTaxChallan = incomeTaxChallanRepository.findById(incomeTaxChallan.getId()).get();
        // Disconnect from session so that the updates on updatedIncomeTaxChallan are not directly saved in db
        em.detach(updatedIncomeTaxChallan);
        updatedIncomeTaxChallan
            .challanNo(UPDATED_CHALLAN_NO)
            .challanDate(UPDATED_CHALLAN_DATE)
            .amount(UPDATED_AMOUNT)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .remarks(UPDATED_REMARKS);
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(updatedIncomeTaxChallan);

        restIncomeTaxChallanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incomeTaxChallanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isOk());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
        IncomeTaxChallan testIncomeTaxChallan = incomeTaxChallanList.get(incomeTaxChallanList.size() - 1);
        assertThat(testIncomeTaxChallan.getChallanNo()).isEqualTo(UPDATED_CHALLAN_NO);
        assertThat(testIncomeTaxChallan.getChallanDate()).isEqualTo(UPDATED_CHALLAN_DATE);
        assertThat(testIncomeTaxChallan.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testIncomeTaxChallan.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testIncomeTaxChallan.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testIncomeTaxChallan.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingIncomeTaxChallan() throws Exception {
        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();
        incomeTaxChallan.setId(count.incrementAndGet());

        // Create the IncomeTaxChallan
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeTaxChallanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incomeTaxChallanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncomeTaxChallan() throws Exception {
        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();
        incomeTaxChallan.setId(count.incrementAndGet());

        // Create the IncomeTaxChallan
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeTaxChallanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncomeTaxChallan() throws Exception {
        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();
        incomeTaxChallan.setId(count.incrementAndGet());

        // Create the IncomeTaxChallan
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeTaxChallanMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncomeTaxChallanWithPatch() throws Exception {
        // Initialize the database
        incomeTaxChallanRepository.saveAndFlush(incomeTaxChallan);

        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();

        // Update the incomeTaxChallan using partial update
        IncomeTaxChallan partialUpdatedIncomeTaxChallan = new IncomeTaxChallan();
        partialUpdatedIncomeTaxChallan.setId(incomeTaxChallan.getId());

        partialUpdatedIncomeTaxChallan.challanNo(UPDATED_CHALLAN_NO).challanDate(UPDATED_CHALLAN_DATE).year(UPDATED_YEAR);

        restIncomeTaxChallanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomeTaxChallan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncomeTaxChallan))
            )
            .andExpect(status().isOk());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
        IncomeTaxChallan testIncomeTaxChallan = incomeTaxChallanList.get(incomeTaxChallanList.size() - 1);
        assertThat(testIncomeTaxChallan.getChallanNo()).isEqualTo(UPDATED_CHALLAN_NO);
        assertThat(testIncomeTaxChallan.getChallanDate()).isEqualTo(UPDATED_CHALLAN_DATE);
        assertThat(testIncomeTaxChallan.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testIncomeTaxChallan.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testIncomeTaxChallan.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testIncomeTaxChallan.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateIncomeTaxChallanWithPatch() throws Exception {
        // Initialize the database
        incomeTaxChallanRepository.saveAndFlush(incomeTaxChallan);

        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();

        // Update the incomeTaxChallan using partial update
        IncomeTaxChallan partialUpdatedIncomeTaxChallan = new IncomeTaxChallan();
        partialUpdatedIncomeTaxChallan.setId(incomeTaxChallan.getId());

        partialUpdatedIncomeTaxChallan
            .challanNo(UPDATED_CHALLAN_NO)
            .challanDate(UPDATED_CHALLAN_DATE)
            .amount(UPDATED_AMOUNT)
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .remarks(UPDATED_REMARKS);

        restIncomeTaxChallanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncomeTaxChallan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncomeTaxChallan))
            )
            .andExpect(status().isOk());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
        IncomeTaxChallan testIncomeTaxChallan = incomeTaxChallanList.get(incomeTaxChallanList.size() - 1);
        assertThat(testIncomeTaxChallan.getChallanNo()).isEqualTo(UPDATED_CHALLAN_NO);
        assertThat(testIncomeTaxChallan.getChallanDate()).isEqualTo(UPDATED_CHALLAN_DATE);
        assertThat(testIncomeTaxChallan.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testIncomeTaxChallan.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testIncomeTaxChallan.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testIncomeTaxChallan.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingIncomeTaxChallan() throws Exception {
        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();
        incomeTaxChallan.setId(count.incrementAndGet());

        // Create the IncomeTaxChallan
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeTaxChallanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incomeTaxChallanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncomeTaxChallan() throws Exception {
        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();
        incomeTaxChallan.setId(count.incrementAndGet());

        // Create the IncomeTaxChallan
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeTaxChallanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncomeTaxChallan() throws Exception {
        int databaseSizeBeforeUpdate = incomeTaxChallanRepository.findAll().size();
        incomeTaxChallan.setId(count.incrementAndGet());

        // Create the IncomeTaxChallan
        IncomeTaxChallanDTO incomeTaxChallanDTO = incomeTaxChallanMapper.toDto(incomeTaxChallan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeTaxChallanMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incomeTaxChallanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IncomeTaxChallan in the database
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncomeTaxChallan() throws Exception {
        // Initialize the database
        incomeTaxChallanRepository.saveAndFlush(incomeTaxChallan);

        int databaseSizeBeforeDelete = incomeTaxChallanRepository.findAll().size();

        // Delete the incomeTaxChallan
        restIncomeTaxChallanMockMvc
            .perform(delete(ENTITY_API_URL_ID, incomeTaxChallan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IncomeTaxChallan> incomeTaxChallanList = incomeTaxChallanRepository.findAll();
        assertThat(incomeTaxChallanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
