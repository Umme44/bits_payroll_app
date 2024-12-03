package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Department;
import com.bits.hr.domain.ItemInformation;
import com.bits.hr.domain.UnitOfMeasurement;
import com.bits.hr.domain.User;
import com.bits.hr.repository.ItemInformationRepository;
import com.bits.hr.service.ItemInformationService;
import com.bits.hr.service.dto.ItemInformationDTO;
import com.bits.hr.service.mapper.ItemInformationMapper;
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
 * Integration tests for the {@link ItemInformationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ItemInformationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIFICATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/item-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemInformationRepository itemInformationRepository;

    @Mock
    private ItemInformationRepository itemInformationRepositoryMock;

    @Autowired
    private ItemInformationMapper itemInformationMapper;

    @Mock
    private ItemInformationService itemInformationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemInformationMockMvc;

    private ItemInformation itemInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemInformation createEntity(EntityManager em) {
        ItemInformation itemInformation = new ItemInformation()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .specification(DEFAULT_SPECIFICATION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        itemInformation.setDepartment(department);
        // Add required entity
        UnitOfMeasurement unitOfMeasurement;
        if (TestUtil.findAll(em, UnitOfMeasurement.class).isEmpty()) {
            unitOfMeasurement = UnitOfMeasurementResourceIT.createEntity(em);
            em.persist(unitOfMeasurement);
            em.flush();
        } else {
            unitOfMeasurement = TestUtil.findAll(em, UnitOfMeasurement.class).get(0);
        }
        itemInformation.setUnitOfMeasurement(unitOfMeasurement);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        itemInformation.setCreatedBy(user);
        return itemInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemInformation createUpdatedEntity(EntityManager em) {
        ItemInformation itemInformation = new ItemInformation()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .specification(UPDATED_SPECIFICATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        itemInformation.setDepartment(department);
        // Add required entity
        UnitOfMeasurement unitOfMeasurement;
        if (TestUtil.findAll(em, UnitOfMeasurement.class).isEmpty()) {
            unitOfMeasurement = UnitOfMeasurementResourceIT.createUpdatedEntity(em);
            em.persist(unitOfMeasurement);
            em.flush();
        } else {
            unitOfMeasurement = TestUtil.findAll(em, UnitOfMeasurement.class).get(0);
        }
        itemInformation.setUnitOfMeasurement(unitOfMeasurement);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        itemInformation.setCreatedBy(user);
        return itemInformation;
    }

    @BeforeEach
    public void initTest() {
        itemInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createItemInformation() throws Exception {
        int databaseSizeBeforeCreate = itemInformationRepository.findAll().size();
        // Create the ItemInformation
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);
        restItemInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeCreate + 1);
        ItemInformation testItemInformation = itemInformationList.get(itemInformationList.size() - 1);
        assertThat(testItemInformation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testItemInformation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemInformation.getSpecification()).isEqualTo(DEFAULT_SPECIFICATION);
        assertThat(testItemInformation.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testItemInformation.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createItemInformationWithExistingId() throws Exception {
        // Create the ItemInformation with an existing ID
        itemInformation.setId(1L);
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        int databaseSizeBeforeCreate = itemInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemInformationRepository.findAll().size();
        // set the field null
        itemInformation.setCode(null);

        // Create the ItemInformation, which fails.
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        restItemInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemInformationRepository.findAll().size();
        // set the field null
        itemInformation.setName(null);

        // Create the ItemInformation, which fails.
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        restItemInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemInformationRepository.findAll().size();
        // set the field null
        itemInformation.setCreatedAt(null);

        // Create the ItemInformation, which fails.
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        restItemInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemInformations() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

        // Get all the itemInformationList
        restItemInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].specification").value(hasItem(DEFAULT_SPECIFICATION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemInformationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemInformationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemInformationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(itemInformationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemInformationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemInformationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemInformationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(itemInformationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getItemInformation() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

        // Get the itemInformation
        restItemInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, itemInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemInformation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.specification").value(DEFAULT_SPECIFICATION.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingItemInformation() throws Exception {
        // Get the itemInformation
        restItemInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemInformation() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();

        // Update the itemInformation
        ItemInformation updatedItemInformation = itemInformationRepository.findById(itemInformation.getId()).get();
        // Disconnect from session so that the updates on updatedItemInformation are not directly saved in db
        em.detach(updatedItemInformation);
        updatedItemInformation
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .specification(UPDATED_SPECIFICATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(updatedItemInformation);

        restItemInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
        ItemInformation testItemInformation = itemInformationList.get(itemInformationList.size() - 1);
        assertThat(testItemInformation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItemInformation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemInformation.getSpecification()).isEqualTo(UPDATED_SPECIFICATION);
        assertThat(testItemInformation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testItemInformation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingItemInformation() throws Exception {
        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();
        itemInformation.setId(count.incrementAndGet());

        // Create the ItemInformation
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemInformation() throws Exception {
        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();
        itemInformation.setId(count.incrementAndGet());

        // Create the ItemInformation
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemInformation() throws Exception {
        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();
        itemInformation.setId(count.incrementAndGet());

        // Create the ItemInformation
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemInformationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemInformationWithPatch() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();

        // Update the itemInformation using partial update
        ItemInformation partialUpdatedItemInformation = new ItemInformation();
        partialUpdatedItemInformation.setId(itemInformation.getId());

        partialUpdatedItemInformation.specification(UPDATED_SPECIFICATION).updatedAt(UPDATED_UPDATED_AT);

        restItemInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemInformation))
            )
            .andExpect(status().isOk());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
        ItemInformation testItemInformation = itemInformationList.get(itemInformationList.size() - 1);
        assertThat(testItemInformation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testItemInformation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemInformation.getSpecification()).isEqualTo(UPDATED_SPECIFICATION);
        assertThat(testItemInformation.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testItemInformation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateItemInformationWithPatch() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();

        // Update the itemInformation using partial update
        ItemInformation partialUpdatedItemInformation = new ItemInformation();
        partialUpdatedItemInformation.setId(itemInformation.getId());

        partialUpdatedItemInformation
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .specification(UPDATED_SPECIFICATION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restItemInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemInformation))
            )
            .andExpect(status().isOk());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
        ItemInformation testItemInformation = itemInformationList.get(itemInformationList.size() - 1);
        assertThat(testItemInformation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItemInformation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemInformation.getSpecification()).isEqualTo(UPDATED_SPECIFICATION);
        assertThat(testItemInformation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testItemInformation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingItemInformation() throws Exception {
        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();
        itemInformation.setId(count.incrementAndGet());

        // Create the ItemInformation
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemInformation() throws Exception {
        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();
        itemInformation.setId(count.incrementAndGet());

        // Create the ItemInformation
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemInformation() throws Exception {
        int databaseSizeBeforeUpdate = itemInformationRepository.findAll().size();
        itemInformation.setId(count.incrementAndGet());

        // Create the ItemInformation
        ItemInformationDTO itemInformationDTO = itemInformationMapper.toDto(itemInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemInformation in the database
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemInformation() throws Exception {
        // Initialize the database
        itemInformationRepository.saveAndFlush(itemInformation);

        int databaseSizeBeforeDelete = itemInformationRepository.findAll().size();

        // Delete the itemInformation
        restItemInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemInformation> itemInformationList = itemInformationRepository.findAll();
        assertThat(itemInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
