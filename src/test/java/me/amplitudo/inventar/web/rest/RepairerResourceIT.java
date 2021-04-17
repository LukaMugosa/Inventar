package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.Repairer;
import me.amplitudo.inventar.domain.EquipmentService;
import me.amplitudo.inventar.repository.RepairerRepository;
import me.amplitudo.inventar.service.RepairerService;
import me.amplitudo.inventar.service.dto.RepairerDTO;
import me.amplitudo.inventar.service.mapper.RepairerMapper;
import me.amplitudo.inventar.service.dto.RepairerCriteria;
import me.amplitudo.inventar.service.RepairerQueryService;

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
 * Integration tests for the {@link RepairerResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RepairerResourceIT {

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
    private RepairerRepository repairerRepository;

    @Autowired
    private RepairerMapper repairerMapper;

    @Autowired
    private RepairerService repairerService;

    @Autowired
    private RepairerQueryService repairerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepairerMockMvc;

    private Repairer repairer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repairer createEntity(EntityManager em) {
        Repairer repairer = new Repairer()
            .name(DEFAULT_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return repairer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repairer createUpdatedEntity(EntityManager em) {
        Repairer repairer = new Repairer()
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return repairer;
    }

    @BeforeEach
    public void initTest() {
        repairer = createEntity(em);
    }

    @Test
    @Transactional
    public void createRepairer() throws Exception {
        int databaseSizeBeforeCreate = repairerRepository.findAll().size();
        // Create the Repairer
        RepairerDTO repairerDTO = repairerMapper.toDto(repairer);
        restRepairerMockMvc.perform(post("/api/repairers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(repairerDTO)))
            .andExpect(status().isCreated());

        // Validate the Repairer in the database
        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeCreate + 1);
        Repairer testRepairer = repairerList.get(repairerList.size() - 1);
        assertThat(testRepairer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRepairer.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testRepairer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRepairer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testRepairer.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRepairer.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createRepairerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = repairerRepository.findAll().size();

        // Create the Repairer with an existing ID
        repairer.setId(1L);
        RepairerDTO repairerDTO = repairerMapper.toDto(repairer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepairerMockMvc.perform(post("/api/repairers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(repairerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Repairer in the database
        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = repairerRepository.findAll().size();
        // set the field null
        repairer.setName(null);

        // Create the Repairer, which fails.
        RepairerDTO repairerDTO = repairerMapper.toDto(repairer);


        restRepairerMockMvc.perform(post("/api/repairers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(repairerDTO)))
            .andExpect(status().isBadRequest());

        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = repairerRepository.findAll().size();
        // set the field null
        repairer.setPhoneNumber(null);

        // Create the Repairer, which fails.
        RepairerDTO repairerDTO = repairerMapper.toDto(repairer);


        restRepairerMockMvc.perform(post("/api/repairers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(repairerDTO)))
            .andExpect(status().isBadRequest());

        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = repairerRepository.findAll().size();
        // set the field null
        repairer.setEmail(null);

        // Create the Repairer, which fails.
        RepairerDTO repairerDTO = repairerMapper.toDto(repairer);


        restRepairerMockMvc.perform(post("/api/repairers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(repairerDTO)))
            .andExpect(status().isBadRequest());

        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = repairerRepository.findAll().size();
        // set the field null
        repairer.setAddress(null);

        // Create the Repairer, which fails.
        RepairerDTO repairerDTO = repairerMapper.toDto(repairer);


        restRepairerMockMvc.perform(post("/api/repairers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(repairerDTO)))
            .andExpect(status().isBadRequest());

        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRepairers() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList
        restRepairerMockMvc.perform(get("/api/repairers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getRepairer() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get the repairer
        restRepairerMockMvc.perform(get("/api/repairers/{id}", repairer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repairer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getRepairersByIdFiltering() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        Long id = repairer.getId();

        defaultRepairerShouldBeFound("id.equals=" + id);
        defaultRepairerShouldNotBeFound("id.notEquals=" + id);

        defaultRepairerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRepairerShouldNotBeFound("id.greaterThan=" + id);

        defaultRepairerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRepairerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRepairersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where name equals to DEFAULT_NAME
        defaultRepairerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the repairerList where name equals to UPDATED_NAME
        defaultRepairerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRepairersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where name not equals to DEFAULT_NAME
        defaultRepairerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the repairerList where name not equals to UPDATED_NAME
        defaultRepairerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRepairersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRepairerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the repairerList where name equals to UPDATED_NAME
        defaultRepairerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRepairersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where name is not null
        defaultRepairerShouldBeFound("name.specified=true");

        // Get all the repairerList where name is null
        defaultRepairerShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllRepairersByNameContainsSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where name contains DEFAULT_NAME
        defaultRepairerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the repairerList where name contains UPDATED_NAME
        defaultRepairerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRepairersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where name does not contain DEFAULT_NAME
        defaultRepairerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the repairerList where name does not contain UPDATED_NAME
        defaultRepairerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllRepairersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultRepairerShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the repairerList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultRepairerShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRepairersByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultRepairerShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the repairerList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultRepairerShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRepairersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultRepairerShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the repairerList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultRepairerShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRepairersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where phoneNumber is not null
        defaultRepairerShouldBeFound("phoneNumber.specified=true");

        // Get all the repairerList where phoneNumber is null
        defaultRepairerShouldNotBeFound("phoneNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllRepairersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultRepairerShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the repairerList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultRepairerShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllRepairersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultRepairerShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the repairerList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultRepairerShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllRepairersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where email equals to DEFAULT_EMAIL
        defaultRepairerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the repairerList where email equals to UPDATED_EMAIL
        defaultRepairerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRepairersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where email not equals to DEFAULT_EMAIL
        defaultRepairerShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the repairerList where email not equals to UPDATED_EMAIL
        defaultRepairerShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRepairersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultRepairerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the repairerList where email equals to UPDATED_EMAIL
        defaultRepairerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRepairersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where email is not null
        defaultRepairerShouldBeFound("email.specified=true");

        // Get all the repairerList where email is null
        defaultRepairerShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllRepairersByEmailContainsSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where email contains DEFAULT_EMAIL
        defaultRepairerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the repairerList where email contains UPDATED_EMAIL
        defaultRepairerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRepairersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where email does not contain DEFAULT_EMAIL
        defaultRepairerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the repairerList where email does not contain UPDATED_EMAIL
        defaultRepairerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllRepairersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where address equals to DEFAULT_ADDRESS
        defaultRepairerShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the repairerList where address equals to UPDATED_ADDRESS
        defaultRepairerShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllRepairersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where address not equals to DEFAULT_ADDRESS
        defaultRepairerShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the repairerList where address not equals to UPDATED_ADDRESS
        defaultRepairerShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllRepairersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultRepairerShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the repairerList where address equals to UPDATED_ADDRESS
        defaultRepairerShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllRepairersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where address is not null
        defaultRepairerShouldBeFound("address.specified=true");

        // Get all the repairerList where address is null
        defaultRepairerShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllRepairersByAddressContainsSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where address contains DEFAULT_ADDRESS
        defaultRepairerShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the repairerList where address contains UPDATED_ADDRESS
        defaultRepairerShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllRepairersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where address does not contain DEFAULT_ADDRESS
        defaultRepairerShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the repairerList where address does not contain UPDATED_ADDRESS
        defaultRepairerShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllRepairersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where createdAt equals to DEFAULT_CREATED_AT
        defaultRepairerShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the repairerList where createdAt equals to UPDATED_CREATED_AT
        defaultRepairerShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllRepairersByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where createdAt not equals to DEFAULT_CREATED_AT
        defaultRepairerShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the repairerList where createdAt not equals to UPDATED_CREATED_AT
        defaultRepairerShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllRepairersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultRepairerShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the repairerList where createdAt equals to UPDATED_CREATED_AT
        defaultRepairerShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllRepairersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where createdAt is not null
        defaultRepairerShouldBeFound("createdAt.specified=true");

        // Get all the repairerList where createdAt is null
        defaultRepairerShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllRepairersByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultRepairerShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the repairerList where updatedAt equals to UPDATED_UPDATED_AT
        defaultRepairerShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllRepairersByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultRepairerShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the repairerList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultRepairerShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllRepairersByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultRepairerShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the repairerList where updatedAt equals to UPDATED_UPDATED_AT
        defaultRepairerShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllRepairersByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        // Get all the repairerList where updatedAt is not null
        defaultRepairerShouldBeFound("updatedAt.specified=true");

        // Get all the repairerList where updatedAt is null
        defaultRepairerShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllRepairersByEquipmentServiceIsEqualToSomething() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);
        EquipmentService equipmentService = EquipmentServiceResourceIT.createEntity(em);
        em.persist(equipmentService);
        em.flush();
        repairer.addEquipmentService(equipmentService);
        repairerRepository.saveAndFlush(repairer);
        Long equipmentServiceId = equipmentService.getId();

        // Get all the repairerList where equipmentService equals to equipmentServiceId
        defaultRepairerShouldBeFound("equipmentServiceId.equals=" + equipmentServiceId);

        // Get all the repairerList where equipmentService equals to equipmentServiceId + 1
        defaultRepairerShouldNotBeFound("equipmentServiceId.equals=" + (equipmentServiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRepairerShouldBeFound(String filter) throws Exception {
        restRepairerMockMvc.perform(get("/api/repairers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repairer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restRepairerMockMvc.perform(get("/api/repairers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRepairerShouldNotBeFound(String filter) throws Exception {
        restRepairerMockMvc.perform(get("/api/repairers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRepairerMockMvc.perform(get("/api/repairers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRepairer() throws Exception {
        // Get the repairer
        restRepairerMockMvc.perform(get("/api/repairers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRepairer() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        int databaseSizeBeforeUpdate = repairerRepository.findAll().size();

        // Update the repairer
        Repairer updatedRepairer = repairerRepository.findById(repairer.getId()).get();
        // Disconnect from session so that the updates on updatedRepairer are not directly saved in db
        em.detach(updatedRepairer);
        updatedRepairer
            .name(UPDATED_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        RepairerDTO repairerDTO = repairerMapper.toDto(updatedRepairer);

        restRepairerMockMvc.perform(put("/api/repairers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(repairerDTO)))
            .andExpect(status().isOk());

        // Validate the Repairer in the database
        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeUpdate);
        Repairer testRepairer = repairerList.get(repairerList.size() - 1);
        assertThat(testRepairer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepairer.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testRepairer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRepairer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testRepairer.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRepairer.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingRepairer() throws Exception {
        int databaseSizeBeforeUpdate = repairerRepository.findAll().size();

        // Create the Repairer
        RepairerDTO repairerDTO = repairerMapper.toDto(repairer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepairerMockMvc.perform(put("/api/repairers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(repairerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Repairer in the database
        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRepairer() throws Exception {
        // Initialize the database
        repairerRepository.saveAndFlush(repairer);

        int databaseSizeBeforeDelete = repairerRepository.findAll().size();

        // Delete the repairer
        restRepairerMockMvc.perform(delete("/api/repairers/{id}", repairer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Repairer> repairerList = repairerRepository.findAll();
        assertThat(repairerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
