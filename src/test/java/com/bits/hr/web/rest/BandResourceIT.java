package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Band;
import com.bits.hr.repository.BandRepository;
import com.bits.hr.service.BandService;
import com.bits.hr.service.dto.BandDTO;
import com.bits.hr.service.mapper.BandMapper;
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
 * Integration tests for the {@link BandResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BandResourceIT {

    private static final String DEFAULT_BAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BAND_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_MIN_SALARY = 1D;
    private static final Double UPDATED_MIN_SALARY = 2D;

    private static final Double DEFAULT_MAX_SALARY = 1D;
    private static final Double UPDATED_MAX_SALARY = 2D;

    private static final Double DEFAULT_WELFARE_FUND = 0D;
    private static final Double UPDATED_WELFARE_FUND = 1D;

    private static final Double DEFAULT_MOBILE_CELLING = 0D;
    private static final Double UPDATED_MOBILE_CELLING = 1D;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/bands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BandRepository bandRepository;

    @Mock
    private BandRepository bandRepositoryMock;

    @Autowired
    private BandMapper bandMapper;

    @Mock
    private BandService bandServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBandMockMvc;

    private Band band;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Band createEntity(EntityManager em) {
        Band band = new Band()
            .bandName(DEFAULT_BAND_NAME)
            .minSalary(DEFAULT_MIN_SALARY)
            .maxSalary(DEFAULT_MAX_SALARY)
            .welfareFund(DEFAULT_WELFARE_FUND)
            .mobileCelling(DEFAULT_MOBILE_CELLING)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return band;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Band createUpdatedEntity(EntityManager em) {
        Band band = new Band()
            .bandName(UPDATED_BAND_NAME)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .welfareFund(UPDATED_WELFARE_FUND)
            .mobileCelling(UPDATED_MOBILE_CELLING)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return band;
    }

    @BeforeEach
    public void initTest() {
        band = createEntity(em);
    }

    @Test
    @Transactional
    void createBand() throws Exception {
        int databaseSizeBeforeCreate = bandRepository.findAll().size();
        // Create the Band
        BandDTO bandDTO = bandMapper.toDto(band);
        restBandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bandDTO)))
            .andExpect(status().isCreated());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeCreate + 1);
        Band testBand = bandList.get(bandList.size() - 1);
        assertThat(testBand.getBandName()).isEqualTo(DEFAULT_BAND_NAME);
        assertThat(testBand.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testBand.getMaxSalary()).isEqualTo(DEFAULT_MAX_SALARY);
        assertThat(testBand.getWelfareFund()).isEqualTo(DEFAULT_WELFARE_FUND);
        assertThat(testBand.getMobileCelling()).isEqualTo(DEFAULT_MOBILE_CELLING);
        assertThat(testBand.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testBand.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createBandWithExistingId() throws Exception {
        // Create the Band with an existing ID
        band.setId(1L);
        BandDTO bandDTO = bandMapper.toDto(band);

        int databaseSizeBeforeCreate = bandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBandNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bandRepository.findAll().size();
        // set the field null
        band.setBandName(null);

        // Create the Band, which fails.
        BandDTO bandDTO = bandMapper.toDto(band);

        restBandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bandDTO)))
            .andExpect(status().isBadRequest());

        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinSalaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = bandRepository.findAll().size();
        // set the field null
        band.setMinSalary(null);

        // Create the Band, which fails.
        BandDTO bandDTO = bandMapper.toDto(band);

        restBandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bandDTO)))
            .andExpect(status().isBadRequest());

        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxSalaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = bandRepository.findAll().size();
        // set the field null
        band.setMaxSalary(null);

        // Create the Band, which fails.
        BandDTO bandDTO = bandMapper.toDto(band);

        restBandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bandDTO)))
            .andExpect(status().isBadRequest());

        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBands() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get all the bandList
        restBandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(band.getId().intValue())))
            .andExpect(jsonPath("$.[*].bandName").value(hasItem(DEFAULT_BAND_NAME)))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(DEFAULT_MAX_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].welfareFund").value(hasItem(DEFAULT_WELFARE_FUND.doubleValue())))
            .andExpect(jsonPath("$.[*].mobileCelling").value(hasItem(DEFAULT_MOBILE_CELLING.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBandsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBandMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bandServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBandsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bandServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBandMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bandRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBand() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        // Get the band
        restBandMockMvc
            .perform(get(ENTITY_API_URL_ID, band.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(band.getId().intValue()))
            .andExpect(jsonPath("$.bandName").value(DEFAULT_BAND_NAME))
            .andExpect(jsonPath("$.minSalary").value(DEFAULT_MIN_SALARY.doubleValue()))
            .andExpect(jsonPath("$.maxSalary").value(DEFAULT_MAX_SALARY.doubleValue()))
            .andExpect(jsonPath("$.welfareFund").value(DEFAULT_WELFARE_FUND.doubleValue()))
            .andExpect(jsonPath("$.mobileCelling").value(DEFAULT_MOBILE_CELLING.doubleValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBand() throws Exception {
        // Get the band
        restBandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBand() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        int databaseSizeBeforeUpdate = bandRepository.findAll().size();

        // Update the band
        Band updatedBand = bandRepository.findById(band.getId()).get();
        // Disconnect from session so that the updates on updatedBand are not directly saved in db
        em.detach(updatedBand);
        updatedBand
            .bandName(UPDATED_BAND_NAME)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .welfareFund(UPDATED_WELFARE_FUND)
            .mobileCelling(UPDATED_MOBILE_CELLING)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        BandDTO bandDTO = bandMapper.toDto(updatedBand);

        restBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bandDTO))
            )
            .andExpect(status().isOk());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
        Band testBand = bandList.get(bandList.size() - 1);
        assertThat(testBand.getBandName()).isEqualTo(UPDATED_BAND_NAME);
        assertThat(testBand.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testBand.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testBand.getWelfareFund()).isEqualTo(UPDATED_WELFARE_FUND);
        assertThat(testBand.getMobileCelling()).isEqualTo(UPDATED_MOBILE_CELLING);
        assertThat(testBand.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBand.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingBand() throws Exception {
        int databaseSizeBeforeUpdate = bandRepository.findAll().size();
        band.setId(count.incrementAndGet());

        // Create the Band
        BandDTO bandDTO = bandMapper.toDto(band);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBand() throws Exception {
        int databaseSizeBeforeUpdate = bandRepository.findAll().size();
        band.setId(count.incrementAndGet());

        // Create the Band
        BandDTO bandDTO = bandMapper.toDto(band);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBand() throws Exception {
        int databaseSizeBeforeUpdate = bandRepository.findAll().size();
        band.setId(count.incrementAndGet());

        // Create the Band
        BandDTO bandDTO = bandMapper.toDto(band);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBandWithPatch() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        int databaseSizeBeforeUpdate = bandRepository.findAll().size();

        // Update the band using partial update
        Band partialUpdatedBand = new Band();
        partialUpdatedBand.setId(band.getId());

        partialUpdatedBand.bandName(UPDATED_BAND_NAME).mobileCelling(UPDATED_MOBILE_CELLING).updatedAt(UPDATED_UPDATED_AT);

        restBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBand))
            )
            .andExpect(status().isOk());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
        Band testBand = bandList.get(bandList.size() - 1);
        assertThat(testBand.getBandName()).isEqualTo(UPDATED_BAND_NAME);
        assertThat(testBand.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testBand.getMaxSalary()).isEqualTo(DEFAULT_MAX_SALARY);
        assertThat(testBand.getWelfareFund()).isEqualTo(DEFAULT_WELFARE_FUND);
        assertThat(testBand.getMobileCelling()).isEqualTo(UPDATED_MOBILE_CELLING);
        assertThat(testBand.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testBand.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateBandWithPatch() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        int databaseSizeBeforeUpdate = bandRepository.findAll().size();

        // Update the band using partial update
        Band partialUpdatedBand = new Band();
        partialUpdatedBand.setId(band.getId());

        partialUpdatedBand
            .bandName(UPDATED_BAND_NAME)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .welfareFund(UPDATED_WELFARE_FUND)
            .mobileCelling(UPDATED_MOBILE_CELLING)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBand))
            )
            .andExpect(status().isOk());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
        Band testBand = bandList.get(bandList.size() - 1);
        assertThat(testBand.getBandName()).isEqualTo(UPDATED_BAND_NAME);
        assertThat(testBand.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testBand.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testBand.getWelfareFund()).isEqualTo(UPDATED_WELFARE_FUND);
        assertThat(testBand.getMobileCelling()).isEqualTo(UPDATED_MOBILE_CELLING);
        assertThat(testBand.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBand.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingBand() throws Exception {
        int databaseSizeBeforeUpdate = bandRepository.findAll().size();
        band.setId(count.incrementAndGet());

        // Create the Band
        BandDTO bandDTO = bandMapper.toDto(band);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBand() throws Exception {
        int databaseSizeBeforeUpdate = bandRepository.findAll().size();
        band.setId(count.incrementAndGet());

        // Create the Band
        BandDTO bandDTO = bandMapper.toDto(band);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBand() throws Exception {
        int databaseSizeBeforeUpdate = bandRepository.findAll().size();
        band.setId(count.incrementAndGet());

        // Create the Band
        BandDTO bandDTO = bandMapper.toDto(band);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBandMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Band in the database
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBand() throws Exception {
        // Initialize the database
        bandRepository.saveAndFlush(band);

        int databaseSizeBeforeDelete = bandRepository.findAll().size();

        // Delete the band
        restBandMockMvc
            .perform(delete(ENTITY_API_URL_ID, band.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Band> bandList = bandRepository.findAll();
        assertThat(bandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
