package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.service.dto.GeneratedSalaryHistoryDTO;
import com.bits.hr.service.mapper.GeneratedSalaryHistoryMapper;
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
 * Integration tests for the {@link GeneratedSalaryHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GeneratedSalaryHistoryResourceIT {

    private static final Integer DEFAULT_YEAR = 1990;
    private static final Integer UPDATED_YEAR = 1991;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/generated-salary-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GeneratedSalaryHistoryRepository generatedSalaryHistoryRepository;

    @Autowired
    private GeneratedSalaryHistoryMapper generatedSalaryHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGeneratedSalaryHistoryMockMvc;

    private GeneratedSalaryHistory generatedSalaryHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneratedSalaryHistory createEntity(EntityManager em) {
        GeneratedSalaryHistory generatedSalaryHistory = new GeneratedSalaryHistory()
            .year(DEFAULT_YEAR)
            .month(DEFAULT_MONTH)
            .status(DEFAULT_STATUS);
        return generatedSalaryHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneratedSalaryHistory createUpdatedEntity(EntityManager em) {
        GeneratedSalaryHistory generatedSalaryHistory = new GeneratedSalaryHistory()
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .status(UPDATED_STATUS);
        return generatedSalaryHistory;
    }

    @BeforeEach
    public void initTest() {
        generatedSalaryHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createGeneratedSalaryHistory() throws Exception {
        int databaseSizeBeforeCreate = generatedSalaryHistoryRepository.findAll().size();
        // Create the GeneratedSalaryHistory
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(generatedSalaryHistory);
        restGeneratedSalaryHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        GeneratedSalaryHistory testGeneratedSalaryHistory = generatedSalaryHistoryList.get(generatedSalaryHistoryList.size() - 1);
        assertThat(testGeneratedSalaryHistory.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testGeneratedSalaryHistory.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testGeneratedSalaryHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createGeneratedSalaryHistoryWithExistingId() throws Exception {
        // Create the GeneratedSalaryHistory with an existing ID
        generatedSalaryHistory.setId(1L);
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(generatedSalaryHistory);

        int databaseSizeBeforeCreate = generatedSalaryHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneratedSalaryHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGeneratedSalaryHistories() throws Exception {
        // Initialize the database
        generatedSalaryHistoryRepository.saveAndFlush(generatedSalaryHistory);

        // Get all the generatedSalaryHistoryList
        restGeneratedSalaryHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generatedSalaryHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getGeneratedSalaryHistory() throws Exception {
        // Initialize the database
        generatedSalaryHistoryRepository.saveAndFlush(generatedSalaryHistory);

        // Get the generatedSalaryHistory
        restGeneratedSalaryHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, generatedSalaryHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(generatedSalaryHistory.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGeneratedSalaryHistory() throws Exception {
        // Get the generatedSalaryHistory
        restGeneratedSalaryHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGeneratedSalaryHistory() throws Exception {
        // Initialize the database
        generatedSalaryHistoryRepository.saveAndFlush(generatedSalaryHistory);

        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();

        // Update the generatedSalaryHistory
        GeneratedSalaryHistory updatedGeneratedSalaryHistory = generatedSalaryHistoryRepository
            .findById(generatedSalaryHistory.getId())
            .get();
        // Disconnect from session so that the updates on updatedGeneratedSalaryHistory are not directly saved in db
        em.detach(updatedGeneratedSalaryHistory);
        updatedGeneratedSalaryHistory.year(UPDATED_YEAR).month(UPDATED_MONTH).status(UPDATED_STATUS);
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(updatedGeneratedSalaryHistory);

        restGeneratedSalaryHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generatedSalaryHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
        GeneratedSalaryHistory testGeneratedSalaryHistory = generatedSalaryHistoryList.get(generatedSalaryHistoryList.size() - 1);
        assertThat(testGeneratedSalaryHistory.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testGeneratedSalaryHistory.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testGeneratedSalaryHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingGeneratedSalaryHistory() throws Exception {
        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();
        generatedSalaryHistory.setId(count.incrementAndGet());

        // Create the GeneratedSalaryHistory
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(generatedSalaryHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneratedSalaryHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generatedSalaryHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGeneratedSalaryHistory() throws Exception {
        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();
        generatedSalaryHistory.setId(count.incrementAndGet());

        // Create the GeneratedSalaryHistory
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(generatedSalaryHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratedSalaryHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGeneratedSalaryHistory() throws Exception {
        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();
        generatedSalaryHistory.setId(count.incrementAndGet());

        // Create the GeneratedSalaryHistory
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(generatedSalaryHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratedSalaryHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGeneratedSalaryHistoryWithPatch() throws Exception {
        // Initialize the database
        generatedSalaryHistoryRepository.saveAndFlush(generatedSalaryHistory);

        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();

        // Update the generatedSalaryHistory using partial update
        GeneratedSalaryHistory partialUpdatedGeneratedSalaryHistory = new GeneratedSalaryHistory();
        partialUpdatedGeneratedSalaryHistory.setId(generatedSalaryHistory.getId());

        partialUpdatedGeneratedSalaryHistory.month(UPDATED_MONTH).status(UPDATED_STATUS);

        restGeneratedSalaryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneratedSalaryHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneratedSalaryHistory))
            )
            .andExpect(status().isOk());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
        GeneratedSalaryHistory testGeneratedSalaryHistory = generatedSalaryHistoryList.get(generatedSalaryHistoryList.size() - 1);
        assertThat(testGeneratedSalaryHistory.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testGeneratedSalaryHistory.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testGeneratedSalaryHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateGeneratedSalaryHistoryWithPatch() throws Exception {
        // Initialize the database
        generatedSalaryHistoryRepository.saveAndFlush(generatedSalaryHistory);

        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();

        // Update the generatedSalaryHistory using partial update
        GeneratedSalaryHistory partialUpdatedGeneratedSalaryHistory = new GeneratedSalaryHistory();
        partialUpdatedGeneratedSalaryHistory.setId(generatedSalaryHistory.getId());

        partialUpdatedGeneratedSalaryHistory.year(UPDATED_YEAR).month(UPDATED_MONTH).status(UPDATED_STATUS);

        restGeneratedSalaryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGeneratedSalaryHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGeneratedSalaryHistory))
            )
            .andExpect(status().isOk());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
        GeneratedSalaryHistory testGeneratedSalaryHistory = generatedSalaryHistoryList.get(generatedSalaryHistoryList.size() - 1);
        assertThat(testGeneratedSalaryHistory.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testGeneratedSalaryHistory.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testGeneratedSalaryHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingGeneratedSalaryHistory() throws Exception {
        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();
        generatedSalaryHistory.setId(count.incrementAndGet());

        // Create the GeneratedSalaryHistory
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(generatedSalaryHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneratedSalaryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, generatedSalaryHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGeneratedSalaryHistory() throws Exception {
        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();
        generatedSalaryHistory.setId(count.incrementAndGet());

        // Create the GeneratedSalaryHistory
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(generatedSalaryHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratedSalaryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGeneratedSalaryHistory() throws Exception {
        int databaseSizeBeforeUpdate = generatedSalaryHistoryRepository.findAll().size();
        generatedSalaryHistory.setId(count.incrementAndGet());

        // Create the GeneratedSalaryHistory
        GeneratedSalaryHistoryDTO generatedSalaryHistoryDTO = generatedSalaryHistoryMapper.toDto(generatedSalaryHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGeneratedSalaryHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generatedSalaryHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GeneratedSalaryHistory in the database
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGeneratedSalaryHistory() throws Exception {
        // Initialize the database
        generatedSalaryHistoryRepository.saveAndFlush(generatedSalaryHistory);

        int databaseSizeBeforeDelete = generatedSalaryHistoryRepository.findAll().size();

        // Delete the generatedSalaryHistory
        restGeneratedSalaryHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, generatedSalaryHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GeneratedSalaryHistory> generatedSalaryHistoryList = generatedSalaryHistoryRepository.findAll();
        assertThat(generatedSalaryHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
