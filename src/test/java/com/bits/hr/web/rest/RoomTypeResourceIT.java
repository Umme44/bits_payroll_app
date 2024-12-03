package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.RoomType;
import com.bits.hr.repository.RoomTypeRepository;
import com.bits.hr.service.RoomTypeService;
import com.bits.hr.service.dto.RoomTypeDTO;
import com.bits.hr.service.mapper.RoomTypeMapper;
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
 * Integration tests for the {@link RoomTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RoomTypeResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/room-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Mock
    private RoomTypeRepository roomTypeRepositoryMock;

    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @Mock
    private RoomTypeService roomTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomTypeMockMvc;

    private RoomType roomType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomType createEntity(EntityManager em) {
        RoomType roomType = new RoomType()
            .typeName(DEFAULT_TYPE_NAME)
            .remarks(DEFAULT_REMARKS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return roomType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomType createUpdatedEntity(EntityManager em) {
        RoomType roomType = new RoomType()
            .typeName(UPDATED_TYPE_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return roomType;
    }

    @BeforeEach
    public void initTest() {
        roomType = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomType() throws Exception {
        int databaseSizeBeforeCreate = roomTypeRepository.findAll().size();
        // Create the RoomType
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);
        restRoomTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RoomType testRoomType = roomTypeList.get(roomTypeList.size() - 1);
        assertThat(testRoomType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);
        assertThat(testRoomType.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testRoomType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRoomType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createRoomTypeWithExistingId() throws Exception {
        // Create the RoomType with an existing ID
        roomType.setId(1L);
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);

        int databaseSizeBeforeCreate = roomTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomTypeRepository.findAll().size();
        // set the field null
        roomType.setTypeName(null);

        // Create the RoomType, which fails.
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);

        restRoomTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomTypeDTO)))
            .andExpect(status().isBadRequest());

        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoomTypes() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        // Get all the roomTypeList
        restRoomTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomType.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRoomTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(roomTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoomTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(roomTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRoomTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(roomTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoomTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(roomTypeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRoomType() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        // Get the roomType
        restRoomTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, roomType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomType.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRoomType() throws Exception {
        // Get the roomType
        restRoomTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoomType() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();

        // Update the roomType
        RoomType updatedRoomType = roomTypeRepository.findById(roomType.getId()).get();
        // Disconnect from session so that the updates on updatedRoomType are not directly saved in db
        em.detach(updatedRoomType);
        updatedRoomType.typeName(UPDATED_TYPE_NAME).remarks(UPDATED_REMARKS).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(updatedRoomType);

        restRoomTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
        RoomType testRoomType = roomTypeList.get(roomTypeList.size() - 1);
        assertThat(testRoomType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
        assertThat(testRoomType.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testRoomType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoomType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(count.incrementAndGet());

        // Create the RoomType
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(count.incrementAndGet());

        // Create the RoomType
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(count.incrementAndGet());

        // Create the RoomType
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomTypeWithPatch() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();

        // Update the roomType using partial update
        RoomType partialUpdatedRoomType = new RoomType();
        partialUpdatedRoomType.setId(roomType.getId());

        partialUpdatedRoomType.typeName(UPDATED_TYPE_NAME).remarks(UPDATED_REMARKS);

        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomType))
            )
            .andExpect(status().isOk());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
        RoomType testRoomType = roomTypeList.get(roomTypeList.size() - 1);
        assertThat(testRoomType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
        assertThat(testRoomType.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testRoomType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRoomType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateRoomTypeWithPatch() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();

        // Update the roomType using partial update
        RoomType partialUpdatedRoomType = new RoomType();
        partialUpdatedRoomType.setId(roomType.getId());

        partialUpdatedRoomType
            .typeName(UPDATED_TYPE_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomType))
            )
            .andExpect(status().isOk());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
        RoomType testRoomType = roomTypeList.get(roomTypeList.size() - 1);
        assertThat(testRoomType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
        assertThat(testRoomType.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testRoomType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoomType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(count.incrementAndGet());

        // Create the RoomType
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(count.incrementAndGet());

        // Create the RoomType
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(count.incrementAndGet());

        // Create the RoomType
        RoomTypeDTO roomTypeDTO = roomTypeMapper.toDto(roomType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomType() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        int databaseSizeBeforeDelete = roomTypeRepository.findAll().size();

        // Delete the roomType
        restRoomTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
