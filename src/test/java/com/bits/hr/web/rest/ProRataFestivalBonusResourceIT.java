package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.ProRataFestivalBonus;
import com.bits.hr.repository.ProRataFestivalBonusRepository;
import com.bits.hr.service.dto.ProRataFestivalBonusDTO;
import com.bits.hr.service.mapper.ProRataFestivalBonusMapper;
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
 * Integration tests for the {@link ProRataFestivalBonusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProRataFestivalBonusResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pro-rata-festival-bonuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProRataFestivalBonusRepository proRataFestivalBonusRepository;

    @Autowired
    private ProRataFestivalBonusMapper proRataFestivalBonusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProRataFestivalBonusMockMvc;

    private ProRataFestivalBonus proRataFestivalBonus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProRataFestivalBonus createEntity(EntityManager em) {
        ProRataFestivalBonus proRataFestivalBonus = new ProRataFestivalBonus()
            .date(DEFAULT_DATE)
            .amount(DEFAULT_AMOUNT)
            .description(DEFAULT_DESCRIPTION);
        return proRataFestivalBonus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProRataFestivalBonus createUpdatedEntity(EntityManager em) {
        ProRataFestivalBonus proRataFestivalBonus = new ProRataFestivalBonus()
            .date(UPDATED_DATE)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION);
        return proRataFestivalBonus;
    }

    @BeforeEach
    public void initTest() {
        proRataFestivalBonus = createEntity(em);
    }

    @Test
    @Transactional
    void createProRataFestivalBonus() throws Exception {
        int databaseSizeBeforeCreate = proRataFestivalBonusRepository.findAll().size();
        // Create the ProRataFestivalBonus
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(proRataFestivalBonus);
        restProRataFestivalBonusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeCreate + 1);
        ProRataFestivalBonus testProRataFestivalBonus = proRataFestivalBonusList.get(proRataFestivalBonusList.size() - 1);
        assertThat(testProRataFestivalBonus.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProRataFestivalBonus.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testProRataFestivalBonus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createProRataFestivalBonusWithExistingId() throws Exception {
        // Create the ProRataFestivalBonus with an existing ID
        proRataFestivalBonus.setId(1L);
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(proRataFestivalBonus);

        int databaseSizeBeforeCreate = proRataFestivalBonusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProRataFestivalBonusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProRataFestivalBonuses() throws Exception {
        // Initialize the database
        proRataFestivalBonusRepository.saveAndFlush(proRataFestivalBonus);

        // Get all the proRataFestivalBonusList
        restProRataFestivalBonusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proRataFestivalBonus.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getProRataFestivalBonus() throws Exception {
        // Initialize the database
        proRataFestivalBonusRepository.saveAndFlush(proRataFestivalBonus);

        // Get the proRataFestivalBonus
        restProRataFestivalBonusMockMvc
            .perform(get(ENTITY_API_URL_ID, proRataFestivalBonus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proRataFestivalBonus.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingProRataFestivalBonus() throws Exception {
        // Get the proRataFestivalBonus
        restProRataFestivalBonusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProRataFestivalBonus() throws Exception {
        // Initialize the database
        proRataFestivalBonusRepository.saveAndFlush(proRataFestivalBonus);

        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();

        // Update the proRataFestivalBonus
        ProRataFestivalBonus updatedProRataFestivalBonus = proRataFestivalBonusRepository.findById(proRataFestivalBonus.getId()).get();
        // Disconnect from session so that the updates on updatedProRataFestivalBonus are not directly saved in db
        em.detach(updatedProRataFestivalBonus);
        updatedProRataFestivalBonus.date(UPDATED_DATE).amount(UPDATED_AMOUNT).description(UPDATED_DESCRIPTION);
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(updatedProRataFestivalBonus);

        restProRataFestivalBonusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proRataFestivalBonusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
        ProRataFestivalBonus testProRataFestivalBonus = proRataFestivalBonusList.get(proRataFestivalBonusList.size() - 1);
        assertThat(testProRataFestivalBonus.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProRataFestivalBonus.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testProRataFestivalBonus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProRataFestivalBonus() throws Exception {
        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();
        proRataFestivalBonus.setId(count.incrementAndGet());

        // Create the ProRataFestivalBonus
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(proRataFestivalBonus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProRataFestivalBonusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proRataFestivalBonusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProRataFestivalBonus() throws Exception {
        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();
        proRataFestivalBonus.setId(count.incrementAndGet());

        // Create the ProRataFestivalBonus
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(proRataFestivalBonus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProRataFestivalBonusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProRataFestivalBonus() throws Exception {
        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();
        proRataFestivalBonus.setId(count.incrementAndGet());

        // Create the ProRataFestivalBonus
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(proRataFestivalBonus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProRataFestivalBonusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProRataFestivalBonusWithPatch() throws Exception {
        // Initialize the database
        proRataFestivalBonusRepository.saveAndFlush(proRataFestivalBonus);

        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();

        // Update the proRataFestivalBonus using partial update
        ProRataFestivalBonus partialUpdatedProRataFestivalBonus = new ProRataFestivalBonus();
        partialUpdatedProRataFestivalBonus.setId(proRataFestivalBonus.getId());

        partialUpdatedProRataFestivalBonus.date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

        restProRataFestivalBonusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProRataFestivalBonus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProRataFestivalBonus))
            )
            .andExpect(status().isOk());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
        ProRataFestivalBonus testProRataFestivalBonus = proRataFestivalBonusList.get(proRataFestivalBonusList.size() - 1);
        assertThat(testProRataFestivalBonus.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProRataFestivalBonus.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testProRataFestivalBonus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProRataFestivalBonusWithPatch() throws Exception {
        // Initialize the database
        proRataFestivalBonusRepository.saveAndFlush(proRataFestivalBonus);

        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();

        // Update the proRataFestivalBonus using partial update
        ProRataFestivalBonus partialUpdatedProRataFestivalBonus = new ProRataFestivalBonus();
        partialUpdatedProRataFestivalBonus.setId(proRataFestivalBonus.getId());

        partialUpdatedProRataFestivalBonus.date(UPDATED_DATE).amount(UPDATED_AMOUNT).description(UPDATED_DESCRIPTION);

        restProRataFestivalBonusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProRataFestivalBonus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProRataFestivalBonus))
            )
            .andExpect(status().isOk());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
        ProRataFestivalBonus testProRataFestivalBonus = proRataFestivalBonusList.get(proRataFestivalBonusList.size() - 1);
        assertThat(testProRataFestivalBonus.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProRataFestivalBonus.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testProRataFestivalBonus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProRataFestivalBonus() throws Exception {
        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();
        proRataFestivalBonus.setId(count.incrementAndGet());

        // Create the ProRataFestivalBonus
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(proRataFestivalBonus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProRataFestivalBonusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proRataFestivalBonusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProRataFestivalBonus() throws Exception {
        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();
        proRataFestivalBonus.setId(count.incrementAndGet());

        // Create the ProRataFestivalBonus
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(proRataFestivalBonus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProRataFestivalBonusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProRataFestivalBonus() throws Exception {
        int databaseSizeBeforeUpdate = proRataFestivalBonusRepository.findAll().size();
        proRataFestivalBonus.setId(count.incrementAndGet());

        // Create the ProRataFestivalBonus
        ProRataFestivalBonusDTO proRataFestivalBonusDTO = proRataFestivalBonusMapper.toDto(proRataFestivalBonus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProRataFestivalBonusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proRataFestivalBonusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProRataFestivalBonus in the database
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProRataFestivalBonus() throws Exception {
        // Initialize the database
        proRataFestivalBonusRepository.saveAndFlush(proRataFestivalBonus);

        int databaseSizeBeforeDelete = proRataFestivalBonusRepository.findAll().size();

        // Delete the proRataFestivalBonus
        restProRataFestivalBonusMockMvc
            .perform(delete(ENTITY_API_URL_ID, proRataFestivalBonus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProRataFestivalBonus> proRataFestivalBonusList = proRataFestivalBonusRepository.findAll();
        assertThat(proRataFestivalBonusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
