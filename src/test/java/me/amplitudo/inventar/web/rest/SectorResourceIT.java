package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.Sector;
import me.amplitudo.inventar.domain.Position;
import me.amplitudo.inventar.repository.SectorRepository;
import me.amplitudo.inventar.service.SectorService;
import me.amplitudo.inventar.service.dto.SectorDTO;
import me.amplitudo.inventar.service.mapper.SectorMapper;
import me.amplitudo.inventar.service.dto.SectorCriteria;
import me.amplitudo.inventar.service.SectorQueryService;

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
 * Integration tests for the {@link SectorResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SectorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private SectorMapper sectorMapper;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private SectorQueryService sectorQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSectorMockMvc;

    private Sector sector;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sector createEntity(EntityManager em) {
        Sector sector = new Sector()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return sector;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sector createUpdatedEntity(EntityManager em) {
        Sector sector = new Sector()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return sector;
    }

    @BeforeEach
    public void initTest() {
        sector = createEntity(em);
    }

    @Test
    @Transactional
    public void createSector() throws Exception {
        int databaseSizeBeforeCreate = sectorRepository.findAll().size();
        // Create the Sector
        SectorDTO sectorDTO = sectorMapper.toDto(sector);
        restSectorMockMvc.perform(post("/api/sectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sectorDTO)))
            .andExpect(status().isCreated());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeCreate + 1);
        Sector testSector = sectorList.get(sectorList.size() - 1);
        assertThat(testSector.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSector.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSector.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createSectorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sectorRepository.findAll().size();

        // Create the Sector with an existing ID
        sector.setId(1L);
        SectorDTO sectorDTO = sectorMapper.toDto(sector);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectorMockMvc.perform(post("/api/sectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sectorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sectorRepository.findAll().size();
        // set the field null
        sector.setName(null);

        // Create the Sector, which fails.
        SectorDTO sectorDTO = sectorMapper.toDto(sector);


        restSectorMockMvc.perform(post("/api/sectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sectorDTO)))
            .andExpect(status().isBadRequest());

        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSectors() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList
        restSectorMockMvc.perform(get("/api/sectors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get the sector
        restSectorMockMvc.perform(get("/api/sectors/{id}", sector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sector.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getSectorsByIdFiltering() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        Long id = sector.getId();

        defaultSectorShouldBeFound("id.equals=" + id);
        defaultSectorShouldNotBeFound("id.notEquals=" + id);

        defaultSectorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSectorShouldNotBeFound("id.greaterThan=" + id);

        defaultSectorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSectorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSectorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name equals to DEFAULT_NAME
        defaultSectorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sectorList where name equals to UPDATED_NAME
        defaultSectorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSectorsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name not equals to DEFAULT_NAME
        defaultSectorShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the sectorList where name not equals to UPDATED_NAME
        defaultSectorShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSectorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSectorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sectorList where name equals to UPDATED_NAME
        defaultSectorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSectorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name is not null
        defaultSectorShouldBeFound("name.specified=true");

        // Get all the sectorList where name is null
        defaultSectorShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllSectorsByNameContainsSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name contains DEFAULT_NAME
        defaultSectorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sectorList where name contains UPDATED_NAME
        defaultSectorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSectorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where name does not contain DEFAULT_NAME
        defaultSectorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sectorList where name does not contain UPDATED_NAME
        defaultSectorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllSectorsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where createdAt equals to DEFAULT_CREATED_AT
        defaultSectorShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the sectorList where createdAt equals to UPDATED_CREATED_AT
        defaultSectorShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSectorsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where createdAt not equals to DEFAULT_CREATED_AT
        defaultSectorShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the sectorList where createdAt not equals to UPDATED_CREATED_AT
        defaultSectorShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSectorsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSectorShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the sectorList where createdAt equals to UPDATED_CREATED_AT
        defaultSectorShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSectorsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where createdAt is not null
        defaultSectorShouldBeFound("createdAt.specified=true");

        // Get all the sectorList where createdAt is null
        defaultSectorShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllSectorsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultSectorShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the sectorList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSectorShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllSectorsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultSectorShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the sectorList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultSectorShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllSectorsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultSectorShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the sectorList where updatedAt equals to UPDATED_UPDATED_AT
        defaultSectorShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllSectorsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        // Get all the sectorList where updatedAt is not null
        defaultSectorShouldBeFound("updatedAt.specified=true");

        // Get all the sectorList where updatedAt is null
        defaultSectorShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllSectorsByPositionsIsEqualToSomething() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);
        Position positions = PositionResourceIT.createEntity(em);
        em.persist(positions);
        em.flush();
        sector.addPositions(positions);
        sectorRepository.saveAndFlush(sector);
        Long positionsId = positions.getId();

        // Get all the sectorList where positions equals to positionsId
        defaultSectorShouldBeFound("positionsId.equals=" + positionsId);

        // Get all the sectorList where positions equals to positionsId + 1
        defaultSectorShouldNotBeFound("positionsId.equals=" + (positionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSectorShouldBeFound(String filter) throws Exception {
        restSectorMockMvc.perform(get("/api/sectors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restSectorMockMvc.perform(get("/api/sectors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSectorShouldNotBeFound(String filter) throws Exception {
        restSectorMockMvc.perform(get("/api/sectors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSectorMockMvc.perform(get("/api/sectors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSector() throws Exception {
        // Get the sector
        restSectorMockMvc.perform(get("/api/sectors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();

        // Update the sector
        Sector updatedSector = sectorRepository.findById(sector.getId()).get();
        // Disconnect from session so that the updates on updatedSector are not directly saved in db
        em.detach(updatedSector);
        updatedSector
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        SectorDTO sectorDTO = sectorMapper.toDto(updatedSector);

        restSectorMockMvc.perform(put("/api/sectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sectorDTO)))
            .andExpect(status().isOk());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
        Sector testSector = sectorList.get(sectorList.size() - 1);
        assertThat(testSector.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSector.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSector.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingSector() throws Exception {
        int databaseSizeBeforeUpdate = sectorRepository.findAll().size();

        // Create the Sector
        SectorDTO sectorDTO = sectorMapper.toDto(sector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectorMockMvc.perform(put("/api/sectors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sectorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sector in the database
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSector() throws Exception {
        // Initialize the database
        sectorRepository.saveAndFlush(sector);

        int databaseSizeBeforeDelete = sectorRepository.findAll().size();

        // Delete the sector
        restSectorMockMvc.perform(delete("/api/sectors/{id}", sector.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sector> sectorList = sectorRepository.findAll();
        assertThat(sectorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
