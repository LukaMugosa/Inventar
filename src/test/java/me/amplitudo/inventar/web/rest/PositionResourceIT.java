package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.Position;
import me.amplitudo.inventar.domain.Employee;
import me.amplitudo.inventar.domain.Sector;
import me.amplitudo.inventar.repository.PositionRepository;
import me.amplitudo.inventar.service.PositionService;
import me.amplitudo.inventar.service.dto.PositionDTO;
import me.amplitudo.inventar.service.mapper.PositionMapper;
import me.amplitudo.inventar.service.dto.PositionCriteria;
import me.amplitudo.inventar.service.PositionQueryService;

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
 * Integration tests for the {@link PositionResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PositionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private PositionService positionService;

    @Autowired
    private PositionQueryService positionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionMockMvc;

    private Position position;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createEntity(EntityManager em) {
        Position position = new Position()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return position;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createUpdatedEntity(EntityManager em) {
        Position position = new Position()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return position;
    }

    @BeforeEach
    public void initTest() {
        position = createEntity(em);
    }

    @Test
    @Transactional
    public void createPosition() throws Exception {
        int databaseSizeBeforeCreate = positionRepository.findAll().size();
        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);
        restPositionMockMvc.perform(post("/api/positions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isCreated());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeCreate + 1);
        Position testPosition = positionList.get(positionList.size() - 1);
        assertThat(testPosition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPosition.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPosition.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createPositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionRepository.findAll().size();

        // Create the Position with an existing ID
        position.setId(1L);
        PositionDTO positionDTO = positionMapper.toDto(position);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMockMvc.perform(post("/api/positions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionRepository.findAll().size();
        // set the field null
        position.setName(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);


        restPositionMockMvc.perform(post("/api/positions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPositions() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList
        restPositionMockMvc.perform(get("/api/positions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getPosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get the position
        restPositionMockMvc.perform(get("/api/positions/{id}", position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(position.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getPositionsByIdFiltering() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        Long id = position.getId();

        defaultPositionShouldBeFound("id.equals=" + id);
        defaultPositionShouldNotBeFound("id.notEquals=" + id);

        defaultPositionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPositionShouldNotBeFound("id.greaterThan=" + id);

        defaultPositionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPositionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPositionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where name equals to DEFAULT_NAME
        defaultPositionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the positionList where name equals to UPDATED_NAME
        defaultPositionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPositionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where name not equals to DEFAULT_NAME
        defaultPositionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the positionList where name not equals to UPDATED_NAME
        defaultPositionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPositionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPositionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the positionList where name equals to UPDATED_NAME
        defaultPositionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPositionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where name is not null
        defaultPositionShouldBeFound("name.specified=true");

        // Get all the positionList where name is null
        defaultPositionShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPositionsByNameContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where name contains DEFAULT_NAME
        defaultPositionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the positionList where name contains UPDATED_NAME
        defaultPositionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPositionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where name does not contain DEFAULT_NAME
        defaultPositionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the positionList where name does not contain UPDATED_NAME
        defaultPositionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPositionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where createdAt equals to DEFAULT_CREATED_AT
        defaultPositionShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the positionList where createdAt equals to UPDATED_CREATED_AT
        defaultPositionShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllPositionsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where createdAt not equals to DEFAULT_CREATED_AT
        defaultPositionShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the positionList where createdAt not equals to UPDATED_CREATED_AT
        defaultPositionShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllPositionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPositionShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the positionList where createdAt equals to UPDATED_CREATED_AT
        defaultPositionShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllPositionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where createdAt is not null
        defaultPositionShouldBeFound("createdAt.specified=true");

        // Get all the positionList where createdAt is null
        defaultPositionShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPositionsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultPositionShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the positionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPositionShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllPositionsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultPositionShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the positionList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultPositionShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllPositionsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultPositionShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the positionList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPositionShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllPositionsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        // Get all the positionList where updatedAt is not null
        defaultPositionShouldBeFound("updatedAt.specified=true");

        // Get all the positionList where updatedAt is null
        defaultPositionShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPositionsByEmployeesIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);
        Employee employees = EmployeeResourceIT.createEntity(em);
        em.persist(employees);
        em.flush();
        position.addEmployees(employees);
        positionRepository.saveAndFlush(position);
        Long employeesId = employees.getId();

        // Get all the positionList where employees equals to employeesId
        defaultPositionShouldBeFound("employeesId.equals=" + employeesId);

        // Get all the positionList where employees equals to employeesId + 1
        defaultPositionShouldNotBeFound("employeesId.equals=" + (employeesId + 1));
    }


    @Test
    @Transactional
    public void getAllPositionsBySectorIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);
        Sector sector = SectorResourceIT.createEntity(em);
        em.persist(sector);
        em.flush();
        position.setSector(sector);
        positionRepository.saveAndFlush(position);
        Long sectorId = sector.getId();

        // Get all the positionList where sector equals to sectorId
        defaultPositionShouldBeFound("sectorId.equals=" + sectorId);

        // Get all the positionList where sector equals to sectorId + 1
        defaultPositionShouldNotBeFound("sectorId.equals=" + (sectorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionShouldBeFound(String filter) throws Exception {
        restPositionMockMvc.perform(get("/api/positions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restPositionMockMvc.perform(get("/api/positions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionShouldNotBeFound(String filter) throws Exception {
        restPositionMockMvc.perform(get("/api/positions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionMockMvc.perform(get("/api/positions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get("/api/positions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Update the position
        Position updatedPosition = positionRepository.findById(position.getId()).get();
        // Disconnect from session so that the updates on updatedPosition are not directly saved in db
        em.detach(updatedPosition);
        updatedPosition
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PositionDTO positionDTO = positionMapper.toDto(updatedPosition);

        restPositionMockMvc.perform(put("/api/positions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isOk());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
        Position testPosition = positionList.get(positionList.size() - 1);
        assertThat(testPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPosition.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPosition.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingPosition() throws Exception {
        int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc.perform(put("/api/positions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        int databaseSizeBeforeDelete = positionRepository.findAll().size();

        // Delete the position
        restPositionMockMvc.perform(delete("/api/positions/{id}", position.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
