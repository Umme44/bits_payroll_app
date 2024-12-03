package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.HoldFbDisbursement;
import com.bits.hr.domain.User;
import com.bits.hr.repository.HoldFbDisbursementRepository;
import com.bits.hr.service.HoldFbDisbursementService;
import com.bits.hr.service.dto.HoldFbDisbursementDTO;
import com.bits.hr.service.mapper.HoldFbDisbursementMapper;
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
 * Integration tests for the {@link HoldFbDisbursementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HoldFbDisbursementResourceIT {

    private static final LocalDate DEFAULT_DISBURSED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DISBURSED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hold-fb-disbursements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HoldFbDisbursementRepository holdFbDisbursementRepository;

    @Mock
    private HoldFbDisbursementRepository holdFbDisbursementRepositoryMock;

    @Autowired
    private HoldFbDisbursementMapper holdFbDisbursementMapper;

    @Mock
    private HoldFbDisbursementService holdFbDisbursementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHoldFbDisbursementMockMvc;

    private HoldFbDisbursement holdFbDisbursement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HoldFbDisbursement createEntity(EntityManager em) {
        HoldFbDisbursement holdFbDisbursement = new HoldFbDisbursement().disbursedAt(DEFAULT_DISBURSED_AT).remarks(DEFAULT_REMARKS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        holdFbDisbursement.setDisbursedBy(user);
        // Add required entity
        FestivalBonusDetails festivalBonusDetails;
        if (TestUtil.findAll(em, FestivalBonusDetails.class).isEmpty()) {
            festivalBonusDetails = FestivalBonusDetailsResourceIT.createEntity(em);
            em.persist(festivalBonusDetails);
            em.flush();
        } else {
            festivalBonusDetails = TestUtil.findAll(em, FestivalBonusDetails.class).get(0);
        }
        holdFbDisbursement.setFestivalBonusDetail(festivalBonusDetails);
        return holdFbDisbursement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HoldFbDisbursement createUpdatedEntity(EntityManager em) {
        HoldFbDisbursement holdFbDisbursement = new HoldFbDisbursement().disbursedAt(UPDATED_DISBURSED_AT).remarks(UPDATED_REMARKS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        holdFbDisbursement.setDisbursedBy(user);
        // Add required entity
        FestivalBonusDetails festivalBonusDetails;
        if (TestUtil.findAll(em, FestivalBonusDetails.class).isEmpty()) {
            festivalBonusDetails = FestivalBonusDetailsResourceIT.createUpdatedEntity(em);
            em.persist(festivalBonusDetails);
            em.flush();
        } else {
            festivalBonusDetails = TestUtil.findAll(em, FestivalBonusDetails.class).get(0);
        }
        holdFbDisbursement.setFestivalBonusDetail(festivalBonusDetails);
        return holdFbDisbursement;
    }

    @BeforeEach
    public void initTest() {
        holdFbDisbursement = createEntity(em);
    }

    @Test
    @Transactional
    void createHoldFbDisbursement() throws Exception {
        int databaseSizeBeforeCreate = holdFbDisbursementRepository.findAll().size();
        // Create the HoldFbDisbursement
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);
        restHoldFbDisbursementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeCreate + 1);
        HoldFbDisbursement testHoldFbDisbursement = holdFbDisbursementList.get(holdFbDisbursementList.size() - 1);
        assertThat(testHoldFbDisbursement.getDisbursedAt()).isEqualTo(DEFAULT_DISBURSED_AT);
        assertThat(testHoldFbDisbursement.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createHoldFbDisbursementWithExistingId() throws Exception {
        // Create the HoldFbDisbursement with an existing ID
        holdFbDisbursement.setId(1L);
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

        int databaseSizeBeforeCreate = holdFbDisbursementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHoldFbDisbursementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDisbursedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = holdFbDisbursementRepository.findAll().size();
        // set the field null
        holdFbDisbursement.setDisbursedAt(null);

        // Create the HoldFbDisbursement, which fails.
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

        restHoldFbDisbursementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHoldFbDisbursements() throws Exception {
        // Initialize the database
        holdFbDisbursementRepository.saveAndFlush(holdFbDisbursement);

        // Get all the holdFbDisbursementList
        restHoldFbDisbursementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holdFbDisbursement.getId().intValue())))
            .andExpect(jsonPath("$.[*].disbursedAt").value(hasItem(DEFAULT_DISBURSED_AT.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHoldFbDisbursementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(holdFbDisbursementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHoldFbDisbursementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(holdFbDisbursementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHoldFbDisbursementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(holdFbDisbursementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHoldFbDisbursementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(holdFbDisbursementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHoldFbDisbursement() throws Exception {
        // Initialize the database
        holdFbDisbursementRepository.saveAndFlush(holdFbDisbursement);

        // Get the holdFbDisbursement
        restHoldFbDisbursementMockMvc
            .perform(get(ENTITY_API_URL_ID, holdFbDisbursement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(holdFbDisbursement.getId().intValue()))
            .andExpect(jsonPath("$.disbursedAt").value(DEFAULT_DISBURSED_AT.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getNonExistingHoldFbDisbursement() throws Exception {
        // Get the holdFbDisbursement
        restHoldFbDisbursementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHoldFbDisbursement() throws Exception {
        // Initialize the database
        holdFbDisbursementRepository.saveAndFlush(holdFbDisbursement);

        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();

        // Update the holdFbDisbursement
        HoldFbDisbursement updatedHoldFbDisbursement = holdFbDisbursementRepository.findById(holdFbDisbursement.getId()).get();
        // Disconnect from session so that the updates on updatedHoldFbDisbursement are not directly saved in db
        em.detach(updatedHoldFbDisbursement);
        updatedHoldFbDisbursement.disbursedAt(UPDATED_DISBURSED_AT).remarks(UPDATED_REMARKS);
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(updatedHoldFbDisbursement);

        restHoldFbDisbursementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holdFbDisbursementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isOk());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
        HoldFbDisbursement testHoldFbDisbursement = holdFbDisbursementList.get(holdFbDisbursementList.size() - 1);
        assertThat(testHoldFbDisbursement.getDisbursedAt()).isEqualTo(UPDATED_DISBURSED_AT);
        assertThat(testHoldFbDisbursement.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingHoldFbDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();
        holdFbDisbursement.setId(count.incrementAndGet());

        // Create the HoldFbDisbursement
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoldFbDisbursementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holdFbDisbursementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHoldFbDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();
        holdFbDisbursement.setId(count.incrementAndGet());

        // Create the HoldFbDisbursement
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldFbDisbursementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHoldFbDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();
        holdFbDisbursement.setId(count.incrementAndGet());

        // Create the HoldFbDisbursement
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldFbDisbursementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHoldFbDisbursementWithPatch() throws Exception {
        // Initialize the database
        holdFbDisbursementRepository.saveAndFlush(holdFbDisbursement);

        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();

        // Update the holdFbDisbursement using partial update
        HoldFbDisbursement partialUpdatedHoldFbDisbursement = new HoldFbDisbursement();
        partialUpdatedHoldFbDisbursement.setId(holdFbDisbursement.getId());

        partialUpdatedHoldFbDisbursement.disbursedAt(UPDATED_DISBURSED_AT);

        restHoldFbDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoldFbDisbursement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoldFbDisbursement))
            )
            .andExpect(status().isOk());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
        HoldFbDisbursement testHoldFbDisbursement = holdFbDisbursementList.get(holdFbDisbursementList.size() - 1);
        assertThat(testHoldFbDisbursement.getDisbursedAt()).isEqualTo(UPDATED_DISBURSED_AT);
        assertThat(testHoldFbDisbursement.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateHoldFbDisbursementWithPatch() throws Exception {
        // Initialize the database
        holdFbDisbursementRepository.saveAndFlush(holdFbDisbursement);

        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();

        // Update the holdFbDisbursement using partial update
        HoldFbDisbursement partialUpdatedHoldFbDisbursement = new HoldFbDisbursement();
        partialUpdatedHoldFbDisbursement.setId(holdFbDisbursement.getId());

        partialUpdatedHoldFbDisbursement.disbursedAt(UPDATED_DISBURSED_AT).remarks(UPDATED_REMARKS);

        restHoldFbDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoldFbDisbursement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoldFbDisbursement))
            )
            .andExpect(status().isOk());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
        HoldFbDisbursement testHoldFbDisbursement = holdFbDisbursementList.get(holdFbDisbursementList.size() - 1);
        assertThat(testHoldFbDisbursement.getDisbursedAt()).isEqualTo(UPDATED_DISBURSED_AT);
        assertThat(testHoldFbDisbursement.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingHoldFbDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();
        holdFbDisbursement.setId(count.incrementAndGet());

        // Create the HoldFbDisbursement
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHoldFbDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, holdFbDisbursementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHoldFbDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();
        holdFbDisbursement.setId(count.incrementAndGet());

        // Create the HoldFbDisbursement
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldFbDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHoldFbDisbursement() throws Exception {
        int databaseSizeBeforeUpdate = holdFbDisbursementRepository.findAll().size();
        holdFbDisbursement.setId(count.incrementAndGet());

        // Create the HoldFbDisbursement
        HoldFbDisbursementDTO holdFbDisbursementDTO = holdFbDisbursementMapper.toDto(holdFbDisbursement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHoldFbDisbursementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holdFbDisbursementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HoldFbDisbursement in the database
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHoldFbDisbursement() throws Exception {
        // Initialize the database
        holdFbDisbursementRepository.saveAndFlush(holdFbDisbursement);

        int databaseSizeBeforeDelete = holdFbDisbursementRepository.findAll().size();

        // Delete the holdFbDisbursement
        restHoldFbDisbursementMockMvc
            .perform(delete(ENTITY_API_URL_ID, holdFbDisbursement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HoldFbDisbursement> holdFbDisbursementList = holdFbDisbursementRepository.findAll();
        assertThat(holdFbDisbursementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
