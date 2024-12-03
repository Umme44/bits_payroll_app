package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.References;
import com.bits.hr.domain.enumeration.RelationshipWithEmployee;
import com.bits.hr.repository.ReferencesRepository;
import com.bits.hr.service.dto.ReferencesDTO;
import com.bits.hr.service.mapper.ReferencesMapper;
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
 * Integration tests for the {@link ReferencesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferencesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTE = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final RelationshipWithEmployee DEFAULT_RELATIONSHIP_WITH_EMPLOYEE = RelationshipWithEmployee.PERSONAL;
    private static final RelationshipWithEmployee UPDATED_RELATIONSHIP_WITH_EMPLOYEE = RelationshipWithEmployee.PROFESSIONAL;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/references";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReferencesRepository referencesRepository;

    @Autowired
    private ReferencesMapper referencesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferencesMockMvc;

    private References references;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static References createEntity(EntityManager em) {
        References references = new References()
            .name(DEFAULT_NAME)
            .institute(DEFAULT_INSTITUTE)
            .designation(DEFAULT_DESIGNATION)
            .relationshipWithEmployee(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE)
            .email(DEFAULT_EMAIL)
            .contactNumber(DEFAULT_CONTACT_NUMBER);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        references.setEmployee(employee);
        return references;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static References createUpdatedEntity(EntityManager em) {
        References references = new References()
            .name(UPDATED_NAME)
            .institute(UPDATED_INSTITUTE)
            .designation(UPDATED_DESIGNATION)
            .relationshipWithEmployee(UPDATED_RELATIONSHIP_WITH_EMPLOYEE)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        references.setEmployee(employee);
        return references;
    }

    @BeforeEach
    public void initTest() {
        references = createEntity(em);
    }

    @Test
    @Transactional
    void createReferences() throws Exception {
        int databaseSizeBeforeCreate = referencesRepository.findAll().size();
        // Create the References
        ReferencesDTO referencesDTO = referencesMapper.toDto(references);
        restReferencesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referencesDTO)))
            .andExpect(status().isCreated());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeCreate + 1);
        References testReferences = referencesList.get(referencesList.size() - 1);
        assertThat(testReferences.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReferences.getInstitute()).isEqualTo(DEFAULT_INSTITUTE);
        assertThat(testReferences.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testReferences.getRelationshipWithEmployee()).isEqualTo(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testReferences.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testReferences.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void createReferencesWithExistingId() throws Exception {
        // Create the References with an existing ID
        references.setId(1L);
        ReferencesDTO referencesDTO = referencesMapper.toDto(references);

        int databaseSizeBeforeCreate = referencesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferencesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReferences() throws Exception {
        // Initialize the database
        referencesRepository.saveAndFlush(references);

        // Get all the referencesList
        restReferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(references.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].institute").value(hasItem(DEFAULT_INSTITUTE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].relationshipWithEmployee").value(hasItem(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)));
    }

    @Test
    @Transactional
    void getReferences() throws Exception {
        // Initialize the database
        referencesRepository.saveAndFlush(references);

        // Get the references
        restReferencesMockMvc
            .perform(get(ENTITY_API_URL_ID, references.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(references.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.institute").value(DEFAULT_INSTITUTE))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.relationshipWithEmployee").value(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingReferences() throws Exception {
        // Get the references
        restReferencesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReferences() throws Exception {
        // Initialize the database
        referencesRepository.saveAndFlush(references);

        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();

        // Update the references
        References updatedReferences = referencesRepository.findById(references.getId()).get();
        // Disconnect from session so that the updates on updatedReferences are not directly saved in db
        em.detach(updatedReferences);
        updatedReferences
            .name(UPDATED_NAME)
            .institute(UPDATED_INSTITUTE)
            .designation(UPDATED_DESIGNATION)
            .relationshipWithEmployee(UPDATED_RELATIONSHIP_WITH_EMPLOYEE)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER);
        ReferencesDTO referencesDTO = referencesMapper.toDto(updatedReferences);

        restReferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referencesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referencesDTO))
            )
            .andExpect(status().isOk());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
        References testReferences = referencesList.get(referencesList.size() - 1);
        assertThat(testReferences.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReferences.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testReferences.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testReferences.getRelationshipWithEmployee()).isEqualTo(UPDATED_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testReferences.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testReferences.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingReferences() throws Exception {
        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();
        references.setId(count.incrementAndGet());

        // Create the References
        ReferencesDTO referencesDTO = referencesMapper.toDto(references);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referencesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferences() throws Exception {
        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();
        references.setId(count.incrementAndGet());

        // Create the References
        ReferencesDTO referencesDTO = referencesMapper.toDto(references);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferences() throws Exception {
        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();
        references.setId(count.incrementAndGet());

        // Create the References
        ReferencesDTO referencesDTO = referencesMapper.toDto(references);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferencesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referencesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferencesWithPatch() throws Exception {
        // Initialize the database
        referencesRepository.saveAndFlush(references);

        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();

        // Update the references using partial update
        References partialUpdatedReferences = new References();
        partialUpdatedReferences.setId(references.getId());

        partialUpdatedReferences.institute(UPDATED_INSTITUTE).contactNumber(UPDATED_CONTACT_NUMBER);

        restReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferences))
            )
            .andExpect(status().isOk());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
        References testReferences = referencesList.get(referencesList.size() - 1);
        assertThat(testReferences.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReferences.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testReferences.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testReferences.getRelationshipWithEmployee()).isEqualTo(DEFAULT_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testReferences.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testReferences.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateReferencesWithPatch() throws Exception {
        // Initialize the database
        referencesRepository.saveAndFlush(references);

        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();

        // Update the references using partial update
        References partialUpdatedReferences = new References();
        partialUpdatedReferences.setId(references.getId());

        partialUpdatedReferences
            .name(UPDATED_NAME)
            .institute(UPDATED_INSTITUTE)
            .designation(UPDATED_DESIGNATION)
            .relationshipWithEmployee(UPDATED_RELATIONSHIP_WITH_EMPLOYEE)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER);

        restReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferences))
            )
            .andExpect(status().isOk());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
        References testReferences = referencesList.get(referencesList.size() - 1);
        assertThat(testReferences.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReferences.getInstitute()).isEqualTo(UPDATED_INSTITUTE);
        assertThat(testReferences.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testReferences.getRelationshipWithEmployee()).isEqualTo(UPDATED_RELATIONSHIP_WITH_EMPLOYEE);
        assertThat(testReferences.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testReferences.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingReferences() throws Exception {
        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();
        references.setId(count.incrementAndGet());

        // Create the References
        ReferencesDTO referencesDTO = referencesMapper.toDto(references);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referencesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferences() throws Exception {
        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();
        references.setId(count.incrementAndGet());

        // Create the References
        ReferencesDTO referencesDTO = referencesMapper.toDto(references);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferences() throws Exception {
        int databaseSizeBeforeUpdate = referencesRepository.findAll().size();
        references.setId(count.incrementAndGet());

        // Create the References
        ReferencesDTO referencesDTO = referencesMapper.toDto(references);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferencesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(referencesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the References in the database
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferences() throws Exception {
        // Initialize the database
        referencesRepository.saveAndFlush(references);

        int databaseSizeBeforeDelete = referencesRepository.findAll().size();

        // Delete the references
        restReferencesMockMvc
            .perform(delete(ENTITY_API_URL_ID, references.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<References> referencesList = referencesRepository.findAll();
        assertThat(referencesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
