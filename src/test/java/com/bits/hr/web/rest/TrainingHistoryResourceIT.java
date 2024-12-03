package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.TrainingHistory;
import com.bits.hr.repository.TrainingHistoryRepository;
import com.bits.hr.service.dto.TrainingHistoryDTO;
import com.bits.hr.service.mapper.TrainingHistoryMapper;
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
 * Integration tests for the {@link TrainingHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingHistoryResourceIT {

    private static final String DEFAULT_TRAINING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRAINING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COORDINATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_COORDINATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_COMPLETION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_COMPLETION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/training-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrainingHistoryRepository trainingHistoryRepository;

    @Autowired
    private TrainingHistoryMapper trainingHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingHistoryMockMvc;

    private TrainingHistory trainingHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingHistory createEntity(EntityManager em) {
        TrainingHistory trainingHistory = new TrainingHistory()
            .trainingName(DEFAULT_TRAINING_NAME)
            .coordinatedBy(DEFAULT_COORDINATED_BY)
            .dateOfCompletion(DEFAULT_DATE_OF_COMPLETION);
        return trainingHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingHistory createUpdatedEntity(EntityManager em) {
        TrainingHistory trainingHistory = new TrainingHistory()
            .trainingName(UPDATED_TRAINING_NAME)
            .coordinatedBy(UPDATED_COORDINATED_BY)
            .dateOfCompletion(UPDATED_DATE_OF_COMPLETION);
        return trainingHistory;
    }

    @BeforeEach
    public void initTest() {
        trainingHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createTrainingHistory() throws Exception {
        int databaseSizeBeforeCreate = trainingHistoryRepository.findAll().size();
        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);
        restTrainingHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getTrainingName()).isEqualTo(DEFAULT_TRAINING_NAME);
        assertThat(testTrainingHistory.getCoordinatedBy()).isEqualTo(DEFAULT_COORDINATED_BY);
        assertThat(testTrainingHistory.getDateOfCompletion()).isEqualTo(DEFAULT_DATE_OF_COMPLETION);
    }

    @Test
    @Transactional
    void createTrainingHistoryWithExistingId() throws Exception {
        // Create the TrainingHistory with an existing ID
        trainingHistory.setId(1L);
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        int databaseSizeBeforeCreate = trainingHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrainingHistories() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].trainingName").value(hasItem(DEFAULT_TRAINING_NAME)))
            .andExpect(jsonPath("$.[*].coordinatedBy").value(hasItem(DEFAULT_COORDINATED_BY)))
            .andExpect(jsonPath("$.[*].dateOfCompletion").value(hasItem(DEFAULT_DATE_OF_COMPLETION.toString())));
    }

    @Test
    @Transactional
    void getTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get the trainingHistory
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingHistory.getId().intValue()))
            .andExpect(jsonPath("$.trainingName").value(DEFAULT_TRAINING_NAME))
            .andExpect(jsonPath("$.coordinatedBy").value(DEFAULT_COORDINATED_BY))
            .andExpect(jsonPath("$.dateOfCompletion").value(DEFAULT_DATE_OF_COMPLETION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTrainingHistory() throws Exception {
        // Get the trainingHistory
        restTrainingHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory
        TrainingHistory updatedTrainingHistory = trainingHistoryRepository.findById(trainingHistory.getId()).get();
        // Disconnect from session so that the updates on updatedTrainingHistory are not directly saved in db
        em.detach(updatedTrainingHistory);
        updatedTrainingHistory
            .trainingName(UPDATED_TRAINING_NAME)
            .coordinatedBy(UPDATED_COORDINATED_BY)
            .dateOfCompletion(UPDATED_DATE_OF_COMPLETION);
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(updatedTrainingHistory);

        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getTrainingName()).isEqualTo(UPDATED_TRAINING_NAME);
        assertThat(testTrainingHistory.getCoordinatedBy()).isEqualTo(UPDATED_COORDINATED_BY);
        assertThat(testTrainingHistory.getDateOfCompletion()).isEqualTo(UPDATED_DATE_OF_COMPLETION);
    }

    @Test
    @Transactional
    void putNonExistingTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingHistoryWithPatch() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory using partial update
        TrainingHistory partialUpdatedTrainingHistory = new TrainingHistory();
        partialUpdatedTrainingHistory.setId(trainingHistory.getId());

        partialUpdatedTrainingHistory.dateOfCompletion(UPDATED_DATE_OF_COMPLETION);

        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingHistory))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getTrainingName()).isEqualTo(DEFAULT_TRAINING_NAME);
        assertThat(testTrainingHistory.getCoordinatedBy()).isEqualTo(DEFAULT_COORDINATED_BY);
        assertThat(testTrainingHistory.getDateOfCompletion()).isEqualTo(UPDATED_DATE_OF_COMPLETION);
    }

    @Test
    @Transactional
    void fullUpdateTrainingHistoryWithPatch() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory using partial update
        TrainingHistory partialUpdatedTrainingHistory = new TrainingHistory();
        partialUpdatedTrainingHistory.setId(trainingHistory.getId());

        partialUpdatedTrainingHistory
            .trainingName(UPDATED_TRAINING_NAME)
            .coordinatedBy(UPDATED_COORDINATED_BY)
            .dateOfCompletion(UPDATED_DATE_OF_COMPLETION);

        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingHistory))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getTrainingName()).isEqualTo(UPDATED_TRAINING_NAME);
        assertThat(testTrainingHistory.getCoordinatedBy()).isEqualTo(UPDATED_COORDINATED_BY);
        assertThat(testTrainingHistory.getDateOfCompletion()).isEqualTo(UPDATED_DATE_OF_COMPLETION);
    }

    @Test
    @Transactional
    void patchNonExistingTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeDelete = trainingHistoryRepository.findAll().size();

        // Delete the trainingHistory
        restTrainingHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
