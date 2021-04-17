package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.EquipmentCategory;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.repository.EquipmentCategoryRepository;
import me.amplitudo.inventar.service.EquipmentCategoryService;
import me.amplitudo.inventar.service.dto.EquipmentCategoryDTO;
import me.amplitudo.inventar.service.mapper.EquipmentCategoryMapper;
import me.amplitudo.inventar.service.dto.EquipmentCategoryCriteria;
import me.amplitudo.inventar.service.EquipmentCategoryQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EquipmentCategoryResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EquipmentCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    private EquipmentCategoryMapper equipmentCategoryMapper;

    @Autowired
    private EquipmentCategoryService equipmentCategoryService;

    @Autowired
    private EquipmentCategoryQueryService equipmentCategoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentCategoryMockMvc;

    private EquipmentCategory equipmentCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentCategory createEntity(EntityManager em) {
        EquipmentCategory equipmentCategory = new EquipmentCategory()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return equipmentCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentCategory createUpdatedEntity(EntityManager em) {
        EquipmentCategory equipmentCategory = new EquipmentCategory()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return equipmentCategory;
    }

    @BeforeEach
    public void initTest() {
        equipmentCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentCategory() throws Exception {
        int databaseSizeBeforeCreate = equipmentCategoryRepository.findAll().size();
        // Create the EquipmentCategory
        EquipmentCategoryDTO equipmentCategoryDTO = equipmentCategoryMapper.toDto(equipmentCategory);
        restEquipmentCategoryMockMvc.perform(post("/api/equipment-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentCategory in the database
        List<EquipmentCategory> equipmentCategoryList = equipmentCategoryRepository.findAll();
        assertThat(equipmentCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentCategory testEquipmentCategory = equipmentCategoryList.get(equipmentCategoryList.size() - 1);
        assertThat(testEquipmentCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEquipmentCategory.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEquipmentCategory.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createEquipmentCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentCategoryRepository.findAll().size();

        // Create the EquipmentCategory with an existing ID
        equipmentCategory.setId(1L);
        EquipmentCategoryDTO equipmentCategoryDTO = equipmentCategoryMapper.toDto(equipmentCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentCategoryMockMvc.perform(post("/api/equipment-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentCategory in the database
        List<EquipmentCategory> equipmentCategoryList = equipmentCategoryRepository.findAll();
        assertThat(equipmentCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentCategoryRepository.findAll().size();
        // set the field null
        equipmentCategory.setName(null);

        // Create the EquipmentCategory, which fails.
        EquipmentCategoryDTO equipmentCategoryDTO = equipmentCategoryMapper.toDto(equipmentCategory);


        restEquipmentCategoryMockMvc.perform(post("/api/equipment-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentCategory> equipmentCategoryList = equipmentCategoryRepository.findAll();
        assertThat(equipmentCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategories() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList
        restEquipmentCategoryMockMvc.perform(get("/api/equipment-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipmentCategory() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get the equipmentCategory
        restEquipmentCategoryMockMvc.perform(get("/api/equipment-categories/{id}", equipmentCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getEquipmentCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        Long id = equipmentCategory.getId();

        defaultEquipmentCategoryShouldBeFound("id.equals=" + id);
        defaultEquipmentCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultEquipmentCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipmentCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipmentCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipmentCategoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEquipmentCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where name equals to DEFAULT_NAME
        defaultEquipmentCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the equipmentCategoryList where name equals to UPDATED_NAME
        defaultEquipmentCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where name not equals to DEFAULT_NAME
        defaultEquipmentCategoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the equipmentCategoryList where name not equals to UPDATED_NAME
        defaultEquipmentCategoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEquipmentCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the equipmentCategoryList where name equals to UPDATED_NAME
        defaultEquipmentCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where name is not null
        defaultEquipmentCategoryShouldBeFound("name.specified=true");

        // Get all the equipmentCategoryList where name is null
        defaultEquipmentCategoryShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where name contains DEFAULT_NAME
        defaultEquipmentCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the equipmentCategoryList where name contains UPDATED_NAME
        defaultEquipmentCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where name does not contain DEFAULT_NAME
        defaultEquipmentCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the equipmentCategoryList where name does not contain UPDATED_NAME
        defaultEquipmentCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllEquipmentCategoriesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where createdAt equals to DEFAULT_CREATED_AT
        defaultEquipmentCategoryShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the equipmentCategoryList where createdAt equals to UPDATED_CREATED_AT
        defaultEquipmentCategoryShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where createdAt not equals to DEFAULT_CREATED_AT
        defaultEquipmentCategoryShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the equipmentCategoryList where createdAt not equals to UPDATED_CREATED_AT
        defaultEquipmentCategoryShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEquipmentCategoryShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the equipmentCategoryList where createdAt equals to UPDATED_CREATED_AT
        defaultEquipmentCategoryShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where createdAt is not null
        defaultEquipmentCategoryShouldBeFound("createdAt.specified=true");

        // Get all the equipmentCategoryList where createdAt is null
        defaultEquipmentCategoryShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEquipmentCategoryShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the equipmentCategoryList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEquipmentCategoryShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultEquipmentCategoryShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the equipmentCategoryList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultEquipmentCategoryShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEquipmentCategoryShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the equipmentCategoryList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEquipmentCategoryShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        // Get all the equipmentCategoryList where updatedAt is not null
        defaultEquipmentCategoryShouldBeFound("updatedAt.specified=true");

        // Get all the equipmentCategoryList where updatedAt is null
        defaultEquipmentCategoryShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentCategoriesByEquipmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);
        Equipment equipments = EquipmentResourceIT.createEntity(em);
        em.persist(equipments);
        em.flush();
        equipmentCategory.addEquipments(equipments);
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);
        Long equipmentsId = equipments.getId();

        // Get all the equipmentCategoryList where equipments equals to equipmentsId
        defaultEquipmentCategoryShouldBeFound("equipmentsId.equals=" + equipmentsId);

        // Get all the equipmentCategoryList where equipments equals to equipmentsId + 1
        defaultEquipmentCategoryShouldNotBeFound("equipmentsId.equals=" + (equipmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentCategoryShouldBeFound(String filter) throws Exception {
        restEquipmentCategoryMockMvc.perform(get("/api/equipment-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restEquipmentCategoryMockMvc.perform(get("/api/equipment-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentCategoryShouldNotBeFound(String filter) throws Exception {
        restEquipmentCategoryMockMvc.perform(get("/api/equipment-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentCategoryMockMvc.perform(get("/api/equipment-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentCategory() throws Exception {
        // Get the equipmentCategory
        restEquipmentCategoryMockMvc.perform(get("/api/equipment-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentCategory() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        int databaseSizeBeforeUpdate = equipmentCategoryRepository.findAll().size();

        // Update the equipmentCategory
        EquipmentCategory updatedEquipmentCategory = equipmentCategoryRepository.findById(equipmentCategory.getId()).get();
        // Disconnect from session so that the updates on updatedEquipmentCategory are not directly saved in db
        em.detach(updatedEquipmentCategory);
        updatedEquipmentCategory
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EquipmentCategoryDTO equipmentCategoryDTO = equipmentCategoryMapper.toDto(updatedEquipmentCategory);

        restEquipmentCategoryMockMvc.perform(put("/api/equipment-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentCategory in the database
        List<EquipmentCategory> equipmentCategoryList = equipmentCategoryRepository.findAll();
        assertThat(equipmentCategoryList).hasSize(databaseSizeBeforeUpdate);
        EquipmentCategory testEquipmentCategory = equipmentCategoryList.get(equipmentCategoryList.size() - 1);
        assertThat(testEquipmentCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEquipmentCategory.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEquipmentCategory.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentCategory() throws Exception {
        int databaseSizeBeforeUpdate = equipmentCategoryRepository.findAll().size();

        // Create the EquipmentCategory
        EquipmentCategoryDTO equipmentCategoryDTO = equipmentCategoryMapper.toDto(equipmentCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentCategoryMockMvc.perform(put("/api/equipment-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentCategory in the database
        List<EquipmentCategory> equipmentCategoryList = equipmentCategoryRepository.findAll();
        assertThat(equipmentCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipmentCategory() throws Exception {
        // Initialize the database
        equipmentCategoryRepository.saveAndFlush(equipmentCategory);

        int databaseSizeBeforeDelete = equipmentCategoryRepository.findAll().size();

        // Delete the equipmentCategory
        restEquipmentCategoryMockMvc.perform(delete("/api/equipment-categories/{id}", equipmentCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipmentCategory> equipmentCategoryList = equipmentCategoryRepository.findAll();
        assertThat(equipmentCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
