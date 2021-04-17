package me.amplitudo.inventar.web.rest;

import me.amplitudo.inventar.InventarApp;
import me.amplitudo.inventar.domain.ProblemReportCategory;
import me.amplitudo.inventar.domain.ProblemReport;
import me.amplitudo.inventar.repository.ProblemReportCategoryRepository;
import me.amplitudo.inventar.service.ProblemReportCategoryService;
import me.amplitudo.inventar.service.dto.ProblemReportCategoryDTO;
import me.amplitudo.inventar.service.mapper.ProblemReportCategoryMapper;
import me.amplitudo.inventar.service.dto.ProblemReportCategoryCriteria;
import me.amplitudo.inventar.service.ProblemReportCategoryQueryService;

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
 * Integration tests for the {@link ProblemReportCategoryResource} REST controller.
 */
@SpringBootTest(classes = InventarApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProblemReportCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProblemReportCategoryRepository problemReportCategoryRepository;

    @Autowired
    private ProblemReportCategoryMapper problemReportCategoryMapper;

    @Autowired
    private ProblemReportCategoryService problemReportCategoryService;

    @Autowired
    private ProblemReportCategoryQueryService problemReportCategoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProblemReportCategoryMockMvc;

    private ProblemReportCategory problemReportCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProblemReportCategory createEntity(EntityManager em) {
        ProblemReportCategory problemReportCategory = new ProblemReportCategory()
            .name(DEFAULT_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return problemReportCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProblemReportCategory createUpdatedEntity(EntityManager em) {
        ProblemReportCategory problemReportCategory = new ProblemReportCategory()
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return problemReportCategory;
    }

    @BeforeEach
    public void initTest() {
        problemReportCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProblemReportCategory() throws Exception {
        int databaseSizeBeforeCreate = problemReportCategoryRepository.findAll().size();
        // Create the ProblemReportCategory
        ProblemReportCategoryDTO problemReportCategoryDTO = problemReportCategoryMapper.toDto(problemReportCategory);
        restProblemReportCategoryMockMvc.perform(post("/api/problem-report-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProblemReportCategory in the database
        List<ProblemReportCategory> problemReportCategoryList = problemReportCategoryRepository.findAll();
        assertThat(problemReportCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProblemReportCategory testProblemReportCategory = problemReportCategoryList.get(problemReportCategoryList.size() - 1);
        assertThat(testProblemReportCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProblemReportCategory.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testProblemReportCategory.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createProblemReportCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = problemReportCategoryRepository.findAll().size();

        // Create the ProblemReportCategory with an existing ID
        problemReportCategory.setId(1L);
        ProblemReportCategoryDTO problemReportCategoryDTO = problemReportCategoryMapper.toDto(problemReportCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProblemReportCategoryMockMvc.perform(post("/api/problem-report-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProblemReportCategory in the database
        List<ProblemReportCategory> problemReportCategoryList = problemReportCategoryRepository.findAll();
        assertThat(problemReportCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = problemReportCategoryRepository.findAll().size();
        // set the field null
        problemReportCategory.setName(null);

        // Create the ProblemReportCategory, which fails.
        ProblemReportCategoryDTO problemReportCategoryDTO = problemReportCategoryMapper.toDto(problemReportCategory);


        restProblemReportCategoryMockMvc.perform(post("/api/problem-report-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProblemReportCategory> problemReportCategoryList = problemReportCategoryRepository.findAll();
        assertThat(problemReportCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategories() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList
        restProblemReportCategoryMockMvc.perform(get("/api/problem-report-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problemReportCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getProblemReportCategory() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get the problemReportCategory
        restProblemReportCategoryMockMvc.perform(get("/api/problem-report-categories/{id}", problemReportCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(problemReportCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getProblemReportCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        Long id = problemReportCategory.getId();

        defaultProblemReportCategoryShouldBeFound("id.equals=" + id);
        defaultProblemReportCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultProblemReportCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProblemReportCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultProblemReportCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProblemReportCategoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProblemReportCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where name equals to DEFAULT_NAME
        defaultProblemReportCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the problemReportCategoryList where name equals to UPDATED_NAME
        defaultProblemReportCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where name not equals to DEFAULT_NAME
        defaultProblemReportCategoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the problemReportCategoryList where name not equals to UPDATED_NAME
        defaultProblemReportCategoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProblemReportCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the problemReportCategoryList where name equals to UPDATED_NAME
        defaultProblemReportCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where name is not null
        defaultProblemReportCategoryShouldBeFound("name.specified=true");

        // Get all the problemReportCategoryList where name is null
        defaultProblemReportCategoryShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProblemReportCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where name contains DEFAULT_NAME
        defaultProblemReportCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the problemReportCategoryList where name contains UPDATED_NAME
        defaultProblemReportCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where name does not contain DEFAULT_NAME
        defaultProblemReportCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the problemReportCategoryList where name does not contain UPDATED_NAME
        defaultProblemReportCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProblemReportCategoriesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where createdAt equals to DEFAULT_CREATED_AT
        defaultProblemReportCategoryShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the problemReportCategoryList where createdAt equals to UPDATED_CREATED_AT
        defaultProblemReportCategoryShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where createdAt not equals to DEFAULT_CREATED_AT
        defaultProblemReportCategoryShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the problemReportCategoryList where createdAt not equals to UPDATED_CREATED_AT
        defaultProblemReportCategoryShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultProblemReportCategoryShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the problemReportCategoryList where createdAt equals to UPDATED_CREATED_AT
        defaultProblemReportCategoryShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where createdAt is not null
        defaultProblemReportCategoryShouldBeFound("createdAt.specified=true");

        // Get all the problemReportCategoryList where createdAt is null
        defaultProblemReportCategoryShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultProblemReportCategoryShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the problemReportCategoryList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProblemReportCategoryShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultProblemReportCategoryShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the problemReportCategoryList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultProblemReportCategoryShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultProblemReportCategoryShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the problemReportCategoryList where updatedAt equals to UPDATED_UPDATED_AT
        defaultProblemReportCategoryShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        // Get all the problemReportCategoryList where updatedAt is not null
        defaultProblemReportCategoryShouldBeFound("updatedAt.specified=true");

        // Get all the problemReportCategoryList where updatedAt is null
        defaultProblemReportCategoryShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllProblemReportCategoriesByProblemsIsEqualToSomething() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);
        ProblemReport problems = ProblemReportResourceIT.createEntity(em);
        em.persist(problems);
        em.flush();
        problemReportCategory.addProblems(problems);
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);
        Long problemsId = problems.getId();

        // Get all the problemReportCategoryList where problems equals to problemsId
        defaultProblemReportCategoryShouldBeFound("problemsId.equals=" + problemsId);

        // Get all the problemReportCategoryList where problems equals to problemsId + 1
        defaultProblemReportCategoryShouldNotBeFound("problemsId.equals=" + (problemsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProblemReportCategoryShouldBeFound(String filter) throws Exception {
        restProblemReportCategoryMockMvc.perform(get("/api/problem-report-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problemReportCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restProblemReportCategoryMockMvc.perform(get("/api/problem-report-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProblemReportCategoryShouldNotBeFound(String filter) throws Exception {
        restProblemReportCategoryMockMvc.perform(get("/api/problem-report-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProblemReportCategoryMockMvc.perform(get("/api/problem-report-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProblemReportCategory() throws Exception {
        // Get the problemReportCategory
        restProblemReportCategoryMockMvc.perform(get("/api/problem-report-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProblemReportCategory() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        int databaseSizeBeforeUpdate = problemReportCategoryRepository.findAll().size();

        // Update the problemReportCategory
        ProblemReportCategory updatedProblemReportCategory = problemReportCategoryRepository.findById(problemReportCategory.getId()).get();
        // Disconnect from session so that the updates on updatedProblemReportCategory are not directly saved in db
        em.detach(updatedProblemReportCategory);
        updatedProblemReportCategory
            .name(UPDATED_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ProblemReportCategoryDTO problemReportCategoryDTO = problemReportCategoryMapper.toDto(updatedProblemReportCategory);

        restProblemReportCategoryMockMvc.perform(put("/api/problem-report-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the ProblemReportCategory in the database
        List<ProblemReportCategory> problemReportCategoryList = problemReportCategoryRepository.findAll();
        assertThat(problemReportCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProblemReportCategory testProblemReportCategory = problemReportCategoryList.get(problemReportCategoryList.size() - 1);
        assertThat(testProblemReportCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProblemReportCategory.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testProblemReportCategory.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingProblemReportCategory() throws Exception {
        int databaseSizeBeforeUpdate = problemReportCategoryRepository.findAll().size();

        // Create the ProblemReportCategory
        ProblemReportCategoryDTO problemReportCategoryDTO = problemReportCategoryMapper.toDto(problemReportCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProblemReportCategoryMockMvc.perform(put("/api/problem-report-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(problemReportCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProblemReportCategory in the database
        List<ProblemReportCategory> problemReportCategoryList = problemReportCategoryRepository.findAll();
        assertThat(problemReportCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProblemReportCategory() throws Exception {
        // Initialize the database
        problemReportCategoryRepository.saveAndFlush(problemReportCategory);

        int databaseSizeBeforeDelete = problemReportCategoryRepository.findAll().size();

        // Delete the problemReportCategory
        restProblemReportCategoryMockMvc.perform(delete("/api/problem-report-categories/{id}", problemReportCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProblemReportCategory> problemReportCategoryList = problemReportCategoryRepository.findAll();
        assertThat(problemReportCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
