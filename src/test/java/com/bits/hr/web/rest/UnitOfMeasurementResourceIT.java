package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.UnitOfMeasurement;
import com.bits.hr.domain.User;
import com.bits.hr.repository.UnitOfMeasurementRepository;
import com.bits.hr.service.UnitOfMeasurementService;
import com.bits.hr.service.dto.UnitOfMeasurementDTO;
import com.bits.hr.service.mapper.UnitOfMeasurementMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UnitOfMeasurementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UnitOfMeasurementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/unit-of-measurements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitOfMeasurementRepository unitOfMeasurementRepository;

    @Mock
    private UnitOfMeasurementRepository unitOfMeasurementRepositoryMock;

    @Autowired
    private UnitOfMeasurementMapper unitOfMeasurementMapper;

    @Mock
    private UnitOfMeasurementService unitOfMeasurementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitOfMeasurementMockMvc;

    private UnitOfMeasurement unitOfMeasurement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasurement createEntity(EntityManager em) {
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement()
            .name(DEFAULT_NAME)
            .remarks(DEFAULT_REMARKS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        unitOfMeasurement.setCreatedBy(user);
        return unitOfMeasurement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitOfMeasurement createUpdatedEntity(EntityManager em) {
        UnitOfMeasurement unitOfMeasurement = new UnitOfMeasurement()
            .name(UPDATED_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        unitOfMeasurement.setCreatedBy(user);
        return unitOfMeasurement;
    }

    @BeforeEach
    public void initTest() {
        unitOfMeasurement = createEntity(em);
    }

    @Test
    @Transactional
    void createUnitOfMeasurement() throws Exception {
        int databaseSizeBeforeCreate = unitOfMeasurementRepository.findAll().size();
        // Create the UnitOfMeasurement
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);
        restUnitOfMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeCreate + 1);
        UnitOfMeasurement testUnitOfMeasurement = unitOfMeasurementList.get(unitOfMeasurementList.size() - 1);
        assertThat(testUnitOfMeasurement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitOfMeasurement.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testUnitOfMeasurement.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUnitOfMeasurement.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createUnitOfMeasurementWithExistingId() throws Exception {
        // Create the UnitOfMeasurement with an existing ID
        unitOfMeasurement.setId(1L);
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        int databaseSizeBeforeCreate = unitOfMeasurementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitOfMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasurementRepository.findAll().size();
        // set the field null
        unitOfMeasurement.setName(null);

        // Create the UnitOfMeasurement, which fails.
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        restUnitOfMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitOfMeasurementRepository.findAll().size();
        // set the field null
        unitOfMeasurement.setCreatedAt(null);

        // Create the UnitOfMeasurement, which fails.
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        restUnitOfMeasurementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnitOfMeasurements() throws Exception {
        // Initialize the database
        unitOfMeasurementRepository.saveAndFlush(unitOfMeasurement);

        // Get all the unitOfMeasurementList
        restUnitOfMeasurementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitOfMeasurement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUnitOfMeasurementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(unitOfMeasurementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUnitOfMeasurementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(unitOfMeasurementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUnitOfMeasurementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(unitOfMeasurementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUnitOfMeasurementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(unitOfMeasurementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUnitOfMeasurement() throws Exception {
        // Initialize the database
        unitOfMeasurementRepository.saveAndFlush(unitOfMeasurement);

        // Get the unitOfMeasurement
        restUnitOfMeasurementMockMvc
            .perform(get(ENTITY_API_URL_ID, unitOfMeasurement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unitOfMeasurement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUnitOfMeasurement() throws Exception {
        // Get the unitOfMeasurement
        restUnitOfMeasurementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnitOfMeasurement() throws Exception {
        // Initialize the database
        unitOfMeasurementRepository.saveAndFlush(unitOfMeasurement);

        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();

        // Update the unitOfMeasurement
        UnitOfMeasurement updatedUnitOfMeasurement = unitOfMeasurementRepository.findById(unitOfMeasurement.getId()).get();
        // Disconnect from session so that the updates on updatedUnitOfMeasurement are not directly saved in db
        em.detach(updatedUnitOfMeasurement);
        updatedUnitOfMeasurement.name(UPDATED_NAME).remarks(UPDATED_REMARKS).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(updatedUnitOfMeasurement);

        restUnitOfMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitOfMeasurementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasurement testUnitOfMeasurement = unitOfMeasurementList.get(unitOfMeasurementList.size() - 1);
        assertThat(testUnitOfMeasurement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitOfMeasurement.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testUnitOfMeasurement.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUnitOfMeasurement.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUnitOfMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();
        unitOfMeasurement.setId(count.incrementAndGet());

        // Create the UnitOfMeasurement
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitOfMeasurementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnitOfMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();
        unitOfMeasurement.setId(count.incrementAndGet());

        // Create the UnitOfMeasurement
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnitOfMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();
        unitOfMeasurement.setId(count.incrementAndGet());

        // Create the UnitOfMeasurement
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasurementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitOfMeasurementWithPatch() throws Exception {
        // Initialize the database
        unitOfMeasurementRepository.saveAndFlush(unitOfMeasurement);

        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();

        // Update the unitOfMeasurement using partial update
        UnitOfMeasurement partialUpdatedUnitOfMeasurement = new UnitOfMeasurement();
        partialUpdatedUnitOfMeasurement.setId(unitOfMeasurement.getId());

        partialUpdatedUnitOfMeasurement.createdAt(UPDATED_CREATED_AT);

        restUnitOfMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasurement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitOfMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasurement testUnitOfMeasurement = unitOfMeasurementList.get(unitOfMeasurementList.size() - 1);
        assertThat(testUnitOfMeasurement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUnitOfMeasurement.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testUnitOfMeasurement.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUnitOfMeasurement.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUnitOfMeasurementWithPatch() throws Exception {
        // Initialize the database
        unitOfMeasurementRepository.saveAndFlush(unitOfMeasurement);

        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();

        // Update the unitOfMeasurement using partial update
        UnitOfMeasurement partialUpdatedUnitOfMeasurement = new UnitOfMeasurement();
        partialUpdatedUnitOfMeasurement.setId(unitOfMeasurement.getId());

        partialUpdatedUnitOfMeasurement
            .name(UPDATED_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restUnitOfMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnitOfMeasurement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnitOfMeasurement))
            )
            .andExpect(status().isOk());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
        UnitOfMeasurement testUnitOfMeasurement = unitOfMeasurementList.get(unitOfMeasurementList.size() - 1);
        assertThat(testUnitOfMeasurement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUnitOfMeasurement.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testUnitOfMeasurement.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUnitOfMeasurement.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUnitOfMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();
        unitOfMeasurement.setId(count.incrementAndGet());

        // Create the UnitOfMeasurement
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitOfMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitOfMeasurementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnitOfMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();
        unitOfMeasurement.setId(count.incrementAndGet());

        // Create the UnitOfMeasurement
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnitOfMeasurement() throws Exception {
        int databaseSizeBeforeUpdate = unitOfMeasurementRepository.findAll().size();
        unitOfMeasurement.setId(count.incrementAndGet());

        // Create the UnitOfMeasurement
        UnitOfMeasurementDTO unitOfMeasurementDTO = unitOfMeasurementMapper.toDto(unitOfMeasurement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitOfMeasurementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitOfMeasurementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UnitOfMeasurement in the database
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnitOfMeasurement() throws Exception {
        // Initialize the database
        unitOfMeasurementRepository.saveAndFlush(unitOfMeasurement);

        int databaseSizeBeforeDelete = unitOfMeasurementRepository.findAll().size();

        // Delete the unitOfMeasurement
        restUnitOfMeasurementMockMvc
            .perform(delete(ENTITY_API_URL_ID, unitOfMeasurement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UnitOfMeasurement> unitOfMeasurementList = unitOfMeasurementRepository.findAll();
        assertThat(unitOfMeasurementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
