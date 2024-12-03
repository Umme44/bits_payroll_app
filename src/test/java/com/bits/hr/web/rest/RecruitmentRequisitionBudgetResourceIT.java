package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.RecruitmentRequisitionBudget;
import com.bits.hr.repository.RecruitmentRequisitionBudgetRepository;
import com.bits.hr.service.dto.RecruitmentRequisitionBudgetDTO;
import com.bits.hr.service.mapper.RecruitmentRequisitionBudgetMapper;
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
 * Integration tests for the {@link RecruitmentRequisitionBudgetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecruitmentRequisitionBudgetResourceIT {

    private static final Long DEFAULT_YEAR = 1970L;
    private static final Long UPDATED_YEAR = 1971L;

    private static final Long DEFAULT_BUDGET = 1L;
    private static final Long UPDATED_BUDGET = 2L;

    private static final Long DEFAULT_REMAINING_BUDGET = 1L;
    private static final Long UPDATED_REMAINING_BUDGET = 2L;

    private static final Long DEFAULT_REMAINING_MANPOWER = 1L;
    private static final Long UPDATED_REMAINING_MANPOWER = 2L;

    private static final String ENTITY_API_URL = "/api/recruitment-requisition-budgets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecruitmentRequisitionBudgetRepository recruitmentRequisitionBudgetRepository;

    @Autowired
    private RecruitmentRequisitionBudgetMapper recruitmentRequisitionBudgetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecruitmentRequisitionBudgetMockMvc;

    private RecruitmentRequisitionBudget recruitmentRequisitionBudget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisitionBudget createEntity(EntityManager em) {
        RecruitmentRequisitionBudget recruitmentRequisitionBudget = new RecruitmentRequisitionBudget()
            .year(DEFAULT_YEAR)
            .budget(DEFAULT_BUDGET)
            .remainingBudget(DEFAULT_REMAINING_BUDGET)
            .remainingManpower(DEFAULT_REMAINING_MANPOWER);
        return recruitmentRequisitionBudget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentRequisitionBudget createUpdatedEntity(EntityManager em) {
        RecruitmentRequisitionBudget recruitmentRequisitionBudget = new RecruitmentRequisitionBudget()
            .year(UPDATED_YEAR)
            .budget(UPDATED_BUDGET)
            .remainingBudget(UPDATED_REMAINING_BUDGET)
            .remainingManpower(UPDATED_REMAINING_MANPOWER);
        return recruitmentRequisitionBudget;
    }

    @BeforeEach
    public void initTest() {
        recruitmentRequisitionBudget = createEntity(em);
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionBudget() throws Exception {
        int databaseSizeBeforeCreate = recruitmentRequisitionBudgetRepository.findAll().size();
        // Create the RecruitmentRequisitionBudget
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );
        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeCreate + 1);
        RecruitmentRequisitionBudget testRecruitmentRequisitionBudget = recruitmentRequisitionBudgetList.get(
            recruitmentRequisitionBudgetList.size() - 1
        );
        assertThat(testRecruitmentRequisitionBudget.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testRecruitmentRequisitionBudget.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testRecruitmentRequisitionBudget.getRemainingBudget()).isEqualTo(DEFAULT_REMAINING_BUDGET);
        assertThat(testRecruitmentRequisitionBudget.getRemainingManpower()).isEqualTo(DEFAULT_REMAINING_MANPOWER);
    }

    @Test
    @Transactional
    void createRecruitmentRequisitionBudgetWithExistingId() throws Exception {
        // Create the RecruitmentRequisitionBudget with an existing ID
        recruitmentRequisitionBudget.setId(1L);
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        int databaseSizeBeforeCreate = recruitmentRequisitionBudgetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionBudgetRepository.findAll().size();
        // set the field null
        recruitmentRequisitionBudget.setYear(null);

        // Create the RecruitmentRequisitionBudget, which fails.
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBudgetIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionBudgetRepository.findAll().size();
        // set the field null
        recruitmentRequisitionBudget.setBudget(null);

        // Create the RecruitmentRequisitionBudget, which fails.
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemainingBudgetIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionBudgetRepository.findAll().size();
        // set the field null
        recruitmentRequisitionBudget.setRemainingBudget(null);

        // Create the RecruitmentRequisitionBudget, which fails.
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemainingManpowerIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentRequisitionBudgetRepository.findAll().size();
        // set the field null
        recruitmentRequisitionBudget.setRemainingManpower(null);

        // Create the RecruitmentRequisitionBudget, which fails.
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecruitmentRequisitionBudgets() throws Exception {
        // Initialize the database
        recruitmentRequisitionBudgetRepository.saveAndFlush(recruitmentRequisitionBudget);

        // Get all the recruitmentRequisitionBudgetList
        restRecruitmentRequisitionBudgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentRequisitionBudget.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.intValue())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.intValue())))
            .andExpect(jsonPath("$.[*].remainingBudget").value(hasItem(DEFAULT_REMAINING_BUDGET.intValue())))
            .andExpect(jsonPath("$.[*].remainingManpower").value(hasItem(DEFAULT_REMAINING_MANPOWER.intValue())));
    }

    @Test
    @Transactional
    void getRecruitmentRequisitionBudget() throws Exception {
        // Initialize the database
        recruitmentRequisitionBudgetRepository.saveAndFlush(recruitmentRequisitionBudget);

        // Get the recruitmentRequisitionBudget
        restRecruitmentRequisitionBudgetMockMvc
            .perform(get(ENTITY_API_URL_ID, recruitmentRequisitionBudget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recruitmentRequisitionBudget.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.intValue()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.intValue()))
            .andExpect(jsonPath("$.remainingBudget").value(DEFAULT_REMAINING_BUDGET.intValue()))
            .andExpect(jsonPath("$.remainingManpower").value(DEFAULT_REMAINING_MANPOWER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRecruitmentRequisitionBudget() throws Exception {
        // Get the recruitmentRequisitionBudget
        restRecruitmentRequisitionBudgetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecruitmentRequisitionBudget() throws Exception {
        // Initialize the database
        recruitmentRequisitionBudgetRepository.saveAndFlush(recruitmentRequisitionBudget);

        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();

        // Update the recruitmentRequisitionBudget
        RecruitmentRequisitionBudget updatedRecruitmentRequisitionBudget = recruitmentRequisitionBudgetRepository
            .findById(recruitmentRequisitionBudget.getId())
            .get();
        // Disconnect from session so that the updates on updatedRecruitmentRequisitionBudget are not directly saved in db
        em.detach(updatedRecruitmentRequisitionBudget);
        updatedRecruitmentRequisitionBudget
            .year(UPDATED_YEAR)
            .budget(UPDATED_BUDGET)
            .remainingBudget(UPDATED_REMAINING_BUDGET)
            .remainingManpower(UPDATED_REMAINING_MANPOWER);
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            updatedRecruitmentRequisitionBudget
        );

        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionBudgetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
        RecruitmentRequisitionBudget testRecruitmentRequisitionBudget = recruitmentRequisitionBudgetList.get(
            recruitmentRequisitionBudgetList.size() - 1
        );
        assertThat(testRecruitmentRequisitionBudget.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testRecruitmentRequisitionBudget.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testRecruitmentRequisitionBudget.getRemainingBudget()).isEqualTo(UPDATED_REMAINING_BUDGET);
        assertThat(testRecruitmentRequisitionBudget.getRemainingManpower()).isEqualTo(UPDATED_REMAINING_MANPOWER);
    }

    @Test
    @Transactional
    void putNonExistingRecruitmentRequisitionBudget() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();
        recruitmentRequisitionBudget.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionBudget
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recruitmentRequisitionBudgetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecruitmentRequisitionBudget() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();
        recruitmentRequisitionBudget.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionBudget
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecruitmentRequisitionBudget() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();
        recruitmentRequisitionBudget.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionBudget
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecruitmentRequisitionBudgetWithPatch() throws Exception {
        // Initialize the database
        recruitmentRequisitionBudgetRepository.saveAndFlush(recruitmentRequisitionBudget);

        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();

        // Update the recruitmentRequisitionBudget using partial update
        RecruitmentRequisitionBudget partialUpdatedRecruitmentRequisitionBudget = new RecruitmentRequisitionBudget();
        partialUpdatedRecruitmentRequisitionBudget.setId(recruitmentRequisitionBudget.getId());

        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisitionBudget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecruitmentRequisitionBudget))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
        RecruitmentRequisitionBudget testRecruitmentRequisitionBudget = recruitmentRequisitionBudgetList.get(
            recruitmentRequisitionBudgetList.size() - 1
        );
        assertThat(testRecruitmentRequisitionBudget.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testRecruitmentRequisitionBudget.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testRecruitmentRequisitionBudget.getRemainingBudget()).isEqualTo(DEFAULT_REMAINING_BUDGET);
        assertThat(testRecruitmentRequisitionBudget.getRemainingManpower()).isEqualTo(DEFAULT_REMAINING_MANPOWER);
    }

    @Test
    @Transactional
    void fullUpdateRecruitmentRequisitionBudgetWithPatch() throws Exception {
        // Initialize the database
        recruitmentRequisitionBudgetRepository.saveAndFlush(recruitmentRequisitionBudget);

        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();

        // Update the recruitmentRequisitionBudget using partial update
        RecruitmentRequisitionBudget partialUpdatedRecruitmentRequisitionBudget = new RecruitmentRequisitionBudget();
        partialUpdatedRecruitmentRequisitionBudget.setId(recruitmentRequisitionBudget.getId());

        partialUpdatedRecruitmentRequisitionBudget
            .year(UPDATED_YEAR)
            .budget(UPDATED_BUDGET)
            .remainingBudget(UPDATED_REMAINING_BUDGET)
            .remainingManpower(UPDATED_REMAINING_MANPOWER);

        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecruitmentRequisitionBudget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecruitmentRequisitionBudget))
            )
            .andExpect(status().isOk());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
        RecruitmentRequisitionBudget testRecruitmentRequisitionBudget = recruitmentRequisitionBudgetList.get(
            recruitmentRequisitionBudgetList.size() - 1
        );
        assertThat(testRecruitmentRequisitionBudget.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testRecruitmentRequisitionBudget.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testRecruitmentRequisitionBudget.getRemainingBudget()).isEqualTo(UPDATED_REMAINING_BUDGET);
        assertThat(testRecruitmentRequisitionBudget.getRemainingManpower()).isEqualTo(UPDATED_REMAINING_MANPOWER);
    }

    @Test
    @Transactional
    void patchNonExistingRecruitmentRequisitionBudget() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();
        recruitmentRequisitionBudget.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionBudget
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recruitmentRequisitionBudgetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecruitmentRequisitionBudget() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();
        recruitmentRequisitionBudget.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionBudget
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecruitmentRequisitionBudget() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentRequisitionBudgetRepository.findAll().size();
        recruitmentRequisitionBudget.setId(count.incrementAndGet());

        // Create the RecruitmentRequisitionBudget
        RecruitmentRequisitionBudgetDTO recruitmentRequisitionBudgetDTO = recruitmentRequisitionBudgetMapper.toDto(
            recruitmentRequisitionBudget
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecruitmentRequisitionBudgetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recruitmentRequisitionBudgetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecruitmentRequisitionBudget in the database
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecruitmentRequisitionBudget() throws Exception {
        // Initialize the database
        recruitmentRequisitionBudgetRepository.saveAndFlush(recruitmentRequisitionBudget);

        int databaseSizeBeforeDelete = recruitmentRequisitionBudgetRepository.findAll().size();

        // Delete the recruitmentRequisitionBudget
        restRecruitmentRequisitionBudgetMockMvc
            .perform(delete(ENTITY_API_URL_ID, recruitmentRequisitionBudget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecruitmentRequisitionBudget> recruitmentRequisitionBudgetList = recruitmentRequisitionBudgetRepository.findAll();
        assertThat(recruitmentRequisitionBudgetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
