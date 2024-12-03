package com.bits.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bits.hr.IntegrationTest;
import com.bits.hr.domain.Unit;
import com.bits.hr.repository.UnitRepository;
import com.bits.hr.service.dto.UnitDTO;
import com.bits.hr.service.mapper.UnitMapper;
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
 * Integration tests for the {@link UnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnitResourceIT {

    private static final String DEFAULT_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnitMockMvc;

    private Unit unit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unit createEntity(EntityManager em) {
        Unit unit = new Unit().unitName(DEFAULT_UNIT_NAME);
        return unit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unit createUpdatedEntity(EntityManager em) {
        Unit unit = new Unit().unitName(UPDATED_UNIT_NAME);
        return unit;
    }

    @BeforeEach
    public void initTest() {
        unit = createEntity(em);
    }

    @Test
    @Transactional
    void createUnit() throws Exception {
        int databaseSizeBeforeCreate = unitRepository.findAll().size();
        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);
        restUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isCreated());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeCreate + 1);
        Unit testUnit = unitList.get(unitList.size() - 1);
        assertThat(testUnit.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
    }

    @Test
    @Transactional
    void createUnitWithExistingId() throws Exception {
        // Create the Unit with an existing ID
        unit.setId(1L);
        UnitDTO unitDTO = unitMapper.toDto(unit);

        int databaseSizeBeforeCreate = unitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUnitNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = unitRepository.findAll().size();
        // set the field null
        unit.setUnitName(null);

        // Create the Unit, which fails.
        UnitDTO unitDTO = unitMapper.toDto(unit);

        restUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isBadRequest());

        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnits() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        // Get all the unitList
        restUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unit.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME)));
    }

    @Test
    @Transactional
    void getUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        // Get the unit
        restUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, unit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unit.getId().intValue()))
            .andExpect(jsonPath("$.unitName").value(DEFAULT_UNIT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingUnit() throws Exception {
        // Get the unit
        restUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        int databaseSizeBeforeUpdate = unitRepository.findAll().size();

        // Update the unit
        Unit updatedUnit = unitRepository.findById(unit.getId()).get();
        // Disconnect from session so that the updates on updatedUnit are not directly saved in db
        em.detach(updatedUnit);
        updatedUnit.unitName(UPDATED_UNIT_NAME);
        UnitDTO unitDTO = unitMapper.toDto(updatedUnit);

        restUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitDTO))
            )
            .andExpect(status().isOk());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
        Unit testUnit = unitList.get(unitList.size() - 1);
        assertThat(testUnit.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingUnit() throws Exception {
        int databaseSizeBeforeUpdate = unitRepository.findAll().size();
        unit.setId(count.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnit() throws Exception {
        int databaseSizeBeforeUpdate = unitRepository.findAll().size();
        unit.setId(count.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnit() throws Exception {
        int databaseSizeBeforeUpdate = unitRepository.findAll().size();
        unit.setId(count.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnitWithPatch() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        int databaseSizeBeforeUpdate = unitRepository.findAll().size();

        // Update the unit using partial update
        Unit partialUpdatedUnit = new Unit();
        partialUpdatedUnit.setId(unit.getId());

        partialUpdatedUnit.unitName(UPDATED_UNIT_NAME);

        restUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnit))
            )
            .andExpect(status().isOk());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
        Unit testUnit = unitList.get(unitList.size() - 1);
        assertThat(testUnit.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateUnitWithPatch() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        int databaseSizeBeforeUpdate = unitRepository.findAll().size();

        // Update the unit using partial update
        Unit partialUpdatedUnit = new Unit();
        partialUpdatedUnit.setId(unit.getId());

        partialUpdatedUnit.unitName(UPDATED_UNIT_NAME);

        restUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnit))
            )
            .andExpect(status().isOk());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
        Unit testUnit = unitList.get(unitList.size() - 1);
        assertThat(testUnit.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingUnit() throws Exception {
        int databaseSizeBeforeUpdate = unitRepository.findAll().size();
        unit.setId(count.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnit() throws Exception {
        int databaseSizeBeforeUpdate = unitRepository.findAll().size();
        unit.setId(count.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnit() throws Exception {
        int databaseSizeBeforeUpdate = unitRepository.findAll().size();
        unit.setId(count.incrementAndGet());

        // Create the Unit
        UnitDTO unitDTO = unitMapper.toDto(unit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(unitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Unit in the database
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnit() throws Exception {
        // Initialize the database
        unitRepository.saveAndFlush(unit);

        int databaseSizeBeforeDelete = unitRepository.findAll().size();

        // Delete the unit
        restUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, unit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Unit> unitList = unitRepository.findAll();
        assertThat(unitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
