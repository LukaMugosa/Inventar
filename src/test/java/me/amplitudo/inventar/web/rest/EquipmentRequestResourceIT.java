package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.EquipmentRequest;
import me.amplitudo.inventar.domain.Equipment;
import me.amplitudo.inventar.domain.Employee;
import me.amplitudo.inventar.repository.EquipmentRequestRepository;
import me.amplitudo.inventar.service.EquipmentRequestService;
import me.amplitudo.inventar.service.dto.EquipmentRequestDTO;
import me.amplitudo.inventar.service.mapper.EquipmentRequestMapper;
import me.amplitudo.inventar.service.dto.EquipmentRequestCriteria;
import me.amplitudo.inventar.service.EquipmentRequestQueryService;

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

import me.amplitudo.inventar.domain.enumeration.EquipmentRequestStatus;
/**
 * Integration tests for the {@link EquipmentRequestResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EquipmentRequestResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final EquipmentRequestStatus DEFAULT_STATUS = EquipmentRequestStatus.WAITING_FOR_HR;
    private static final EquipmentRequestStatus UPDATED_STATUS = EquipmentRequestStatus.WAITING_FOR_IT;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EquipmentRequestRepository equipmentRequestRepository;

    @Autowired
    private EquipmentRequestMapper equipmentRequestMapper;

    @Autowired
    private EquipmentRequestService equipmentRequestService;

    @Autowired
    private EquipmentRequestQueryService equipmentRequestQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipmentRequestMockMvc;

    private EquipmentRequest equipmentRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentRequest createEntity(EntityManager em) {
        EquipmentRequest equipmentRequest = new EquipmentRequest()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT);
        return equipmentRequest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipmentRequest createUpdatedEntity(EntityManager em) {
        EquipmentRequest equipmentRequest = new EquipmentRequest()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT);
        return equipmentRequest;
    }

    @BeforeEach
    public void initTest() {
        equipmentRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipmentRequest() throws Exception {
        int databaseSizeBeforeCreate = equipmentRequestRepository.findAll().size();
        // Create the EquipmentRequest
        EquipmentRequestDTO equipmentRequestDTO = equipmentRequestMapper.toDto(equipmentRequest);
        restEquipmentRequestMockMvc.perform(post("/api/equipment-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the EquipmentRequest in the database
        List<EquipmentRequest> equipmentRequestList = equipmentRequestRepository.findAll();
        assertThat(equipmentRequestList).hasSize(databaseSizeBeforeCreate + 1);
        EquipmentRequest testEquipmentRequest = equipmentRequestList.get(equipmentRequestList.size() - 1);
        assertThat(testEquipmentRequest.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEquipmentRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEquipmentRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEquipmentRequest.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createEquipmentRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentRequestRepository.findAll().size();

        // Create the EquipmentRequest with an existing ID
        equipmentRequest.setId(1L);
        EquipmentRequestDTO equipmentRequestDTO = equipmentRequestMapper.toDto(equipmentRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentRequestMockMvc.perform(post("/api/equipment-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentRequest in the database
        List<EquipmentRequest> equipmentRequestList = equipmentRequestRepository.findAll();
        assertThat(equipmentRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRequestRepository.findAll().size();
        // set the field null
        equipmentRequest.setTitle(null);

        // Create the EquipmentRequest, which fails.
        EquipmentRequestDTO equipmentRequestDTO = equipmentRequestMapper.toDto(equipmentRequest);


        restEquipmentRequestMockMvc.perform(post("/api/equipment-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentRequestDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentRequest> equipmentRequestList = equipmentRequestRepository.findAll();
        assertThat(equipmentRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRequestRepository.findAll().size();
        // set the field null
        equipmentRequest.setDescription(null);

        // Create the EquipmentRequest, which fails.
        EquipmentRequestDTO equipmentRequestDTO = equipmentRequestMapper.toDto(equipmentRequest);


        restEquipmentRequestMockMvc.perform(post("/api/equipment-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentRequestDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentRequest> equipmentRequestList = equipmentRequestRepository.findAll();
        assertThat(equipmentRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRequestRepository.findAll().size();
        // set the field null
        equipmentRequest.setStatus(null);

        // Create the EquipmentRequest, which fails.
        EquipmentRequestDTO equipmentRequestDTO = equipmentRequestMapper.toDto(equipmentRequest);


        restEquipmentRequestMockMvc.perform(post("/api/equipment-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentRequestDTO)))
            .andExpect(status().isBadRequest());

        List<EquipmentRequest> equipmentRequestList = equipmentRequestRepository.findAll();
        assertThat(equipmentRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequests() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList
        restEquipmentRequestMockMvc.perform(get("/api/equipment-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getEquipmentRequest() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get the equipmentRequest
        restEquipmentRequestMockMvc.perform(get("/api/equipment-requests/{id}", equipmentRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipmentRequest.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getEquipmentRequestsByIdFiltering() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        Long id = equipmentRequest.getId();

        defaultEquipmentRequestShouldBeFound("id.equals=" + id);
        defaultEquipmentRequestShouldNotBeFound("id.notEquals=" + id);

        defaultEquipmentRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipmentRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipmentRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipmentRequestShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEquipmentRequestsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where title equals to DEFAULT_TITLE
        defaultEquipmentRequestShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the equipmentRequestList where title equals to UPDATED_TITLE
        defaultEquipmentRequestShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where title not equals to DEFAULT_TITLE
        defaultEquipmentRequestShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the equipmentRequestList where title not equals to UPDATED_TITLE
        defaultEquipmentRequestShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEquipmentRequestShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the equipmentRequestList where title equals to UPDATED_TITLE
        defaultEquipmentRequestShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where title is not null
        defaultEquipmentRequestShouldBeFound("title.specified=true");

        // Get all the equipmentRequestList where title is null
        defaultEquipmentRequestShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentRequestsByTitleContainsSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where title contains DEFAULT_TITLE
        defaultEquipmentRequestShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the equipmentRequestList where title contains UPDATED_TITLE
        defaultEquipmentRequestShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where title does not contain DEFAULT_TITLE
        defaultEquipmentRequestShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the equipmentRequestList where title does not contain UPDATED_TITLE
        defaultEquipmentRequestShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllEquipmentRequestsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where description equals to DEFAULT_DESCRIPTION
        defaultEquipmentRequestShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentRequestList where description equals to UPDATED_DESCRIPTION
        defaultEquipmentRequestShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where description not equals to DEFAULT_DESCRIPTION
        defaultEquipmentRequestShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentRequestList where description not equals to UPDATED_DESCRIPTION
        defaultEquipmentRequestShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEquipmentRequestShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the equipmentRequestList where description equals to UPDATED_DESCRIPTION
        defaultEquipmentRequestShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where description is not null
        defaultEquipmentRequestShouldBeFound("description.specified=true");

        // Get all the equipmentRequestList where description is null
        defaultEquipmentRequestShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllEquipmentRequestsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where description contains DEFAULT_DESCRIPTION
        defaultEquipmentRequestShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentRequestList where description contains UPDATED_DESCRIPTION
        defaultEquipmentRequestShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where description does not contain DEFAULT_DESCRIPTION
        defaultEquipmentRequestShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the equipmentRequestList where description does not contain UPDATED_DESCRIPTION
        defaultEquipmentRequestShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEquipmentRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where status equals to DEFAULT_STATUS
        defaultEquipmentRequestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the equipmentRequestList where status equals to UPDATED_STATUS
        defaultEquipmentRequestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where status not equals to DEFAULT_STATUS
        defaultEquipmentRequestShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the equipmentRequestList where status not equals to UPDATED_STATUS
        defaultEquipmentRequestShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultEquipmentRequestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the equipmentRequestList where status equals to UPDATED_STATUS
        defaultEquipmentRequestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where status is not null
        defaultEquipmentRequestShouldBeFound("status.specified=true");

        // Get all the equipmentRequestList where status is null
        defaultEquipmentRequestShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where createdAt equals to DEFAULT_CREATED_AT
        defaultEquipmentRequestShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the equipmentRequestList where createdAt equals to UPDATED_CREATED_AT
        defaultEquipmentRequestShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where createdAt not equals to DEFAULT_CREATED_AT
        defaultEquipmentRequestShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the equipmentRequestList where createdAt not equals to UPDATED_CREATED_AT
        defaultEquipmentRequestShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultEquipmentRequestShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the equipmentRequestList where createdAt equals to UPDATED_CREATED_AT
        defaultEquipmentRequestShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        // Get all the equipmentRequestList where createdAt is not null
        defaultEquipmentRequestShouldBeFound("createdAt.specified=true");

        // Get all the equipmentRequestList where createdAt is null
        defaultEquipmentRequestShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllEquipmentRequestsByEquipmentIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);
        Equipment equipment = EquipmentResourceIT.createEntity(em);
        em.persist(equipment);
        em.flush();
        equipmentRequest.setEquipment(equipment);
        equipmentRequestRepository.saveAndFlush(equipmentRequest);
        Long equipmentId = equipment.getId();

        // Get all the equipmentRequestList where equipment equals to equipmentId
        defaultEquipmentRequestShouldBeFound("equipmentId.equals=" + equipmentId);

        // Get all the equipmentRequestList where equipment equals to equipmentId + 1
        defaultEquipmentRequestShouldNotBeFound("equipmentId.equals=" + (equipmentId + 1));
    }


    @Test
    @Transactional
    public void getAllEquipmentRequestsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        equipmentRequest.setEmployee(employee);
        equipmentRequestRepository.saveAndFlush(equipmentRequest);
        Long employeeId = employee.getId();

        // Get all the equipmentRequestList where employee equals to employeeId
        defaultEquipmentRequestShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the equipmentRequestList where employee equals to employeeId + 1
        defaultEquipmentRequestShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipmentRequestShouldBeFound(String filter) throws Exception {
        restEquipmentRequestMockMvc.perform(get("/api/equipment-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipmentRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));

        // Check, that the count call also returns 1
        restEquipmentRequestMockMvc.perform(get("/api/equipment-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipmentRequestShouldNotBeFound(String filter) throws Exception {
        restEquipmentRequestMockMvc.perform(get("/api/equipment-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipmentRequestMockMvc.perform(get("/api/equipment-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEquipmentRequest() throws Exception {
        // Get the equipmentRequest
        restEquipmentRequestMockMvc.perform(get("/api/equipment-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipmentRequest() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        int databaseSizeBeforeUpdate = equipmentRequestRepository.findAll().size();

        // Update the equipmentRequest
        EquipmentRequest updatedEquipmentRequest = equipmentRequestRepository.findById(equipmentRequest.getId()).get();
        // Disconnect from session so that the updates on updatedEquipmentRequest are not directly saved in db
        em.detach(updatedEquipmentRequest);
        updatedEquipmentRequest
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT);
        EquipmentRequestDTO equipmentRequestDTO = equipmentRequestMapper.toDto(updatedEquipmentRequest);

        restEquipmentRequestMockMvc.perform(put("/api/equipment-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentRequestDTO)))
            .andExpect(status().isOk());

        // Validate the EquipmentRequest in the database
        List<EquipmentRequest> equipmentRequestList = equipmentRequestRepository.findAll();
        assertThat(equipmentRequestList).hasSize(databaseSizeBeforeUpdate);
        EquipmentRequest testEquipmentRequest = equipmentRequestList.get(equipmentRequestList.size() - 1);
        assertThat(testEquipmentRequest.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEquipmentRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEquipmentRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEquipmentRequest.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingEquipmentRequest() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRequestRepository.findAll().size();

        // Create the EquipmentRequest
        EquipmentRequestDTO equipmentRequestDTO = equipmentRequestMapper.toDto(equipmentRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentRequestMockMvc.perform(put("/api/equipment-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(equipmentRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipmentRequest in the database
        List<EquipmentRequest> equipmentRequestList = equipmentRequestRepository.findAll();
        assertThat(equipmentRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEquipmentRequest() throws Exception {
        // Initialize the database
        equipmentRequestRepository.saveAndFlush(equipmentRequest);

        int databaseSizeBeforeDelete = equipmentRequestRepository.findAll().size();

        // Delete the equipmentRequest
        restEquipmentRequestMockMvc.perform(delete("/api/equipment-requests/{id}", equipmentRequest.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EquipmentRequest> equipmentRequestList = equipmentRequestRepository.findAll();
        assertThat(equipmentRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
