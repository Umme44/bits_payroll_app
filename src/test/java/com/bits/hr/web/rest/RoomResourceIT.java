package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Building;
import com.bits.hr.domain.Floor;
import com.bits.hr.domain.Room;
import com.bits.hr.domain.RoomType;
import com.bits.hr.repository.RoomRepository;
import com.bits.hr.service.RoomService;
import com.bits.hr.service.dto.RoomDTO;
import com.bits.hr.service.mapper.RoomMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RoomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RoomResourceIT {

    private static final String DEFAULT_ROOM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final String DEFAULT_FACILITIES = "AAAAAAAAAA";
    private static final String UPDATED_FACILITIES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomRepository roomRepository;

    @Mock
    private RoomRepository roomRepositoryMock;

    @Autowired
    private RoomMapper roomMapper;

    @Mock
    private RoomService roomServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room()
            .roomName(DEFAULT_ROOM_NAME)
            .remarks(DEFAULT_REMARKS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .capacity(DEFAULT_CAPACITY)
            .facilities(DEFAULT_FACILITIES);
        // Add required entity
        Building building;
        if (TestUtil.findAll(em, Building.class).isEmpty()) {
            building = BuildingResourceIT.createEntity(em);
            em.persist(building);
            em.flush();
        } else {
            building = TestUtil.findAll(em, Building.class).get(0);
        }
        room.setBuilding(building);
        // Add required entity
        Floor floor;
        if (TestUtil.findAll(em, Floor.class).isEmpty()) {
            floor = FloorResourceIT.createEntity(em);
            em.persist(floor);
            em.flush();
        } else {
            floor = TestUtil.findAll(em, Floor.class).get(0);
        }
        room.setFloor(floor);
        // Add required entity
        RoomType roomType;
        if (TestUtil.findAll(em, RoomType.class).isEmpty()) {
            roomType = RoomTypeResourceIT.createEntity(em);
            em.persist(roomType);
            em.flush();
        } else {
            roomType = TestUtil.findAll(em, RoomType.class).get(0);
        }
        room.setRoomType(roomType);
        return room;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room room = new Room()
            .roomName(UPDATED_ROOM_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .capacity(UPDATED_CAPACITY)
            .facilities(UPDATED_FACILITIES);
        // Add required entity
        Building building;
        if (TestUtil.findAll(em, Building.class).isEmpty()) {
            building = BuildingResourceIT.createUpdatedEntity(em);
            em.persist(building);
            em.flush();
        } else {
            building = TestUtil.findAll(em, Building.class).get(0);
        }
        room.setBuilding(building);
        // Add required entity
        Floor floor;
        if (TestUtil.findAll(em, Floor.class).isEmpty()) {
            floor = FloorResourceIT.createUpdatedEntity(em);
            em.persist(floor);
            em.flush();
        } else {
            floor = TestUtil.findAll(em, Floor.class).get(0);
        }
        room.setFloor(floor);
        // Add required entity
        RoomType roomType;
        if (TestUtil.findAll(em, RoomType.class).isEmpty()) {
            roomType = RoomTypeResourceIT.createUpdatedEntity(em);
            em.persist(roomType);
            em.flush();
        } else {
            roomType = TestUtil.findAll(em, RoomType.class).get(0);
        }
        room.setRoomType(roomType);
        return room;
    }

    @BeforeEach
    public void initTest() {
        room = createEntity(em);
    }

    @Test
    @Transactional
    void createRoom() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();
        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isCreated());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate + 1);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getRoomName()).isEqualTo(DEFAULT_ROOM_NAME);
        assertThat(testRoom.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testRoom.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRoom.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testRoom.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testRoom.getFacilities()).isEqualTo(DEFAULT_FACILITIES);
    }

    @Test
    @Transactional
    void createRoomWithExistingId() throws Exception {
        // Create the Room with an existing ID
        room.setId(1L);
        RoomDTO roomDTO = roomMapper.toDto(room);

        int databaseSizeBeforeCreate = roomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoomNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRepository.findAll().size();
        // set the field null
        room.setRoomName(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRepository.findAll().size();
        // set the field null
        room.setCapacity(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRooms() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomName").value(hasItem(DEFAULT_ROOM_NAME)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].facilities").value(hasItem(DEFAULT_FACILITIES.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRoomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(roomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(roomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRoomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(roomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(roomRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.roomName").value(DEFAULT_ROOM_NAME))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.facilities").value(DEFAULT_FACILITIES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room
        Room updatedRoom = roomRepository.findById(room.getId()).get();
        // Disconnect from session so that the updates on updatedRoom are not directly saved in db
        em.detach(updatedRoom);
        updatedRoom
            .roomName(UPDATED_ROOM_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .capacity(UPDATED_CAPACITY)
            .facilities(UPDATED_FACILITIES);
        RoomDTO roomDTO = roomMapper.toDto(updatedRoom);

        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getRoomName()).isEqualTo(UPDATED_ROOM_NAME);
        assertThat(testRoom.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testRoom.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoom.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testRoom.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testRoom.getFacilities()).isEqualTo(UPDATED_FACILITIES);
    }

    @Test
    @Transactional
    void putNonExistingRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .facilities(UPDATED_FACILITIES);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getRoomName()).isEqualTo(DEFAULT_ROOM_NAME);
        assertThat(testRoom.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testRoom.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoom.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testRoom.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testRoom.getFacilities()).isEqualTo(UPDATED_FACILITIES);
    }

    @Test
    @Transactional
    void fullUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom
            .roomName(UPDATED_ROOM_NAME)
            .remarks(UPDATED_REMARKS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .capacity(UPDATED_CAPACITY)
            .facilities(UPDATED_FACILITIES);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getRoomName()).isEqualTo(UPDATED_ROOM_NAME);
        assertThat(testRoom.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testRoom.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoom.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testRoom.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testRoom.getFacilities()).isEqualTo(UPDATED_FACILITIES);
    }

    @Test
    @Transactional
    void patchNonExistingRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(count.incrementAndGet());

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeDelete = roomRepository.findAll().size();

        // Delete the room
        restRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, room.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
