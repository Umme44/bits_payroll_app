package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import com.bits.hr.service.mapper.FestivalBonusDetailsMapper;
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
 * Integration tests for the {@link FestivalBonusDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FestivalBonusDetailsResourceIT {

    private static final Double DEFAULT_BONUS_AMOUNT = 0D;
    private static final Double UPDATED_BONUS_AMOUNT = 1D;

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_HOLD = false;
    private static final Boolean UPDATED_IS_HOLD = true;

    private static final Double DEFAULT_BASIC = 1D;
    private static final Double UPDATED_BASIC = 2D;

    private static final Double DEFAULT_GROSS = 1D;
    private static final Double UPDATED_GROSS = 2D;

    private static final String ENTITY_API_URL = "/api/festival-bonus-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    private FestivalBonusDetailsMapper festivalBonusDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFestivalBonusDetailsMockMvc;

    private FestivalBonusDetails festivalBonusDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FestivalBonusDetails createEntity(EntityManager em) {
        FestivalBonusDetails festivalBonusDetails = new FestivalBonusDetails()
            .bonusAmount(DEFAULT_BONUS_AMOUNT)
            .remarks(DEFAULT_REMARKS)
            .isHold(DEFAULT_IS_HOLD)
            .basic(DEFAULT_BASIC)
            .gross(DEFAULT_GROSS);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        festivalBonusDetails.setEmployee(employee);
        // Add required entity
        Festival festival;
        if (TestUtil.findAll(em, Festival.class).isEmpty()) {
            festival = FestivalResourceIT.createEntity(em);
            em.persist(festival);
            em.flush();
        } else {
            festival = TestUtil.findAll(em, Festival.class).get(0);
        }
        festivalBonusDetails.setFestival(festival);
        return festivalBonusDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FestivalBonusDetails createUpdatedEntity(EntityManager em) {
        FestivalBonusDetails festivalBonusDetails = new FestivalBonusDetails()
            .bonusAmount(UPDATED_BONUS_AMOUNT)
            .remarks(UPDATED_REMARKS)
            .isHold(UPDATED_IS_HOLD)
            .basic(UPDATED_BASIC)
            .gross(UPDATED_GROSS);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        festivalBonusDetails.setEmployee(employee);
        // Add required entity
        Festival festival;
        if (TestUtil.findAll(em, Festival.class).isEmpty()) {
            festival = FestivalResourceIT.createUpdatedEntity(em);
            em.persist(festival);
            em.flush();
        } else {
            festival = TestUtil.findAll(em, Festival.class).get(0);
        }
        festivalBonusDetails.setFestival(festival);
        return festivalBonusDetails;
    }

    @BeforeEach
    public void initTest() {
        festivalBonusDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createFestivalBonusDetails() throws Exception {
        int databaseSizeBeforeCreate = festivalBonusDetailsRepository.findAll().size();
        // Create the FestivalBonusDetails
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);
        restFestivalBonusDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        FestivalBonusDetails testFestivalBonusDetails = festivalBonusDetailsList.get(festivalBonusDetailsList.size() - 1);
        assertThat(testFestivalBonusDetails.getBonusAmount()).isEqualTo(DEFAULT_BONUS_AMOUNT);
        assertThat(testFestivalBonusDetails.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testFestivalBonusDetails.getIsHold()).isEqualTo(DEFAULT_IS_HOLD);
        assertThat(testFestivalBonusDetails.getBasic()).isEqualTo(DEFAULT_BASIC);
        assertThat(testFestivalBonusDetails.getGross()).isEqualTo(DEFAULT_GROSS);
    }

    @Test
    @Transactional
    void createFestivalBonusDetailsWithExistingId() throws Exception {
        // Create the FestivalBonusDetails with an existing ID
        festivalBonusDetails.setId(1L);
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        int databaseSizeBeforeCreate = festivalBonusDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFestivalBonusDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBonusAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = festivalBonusDetailsRepository.findAll().size();
        // set the field null
        festivalBonusDetails.setBonusAmount(null);

        // Create the FestivalBonusDetails, which fails.
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        restFestivalBonusDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsHoldIsRequired() throws Exception {
        int databaseSizeBeforeTest = festivalBonusDetailsRepository.findAll().size();
        // set the field null
        festivalBonusDetails.setIsHold(null);

        // Create the FestivalBonusDetails, which fails.
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        restFestivalBonusDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFestivalBonusDetails() throws Exception {
        // Initialize the database
        festivalBonusDetailsRepository.saveAndFlush(festivalBonusDetails);

        // Get all the festivalBonusDetailsList
        restFestivalBonusDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(festivalBonusDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].bonusAmount").value(hasItem(DEFAULT_BONUS_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].isHold").value(hasItem(DEFAULT_IS_HOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].basic").value(hasItem(DEFAULT_BASIC.doubleValue())))
            .andExpect(jsonPath("$.[*].gross").value(hasItem(DEFAULT_GROSS.doubleValue())));
    }

    @Test
    @Transactional
    void getFestivalBonusDetails() throws Exception {
        // Initialize the database
        festivalBonusDetailsRepository.saveAndFlush(festivalBonusDetails);

        // Get the festivalBonusDetails
        restFestivalBonusDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, festivalBonusDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(festivalBonusDetails.getId().intValue()))
            .andExpect(jsonPath("$.bonusAmount").value(DEFAULT_BONUS_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.isHold").value(DEFAULT_IS_HOLD.booleanValue()))
            .andExpect(jsonPath("$.basic").value(DEFAULT_BASIC.doubleValue()))
            .andExpect(jsonPath("$.gross").value(DEFAULT_GROSS.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFestivalBonusDetails() throws Exception {
        // Get the festivalBonusDetails
        restFestivalBonusDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFestivalBonusDetails() throws Exception {
        // Initialize the database
        festivalBonusDetailsRepository.saveAndFlush(festivalBonusDetails);

        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();

        // Update the festivalBonusDetails
        FestivalBonusDetails updatedFestivalBonusDetails = festivalBonusDetailsRepository.findById(festivalBonusDetails.getId()).get();
        // Disconnect from session so that the updates on updatedFestivalBonusDetails are not directly saved in db
        em.detach(updatedFestivalBonusDetails);
        updatedFestivalBonusDetails
            .bonusAmount(UPDATED_BONUS_AMOUNT)
            .remarks(UPDATED_REMARKS)
            .isHold(UPDATED_IS_HOLD)
            .basic(UPDATED_BASIC)
            .gross(UPDATED_GROSS);
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(updatedFestivalBonusDetails);

        restFestivalBonusDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, festivalBonusDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
        FestivalBonusDetails testFestivalBonusDetails = festivalBonusDetailsList.get(festivalBonusDetailsList.size() - 1);
        assertThat(testFestivalBonusDetails.getBonusAmount()).isEqualTo(UPDATED_BONUS_AMOUNT);
        assertThat(testFestivalBonusDetails.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testFestivalBonusDetails.getIsHold()).isEqualTo(UPDATED_IS_HOLD);
        assertThat(testFestivalBonusDetails.getBasic()).isEqualTo(UPDATED_BASIC);
        assertThat(testFestivalBonusDetails.getGross()).isEqualTo(UPDATED_GROSS);
    }

    @Test
    @Transactional
    void putNonExistingFestivalBonusDetails() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();
        festivalBonusDetails.setId(count.incrementAndGet());

        // Create the FestivalBonusDetails
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFestivalBonusDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, festivalBonusDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFestivalBonusDetails() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();
        festivalBonusDetails.setId(count.incrementAndGet());

        // Create the FestivalBonusDetails
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalBonusDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFestivalBonusDetails() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();
        festivalBonusDetails.setId(count.incrementAndGet());

        // Create the FestivalBonusDetails
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalBonusDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFestivalBonusDetailsWithPatch() throws Exception {
        // Initialize the database
        festivalBonusDetailsRepository.saveAndFlush(festivalBonusDetails);

        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();

        // Update the festivalBonusDetails using partial update
        FestivalBonusDetails partialUpdatedFestivalBonusDetails = new FestivalBonusDetails();
        partialUpdatedFestivalBonusDetails.setId(festivalBonusDetails.getId());

        partialUpdatedFestivalBonusDetails
            .bonusAmount(UPDATED_BONUS_AMOUNT)
            .remarks(UPDATED_REMARKS)
            .isHold(UPDATED_IS_HOLD)
            .basic(UPDATED_BASIC)
            .gross(UPDATED_GROSS);

        restFestivalBonusDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFestivalBonusDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFestivalBonusDetails))
            )
            .andExpect(status().isOk());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
        FestivalBonusDetails testFestivalBonusDetails = festivalBonusDetailsList.get(festivalBonusDetailsList.size() - 1);
        assertThat(testFestivalBonusDetails.getBonusAmount()).isEqualTo(UPDATED_BONUS_AMOUNT);
        assertThat(testFestivalBonusDetails.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testFestivalBonusDetails.getIsHold()).isEqualTo(UPDATED_IS_HOLD);
        assertThat(testFestivalBonusDetails.getBasic()).isEqualTo(UPDATED_BASIC);
        assertThat(testFestivalBonusDetails.getGross()).isEqualTo(UPDATED_GROSS);
    }

    @Test
    @Transactional
    void fullUpdateFestivalBonusDetailsWithPatch() throws Exception {
        // Initialize the database
        festivalBonusDetailsRepository.saveAndFlush(festivalBonusDetails);

        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();

        // Update the festivalBonusDetails using partial update
        FestivalBonusDetails partialUpdatedFestivalBonusDetails = new FestivalBonusDetails();
        partialUpdatedFestivalBonusDetails.setId(festivalBonusDetails.getId());

        partialUpdatedFestivalBonusDetails
            .bonusAmount(UPDATED_BONUS_AMOUNT)
            .remarks(UPDATED_REMARKS)
            .isHold(UPDATED_IS_HOLD)
            .basic(UPDATED_BASIC)
            .gross(UPDATED_GROSS);

        restFestivalBonusDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFestivalBonusDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFestivalBonusDetails))
            )
            .andExpect(status().isOk());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
        FestivalBonusDetails testFestivalBonusDetails = festivalBonusDetailsList.get(festivalBonusDetailsList.size() - 1);
        assertThat(testFestivalBonusDetails.getBonusAmount()).isEqualTo(UPDATED_BONUS_AMOUNT);
        assertThat(testFestivalBonusDetails.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testFestivalBonusDetails.getIsHold()).isEqualTo(UPDATED_IS_HOLD);
        assertThat(testFestivalBonusDetails.getBasic()).isEqualTo(UPDATED_BASIC);
        assertThat(testFestivalBonusDetails.getGross()).isEqualTo(UPDATED_GROSS);
    }

    @Test
    @Transactional
    void patchNonExistingFestivalBonusDetails() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();
        festivalBonusDetails.setId(count.incrementAndGet());

        // Create the FestivalBonusDetails
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFestivalBonusDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, festivalBonusDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFestivalBonusDetails() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();
        festivalBonusDetails.setId(count.incrementAndGet());

        // Create the FestivalBonusDetails
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalBonusDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFestivalBonusDetails() throws Exception {
        int databaseSizeBeforeUpdate = festivalBonusDetailsRepository.findAll().size();
        festivalBonusDetails.setId(count.incrementAndGet());

        // Create the FestivalBonusDetails
        FestivalBonusDetailsDTO festivalBonusDetailsDTO = festivalBonusDetailsMapper.toDto(festivalBonusDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFestivalBonusDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(festivalBonusDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FestivalBonusDetails in the database
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFestivalBonusDetails() throws Exception {
        // Initialize the database
        festivalBonusDetailsRepository.saveAndFlush(festivalBonusDetails);

        int databaseSizeBeforeDelete = festivalBonusDetailsRepository.findAll().size();

        // Delete the festivalBonusDetails
        restFestivalBonusDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, festivalBonusDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAll();
        assertThat(festivalBonusDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
