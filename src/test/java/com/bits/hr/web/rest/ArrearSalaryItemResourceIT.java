package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.ArrearSalaryItem;
import com.bits.hr.domain.ArrearSalaryMaster;
import com.bits.hr.domain.Employee;
import com.bits.hr.repository.ArrearSalaryItemRepository;
import com.bits.hr.service.dto.ArrearSalaryItemDTO;
import com.bits.hr.service.mapper.ArrearSalaryItemMapper;
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
 * Integration tests for the {@link ArrearSalaryItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArrearSalaryItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_ARREAR_AMOUNT = 0D;
    private static final Double UPDATED_ARREAR_AMOUNT = 1D;

    private static final Boolean DEFAULT_HAS_PF_ARREAR_DEDUCTION = false;
    private static final Boolean UPDATED_HAS_PF_ARREAR_DEDUCTION = true;

    private static final Double DEFAULT_PF_ARREAR_DEDUCTION = 1D;
    private static final Double UPDATED_PF_ARREAR_DEDUCTION = 2D;

    private static final Boolean DEFAULT_IS_FESTIVAL_BONUS = false;
    private static final Boolean UPDATED_IS_FESTIVAL_BONUS = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/arrear-salary-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArrearSalaryItemRepository arrearSalaryItemRepository;

    @Autowired
    private ArrearSalaryItemMapper arrearSalaryItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArrearSalaryItemMockMvc;

    private ArrearSalaryItem arrearSalaryItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArrearSalaryItem createEntity(EntityManager em) {
        ArrearSalaryItem arrearSalaryItem = new ArrearSalaryItem()
            .title(DEFAULT_TITLE)
            .arrearAmount(DEFAULT_ARREAR_AMOUNT)
            .hasPfArrearDeduction(DEFAULT_HAS_PF_ARREAR_DEDUCTION)
            .pfArrearDeduction(DEFAULT_PF_ARREAR_DEDUCTION)
            .isFestivalBonus(DEFAULT_IS_FESTIVAL_BONUS)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        ArrearSalaryMaster arrearSalaryMaster;
        if (TestUtil.findAll(em, ArrearSalaryMaster.class).isEmpty()) {
            arrearSalaryMaster = ArrearSalaryMasterResourceIT.createEntity(em);
            em.persist(arrearSalaryMaster);
            em.flush();
        } else {
            arrearSalaryMaster = TestUtil.findAll(em, ArrearSalaryMaster.class).get(0);
        }
        arrearSalaryItem.setArrearSalaryMaster(arrearSalaryMaster);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        arrearSalaryItem.setEmployee(employee);
        return arrearSalaryItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArrearSalaryItem createUpdatedEntity(EntityManager em) {
        ArrearSalaryItem arrearSalaryItem = new ArrearSalaryItem()
            .title(UPDATED_TITLE)
            .arrearAmount(UPDATED_ARREAR_AMOUNT)
            .hasPfArrearDeduction(UPDATED_HAS_PF_ARREAR_DEDUCTION)
            .pfArrearDeduction(UPDATED_PF_ARREAR_DEDUCTION)
            .isFestivalBonus(UPDATED_IS_FESTIVAL_BONUS)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        ArrearSalaryMaster arrearSalaryMaster;
        if (TestUtil.findAll(em, ArrearSalaryMaster.class).isEmpty()) {
            arrearSalaryMaster = ArrearSalaryMasterResourceIT.createUpdatedEntity(em);
            em.persist(arrearSalaryMaster);
            em.flush();
        } else {
            arrearSalaryMaster = TestUtil.findAll(em, ArrearSalaryMaster.class).get(0);
        }
        arrearSalaryItem.setArrearSalaryMaster(arrearSalaryMaster);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        arrearSalaryItem.setEmployee(employee);
        return arrearSalaryItem;
    }

    @BeforeEach
    public void initTest() {
        arrearSalaryItem = createEntity(em);
    }

    @Test
    @Transactional
    void createArrearSalaryItem() throws Exception {
        int databaseSizeBeforeCreate = arrearSalaryItemRepository.findAll().size();
        // Create the ArrearSalaryItem
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);
        restArrearSalaryItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeCreate + 1);
        ArrearSalaryItem testArrearSalaryItem = arrearSalaryItemList.get(arrearSalaryItemList.size() - 1);
        assertThat(testArrearSalaryItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArrearSalaryItem.getArrearAmount()).isEqualTo(DEFAULT_ARREAR_AMOUNT);
        assertThat(testArrearSalaryItem.getHasPfArrearDeduction()).isEqualTo(DEFAULT_HAS_PF_ARREAR_DEDUCTION);
        assertThat(testArrearSalaryItem.getPfArrearDeduction()).isEqualTo(DEFAULT_PF_ARREAR_DEDUCTION);
        assertThat(testArrearSalaryItem.getIsFestivalBonus()).isEqualTo(DEFAULT_IS_FESTIVAL_BONUS);
        assertThat(testArrearSalaryItem.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createArrearSalaryItemWithExistingId() throws Exception {
        // Create the ArrearSalaryItem with an existing ID
        arrearSalaryItem.setId(1L);
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        int databaseSizeBeforeCreate = arrearSalaryItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArrearSalaryItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryItemRepository.findAll().size();
        // set the field null
        arrearSalaryItem.setTitle(null);

        // Create the ArrearSalaryItem, which fails.
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        restArrearSalaryItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArrearAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryItemRepository.findAll().size();
        // set the field null
        arrearSalaryItem.setArrearAmount(null);

        // Create the ArrearSalaryItem, which fails.
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        restArrearSalaryItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = arrearSalaryItemRepository.findAll().size();
        // set the field null
        arrearSalaryItem.setIsDeleted(null);

        // Create the ArrearSalaryItem, which fails.
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        restArrearSalaryItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArrearSalaryItems() throws Exception {
        // Initialize the database
        arrearSalaryItemRepository.saveAndFlush(arrearSalaryItem);

        // Get all the arrearSalaryItemList
        restArrearSalaryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arrearSalaryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].arrearAmount").value(hasItem(DEFAULT_ARREAR_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].hasPfArrearDeduction").value(hasItem(DEFAULT_HAS_PF_ARREAR_DEDUCTION.booleanValue())))
            .andExpect(jsonPath("$.[*].pfArrearDeduction").value(hasItem(DEFAULT_PF_ARREAR_DEDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].isFestivalBonus").value(hasItem(DEFAULT_IS_FESTIVAL_BONUS.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getArrearSalaryItem() throws Exception {
        // Initialize the database
        arrearSalaryItemRepository.saveAndFlush(arrearSalaryItem);

        // Get the arrearSalaryItem
        restArrearSalaryItemMockMvc
            .perform(get(ENTITY_API_URL_ID, arrearSalaryItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arrearSalaryItem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.arrearAmount").value(DEFAULT_ARREAR_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.hasPfArrearDeduction").value(DEFAULT_HAS_PF_ARREAR_DEDUCTION.booleanValue()))
            .andExpect(jsonPath("$.pfArrearDeduction").value(DEFAULT_PF_ARREAR_DEDUCTION.doubleValue()))
            .andExpect(jsonPath("$.isFestivalBonus").value(DEFAULT_IS_FESTIVAL_BONUS.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingArrearSalaryItem() throws Exception {
        // Get the arrearSalaryItem
        restArrearSalaryItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArrearSalaryItem() throws Exception {
        // Initialize the database
        arrearSalaryItemRepository.saveAndFlush(arrearSalaryItem);

        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();

        // Update the arrearSalaryItem
        ArrearSalaryItem updatedArrearSalaryItem = arrearSalaryItemRepository.findById(arrearSalaryItem.getId()).get();
        // Disconnect from session so that the updates on updatedArrearSalaryItem are not directly saved in db
        em.detach(updatedArrearSalaryItem);
        updatedArrearSalaryItem
            .title(UPDATED_TITLE)
            .arrearAmount(UPDATED_ARREAR_AMOUNT)
            .hasPfArrearDeduction(UPDATED_HAS_PF_ARREAR_DEDUCTION)
            .pfArrearDeduction(UPDATED_PF_ARREAR_DEDUCTION)
            .isFestivalBonus(UPDATED_IS_FESTIVAL_BONUS)
            .isDeleted(UPDATED_IS_DELETED);
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(updatedArrearSalaryItem);

        restArrearSalaryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arrearSalaryItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalaryItem testArrearSalaryItem = arrearSalaryItemList.get(arrearSalaryItemList.size() - 1);
        assertThat(testArrearSalaryItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArrearSalaryItem.getArrearAmount()).isEqualTo(UPDATED_ARREAR_AMOUNT);
        assertThat(testArrearSalaryItem.getHasPfArrearDeduction()).isEqualTo(UPDATED_HAS_PF_ARREAR_DEDUCTION);
        assertThat(testArrearSalaryItem.getPfArrearDeduction()).isEqualTo(UPDATED_PF_ARREAR_DEDUCTION);
        assertThat(testArrearSalaryItem.getIsFestivalBonus()).isEqualTo(UPDATED_IS_FESTIVAL_BONUS);
        assertThat(testArrearSalaryItem.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingArrearSalaryItem() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();
        arrearSalaryItem.setId(count.incrementAndGet());

        // Create the ArrearSalaryItem
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArrearSalaryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arrearSalaryItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArrearSalaryItem() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();
        arrearSalaryItem.setId(count.incrementAndGet());

        // Create the ArrearSalaryItem
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArrearSalaryItem() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();
        arrearSalaryItem.setId(count.incrementAndGet());

        // Create the ArrearSalaryItem
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArrearSalaryItemWithPatch() throws Exception {
        // Initialize the database
        arrearSalaryItemRepository.saveAndFlush(arrearSalaryItem);

        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();

        // Update the arrearSalaryItem using partial update
        ArrearSalaryItem partialUpdatedArrearSalaryItem = new ArrearSalaryItem();
        partialUpdatedArrearSalaryItem.setId(arrearSalaryItem.getId());

        partialUpdatedArrearSalaryItem
            .title(UPDATED_TITLE)
            .hasPfArrearDeduction(UPDATED_HAS_PF_ARREAR_DEDUCTION)
            .isFestivalBonus(UPDATED_IS_FESTIVAL_BONUS)
            .isDeleted(UPDATED_IS_DELETED);

        restArrearSalaryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArrearSalaryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArrearSalaryItem))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalaryItem testArrearSalaryItem = arrearSalaryItemList.get(arrearSalaryItemList.size() - 1);
        assertThat(testArrearSalaryItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArrearSalaryItem.getArrearAmount()).isEqualTo(DEFAULT_ARREAR_AMOUNT);
        assertThat(testArrearSalaryItem.getHasPfArrearDeduction()).isEqualTo(UPDATED_HAS_PF_ARREAR_DEDUCTION);
        assertThat(testArrearSalaryItem.getPfArrearDeduction()).isEqualTo(DEFAULT_PF_ARREAR_DEDUCTION);
        assertThat(testArrearSalaryItem.getIsFestivalBonus()).isEqualTo(UPDATED_IS_FESTIVAL_BONUS);
        assertThat(testArrearSalaryItem.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateArrearSalaryItemWithPatch() throws Exception {
        // Initialize the database
        arrearSalaryItemRepository.saveAndFlush(arrearSalaryItem);

        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();

        // Update the arrearSalaryItem using partial update
        ArrearSalaryItem partialUpdatedArrearSalaryItem = new ArrearSalaryItem();
        partialUpdatedArrearSalaryItem.setId(arrearSalaryItem.getId());

        partialUpdatedArrearSalaryItem
            .title(UPDATED_TITLE)
            .arrearAmount(UPDATED_ARREAR_AMOUNT)
            .hasPfArrearDeduction(UPDATED_HAS_PF_ARREAR_DEDUCTION)
            .pfArrearDeduction(UPDATED_PF_ARREAR_DEDUCTION)
            .isFestivalBonus(UPDATED_IS_FESTIVAL_BONUS)
            .isDeleted(UPDATED_IS_DELETED);

        restArrearSalaryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArrearSalaryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArrearSalaryItem))
            )
            .andExpect(status().isOk());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
        ArrearSalaryItem testArrearSalaryItem = arrearSalaryItemList.get(arrearSalaryItemList.size() - 1);
        assertThat(testArrearSalaryItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArrearSalaryItem.getArrearAmount()).isEqualTo(UPDATED_ARREAR_AMOUNT);
        assertThat(testArrearSalaryItem.getHasPfArrearDeduction()).isEqualTo(UPDATED_HAS_PF_ARREAR_DEDUCTION);
        assertThat(testArrearSalaryItem.getPfArrearDeduction()).isEqualTo(UPDATED_PF_ARREAR_DEDUCTION);
        assertThat(testArrearSalaryItem.getIsFestivalBonus()).isEqualTo(UPDATED_IS_FESTIVAL_BONUS);
        assertThat(testArrearSalaryItem.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingArrearSalaryItem() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();
        arrearSalaryItem.setId(count.incrementAndGet());

        // Create the ArrearSalaryItem
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArrearSalaryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arrearSalaryItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArrearSalaryItem() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();
        arrearSalaryItem.setId(count.incrementAndGet());

        // Create the ArrearSalaryItem
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArrearSalaryItem() throws Exception {
        int databaseSizeBeforeUpdate = arrearSalaryItemRepository.findAll().size();
        arrearSalaryItem.setId(count.incrementAndGet());

        // Create the ArrearSalaryItem
        ArrearSalaryItemDTO arrearSalaryItemDTO = arrearSalaryItemMapper.toDto(arrearSalaryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArrearSalaryItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arrearSalaryItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArrearSalaryItem in the database
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArrearSalaryItem() throws Exception {
        // Initialize the database
        arrearSalaryItemRepository.saveAndFlush(arrearSalaryItem);

        int databaseSizeBeforeDelete = arrearSalaryItemRepository.findAll().size();

        // Delete the arrearSalaryItem
        restArrearSalaryItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, arrearSalaryItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArrearSalaryItem> arrearSalaryItemList = arrearSalaryItemRepository.findAll();
        assertThat(arrearSalaryItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
