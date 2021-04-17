package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.domain.EquipmentRequest;
import me.amplitudo.inventar.domain.EquipmentService;
import me.amplitudo.inventar.domain.EquipmentEmployee;
import me.amplitudo.inventar.domain.EquipmentImage;
import me.amplitudo.inventar.domain.Manufacturer;
import me.amplitudo.inventar.domain.EquipmentCategory;
import me.amplitudo.inventar.domain.Supplier;
import me.amplitudo.inventar.repository.EquipmentRepository;
import me.amplitudo.inventar.service.EquipmentService;
import me.amplitudo.inventar.service.dto.EquipmentDTO;
import me.amplitudo.inventar.service.mapper.EquipmentMapper;
import me.amplitudo.inventar.service.dto.EquipmentCriteria;
import me.amplitudo.inventar.service.EquipmentQueryService;

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
 * Integration tests for the {@link EquipmentResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EquipmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;
    private static final Integer SMALLER_STOCK = 1 - 1;

    private static final Double DEFAULT_PRICE_PER_UNIT = 1D;
    private static final Double UPDATED_PRICE_PER_UNIT = 2D;
    private static final Double SMALLER_PRICE_PER_UNIT = 1D - 1D;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentQueryService equipmentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createEntity(EntityManager em) {
        Equipment equipment = new Equipment()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .stock(DEFAULT_STOCK)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return equipment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createUpdatedEntity(EntityManager em) {
        Equipment equipment = new Equipment()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .stock(UPDATED_STOCK)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return equipment;
    }

    @BeforeEach
    public void initTest() {
        equipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();
        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEquipment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipment.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testEquipment.getPricePerUnit()).isEqualTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testEquipment.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEquipment.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createEquipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment with an existing ID
        equipment.setId(1L);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setName(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);


        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setDescription(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);


        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setStock(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);


        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricePerUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setPricePerUnit(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);


        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.pricePerUnit").value(DEFAULT_PRICE_PER_UNIT.doubleValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getEquipmentByIdFiltering() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        Long id = equipment.getId();

        defaultEquipmentShouldBeFound("id.equals=" + id);
        defaultEquipmentShouldNotBeFound("id.notEquals=" + id);

        defaultEquipmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipmentShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEquipmentByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name equals to DEFAULT_NAME
        defaultEquipmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the equipmentList where name equals to UPDATED_NAME
        defaultEquipmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name not equals to DEFAULT_NAME
        defaultEquipmentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the equipmentList where name not equals to UPDATED_NAME
        defaultEquipmentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentByNameIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEquipmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the equipmentList where name equals to UPDATED_NAME
        defaultEquipmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name is not null
        defaultEquipmentShouldBeFound("name.specified=true");

        // Get all the equipmentList where name is null
        defaultEquipmentShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentByNameContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name contains DEFAULT_NAME
        defaultEquipmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the equipmentList where name contains UPDATED_NAME
        defaultEquipmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEquipmentByNameNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where name does not contain DEFAULT_NAME
        defaultEquipmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the equipmentList where name does not contain UPDATED_NAME
        defaultEquipmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllEquipmentByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where description equals to DEFAULT_DESCRIPTION
        defaultEquipmentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentList where description equals to UPDATED_DESCRIPTION
        defaultEquipmentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where description not equals to DEFAULT_DESCRIPTION
        defaultEquipmentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentList where description not equals to UPDATED_DESCRIPTION
        defaultEquipmentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEquipmentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the equipmentList where description equals to UPDATED_DESCRIPTION
        defaultEquipmentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where description is not null
        defaultEquipmentShouldBeFound("description.specified=true");

        // Get all the equipmentList where description is null
        defaultEquipmentShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where description contains DEFAULT_DESCRIPTION
        defaultEquipmentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentList where description contains UPDATED_DESCRIPTION
        defaultEquipmentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where description does not contain DEFAULT_DESCRIPTION
        defaultEquipmentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentList where description does not contain UPDATED_DESCRIPTION
        defaultEquipmentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEquipmentByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where stock equals to DEFAULT_STOCK
        defaultEquipmentShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the equipmentList where stock equals to UPDATED_STOCK
        defaultEquipmentShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllEquipmentByStockIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where stock not equals to DEFAULT_STOCK
        defaultEquipmentShouldNotBeFound("stock.notEquals=" + DEFAULT_STOCK);

        // Get all the equipmentList where stock not equals to UPDATED_STOCK
        defaultEquipmentShouldBeFound("stock.notEquals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllEquipmentByStockIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultEquipmentShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the equipmentList where stock equals to UPDATED_STOCK
        defaultEquipmentShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllEquipmentByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where stock is not null
        defaultEquipmentShouldBeFound("stock.specified=true");

        // Get all the equipmentList where stock is null
        defaultEquipmentShouldNotBeFound("stock.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where stock is greater than or equal to DEFAULT_STOCK
        defaultEquipmentShouldBeFound("stock.greaterThanOrEqual=" + DEFAULT_STOCK);

        // Get all the equipmentList where stock is greater than or equal to UPDATED_STOCK
        defaultEquipmentShouldNotBeFound("stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllEquipmentByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where stock is less than or equal to DEFAULT_STOCK
        defaultEquipmentShouldBeFound("stock.lessThanOrEqual=" + DEFAULT_STOCK);

        // Get all the equipmentList where stock is less than or equal to SMALLER_STOCK
        defaultEquipmentShouldNotBeFound("stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    public void getAllEquipmentByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where stock is less than DEFAULT_STOCK
        defaultEquipmentShouldNotBeFound("stock.lessThan=" + DEFAULT_STOCK);

        // Get all the equipmentList where stock is less than UPDATED_STOCK
        defaultEquipmentShouldBeFound("stock.lessThan=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllEquipmentByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where stock is greater than DEFAULT_STOCK
        defaultEquipmentShouldNotBeFound("stock.greaterThan=" + DEFAULT_STOCK);

        // Get all the equipmentList where stock is greater than SMALLER_STOCK
        defaultEquipmentShouldBeFound("stock.greaterThan=" + SMALLER_STOCK);
    }


    @Test
    @Transactional
    public void getAllEquipmentByPricePerUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where pricePerUnit equals to DEFAULT_PRICE_PER_UNIT
        defaultEquipmentShouldBeFound("pricePerUnit.equals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the equipmentList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultEquipmentShouldNotBeFound("pricePerUnit.equals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByPricePerUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where pricePerUnit not equals to DEFAULT_PRICE_PER_UNIT
        defaultEquipmentShouldNotBeFound("pricePerUnit.notEquals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the equipmentList where pricePerUnit not equals to UPDATED_PRICE_PER_UNIT
        defaultEquipmentShouldBeFound("pricePerUnit.notEquals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByPricePerUnitIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where pricePerUnit in DEFAULT_PRICE_PER_UNIT or UPDATED_PRICE_PER_UNIT
        defaultEquipmentShouldBeFound("pricePerUnit.in=" + DEFAULT_PRICE_PER_UNIT + "," + UPDATED_PRICE_PER_UNIT);

        // Get all the equipmentList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultEquipmentShouldNotBeFound("pricePerUnit.in=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByPricePerUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where pricePerUnit is not null
        defaultEquipmentShouldBeFound("pricePerUnit.specified=true");

        // Get all the equipmentList where pricePerUnit is null
        defaultEquipmentShouldNotBeFound("pricePerUnit.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentByPricePerUnitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where pricePerUnit is greater than or equal to DEFAULT_PRICE_PER_UNIT
        defaultEquipmentShouldBeFound("pricePerUnit.greaterThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the equipmentList where pricePerUnit is greater than or equal to UPDATED_PRICE_PER_UNIT
        defaultEquipmentShouldNotBeFound("pricePerUnit.greaterThanOrEqual=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByPricePerUnitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where pricePerUnit is less than or equal to DEFAULT_PRICE_PER_UNIT
        defaultEquipmentShouldBeFound("pricePerUnit.lessThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the equipmentList where pricePerUnit is less than or equal to SMALLER_PRICE_PER_UNIT
        defaultEquipmentShouldNotBeFound("pricePerUnit.lessThanOrEqual=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByPricePerUnitIsLessThanSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where pricePerUnit is less than DEFAULT_PRICE_PER_UNIT
        defaultEquipmentShouldNotBeFound("pricePerUnit.lessThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the equipmentList where pricePerUnit is less than UPDATED_PRICE_PER_UNIT
        defaultEquipmentShouldBeFound("pricePerUnit.lessThan=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByPricePerUnitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where pricePerUnit is greater than DEFAULT_PRICE_PER_UNIT
        defaultEquipmentShouldNotBeFound("pricePerUnit.greaterThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the equipmentList where pricePerUnit is greater than SMALLER_PRICE_PER_UNIT
        defaultEquipmentShouldBeFound("pricePerUnit.greaterThan=" + SMALLER_PRICE_PER_UNIT);
    }


    @Test
    @Transactional
    public void getAllEquipmentByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where createdAt equals to DEFAULT_CREATED_AT
        defaultEquipmentShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the equipmentList where createdAt equals to UPDATED_CREATED_AT
        defaultEquipmentShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where createdAt not equals to DEFAULT_CREATED_AT
        defaultEquipmentShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the equipmentList where createdAt not equals to UPDATED_CREATED_AT
        defaultEquipmentShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEquipmentShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the equipmentList where createdAt equals to UPDATED_CREATED_AT
        defaultEquipmentShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where createdAt is not null
        defaultEquipmentShouldBeFound("createdAt.specified=true");

        // Get all the equipmentList where createdAt is null
        defaultEquipmentShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultEquipmentShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the equipmentList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEquipmentShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultEquipmentShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the equipmentList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultEquipmentShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultEquipmentShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the equipmentList where updatedAt equals to UPDATED_UPDATED_AT
        defaultEquipmentShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipmentList where updatedAt is not null
        defaultEquipmentShouldBeFound("updatedAt.specified=true");

        // Get all the equipmentList where updatedAt is null
        defaultEquipmentShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentByEquipmentRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        EquipmentRequest equipmentRequest = EquipmentRequestResourceIT.createEntity(em);
        em.persist(equipmentRequest);
        em.flush();
        equipment.addEquipmentRequest(equipmentRequest);
        equipmentRepository.saveAndFlush(equipment);
        Long equipmentRequestId = equipmentRequest.getId();

        // Get all the equipmentList where equipmentRequest equals to equipmentRequestId
        defaultEquipmentShouldBeFound("equipmentRequestId.equals=" + equipmentRequestId);

        // Get all the equipmentList where equipmentRequest equals to equipmentRequestId + 1
        defaultEquipmentShouldNotBeFound("equipmentRequestId.equals=" + (equipmentRequestId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentByEquipmentServiceIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        EquipmentService equipmentService = EquipmentServiceResourceIT.createEntity(em);
        em.persist(equipmentService);
        em.flush();
        equipment.addEquipmentService(equipmentService);
        equipmentRepository.saveAndFlush(equipment);
        Long equipmentServiceId = equipmentService.getId();

        // Get all the equipmentList where equipmentService equals to equipmentServiceId
        defaultEquipmentShouldBeFound("equipmentServiceId.equals=" + equipmentServiceId);

        // Get all the equipmentList where equipmentService equals to equipmentServiceId + 1
        defaultEquipmentShouldNotBeFound("equipmentServiceId.equals=" + (equipmentServiceId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentByEquipmentEmployeesIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        EquipmentEmployee equipmentEmployees = EquipmentEmployeeResourceIT.createEntity(em);
        em.persist(equipmentEmployees);
        em.flush();
        equipment.addEquipmentEmployees(equipmentEmployees);
        equipmentRepository.saveAndFlush(equipment);
        Long equipmentEmployeesId = equipmentEmployees.getId();

        // Get all the equipmentList where equipmentEmployees equals to equipmentEmployeesId
        defaultEquipmentShouldBeFound("equipmentEmployeesId.equals=" + equipmentEmployeesId);

        // Get all the equipmentList where equipmentEmployees equals to equipmentEmployeesId + 1
        defaultEquipmentShouldNotBeFound("equipmentEmployeesId.equals=" + (equipmentEmployeesId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentByImagesIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        EquipmentImage images = EquipmentImageResourceIT.createEntity(em);
        em.persist(images);
        em.flush();
        equipment.addImages(images);
        equipmentRepository.saveAndFlush(equipment);
        Long imagesId = images.getId();

        // Get all the equipmentList where images equals to imagesId
        defaultEquipmentShouldBeFound("imagesId.equals=" + imagesId);

        // Get all the equipmentList where images equals to imagesId + 1
        defaultEquipmentShouldNotBeFound("imagesId.equals=" + (imagesId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentByManufacturerIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        Manufacturer manufacturer = ManufacturerResourceIT.createEntity(em);
        em.persist(manufacturer);
        em.flush();
        equipment.setManufacturer(manufacturer);
        equipmentRepository.saveAndFlush(equipment);
        Long manufacturerId = manufacturer.getId();

        // Get all the equipmentList where manufacturer equals to manufacturerId
        defaultEquipmentShouldBeFound("manufacturerId.equals=" + manufacturerId);

        // Get all the equipmentList where manufacturer equals to manufacturerId + 1
        defaultEquipmentShouldNotBeFound("manufacturerId.equals=" + (manufacturerId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentByEquipmentCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        EquipmentCategory equipmentCategory = EquipmentCategoryResourceIT.createEntity(em);
        em.persist(equipmentCategory);
        em.flush();
        equipment.setEquipmentCategory(equipmentCategory);
        equipmentRepository.saveAndFlush(equipment);
        Long equipmentCategoryId = equipmentCategory.getId();

        // Get all the equipmentList where equipmentCategory equals to equipmentCategoryId
        defaultEquipmentShouldBeFound("equipmentCategoryId.equals=" + equipmentCategoryId);

        // Get all the equipmentList where equipmentCategory equals to equipmentCategoryId + 1
        defaultEquipmentShouldNotBeFound("equipmentCategoryId.equals=" + (equipmentCategoryId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentBySuplierIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        Supplier suplier = SupplierResourceIT.createEntity(em);
        em.persist(suplier);
        em.flush();
        equipment.setSuplier(suplier);
        equipmentRepository.saveAndFlush(equipment);
        Long suplierId = suplier.getId();

        // Get all the equipmentList where suplier equals to suplierId
        defaultEquipmentShouldBeFound("suplierId.equals=" + suplierId);

        // Get all the equipmentList where suplier equals to suplierId + 1
        defaultEquipmentShouldNotBeFound("suplierId.equals=" + (suplierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentShouldBeFound(String filter) throws Exception {
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE_PER_UNIT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restEquipmentMockMvc.perform(get("/api/equipment/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentShouldNotBeFound(String filter) throws Exception {
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentMockMvc.perform(get("/api/equipment/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findById(equipment.getId()).get();
        // Disconnect from session so that the updates on updatedEquipment are not directly saved in db
        em.detach(updatedEquipment);
        updatedEquipment
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .stock(UPDATED_STOCK)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(updatedEquipment);

        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEquipment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipment.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testEquipment.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testEquipment.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEquipment.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Delete the equipment
        restEquipmentMockMvc.perform(delete("/api/equipment/{id}", equipment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
