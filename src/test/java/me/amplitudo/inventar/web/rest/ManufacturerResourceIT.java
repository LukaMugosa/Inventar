package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.Manufacturer;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.repository.ManufacturerRepository;
import me.amplitudo.inventar.service.ManufacturerService;
import me.amplitudo.inventar.service.dto.ManufacturerDTO;
import me.amplitudo.inventar.service.mapper.ManufacturerMapper;
import me.amplitudo.inventar.service.dto.ManufacturerCriteria;
import me.amplitudo.inventar.service.ManufacturerQueryService;

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
 * Integration tests for the {@link ManufacturerResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ManufacturerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ManufacturerMapper manufacturerMapper;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ManufacturerQueryService manufacturerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManufacturerMockMvc;

    private Manufacturer manufacturer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufacturer createEntity(EntityManager em) {
        Manufacturer manufacturer = new Manufacturer()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return manufacturer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manufacturer createUpdatedEntity(EntityManager em) {
        Manufacturer manufacturer = new Manufacturer()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return manufacturer;
    }

    @BeforeEach
    public void initTest() {
        manufacturer = createEntity(em);
    }

    @Test
    @Transactional
    public void createManufacturer() throws Exception {
        int databaseSizeBeforeCreate = manufacturerRepository.findAll().size();
        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);
        restManufacturerMockMvc.perform(post("/api/manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO)))
            .andExpect(status().isCreated());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeCreate + 1);
        Manufacturer testManufacturer = manufacturerList.get(manufacturerList.size() - 1);
        assertThat(testManufacturer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testManufacturer.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testManufacturer.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createManufacturerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = manufacturerRepository.findAll().size();

        // Create the Manufacturer with an existing ID
        manufacturer.setId(1L);
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restManufacturerMockMvc.perform(post("/api/manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = manufacturerRepository.findAll().size();
        // set the field null
        manufacturer.setName(null);

        // Create the Manufacturer, which fails.
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);


        restManufacturerMockMvc.perform(post("/api/manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO)))
            .andExpect(status().isBadRequest());

        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllManufacturers() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList
        restManufacturerMockMvc.perform(get("/api/manufacturers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get the manufacturer
        restManufacturerMockMvc.perform(get("/api/manufacturers/{id}", manufacturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manufacturer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getManufacturersByIdFiltering() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        Long id = manufacturer.getId();

        defaultManufacturerShouldBeFound("id.equals=" + id);
        defaultManufacturerShouldNotBeFound("id.notEquals=" + id);

        defaultManufacturerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultManufacturerShouldNotBeFound("id.greaterThan=" + id);

        defaultManufacturerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultManufacturerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllManufacturersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name equals to DEFAULT_NAME
        defaultManufacturerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the manufacturerList where name equals to UPDATED_NAME
        defaultManufacturerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllManufacturersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name not equals to DEFAULT_NAME
        defaultManufacturerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the manufacturerList where name not equals to UPDATED_NAME
        defaultManufacturerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllManufacturersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultManufacturerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the manufacturerList where name equals to UPDATED_NAME
        defaultManufacturerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllManufacturersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name is not null
        defaultManufacturerShouldBeFound("name.specified=true");

        // Get all the manufacturerList where name is null
        defaultManufacturerShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllManufacturersByNameContainsSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name contains DEFAULT_NAME
        defaultManufacturerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the manufacturerList where name contains UPDATED_NAME
        defaultManufacturerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllManufacturersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where name does not contain DEFAULT_NAME
        defaultManufacturerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the manufacturerList where name does not contain UPDATED_NAME
        defaultManufacturerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllManufacturersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where createdAt equals to DEFAULT_CREATED_AT
        defaultManufacturerShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the manufacturerList where createdAt equals to UPDATED_CREATED_AT
        defaultManufacturerShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllManufacturersByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where createdAt not equals to DEFAULT_CREATED_AT
        defaultManufacturerShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the manufacturerList where createdAt not equals to UPDATED_CREATED_AT
        defaultManufacturerShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllManufacturersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultManufacturerShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the manufacturerList where createdAt equals to UPDATED_CREATED_AT
        defaultManufacturerShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllManufacturersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where createdAt is not null
        defaultManufacturerShouldBeFound("createdAt.specified=true");

        // Get all the manufacturerList where createdAt is null
        defaultManufacturerShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllManufacturersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultManufacturerShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the manufacturerList where updatedAt equals to UPDATED_UPDATED_AT
        defaultManufacturerShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllManufacturersByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultManufacturerShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the manufacturerList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultManufacturerShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllManufacturersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultManufacturerShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the manufacturerList where updatedAt equals to UPDATED_UPDATED_AT
        defaultManufacturerShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllManufacturersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturerList where updatedAt is not null
        defaultManufacturerShouldBeFound("updatedAt.specified=true");

        // Get all the manufacturerList where updatedAt is null
        defaultManufacturerShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllManufacturersByEquipmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);
        Equipment equipments = EquipmentResourceIT.createEntity(em);
        em.persist(equipments);
        em.flush();
        manufacturer.addEquipments(equipments);
        manufacturerRepository.saveAndFlush(manufacturer);
        Long equipmentsId = equipments.getId();

        // Get all the manufacturerList where equipments equals to equipmentsId
        defaultManufacturerShouldBeFound("equipmentsId.equals=" + equipmentsId);

        // Get all the manufacturerList where equipments equals to equipmentsId + 1
        defaultManufacturerShouldNotBeFound("equipmentsId.equals=" + (equipmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultManufacturerShouldBeFound(String filter) throws Exception {
        restManufacturerMockMvc.perform(get("/api/manufacturers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manufacturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restManufacturerMockMvc.perform(get("/api/manufacturers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultManufacturerShouldNotBeFound(String filter) throws Exception {
        restManufacturerMockMvc.perform(get("/api/manufacturers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restManufacturerMockMvc.perform(get("/api/manufacturers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingManufacturer() throws Exception {
        // Get the manufacturer
        restManufacturerMockMvc.perform(get("/api/manufacturers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();

        // Update the manufacturer
        Manufacturer updatedManufacturer = manufacturerRepository.findById(manufacturer.getId()).get();
        // Disconnect from session so that the updates on updatedManufacturer are not directly saved in db
        em.detach(updatedManufacturer);
        updatedManufacturer
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(updatedManufacturer);

        restManufacturerMockMvc.perform(put("/api/manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO)))
            .andExpect(status().isOk());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
        Manufacturer testManufacturer = manufacturerList.get(manufacturerList.size() - 1);
        assertThat(testManufacturer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManufacturer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testManufacturer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingManufacturer() throws Exception {
        int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();

        // Create the Manufacturer
        ManufacturerDTO manufacturerDTO = manufacturerMapper.toDto(manufacturer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManufacturerMockMvc.perform(put("/api/manufacturers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(manufacturerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        int databaseSizeBeforeDelete = manufacturerRepository.findAll().size();

        // Delete the manufacturer
        restManufacturerMockMvc.perform(delete("/api/manufacturers/{id}", manufacturer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        assertThat(manufacturerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
