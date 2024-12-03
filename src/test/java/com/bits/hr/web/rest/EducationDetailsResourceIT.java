package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.EducationDetails;
import com.bits.hr.repository.EducationDetailsRepository;
import com.bits.hr.service.dto.EducationDetailsDTO;
import com.bits.hr.service.mapper.EducationDetailsMapper;
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
 * Integration tests for the {@link EducationDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EducationDetailsResourceIT {

    private static final String DEFAULT_NAME_OF_DEGREE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_OF_DEGREE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTE = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR_OF_DEGREE_COMPLETION = "AAAAAAAAAA";
    private static final String UPDATED_YEAR_OF_DEGREE_COMPLETION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/education-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EducationDetailsRepository educationDetailsRepository;

    @Autowired
    private EducationDetailsMapper educationDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEducationDetailsMockMvc;

    private EducationDetails educationDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationDetails createEntity(EntityManager em) {
        EducationDetails educationDetails = new EducationDetails()
            .nameOfDegree(DEFAULT_NAME_OF_DEGREE)
            .subject(DEFAULT_SUBJECT)
            .institute(DEFAULT_INSTITUTE)
            .yearOfDegreeCompletion(DEFAULT_YEAR_OF_DEGREE_COMPLETION);
        return educationDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationDetails createUpdatedEntity(EntityManager em) {
        EducationDetails educationDetails = new EducationDetails()
            .nameOfDegree(UPDATED_NAME_OF_DEGREE)
            .subject(UPDATED_SUBJECT)
            .institute(UPDATED_INSTITUTE)
            .yearOfDegreeCompletion(UPDATED_YEAR_OF_DEGREE_COMPLETION);
        return educationDetails;
    }

    @BeforeEach
    public void initTest() {
        educationDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createEducationDetails() throws Exception {
        int databaseSizeBeforeCreate = educationDetailsRepository.findAll().size();
        // Create the EducationDetails
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(educationDetails);
        restEducationDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        EducationDetails testEducationDetails = educationDetailsList.get(educationDetailsList.size() - 1);
        assertThat(testEducationDetails.getNameOfDegree()).isEqualTo(DEFAULT_NAME_OF_DEGREE);
        assertThat(testEducationDetails.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testEducationDetails.getInstitute()).isEqualTo(DEFAULT_INSTITUTE);
        assertThat(testEducationDetails.getYearOfDegreeCompletion()).isEqualTo(DEFAULT_YEAR_OF_DEGREE_COMPLETION);
    }

    @Test
    @Transactional
    void createEducationDetailsWithExistingId() throws Exception {
        // Create the EducationDetails with an existing ID
        educationDetails.setId(1L);
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(educationDetails);

        int databaseSizeBeforeCreate = educationDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEducationDetails() throws Exception {
        // Initialize the database
        educationDetailsRepository.saveAndFlush(educationDetails);

        // Get all the educationDetailsList
        restEducationDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(educationDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameOfDegree").value(hasItem(DEFAULT_NAME_OF_DEGREE)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].institute").value(hasItem(DEFAULT_INSTITUTE)))
            .andExpect(jsonPath("$.[*].yearOfDegreeCompletion").value(hasItem(DEFAULT_YEAR_OF_DEGREE_COMPLETION)));
    }

    @Test
    @Transactional
    void getEducationDetails() throws Exception {
        // Initialize the database
        educationDetailsRepository.saveAndFlush(educationDetails);

        // Get the educationDetails
        restEducationDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, educationDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(educationDetails.getId().intValue()))
            .andExpect(jsonPath("$.nameOfDegree").value(DEFAULT_NAME_OF_DEGREE))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.institute").value(DEFAULT_INSTITUTE))
            .andExpect(jsonPath("$.yearOfDegreeCompletion").value(DEFAULT_YEAR_OF_DEGREE_COMPLETION));
    }

    @Test
    @Transactional
    void getNonExistingEducationDetails() throws Exception {
        // Get the educationDetails
        restEducationDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEducationDetails() throws Exception {
        // Initialize the database
        educationDetailsRepository.saveAndFlush(educationDetails);

        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();

        // Update the educationDetails
        EducationDetails updatedEducationDetails = educationDetailsRepository.findById(educationDetails.getId()).get();
        // Disconnect from session so that the updates on updatedEducationDetails are not directly saved in db
        em.detach(updatedEducationDetails);
        updatedEducationDetails
            .nameOfDegree(UPDATED_NAME_OF_DEGREE)
            .subject(UPDATED_SUBJECT)
            .institute(UPDATED_INSTITUTE)
            .yearOfDegreeCompletion(UPDATED_YEAR_OF_DEGREE_COMPLETION);
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(updatedEducationDetails);

        restEducationDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
        EducationDetails testEducationDetails = educationDetailsList.get(educationDetailsList.size() - 1);
        assertThat(testEducationDetails.getNameOfDegree()).isEqualTo(UPDATED_NAME_OF_DEGREE);
        assertThat(testEducationDetails.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testEducationDetails.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testEducationDetails.getYearOfDegreeCompletion()).isEqualTo(UPDATED_YEAR_OF_DEGREE_COMPLETION);
    }

    @Test
    @Transactional
    void putNonExistingEducationDetails() throws Exception {
        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();
        educationDetails.setId(count.incrementAndGet());

        // Create the EducationDetails
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(educationDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEducationDetails() throws Exception {
        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();
        educationDetails.setId(count.incrementAndGet());

        // Create the EducationDetails
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(educationDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEducationDetails() throws Exception {
        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();
        educationDetails.setId(count.incrementAndGet());

        // Create the EducationDetails
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(educationDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEducationDetailsWithPatch() throws Exception {
        // Initialize the database
        educationDetailsRepository.saveAndFlush(educationDetails);

        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();

        // Update the educationDetails using partial update
        EducationDetails partialUpdatedEducationDetails = new EducationDetails();
        partialUpdatedEducationDetails.setId(educationDetails.getId());

        partialUpdatedEducationDetails
            .subject(UPDATED_SUBJECT)
            .institute(UPDATED_INSTITUTE)
            .yearOfDegreeCompletion(UPDATED_YEAR_OF_DEGREE_COMPLETION);

        restEducationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducationDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducationDetails))
            )
            .andExpect(status().isOk());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
        EducationDetails testEducationDetails = educationDetailsList.get(educationDetailsList.size() - 1);
        assertThat(testEducationDetails.getNameOfDegree()).isEqualTo(DEFAULT_NAME_OF_DEGREE);
        assertThat(testEducationDetails.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testEducationDetails.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testEducationDetails.getYearOfDegreeCompletion()).isEqualTo(UPDATED_YEAR_OF_DEGREE_COMPLETION);
    }

    @Test
    @Transactional
    void fullUpdateEducationDetailsWithPatch() throws Exception {
        // Initialize the database
        educationDetailsRepository.saveAndFlush(educationDetails);

        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();

        // Update the educationDetails using partial update
        EducationDetails partialUpdatedEducationDetails = new EducationDetails();
        partialUpdatedEducationDetails.setId(educationDetails.getId());

        partialUpdatedEducationDetails
            .nameOfDegree(UPDATED_NAME_OF_DEGREE)
            .subject(UPDATED_SUBJECT)
            .institute(UPDATED_INSTITUTE)
            .yearOfDegreeCompletion(UPDATED_YEAR_OF_DEGREE_COMPLETION);

        restEducationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducationDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducationDetails))
            )
            .andExpect(status().isOk());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
        EducationDetails testEducationDetails = educationDetailsList.get(educationDetailsList.size() - 1);
        assertThat(testEducationDetails.getNameOfDegree()).isEqualTo(UPDATED_NAME_OF_DEGREE);
        assertThat(testEducationDetails.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testEducationDetails.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testEducationDetails.getYearOfDegreeCompletion()).isEqualTo(UPDATED_YEAR_OF_DEGREE_COMPLETION);
    }

    @Test
    @Transactional
    void patchNonExistingEducationDetails() throws Exception {
        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();
        educationDetails.setId(count.incrementAndGet());

        // Create the EducationDetails
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(educationDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, educationDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEducationDetails() throws Exception {
        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();
        educationDetails.setId(count.incrementAndGet());

        // Create the EducationDetails
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(educationDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEducationDetails() throws Exception {
        int databaseSizeBeforeUpdate = educationDetailsRepository.findAll().size();
        educationDetails.setId(count.incrementAndGet());

        // Create the EducationDetails
        EducationDetailsDTO educationDetailsDTO = educationDetailsMapper.toDto(educationDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EducationDetails in the database
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEducationDetails() throws Exception {
        // Initialize the database
        educationDetailsRepository.saveAndFlush(educationDetails);

        int databaseSizeBeforeDelete = educationDetailsRepository.findAll().size();

        // Delete the educationDetails
        restEducationDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, educationDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EducationDetails> educationDetailsList = educationDetailsRepository.findAll();
        assertThat(educationDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
