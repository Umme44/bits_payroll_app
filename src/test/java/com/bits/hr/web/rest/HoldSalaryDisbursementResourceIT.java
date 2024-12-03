package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.HoldSalaryDisbursement;
import com.bits.hr.repository.HoldSalaryDisbursementRepository;
import com.bits.hr.service.HoldSalaryDisbursementService;
import com.bits.hr.service.dto.HoldSalaryDisbursementDTO;
import com.bits.hr.service.mapper.HoldSalaryDisbursementMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HoldSalaryDisbursementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HoldSalaryDisbursementResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/hold-salary-disbursements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HoldSalaryDisbursementRepository holdSalaryDisbursementRepository;

    @Mock
    private HoldSalaryDisbursementRepository holdSalaryDisbursementRepositoryMock;

    @Autowired
    private HoldSalaryDisbursementMapper holdSalaryDisbursementMapper;

    @Mock
    private HoldSalaryDisbursementService holdSalaryDisbursementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHoldSalaryDisbursementMockMvc;

    private HoldSalaryDisbursement holdSalaryDisbursement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HoldSalaryDisbursement createEntity(EntityManager em) {
        HoldSalaryDisbursement holdSalaryDisbursement = new HoldSalaryDisbursement().date(DEFAULT_DATE);
        return holdSalaryDisbursement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HoldSalaryDisbursement createUpdatedEntity(EntityManager em) {
        HoldSalaryDisbursement holdSalaryDisbursement = new HoldSalaryDisbursement().date(UPDATED_DATE);
        return holdSalaryDisbursement;
    }

    @BeforeEach
    public void initTest() {
        holdSalaryDisbursement = createEntity(em);
    }

    @Test
    @Transactional
    void createHoldSalaryDisbursement() throws Exception {
        int databaseSizeBeforeCreate = holdSalaryDisbursementRepository.findAll().size();
        // Create the HoldSalaryDisbursement
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);
        restHoldSalaryDisbursementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeCreate + 1);
        HoldSalaryDisbursement testHoldSalaryDisbursement = holdSalaryDisbursementList.get(holdSalaryDisbursementList.size() - 1);
        assertThat(testHoldSalaryDisbursement.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createHoldSalaryDisbursementWithExistingId() throws Exception {
        // Create the HoldSalaryDisbursement with an existing ID
        holdSalaryDisbursement.setId(1L);
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);

        int databaseSizeBeforeCreate = holdSalaryDisbursementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoldSalaryDisbursementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = holdSalaryDisbursementRepository.findAll().size();
        // set the field null
        holdSalaryDisbursement.setDate(null);

        // Create the HoldSalaryDisbursement, which fails.
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);

        restHoldSalaryDisbursementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHoldSalaryDisbursements() throws Exception {
        // Initialize the database
        holdSalaryDisbursementRepository.saveAndFlush(holdSalaryDisbursement);

        // Get all the holdSalaryDisbursementList
        restHoldSalaryDisbursementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holdSalaryDisbursement.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHoldSalaryDisbursementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(holdSalaryDisbursementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHoldSalaryDisbursementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(holdSalaryDisbursementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHoldSalaryDisbursementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(holdSalaryDisbursementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHoldSalaryDisbursementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(holdSalaryDisbursementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHoldSalaryDisbursement() throws Exception {
        // Initialize the database
        holdSalaryDisbursementRepository.saveAndFlush(holdSalaryDisbursement);

        // Get the holdSalaryDisbursement
        restHoldSalaryDisbursementMockMvc
            .perform(get(ENTITY_API_URL_ID, holdSalaryDisbursement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(holdSalaryDisbursement.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHoldSalaryDisbursement() throws Exception {
        // Get the holdSalaryDisbursement
        restHoldSalaryDisbursementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHoldSalaryDisbursement() throws Exception {
        // Initialize the database
        holdSalaryDisbursementRepository.saveAndFlush(holdSalaryDisbursement);

        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();

        // Update the holdSalaryDisbursement
        HoldSalaryDisbursement updatedHoldSalaryDisbursement = holdSalaryDisbursementRepository
            .findById(holdSalaryDisbursement.getId())
            .get();
        // Disconnect from session so that the updates on updatedHoldSalaryDisbursement are not directly saved in db
        em.detach(updatedHoldSalaryDisbursement);
        updatedHoldSalaryDisbursement.date(UPDATED_DATE);
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(updatedHoldSalaryDisbursement);

        restHoldSalaryDisbursementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holdSalaryDisbursementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isOk());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
        HoldSalaryDisbursement testHoldSalaryDisbursement = holdSalaryDisbursementList.get(holdSalaryDisbursementList.size() - 1);
        assertThat(testHoldSalaryDisbursement.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingHoldSalaryDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();
        holdSalaryDisbursement.setId(count.incrementAndGet());

        // Create the HoldSalaryDisbursement
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoldSalaryDisbursementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holdSalaryDisbursementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHoldSalaryDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();
        holdSalaryDisbursement.setId(count.incrementAndGet());

        // Create the HoldSalaryDisbursement
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldSalaryDisbursementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHoldSalaryDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();
        holdSalaryDisbursement.setId(count.incrementAndGet());

        // Create the HoldSalaryDisbursement
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldSalaryDisbursementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHoldSalaryDisbursementWithPatch() throws Exception {
        // Initialize the database
        holdSalaryDisbursementRepository.saveAndFlush(holdSalaryDisbursement);

        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();

        // Update the holdSalaryDisbursement using partial update
        HoldSalaryDisbursement partialUpdatedHoldSalaryDisbursement = new HoldSalaryDisbursement();
        partialUpdatedHoldSalaryDisbursement.setId(holdSalaryDisbursement.getId());

        restHoldSalaryDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoldSalaryDisbursement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoldSalaryDisbursement))
            )
            .andExpect(status().isOk());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
        HoldSalaryDisbursement testHoldSalaryDisbursement = holdSalaryDisbursementList.get(holdSalaryDisbursementList.size() - 1);
        assertThat(testHoldSalaryDisbursement.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateHoldSalaryDisbursementWithPatch() throws Exception {
        // Initialize the database
        holdSalaryDisbursementRepository.saveAndFlush(holdSalaryDisbursement);

        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();

        // Update the holdSalaryDisbursement using partial update
        HoldSalaryDisbursement partialUpdatedHoldSalaryDisbursement = new HoldSalaryDisbursement();
        partialUpdatedHoldSalaryDisbursement.setId(holdSalaryDisbursement.getId());

        partialUpdatedHoldSalaryDisbursement.date(UPDATED_DATE);

        restHoldSalaryDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoldSalaryDisbursement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoldSalaryDisbursement))
            )
            .andExpect(status().isOk());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
        HoldSalaryDisbursement testHoldSalaryDisbursement = holdSalaryDisbursementList.get(holdSalaryDisbursementList.size() - 1);
        assertThat(testHoldSalaryDisbursement.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingHoldSalaryDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();
        holdSalaryDisbursement.setId(count.incrementAndGet());

        // Create the HoldSalaryDisbursement
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoldSalaryDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, holdSalaryDisbursementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHoldSalaryDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();
        holdSalaryDisbursement.setId(count.incrementAndGet());

        // Create the HoldSalaryDisbursement
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldSalaryDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHoldSalaryDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdSalaryDisbursementRepository.findAll().size();
        holdSalaryDisbursement.setId(count.incrementAndGet());

        // Create the HoldSalaryDisbursement
        HoldSalaryDisbursementDTO holdSalaryDisbursementDTO = holdSalaryDisbursementMapper.toDto(holdSalaryDisbursement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldSalaryDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holdSalaryDisbursementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HoldSalaryDisbursement in the database
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHoldSalaryDisbursement() throws Exception {
        // Initialize the database
        holdSalaryDisbursementRepository.saveAndFlush(holdSalaryDisbursement);

        int databaseSizeBeforeDelete = holdSalaryDisbursementRepository.findAll().size();

        // Delete the holdSalaryDisbursement
        restHoldSalaryDisbursementMockMvc
            .perform(delete(ENTITY_API_URL_ID, holdSalaryDisbursement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HoldSalaryDisbursement> holdSalaryDisbursementList = holdSalaryDisbursementRepository.findAll();
        assertThat(holdSalaryDisbursementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
