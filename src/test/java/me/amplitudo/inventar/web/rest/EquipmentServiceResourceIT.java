package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.EquipmentService;
import me.amplitudo.inventar.domain.Employee;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.domain.Repairer;
import me.amplitudo.inventar.repository.EquipmentServiceRepository;
import me.amplitudo.inventar.service.EquipmentServiceService;
import me.amplitudo.inventar.service.dto.EquipmentServiceDTO;
import me.amplitudo.inventar.service.mapper.EquipmentServiceMapper;
import me.amplitudo.inventar.service.dto.EquipmentServiceCriteria;
import me.amplitudo.inventar.service.EquipmentServiceQueryService;

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
 * Integration tests for the {@link EquipmentServiceResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EquipmentServiceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_SENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_SENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ETA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ETA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EquipmentServiceRepository equipmentServiceRepository;

    @Autowired
    private EquipmentServiceMapper equipmentServiceMapper;

    @Autowired
    private EquipmentServiceService equipmentServiceService;

    @Autowired
    private EquipmentServiceQueryService equipmentServiceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentServiceMockMvc;

    private EquipmentService equipmentService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentService createEntity(EntityManager em) {
        EquipmentService equipmentService = new EquipmentService()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .dateSent(DEFAULT_DATE_SENT)
            .eta(DEFAULT_ETA);
        return equipmentService;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentService createUpdatedEntity(EntityManager em) {
        EquipmentService equipmentService = new EquipmentService()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .dateSent(UPDATED_DATE_SENT)
            .eta(UPDATED_ETA);
        return equipmentService;
    }

    @BeforeEach
    public void initTest() {
        equipmentService = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentService() throws Exception {
        int databaseSizeBeforeCreate = equipmentServiceRepository.findAll().size();
        // Create the EquipmentService
        EquipmentServiceDTO equipmentServiceDTO = equipmentServiceMapper.toDto(equipmentService);
        restEquipmentServiceMockMvc.perform(post("/api/equipment-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServiceDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentService in the database
        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentService testEquipmentService = equipmentServiceList.get(equipmentServiceList.size() - 1);
        assertThat(testEquipmentService.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEquipmentService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipmentService.getDateSent()).isEqualTo(DEFAULT_DATE_SENT);
        assertThat(testEquipmentService.getEta()).isEqualTo(DEFAULT_ETA);
    }

    @Test
    @Transactional
    public void createEquipmentServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentServiceRepository.findAll().size();

        // Create the EquipmentService with an existing ID
        equipmentService.setId(1L);
        EquipmentServiceDTO equipmentServiceDTO = equipmentServiceMapper.toDto(equipmentService);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentServiceMockMvc.perform(post("/api/equipment-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentService in the database
        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentServiceRepository.findAll().size();
        // set the field null
        equipmentService.setTitle(null);

        // Create the EquipmentService, which fails.
        EquipmentServiceDTO equipmentServiceDTO = equipmentServiceMapper.toDto(equipmentService);


        restEquipmentServiceMockMvc.perform(post("/api/equipment-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServiceDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentServiceRepository.findAll().size();
        // set the field null
        equipmentService.setDescription(null);

        // Create the EquipmentService, which fails.
        EquipmentServiceDTO equipmentServiceDTO = equipmentServiceMapper.toDto(equipmentService);


        restEquipmentServiceMockMvc.perform(post("/api/equipment-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServiceDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateSentIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentServiceRepository.findAll().size();
        // set the field null
        equipmentService.setDateSent(null);

        // Create the EquipmentService, which fails.
        EquipmentServiceDTO equipmentServiceDTO = equipmentServiceMapper.toDto(equipmentService);


        restEquipmentServiceMockMvc.perform(post("/api/equipment-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServiceDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEtaIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentServiceRepository.findAll().size();
        // set the field null
        equipmentService.setEta(null);

        // Create the EquipmentService, which fails.
        EquipmentServiceDTO equipmentServiceDTO = equipmentServiceMapper.toDto(equipmentService);


        restEquipmentServiceMockMvc.perform(post("/api/equipment-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServiceDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipmentServices() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList
        restEquipmentServiceMockMvc.perform(get("/api/equipment-services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentService.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateSent").value(hasItem(DEFAULT_DATE_SENT.toString())))
            .andExpect(jsonPath("$.[*].eta").value(hasItem(DEFAULT_ETA.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipmentService() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get the equipmentService
        restEquipmentServiceMockMvc.perform(get("/api/equipment-services/{id}", equipmentService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentService.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateSent").value(DEFAULT_DATE_SENT.toString()))
            .andExpect(jsonPath("$.eta").value(DEFAULT_ETA.toString()));
    }


    @Test
    @Transactional
    public void getEquipmentServicesByIdFiltering() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        Long id = equipmentService.getId();

        defaultEquipmentServiceShouldBeFound("id.equals=" + id);
        defaultEquipmentServiceShouldNotBeFound("id.notEquals=" + id);

        defaultEquipmentServiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipmentServiceShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipmentServiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipmentServiceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEquipmentServicesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where title equals to DEFAULT_TITLE
        defaultEquipmentServiceShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the equipmentServiceList where title equals to UPDATED_TITLE
        defaultEquipmentServiceShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where title not equals to DEFAULT_TITLE
        defaultEquipmentServiceShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the equipmentServiceList where title not equals to UPDATED_TITLE
        defaultEquipmentServiceShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEquipmentServiceShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the equipmentServiceList where title equals to UPDATED_TITLE
        defaultEquipmentServiceShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where title is not null
        defaultEquipmentServiceShouldBeFound("title.specified=true");

        // Get all the equipmentServiceList where title is null
        defaultEquipmentServiceShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentServicesByTitleContainsSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where title contains DEFAULT_TITLE
        defaultEquipmentServiceShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the equipmentServiceList where title contains UPDATED_TITLE
        defaultEquipmentServiceShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where title does not contain DEFAULT_TITLE
        defaultEquipmentServiceShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the equipmentServiceList where title does not contain UPDATED_TITLE
        defaultEquipmentServiceShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllEquipmentServicesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where description equals to DEFAULT_DESCRIPTION
        defaultEquipmentServiceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentServiceList where description equals to UPDATED_DESCRIPTION
        defaultEquipmentServiceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where description not equals to DEFAULT_DESCRIPTION
        defaultEquipmentServiceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentServiceList where description not equals to UPDATED_DESCRIPTION
        defaultEquipmentServiceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEquipmentServiceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the equipmentServiceList where description equals to UPDATED_DESCRIPTION
        defaultEquipmentServiceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where description is not null
        defaultEquipmentServiceShouldBeFound("description.specified=true");

        // Get all the equipmentServiceList where description is null
        defaultEquipmentServiceShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentServicesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where description contains DEFAULT_DESCRIPTION
        defaultEquipmentServiceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentServiceList where description contains UPDATED_DESCRIPTION
        defaultEquipmentServiceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where description does not contain DEFAULT_DESCRIPTION
        defaultEquipmentServiceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentServiceList where description does not contain UPDATED_DESCRIPTION
        defaultEquipmentServiceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEquipmentServicesByDateSentIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where dateSent equals to DEFAULT_DATE_SENT
        defaultEquipmentServiceShouldBeFound("dateSent.equals=" + DEFAULT_DATE_SENT);

        // Get all the equipmentServiceList where dateSent equals to UPDATED_DATE_SENT
        defaultEquipmentServiceShouldNotBeFound("dateSent.equals=" + UPDATED_DATE_SENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByDateSentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where dateSent not equals to DEFAULT_DATE_SENT
        defaultEquipmentServiceShouldNotBeFound("dateSent.notEquals=" + DEFAULT_DATE_SENT);

        // Get all the equipmentServiceList where dateSent not equals to UPDATED_DATE_SENT
        defaultEquipmentServiceShouldBeFound("dateSent.notEquals=" + UPDATED_DATE_SENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByDateSentIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where dateSent in DEFAULT_DATE_SENT or UPDATED_DATE_SENT
        defaultEquipmentServiceShouldBeFound("dateSent.in=" + DEFAULT_DATE_SENT + "," + UPDATED_DATE_SENT);

        // Get all the equipmentServiceList where dateSent equals to UPDATED_DATE_SENT
        defaultEquipmentServiceShouldNotBeFound("dateSent.in=" + UPDATED_DATE_SENT);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByDateSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where dateSent is not null
        defaultEquipmentServiceShouldBeFound("dateSent.specified=true");

        // Get all the equipmentServiceList where dateSent is null
        defaultEquipmentServiceShouldNotBeFound("dateSent.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByEtaIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where eta equals to DEFAULT_ETA
        defaultEquipmentServiceShouldBeFound("eta.equals=" + DEFAULT_ETA);

        // Get all the equipmentServiceList where eta equals to UPDATED_ETA
        defaultEquipmentServiceShouldNotBeFound("eta.equals=" + UPDATED_ETA);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByEtaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where eta not equals to DEFAULT_ETA
        defaultEquipmentServiceShouldNotBeFound("eta.notEquals=" + DEFAULT_ETA);

        // Get all the equipmentServiceList where eta not equals to UPDATED_ETA
        defaultEquipmentServiceShouldBeFound("eta.notEquals=" + UPDATED_ETA);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByEtaIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where eta in DEFAULT_ETA or UPDATED_ETA
        defaultEquipmentServiceShouldBeFound("eta.in=" + DEFAULT_ETA + "," + UPDATED_ETA);

        // Get all the equipmentServiceList where eta equals to UPDATED_ETA
        defaultEquipmentServiceShouldNotBeFound("eta.in=" + UPDATED_ETA);
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByEtaIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        // Get all the equipmentServiceList where eta is not null
        defaultEquipmentServiceShouldBeFound("eta.specified=true");

        // Get all the equipmentServiceList where eta is null
        defaultEquipmentServiceShouldNotBeFound("eta.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentServicesByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        equipmentService.setEmployee(employee);
        equipmentServiceRepository.saveAndFlush(equipmentService);
        Long employeeId = employee.getId();

        // Get all the equipmentServiceList where employee equals to employeeId
        defaultEquipmentServiceShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the equipmentServiceList where employee equals to employeeId + 1
        defaultEquipmentServiceShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentServicesByEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);
        Equipment equipment = EquipmentResourceIT.createEntity(em);
        em.persist(equipment);
        em.flush();
        equipmentService.setEquipment(equipment);
        equipmentServiceRepository.saveAndFlush(equipmentService);
        Long equipmentId = equipment.getId();

        // Get all the equipmentServiceList where equipment equals to equipmentId
        defaultEquipmentServiceShouldBeFound("equipmentId.equals=" + equipmentId);

        // Get all the equipmentServiceList where equipment equals to equipmentId + 1
        defaultEquipmentServiceShouldNotBeFound("equipmentId.equals=" + (equipmentId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentServicesByRepairerIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);
        Repairer repairer = RepairerResourceIT.createEntity(em);
        em.persist(repairer);
        em.flush();
        equipmentService.setRepairer(repairer);
        equipmentServiceRepository.saveAndFlush(equipmentService);
        Long repairerId = repairer.getId();

        // Get all the equipmentServiceList where repairer equals to repairerId
        defaultEquipmentServiceShouldBeFound("repairerId.equals=" + repairerId);

        // Get all the equipmentServiceList where repairer equals to repairerId + 1
        defaultEquipmentServiceShouldNotBeFound("repairerId.equals=" + (repairerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentServiceShouldBeFound(String filter) throws Exception {
        restEquipmentServiceMockMvc.perform(get("/api/equipment-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentService.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateSent").value(hasItem(DEFAULT_DATE_SENT.toString())))
            .andExpect(jsonPath("$.[*].eta").value(hasItem(DEFAULT_ETA.toString())));

        // Check, that the count call also returns 1
        restEquipmentServiceMockMvc.perform(get("/api/equipment-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentServiceShouldNotBeFound(String filter) throws Exception {
        restEquipmentServiceMockMvc.perform(get("/api/equipment-services?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentServiceMockMvc.perform(get("/api/equipment-services/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentService() throws Exception {
        // Get the equipmentService
        restEquipmentServiceMockMvc.perform(get("/api/equipment-services/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentService() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        int databaseSizeBeforeUpdate = equipmentServiceRepository.findAll().size();

        // Update the equipmentService
        EquipmentService updatedEquipmentService = equipmentServiceRepository.findById(equipmentService.getId()).get();
        // Disconnect from session so that the updates on updatedEquipmentService are not directly saved in db
        em.detach(updatedEquipmentService);
        updatedEquipmentService
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .dateSent(UPDATED_DATE_SENT)
            .eta(UPDATED_ETA);
        EquipmentServiceDTO equipmentServiceDTO = equipmentServiceMapper.toDto(updatedEquipmentService);

        restEquipmentServiceMockMvc.perform(put("/api/equipment-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServiceDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentService in the database
        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeUpdate);
        EquipmentService testEquipmentService = equipmentServiceList.get(equipmentServiceList.size() - 1);
        assertThat(testEquipmentService.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEquipmentService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipmentService.getDateSent()).isEqualTo(UPDATED_DATE_SENT);
        assertThat(testEquipmentService.getEta()).isEqualTo(UPDATED_ETA);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentService() throws Exception {
        int databaseSizeBeforeUpdate = equipmentServiceRepository.findAll().size();

        // Create the EquipmentService
        EquipmentServiceDTO equipmentServiceDTO = equipmentServiceMapper.toDto(equipmentService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentServiceMockMvc.perform(put("/api/equipment-services")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentService in the database
        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipmentService() throws Exception {
        // Initialize the database
        equipmentServiceRepository.saveAndFlush(equipmentService);

        int databaseSizeBeforeDelete = equipmentServiceRepository.findAll().size();

        // Delete the equipmentService
        restEquipmentServiceMockMvc.perform(delete("/api/equipment-services/{id}", equipmentService.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipmentService> equipmentServiceList = equipmentServiceRepository.findAll();
        assertThat(equipmentServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
