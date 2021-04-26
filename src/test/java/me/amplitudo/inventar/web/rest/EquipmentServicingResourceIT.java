package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.EquipmentServicing;
import me.amplitudo.inventar.domain.Employee;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.domain.Repairer;
import me.amplitudo.inventar.repository.EquipmentServicingRepository;
import me.amplitudo.inventar.service.EquipmentServicingService;
import me.amplitudo.inventar.service.dto.EquipmentServicingDTO;
import me.amplitudo.inventar.service.mapper.EquipmentServicingMapper;
import me.amplitudo.inventar.service.dto.EquipmentServicingCriteria;
import me.amplitudo.inventar.service.EquipmentServicingQueryService;

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
 * Integration tests for the {@link EquipmentServicingResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EquipmentServicingResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_SENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_SENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ETA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ETA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EquipmentServicingRepository equipmentServicingRepository;

    @Autowired
    private EquipmentServicingMapper equipmentServicingMapper;

    @Autowired
    private EquipmentServicingService equipmentServicingService;

    @Autowired
    private EquipmentServicingQueryService equipmentServicingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentServicingMockMvc;

    private EquipmentServicing equipmentServicing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentServicing createEntity(EntityManager em) {
        EquipmentServicing equipmentServicing = new EquipmentServicing()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .dateSent(DEFAULT_DATE_SENT)
            .eta(DEFAULT_ETA);
        return equipmentServicing;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentServicing createUpdatedEntity(EntityManager em) {
        EquipmentServicing equipmentServicing = new EquipmentServicing()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .dateSent(UPDATED_DATE_SENT)
            .eta(UPDATED_ETA);
        return equipmentServicing;
    }

    @BeforeEach
    public void initTest() {
        equipmentServicing = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentServicing() throws Exception {
        int databaseSizeBeforeCreate = equipmentServicingRepository.findAll().size();
        // Create the EquipmentServicing
        EquipmentServicingDTO equipmentServicingDTO = equipmentServicingMapper.toDto(equipmentServicing);
        restEquipmentServicingMockMvc.perform(post("/api/equipment-servicings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServicingDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentServicing in the database
        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentServicing testEquipmentServicing = equipmentServicingList.get(equipmentServicingList.size() - 1);
        assertThat(testEquipmentServicing.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEquipmentServicing.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipmentServicing.getDateSent()).isEqualTo(DEFAULT_DATE_SENT);
        assertThat(testEquipmentServicing.getEta()).isEqualTo(DEFAULT_ETA);
    }

    @Test
    @Transactional
    public void createEquipmentServicingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentServicingRepository.findAll().size();

        // Create the EquipmentServicing with an existing ID
        equipmentServicing.setId(1L);
        EquipmentServicingDTO equipmentServicingDTO = equipmentServicingMapper.toDto(equipmentServicing);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentServicingMockMvc.perform(post("/api/equipment-servicings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServicingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentServicing in the database
        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentServicingRepository.findAll().size();
        // set the field null
        equipmentServicing.setTitle(null);

        // Create the EquipmentServicing, which fails.
        EquipmentServicingDTO equipmentServicingDTO = equipmentServicingMapper.toDto(equipmentServicing);


        restEquipmentServicingMockMvc.perform(post("/api/equipment-servicings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServicingDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentServicingRepository.findAll().size();
        // set the field null
        equipmentServicing.setDescription(null);

        // Create the EquipmentServicing, which fails.
        EquipmentServicingDTO equipmentServicingDTO = equipmentServicingMapper.toDto(equipmentServicing);


        restEquipmentServicingMockMvc.perform(post("/api/equipment-servicings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServicingDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateSentIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentServicingRepository.findAll().size();
        // set the field null
        equipmentServicing.setDateSent(null);

        // Create the EquipmentServicing, which fails.
        EquipmentServicingDTO equipmentServicingDTO = equipmentServicingMapper.toDto(equipmentServicing);


        restEquipmentServicingMockMvc.perform(post("/api/equipment-servicings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServicingDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEtaIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentServicingRepository.findAll().size();
        // set the field null
        equipmentServicing.setEta(null);

        // Create the EquipmentServicing, which fails.
        EquipmentServicingDTO equipmentServicingDTO = equipmentServicingMapper.toDto(equipmentServicing);


        restEquipmentServicingMockMvc.perform(post("/api/equipment-servicings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServicingDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicings() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList
        restEquipmentServicingMockMvc.perform(get("/api/equipment-servicings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentServicing.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateSent").value(hasItem(DEFAULT_DATE_SENT.toString())))
            .andExpect(jsonPath("$.[*].eta").value(hasItem(DEFAULT_ETA.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipmentServicing() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get the equipmentServicing
        restEquipmentServicingMockMvc.perform(get("/api/equipment-servicings/{id}", equipmentServicing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentServicing.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateSent").value(DEFAULT_DATE_SENT.toString()))
            .andExpect(jsonPath("$.eta").value(DEFAULT_ETA.toString()));
    }


    @Test
    @Transactional
    public void getEquipmentServicingsByIdFiltering() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        Long id = equipmentServicing.getId();

        defaultEquipmentServicingShouldBeFound("id.equals=" + id);
        defaultEquipmentServicingShouldNotBeFound("id.notEquals=" + id);

        defaultEquipmentServicingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipmentServicingShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipmentServicingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipmentServicingShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEquipmentServicingsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where title equals to DEFAULT_TITLE
        defaultEquipmentServicingShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the equipmentServicingList where title equals to UPDATED_TITLE
        defaultEquipmentServicingShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where title not equals to DEFAULT_TITLE
        defaultEquipmentServicingShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the equipmentServicingList where title not equals to UPDATED_TITLE
        defaultEquipmentServicingShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEquipmentServicingShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the equipmentServicingList where title equals to UPDATED_TITLE
        defaultEquipmentServicingShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where title is not null
        defaultEquipmentServicingShouldBeFound("title.specified=true");

        // Get all the equipmentServicingList where title is null
        defaultEquipmentServicingShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentServicingsByTitleContainsSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where title contains DEFAULT_TITLE
        defaultEquipmentServicingShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the equipmentServicingList where title contains UPDATED_TITLE
        defaultEquipmentServicingShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where title does not contain DEFAULT_TITLE
        defaultEquipmentServicingShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the equipmentServicingList where title does not contain UPDATED_TITLE
        defaultEquipmentServicingShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllEquipmentServicingsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where description equals to DEFAULT_DESCRIPTION
        defaultEquipmentServicingShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentServicingList where description equals to UPDATED_DESCRIPTION
        defaultEquipmentServicingShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where description not equals to DEFAULT_DESCRIPTION
        defaultEquipmentServicingShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentServicingList where description not equals to UPDATED_DESCRIPTION
        defaultEquipmentServicingShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEquipmentServicingShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the equipmentServicingList where description equals to UPDATED_DESCRIPTION
        defaultEquipmentServicingShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where description is not null
        defaultEquipmentServicingShouldBeFound("description.specified=true");

        // Get all the equipmentServicingList where description is null
        defaultEquipmentServicingShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentServicingsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where description contains DEFAULT_DESCRIPTION
        defaultEquipmentServicingShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentServicingList where description contains UPDATED_DESCRIPTION
        defaultEquipmentServicingShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where description does not contain DEFAULT_DESCRIPTION
        defaultEquipmentServicingShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentServicingList where description does not contain UPDATED_DESCRIPTION
        defaultEquipmentServicingShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEquipmentServicingsByDateSentIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where dateSent equals to DEFAULT_DATE_SENT
        defaultEquipmentServicingShouldBeFound("dateSent.equals=" + DEFAULT_DATE_SENT);

        // Get all the equipmentServicingList where dateSent equals to UPDATED_DATE_SENT
        defaultEquipmentServicingShouldNotBeFound("dateSent.equals=" + UPDATED_DATE_SENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByDateSentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where dateSent not equals to DEFAULT_DATE_SENT
        defaultEquipmentServicingShouldNotBeFound("dateSent.notEquals=" + DEFAULT_DATE_SENT);

        // Get all the equipmentServicingList where dateSent not equals to UPDATED_DATE_SENT
        defaultEquipmentServicingShouldBeFound("dateSent.notEquals=" + UPDATED_DATE_SENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByDateSentIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where dateSent in DEFAULT_DATE_SENT or UPDATED_DATE_SENT
        defaultEquipmentServicingShouldBeFound("dateSent.in=" + DEFAULT_DATE_SENT + "," + UPDATED_DATE_SENT);

        // Get all the equipmentServicingList where dateSent equals to UPDATED_DATE_SENT
        defaultEquipmentServicingShouldNotBeFound("dateSent.in=" + UPDATED_DATE_SENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByDateSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where dateSent is not null
        defaultEquipmentServicingShouldBeFound("dateSent.specified=true");

        // Get all the equipmentServicingList where dateSent is null
        defaultEquipmentServicingShouldNotBeFound("dateSent.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByEtaIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where eta equals to DEFAULT_ETA
        defaultEquipmentServicingShouldBeFound("eta.equals=" + DEFAULT_ETA);

        // Get all the equipmentServicingList where eta equals to UPDATED_ETA
        defaultEquipmentServicingShouldNotBeFound("eta.equals=" + UPDATED_ETA);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByEtaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where eta not equals to DEFAULT_ETA
        defaultEquipmentServicingShouldNotBeFound("eta.notEquals=" + DEFAULT_ETA);

        // Get all the equipmentServicingList where eta not equals to UPDATED_ETA
        defaultEquipmentServicingShouldBeFound("eta.notEquals=" + UPDATED_ETA);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByEtaIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where eta in DEFAULT_ETA or UPDATED_ETA
        defaultEquipmentServicingShouldBeFound("eta.in=" + DEFAULT_ETA + "," + UPDATED_ETA);

        // Get all the equipmentServicingList where eta equals to UPDATED_ETA
        defaultEquipmentServicingShouldNotBeFound("eta.in=" + UPDATED_ETA);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByEtaIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        // Get all the equipmentServicingList where eta is not null
        defaultEquipmentServicingShouldBeFound("eta.specified=true");

        // Get all the equipmentServicingList where eta is null
        defaultEquipmentServicingShouldNotBeFound("eta.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentServicingsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        equipmentServicing.setEmployee(employee);
        equipmentServicingRepository.saveAndFlush(equipmentServicing);
        Long employeeId = employee.getId();

        // Get all the equipmentServicingList where employee equals to employeeId
        defaultEquipmentServicingShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the equipmentServicingList where employee equals to employeeId + 1
        defaultEquipmentServicingShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentServicingsByEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);
        Equipment equipment = EquipmentResourceIT.createEntity(em);
        em.persist(equipment);
        em.flush();
        equipmentServicing.setEquipment(equipment);
        equipmentServicingRepository.saveAndFlush(equipmentServicing);
        Long equipmentId = equipment.getId();

        // Get all the equipmentServicingList where equipment equals to equipmentId
        defaultEquipmentServicingShouldBeFound("equipmentId.equals=" + equipmentId);

        // Get all the equipmentServicingList where equipment equals to equipmentId + 1
        defaultEquipmentServicingShouldNotBeFound("equipmentId.equals=" + (equipmentId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentServicingsByRepairerIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);
        Repairer repairer = RepairerResourceIT.createEntity(em);
        em.persist(repairer);
        em.flush();
        equipmentServicing.setRepairer(repairer);
        equipmentServicingRepository.saveAndFlush(equipmentServicing);
        Long repairerId = repairer.getId();

        // Get all the equipmentServicingList where repairer equals to repairerId
        defaultEquipmentServicingShouldBeFound("repairerId.equals=" + repairerId);

        // Get all the equipmentServicingList where repairer equals to repairerId + 1
        defaultEquipmentServicingShouldNotBeFound("repairerId.equals=" + (repairerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentServicingShouldBeFound(String filter) throws Exception {
        restEquipmentServicingMockMvc.perform(get("/api/equipment-servicings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentServicing.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateSent").value(hasItem(DEFAULT_DATE_SENT.toString())))
            .andExpect(jsonPath("$.[*].eta").value(hasItem(DEFAULT_ETA.toString())));

        // Check, that the count call also returns 1
        restEquipmentServicingMockMvc.perform(get("/api/equipment-servicings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentServicingShouldNotBeFound(String filter) throws Exception {
        restEquipmentServicingMockMvc.perform(get("/api/equipment-servicings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentServicingMockMvc.perform(get("/api/equipment-servicings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentServicing() throws Exception {
        // Get the equipmentServicing
        restEquipmentServicingMockMvc.perform(get("/api/equipment-servicings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentServicing() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        int databaseSizeBeforeUpdate = equipmentServicingRepository.findAll().size();

        // Update the equipmentServicing
        EquipmentServicing updatedEquipmentServicing = equipmentServicingRepository.findById(equipmentServicing.getId()).get();
        // Disconnect from session so that the updates on updatedEquipmentServicing are not directly saved in db
        em.detach(updatedEquipmentServicing);
        updatedEquipmentServicing
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .dateSent(UPDATED_DATE_SENT)
            .eta(UPDATED_ETA);
        EquipmentServicingDTO equipmentServicingDTO = equipmentServicingMapper.toDto(updatedEquipmentServicing);

        restEquipmentServicingMockMvc.perform(put("/api/equipment-servicings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServicingDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentServicing in the database
        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeUpdate);
        EquipmentServicing testEquipmentServicing = equipmentServicingList.get(equipmentServicingList.size() - 1);
        assertThat(testEquipmentServicing.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEquipmentServicing.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipmentServicing.getDateSent()).isEqualTo(UPDATED_DATE_SENT);
        assertThat(testEquipmentServicing.getEta()).isEqualTo(UPDATED_ETA);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentServicing() throws Exception {
        int databaseSizeBeforeUpdate = equipmentServicingRepository.findAll().size();

        // Create the EquipmentServicing
        EquipmentServicingDTO equipmentServicingDTO = equipmentServicingMapper.toDto(equipmentServicing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentServicingMockMvc.perform(put("/api/equipment-servicings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServicingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentServicing in the database
        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipmentServicing() throws Exception {
        // Initialize the database
        equipmentServicingRepository.saveAndFlush(equipmentServicing);

        int databaseSizeBeforeDelete = equipmentServicingRepository.findAll().size();

        // Delete the equipmentServicing
        restEquipmentServicingMockMvc.perform(delete("/api/equipment-servicings/{id}", equipmentServicing.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipmentServicing> equipmentServicingList = equipmentServicingRepository.findAll();
        assertThat(equipmentServicingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
