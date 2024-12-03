package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.dto.PfCollectionDTO;
import com.bits.hr.service.mapper.PfCollectionMapper;
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
 * Integration tests for the {@link PfCollectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PfCollectionResourceIT {

    private static final Double DEFAULT_EMPLOYEE_CONTRIBUTION = 1D;
    private static final Double UPDATED_EMPLOYEE_CONTRIBUTION = 2D;

    private static final Double DEFAULT_EMPLOYER_CONTRIBUTION = 1D;
    private static final Double UPDATED_EMPLOYER_CONTRIBUTION = 2D;

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final PfCollectionType DEFAULT_COLLECTION_TYPE = PfCollectionType.ARREAR;
    private static final PfCollectionType UPDATED_COLLECTION_TYPE = PfCollectionType.ADVANCE;

    private static final Double DEFAULT_EMPLOYEE_INTEREST = 0D;
    private static final Double UPDATED_EMPLOYEE_INTEREST = 1D;

    private static final Double DEFAULT_EMPLOYER_INTEREST = 0D;
    private static final Double UPDATED_EMPLOYER_INTEREST = 1D;

    private static final Double DEFAULT_GROSS = 0D;
    private static final Double UPDATED_GROSS = 1D;

    private static final Double DEFAULT_BASIC = 0D;
    private static final Double UPDATED_BASIC = 1D;

    private static final String ENTITY_API_URL = "/api/pf-collections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private PfCollectionMapper pfCollectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPfCollectionMockMvc;

    private PfCollection pfCollection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfCollection createEntity(EntityManager em) {
        PfCollection pfCollection = new PfCollection()
            .employeeContribution(DEFAULT_EMPLOYEE_CONTRIBUTION)
            .employerContribution(DEFAULT_EMPLOYER_CONTRIBUTION)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .year(DEFAULT_YEAR)
            .month(DEFAULT_MONTH)
            .collectionType(DEFAULT_COLLECTION_TYPE)
            .employeeInterest(DEFAULT_EMPLOYEE_INTEREST)
            .employerInterest(DEFAULT_EMPLOYER_INTEREST)
            .gross(DEFAULT_GROSS)
            .basic(DEFAULT_BASIC);
        return pfCollection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfCollection createUpdatedEntity(EntityManager em) {
        PfCollection pfCollection = new PfCollection()
            .employeeContribution(UPDATED_EMPLOYEE_CONTRIBUTION)
            .employerContribution(UPDATED_EMPLOYER_CONTRIBUTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .collectionType(UPDATED_COLLECTION_TYPE)
            .employeeInterest(UPDATED_EMPLOYEE_INTEREST)
            .employerInterest(UPDATED_EMPLOYER_INTEREST)
            .gross(UPDATED_GROSS)
            .basic(UPDATED_BASIC);
        return pfCollection;
    }

    @BeforeEach
    public void initTest() {
        pfCollection = createEntity(em);
    }

    @Test
    @Transactional
    void createPfCollection() throws Exception {
        int databaseSizeBeforeCreate = pfCollectionRepository.findAll().size();
        // Create the PfCollection
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(pfCollection);
        restPfCollectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeCreate + 1);
        PfCollection testPfCollection = pfCollectionList.get(pfCollectionList.size() - 1);
        assertThat(testPfCollection.getEmployeeContribution()).isEqualTo(DEFAULT_EMPLOYEE_CONTRIBUTION);
        assertThat(testPfCollection.getEmployerContribution()).isEqualTo(DEFAULT_EMPLOYER_CONTRIBUTION);
        assertThat(testPfCollection.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testPfCollection.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPfCollection.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testPfCollection.getCollectionType()).isEqualTo(DEFAULT_COLLECTION_TYPE);
        assertThat(testPfCollection.getEmployeeInterest()).isEqualTo(DEFAULT_EMPLOYEE_INTEREST);
        assertThat(testPfCollection.getEmployerInterest()).isEqualTo(DEFAULT_EMPLOYER_INTEREST);
        assertThat(testPfCollection.getGross()).isEqualTo(DEFAULT_GROSS);
        assertThat(testPfCollection.getBasic()).isEqualTo(DEFAULT_BASIC);
    }

    @Test
    @Transactional
    void createPfCollectionWithExistingId() throws Exception {
        // Create the PfCollection with an existing ID
        pfCollection.setId(1L);
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(pfCollection);

        int databaseSizeBeforeCreate = pfCollectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPfCollectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPfCollections() throws Exception {
        // Initialize the database
        pfCollectionRepository.saveAndFlush(pfCollection);

        // Get all the pfCollectionList
        restPfCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pfCollection.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeContribution").value(hasItem(DEFAULT_EMPLOYEE_CONTRIBUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].employerContribution").value(hasItem(DEFAULT_EMPLOYER_CONTRIBUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].collectionType").value(hasItem(DEFAULT_COLLECTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].employeeInterest").value(hasItem(DEFAULT_EMPLOYEE_INTEREST.doubleValue())))
            .andExpect(jsonPath("$.[*].employerInterest").value(hasItem(DEFAULT_EMPLOYER_INTEREST.doubleValue())))
            .andExpect(jsonPath("$.[*].gross").value(hasItem(DEFAULT_GROSS.doubleValue())))
            .andExpect(jsonPath("$.[*].basic").value(hasItem(DEFAULT_BASIC.doubleValue())));
    }

    @Test
    @Transactional
    void getPfCollection() throws Exception {
        // Initialize the database
        pfCollectionRepository.saveAndFlush(pfCollection);

        // Get the pfCollection
        restPfCollectionMockMvc
            .perform(get(ENTITY_API_URL_ID, pfCollection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pfCollection.getId().intValue()))
            .andExpect(jsonPath("$.employeeContribution").value(DEFAULT_EMPLOYEE_CONTRIBUTION.doubleValue()))
            .andExpect(jsonPath("$.employerContribution").value(DEFAULT_EMPLOYER_CONTRIBUTION.doubleValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.collectionType").value(DEFAULT_COLLECTION_TYPE.toString()))
            .andExpect(jsonPath("$.employeeInterest").value(DEFAULT_EMPLOYEE_INTEREST.doubleValue()))
            .andExpect(jsonPath("$.employerInterest").value(DEFAULT_EMPLOYER_INTEREST.doubleValue()))
            .andExpect(jsonPath("$.gross").value(DEFAULT_GROSS.doubleValue()))
            .andExpect(jsonPath("$.basic").value(DEFAULT_BASIC.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPfCollection() throws Exception {
        // Get the pfCollection
        restPfCollectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPfCollection() throws Exception {
        // Initialize the database
        pfCollectionRepository.saveAndFlush(pfCollection);

        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();

        // Update the pfCollection
        PfCollection updatedPfCollection = pfCollectionRepository.findById(pfCollection.getId()).get();
        // Disconnect from session so that the updates on updatedPfCollection are not directly saved in db
        em.detach(updatedPfCollection);
        updatedPfCollection
            .employeeContribution(UPDATED_EMPLOYEE_CONTRIBUTION)
            .employerContribution(UPDATED_EMPLOYER_CONTRIBUTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .collectionType(UPDATED_COLLECTION_TYPE)
            .employeeInterest(UPDATED_EMPLOYEE_INTEREST)
            .employerInterest(UPDATED_EMPLOYER_INTEREST)
            .gross(UPDATED_GROSS)
            .basic(UPDATED_BASIC);
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(updatedPfCollection);

        restPfCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfCollectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
        PfCollection testPfCollection = pfCollectionList.get(pfCollectionList.size() - 1);
        assertThat(testPfCollection.getEmployeeContribution()).isEqualTo(UPDATED_EMPLOYEE_CONTRIBUTION);
        assertThat(testPfCollection.getEmployerContribution()).isEqualTo(UPDATED_EMPLOYER_CONTRIBUTION);
        assertThat(testPfCollection.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPfCollection.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPfCollection.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testPfCollection.getCollectionType()).isEqualTo(UPDATED_COLLECTION_TYPE);
        assertThat(testPfCollection.getEmployeeInterest()).isEqualTo(UPDATED_EMPLOYEE_INTEREST);
        assertThat(testPfCollection.getEmployerInterest()).isEqualTo(UPDATED_EMPLOYER_INTEREST);
        assertThat(testPfCollection.getGross()).isEqualTo(UPDATED_GROSS);
        assertThat(testPfCollection.getBasic()).isEqualTo(UPDATED_BASIC);
    }

    @Test
    @Transactional
    void putNonExistingPfCollection() throws Exception {
        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();
        pfCollection.setId(count.incrementAndGet());

        // Create the PfCollection
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(pfCollection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfCollectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPfCollection() throws Exception {
        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();
        pfCollection.setId(count.incrementAndGet());

        // Create the PfCollection
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(pfCollection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPfCollection() throws Exception {
        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();
        pfCollection.setId(count.incrementAndGet());

        // Create the PfCollection
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(pfCollection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfCollectionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePfCollectionWithPatch() throws Exception {
        // Initialize the database
        pfCollectionRepository.saveAndFlush(pfCollection);

        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();

        // Update the pfCollection using partial update
        PfCollection partialUpdatedPfCollection = new PfCollection();
        partialUpdatedPfCollection.setId(pfCollection.getId());

        partialUpdatedPfCollection
            .employerContribution(UPDATED_EMPLOYER_CONTRIBUTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .basic(UPDATED_BASIC);

        restPfCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfCollection))
            )
            .andExpect(status().isOk());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
        PfCollection testPfCollection = pfCollectionList.get(pfCollectionList.size() - 1);
        assertThat(testPfCollection.getEmployeeContribution()).isEqualTo(DEFAULT_EMPLOYEE_CONTRIBUTION);
        assertThat(testPfCollection.getEmployerContribution()).isEqualTo(UPDATED_EMPLOYER_CONTRIBUTION);
        assertThat(testPfCollection.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPfCollection.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPfCollection.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testPfCollection.getCollectionType()).isEqualTo(DEFAULT_COLLECTION_TYPE);
        assertThat(testPfCollection.getEmployeeInterest()).isEqualTo(DEFAULT_EMPLOYEE_INTEREST);
        assertThat(testPfCollection.getEmployerInterest()).isEqualTo(DEFAULT_EMPLOYER_INTEREST);
        assertThat(testPfCollection.getGross()).isEqualTo(DEFAULT_GROSS);
        assertThat(testPfCollection.getBasic()).isEqualTo(UPDATED_BASIC);
    }

    @Test
    @Transactional
    void fullUpdatePfCollectionWithPatch() throws Exception {
        // Initialize the database
        pfCollectionRepository.saveAndFlush(pfCollection);

        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();

        // Update the pfCollection using partial update
        PfCollection partialUpdatedPfCollection = new PfCollection();
        partialUpdatedPfCollection.setId(pfCollection.getId());

        partialUpdatedPfCollection
            .employeeContribution(UPDATED_EMPLOYEE_CONTRIBUTION)
            .employerContribution(UPDATED_EMPLOYER_CONTRIBUTION)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .year(UPDATED_YEAR)
            .month(UPDATED_MONTH)
            .collectionType(UPDATED_COLLECTION_TYPE)
            .employeeInterest(UPDATED_EMPLOYEE_INTEREST)
            .employerInterest(UPDATED_EMPLOYER_INTEREST)
            .gross(UPDATED_GROSS)
            .basic(UPDATED_BASIC);

        restPfCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfCollection))
            )
            .andExpect(status().isOk());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
        PfCollection testPfCollection = pfCollectionList.get(pfCollectionList.size() - 1);
        assertThat(testPfCollection.getEmployeeContribution()).isEqualTo(UPDATED_EMPLOYEE_CONTRIBUTION);
        assertThat(testPfCollection.getEmployerContribution()).isEqualTo(UPDATED_EMPLOYER_CONTRIBUTION);
        assertThat(testPfCollection.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPfCollection.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPfCollection.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testPfCollection.getCollectionType()).isEqualTo(UPDATED_COLLECTION_TYPE);
        assertThat(testPfCollection.getEmployeeInterest()).isEqualTo(UPDATED_EMPLOYEE_INTEREST);
        assertThat(testPfCollection.getEmployerInterest()).isEqualTo(UPDATED_EMPLOYER_INTEREST);
        assertThat(testPfCollection.getGross()).isEqualTo(UPDATED_GROSS);
        assertThat(testPfCollection.getBasic()).isEqualTo(UPDATED_BASIC);
    }

    @Test
    @Transactional
    void patchNonExistingPfCollection() throws Exception {
        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();
        pfCollection.setId(count.incrementAndGet());

        // Create the PfCollection
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(pfCollection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pfCollectionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPfCollection() throws Exception {
        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();
        pfCollection.setId(count.incrementAndGet());

        // Create the PfCollection
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(pfCollection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPfCollection() throws Exception {
        int databaseSizeBeforeUpdate = pfCollectionRepository.findAll().size();
        pfCollection.setId(count.incrementAndGet());

        // Create the PfCollection
        PfCollectionDTO pfCollectionDTO = pfCollectionMapper.toDto(pfCollection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfCollectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfCollection in the database
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePfCollection() throws Exception {
        // Initialize the database
        pfCollectionRepository.saveAndFlush(pfCollection);

        int databaseSizeBeforeDelete = pfCollectionRepository.findAll().size();

        // Delete the pfCollection
        restPfCollectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, pfCollection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PfCollection> pfCollectionList = pfCollectionRepository.findAll();
        assertThat(pfCollectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
