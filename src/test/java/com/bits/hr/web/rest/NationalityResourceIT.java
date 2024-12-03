package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Nationality;
import com.bits.hr.repository.NationalityRepository;
import com.bits.hr.service.dto.NationalityDTO;
import com.bits.hr.service.mapper.NationalityMapper;
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
 * Integration tests for the {@link NationalityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NationalityResourceIT {

    private static final String DEFAULT_NATIONALITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nationalities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NationalityRepository nationalityRepository;

    @Autowired
    private NationalityMapper nationalityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNationalityMockMvc;

    private Nationality nationality;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nationality createEntity(EntityManager em) {
        Nationality nationality = new Nationality().nationalityName(DEFAULT_NATIONALITY_NAME);
        return nationality;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nationality createUpdatedEntity(EntityManager em) {
        Nationality nationality = new Nationality().nationalityName(UPDATED_NATIONALITY_NAME);
        return nationality;
    }

    @BeforeEach
    public void initTest() {
        nationality = createEntity(em);
    }

    @Test
    @Transactional
    void createNationality() throws Exception {
        int databaseSizeBeforeCreate = nationalityRepository.findAll().size();
        // Create the Nationality
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);
        restNationalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeCreate + 1);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNationalityName()).isEqualTo(DEFAULT_NATIONALITY_NAME);
    }

    @Test
    @Transactional
    void createNationalityWithExistingId() throws Exception {
        // Create the Nationality with an existing ID
        nationality.setId(1L);
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);

        int databaseSizeBeforeCreate = nationalityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNationalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNationalityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nationalityRepository.findAll().size();
        // set the field null
        nationality.setNationalityName(null);

        // Create the Nationality, which fails.
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);

        restNationalityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isBadRequest());

        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNationalities() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        // Get all the nationalityList
        restNationalityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nationality.getId().intValue())))
            .andExpect(jsonPath("$.[*].nationalityName").value(hasItem(DEFAULT_NATIONALITY_NAME)));
    }

    @Test
    @Transactional
    void getNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        // Get the nationality
        restNationalityMockMvc
            .perform(get(ENTITY_API_URL_ID, nationality.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nationality.getId().intValue()))
            .andExpect(jsonPath("$.nationalityName").value(DEFAULT_NATIONALITY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingNationality() throws Exception {
        // Get the nationality
        restNationalityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Update the nationality
        Nationality updatedNationality = nationalityRepository.findById(nationality.getId()).get();
        // Disconnect from session so that the updates on updatedNationality are not directly saved in db
        em.detach(updatedNationality);
        updatedNationality.nationalityName(UPDATED_NATIONALITY_NAME);
        NationalityDTO nationalityDTO = nationalityMapper.toDto(updatedNationality);

        restNationalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nationalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNationalityName()).isEqualTo(UPDATED_NATIONALITY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        nationality.setId(count.incrementAndGet());

        // Create the Nationality
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nationalityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        nationality.setId(count.incrementAndGet());

        // Create the Nationality
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        nationality.setId(count.incrementAndGet());

        // Create the Nationality
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nationalityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNationalityWithPatch() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Update the nationality using partial update
        Nationality partialUpdatedNationality = new Nationality();
        partialUpdatedNationality.setId(nationality.getId());

        partialUpdatedNationality.nationalityName(UPDATED_NATIONALITY_NAME);

        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNationality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNationality))
            )
            .andExpect(status().isOk());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNationalityName()).isEqualTo(UPDATED_NATIONALITY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateNationalityWithPatch() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

        // Update the nationality using partial update
        Nationality partialUpdatedNationality = new Nationality();
        partialUpdatedNationality.setId(nationality.getId());

        partialUpdatedNationality.nationalityName(UPDATED_NATIONALITY_NAME);

        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNationality.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNationality))
            )
            .andExpect(status().isOk());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
        Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
        assertThat(testNationality.getNationalityName()).isEqualTo(UPDATED_NATIONALITY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        nationality.setId(count.incrementAndGet());

        // Create the Nationality
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nationalityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        nationality.setId(count.incrementAndGet());

        // Create the Nationality
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNationality() throws Exception {
        int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();
        nationality.setId(count.incrementAndGet());

        // Create the Nationality
        NationalityDTO nationalityDTO = nationalityMapper.toDto(nationality);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNationalityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nationalityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Nationality in the database
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNationality() throws Exception {
        // Initialize the database
        nationalityRepository.saveAndFlush(nationality);

        int databaseSizeBeforeDelete = nationalityRepository.findAll().size();

        // Delete the nationality
        restNationalityMockMvc
            .perform(delete(ENTITY_API_URL_ID, nationality.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Nationality> nationalityList = nationalityRepository.findAll();
        assertThat(nationalityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
