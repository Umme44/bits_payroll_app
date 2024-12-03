package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.AitPayment;
import com.bits.hr.repository.AitPaymentRepository;
import com.bits.hr.service.dto.AitPaymentDTO;
import com.bits.hr.service.mapper.AitPaymentMapper;
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
 * Integration tests for the {@link AitPaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AitPaymentResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ait-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AitPaymentRepository aitPaymentRepository;

    @Autowired
    private AitPaymentMapper aitPaymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAitPaymentMockMvc;

    private AitPayment aitPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AitPayment createEntity(EntityManager em) {
        AitPayment aitPayment = new AitPayment().date(DEFAULT_DATE).amount(DEFAULT_AMOUNT).description(DEFAULT_DESCRIPTION);
        return aitPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AitPayment createUpdatedEntity(EntityManager em) {
        AitPayment aitPayment = new AitPayment().date(UPDATED_DATE).amount(UPDATED_AMOUNT).description(UPDATED_DESCRIPTION);
        return aitPayment;
    }

    @BeforeEach
    public void initTest() {
        aitPayment = createEntity(em);
    }

    @Test
    @Transactional
    void createAitPayment() throws Exception {
        int databaseSizeBeforeCreate = aitPaymentRepository.findAll().size();
        // Create the AitPayment
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(aitPayment);
        restAitPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO)))
            .andExpect(status().isCreated());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        AitPayment testAitPayment = aitPaymentList.get(aitPaymentList.size() - 1);
        assertThat(testAitPayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAitPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAitPayment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createAitPaymentWithExistingId() throws Exception {
        // Create the AitPayment with an existing ID
        aitPayment.setId(1L);
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(aitPayment);

        int databaseSizeBeforeCreate = aitPaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAitPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAitPayments() throws Exception {
        // Initialize the database
        aitPaymentRepository.saveAndFlush(aitPayment);

        // Get all the aitPaymentList
        restAitPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aitPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAitPayment() throws Exception {
        // Initialize the database
        aitPaymentRepository.saveAndFlush(aitPayment);

        // Get the aitPayment
        restAitPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, aitPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aitPayment.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingAitPayment() throws Exception {
        // Get the aitPayment
        restAitPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAitPayment() throws Exception {
        // Initialize the database
        aitPaymentRepository.saveAndFlush(aitPayment);

        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();

        // Update the aitPayment
        AitPayment updatedAitPayment = aitPaymentRepository.findById(aitPayment.getId()).get();
        // Disconnect from session so that the updates on updatedAitPayment are not directly saved in db
        em.detach(updatedAitPayment);
        updatedAitPayment.date(UPDATED_DATE).amount(UPDATED_AMOUNT).description(UPDATED_DESCRIPTION);
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(updatedAitPayment);

        restAitPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aitPaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
        AitPayment testAitPayment = aitPaymentList.get(aitPaymentList.size() - 1);
        assertThat(testAitPayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAitPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAitPayment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingAitPayment() throws Exception {
        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();
        aitPayment.setId(count.incrementAndGet());

        // Create the AitPayment
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(aitPayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAitPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aitPaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAitPayment() throws Exception {
        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();
        aitPayment.setId(count.incrementAndGet());

        // Create the AitPayment
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(aitPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAitPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAitPayment() throws Exception {
        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();
        aitPayment.setId(count.incrementAndGet());

        // Create the AitPayment
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(aitPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAitPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAitPaymentWithPatch() throws Exception {
        // Initialize the database
        aitPaymentRepository.saveAndFlush(aitPayment);

        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();

        // Update the aitPayment using partial update
        AitPayment partialUpdatedAitPayment = new AitPayment();
        partialUpdatedAitPayment.setId(aitPayment.getId());

        partialUpdatedAitPayment.amount(UPDATED_AMOUNT).description(UPDATED_DESCRIPTION);

        restAitPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAitPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAitPayment))
            )
            .andExpect(status().isOk());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
        AitPayment testAitPayment = aitPaymentList.get(aitPaymentList.size() - 1);
        assertThat(testAitPayment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAitPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAitPayment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateAitPaymentWithPatch() throws Exception {
        // Initialize the database
        aitPaymentRepository.saveAndFlush(aitPayment);

        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();

        // Update the aitPayment using partial update
        AitPayment partialUpdatedAitPayment = new AitPayment();
        partialUpdatedAitPayment.setId(aitPayment.getId());

        partialUpdatedAitPayment.date(UPDATED_DATE).amount(UPDATED_AMOUNT).description(UPDATED_DESCRIPTION);

        restAitPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAitPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAitPayment))
            )
            .andExpect(status().isOk());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
        AitPayment testAitPayment = aitPaymentList.get(aitPaymentList.size() - 1);
        assertThat(testAitPayment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAitPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAitPayment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingAitPayment() throws Exception {
        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();
        aitPayment.setId(count.incrementAndGet());

        // Create the AitPayment
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(aitPayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAitPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aitPaymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAitPayment() throws Exception {
        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();
        aitPayment.setId(count.incrementAndGet());

        // Create the AitPayment
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(aitPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAitPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAitPayment() throws Exception {
        int databaseSizeBeforeUpdate = aitPaymentRepository.findAll().size();
        aitPayment.setId(count.incrementAndGet());

        // Create the AitPayment
        AitPaymentDTO aitPaymentDTO = aitPaymentMapper.toDto(aitPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAitPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aitPaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AitPayment in the database
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAitPayment() throws Exception {
        // Initialize the database
        aitPaymentRepository.saveAndFlush(aitPayment);

        int databaseSizeBeforeDelete = aitPaymentRepository.findAll().size();

        // Delete the aitPayment
        restAitPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, aitPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AitPayment> aitPaymentList = aitPaymentRepository.findAll();
        assertThat(aitPaymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
