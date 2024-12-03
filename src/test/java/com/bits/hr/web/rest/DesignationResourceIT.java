package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Designation;
import com.bits.hr.repository.DesignationRepository;
import com.bits.hr.service.dto.DesignationDTO;
import com.bits.hr.service.mapper.DesignationMapper;
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
 * Integration tests for the {@link DesignationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DesignationResourceIT {

    private static final String DEFAULT_DESIGNATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/designations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private DesignationMapper designationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDesignationMockMvc;

    private Designation designation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createEntity(EntityManager em) {
        Designation designation = new Designation().designationName(DEFAULT_DESIGNATION_NAME);
        return designation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createUpdatedEntity(EntityManager em) {
        Designation designation = new Designation().designationName(UPDATED_DESIGNATION_NAME);
        return designation;
    }

    @BeforeEach
    public void initTest() {
        designation = createEntity(em);
    }

    @Test
    @Transactional
    void createDesignation() throws Exception {
        int databaseSizeBeforeCreate = designationRepository.findAll().size();
        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);
        restDesignationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeCreate + 1);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getDesignationName()).isEqualTo(DEFAULT_DESIGNATION_NAME);
    }

    @Test
    @Transactional
    void createDesignationWithExistingId() throws Exception {
        // Create the Designation with an existing ID
        designation.setId(1L);
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        int databaseSizeBeforeCreate = designationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesignationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = designationRepository.findAll().size();
        // set the field null
        designation.setDesignationName(null);

        // Create the Designation, which fails.
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        restDesignationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDesignations() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(designation.getId().intValue())))
            .andExpect(jsonPath("$.[*].designationName").value(hasItem(DEFAULT_DESIGNATION_NAME)));
    }

    @Test
    @Transactional
    void getDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get the designation
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL_ID, designation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(designation.getId().intValue()))
            .andExpect(jsonPath("$.designationName").value(DEFAULT_DESIGNATION_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDesignation() throws Exception {
        // Get the designation
        restDesignationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation
        Designation updatedDesignation = designationRepository.findById(designation.getId()).get();
        // Disconnect from session so that the updates on updatedDesignation are not directly saved in db
        em.detach(updatedDesignation);
        updatedDesignation.designationName(UPDATED_DESIGNATION_NAME);
        DesignationDTO designationDTO = designationMapper.toDto(updatedDesignation);

        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, designationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, designationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDesignationWithPatch() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation using partial update
        Designation partialUpdatedDesignation = new Designation();
        partialUpdatedDesignation.setId(designation.getId());

        partialUpdatedDesignation.designationName(UPDATED_DESIGNATION_NAME);

        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDesignationWithPatch() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation using partial update
        Designation partialUpdatedDesignation = new Designation();
        partialUpdatedDesignation.setId(designation.getId());

        partialUpdatedDesignation.designationName(UPDATED_DESIGNATION_NAME);

        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, designationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeDelete = designationRepository.findAll().size();

        // Delete the designation
        restDesignationMockMvc
            .perform(delete(ENTITY_API_URL_ID, designation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
