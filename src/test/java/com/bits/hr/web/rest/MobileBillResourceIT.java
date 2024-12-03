package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.MobileBill;
import com.bits.hr.repository.MobileBillRepository;
import com.bits.hr.service.dto.MobileBillDTO;
import com.bits.hr.service.mapper.MobileBillMapper;
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
 * Integration tests for the {@link MobileBillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MobileBillResourceIT {

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/mobile-bills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MobileBillRepository mobileBillRepository;

    @Autowired
    private MobileBillMapper mobileBillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMobileBillMockMvc;

    private MobileBill mobileBill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileBill createEntity(EntityManager em) {
        MobileBill mobileBill = new MobileBill().month(DEFAULT_MONTH).amount(DEFAULT_AMOUNT).year(DEFAULT_YEAR);
        return mobileBill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileBill createUpdatedEntity(EntityManager em) {
        MobileBill mobileBill = new MobileBill().month(UPDATED_MONTH).amount(UPDATED_AMOUNT).year(UPDATED_YEAR);
        return mobileBill;
    }

    @BeforeEach
    public void initTest() {
        mobileBill = createEntity(em);
    }

    @Test
    @Transactional
    void createMobileBill() throws Exception {
        int databaseSizeBeforeCreate = mobileBillRepository.findAll().size();
        // Create the MobileBill
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(mobileBill);
        restMobileBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileBillDTO)))
            .andExpect(status().isCreated());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeCreate + 1);
        MobileBill testMobileBill = mobileBillList.get(mobileBillList.size() - 1);
        assertThat(testMobileBill.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testMobileBill.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMobileBill.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void createMobileBillWithExistingId() throws Exception {
        // Create the MobileBill with an existing ID
        mobileBill.setId(1L);
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(mobileBill);

        int databaseSizeBeforeCreate = mobileBillRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMobileBillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileBillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMobileBills() throws Exception {
        // Initialize the database
        mobileBillRepository.saveAndFlush(mobileBill);

        // Get all the mobileBillList
        restMobileBillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mobileBill.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getMobileBill() throws Exception {
        // Initialize the database
        mobileBillRepository.saveAndFlush(mobileBill);

        // Get the mobileBill
        restMobileBillMockMvc
            .perform(get(ENTITY_API_URL_ID, mobileBill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mobileBill.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingMobileBill() throws Exception {
        // Get the mobileBill
        restMobileBillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMobileBill() throws Exception {
        // Initialize the database
        mobileBillRepository.saveAndFlush(mobileBill);

        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();

        // Update the mobileBill
        MobileBill updatedMobileBill = mobileBillRepository.findById(mobileBill.getId()).get();
        // Disconnect from session so that the updates on updatedMobileBill are not directly saved in db
        em.detach(updatedMobileBill);
        updatedMobileBill.month(UPDATED_MONTH).amount(UPDATED_AMOUNT).year(UPDATED_YEAR);
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(updatedMobileBill);

        restMobileBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mobileBillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileBillDTO))
            )
            .andExpect(status().isOk());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
        MobileBill testMobileBill = mobileBillList.get(mobileBillList.size() - 1);
        assertThat(testMobileBill.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testMobileBill.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMobileBill.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingMobileBill() throws Exception {
        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();
        mobileBill.setId(count.incrementAndGet());

        // Create the MobileBill
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(mobileBill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mobileBillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileBillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMobileBill() throws Exception {
        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();
        mobileBill.setId(count.incrementAndGet());

        // Create the MobileBill
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(mobileBill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileBillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileBillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMobileBill() throws Exception {
        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();
        mobileBill.setId(count.incrementAndGet());

        // Create the MobileBill
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(mobileBill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileBillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileBillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMobileBillWithPatch() throws Exception {
        // Initialize the database
        mobileBillRepository.saveAndFlush(mobileBill);

        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();

        // Update the mobileBill using partial update
        MobileBill partialUpdatedMobileBill = new MobileBill();
        partialUpdatedMobileBill.setId(mobileBill.getId());

        partialUpdatedMobileBill.month(UPDATED_MONTH);

        restMobileBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMobileBill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMobileBill))
            )
            .andExpect(status().isOk());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
        MobileBill testMobileBill = mobileBillList.get(mobileBillList.size() - 1);
        assertThat(testMobileBill.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testMobileBill.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMobileBill.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateMobileBillWithPatch() throws Exception {
        // Initialize the database
        mobileBillRepository.saveAndFlush(mobileBill);

        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();

        // Update the mobileBill using partial update
        MobileBill partialUpdatedMobileBill = new MobileBill();
        partialUpdatedMobileBill.setId(mobileBill.getId());

        partialUpdatedMobileBill.month(UPDATED_MONTH).amount(UPDATED_AMOUNT).year(UPDATED_YEAR);

        restMobileBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMobileBill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMobileBill))
            )
            .andExpect(status().isOk());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
        MobileBill testMobileBill = mobileBillList.get(mobileBillList.size() - 1);
        assertThat(testMobileBill.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testMobileBill.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMobileBill.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingMobileBill() throws Exception {
        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();
        mobileBill.setId(count.incrementAndGet());

        // Create the MobileBill
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(mobileBill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mobileBillDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mobileBillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMobileBill() throws Exception {
        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();
        mobileBill.setId(count.incrementAndGet());

        // Create the MobileBill
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(mobileBill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileBillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mobileBillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMobileBill() throws Exception {
        int databaseSizeBeforeUpdate = mobileBillRepository.findAll().size();
        mobileBill.setId(count.incrementAndGet());

        // Create the MobileBill
        MobileBillDTO mobileBillDTO = mobileBillMapper.toDto(mobileBill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileBillMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mobileBillDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MobileBill in the database
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMobileBill() throws Exception {
        // Initialize the database
        mobileBillRepository.saveAndFlush(mobileBill);

        int databaseSizeBeforeDelete = mobileBillRepository.findAll().size();

        // Delete the mobileBill
        restMobileBillMockMvc
            .perform(delete(ENTITY_API_URL_ID, mobileBill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MobileBill> mobileBillList = mobileBillRepository.findAll();
        assertThat(mobileBillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
