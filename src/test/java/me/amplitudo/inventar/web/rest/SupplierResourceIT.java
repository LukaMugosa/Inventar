package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.Supplier;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.repository.SupplierRepository;
import me.amplitudo.inventar.service.SupplierService;
import me.amplitudo.inventar.service.dto.SupplierDTO;
import me.amplitudo.inventar.service.mapper.SupplierMapper;
import me.amplitudo.inventar.service.dto.SupplierCriteria;
import me.amplitudo.inventar.service.SupplierQueryService;

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
 * Integration tests for the {@link SupplierResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SupplierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierQueryService supplierQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createEntity(EntityManager em) {
        Supplier supplier = new Supplier()
            .name(DEFAULT_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return supplier;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createUpdatedEntity(EntityManager em) {
        Supplier supplier = new Supplier()
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return supplier;
    }

    @BeforeEach
    public void initTest() {
        supplier = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupplier() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();
        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);
        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isCreated());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate + 1);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSupplier.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSupplier.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSupplier.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSupplier.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSupplier.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createSupplierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // Create the Supplier with an existing ID
        supplier.setId(1L);
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setName(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);


        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setPhoneNumber(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);


        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setEmail(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);


        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setAddress(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);


        restSupplierMockMvc.perform(post("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuppliers() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList
        restSupplierMockMvc.perform(get("/api/suppliers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getSuppliersByIdFiltering() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        Long id = supplier.getId();

        defaultSupplierShouldBeFound("id.equals=" + id);
        defaultSupplierShouldNotBeFound("id.notEquals=" + id);

        defaultSupplierShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSupplierShouldNotBeFound("id.greaterThan=" + id);

        defaultSupplierShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSupplierShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSuppliersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name equals to DEFAULT_NAME
        defaultSupplierShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the supplierList where name equals to UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSuppliersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name not equals to DEFAULT_NAME
        defaultSupplierShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the supplierList where name not equals to UPDATED_NAME
        defaultSupplierShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSuppliersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSupplierShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the supplierList where name equals to UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSuppliersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name is not null
        defaultSupplierShouldBeFound("name.specified=true");

        // Get all the supplierList where name is null
        defaultSupplierShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSuppliersByNameContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name contains DEFAULT_NAME
        defaultSupplierShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the supplierList where name contains UPDATED_NAME
        defaultSupplierShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSuppliersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where name does not contain DEFAULT_NAME
        defaultSupplierShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the supplierList where name does not contain UPDATED_NAME
        defaultSupplierShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllSuppliersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultSupplierShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the supplierList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSupplierShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSuppliersByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultSupplierShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the supplierList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultSupplierShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSuppliersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultSupplierShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the supplierList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultSupplierShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSuppliersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phoneNumber is not null
        defaultSupplierShouldBeFound("phoneNumber.specified=true");

        // Get all the supplierList where phoneNumber is null
        defaultSupplierShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllSuppliersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultSupplierShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the supplierList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultSupplierShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllSuppliersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultSupplierShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the supplierList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultSupplierShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllSuppliersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email equals to DEFAULT_EMAIL
        defaultSupplierShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the supplierList where email equals to UPDATED_EMAIL
        defaultSupplierShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSuppliersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email not equals to DEFAULT_EMAIL
        defaultSupplierShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the supplierList where email not equals to UPDATED_EMAIL
        defaultSupplierShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSuppliersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultSupplierShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the supplierList where email equals to UPDATED_EMAIL
        defaultSupplierShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSuppliersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email is not null
        defaultSupplierShouldBeFound("email.specified=true");

        // Get all the supplierList where email is null
        defaultSupplierShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllSuppliersByEmailContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email contains DEFAULT_EMAIL
        defaultSupplierShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the supplierList where email contains UPDATED_EMAIL
        defaultSupplierShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllSuppliersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email does not contain DEFAULT_EMAIL
        defaultSupplierShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the supplierList where email does not contain UPDATED_EMAIL
        defaultSupplierShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllSuppliersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address equals to DEFAULT_ADDRESS
        defaultSupplierShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the supplierList where address equals to UPDATED_ADDRESS
        defaultSupplierShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSuppliersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address not equals to DEFAULT_ADDRESS
        defaultSupplierShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the supplierList where address not equals to UPDATED_ADDRESS
        defaultSupplierShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSuppliersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSupplierShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the supplierList where address equals to UPDATED_ADDRESS
        defaultSupplierShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSuppliersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address is not null
        defaultSupplierShouldBeFound("address.specified=true");

        // Get all the supplierList where address is null
        defaultSupplierShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllSuppliersByAddressContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address contains DEFAULT_ADDRESS
        defaultSupplierShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the supplierList where address contains UPDATED_ADDRESS
        defaultSupplierShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllSuppliersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address does not contain DEFAULT_ADDRESS
        defaultSupplierShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the supplierList where address does not contain UPDATED_ADDRESS
        defaultSupplierShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllSuppliersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where createdAt equals to DEFAULT_CREATED_AT
        defaultSupplierShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the supplierList where createdAt equals to UPDATED_CREATED_AT
        defaultSupplierShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSuppliersByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where createdAt not equals to DEFAULT_CREATED_AT
        defaultSupplierShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the supplierList where createdAt not equals to UPDATED_CREATED_AT
        defaultSupplierShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSuppliersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSupplierShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the supplierList where createdAt equals to UPDATED_CREATED_AT
        defaultSupplierShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSuppliersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where createdAt is not null
        defaultSupplierShouldBeFound("createdAt.specified=true");

        // Get all the supplierList where createdAt is null
        defaultSupplierShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuppliersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSupplierShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the supplierList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSupplierShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllSuppliersByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultSupplierShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the supplierList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultSupplierShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllSuppliersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSupplierShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the supplierList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSupplierShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllSuppliersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where updatedAt is not null
        defaultSupplierShouldBeFound("updatedAt.specified=true");

        // Get all the supplierList where updatedAt is null
        defaultSupplierShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllSuppliersByEquipmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);
        Equipment equipments = EquipmentResourceIT.createEntity(em);
        em.persist(equipments);
        em.flush();
        supplier.addEquipments(equipments);
        supplierRepository.saveAndFlush(supplier);
        Long equipmentsId = equipments.getId();

        // Get all the supplierList where equipments equals to equipmentsId
        defaultSupplierShouldBeFound("equipmentsId.equals=" + equipmentsId);

        // Get all the supplierList where equipments equals to equipmentsId + 1
        defaultSupplierShouldNotBeFound("equipmentsId.equals=" + (equipmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierShouldBeFound(String filter) throws Exception {
        restSupplierMockMvc.perform(get("/api/suppliers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restSupplierMockMvc.perform(get("/api/suppliers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierShouldNotBeFound(String filter) throws Exception {
        restSupplierMockMvc.perform(get("/api/suppliers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierMockMvc.perform(get("/api/suppliers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier
        Supplier updatedSupplier = supplierRepository.findById(supplier.getId()).get();
        // Disconnect from session so that the updates on updatedSupplier are not directly saved in db
        em.detach(updatedSupplier);
        updatedSupplier
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SupplierDTO supplierDTO = supplierMapper.toDto(updatedSupplier);

        restSupplierMockMvc.perform(put("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = supplierList.get(supplierList.size() - 1);
        assertThat(testSupplier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSupplier.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSupplier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSupplier.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSupplier.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSupplier.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingSupplier() throws Exception {
        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc.perform(put("/api/suppliers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        int databaseSizeBeforeDelete = supplierRepository.findAll().size();

        // Delete the supplier
        restSupplierMockMvc.perform(delete("/api/suppliers/{id}", supplier.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Supplier> supplierList = supplierRepository.findAll();
        assertThat(supplierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
