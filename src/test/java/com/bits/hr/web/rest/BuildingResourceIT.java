package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Building;
import com.bits.hr.repository.BuildingRepository;
import com.bits.hr.service.BuildingService;
import com.bits.hr.service.dto.BuildingDTO;
import com.bits.hr.service.mapper.BuildingMapper;
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
 * Integration tests for the {@link BuildingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BuildingResourceIT {

    private static final String DEFAULT_BUILDING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUILDING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUILDING_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_BUILDING_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/buildings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BuildingRepository buildingRepository;

    @Mock
    private BuildingRepository buildingRepositoryMock;

    @Autowired
    private BuildingMapper buildingMapper;

    @Mock
    private BuildingService buildingServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBuildingMockMvc;

    private Building building;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createEntity(EntityManager em) {
        Building building = new Building()
            .buildingName(DEFAULT_BUILDING_NAME)
            .buildingLocation(DEFAULT_BUILDING_LOCATION)
            .remarks(DEFAULT_REMARKS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return building;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Building createUpdatedEntity(EntityManager em) {
        Building building = new Building()
            .buildingName(UPDATED_BUILDING_NAME)
            .buildingLocation(UPDATED_BUILDING_LOCATION)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return building;
    }

    @BeforeEach
    public void initTest() {
        building = createEntity(em);
    }

    @Test
    @Transactional
    void createBuilding() throws Exception {
        int databaseSizeBeforeCreate = buildingRepository.findAll().size();
        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);
        restBuildingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isCreated());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate + 1);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getBuildingName()).isEqualTo(DEFAULT_BUILDING_NAME);
        assertThat(testBuilding.getBuildingLocation()).isEqualTo(DEFAULT_BUILDING_LOCATION);
        assertThat(testBuilding.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testBuilding.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testBuilding.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createBuildingWithExistingId() throws Exception {
        // Create the Building with an existing ID
        building.setId(1L);
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        int databaseSizeBeforeCreate = buildingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuildingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBuildingNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingRepository.findAll().size();
        // set the field null
        building.setBuildingName(null);

        // Create the Building, which fails.
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        restBuildingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isBadRequest());

        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBuildings() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get all the buildingList
        restBuildingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(building.getId().intValue())))
            .andExpect(jsonPath("$.[*].buildingName").value(hasItem(DEFAULT_BUILDING_NAME)))
            .andExpect(jsonPath("$.[*].buildingLocation").value(hasItem(DEFAULT_BUILDING_LOCATION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBuildingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(buildingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBuildingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(buildingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBuildingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(buildingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBuildingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(buildingRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        // Get the building
        restBuildingMockMvc
            .perform(get(ENTITY_API_URL_ID, building.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(building.getId().intValue()))
            .andExpect(jsonPath("$.buildingName").value(DEFAULT_BUILDING_NAME))
            .andExpect(jsonPath("$.buildingLocation").value(DEFAULT_BUILDING_LOCATION))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBuilding() throws Exception {
        // Get the building
        restBuildingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building
        Building updatedBuilding = buildingRepository.findById(building.getId()).get();
        // Disconnect from session so that the updates on updatedBuilding are not directly saved in db
        em.detach(updatedBuilding);
        updatedBuilding
            .buildingName(UPDATED_BUILDING_NAME)
            .buildingLocation(UPDATED_BUILDING_LOCATION)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        BuildingDTO buildingDTO = buildingMapper.toDto(updatedBuilding);

        restBuildingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, buildingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buildingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testBuilding.getBuildingLocation()).isEqualTo(UPDATED_BUILDING_LOCATION);
        assertThat(testBuilding.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testBuilding.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBuilding.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();
        building.setId(count.incrementAndGet());

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, buildingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buildingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();
        building.setId(count.incrementAndGet());

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buildingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();
        building.setId(count.incrementAndGet());

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBuildingWithPatch() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building using partial update
        Building partialUpdatedBuilding = new Building();
        partialUpdatedBuilding.setId(building.getId());

        partialUpdatedBuilding.buildingName(UPDATED_BUILDING_NAME).createdAt(UPDATED_CREATED_AT);

        restBuildingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBuilding.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuilding))
            )
            .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testBuilding.getBuildingLocation()).isEqualTo(DEFAULT_BUILDING_LOCATION);
        assertThat(testBuilding.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testBuilding.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBuilding.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateBuildingWithPatch() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();

        // Update the building using partial update
        Building partialUpdatedBuilding = new Building();
        partialUpdatedBuilding.setId(building.getId());

        partialUpdatedBuilding
            .buildingName(UPDATED_BUILDING_NAME)
            .buildingLocation(UPDATED_BUILDING_LOCATION)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restBuildingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBuilding.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuilding))
            )
            .andExpect(status().isOk());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
        Building testBuilding = buildingList.get(buildingList.size() - 1);
        assertThat(testBuilding.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testBuilding.getBuildingLocation()).isEqualTo(UPDATED_BUILDING_LOCATION);
        assertThat(testBuilding.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testBuilding.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBuilding.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();
        building.setId(count.incrementAndGet());

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, buildingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buildingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();
        building.setId(count.incrementAndGet());

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buildingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBuilding() throws Exception {
        int databaseSizeBeforeUpdate = buildingRepository.findAll().size();
        building.setId(count.incrementAndGet());

        // Create the Building
        BuildingDTO buildingDTO = buildingMapper.toDto(building);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(buildingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Building in the database
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBuilding() throws Exception {
        // Initialize the database
        buildingRepository.saveAndFlush(building);

        int databaseSizeBeforeDelete = buildingRepository.findAll().size();

        // Delete the building
        restBuildingMockMvc
            .perform(delete(ENTITY_API_URL_ID, building.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Building> buildingList = buildingRepository.findAll();
        assertThat(buildingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
