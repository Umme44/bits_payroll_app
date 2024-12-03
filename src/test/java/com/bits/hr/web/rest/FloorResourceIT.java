package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Floor;
import com.bits.hr.repository.FloorRepository;
import com.bits.hr.service.FloorService;
import com.bits.hr.service.dto.FloorDTO;
import com.bits.hr.service.mapper.FloorMapper;
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
 * Integration tests for the {@link FloorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FloorResourceIT {

    private static final String DEFAULT_FLOOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FLOOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/floors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FloorRepository floorRepository;

    @Mock
    private FloorRepository floorRepositoryMock;

    @Autowired
    private FloorMapper floorMapper;

    @Mock
    private FloorService floorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFloorMockMvc;

    private Floor floor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Floor createEntity(EntityManager em) {
        Floor floor = new Floor()
            .floorName(DEFAULT_FLOOR_NAME)
            .remarks(DEFAULT_REMARKS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return floor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Floor createUpdatedEntity(EntityManager em) {
        Floor floor = new Floor()
            .floorName(UPDATED_FLOOR_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return floor;
    }

    @BeforeEach
    public void initTest() {
        floor = createEntity(em);
    }

    @Test
    @Transactional
    void createFloor() throws Exception {
        int databaseSizeBeforeCreate = floorRepository.findAll().size();
        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);
        restFloorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isCreated());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeCreate + 1);
        Floor testFloor = floorList.get(floorList.size() - 1);
        assertThat(testFloor.getFloorName()).isEqualTo(DEFAULT_FLOOR_NAME);
        assertThat(testFloor.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testFloor.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testFloor.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createFloorWithExistingId() throws Exception {
        // Create the Floor with an existing ID
        floor.setId(1L);
        FloorDTO floorDTO = floorMapper.toDto(floor);

        int databaseSizeBeforeCreate = floorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFloorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFloorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = floorRepository.findAll().size();
        // set the field null
        floor.setFloorName(null);

        // Create the Floor, which fails.
        FloorDTO floorDTO = floorMapper.toDto(floor);

        restFloorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isBadRequest());

        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFloors() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        // Get all the floorList
        restFloorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(floor.getId().intValue())))
            .andExpect(jsonPath("$.[*].floorName").value(hasItem(DEFAULT_FLOOR_NAME)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFloorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(floorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFloorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(floorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFloorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(floorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFloorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(floorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFloor() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        // Get the floor
        restFloorMockMvc
            .perform(get(ENTITY_API_URL_ID, floor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(floor.getId().intValue()))
            .andExpect(jsonPath("$.floorName").value(DEFAULT_FLOOR_NAME))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFloor() throws Exception {
        // Get the floor
        restFloorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFloor() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        int databaseSizeBeforeUpdate = floorRepository.findAll().size();

        // Update the floor
        Floor updatedFloor = floorRepository.findById(floor.getId()).get();
        // Disconnect from session so that the updates on updatedFloor are not directly saved in db
        em.detach(updatedFloor);
        updatedFloor.floorName(UPDATED_FLOOR_NAME).remarks(UPDATED_REMARKS).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        FloorDTO floorDTO = floorMapper.toDto(updatedFloor);

        restFloorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, floorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
        Floor testFloor = floorList.get(floorList.size() - 1);
        assertThat(testFloor.getFloorName()).isEqualTo(UPDATED_FLOOR_NAME);
        assertThat(testFloor.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testFloor.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFloor.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, floorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFloorWithPatch() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        int databaseSizeBeforeUpdate = floorRepository.findAll().size();

        // Update the floor using partial update
        Floor partialUpdatedFloor = new Floor();
        partialUpdatedFloor.setId(floor.getId());

        partialUpdatedFloor.remarks(UPDATED_REMARKS).createdAt(UPDATED_CREATED_AT);

        restFloorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFloor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFloor))
            )
            .andExpect(status().isOk());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
        Floor testFloor = floorList.get(floorList.size() - 1);
        assertThat(testFloor.getFloorName()).isEqualTo(DEFAULT_FLOOR_NAME);
        assertThat(testFloor.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testFloor.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFloor.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateFloorWithPatch() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        int databaseSizeBeforeUpdate = floorRepository.findAll().size();

        // Update the floor using partial update
        Floor partialUpdatedFloor = new Floor();
        partialUpdatedFloor.setId(floor.getId());

        partialUpdatedFloor
            .floorName(UPDATED_FLOOR_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restFloorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFloor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFloor))
            )
            .andExpect(status().isOk());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
        Floor testFloor = floorList.get(floorList.size() - 1);
        assertThat(testFloor.getFloorName()).isEqualTo(UPDATED_FLOOR_NAME);
        assertThat(testFloor.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testFloor.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFloor.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, floorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFloor() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        int databaseSizeBeforeDelete = floorRepository.findAll().size();

        // Delete the floor
        restFloorMockMvc
            .perform(delete(ENTITY_API_URL_ID, floor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
