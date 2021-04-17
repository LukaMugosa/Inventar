package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.Tenant;
import me.amplitudo.inventar.domain.Employee;
import me.amplitudo.inventar.repository.TenantRepository;
import me.amplitudo.inventar.service.TenantService;
import me.amplitudo.inventar.service.dto.TenantDTO;
import me.amplitudo.inventar.service.mapper.TenantMapper;
import me.amplitudo.inventar.service.dto.TenantCriteria;
import me.amplitudo.inventar.service.TenantQueryService;

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
 * Integration tests for the {@link TenantResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TenantResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantQueryService tenantQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantMockMvc;

    private Tenant tenant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tenant createEntity(EntityManager em) {
        Tenant tenant = new Tenant()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .active(DEFAULT_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return tenant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tenant createUpdatedEntity(EntityManager em) {
        Tenant tenant = new Tenant()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return tenant;
    }

    @BeforeEach
    public void initTest() {
        tenant = createEntity(em);
    }

    @Test
    @Transactional
    public void createTenant() throws Exception {
        int databaseSizeBeforeCreate = tenantRepository.findAll().size();
        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);
        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isCreated());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeCreate + 1);
        Tenant testTenant = tenantList.get(tenantList.size() - 1);
        assertThat(testTenant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTenant.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testTenant.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTenant.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testTenant.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTenant.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createTenantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tenantRepository.findAll().size();

        // Create the Tenant with an existing ID
        tenant.setId(1L);
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setName(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);


        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setAddress(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);


        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setPhoneNumber(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);


        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setActive(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);


        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTenants() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList
        restTenantMockMvc.perform(get("/api/tenants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get the tenant
        restTenantMockMvc.perform(get("/api/tenants/{id}", tenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getTenantsByIdFiltering() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        Long id = tenant.getId();

        defaultTenantShouldBeFound("id.equals=" + id);
        defaultTenantShouldNotBeFound("id.notEquals=" + id);

        defaultTenantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTenantShouldNotBeFound("id.greaterThan=" + id);

        defaultTenantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTenantShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTenantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name equals to DEFAULT_NAME
        defaultTenantShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tenantList where name equals to UPDATED_NAME
        defaultTenantShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTenantsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name not equals to DEFAULT_NAME
        defaultTenantShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the tenantList where name not equals to UPDATED_NAME
        defaultTenantShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTenantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTenantShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tenantList where name equals to UPDATED_NAME
        defaultTenantShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTenantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name is not null
        defaultTenantShouldBeFound("name.specified=true");

        // Get all the tenantList where name is null
        defaultTenantShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllTenantsByNameContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name contains DEFAULT_NAME
        defaultTenantShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the tenantList where name contains UPDATED_NAME
        defaultTenantShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTenantsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name does not contain DEFAULT_NAME
        defaultTenantShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the tenantList where name does not contain UPDATED_NAME
        defaultTenantShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllTenantsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where address equals to DEFAULT_ADDRESS
        defaultTenantShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the tenantList where address equals to UPDATED_ADDRESS
        defaultTenantShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllTenantsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where address not equals to DEFAULT_ADDRESS
        defaultTenantShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the tenantList where address not equals to UPDATED_ADDRESS
        defaultTenantShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllTenantsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultTenantShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the tenantList where address equals to UPDATED_ADDRESS
        defaultTenantShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllTenantsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where address is not null
        defaultTenantShouldBeFound("address.specified=true");

        // Get all the tenantList where address is null
        defaultTenantShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllTenantsByAddressContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where address contains DEFAULT_ADDRESS
        defaultTenantShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the tenantList where address contains UPDATED_ADDRESS
        defaultTenantShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllTenantsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where address does not contain DEFAULT_ADDRESS
        defaultTenantShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the tenantList where address does not contain UPDATED_ADDRESS
        defaultTenantShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllTenantsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultTenantShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the tenantList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultTenantShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTenantsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultTenantShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the tenantList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultTenantShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTenantsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultTenantShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the tenantList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultTenantShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTenantsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where phoneNumber is not null
        defaultTenantShouldBeFound("phoneNumber.specified=true");

        // Get all the tenantList where phoneNumber is null
        defaultTenantShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllTenantsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultTenantShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the tenantList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultTenantShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTenantsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultTenantShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the tenantList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultTenantShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllTenantsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where active equals to DEFAULT_ACTIVE
        defaultTenantShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the tenantList where active equals to UPDATED_ACTIVE
        defaultTenantShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTenantsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where active not equals to DEFAULT_ACTIVE
        defaultTenantShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the tenantList where active not equals to UPDATED_ACTIVE
        defaultTenantShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTenantsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultTenantShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the tenantList where active equals to UPDATED_ACTIVE
        defaultTenantShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTenantsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where active is not null
        defaultTenantShouldBeFound("active.specified=true");

        // Get all the tenantList where active is null
        defaultTenantShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where createdAt equals to DEFAULT_CREATED_AT
        defaultTenantShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the tenantList where createdAt equals to UPDATED_CREATED_AT
        defaultTenantShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllTenantsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where createdAt not equals to DEFAULT_CREATED_AT
        defaultTenantShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the tenantList where createdAt not equals to UPDATED_CREATED_AT
        defaultTenantShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllTenantsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTenantShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the tenantList where createdAt equals to UPDATED_CREATED_AT
        defaultTenantShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllTenantsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where createdAt is not null
        defaultTenantShouldBeFound("createdAt.specified=true");

        // Get all the tenantList where createdAt is null
        defaultTenantShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultTenantShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the tenantList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTenantShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllTenantsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultTenantShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the tenantList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultTenantShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllTenantsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultTenantShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the tenantList where updatedAt equals to UPDATED_UPDATED_AT
        defaultTenantShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllTenantsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where updatedAt is not null
        defaultTenantShouldBeFound("updatedAt.specified=true");

        // Get all the tenantList where updatedAt is null
        defaultTenantShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByEmployeesIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);
        Employee employees = EmployeeResourceIT.createEntity(em);
        em.persist(employees);
        em.flush();
        tenant.addEmployees(employees);
        tenantRepository.saveAndFlush(tenant);
        Long employeesId = employees.getId();

        // Get all the tenantList where employees equals to employeesId
        defaultTenantShouldBeFound("employeesId.equals=" + employeesId);

        // Get all the tenantList where employees equals to employeesId + 1
        defaultTenantShouldNotBeFound("employeesId.equals=" + (employeesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTenantShouldBeFound(String filter) throws Exception {
        restTenantMockMvc.perform(get("/api/tenants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restTenantMockMvc.perform(get("/api/tenants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTenantShouldNotBeFound(String filter) throws Exception {
        restTenantMockMvc.perform(get("/api/tenants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTenantMockMvc.perform(get("/api/tenants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTenant() throws Exception {
        // Get the tenant
        restTenantMockMvc.perform(get("/api/tenants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();

        // Update the tenant
        Tenant updatedTenant = tenantRepository.findById(tenant.getId()).get();
        // Disconnect from session so that the updates on updatedTenant are not directly saved in db
        em.detach(updatedTenant);
        updatedTenant
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        TenantDTO tenantDTO = tenantMapper.toDto(updatedTenant);

        restTenantMockMvc.perform(put("/api/tenants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isOk());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
        Tenant testTenant = tenantList.get(tenantList.size() - 1);
        assertThat(testTenant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTenant.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testTenant.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTenant.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testTenant.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTenant.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingTenant() throws Exception {
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantMockMvc.perform(put("/api/tenants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        int databaseSizeBeforeDelete = tenantRepository.findAll().size();

        // Delete the tenant
        restTenantMockMvc.perform(delete("/api/tenants/{id}", tenant.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
