package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.PfArrear;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.PfArrearRepository;
import com.bits.hr.service.dto.PfArrearDTO;
import com.bits.hr.service.mapper.PfArrearMapper;
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
 * Integration tests for the {@link PfArrearResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PfArrearResourceIT {

    private static final Month DEFAULT_MONTH = Month.JANUARY;
    private static final Month UPDATED_MONTH = Month.FEBRUARY;

    private static final Integer DEFAULT_YEAR = 1900;
    private static final Integer UPDATED_YEAR = 1901;

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pf-arrears";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PfArrearRepository pfArrearRepository;

    @Autowired
    private PfArrearMapper pfArrearMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPfArrearMockMvc;

    private PfArrear pfArrear;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfArrear createEntity(EntityManager em) {
        PfArrear pfArrear = new PfArrear().month(DEFAULT_MONTH).year(DEFAULT_YEAR).amount(DEFAULT_AMOUNT).remarks(DEFAULT_REMARKS);
        return pfArrear;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PfArrear createUpdatedEntity(EntityManager em) {
        PfArrear pfArrear = new PfArrear().month(UPDATED_MONTH).year(UPDATED_YEAR).amount(UPDATED_AMOUNT).remarks(UPDATED_REMARKS);
        return pfArrear;
    }

    @BeforeEach
    public void initTest() {
        pfArrear = createEntity(em);
    }

    @Test
    @Transactional
    void createPfArrear() throws Exception {
        int databaseSizeBeforeCreate = pfArrearRepository.findAll().size();
        // Create the PfArrear
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);
        restPfArrearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfArrearDTO)))
            .andExpect(status().isCreated());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeCreate + 1);
        PfArrear testPfArrear = pfArrearList.get(pfArrearList.size() - 1);
        assertThat(testPfArrear.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testPfArrear.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPfArrear.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPfArrear.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createPfArrearWithExistingId() throws Exception {
        // Create the PfArrear with an existing ID
        pfArrear.setId(1L);
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        int databaseSizeBeforeCreate = pfArrearRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPfArrearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfArrearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = pfArrearRepository.findAll().size();
        // set the field null
        pfArrear.setMonth(null);

        // Create the PfArrear, which fails.
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        restPfArrearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfArrearDTO)))
            .andExpect(status().isBadRequest());

        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = pfArrearRepository.findAll().size();
        // set the field null
        pfArrear.setYear(null);

        // Create the PfArrear, which fails.
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        restPfArrearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfArrearDTO)))
            .andExpect(status().isBadRequest());

        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = pfArrearRepository.findAll().size();
        // set the field null
        pfArrear.setAmount(null);

        // Create the PfArrear, which fails.
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        restPfArrearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfArrearDTO)))
            .andExpect(status().isBadRequest());

        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemarksIsRequired() throws Exception {
        int databaseSizeBeforeTest = pfArrearRepository.findAll().size();
        // set the field null
        pfArrear.setRemarks(null);

        // Create the PfArrear, which fails.
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        restPfArrearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfArrearDTO)))
            .andExpect(status().isBadRequest());

        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPfArrears() throws Exception {
        // Initialize the database
        pfArrearRepository.saveAndFlush(pfArrear);

        // Get all the pfArrearList
        restPfArrearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pfArrear.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getPfArrear() throws Exception {
        // Initialize the database
        pfArrearRepository.saveAndFlush(pfArrear);

        // Get the pfArrear
        restPfArrearMockMvc
            .perform(get(ENTITY_API_URL_ID, pfArrear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pfArrear.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getNonExistingPfArrear() throws Exception {
        // Get the pfArrear
        restPfArrearMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPfArrear() throws Exception {
        // Initialize the database
        pfArrearRepository.saveAndFlush(pfArrear);

        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();

        // Update the pfArrear
        PfArrear updatedPfArrear = pfArrearRepository.findById(pfArrear.getId()).get();
        // Disconnect from session so that the updates on updatedPfArrear are not directly saved in db
        em.detach(updatedPfArrear);
        updatedPfArrear.month(UPDATED_MONTH).year(UPDATED_YEAR).amount(UPDATED_AMOUNT).remarks(UPDATED_REMARKS);
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(updatedPfArrear);

        restPfArrearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfArrearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfArrearDTO))
            )
            .andExpect(status().isOk());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
        PfArrear testPfArrear = pfArrearList.get(pfArrearList.size() - 1);
        assertThat(testPfArrear.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testPfArrear.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPfArrear.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPfArrear.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingPfArrear() throws Exception {
        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();
        pfArrear.setId(count.incrementAndGet());

        // Create the PfArrear
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfArrearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pfArrearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfArrearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPfArrear() throws Exception {
        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();
        pfArrear.setId(count.incrementAndGet());

        // Create the PfArrear
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfArrearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pfArrearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPfArrear() throws Exception {
        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();
        pfArrear.setId(count.incrementAndGet());

        // Create the PfArrear
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfArrearMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pfArrearDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePfArrearWithPatch() throws Exception {
        // Initialize the database
        pfArrearRepository.saveAndFlush(pfArrear);

        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();

        // Update the pfArrear using partial update
        PfArrear partialUpdatedPfArrear = new PfArrear();
        partialUpdatedPfArrear.setId(pfArrear.getId());

        restPfArrearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfArrear.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfArrear))
            )
            .andExpect(status().isOk());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
        PfArrear testPfArrear = pfArrearList.get(pfArrearList.size() - 1);
        assertThat(testPfArrear.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testPfArrear.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPfArrear.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPfArrear.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdatePfArrearWithPatch() throws Exception {
        // Initialize the database
        pfArrearRepository.saveAndFlush(pfArrear);

        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();

        // Update the pfArrear using partial update
        PfArrear partialUpdatedPfArrear = new PfArrear();
        partialUpdatedPfArrear.setId(pfArrear.getId());

        partialUpdatedPfArrear.month(UPDATED_MONTH).year(UPDATED_YEAR).amount(UPDATED_AMOUNT).remarks(UPDATED_REMARKS);

        restPfArrearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPfArrear.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPfArrear))
            )
            .andExpect(status().isOk());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
        PfArrear testPfArrear = pfArrearList.get(pfArrearList.size() - 1);
        assertThat(testPfArrear.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testPfArrear.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPfArrear.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPfArrear.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingPfArrear() throws Exception {
        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();
        pfArrear.setId(count.incrementAndGet());

        // Create the PfArrear
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPfArrearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pfArrearDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfArrearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPfArrear() throws Exception {
        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();
        pfArrear.setId(count.incrementAndGet());

        // Create the PfArrear
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfArrearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pfArrearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPfArrear() throws Exception {
        int databaseSizeBeforeUpdate = pfArrearRepository.findAll().size();
        pfArrear.setId(count.incrementAndGet());

        // Create the PfArrear
        PfArrearDTO pfArrearDTO = pfArrearMapper.toDto(pfArrear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPfArrearMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pfArrearDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PfArrear in the database
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePfArrear() throws Exception {
        // Initialize the database
        pfArrearRepository.saveAndFlush(pfArrear);

        int databaseSizeBeforeDelete = pfArrearRepository.findAll().size();

        // Delete the pfArrear
        restPfArrearMockMvc
            .perform(delete(ENTITY_API_URL_ID, pfArrear.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PfArrear> pfArrearList = pfArrearRepository.findAll();
        assertThat(pfArrearList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
