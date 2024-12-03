package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Festival;
import com.bits.hr.domain.enumeration.Religion;
import com.bits.hr.repository.FestivalRepository;
import com.bits.hr.service.dto.FestivalDTO;
import com.bits.hr.service.mapper.FestivalMapper;
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
 * Integration tests for the {@link FestivalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FestivalResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FESTIVAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FESTIVAL_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FESTIVAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FESTIVAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_BONUS_DISBURSEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BONUS_DISBURSEMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Religion DEFAULT_RELIGION = Religion.ISLAM;
    private static final Religion UPDATED_RELIGION = Religion.HINDU;

    private static final Boolean DEFAULT_IS_PRO_RATA = false;
    private static final Boolean UPDATED_IS_PRO_RATA = true;

    private static final String ENTITY_API_URL = "/api/festivals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private FestivalMapper festivalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFestivalMockMvc;

    private Festival festival;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Festival createEntity(EntityManager em) {
        Festival festival = new Festival()
            .title(DEFAULT_TITLE)
            .festivalName(DEFAULT_FESTIVAL_NAME)
            .festivalDate(DEFAULT_FESTIVAL_DATE)
            .bonusDisbursementDate(DEFAULT_BONUS_DISBURSEMENT_DATE)
            .religion(DEFAULT_RELIGION)
            .isProRata(DEFAULT_IS_PRO_RATA);
        return festival;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Festival createUpdatedEntity(EntityManager em) {
        Festival festival = new Festival()
            .title(UPDATED_TITLE)
            .festivalName(UPDATED_FESTIVAL_NAME)
            .festivalDate(UPDATED_FESTIVAL_DATE)
            .bonusDisbursementDate(UPDATED_BONUS_DISBURSEMENT_DATE)
            .religion(UPDATED_RELIGION)
            .isProRata(UPDATED_IS_PRO_RATA);
        return festival;
    }

    @BeforeEach
    public void initTest() {
        festival = createEntity(em);
    }

    @Test
    @Transactional
    void createFestival() throws Exception {
        int databaseSizeBeforeCreate = festivalRepository.findAll().size();
        // Create the Festival
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);
        restFestivalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(festivalDTO)))
            .andExpect(status().isCreated());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeCreate + 1);
        Festival testFestival = festivalList.get(festivalList.size() - 1);
        assertThat(testFestival.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFestival.getFestivalName()).isEqualTo(DEFAULT_FESTIVAL_NAME);
        assertThat(testFestival.getFestivalDate()).isEqualTo(DEFAULT_FESTIVAL_DATE);
        assertThat(testFestival.getBonusDisbursementDate()).isEqualTo(DEFAULT_BONUS_DISBURSEMENT_DATE);
        assertThat(testFestival.getReligion()).isEqualTo(DEFAULT_RELIGION);
        assertThat(testFestival.getIsProRata()).isEqualTo(DEFAULT_IS_PRO_RATA);
    }

    @Test
    @Transactional
    void createFestivalWithExistingId() throws Exception {
        // Create the Festival with an existing ID
        festival.setId(1L);
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        int databaseSizeBeforeCreate = festivalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFestivalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(festivalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = festivalRepository.findAll().size();
        // set the field null
        festival.setTitle(null);

        // Create the Festival, which fails.
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        restFestivalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(festivalDTO)))
            .andExpect(status().isBadRequest());

        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBonusDisbursementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = festivalRepository.findAll().size();
        // set the field null
        festival.setBonusDisbursementDate(null);

        // Create the Festival, which fails.
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        restFestivalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(festivalDTO)))
            .andExpect(status().isBadRequest());

        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReligionIsRequired() throws Exception {
        int databaseSizeBeforeTest = festivalRepository.findAll().size();
        // set the field null
        festival.setReligion(null);

        // Create the Festival, which fails.
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        restFestivalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(festivalDTO)))
            .andExpect(status().isBadRequest());

        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsProRataIsRequired() throws Exception {
        int databaseSizeBeforeTest = festivalRepository.findAll().size();
        // set the field null
        festival.setIsProRata(null);

        // Create the Festival, which fails.
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        restFestivalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(festivalDTO)))
            .andExpect(status().isBadRequest());

        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFestivals() throws Exception {
        // Initialize the database
        festivalRepository.saveAndFlush(festival);

        // Get all the festivalList
        restFestivalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(festival.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].festivalName").value(hasItem(DEFAULT_FESTIVAL_NAME)))
            .andExpect(jsonPath("$.[*].festivalDate").value(hasItem(DEFAULT_FESTIVAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].bonusDisbursementDate").value(hasItem(DEFAULT_BONUS_DISBURSEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].religion").value(hasItem(DEFAULT_RELIGION.toString())))
            .andExpect(jsonPath("$.[*].isProRata").value(hasItem(DEFAULT_IS_PRO_RATA.booleanValue())));
    }

    @Test
    @Transactional
    void getFestival() throws Exception {
        // Initialize the database
        festivalRepository.saveAndFlush(festival);

        // Get the festival
        restFestivalMockMvc
            .perform(get(ENTITY_API_URL_ID, festival.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(festival.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.festivalName").value(DEFAULT_FESTIVAL_NAME))
            .andExpect(jsonPath("$.festivalDate").value(DEFAULT_FESTIVAL_DATE.toString()))
            .andExpect(jsonPath("$.bonusDisbursementDate").value(DEFAULT_BONUS_DISBURSEMENT_DATE.toString()))
            .andExpect(jsonPath("$.religion").value(DEFAULT_RELIGION.toString()))
            .andExpect(jsonPath("$.isProRata").value(DEFAULT_IS_PRO_RATA.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFestival() throws Exception {
        // Get the festival
        restFestivalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFestival() throws Exception {
        // Initialize the database
        festivalRepository.saveAndFlush(festival);

        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();

        // Update the festival
        Festival updatedFestival = festivalRepository.findById(festival.getId()).get();
        // Disconnect from session so that the updates on updatedFestival are not directly saved in db
        em.detach(updatedFestival);
        updatedFestival
            .title(UPDATED_TITLE)
            .festivalName(UPDATED_FESTIVAL_NAME)
            .festivalDate(UPDATED_FESTIVAL_DATE)
            .bonusDisbursementDate(UPDATED_BONUS_DISBURSEMENT_DATE)
            .religion(UPDATED_RELIGION)
            .isProRata(UPDATED_IS_PRO_RATA);
        FestivalDTO festivalDTO = festivalMapper.toDto(updatedFestival);

        restFestivalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, festivalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
        Festival testFestival = festivalList.get(festivalList.size() - 1);
        assertThat(testFestival.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFestival.getFestivalName()).isEqualTo(UPDATED_FESTIVAL_NAME);
        assertThat(testFestival.getFestivalDate()).isEqualTo(UPDATED_FESTIVAL_DATE);
        assertThat(testFestival.getBonusDisbursementDate()).isEqualTo(UPDATED_BONUS_DISBURSEMENT_DATE);
        assertThat(testFestival.getReligion()).isEqualTo(UPDATED_RELIGION);
        assertThat(testFestival.getIsProRata()).isEqualTo(UPDATED_IS_PRO_RATA);
    }

    @Test
    @Transactional
    void putNonExistingFestival() throws Exception {
        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();
        festival.setId(count.incrementAndGet());

        // Create the Festival
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFestivalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, festivalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFestival() throws Exception {
        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();
        festival.setId(count.incrementAndGet());

        // Create the Festival
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFestival() throws Exception {
        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();
        festival.setId(count.incrementAndGet());

        // Create the Festival
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(festivalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFestivalWithPatch() throws Exception {
        // Initialize the database
        festivalRepository.saveAndFlush(festival);

        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();

        // Update the festival using partial update
        Festival partialUpdatedFestival = new Festival();
        partialUpdatedFestival.setId(festival.getId());

        partialUpdatedFestival.title(UPDATED_TITLE).festivalName(UPDATED_FESTIVAL_NAME).isProRata(UPDATED_IS_PRO_RATA);

        restFestivalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFestival.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFestival))
            )
            .andExpect(status().isOk());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
        Festival testFestival = festivalList.get(festivalList.size() - 1);
        assertThat(testFestival.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFestival.getFestivalName()).isEqualTo(UPDATED_FESTIVAL_NAME);
        assertThat(testFestival.getFestivalDate()).isEqualTo(DEFAULT_FESTIVAL_DATE);
        assertThat(testFestival.getBonusDisbursementDate()).isEqualTo(DEFAULT_BONUS_DISBURSEMENT_DATE);
        assertThat(testFestival.getReligion()).isEqualTo(DEFAULT_RELIGION);
        assertThat(testFestival.getIsProRata()).isEqualTo(UPDATED_IS_PRO_RATA);
    }

    @Test
    @Transactional
    void fullUpdateFestivalWithPatch() throws Exception {
        // Initialize the database
        festivalRepository.saveAndFlush(festival);

        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();

        // Update the festival using partial update
        Festival partialUpdatedFestival = new Festival();
        partialUpdatedFestival.setId(festival.getId());

        partialUpdatedFestival
            .title(UPDATED_TITLE)
            .festivalName(UPDATED_FESTIVAL_NAME)
            .festivalDate(UPDATED_FESTIVAL_DATE)
            .bonusDisbursementDate(UPDATED_BONUS_DISBURSEMENT_DATE)
            .religion(UPDATED_RELIGION)
            .isProRata(UPDATED_IS_PRO_RATA);

        restFestivalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFestival.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFestival))
            )
            .andExpect(status().isOk());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
        Festival testFestival = festivalList.get(festivalList.size() - 1);
        assertThat(testFestival.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFestival.getFestivalName()).isEqualTo(UPDATED_FESTIVAL_NAME);
        assertThat(testFestival.getFestivalDate()).isEqualTo(UPDATED_FESTIVAL_DATE);
        assertThat(testFestival.getBonusDisbursementDate()).isEqualTo(UPDATED_BONUS_DISBURSEMENT_DATE);
        assertThat(testFestival.getReligion()).isEqualTo(UPDATED_RELIGION);
        assertThat(testFestival.getIsProRata()).isEqualTo(UPDATED_IS_PRO_RATA);
    }

    @Test
    @Transactional
    void patchNonExistingFestival() throws Exception {
        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();
        festival.setId(count.incrementAndGet());

        // Create the Festival
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFestivalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, festivalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(festivalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFestival() throws Exception {
        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();
        festival.setId(count.incrementAndGet());

        // Create the Festival
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(festivalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFestival() throws Exception {
        int databaseSizeBeforeUpdate = festivalRepository.findAll().size();
        festival.setId(count.incrementAndGet());

        // Create the Festival
        FestivalDTO festivalDTO = festivalMapper.toDto(festival);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(festivalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Festival in the database
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFestival() throws Exception {
        // Initialize the database
        festivalRepository.saveAndFlush(festival);

        int databaseSizeBeforeDelete = festivalRepository.findAll().size();

        // Delete the festival
        restFestivalMockMvc
            .perform(delete(ENTITY_API_URL_ID, festival.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Festival> festivalList = festivalRepository.findAll();
        assertThat(festivalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
