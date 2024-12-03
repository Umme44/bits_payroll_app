package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.repository.IndividualArrearSalaryRepository;
import com.bits.hr.service.dto.IndividualArrearSalaryDTO;
import com.bits.hr.service.mapper.IndividualArrearSalaryMapper;
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
 * Integration tests for the {@link IndividualArrearSalaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndividualArrearSalaryResourceIT {

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EXISTING_BAND = "AAAAAAAAAA";
    private static final String UPDATED_EXISTING_BAND = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_BAND = "AAAAAAAAAA";
    private static final String UPDATED_NEW_BAND = "BBBBBBBBBB";

    private static final Double DEFAULT_EXISTING_GROSS = 1D;
    private static final Double UPDATED_EXISTING_GROSS = 2D;

    private static final Double DEFAULT_NEW_GROSS = 1D;
    private static final Double UPDATED_NEW_GROSS = 2D;

    private static final Double DEFAULT_INCREMENT = 1D;
    private static final Double UPDATED_INCREMENT = 2D;

    private static final Double DEFAULT_ARREAR_SALARY = 1D;
    private static final Double UPDATED_ARREAR_SALARY = 2D;

    private static final Double DEFAULT_ARREAR_PF_DEDUCTION = 1D;
    private static final Double UPDATED_ARREAR_PF_DEDUCTION = 2D;

    private static final Double DEFAULT_TAX_DEDUCTION = 1D;
    private static final Double UPDATED_TAX_DEDUCTION = 2D;

    private static final Double DEFAULT_NET_PAY = 1D;
    private static final Double UPDATED_NET_PAY = 2D;

    private static final Double DEFAULT_PF_CONTRIBUTION = 1D;
    private static final Double UPDATED_PF_CONTRIBUTION = 2D;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE_EFFECTIVE_FROM = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_EFFECTIVE_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_ARREAR_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_ARREAR_REMARKS = "BBBBBBBBBB";

    private static final Double DEFAULT_FESTIVAL_BONUS = 1D;
    private static final Double UPDATED_FESTIVAL_BONUS = 2D;

    private static final String ENTITY_API_URL = "/api/individual-arrear-salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IndividualArrearSalaryRepository individualArrearSalaryRepository;

    @Autowired
    private IndividualArrearSalaryMapper individualArrearSalaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndividualArrearSalaryMockMvc;

    private IndividualArrearSalary individualArrearSalary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndividualArrearSalary createEntity(EntityManager em) {
        IndividualArrearSalary individualArrearSalary = new IndividualArrearSalary()
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .existingBand(DEFAULT_EXISTING_BAND)
            .newBand(DEFAULT_NEW_BAND)
            .existingGross(DEFAULT_EXISTING_GROSS)
            .newGross(DEFAULT_NEW_GROSS)
            .increment(DEFAULT_INCREMENT)
            .arrearSalary(DEFAULT_ARREAR_SALARY)
            .arrearPfDeduction(DEFAULT_ARREAR_PF_DEDUCTION)
            .taxDeduction(DEFAULT_TAX_DEDUCTION)
            .netPay(DEFAULT_NET_PAY)
            .pfContribution(DEFAULT_PF_CONTRIBUTION)
            .title(DEFAULT_TITLE)
            .titleEffectiveFrom(DEFAULT_TITLE_EFFECTIVE_FROM)
            .arrearRemarks(DEFAULT_ARREAR_REMARKS)
            .festivalBonus(DEFAULT_FESTIVAL_BONUS);
        return individualArrearSalary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndividualArrearSalary createUpdatedEntity(EntityManager em) {
        IndividualArrearSalary individualArrearSalary = new IndividualArrearSalary()
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .existingBand(UPDATED_EXISTING_BAND)
            .newBand(UPDATED_NEW_BAND)
            .existingGross(UPDATED_EXISTING_GROSS)
            .newGross(UPDATED_NEW_GROSS)
            .increment(UPDATED_INCREMENT)
            .arrearSalary(UPDATED_ARREAR_SALARY)
            .arrearPfDeduction(UPDATED_ARREAR_PF_DEDUCTION)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .netPay(UPDATED_NET_PAY)
            .pfContribution(UPDATED_PF_CONTRIBUTION)
            .title(UPDATED_TITLE)
            .titleEffectiveFrom(UPDATED_TITLE_EFFECTIVE_FROM)
            .arrearRemarks(UPDATED_ARREAR_REMARKS)
            .festivalBonus(UPDATED_FESTIVAL_BONUS);
        return individualArrearSalary;
    }

    @BeforeEach
    public void initTest() {
        individualArrearSalary = createEntity(em);
    }

    @Test
    @Transactional
    void createIndividualArrearSalary() throws Exception {
        int databaseSizeBeforeCreate = individualArrearSalaryRepository.findAll().size();
        // Create the IndividualArrearSalary
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(individualArrearSalary);
        restIndividualArrearSalaryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeCreate + 1);
        IndividualArrearSalary testIndividualArrearSalary = individualArrearSalaryList.get(individualArrearSalaryList.size() - 1);
        assertThat(testIndividualArrearSalary.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testIndividualArrearSalary.getExistingBand()).isEqualTo(DEFAULT_EXISTING_BAND);
        assertThat(testIndividualArrearSalary.getNewBand()).isEqualTo(DEFAULT_NEW_BAND);
        assertThat(testIndividualArrearSalary.getExistingGross()).isEqualTo(DEFAULT_EXISTING_GROSS);
        assertThat(testIndividualArrearSalary.getNewGross()).isEqualTo(DEFAULT_NEW_GROSS);
        assertThat(testIndividualArrearSalary.getIncrement()).isEqualTo(DEFAULT_INCREMENT);
        assertThat(testIndividualArrearSalary.getArrearSalary()).isEqualTo(DEFAULT_ARREAR_SALARY);
        assertThat(testIndividualArrearSalary.getArrearPfDeduction()).isEqualTo(DEFAULT_ARREAR_PF_DEDUCTION);
        assertThat(testIndividualArrearSalary.getTaxDeduction()).isEqualTo(DEFAULT_TAX_DEDUCTION);
        assertThat(testIndividualArrearSalary.getNetPay()).isEqualTo(DEFAULT_NET_PAY);
        assertThat(testIndividualArrearSalary.getPfContribution()).isEqualTo(DEFAULT_PF_CONTRIBUTION);
        assertThat(testIndividualArrearSalary.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testIndividualArrearSalary.getTitleEffectiveFrom()).isEqualTo(DEFAULT_TITLE_EFFECTIVE_FROM);
        assertThat(testIndividualArrearSalary.getArrearRemarks()).isEqualTo(DEFAULT_ARREAR_REMARKS);
        assertThat(testIndividualArrearSalary.getFestivalBonus()).isEqualTo(DEFAULT_FESTIVAL_BONUS);
    }

    @Test
    @Transactional
    void createIndividualArrearSalaryWithExistingId() throws Exception {
        // Create the IndividualArrearSalary with an existing ID
        individualArrearSalary.setId(1L);
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(individualArrearSalary);

        int databaseSizeBeforeCreate = individualArrearSalaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndividualArrearSalaryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIndividualArrearSalaries() throws Exception {
        // Initialize the database
        individualArrearSalaryRepository.saveAndFlush(individualArrearSalary);

        // Get all the individualArrearSalaryList
        restIndividualArrearSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(individualArrearSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].existingBand").value(hasItem(DEFAULT_EXISTING_BAND)))
            .andExpect(jsonPath("$.[*].newBand").value(hasItem(DEFAULT_NEW_BAND)))
            .andExpect(jsonPath("$.[*].existingGross").value(hasItem(DEFAULT_EXISTING_GROSS.doubleValue())))
            .andExpect(jsonPath("$.[*].newGross").value(hasItem(DEFAULT_NEW_GROSS.doubleValue())))
            .andExpect(jsonPath("$.[*].increment").value(hasItem(DEFAULT_INCREMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].arrearSalary").value(hasItem(DEFAULT_ARREAR_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].arrearPfDeduction").value(hasItem(DEFAULT_ARREAR_PF_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].taxDeduction").value(hasItem(DEFAULT_TAX_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].netPay").value(hasItem(DEFAULT_NET_PAY.doubleValue())))
            .andExpect(jsonPath("$.[*].pfContribution").value(hasItem(DEFAULT_PF_CONTRIBUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].titleEffectiveFrom").value(hasItem(DEFAULT_TITLE_EFFECTIVE_FROM)))
            .andExpect(jsonPath("$.[*].arrearRemarks").value(hasItem(DEFAULT_ARREAR_REMARKS)))
            .andExpect(jsonPath("$.[*].festivalBonus").value(hasItem(DEFAULT_FESTIVAL_BONUS.doubleValue())));
    }

    @Test
    @Transactional
    void getIndividualArrearSalary() throws Exception {
        // Initialize the database
        individualArrearSalaryRepository.saveAndFlush(individualArrearSalary);

        // Get the individualArrearSalary
        restIndividualArrearSalaryMockMvc
            .perform(get(ENTITY_API_URL_ID, individualArrearSalary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(individualArrearSalary.getId().intValue()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.existingBand").value(DEFAULT_EXISTING_BAND))
            .andExpect(jsonPath("$.newBand").value(DEFAULT_NEW_BAND))
            .andExpect(jsonPath("$.existingGross").value(DEFAULT_EXISTING_GROSS.doubleValue()))
            .andExpect(jsonPath("$.newGross").value(DEFAULT_NEW_GROSS.doubleValue()))
            .andExpect(jsonPath("$.increment").value(DEFAULT_INCREMENT.doubleValue()))
            .andExpect(jsonPath("$.arrearSalary").value(DEFAULT_ARREAR_SALARY.doubleValue()))
            .andExpect(jsonPath("$.arrearPfDeduction").value(DEFAULT_ARREAR_PF_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.taxDeduction").value(DEFAULT_TAX_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.netPay").value(DEFAULT_NET_PAY.doubleValue()))
            .andExpect(jsonPath("$.pfContribution").value(DEFAULT_PF_CONTRIBUTION.doubleValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.titleEffectiveFrom").value(DEFAULT_TITLE_EFFECTIVE_FROM))
            .andExpect(jsonPath("$.arrearRemarks").value(DEFAULT_ARREAR_REMARKS))
            .andExpect(jsonPath("$.festivalBonus").value(DEFAULT_FESTIVAL_BONUS.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingIndividualArrearSalary() throws Exception {
        // Get the individualArrearSalary
        restIndividualArrearSalaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndividualArrearSalary() throws Exception {
        // Initialize the database
        individualArrearSalaryRepository.saveAndFlush(individualArrearSalary);

        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();

        // Update the individualArrearSalary
        IndividualArrearSalary updatedIndividualArrearSalary = individualArrearSalaryRepository
            .findById(individualArrearSalary.getId())
            .get();
        // Disconnect from session so that the updates on updatedIndividualArrearSalary are not directly saved in db
        em.detach(updatedIndividualArrearSalary);
        updatedIndividualArrearSalary
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .existingBand(UPDATED_EXISTING_BAND)
            .newBand(UPDATED_NEW_BAND)
            .existingGross(UPDATED_EXISTING_GROSS)
            .newGross(UPDATED_NEW_GROSS)
            .increment(UPDATED_INCREMENT)
            .arrearSalary(UPDATED_ARREAR_SALARY)
            .arrearPfDeduction(UPDATED_ARREAR_PF_DEDUCTION)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .netPay(UPDATED_NET_PAY)
            .pfContribution(UPDATED_PF_CONTRIBUTION)
            .title(UPDATED_TITLE)
            .titleEffectiveFrom(UPDATED_TITLE_EFFECTIVE_FROM)
            .arrearRemarks(UPDATED_ARREAR_REMARKS)
            .festivalBonus(UPDATED_FESTIVAL_BONUS);
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(updatedIndividualArrearSalary);

        restIndividualArrearSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, individualArrearSalaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
        IndividualArrearSalary testIndividualArrearSalary = individualArrearSalaryList.get(individualArrearSalaryList.size() - 1);
        assertThat(testIndividualArrearSalary.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testIndividualArrearSalary.getExistingBand()).isEqualTo(UPDATED_EXISTING_BAND);
        assertThat(testIndividualArrearSalary.getNewBand()).isEqualTo(UPDATED_NEW_BAND);
        assertThat(testIndividualArrearSalary.getExistingGross()).isEqualTo(UPDATED_EXISTING_GROSS);
        assertThat(testIndividualArrearSalary.getNewGross()).isEqualTo(UPDATED_NEW_GROSS);
        assertThat(testIndividualArrearSalary.getIncrement()).isEqualTo(UPDATED_INCREMENT);
        assertThat(testIndividualArrearSalary.getArrearSalary()).isEqualTo(UPDATED_ARREAR_SALARY);
        assertThat(testIndividualArrearSalary.getArrearPfDeduction()).isEqualTo(UPDATED_ARREAR_PF_DEDUCTION);
        assertThat(testIndividualArrearSalary.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testIndividualArrearSalary.getNetPay()).isEqualTo(UPDATED_NET_PAY);
        assertThat(testIndividualArrearSalary.getPfContribution()).isEqualTo(UPDATED_PF_CONTRIBUTION);
        assertThat(testIndividualArrearSalary.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIndividualArrearSalary.getTitleEffectiveFrom()).isEqualTo(UPDATED_TITLE_EFFECTIVE_FROM);
        assertThat(testIndividualArrearSalary.getArrearRemarks()).isEqualTo(UPDATED_ARREAR_REMARKS);
        assertThat(testIndividualArrearSalary.getFestivalBonus()).isEqualTo(UPDATED_FESTIVAL_BONUS);
    }

    @Test
    @Transactional
    void putNonExistingIndividualArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();
        individualArrearSalary.setId(count.incrementAndGet());

        // Create the IndividualArrearSalary
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(individualArrearSalary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndividualArrearSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, individualArrearSalaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndividualArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();
        individualArrearSalary.setId(count.incrementAndGet());

        // Create the IndividualArrearSalary
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(individualArrearSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualArrearSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndividualArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();
        individualArrearSalary.setId(count.incrementAndGet());

        // Create the IndividualArrearSalary
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(individualArrearSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualArrearSalaryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndividualArrearSalaryWithPatch() throws Exception {
        // Initialize the database
        individualArrearSalaryRepository.saveAndFlush(individualArrearSalary);

        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();

        // Update the individualArrearSalary using partial update
        IndividualArrearSalary partialUpdatedIndividualArrearSalary = new IndividualArrearSalary();
        partialUpdatedIndividualArrearSalary.setId(individualArrearSalary.getId());

        partialUpdatedIndividualArrearSalary
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .existingBand(UPDATED_EXISTING_BAND)
            .newGross(UPDATED_NEW_GROSS)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .netPay(UPDATED_NET_PAY)
            .titleEffectiveFrom(UPDATED_TITLE_EFFECTIVE_FROM);

        restIndividualArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndividualArrearSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndividualArrearSalary))
            )
            .andExpect(status().isOk());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
        IndividualArrearSalary testIndividualArrearSalary = individualArrearSalaryList.get(individualArrearSalaryList.size() - 1);
        assertThat(testIndividualArrearSalary.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testIndividualArrearSalary.getExistingBand()).isEqualTo(UPDATED_EXISTING_BAND);
        assertThat(testIndividualArrearSalary.getNewBand()).isEqualTo(DEFAULT_NEW_BAND);
        assertThat(testIndividualArrearSalary.getExistingGross()).isEqualTo(DEFAULT_EXISTING_GROSS);
        assertThat(testIndividualArrearSalary.getNewGross()).isEqualTo(UPDATED_NEW_GROSS);
        assertThat(testIndividualArrearSalary.getIncrement()).isEqualTo(DEFAULT_INCREMENT);
        assertThat(testIndividualArrearSalary.getArrearSalary()).isEqualTo(DEFAULT_ARREAR_SALARY);
        assertThat(testIndividualArrearSalary.getArrearPfDeduction()).isEqualTo(DEFAULT_ARREAR_PF_DEDUCTION);
        assertThat(testIndividualArrearSalary.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testIndividualArrearSalary.getNetPay()).isEqualTo(UPDATED_NET_PAY);
        assertThat(testIndividualArrearSalary.getPfContribution()).isEqualTo(DEFAULT_PF_CONTRIBUTION);
        assertThat(testIndividualArrearSalary.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testIndividualArrearSalary.getTitleEffectiveFrom()).isEqualTo(UPDATED_TITLE_EFFECTIVE_FROM);
        assertThat(testIndividualArrearSalary.getArrearRemarks()).isEqualTo(DEFAULT_ARREAR_REMARKS);
        assertThat(testIndividualArrearSalary.getFestivalBonus()).isEqualTo(DEFAULT_FESTIVAL_BONUS);
    }

    @Test
    @Transactional
    void fullUpdateIndividualArrearSalaryWithPatch() throws Exception {
        // Initialize the database
        individualArrearSalaryRepository.saveAndFlush(individualArrearSalary);

        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();

        // Update the individualArrearSalary using partial update
        IndividualArrearSalary partialUpdatedIndividualArrearSalary = new IndividualArrearSalary();
        partialUpdatedIndividualArrearSalary.setId(individualArrearSalary.getId());

        partialUpdatedIndividualArrearSalary
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .existingBand(UPDATED_EXISTING_BAND)
            .newBand(UPDATED_NEW_BAND)
            .existingGross(UPDATED_EXISTING_GROSS)
            .newGross(UPDATED_NEW_GROSS)
            .increment(UPDATED_INCREMENT)
            .arrearSalary(UPDATED_ARREAR_SALARY)
            .arrearPfDeduction(UPDATED_ARREAR_PF_DEDUCTION)
            .taxDeduction(UPDATED_TAX_DEDUCTION)
            .netPay(UPDATED_NET_PAY)
            .pfContribution(UPDATED_PF_CONTRIBUTION)
            .title(UPDATED_TITLE)
            .titleEffectiveFrom(UPDATED_TITLE_EFFECTIVE_FROM)
            .arrearRemarks(UPDATED_ARREAR_REMARKS)
            .festivalBonus(UPDATED_FESTIVAL_BONUS);

        restIndividualArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndividualArrearSalary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIndividualArrearSalary))
            )
            .andExpect(status().isOk());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
        IndividualArrearSalary testIndividualArrearSalary = individualArrearSalaryList.get(individualArrearSalaryList.size() - 1);
        assertThat(testIndividualArrearSalary.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testIndividualArrearSalary.getExistingBand()).isEqualTo(UPDATED_EXISTING_BAND);
        assertThat(testIndividualArrearSalary.getNewBand()).isEqualTo(UPDATED_NEW_BAND);
        assertThat(testIndividualArrearSalary.getExistingGross()).isEqualTo(UPDATED_EXISTING_GROSS);
        assertThat(testIndividualArrearSalary.getNewGross()).isEqualTo(UPDATED_NEW_GROSS);
        assertThat(testIndividualArrearSalary.getIncrement()).isEqualTo(UPDATED_INCREMENT);
        assertThat(testIndividualArrearSalary.getArrearSalary()).isEqualTo(UPDATED_ARREAR_SALARY);
        assertThat(testIndividualArrearSalary.getArrearPfDeduction()).isEqualTo(UPDATED_ARREAR_PF_DEDUCTION);
        assertThat(testIndividualArrearSalary.getTaxDeduction()).isEqualTo(UPDATED_TAX_DEDUCTION);
        assertThat(testIndividualArrearSalary.getNetPay()).isEqualTo(UPDATED_NET_PAY);
        assertThat(testIndividualArrearSalary.getPfContribution()).isEqualTo(UPDATED_PF_CONTRIBUTION);
        assertThat(testIndividualArrearSalary.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIndividualArrearSalary.getTitleEffectiveFrom()).isEqualTo(UPDATED_TITLE_EFFECTIVE_FROM);
        assertThat(testIndividualArrearSalary.getArrearRemarks()).isEqualTo(UPDATED_ARREAR_REMARKS);
        assertThat(testIndividualArrearSalary.getFestivalBonus()).isEqualTo(UPDATED_FESTIVAL_BONUS);
    }

    @Test
    @Transactional
    void patchNonExistingIndividualArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();
        individualArrearSalary.setId(count.incrementAndGet());

        // Create the IndividualArrearSalary
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(individualArrearSalary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndividualArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, individualArrearSalaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndividualArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();
        individualArrearSalary.setId(count.incrementAndGet());

        // Create the IndividualArrearSalary
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(individualArrearSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndividualArrearSalary() throws Exception {
        int databaseSizeBeforeUpdate = individualArrearSalaryRepository.findAll().size();
        individualArrearSalary.setId(count.incrementAndGet());

        // Create the IndividualArrearSalary
        IndividualArrearSalaryDTO individualArrearSalaryDTO = individualArrearSalaryMapper.toDto(individualArrearSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualArrearSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(individualArrearSalaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndividualArrearSalary in the database
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndividualArrearSalary() throws Exception {
        // Initialize the database
        individualArrearSalaryRepository.saveAndFlush(individualArrearSalary);

        int databaseSizeBeforeDelete = individualArrearSalaryRepository.findAll().size();

        // Delete the individualArrearSalary
        restIndividualArrearSalaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, individualArrearSalary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IndividualArrearSalary> individualArrearSalaryList = individualArrearSalaryRepository.findAll();
        assertThat(individualArrearSalaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
